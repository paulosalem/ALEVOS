/*******************************************************************************
 * ALEVOS - ALgebraic Engine for the Verification Of Simulations
 * 
 * This software was developed by Paulo Salem da Silva for his doctoral thesis, 
 * which is entitled
 *   
 *   "Verification of Behaviourist Multi-Agent Systems by means of 
 *    Formally Guided Simulations"
 * 
 * This software, therefore, constitutes a companion to the thesis. As such, 
 * it should be seen as an experimental product, suitable for research purposes,
 * but not ready for production.
 * 
 * 
 * Copyright (c) 2008 - 2012, Paulo Salem da Silva
 * All rights reserved.
 * 
 * This software may be used, modified and distributed freely, provided that the 
 * following rules are followed:
 * 
 *   (i)   this copyright notice must be maintained in any redistribution, in both 
 *         original and modified form,  of this software;
 *   (ii)  this software must be provided free of charge, although services which 
 *         require the software may be charged;
 *   (iii) for non-commercial purposes, this software may be used, modified and 
 *         distributed free of charge;
 *   (iv)  for commercial purposes, only the original, unmodified, version of this 
 *         software may be used.
 * 
 * For other uses of the software, please contact the author.
 ******************************************************************************/
package alevos.expression.picalculus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import alevos.IllegalSemanticsException;
import alevos.expression.Definition;
import alevos.expression.Expression;
import alevos.expression.IllegalStructureException;
import alevos.process.semantics.PiUnfoldingRule;
import alevos.ts.Event;
import alevos.util.Pair;

public class PiIdentifier extends PiProcess{

  protected String name;
  
  protected List<PiName> parameters;
  
  protected PiDefinition definition;
  
  protected PiProcess proc = null;
  
  /**
   * Whether this identifier is being analyzed. This is important because the identifier
   * can be defined recursively, and as a consequence we need a way to know when
   * we have already examined it once.
   */
  protected boolean beingAnalyzed = false;
  


  public PiIdentifier(String name, PiName... parameters) {
    super();
    
    List<PiName> listParameters = new LinkedList<PiName>();
    for(PiName n: parameters){
      listParameters.add(n);
    }
    
    this.name = name;
    this.parameters = listParameters;
    
    this.addApplicableRule(new PiUnfoldingRule());
  }
  
  public PiIdentifier(String name, List<PiName> parameters) {
    super();
    this.name = name;
    this.parameters = parameters;
    
    this.addApplicableRule(new PiUnfoldingRule());
  }
  
  
  public PiIdentifier(String name, List<PiName> parameters, PiDefinition definition) {
    super();
    this.name = name;
    this.parameters = parameters;
    this.definition = definition;
    
    // Import marking
    importMarking(definition);
    
    applyDefinitionIfNecessary();
    
    this.addApplicableRule(new PiUnfoldingRule());
  }
  
  
  public void setDefinition(Definition definition){
    this.definition = (PiDefinition) definition;
    importMarking(definition);
    applyDefinitionIfNecessary();
  }
  
  
  public Definition getDefinition(){
    return definition;
  }

  public PiProcess getProc() {
    applyDefinitionIfNecessary();
    
    return proc;
  }
  
  @Override
  public int size(){
    int size = 1 + parameters.size();
    
    if(proc != null && !beingAnalyzed){
      beingAnalyzed = true;
      size = size + proc.size(); 
      beingAnalyzed = false;
    }
    
    return  size;
  }
  
  @Override
  public int cachedSuccessorsSize() {
    int size = 1 + parameters.size();
    
    if(proc != null && !beingAnalyzed){
      beingAnalyzed = true;
      size = size + proc.cachedSuccessorsSize();
      beingAnalyzed = false;
    }
    
    return cachedSuccessorsSizeAux(size);
    
  }
  
  @Override
  public Object clone() {
    
    List<PiName> newParameters = new LinkedList<PiName>();
    newParameters.addAll(parameters);
    
    PiIdentifier piId = new PiIdentifier(name, newParameters, definition);
    piId.setSuccessorsCached(copySuccessorsCache());
    
    return piId;
  }
  
  @Override
  public String toString() {
    String s = name + "(";
    
    Iterator<PiName> it = parameters.iterator();
    while(it.hasNext()){
      s = s + it.next().toString();
      
      if(it.hasNext()){
        s = s + ", ";
      }
    }
    
    s = s + ")";
    
    return s;
  }

  @Override
  public Set<PiName> boundNames() throws IllegalSemanticsException {
    
    checkDefinitionExistence();
    applyDefinitionIfNecessary();
    
    Set<PiName> names;
    
    if(!definition.isBeingUnfolded()){
      definition.setBeingUnfolded(true);
      
      // Perform an alpha conversion just to make sure that there can be no name capturing
      // in any future unfolding
      // TODO remove: ((PiProcess) definition.getExpression()).alphaConversion();
      proc.alphaConversion();
      
      // Since all bound names are fresh now, we may safely return them
      // TODO remove: names = ((PiProcess) definition.getExpression()).boundNames();
      names = proc.boundNames();
      
      definition.setBeingUnfolded(false);
    }
    
    else{
      
      // No new free names!
      names = new HashSet<PiName>();
    }
    
    return names;
  }

  
  @Override
  public Set<PiName> freeNames() {
    
    checkDefinitionExistence();
    applyDefinitionIfNecessary();
    
    Set<PiName> names;
    
    if(!definition.isBeingUnfolded()){
      definition.setBeingUnfolded(true);
      
      // In principle, all free names of the defining expression should be free ...
      // TODO remove: names = ((PiProcess) definition.getExpression()).freeNames();
      names = proc.freeNames();
      
      // ... but because of unfolding, we must consider the substitutions that would be made.
      // Thus, remove the formal parameters of the definition first.
      names.removeAll(definition.getParameters());
      
      // Then add the formal parameters of this identifier, which would be used
      // to apply the definition
      names.addAll(this.parameters);
      
      definition.setBeingUnfolded(false);
    }
    
    else{
      
      // No new free names!
      names = new HashSet<PiName>();
    }
    
    return names;
  }

  
  @Override
  public boolean substitute(Map<PiName, PiName> substitution) throws IllegalSemanticsException {
    boolean a = false; 
    boolean b = false;

    
    checkDefinitionExistence();
    applyDefinitionIfNecessary();

    
    //
    // Always substitute in the parameter list.
    //
    ListIterator<PiName> it = parameters.listIterator();
    while(it.hasNext()){
      PiName name = it.next();
      
      PiName newName = (PiName) substitution.get(name);
      
      if(newName != null){
        // Substitutes the last element returned by it.next(); 
        it.set(newName);
        a = true;
      }
      
    }
    
    //
    // Substitute in the process itself iff it is not being unfolded. This avoids an infinite
    // loop in the case of recursive definitions.
    //
    if(!definition.isBeingUnfolded()){
      definition.setBeingUnfolded(true);
      
      
      // Substitute in the process
      // TODO remove: b = ((PiProcess) definition.getExpression()).substitute(substitution);
      b = proc.substitute(substitution);
      
      // Done, we can release the identifier
      definition.setBeingUnfolded(false);
    }
    
    if(a || b){
      clearCache();
      return true;
    }
    
    return false;
    
  }

  @Override
  public void alphaConversion() throws IllegalSemanticsException {
    
    checkDefinitionExistence();
    applyDefinitionIfNecessary();
    
    // Alpha convert  iff we have not still visited this identifier
    if(!definition.isBeingUnfolded()){
      definition.setBeingUnfolded(true);
      
      // TODO remove? :  ((PiProcess) definition.getExpression()).alphaConversion();
      proc.alphaConversion(); 
      
      definition.setBeingUnfolded(false);
    }
    
  }
  
  

  public String getName() {
    return name;
  }

  public List<PiName> getParameters() {
    return parameters;
  }


  
  private void checkDefinitionExistence(){
    
    if(definition == null){
      throw new IllegalStructureException("The identifier " + toString() + " must be assigned to a definition before it can be used.");
    }
  }
  
  private void applyDefinitionIfNecessary(){
    if(!definition.isBeingUnfolded()){
      definition.setBeingUnfolded(true);
      
      if(proc == null){
        proc = (PiProcess) definition.getExpression().clone();
      }
      
      definition.setBeingUnfolded(false);
    }
  }

  
  private void importMarking(Definition def){
    this.marker = ((PiProcess)def.getExpression()).marker;
    this.incompatibleMarkers = (HashSet<String>) ((PiProcess)def.getExpression()).incompatibleMarkers.clone();
  }
  
}

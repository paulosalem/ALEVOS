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

import java.util.Map;
import java.util.Set;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
import alevos.process.semantics.PiPrefixRule;
import alevos.ts.Event;
import alevos.util.Pair;

public class PiPrefix extends PiProcess {
  
  protected PiActionPrefix prefix;
  
  protected PiProcess process;

  public PiPrefix(PiActionPrefix... prefixes) {
    this(new PiNilProcess(), prefixes);
  }
  
  
  public PiPrefix(PiProcess process, PiActionPrefix... prefixes) {
    super();
    
    PiProcess proc = process;
    
    // Add the prefixes in the reverse order, except for the first one
    for(int i = prefixes.length - 1; i > 0; i--){
      proc = new PiPrefix(prefixes[i], proc);
    }
    
    // The first prefix is the one we actually keep in this instance
    this.prefix = prefixes[0];
    this.process = proc;
    
    defineMarker(proc);
    
    this.addApplicableRule(new PiPrefixRule());
  }
  
  public PiPrefix(PiActionPrefix prefix, PiProcess process) {
    super();
    this.prefix = prefix;
    this.process = process;
    
    defineMarker(process);
    
    this.addApplicableRule(new PiPrefixRule());
  }

  public PiActionPrefix getPrefix() {
    return prefix;
  }

  public PiProcess getProcess() {
    return process;
  }

  @Override
  public int size() {
    return 1 + prefix.size() + process.size();
  }
  
  @Override
  public int cachedSuccessorsSize() {
    int size = 1 + prefix.size() + process.cachedSuccessorsSize();
    
    return cachedSuccessorsSizeAux(size);
  }
  
  @Override
  public Object clone() {
    PiPrefix obj = new PiPrefix((PiActionPrefix)prefix.clone(), (PiProcess)process.clone());
    obj.setSuccessorsCached(copySuccessorsCache());
    return obj;
  }

  @Override
  public String toString() {
    return "(" + prefix.toString() + "." + process.toString() + ")";
  }

  @Override
  public Set<PiName> boundNames() throws IllegalSemanticsException {
    Set<PiName> bound = this.process.boundNames();
    bound.addAll(prefix.boundNames());
    
    return bound;
  }

  @Override
  public Set<PiName> freeNames() {
    Set<PiName> free = this.process.freeNames();
    free.addAll(prefix.freeNames());
    free.removeAll(this.prefix.boundNames());
        
    return free;
  }

  @Override
  public boolean substitute(Map<PiName, PiName> substitution) throws IllegalSemanticsException {

    boolean a1 = false;
    boolean a2 = false;

    //
    // 1. If the prefix binds names, alpha conversion may be necessary to
    //    avoid captures. 
    //
    if(this.prefix instanceof PiInputAction){
      PiInputAction input = (PiInputAction) this.prefix;
      
      // To avoid capture
      Set<PiName> inter = PiCalculusHelper.intersection(input.parameters, substitution.values());
      
      // To avoid renaming the bound names
      inter.addAll(PiCalculusHelper.intersection(input.parameters, substitution.keySet())); 
      
      // If there are names in the intersection, they need to be alpha-converted
      if(!inter.isEmpty()){
        Map<PiName, PiName> alphaSubstitution = PiCalculusHelper.freshSubstitution(inter);
        
        // We are changing things, must notify to clear cache
        a1 = input.substituteParameters(alphaSubstitution);
        
        // We are changing things, must notify to clear cache
        a2 = this.process.substitute(alphaSubstitution);
      }
      
      

    }
    

    
    
    //
    // 2. Then the substitution can be made safely.
    //
    boolean b1 = this.prefix.substitute(substitution);
    boolean b2 = this.process.substitute(substitution);  // TODO check binding!!! 
    
    if(a1 || a2 || b1 || b2){
      clearCache();
      return true;
    }
    
    return false;
    
    
  }

  @Override
  public void alphaConversion() throws IllegalSemanticsException {
    
    if(this.prefix instanceof PiInputAction){
      PiInputAction input = (PiInputAction) this.prefix;
      
      Map<PiName, PiName> alphaSubstitution = PiCalculusHelper.freshSubstitution(input.parameters);
        
      input.substituteParameters(alphaSubstitution);
        
      this.process.substitute(alphaSubstitution);
      }
    }
    
}

  
  


  

  
  


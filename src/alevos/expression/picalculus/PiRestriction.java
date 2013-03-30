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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import alevos.IllegalSemanticsException;
import alevos.process.semantics.PiResRule;

public class PiRestriction extends PiUnaryProcess {

  protected Collection<PiName> restricted = new LinkedList<PiName>();

  public PiRestriction(PiProcess proc, PiName... restrictedNames) {
    super(proc);
    
    for(PiName name: restrictedNames){
      this.restricted.add(name);  
    }
    
    this.addApplicableRule(new PiResRule());
  }


  public PiRestriction(PiName restrictedName, PiProcess proc) {
    this(proc, restrictedName);
  }
  
  public PiRestriction(Collection<PiName> restrictedNames, PiProcess proc) {
    super(proc);
    this.restricted = restrictedNames;
    
    this.addApplicableRule(new PiResRule());
  }

  @Override
  public Object clone() {
    Set<PiName> newRestricted = new HashSet<PiName>();
    newRestricted.addAll(restricted);
    
    PiRestriction obj = new PiRestriction(newRestricted, (PiProcess) proc.clone());
    obj.setSuccessorsCached(copySuccessorsCache());
    return obj;
  }

  @Override
  public String toString() {
    String s =  "new[";
    
    Iterator<PiName> it = restricted.iterator();
    while(it.hasNext()){
      PiName n = it.next();
      s = s +  n.toString();
      
      if(it.hasNext()){
        s = s + ", ";
      }
    }
    
    s = s + "] {" + proc.toString() + "}";
    
    return s;
  }

  @Override
  public Set<PiName> boundNames() throws IllegalSemanticsException {
    Set<PiName> bound = this.proc.boundNames();
    bound.addAll(this.restricted);
    
    return bound;
  }

  @Override
  public Set<PiName> freeNames() {
    Set<PiName> free = this.proc.freeNames();
    free.removeAll(this.restricted);
        
    return free;
  }

  @Override
  public boolean substitute(Map<PiName, PiName> substitution) throws IllegalSemanticsException {
    
    boolean a1 = false;
    boolean a2 = false;
    
    //
    // 1. The restriction binds names, thus alpha conversion may be necessary to
    //    avoid captures.
    //
      
      // To avoid capture
      Set<PiName> inter = PiCalculusHelper.intersection(this.restricted, substitution.values());
      
      // To avoid renaming the bound names
      inter.addAll(PiCalculusHelper.intersection(this.restricted, substitution.keySet()));
      
      // If there are names in the intersection, they need to be alpha-converted
      if(!inter.isEmpty()){
        
        Map<PiName, PiName> alphaSubstitution = PiCalculusHelper.freshSubstitution(inter);

        a1 = substituteRestrictedNames(alphaSubstitution);
        
        // All free names in proc are possibly bound by this restriction operator, thus a simple
        // substitution will work.
        a2 = this.proc.substitute(alphaSubstitution);
      }
    

    
    //
    // 2. Then the substitution can be made safely.
    //
    boolean b = this.proc.substitute(substitution);
    
    if(a1 || a2 || b){
      clearCache();
      return true;
    }
    
    return false;
    
  }
  
  @Override
  public void alphaConversion() throws IllegalSemanticsException {
    Map<PiName, PiName> alphaSubstitution = PiCalculusHelper.freshSubstitution(restricted);
    
    substituteRestrictedNames(alphaSubstitution);
    
    // All free names in proc are possibly bound by this restriction operator, thus a simple
    // substitution will work.
    this.proc.substitute(alphaSubstitution);
  }
  
  
  protected boolean substituteRestrictedNames(Map<PiName, PiName> substitution){
    
    boolean b = false;
    
    Set<PiName> newRestricted = new HashSet<PiName>();
    
    for(PiName n: restricted){
      
      // If the name should be substituted, then we put the new name
      if(substitution.containsKey(n)){
        newRestricted.add(substitution.get(n));
        b = true;
      }
      
      // If not, then we just put the old name
      else{
        newRestricted.add(n);
      }
    }
   
    // Update the restricted names reference
    this.restricted = newRestricted;
    
    return b;
  }
  
  
  public Collection<PiName> getRestricted() {
    return restricted;
  }




}

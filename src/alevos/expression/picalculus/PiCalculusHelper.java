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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Useful auxiliary procedures for pi-calculus manipulation.
 * 
 * @author Paulo Salem
 *
 */
public class PiCalculusHelper {

  /**
   * Creates a substitution map that takes elements to fresh elements.
   * 
   * @param original The elements to be substituted.
   * @return A map with the substitution.
   */
  protected static Map<PiName, PiName> freshSubstitution(Collection<PiName> original){
    
    Map<PiName, PiName> substitution = new HashMap<PiName, PiName>();
    
    for(PiName n: original){
      substitution.put(n, new PiName());
    }
    
    return substitution;
  }
  
  /**
   * Calculates the intersection of the two collections.
   * 
   * @param names1 The first collection of names.
   * @param names2 The second collection of names.
   * 
   * @return A new set that is the intersection of the two specified ones.
   */
  public static Set<PiName> intersection(Collection<PiName> names1, Collection<PiName> names2){
    
    Set<PiName> inter = new HashSet<PiName>();
    
    for(PiName m: names1){
      for(PiName n: names2){
        if(m.equals(n)){
          inter.add(m);
        }
      }
    }
    
    return inter;
    
  }
}

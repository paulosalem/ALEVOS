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
import java.util.Map;
import java.util.Set;

import alevos.IllegalSemanticsException;

public class PiNilProcess extends PiProcess {

  @Override
  public Object clone() {
    PiNilProcess clone = new PiNilProcess();
    clone.marker = this.marker;
    clone.incompatibleMarkers = (HashSet<String>) this.incompatibleMarkers.clone();
    
    return clone;
  }

  @Override
  public String toString() {
    
    return "0";
  }
  
  @Override
  public int size() {
    return 1; 
  }
  
  @Override
  public int cachedSuccessorsSize() {
    return 0;
  }

  @Override
  public boolean substitute(Map<PiName, PiName> substitution) throws IllegalSemanticsException {
    // Nothing to be done.
    return false;
    
  }

  @Override
  public Set<PiName> boundNames() throws IllegalSemanticsException {
    
    return new HashSet<PiName>();
  }

  @Override
  public Set<PiName> freeNames() {
    
    return new HashSet<PiName>();
  }

  @Override
  public void alphaConversion() throws IllegalSemanticsException {
    // nothing
    
  }



}

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
import alevos.process.semantics.PiReplicationRule;

public class PiReplication extends PiUnaryProcess {

  
  public PiReplication(PiProcess proc) {
    super(proc);
    
    this.addApplicableRule(new PiReplicationRule());
  }

  @Override
  public Object clone() {
    PiReplication obj = new PiReplication((PiProcess)proc.clone());
    obj.setSuccessorsCached(copySuccessorsCache());
    return obj;
  }
  
  @Override
  public String toString() {
    return "!" + proc.toString();
  }

  @Override
  public boolean substitute(Map<PiName, PiName> substitution) throws IllegalSemanticsException {
    
    if(this.getProc().substitute(substitution)){
      clearCache();
      return true;
    }
    
    return false;
    
  }

  @Override
  public Set<PiName> boundNames() throws IllegalSemanticsException {
    return this.getProc().boundNames();
  }

  @Override
  public Set<PiName> freeNames() {
    return this.getProc().freeNames();
  }

  @Override
  public void alphaConversion() throws IllegalSemanticsException {
    this.proc.alphaConversion();
  }


}

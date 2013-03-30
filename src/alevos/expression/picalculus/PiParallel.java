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
import java.util.List;
import java.util.Map;
import java.util.Set;

import alevos.IllegalSemanticsException;
import alevos.process.semantics.PiComRule;
import alevos.process.semantics.PiParRule;

public class PiParallel extends PiBinaryProcess {

  public PiParallel(List<PiProcess> listProcs){
    super(listProcs);
    this.addApplicableRule(new PiParRule());
    this.addApplicableRule(new PiComRule());
  }
  
  public PiParallel(PiProcess... procs){
    super(procs);
    this.addApplicableRule(new PiParRule());
    this.addApplicableRule(new PiComRule());
  }
  
  public PiParallel(PiProcess proc1, PiProcess proc2) {
    super(proc1, proc2);
    this.addApplicableRule(new PiParRule());
    this.addApplicableRule(new PiComRule());
  }
  
  @Override
  public PiProcess build(List<PiProcess> procs){
    
    
    if(procs.size() == 0){
      return new PiNilProcess();
    }
    
    
    else if(procs.size() == 1){
      return procs.get(0);
    }
    
    // Recursive construction
    else{
      PiProcess par1 = build(firstHalfOf(procs));
      PiProcess par2 = build(secondHalfOf(procs));
      
      return new PiParallel(par1, par2);
      
      // TODO linear construction is inefficient later
      //return new PiParallel(procs.get(0), build(procs.subList(1, procs.size())));
    }
    
  }

  @Override
  public Object clone() {
    PiParallel obj = new PiParallel((PiProcess)proc1.clone(), (PiProcess)proc2.clone());
    obj.setSuccessorsCached(copySuccessorsCache());
    return obj;
  }
  
  @Override
  public String toString() {
    return "(" + proc1.toString() + " | " + proc2.toString() + ")";
  }

  @Override
  public boolean substitute(Map<PiName, PiName> substitution) throws IllegalSemanticsException {
    
    // Call the substitution in both sub processes
    boolean b1 = this.getProc1().substitute(substitution);
    boolean b2 = this.getProc2().substitute(substitution);
    
    if(b1 || b2){
      // If any of them succeed, cache must be cleared
      clearCache();
      
      
      // And we notify the caller that the substitution suceeded
      return true;
    }
    
    // Nothing was really substituted
    return false;

    
  }
  
  @Override
  public Set<PiName> boundNames() throws IllegalSemanticsException {
    Set<PiName> names = new HashSet<PiName>();
    
    names.addAll(this.getProc1().boundNames());
    names.addAll(this.getProc2().boundNames());
    
    return names;
  }

  @Override
  public Set<PiName> freeNames() {
    Set<PiName> names = new HashSet<PiName>();
    
    names.addAll(this.getProc1().freeNames());
    names.addAll(this.getProc2().freeNames());
    
    return names;
  }

  @Override
  public void alphaConversion() throws IllegalSemanticsException {
    this.proc1.alphaConversion();
    this.proc2.alphaConversion();
  }

  
  @Override
  protected void cleanUp(){
    // TODO
    if (proc1 instanceof PiParallel){
      PiParallel par1 = (PiParallel) proc1;
      if(par1.getProc1() instanceof PiNilProcess && par1.getProc2() instanceof PiNilProcess){
        proc1 = new PiNilProcess();
      }
      else if(!(par1.getProc1() instanceof PiNilProcess) && par1.getProc2() instanceof PiNilProcess){
        proc1 = par1.getProc1();
      }
      else if(par1.getProc1() instanceof PiNilProcess && !(par1.getProc2() instanceof PiNilProcess)){
        proc1 = par1.getProc2();
      }
    }
    
    if (proc2 instanceof PiParallel){
      PiParallel par2 = (PiParallel) proc2;
      if(par2.getProc1() instanceof PiNilProcess && par2.getProc2() instanceof PiNilProcess){
        proc2 = new PiNilProcess();
      }
      else if(!(par2.getProc1() instanceof PiNilProcess) && par2.getProc2() instanceof PiNilProcess){
        proc1 = par2.getProc1();
      }
      else if(par2.getProc1() instanceof PiNilProcess && !(par2.getProc2() instanceof PiNilProcess)){
        proc1 = par2.getProc2();
      }
    }
  }


}

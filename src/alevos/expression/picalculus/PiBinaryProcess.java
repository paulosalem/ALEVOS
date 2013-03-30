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
import java.util.LinkedList;
import java.util.List;

import alevos.expression.Expression;
import alevos.ts.Event;
import alevos.util.Pair;

public abstract class PiBinaryProcess extends PiProcess {
  
  protected PiProcess proc1 =  new PiNilProcess();
  
  protected PiProcess proc2 =  new PiNilProcess();
  
  protected PiBinaryProcess() {
    super();
    this.proc1 =  new PiNilProcess();
    this.proc2 = new PiNilProcess();
  }
  
  protected PiBinaryProcess(PiProcess proc) {
    super();
    this.proc1 = proc;
    this.proc2 = new PiNilProcess();
  }
  
  public PiBinaryProcess(List<PiProcess> listProcs){
    super();

    // Construct a balanced tree
    proc1 = build(firstHalfOf(listProcs));
    proc2 = build(secondHalfOf(listProcs));
    
    // TODO linear construction is inefficient later.
    /*
    // If there is at least one element, we use it as the first process
    if(listProcs.size() > 0){
      proc1 = listProcs.get(0);
    }
    
    proc2 = build(listProcs.subList(1, listProcs.size()));
    */
    
    defineMarker(proc1, proc2);
  }
  
  protected List<PiProcess> firstHalfOf(List<PiProcess> procs){
    return procs.subList(0, (int)Math.floor(((double)procs.size())/2.0));
  }
  
  protected List<PiProcess> secondHalfOf(List<PiProcess> procs){
    return procs.subList((int)Math.floor(((double)procs.size())/2.0), procs.size());
  }
  
  public PiBinaryProcess(PiProcess... procs){
    this(toList(procs));  // Put the parameters in the form of a list, to facilitate manipulations
    
    // TODO erase if changes did not break anything
    /*
    LinkedList<PiProcess> listProcs = new LinkedList<PiProcess>();
    for(int i = 0; i < procs.length; i++){
      listProcs.add(procs[i]);
    }
    
    
    
    // If there is at least one element, we use it as the first process
    if(listProcs.size() > 0){
      proc1 = listProcs.get(0);
    }
    
    proc2 = build(listProcs.subList(1, listProcs.size()));
    
    defineMarker(proc1, proc2);
    */
  }
  
  public PiBinaryProcess(PiProcess proc1, PiProcess proc2) {
    super();
    this.proc1 = proc1;
    this.proc2 = proc2;
    
    defineMarker(proc1, proc2);
  }
  
  protected static LinkedList<PiProcess> toList(PiProcess... procs){

    LinkedList<PiProcess> listProcs = new LinkedList<PiProcess>();
    for(int i = 0; i < procs.length; i++){
      listProcs.add(procs[i]);
    }
    
    return listProcs;
  }
  
  /**
   * Recursively builds a new composed process using this binary operator.
   * 
   * @param procs The processes to be composed.
   * @return A new process composed by the specified ones using this binary operator.
   */
  public abstract PiProcess build(List<PiProcess> procs);
  
  protected void defineMarker(PiProcess proc1, PiProcess proc2){
    
    // If marking is the same, then the new process can inherit it. Otherwise, it won't be marked.
    if(proc1.marker.equals(proc2.marker) &&  proc1.incompatibleMarkers.equals(proc2.incompatibleMarkers)){
      
        this.marker = proc1.marker;
        this.incompatibleMarkers = (HashSet<String>) proc1.incompatibleMarkers.clone();
      
    }
  }
  
  @Override
  public int size(){
    return 1 + proc1.size() + proc2.size();
  }
  
  @Override
  public int cachedSuccessorsSize() {
    int size = 1 + proc1.cachedSuccessorsSize() + proc2.cachedSuccessorsSize();
    
    return cachedSuccessorsSizeAux(size);
  }


  public PiProcess getProc1() {
    return proc1;
  }

  public PiProcess getProc2() {
    return proc2;
  }

  

}

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
package alevos.ts.sp;

import alevos.ts.Event;
import alevos.ts.IOEvent;
import alevos.ts.State;
import alevos.ts.StaticTransitionSystem;
import alevos.util.Pair;

public class SimulationPurpose extends StaticTransitionSystem{

  public SimulationPurpose(State initial) {
    super(initial);
    
    states.add(SuccessState.instance());
    states.add(FailureState.instance());
  }
  
  /**
   * Checks whether the specified states synchronize.
   * 
   * @param q A state from this simulation purpose.
   * @param s A state from any transition system.
   * 
   * @return <code>true</code> if the states synchronize;
   *         <code>false</code> otherwise.
   */
  public boolean synchronize(State q, State s){
    
    if(s.getLiterals().containsAll(q.getLiterals())){
      return true;
    }
    
    return false;
  }
  
  /**
   * Checks whether the specified events synchronize. The parameters are better visualized as follows:
   *  
   *   q1 ---e1--> q2   = a transition in this simulation purpose;
   *   e2               = an event in some other transition system.
   * 
   * @param q1 A state from this simulation purpose such that it originates <code>e1</code>.
   * @param q2 A state from this simulation purpose such that it is the termination of <code>e1</code>. 
   * @param e1 An event from this simulation purpose that originates in the specified state <code>q1</code>.
   * @param e2 An event from any transition system.
   * 
   * @return <code>true</code> if the events synchronize;
   *         <code>false</code> otherwise.
   */
  public boolean synchronize(State q1, State q2, IOEvent e1, IOEvent e2){
    
    if(e1.getIoType() == IOEvent.IOType.INPUT && e2.getIoType() == IOEvent.IOType.OUTPUT){
      return e1.isComplementary(e2);
    }
    else if(e1.getIoType() == IOEvent.IOType.OUTPUT && e2.getIoType() == IOEvent.IOType.INPUT){
      return e1.isComplementary(e2);
    }
    else if(e1.getIoType() != IOEvent.IOType.OTHER && e2.getIoType() == IOEvent.IOType.OTHER){
      return true;
    }
    else if(e1.getIoType() == IOEvent.IOType.OTHER){
      
      // See the paper for a justification of this case
      
      for(Pair<Event, State> es: this.succ(q1)){
        if(/*!es.getSecond().equals(q2) && */((IOEvent)es.getFirst()).isComplementary(e2)){
          return false;
        }
      }
      
      return true;
    }
    else if(e1.getIoType() == IOEvent.IOType.INTERNAL && e2.getIoType() == IOEvent.IOType.INTERNAL){
      return true;
    }
    
    
    
    return false;
  }
  
  


}

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
package alevos.ts;

import java.util.Collection;

import alevos.IllegalSemanticsException;
import alevos.simulation.SimulatorConnector;
import alevos.util.Pair;
import alevos.verification.TraceInfo;

/**
 * An abstract transition system.
 * 
 * @author Paulo Salem
 *
 */
public abstract class AnnotatedTransitionSystem {
  
  /**
   * A simulator connector may be used in order to determine if transitions
   * are possible or not according to the state of the simulation.
   * This is useful to implement contextual restrictions which cannot
   * be inferred solely from the formal transition system.
   */
  protected SimulatorConnector simulatorConnector = null;
  
  protected State initial;
  
  public AnnotatedTransitionSystem() {
    super();
  }
  
  public AnnotatedTransitionSystem(State initial) {
    super();
    this.initial = initial;
  }

  /**
   * 
   * Calculates the successor states of the specified state.
   * 
   * @param s The state whose successors are to be calculated.
   * @return A collection of pairs, in which each pair contains a successor state and an event
   *         that leads to it.
   * @throws IllegalSemanticsException 
   */
  public abstract Collection<Pair<Event, State>> succ(State s) throws IllegalSemanticsException;
  
  /**
   * 
   * Calculates the successor states of the specified state taking into account particular trace information.
   * 
   * @param s The state whose successors are to be calculated.
   * @param ti Information about the current trace being considered.
   * 
   * @return A collection of pairs, in which each pair contains a successor state and an event
   *         that leads to it.
   * @throws IllegalSemanticsException 
   */
  public abstract Collection<Pair<Event, State>> succ(State s, TraceInfo ti) throws IllegalSemanticsException;

  
  public State getInitialState() {
    return initial;
  }
  
  public TraceInfo getInitialTraceInfo(){
    
    // By default, nothing very informative....
    return new TraceInfo();
  }
  
  public TraceInfo eventScheduled(Event event, TraceInfo ti){
    // By default, nothing to be done
    return ti;
  }
  
  
  
  public void setSimulatorConnector(SimulatorConnector simulatorConnector) {
    this.simulatorConnector = simulatorConnector;
    
  }

  public String toString(){
    String s = "<";
    
    // States
    s += "states=unknown, \n";
    
    // Events
    s += "events=unknown, \n";
    
    // Literals
    s += "literals=unknown, \n";
    
    // Transitions
    s += "transitions=unknown, \n";
    
    // Labelling function
    s += "labelling function=unknown, \n";


    
    // Initial state
    s += "intial state=" + initial.toString();
    
    s += ">";
    
    return s;
  }


  
  
}

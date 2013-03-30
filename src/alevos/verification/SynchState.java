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
package alevos.verification;

import java.util.Collection;
import java.util.List;

import alevos.ts.Event;
import alevos.ts.IOEvent;
import alevos.ts.State;
import alevos.util.Pair;

/**
 * A state on the synchronous product of a simulation purpose and another ATS.
 * 
 * @author Paulo Salem
 *
 */
public class SynchState {
  
  protected State stateSP;
  
  protected State stateATS;
  
  protected IOEvent eventSP;
  
  /**
   * The event in the ATS that led to the present synchronziation.
   */
  protected IOEvent eventATS;
  
  protected State sourceStateSP;
  
  protected Object simulationState;
  
  protected TraceInfo traceInfo;
  
  protected List<Pair<Event, State>> unexplored;
  
  protected int depth;

  public SynchState(State stateSP, State stateATS, IOEvent eventSP, IOEvent eventATS,
      State sourceStateSP, Object simulationState, TraceInfo traceInfo,
      List<Pair<Event, State>> unexplored, int depth) {
    super();
    this.stateSP = stateSP;
    this.stateATS = stateATS;
    this.eventSP = eventSP;
    this.eventATS = eventATS;
    this.sourceStateSP = sourceStateSP;
    this.simulationState = simulationState;
    this.traceInfo = traceInfo;
    this.unexplored = unexplored;
    this.depth = depth;
  }

  public State getStateSP() {
    return stateSP;
  }

  public State getStateATS() {
    return stateATS;
  }

  public IOEvent getEventSP() {
    return eventSP;
  }
  
  public IOEvent getEventATS() {
    return eventATS;
  }

  public State getSourceStateSP() {
    return sourceStateSP;
  }

  public Object getSimulationState() {
    return simulationState;
  }

  public TraceInfo getTraceInfo() {
    return traceInfo;
  }

  public List<Pair<Event, State>> getUnexplored() {
    return unexplored;
  }

  public int getDepth() {
    return depth;
  }
  
  

}

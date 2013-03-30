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
package alevos.simulation;

import alevos.ts.AnnotatedTransitionSystem;
import alevos.ts.Event;

/**
 * Provides a way to access and manipulate a simulator, so that there can be
 * an interplay between the verification and the simulation.
 * Concrete subclasses should provide a way to interface with a simulator in order to
 * fulfill the several obligations of this connector.
 * 
 * @author Paulo Salem
 *
 */
public abstract class SimulatorConnector {
  
  //
  // Possible importance of messages to be transmitted back to the simulator.
  //
  public final static int TRIVIAL_MSG = 0;
  public final static int UNINPORTANT_MSG = 1;
  public final static int NORMAL_MSG = 2;
  public final static int IMPORTANT_MSG = 3;
  public final static int CRUCIAL_MSG = 4;
  
  
  /**
   * The commit event, which signals that it is time for the simulation perform a step based
   * on the scheduled events. Notice that several events may take place between commit events.
   */
  protected Event commitEvent;
  
  
  
  public SimulatorConnector(Event commitEvent) {
    super();
    this.commitEvent = commitEvent;
  }

  /**
   * Prepares the simulator for execution.
   * @throws InvalidSimulatorRequest 
   */
  public abstract void setup() throws InvalidSimulatorRequest;
  
  /**
   * Resets the simulator to its initial conditions.
   * @throws InvalidSimulatorRequest 
   */
  public abstract void reset() throws InvalidSimulatorRequest;
  

  /**
   * Instructs the simulator to schedule one simulation step using
   * the specified event. The step will only be actually performed
   * when the method <code>step()</code> is invoked.
   * 
   * @param event The event to use.
   * 
   * @throws InvalidSimulatorRequest If the specified event cannot take place. To avoid
   *                                 this exception, use the method
   *                                 <code>canStep()</code> to check whether the event
   *                                 is possible.
   */
  public abstract void scheduleStep(Event event) throws InvalidSimulatorRequest;
  
  
  /**
   * Puts the simulation back on the specified state. This state must have been
   * obtained directly from the simulator.
   * 
   * @param state A simulation state to which the simulation will be returned to.
   */
  public abstract void goToState(Object state) throws InvalidSimulatorRequest;
  
  /**
   * Instructs the simulator to proceed with the steps scheduled with the
   * <code>scheduleStep()</code> method. 
   * 
   * @throws InvalidSimulatorRequest 
   */
  public abstract void step() throws InvalidSimulatorRequest;
  

  /**
   * Retrieves the current state of the simulator. The internal structure
   * of this state is not relevant, since only the simulator itself is supposed
   * to manipulate its data. ALEVOS shall only retrieve and restore such
   * states, without knowing their content, in order to implement search
   * algorithms over the simulation traces.  
   * 
   * @return The current state of the simulation.
   */
  public abstract Object currentState();
  
  public abstract SimulatorContext getCurrentContext();
  
  
  /**
   * 
   * @return The current position in the simulation run (i.e., how many simulation steps have been 
   *         given).
   */
  public abstract long getCurrentPosition();

  public Event getCommitEvent() {
    return commitEvent;
  }

  
  public abstract void printDebugMsg(String msg, int msgImportance);
  
  public abstract void printMsg(String msg, int msgImportance);
  
  
  
  
}

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
package alevos.exploration;

import java.util.ArrayList;

import alevos.IllegalSemanticsException;
import alevos.simulation.InvalidSimulatorRequest;
import alevos.simulation.SimulatorConnector;
import alevos.ts.AnnotatedTransitionSystem;
import alevos.ts.Event;
import alevos.ts.State;
import alevos.util.Pair;
import alevos.verification.TraceInfo;

/**
 * Defines a random walk algorithm for exploration.
 * 
 * @author Paulo Salem
 *
 */
public class RandomWalkExploration extends ExplorationAlgorithm {
  
  /**
   * How many traces shall be produced.
   */
  protected int runs = 1;
  
  /**
   * How many simulation steps each trace shall have.
   */
  protected int iterationsPerRun = 100;
  
  

  public RandomWalkExploration(int runs, int iterationsPerRun) {
    super();
    this.runs = runs;
    this.iterationsPerRun = iterationsPerRun;
  }

  @Override
  public void explore(AnnotatedTransitionSystem ts, SimulatorConnector sc) throws IllegalSemanticsException, InvalidSimulatorRequest {

    // Prepare the simulator
    sc.setup();
    
    sc.printDebugMsg("Initial state = " + ts.getInitialState().toString() + "\n\n", SimulatorConnector.IMPORTANT_MSG);
    
    for(int i = runs; i > 0; i--){
      // Explore a trace
      exploreTrace(ts, sc);
      
      // Restore the simulator back to its initial conditions
      sc.reset();
    }
  }

  
  /**
   * Explores one trace of the specified transition system using the specified
   * simulator connector.
   * 
   * @param ts The transition system to be explored.
   * @param sc The simulator connector to use.
   * @throws IllegalSemanticsException 
   * @throws InvalidSimulatorRequest 
   */
  public void exploreTrace(AnnotatedTransitionSystem ts, SimulatorConnector sc) throws IllegalSemanticsException, InvalidSimulatorRequest {

    ts.setSimulatorConnector(sc);
    
    State cur = ts.getInitialState();
    
    TraceInfo ti = (TraceInfo)ts.getInitialTraceInfo().clone();
    
    sc.printMsg("Trace begin.\n\n", SimulatorConnector.IMPORTANT_MSG);
    
    for(int i = iterationsPerRun; i > 0; i--){
      
      // From all possible successors, choose the ones that can be used
      ArrayList<Pair<Event, State>> succs = new ArrayList<Pair<Event, State>>();
      for(Pair<Event, State> es: ts.succ(cur, ti)){
        succs.add(es);
      }
      
      
      if(succs.size() > 0){
        // Pick a random next transition among the available ones
        int next = (int) Math.floor(Math.random() * succs.size());
       
        // Go to it
        Pair<Event, State> trans = succs.get(next);
        sc.scheduleStep(trans.getFirst());
        
        // Inform the transition system that an event has been chosen
        TraceInfo nextTi = (TraceInfo)ti.clone();
        ts.eventScheduled(trans.getFirst(), nextTi);
        
        if(sc.getCommitEvent().equals(trans.getFirst())){
          sc.step();
        }
        
        sc.printMsg("  ---" + trans.getFirst().toString() + "--->", SimulatorConnector.IMPORTANT_MSG);
        //sc.printMsg("  " + trans.getSecond().toString()  + "\n", SimulatorConnector.IMPORTANT_MSG);

        cur = trans.getSecond();

      }
      else{
        // If there are no successors, the trace reached an (premature) end
        break;
      }
    }
    
    sc.printMsg("Trace finished.\n\n", SimulatorConnector.IMPORTANT_MSG); 
    

    
  }

}

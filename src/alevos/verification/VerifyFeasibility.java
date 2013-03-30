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
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import alevos.IllegalSemanticsException;
import alevos.simulation.InvalidSimulatorRequest;
import alevos.simulation.SimulatorConnector;
import alevos.ts.AnnotatedTransitionSystem;
import alevos.ts.Event;
import alevos.ts.IOEvent;
import alevos.ts.State;
import alevos.ts.sp.FailureState;
import alevos.ts.sp.SimulationPurpose;
import alevos.ts.sp.SuccessState;
import alevos.util.Pair;

public class VerifyFeasibility extends VerificationAlgorithm {
  
  public enum Variant {Weak, Strong}
  
  private Variant variant = Variant.Weak;
  
  public VerifyFeasibility(int maxDepth, boolean randomize,
      Integer maxSynchSteps, Variant variant) {
    super(maxDepth, randomize, maxSynchSteps);
    this.variant = variant;
  }

  @Override
  public Verdict verify(SimulationPurpose sp, AnnotatedTransitionSystem ts, SimulatorConnector sc) throws IllegalSemanticsException, InvalidSimulatorRequest {
    
    // Make the simulator available to the transition system, so that the contextual restrictions
    // may be applied
    ts.setSimulatorConnector(sc);
    
    // Preprocess the simulation purpose
    preprocess(sp, SuccessState.instance());
    
    // If we are checking strong feasibility, the Failure state must have locally the highest priority
    if(this.variant.equals(Variant.Strong)){
      FailureState.instance().setUtil1(-1);
    }
    
    int succsCounter = 0; // TODO A counter of how many environment states have been considered. For debugging...
    int synchCounter = 0; // TODO A counter of how many synchronizations happened. For debugging... 
    
    // How many steps in the synchronous transition system have been examined
    long steps = 0;
    
    // Depth in the search tree
    int depth = 0;
    
    //Stack<Sextuple<State, State, IOEvent, State, Object, TraceInfo>> synchStack = new Stack<Sextuple<State, State, IOEvent, State, Object, TraceInfo>>();
    Stack<SynchState> synchStack = new Stack<SynchState>();
    
    // Get the initial simulator state
    Object simInit = sc.currentState();                        // SIMULATION INTERFACE
    
    List<Pair<Event, State>> unexplored = new LinkedList<Pair<Event, State>>();
    unexplored.addAll(sp.succ(sp.getInitialState()));
    SynchState init = new SynchState(sp.getInitialState(), ts.getInitialState(), null, null, null, simInit, ts.getInitialTraceInfo(), unexplored, depth);
    
    synchStack.push(init);
    
    Verdict verdict = Verdict.FAILURE;
    
    while(!synchStack.empty()){
      SynchState current = synchStack.peek();
      
      State q = current.getStateSP();
      State s = current.getStateATS();
      Object currentSimState = current.getSimulationState();             // SIMULATION INTERFACE
      TraceInfo ti = current.getTraceInfo();
      unexplored = current.getUnexplored();
      depth = current.getDepth();
      
      boolean progress = false;
      
      //sc.printDebugMsg("(a) s size = " + s.getExpression().size(), SimulatorConnector.NORMAL_MSG);
      //sc.printDebugMsg("(a) s's cached successors size = " + s.getExpression().cachedSuccessorsSize(), SimulatorConnector.NORMAL_MSG);

      
      while(!unexplored.isEmpty() && progress == false && depth < maxDepth){
        Pair<Event, State> transQ = removeBest(q, unexplored);
        IOEvent f = (IOEvent) transQ.getFirst();
        State nextQ = transQ.getSecond();
        int nextDepth = depth + 1;
        
        
        succsCounter++;
        Collection<Pair<Event, State>> succs = ts.succ(s, ti);
        for(Pair<Event, State> transS: succs ){
          IOEvent g = (IOEvent) transS.getFirst();
          State nextS = transS.getSecond();
          
          // Return the simulation to the appropriate state
          sc.goToState(currentSimState);                        // SIMULATION INTERFACE
          
          sc.scheduleStep(g);                                  // SIMULATION INTERFACE
          
          // Inform the transition system that an event has been chosen
          TraceInfo nextTi = (TraceInfo)ti.clone();
          ts.eventScheduled(g, nextTi);
          
          if(sc.getCommitEvent().equals(g)){                   // SIMULATION INTERFACE
            sc.step();                                         // SIMULATION INTERFACE
          }
          
          if(canSynch(sp, q, f, nextQ, s, g, nextS, sc)){
            
            sc.printMsg("[depth = "+depth +"]" +" Events synch'ed: (" + f +",       " + g +"); " +
                "States annotations synch'ed: (" +nextQ.getLiterals() + ",       " + nextS.getLiterals() +"); "+
                " SP trans.: (" + q.getName() + " -> " + nextQ.getName() + ")", SimulatorConnector.NORMAL_MSG);// + "; " +
                //"  Environment = " + nextS.toString());

            sc.printDebugMsg("[synch. stack size = " + synchStack.size() + "] " +
                             "[synch's = "+synchCounter +"]", SimulatorConnector.NORMAL_MSG);

           
           //sc.printDebugMsg("(b) s size = " + s.getExpression().size(), SimulatorConnector.NORMAL_MSG);
           //sc.printDebugMsg("(b) s's cached successors size = " + s.getExpression().cachedSuccessorsSize(), SimulatorConnector.NORMAL_MSG);
           //sc.printDebugMsg("(b) nextS size = " + nextS.getExpression().size(), SimulatorConnector.NORMAL_MSG);
           //sc.printDebugMsg("(b) nextS's cached successors size = " + nextS.getExpression().cachedSuccessorsSize(), SimulatorConnector.NORMAL_MSG);

           
           
           //
           // If we are checking strong feasibility, it may be necessary to pop the current SynchState.
           // If it is not, we just make the appropriate synchronization and push it.
           //
           
           if(nextQ.equals(FailureState.instance()) && this.variant.equals(Variant.Strong)){
             
             // The current state is bad, so we pop it. This is enough to progress
             // to the next simulation purpose state.
             synchStack.pop();
             progress = true;
             break; // Gets out of the for loop concerning succs
           }
           else{
             
             //
             // Stack the synchronization
             //
             
             Object nextSimState = sc.currentState();         // SIMULATION INTERFACE
             
             List<Pair<Event, State>> nextUnexplored = new LinkedList<Pair<Event, State>>();
             nextUnexplored.addAll(sp.succ(nextQ));
             
             SynchState next = new SynchState(nextQ, nextS, f, g, q, nextSimState, nextTi,nextUnexplored, nextDepth);
             synchStack.push(next);
             
             
             progress = true;
          }

           //
           // Check whether we found the feasible trace
           //
            if(nextQ.equals(SuccessState.instance())){
              buildTrace(synchStack, nextQ, nextDepth); // builds the feasible trace
              return Verdict.SUCCESS;
            }
            
            // Cancel the search if there is a limit to the number of synchronizations allowed.
            synchCounter++;
            if(maxSynchSteps != null){
              if(synchCounter >= maxSynchSteps){
                sc.printMsg("WARNING: The search has been aborted because the maximum number of synchronizations allowed (" + maxSynchSteps + ") has been reached.", SimulatorConnector.IMPORTANT_MSG);
                buildTrace(synchStack, q, depth);
                return Verdict.INCONCLUSIVE;
              }
            }

          }
        }  

      }
      
      if(depth >= maxDepth){
        verdict = Verdict.INCONCLUSIVE;
        
        sc.printMsg("WARNING: Search depth limit (" + maxDepth + ") has been reached.", SimulatorConnector.IMPORTANT_MSG);
      }
      
      
      if(progress == false){
        synchStack.pop();
        
      }
      
    }
    
    return verdict;
    
  }
  
  

}

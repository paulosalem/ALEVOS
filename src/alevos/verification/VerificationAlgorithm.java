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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
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


/**
 * An abstract verification algorithm. All verification strategies must be subclasses of this class.
 * 
 * @author Paulo Salem
 *
 */
public abstract class VerificationAlgorithm {
  
  public enum Verdict {SUCCESS, FAILURE, INCONCLUSIVE}
  
  private final int INFINITY = 1000000;
  
  /**
   * The maximum depth allowed for the search tree.
   */
  protected int maxDepth = 1000;

  /**
   * Whether non-deterministic choices should be randomized.
   */
  protected boolean randomize = true;
  
  /**
   * The maximum number of synchronizations algoritms depending on synchronous products
   * should perform. The <code>null</code> value indicates that no such limit exists
   * (i.e., it is infinite).
   */
  protected Integer maxSynchSteps = null;

  /**
   * A feasible trace or counter-example trace found, if any.
   */
  protected LinkedList<SynchState> trace = new LinkedList<SynchState>();
  
  

  public VerificationAlgorithm(int maxDepth, boolean randomize,
      Integer maxSynchSteps) {
    super();
    this.maxDepth = maxDepth;
    this.randomize = randomize;
    this.maxSynchSteps = maxSynchSteps;
    
    // Sets all Expression calculation to employ randomization
    Expression.setRandomize(randomize);
  }


  /**
   * Verifies whether the specified simulation purpose is successful in the
   * specified system. Concrete algorithms must define what "successful"
   * means. 
   * 
   * @param sp The simulation purpose to guide the verification.
   * @param ts The system to be verified.
   * @param sc The connector used to control the simulator.
   * 
   * @return <tt>Success</tt> if the simulation purpose is successful;
   *         <tt>Failure</tt> if the simulation purpose is not successful;
   *         <tt>Inconclusive</tt> if it is not possible to provide a result.
   * @throws IllegalSemanticsException 
   * @throws InvalidSimulatorRequest 
   */
  public abstract Verdict verify(SimulationPurpose sp, AnnotatedTransitionSystem ts, SimulatorConnector sc) throws IllegalSemanticsException, InvalidSimulatorRequest;

  
  protected void preprocess(SimulationPurpose sp, State destination){
    
    // Preprocess states
    for(State s: sp.getStates()){
      s.setUtil1(null);  // No distance is available yet
      s.setUtil2(false); // Not visited
    }
    
    
    // First pass: annotates the acyclic paths in the graph
    preprocessAux(sp, sp.getInitialState(), destination);
    
    // States must be "unvisited", so that the SP graph can be explored again.
    // (But this time, distances calcultated on the previous pass are kept.)
    for(State s: sp.getStates()){
      s.setUtil2(false); // Not visited
    }

    
    // Second pass: annotates the cyclic paths in the graph using the information available from the first pass
    preprocessAux(sp, sp.getInitialState(), destination);
  }
  
  
  private void preprocessAux  (SimulationPurpose sp, State source, State destination){
    
    // Mark state as visited
    source.setUtil2(true);
    
    // Recursion base: we are at the desired destination (i.e., distance 0)
    if(source.equals(destination)){
      source.setUtil1(0);
    }
    else{
      
      Integer min = null; // No minimum distance found yet
      
      
      
      Collection<Pair<Event, State>> succs = sp.succ(source);
      
      // If there are no successors...
      if(succs.size() == 0){
        // ... we assign this state the a very big distance 
        // (i.e., infinite for all practical purposes)
        min = INFINITY; //Integer.MAX_VALUE; // WARNING: do not use Integer.MAX_VALUE, for this will cause an
                                             // overflow as soon as we make min + 1 below. This breaks everything!
      }
      
      // Else, examine every successor to find the closest one
      else {
        for(Pair<Event, State> next: succs){
        
          
          // If the successor has not been visited yet, we make the recursive call
          if((Boolean)next.getSecond().getUtil2() == false){
            preprocessAux(sp, next.getSecond(), destination);
          }
          
          // If the successor has the desired distance information, we use it.
          if(next.getSecond().getUtil1() != null){
            int distance = (Integer)next.getSecond().getUtil1();
            
            // If no min distance had been found, this one will do as the first
            if(min == null){
              min = distance;
            }
            
            // Else, we need to compare to see which is the minimum
            else if(distance < min){
                min = distance;
              
            }
          }
        }
      }
      
      // If we have found a minimum distance, we now increment it to account for the current transition
      if(min != null){
        min = min + 1;
      }
      
      
      // Assign the best distance found. Notice that this may still be null, since
      // it is possible that none of the successors may have a calculable distance.
      source.setUtil1(min);
    }
    
  }


  
  /**
   * Calculates the trace found after the verification, if any. The meaning of such trace depends on the 
   * particular algorithm. For instance, it could be an example of feasible trace, or a counter-example
   * to some property.
   * 
   * The trace is given in the form of an ordered list of <code>SynchState</code>s, from which
   * client classes may compute other traces (e.g., a trace in the simulation purpose). 
   * 
   * This method should be called after <code>verify</code> in order to return useful data.
   * Otherwise, an empty list is returned (i.e., nothing is found before verification).
   * 
   * @return A list of ordered <code>SynchState</code>.
   */
  public List<SynchState> traceFound() {
    return trace;
  }


  /**
   * 
   * q --f--> nextQ
   * s --g--> nextS
   * 
   * @param q
   * @param f
   * @param nextQ
   * @param s
   * @param g
   * @param nextS
   * @return
   */
  protected boolean canSynch(SimulationPurpose sp, State q, IOEvent f,
      State nextQ, State s, IOEvent g, State nextS, SimulatorConnector sc) {
    
       // Enrich the ATS with context
       nextS.setContext(sc.getCurrentContext());
    
       if(sp.synchronize(nextQ, nextS) && sp.synchronize(q, nextQ, f, g)){
         return true;
       }
      
       return false;
      }


  protected void buildTrace(Stack<SynchState> synchStack, State qTarget, int depthTarget) {
    trace = new LinkedList<SynchState>();
    
    while(!synchStack.empty()){
      SynchState cur = synchStack.pop();
      if(qTarget.equals(cur.getStateSP()) && depthTarget == cur.getDepth()){
        trace.addFirst(cur);
        
        qTarget = cur.getSourceStateSP();
        depthTarget = depthTarget - 1;
      }
    }
  }


  protected Collection<Pair<Event, State>> calculateUnexplored(SimulationPurpose sp, State q, Collection<Pair<Event, State>> unexplored) {
    
    // If no unexplored collection is defined, we do it now
    if(unexplored == null){
      unexplored = new LinkedList<Pair<Event, State>>();
      unexplored.addAll(sp.succ(q));
    }
    
    // If everything has already been explored, it is time to try again all of the successors
    if(unexplored.isEmpty()){
      unexplored.addAll(sp.succ(q));
    }
    
    
    return unexplored;
  }


  protected Pair<Event, State> removeBest(State current, List<Pair<Event, State>> unexplored) {
    return removeBestStrategy4(current, unexplored);
  }


  /**
   * A very naive strategy: remove the first pair.
   * 
   */
  protected Pair<Event, State> removeBestStrategy1(State current, Collection<Pair<Event, State>> unexplored) {
    Pair<Event, State> best = unexplored.iterator().next();
    
    unexplored.remove(best);
    
    return best;    
  }


  protected Pair<Event, State> removeBestStrategy2(State current, Collection<Pair<Event, State>> unexplored) {
    // In principle, we get the first pair
    Pair<Event, State> best = unexplored.iterator().next();
    
    // But we want a pair that is not a loop such as    s --- e ---> s
    if (best.getSecond().equals(current)){
      
      // So we look for one, just in case there is one
      for(Pair<Event, State> es: unexplored){
        
        // If it is not a loop, it is good enough
        if(!es.getSecond().equals(current)){
          best = es;
          break;
        }
      }
    }
    
    unexplored.remove(best);
    return best;
  }


  protected Pair<Event, State> removeBestStrategy3(State current, Collection<Pair<Event, State>> unexplored) {
    
    // Assign anyone as the temporary best
    Pair<Event, State> best = unexplored.iterator().next();
    
    // Now seek the one with the minimum distance calculated at the preprocessing of the simulation purpose
    for(Pair<Event, State> candidate: unexplored){
      if((Integer)candidate.getSecond().getUtil1() < (Integer)best.getSecond().getUtil1()){
        best = candidate;
      }
    }
    
    unexplored.remove(best);
    return best;
  }


  protected Pair<Event, State> removeBestStrategy4(State current, List<Pair<Event, State>> unexplored) {
    
    // Assign anyone as the temporary best
    Pair<Event, State> best = unexplored.iterator().next();
    
    // If required, shuffle the unexplored, so that we avoid always returning the same element (and being stuck somewhere)
    if(randomize){
       Collections.shuffle(unexplored);
    }
    
    
    // Now seek the one with the minimum distance calculated at the preprocessing of the simulation purpose
    for(Pair<Event, State> candidate: unexplored){
      if((Integer)candidate.getSecond().getUtil1() < (Integer)best.getSecond().getUtil1()){
        
        best = candidate;
      }
      
      // If the current best is an OTHER event and we find something different, we discard this OTHER
      /*else if((Integer)candidate.getSecond().getUtil1() == (Integer)best.getSecond().getUtil1() &&
              ((IOEvent)best.getFirst()).getIoType() == IOEvent.IOType.OTHER &&
              ((IOEvent)candidate.getFirst()).getIoType() != IOEvent.IOType.OTHER){
        
        
        best = candidate;
      }*/
    }
    
    unexplored.remove(best);
    return best;
  }
  
  
  

}

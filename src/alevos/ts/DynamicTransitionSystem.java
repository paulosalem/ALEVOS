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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
import alevos.util.Pair;
import alevos.verification.TraceInfo;

/**
 * A transition system whose states and transitions can be built on-the-fly. This class
 * can be extended by ALEVOS' clients in order to implement domain specific transition
 * systems.
 * 
 * @author Paulo Salem
 *
 */
public class DynamicTransitionSystem extends AnnotatedTransitionSystem {
  
  public DynamicTransitionSystem() {
    super();
  }
  
  public DynamicTransitionSystem(State initial) {
    super(initial);
  }

  @Override
  public Collection<Pair<Event, State>> succ(State s) throws IllegalSemanticsException{
    return succ(s, new TraceInfo());
  }
  
  @Override
  public Collection<Pair<Event, State>> succ(State s, TraceInfo ti) throws IllegalSemanticsException{

    List<Pair<Event, State>> ess = new LinkedList<Pair<Event, State>>();
    
    // If a simulator connector is available, we may enrich the state with contextual information
    if(simulatorConnector != null){
      s.setContext(simulatorConnector.getCurrentContext());
    }
    
    // Add static successors, if any
    ess.addAll(s.getStaticSuccessors()); 
    
    // Add dynamic successors based on the algebraic expression
    for(Pair<Event, Expression> ee: s.getExpression().succ()){
      ess.add(new Pair<Event, State>(ee.getFirst(), buildState(ee.getSecond())));
    }
    
    
    // Translate to another format if necessary
    Collection<Pair<Event, State>> translated = translate(ess);
    
    
    
    // Apply any restrictions and return
    return restrict(s, translated, ti);
  }
  
  protected Collection<Pair<Event, State>> restrict(State source, Collection<Pair<Event, State>> originalSuccs, TraceInfo ti)  throws IllegalSemanticsException{
    
    // By default, nothing is forbidden
    return originalSuccs;
  }
  
  
  // TODO is this necessary? I think we may remove.
  /**
   * Builds a state with the given expression. Subclasses may implement different ways
   * to do so (e.g., by using custom <code>State</code> subclasses).
   * 
   * @param exp The expression represented by the state.
   * 
   * @return A new state.
   * @throws IllegalSemanticsException 
   */
  protected State buildState(Expression exp) throws IllegalSemanticsException{
    return new State(exp);
  }
  
  /**
   * Allows the translation of a collection of transitions into another. This can be useful
   * if subclasses must add their own semantics by means of custom events and states.
   * By default, no modification is made to the specified collection of transitions.
   * 
   * @param original The original collection of transitions.
   * @return A possibly modified collection of transitions.
   * @throws IllegalSemanticsException
   */
  protected Collection<Pair<Event, State>> translate(Collection<Pair<Event, State>> original)  throws IllegalSemanticsException{
    
    // By default, no modification is made
    return original;
  }
  
  @Override
  public String toString(){
    return this.initial.toString();    
  }
}

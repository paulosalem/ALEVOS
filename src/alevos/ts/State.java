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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import alevos.expression.Expression;
import alevos.expression.Nil;
import alevos.simulation.SimulatorContext;
import alevos.util.Pair;

public class State {
  
  /**
   * A user friendly name.
   */
  protected String name = "";
  
  /**
   * The expression represented by this state.
   */
  protected Expression expression;
  
  /**
   * Simulator-specific contextual information that enriches the semantics of the state.
   */
  protected SimulatorContext context = null;

  /**
   * Each pair corresponds to a known static transition.
   */
  protected Collection<Pair<Event, State>> staticSuccessors = new LinkedList<Pair<Event, State>>();
  
  /**
   * Literals associated with this state.
   */
  protected Set<Literal> literals = new HashSet<Literal>();
  
  /**
   * A general utility field. May be useful for implementing particular graph algorithms
   * over transition systems.
   */
  protected Object util1 = null;
  
  /**
   * A general utility field. May be useful for implementing particular graph algorithms
   * over transition systems.
   */
  protected Object util2 = null;
  
  /**
   * A general utility field. May be useful for implementing particular graph algorithms
   * over transition systems.
   */
  protected Object util3 = null;
  
  
  
  
  public State(Expression expression, SimulatorContext context) {
    super();
    this.expression = expression;
    
    setContext(context);
  }


  public State(Expression expression) {
    super();
    this.expression = expression;
  }
  

  public State() {
    super();
    this.expression = new Nil();
  }


  

  public Expression getExpression() {
    return expression;
  }
  
  
  
  public SimulatorContext getContext() {
    return context;
  }


  public void setContext(SimulatorContext context) {
    this.context = context;
  }


  public String toString(){
    return "<" + expression.toString() + ">";
  }

  public Collection<Pair<Event, State>> getStaticSuccessors() {
    LinkedList<Pair<Event, State>> succs = new LinkedList<Pair<Event, State>>();
    succs.addAll(staticSuccessors);
    return succs;
  }
  
  public void addStaticSuccessor(Event e, State s){
    staticSuccessors.add(new Pair<Event, State>(e, s));
  }
  
  public Set<Literal> getLiterals() {
    Set<Literal> allLiterals = new HashSet<Literal>();
    
    allLiterals.addAll(literals);
    
    if(context != null){
      allLiterals.addAll(context.getLiterals());
    }
    
    return allLiterals;
  }

  public void addLiteral(Literal l){
    literals.add(l);
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public Object getUtil1() {
    return util1;
  }


  public void setUtil1(Object util1) {
    this.util1 = util1;
  }


  public Object getUtil2() {
    return util2;
  }


  public void setUtil2(Object util2) {
    this.util2 = util2;
  }


  public Object getUtil3() {
    return util3;
  }


  public void setUtil3(Object util3) {
    this.util3 = util3;
  }
  
  
  
  // TODO implement equals() and hashCode()!!
  
}

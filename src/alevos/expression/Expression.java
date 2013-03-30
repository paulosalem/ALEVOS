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
package alevos.expression;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import alevos.IllegalSemanticsException;
import alevos.process.semantics.Rule;
import alevos.ts.Event;
import alevos.util.Pair;

/**
 * An abstract algebraic process.
 * 
 * @author Paulo Salem
 *
 */
public abstract class Expression implements Cloneable{

  /**
   * The list of operational semantic rules that can be applied to this expression.
   * Note that this is a (ordered) list so that determinism can be assured.
   */
  protected List<Rule> applicableRules = new LinkedList<Rule>();
  
  /**
   * Whether the successors of this expression have already been calculated and cached locally.
   */
  protected boolean successorsCached = false;
  
  /**
   * When the successors are calculated for the first time, they are stored here.
   * This avoids the costly need to recalculate (the same) successors later.
   */
  protected Collection<Pair<Event, Expression>> successorsCache = null;
  

  /**
   * Whether non-deterministic choices should be randomized. This is a static configuraton and thus
   * affects the behavior of all Expressions.
   */
  private static boolean randomize = true;
  
  public void addApplicableRule(Rule rule){
    applicableRules.add(rule);
  }
  
  
  
  public List<Rule> getApplicableRules() {
    return applicableRules;
  }

  /**
   * Calculates all the successors of this expression.
   * 
   * @return A collection of pairs, in which each pair contains a successor expression
   *         and an event that leads to it.
   * @throws IllegalSemanticsException 
   */
  public Collection<Pair<Event, Expression>> succ() throws IllegalSemanticsException{
    
    List<Pair<Event, Expression>> ees;
    
    // If the successors have already been calculated and cached, we return them
    if(successorsCached){
      ees = (List<Pair<Event, Expression>>)successorsCache;
    }
    
    // Otherwise, we calculate them
    else{
      ees = new LinkedList<Pair<Event, Expression>>();
      
      for(Rule r: this.applicableRules){
        ees.addAll(r.succ(this));
      }
      
      // Ensures that every successor has an equal chance of being picked
      if(randomize){
        Collections.shuffle(ees);
      }
      
      // Store in the cache
      successorsCache = ees;
      successorsCached = true;
      
      // Clean up whatever useless elements are left
      cleanUp();
    }
    
    return ees;
  }
  
  protected void clearCache(){
    successorsCached = false;
    successorsCache = null;
  }
  
  /**
   * Disposes of useless elements in the expression that might accumulate over time.
   */
  protected abstract void cleanUp();
  
  // TODO remove??
/*
  public Set<Event> enabled(){
    
    Set<Event> es = new HashSet<Event>();
    
    for(Rule r: this.applicableRules){
      es.addAll(r.enabled(this));
    }
    
    return es;
  }
  
  public Set<Expression> emit(Event e){
    
    Set<Expression> exps = new HashSet<Expression>();
    
    for(Rule r: this.applicableRules){
      exps.addAll(r.emit(e, this));
    }
    
    return exps;
    
  }
*/
  

  public static void setRandomize(boolean randomize) {
    Expression.randomize = randomize;
  }
  
  /**
   * Calculates how many sub-components the expression has. This includes
   * both sub-expressions as well as primitive elements.
   * 
   * @return the quantity of sub-components of the expression.
   */
  public abstract int size();
  
  /**
   * Calculates the combined sizes of the cached successors, 
   * recursively, the cached successor's cached successors size.
   *  
   * @return the combined sizes of cached successors.
   */
  public abstract int cachedSuccessorsSize();
  
  protected int cachedSuccessorsSizeAux(int initialSize){
    int size = initialSize;
    
    if(successorsCache != null){
      
      //System.out.println(">>>>>Found a cache!"); // TODO debug!! remove it
      
      for(Pair<Event, Expression> ee: successorsCache){
        size = size + ee.getSecond().cachedSuccessorsSize();
      }
    }
    
    return size;
  }
  
  public abstract Object clone();
  
  public Collection<Pair<Event, Expression>> copySuccessorsCache(){
    List<Pair<Event, Expression>> ees = null;
    
    if(successorsCache != null){
      ees = new LinkedList<Pair<Event, Expression>>();
      
      for(Pair<Event, Expression> ee: successorsCache){
        ees.add(new Pair<Event, Expression>(ee.getFirst(), (Expression) ee.getSecond()));
      }
    }
    
    
    return ees;
    
  }
  
  protected void setSuccessorsCached(Collection<Pair<Event, Expression>> succs){
    if(succs != null){
      this.successorsCache = succs;
      this.successorsCached = true;
    }
    else{
      this.successorsCache = null;
      this.successorsCached = false;
    }
  }
  
  public abstract String toString();
  
  // TODO implement equals()
  
}

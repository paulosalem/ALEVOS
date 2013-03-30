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
package alevos.process.semantics;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
import alevos.expression.picalculus.PiChoice;
import alevos.expression.picalculus.PiProcess;
import alevos.ts.Event;
import alevos.util.Pair;

public class PiSumRule extends Rule {


  @Override
  public Collection<Pair<Event, Expression>> succ(Expression exp) throws IllegalSemanticsException {
    
    List<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    
    if(exp instanceof PiChoice){
      PiChoice chc = (PiChoice) exp;
      
      PiProcess p1 = chc.getProc1();
      PiProcess p2 = chc.getProc2();
      

      // All successors from the first process
      ees.addAll(p1.succ());
      
      // All successors from the second process
      ees.addAll(p2.succ());
      
      return ees;
      
    }
    else{
      throw new IllegalArgumentException("This rule is not defined for the specified expression.");
    }
  }

  
  
  
  // TODO erase ???
  /*
  @Override
  public Set<Event> enabled(Expression exp) {
    
    Set<Event> es = new HashSet<Event>();
    
    if(exp instanceof PiChoice){
      PiChoice chc = (PiChoice) exp;
      
      PiProcess p1 = chc.getProc1();
      PiProcess p2 = chc.getProc2();
      

      // All events from the first process 
      for(Rule r: p1.getApplicableRules()){
        es.addAll(r.enabled(p1));
      }
      
      // All events from the second process
      for(Rule r: p2.getApplicableRules()){
        es.addAll(r.enabled(p2));
      }
      
    }
    
    return es;
  }

  @Override
  public Set<Expression> emit(Event e, Expression exp) {
    
    Set<Expression> exps = new HashSet<Expression>();
    
    if(exp instanceof PiChoice){
      PiChoice chc = (PiChoice) exp;
      
      PiProcess p1 = chc.getProc1();
      PiProcess p2 = chc.getProc2();
      

      // All events from the first process 
      for(Rule r: p1.getApplicableRules()){
        exps.addAll(r.emit(e, p1));
      }
      
      // All events from the second process
      for(Rule r: p2.getApplicableRules()){
        exps.addAll(r.emit(e, p2));
      }
      
    }
    
    return exps;
  }
*/


}

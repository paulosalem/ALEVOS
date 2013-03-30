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
import java.util.LinkedList;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
import alevos.expression.picalculus.PiActionPrefix;
import alevos.expression.picalculus.PiParallel;
import alevos.expression.picalculus.PiProcess;
import alevos.expression.picalculus.PiReplication;
import alevos.ts.Event;
import alevos.ts.PiEvent;
import alevos.util.Pair;

/**
 * Implements an operational semantic rule to account for the replication operator.
 * Notice that this operator is usually defined with the structural congruence relation,
 * but for implementation it is convenient to have an explicit and exclusive 
 * semantic rule for it.
 * 
 * @author Paulo Salem
 *
 */
public class PiReplicationRule extends Rule {

  @Override
  public Collection<Pair<Event, Expression>> succ(Expression exp) throws IllegalSemanticsException {
    
    Collection<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    
    
    if(exp instanceof PiReplication){
      PiReplication rep = (PiReplication) exp;
      
      // Recall that
      //
      //   !P = P | !P    and  if P --a--> Q, then     P | !P   --a-->   Q | !P
      //
      // We shall get a clone of P, create a new parallel, and get the events that the cloned P
      // can perform.
      
      PiProcess newProc = (PiProcess) rep.getProc().clone(); // P
      
      // TODO make sure this change is ok
      //for(Rule r: newProc.getApplicableRules()){
        //for(Pair<Event, Expression> ee: r.succ(newProc)){
        for(Pair<Event, Expression> ee: newProc.succ()){
          PiParallel par = new PiParallel((PiProcess) ee.getSecond(), (PiProcess) rep.clone());  // Q | !P
          
          ees.add(new Pair<Event, Expression>(ee.getFirst(), par)); // --a-->   Q | !P
        }
      //}
     
      return ees;
    }
    else{
      throw new IllegalArgumentException("This rule is not defined for the specified expression.");
    }
    
  }

}

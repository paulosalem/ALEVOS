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
import alevos.expression.picalculus.PiName;
import alevos.expression.picalculus.PiProcess;
import alevos.expression.picalculus.PiRestriction;
import alevos.ts.Event;
import alevos.ts.PiEvent;
import alevos.util.Pair;

public class PiResRule extends Rule {

  @Override
  public Collection<Pair<Event, Expression>> succ(Expression exp) throws IllegalSemanticsException {
    Collection<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    
    if(exp instanceof PiRestriction){
      PiRestriction res = (PiRestriction)exp;
      
      for(Pair<Event, Expression> ee: res.getProc().succ()){
        
        // If the event does not contain restricted names...
        if(!contains((PiEvent)ee.getFirst(), res.getRestricted())){
          // ... it might take place on the restricted environment
          ees.add(new Pair<Event, Expression>(ee.getFirst(), new PiRestriction(res.getRestricted(), (PiProcess) ee.getSecond().clone())));
          
        }
      }
     
      
      return ees;
      
    }
    else{
      throw new IllegalArgumentException("This rule is not defined for the specified expression.");
    }
  }

  /**
   * Checks whether the specified event contains some of the specified names.
   * 
   * @param e The event that may contain some names.
   * @param names The names that might belong to the event.
   * @return <code>true</code> if at least one name is contained in the event;
   *         <code>false</code> otherwise.
   */
  protected boolean contains(PiEvent e, Collection<PiName> names){
    
    for(PiName n: names){
      if(e.getPrefix().contains(n)){
        return true;
      }
    }
    
    return false;
  }




}

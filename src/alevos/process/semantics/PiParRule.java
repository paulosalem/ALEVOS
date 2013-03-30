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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
import alevos.expression.picalculus.PiCalculusHelper;
import alevos.expression.picalculus.PiName;
import alevos.expression.picalculus.PiParallel;
import alevos.expression.picalculus.PiProcess;
import alevos.ts.Event;
import alevos.ts.PiEvent;
import alevos.util.Pair;

public class PiParRule extends Rule {

  @Override
  public Collection<Pair<Event, Expression>> succ(Expression exp) throws IllegalSemanticsException {
    
    Collection<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    
    if(exp instanceof PiParallel){
      PiParallel par = (PiParallel) exp;
      
      PiProcess p1 = par.getProc1();
      PiProcess p2 = par.getProc2();
      

      // Successors from the first process
      ees.addAll(calculate(p1, p2));
      
      // Successors from the second process
      ees.addAll(calculate(p2, p1));
      
      return ees;
      
    }
    else{
      throw new IllegalArgumentException("This rule is not defined for the specified expression.");
    }
  }
  
  protected  Collection<Pair<Event, Expression>> calculate(PiProcess p1, PiProcess p2) throws IllegalSemanticsException{
    Collection<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    
    // TODO make sure this change works
    //for(Rule r: p1.getApplicableRules()){
      //for(Pair<Event, Expression> ee: r.succ(p1)){
      for(Pair<Event, Expression> ee: p1.succ()){
        PiEvent pe = (PiEvent) ee.getFirst();
        PiProcess pp = (PiProcess) ee.getSecond();
        
        // Check (and enforce, if needed) the side-condition of the PAR rule
        Set<PiName> inter = PiCalculusHelper.intersection(pe.getPrefix().boundNames(), p2.freeNames()); 
        if(!inter.isEmpty()){
          // A renaming of bound names is necessary to avoid capture later
          p1.alphaConversion();
        }
        
        ees.add(new Pair<Event, Expression>(pe, new PiParallel(pp, p2)));
        
      }
    //}
    
    return ees;
  }
  
  
  // TODO remove ??
  /*
  @Override
  public Set<Event> enabled(Expression exp) {
    Set<Event> es = new HashSet<Event>();
    
    if(exp instanceof PiParallel){
      PiParallel par = (PiParallel) exp;
      
      PiProcess p1 = par.getProc1();
      PiProcess p2 = par.getProc2();
      

      // Events from the first process 
      for(Rule r: p1.getApplicableRules()){
        for(Event e: r.enabled(p1)){
          PiEvent pe = (PiEvent) e;
          
          if(PiCalculusHelper.intersection(pe.getPrefix().boundNames(), p2.freeNames()).isEmpty()){
            es.add(e);
          }
          
        }
      }
      
      // Events from the second process
      for(Rule r: p2.getApplicableRules()){
        for(Event e: r.enabled(p2)){
          PiEvent pe = (PiEvent) e;
          
          if(PiCalculusHelper.intersection(pe.getPrefix().boundNames(), p1.freeNames()).isEmpty()){
            es.add(e);
          }
          
        }
      }
      
    }
    
    return es;
  }



  // TODO not right?? Must check the rule's precondition here as well...
 
  @Override
  public Set<Expression> emit(Event e, Expression exp) {
    Set<Expression> exps = new HashSet<Expression>();
    
    if(exp instanceof PiParallel){
      PiParallel par = (PiParallel) exp;
      
      PiProcess p1 = par.getProc1();
      PiProcess p2 = par.getProc2();
      

      // All events from the first process 
      for(Rule r: p1.getApplicableRules()){
        for(Expression exp1: r.emit(e, p1)){
          PiParallel newPar = new PiParallel((PiProcess)exp1, p2);
          exps.add(newPar);
        }
      }
      
      // All events from the second process
      for(Rule r: p2.getApplicableRules()){
        for(Expression exp2: r.emit(e, p2)){
          PiParallel newPar = new PiParallel(p1, (PiProcess)exp2);
          exps.add(newPar);
        }
      }
      
    }
    
    return exps;
  }
*/
}

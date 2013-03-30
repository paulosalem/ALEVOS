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
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
import alevos.expression.picalculus.PiActionPrefix;
import alevos.expression.picalculus.PiName;
import alevos.expression.picalculus.PiOutputAction;
import alevos.expression.picalculus.PiParallel;
import alevos.expression.picalculus.PiParametrizedActionPrefix;
import alevos.expression.picalculus.PiProcess;
import alevos.expression.picalculus.PiTauAction;
import alevos.ts.Event;
import alevos.ts.PiEvent;
import alevos.util.Pair;

public class PiComRule extends Rule {

  @Override
  public Collection<Pair<Event, Expression>> succ(Expression exp) throws IllegalSemanticsException {
    Collection<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    if(exp instanceof PiParallel){
      PiParallel par = (PiParallel) exp;
      
      PiProcess p1 = par.getProc1();
      PiProcess p2 = par.getProc2();
      
      // The first process receives the message
      ees.addAll(calculate(p1, p2));
    
      //  The second process receives the message
      ees.addAll(calculate(p2, p1));
    
      return ees;
    }
    else{
      throw new IllegalArgumentException("This rule is not defined for the specified expression.");
    }
  }
  
  
  protected  Collection<Pair<Event, Expression>> calculate(PiProcess p1, PiProcess p2) throws IllegalSemanticsException{
    
    Collection<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    
  
    // TODO OPTIMIZATION: If the processes have incompatible markers, we assume that there is no need
    //               to apply the COM rule.
    /*if(!p1.isCompatibleWith(p2)){
      return ees;
    }*/
    
    // TODO maybe there is a more efficient manner to do this... as it is, the time is
    //      quadratic on the number of events of each process.
    
    for(Pair<Event, Expression> i: p2.succ()){
      
      // Get the channel name
      PiActionPrefix preI = ((PiEvent)i.getFirst()).getPrefix();
      
      // We only care if it is an output
      if(preI instanceof PiOutputAction){
        
        // Now search for the corresponding input, if any
        for(Pair<Event, Expression> j: p1.succ()){
          PiActionPrefix preJ = ((PiEvent)j.getFirst()).getPrefix();
          
          // If they are complementary, the transition is allowed
          if(preJ.complementary(preI)){
            PiProcess pi = (PiProcess) i.getSecond();
            PiProcess pj = (PiProcess) j.getSecond().clone(); // TODO .clone() ??
            
            PiParametrizedActionPrefix preJP = (PiParametrizedActionPrefix) preJ;
            PiParametrizedActionPrefix preIP = (PiParametrizedActionPrefix) preI;
            
            // Apply the substitution, to account for the message being received
            pj.substitute(substitution(preJP.getParameters(), preIP.getParameters()));
            
            // The new event is a tau, and we also set one of the actions as its cause
            ees.add(new Pair<Event, Expression>(new PiEvent(new PiTauAction(), preJ), new PiParallel(pj, pi)));
            
          }
          
        }
        
      }
    }
       
    return ees;
  }


  protected Map<PiName, PiName> substitution(List<PiName> originalNames, List<PiName> newNames){
    Map<PiName, PiName> substitution = new HashMap<PiName, PiName>();
    
    ListIterator<PiName> it1 = originalNames.listIterator();
    ListIterator<PiName> it2 = newNames.listIterator();
    
    while(it1.hasNext()){
      PiName n1 = it1.next();
      
      if(it2.hasNext()){
        PiName n2 = it2.next();
        
        substitution.put(n1, n2);      
      }
      else{
        throw new IllegalArgumentException("There must be an equal number of input and output parameters.");
      }
    }
    
    return substitution;
  }



}

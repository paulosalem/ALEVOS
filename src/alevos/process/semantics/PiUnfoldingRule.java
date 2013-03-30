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
import alevos.expression.picalculus.PiDefinition;
import alevos.expression.picalculus.PiIdentifier;
import alevos.expression.picalculus.PiName;
import alevos.expression.picalculus.PiProcess;
import alevos.ts.Event;
import alevos.util.Pair;

public class PiUnfoldingRule extends Rule {

  @Override
  public Collection<Pair<Event, Expression>> succ(Expression exp) throws IllegalSemanticsException {
    
    Collection<Pair<Event, Expression>> ees = new LinkedList<Pair<Event, Expression>>();
    
    if(exp instanceof PiIdentifier){
      PiIdentifier pid = (PiIdentifier) exp;
      

      
      // Unfold the identifier by using its definition
      // TODO remove: PiProcess proc = (PiProcess) pid.getDefinition().getExpression().clone();
      PiProcess proc = (PiProcess) pid.getProc().clone();
      
      // Determine the appropriate parameter substitution to the application
      Map<PiName, PiName> substitution = calculateSubstitution(
                                    ((PiDefinition)pid.getDefinition()).getParameters(), 
                                    pid.getParameters());
      
      // Apply the substitution
      proc.substitute(substitution);
      
      // Calculate the appropriate successors based on this definition
      ees.addAll(proc.succ());

      return ees;
      
    }
    else{
      throw new IllegalArgumentException("This rule is not defined for the specified expression.");
    }
  }

  public Map<PiName, PiName> calculateSubstitution(List<PiName> originalParameters, List<PiName> newParameters){
    
    Map<PiName, PiName> substitution = new HashMap<PiName, PiName>();
    
    ListIterator<PiName> itOrig = originalParameters.listIterator();
    ListIterator<PiName> itNew = newParameters.listIterator();
    
    while(itOrig.hasNext()){
      PiName originalP = itOrig.next();
      PiName newP = itNew.next(); // We assume both lists have the same length
      
      substitution.put(originalP, newP);
    }
    
    return substitution;
  }
}

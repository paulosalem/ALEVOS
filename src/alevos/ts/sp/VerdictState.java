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
package alevos.ts.sp;

import alevos.ts.Event;
import alevos.ts.Literal;
import alevos.ts.State;

/**
 * An abstract verdict for verifications. Subclasses must be implemented using the singleton pattern,
 * since verdicts should contain no particular data.
 * 
 * @author Paulo Salem
 *
 */
public abstract class VerdictState extends State {
  
  public abstract String toString();
  
  @Override
  public void addStaticSuccessor(Event e, State s){
    // Does nothing, since a verdict is always terminal
  }
  
  @Override
  public void addLiteral(Literal p){
    // Does nothing, since a verdict contains no further information
  }

}

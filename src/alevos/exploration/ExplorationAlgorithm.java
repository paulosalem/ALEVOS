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
package alevos.exploration;

import alevos.IllegalSemanticsException;
import alevos.simulation.InvalidSimulatorRequest;
import alevos.simulation.SimulatorConnector;
import alevos.ts.AnnotatedTransitionSystem;

/**
 * Exploration algorithms aim merely at visiting states and monitoring what happens, without
 * asserting or enforcing any particular goal. That is to say, they are not to be used
 * for verification. Possible uses of such exploration include the collection of
 * statistics for later analysis and the validation of simulation models.
 * 
 * @author Paulo Salem
 *
 */
public abstract class ExplorationAlgorithm {
  
  /**
   * Starts the exploration of the specified transition system using the specified
   * simulator connector.
   * 
   * @param ts The transition system to be explored.
   * @param sc The simulator connector to use.
   * @throws IllegalSemanticsException 
   * @throws InvalidSimulatorRequest 
   */
  public abstract void explore(AnnotatedTransitionSystem ts, SimulatorConnector sc) throws IllegalSemanticsException, InvalidSimulatorRequest;
  

}

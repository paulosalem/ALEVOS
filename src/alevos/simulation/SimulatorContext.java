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
package alevos.simulation;

import java.util.Set;

import alevos.ts.Literal;

/**
 * Each simulation state might provide contextual information useful for exploration and verification
 * which is not available directly on the formal transition systems.
 * This class must be extended in order to implement contexts appropriate to the
 * simulation platform.
 * 
 * 
 * @author Paulo Salem
 *
 */
public abstract class SimulatorContext {

  // TODO
  /**
   * Retrieves the literals that are true in this context.
   * 
   * @return A collection of literals.
   */
  public abstract Set<Literal> getLiterals();
}

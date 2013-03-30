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
 * Explores the whole state-space up to the specified depth.
 * 
 * @author Paulo Salem
 *
 */
public class FullExploration extends ExplorationAlgorithm {
  
  // TODO integrate this explorarion in the system
  
  
  
  
  protected int runs = 1;
  
  private int maxDepth = 2;
  
  public FullExploration(int runs, int maxDepth) {
    this.runs = runs;
    this.maxDepth = maxDepth;
  }

  @Override
  public void explore(AnnotatedTransitionSystem ts, SimulatorConnector sc)
      throws IllegalSemanticsException, InvalidSimulatorRequest {
    
    // Prepare the simulator
    sc.setup();
    
    sc.printDebugMsg("Initial state = " + ts.getInitialState().toString() + "\n\n", SimulatorConnector.IMPORTANT_MSG);

    for(int i = 0; i < runs; i++){
      
      
      
      // TODO
      
      
      
      
      
    }
    

  }

}

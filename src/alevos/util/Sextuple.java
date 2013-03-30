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
package alevos.util;

import java.io.Serializable;

public class Sextuple<T1, T2, T3, T4, T5, T6> extends Quintuple<T1, T2, T3, T4, T5> implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private T6 sixth;

  
  
  
  public Sextuple(T1 first, T2 second, T3 third, T4 fourth, T5 fifth, T6 sixth) {
    super(first, second, third, fourth, fifth);
    this.sixth = sixth;
  }




  public T6 getSixth() {
    return sixth;
  }
  
  
}

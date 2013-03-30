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

public class Pair<T1, T2> implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private T1 first;
  
  private T2 second;

  public Pair(T1 first, T2 second) {
    super();
    this.first = first;
    this.second = second;
  }

  public T1 getFirst() {
    return first;
  }

  public T2 getSecond() {
    return second;
  }
  
  public String toString(){
    return "(" + first.toString() + ", " + second.toString() + ")";
  }
  
  @Override
  public boolean equals(Object obj){
    if(obj instanceof Pair){
      Pair p = (Pair) obj;
      
      if(p.getFirst().equals(this.getFirst()) && p.getSecond().equals(this.getSecond())){
        return true;
      }
    }
    
    return false;
  }
  
  public int hashCode(){
    return this.getFirst().hashCode() + this.getSecond().hashCode();
  }
}

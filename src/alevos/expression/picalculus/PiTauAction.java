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
package alevos.expression.picalculus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PiTauAction extends PiActionPrefix {

  protected static PiName tau = new PiName("TAU");
 
  public PiTauAction(){
    super(tau);
  }
  
  @Override
  public int size() {
    return 1;
  }
  
  
  @Override
  public Object clone() {
    return new PiTauAction();
  }



  @Override
  public String toString() {
    return tau.toString();
  }



  @Override
  public boolean complementary(PiActionPrefix action) {
    // The TAU action has no complement.
    return false;
  }

  @Override
  public Set<PiName> boundNames() {
    
    return new HashSet<PiName>();
  }

  @Override
  public Set<PiName> freeNames() {
    
    return new HashSet<PiName>();
  }


  @Override
  public boolean substitute(Map<PiName, PiName> substitution) {
    // Does nothing.
    return false;
    
  }


  @Override
  public boolean contains(PiName name) {
    // Tau does not contain any name
    return false;
  }


  @Override
  public boolean equals(Object obj) {
    
    if(obj instanceof PiTauAction){
      PiTauAction pta = (PiTauAction) obj;
      
      if(pta.getChannel().equals(this.channel)){
        return true;
      }
    }
    
    return false;
  }


  @Override
  public int hashCode() {
    return channel.hashCode();
  }
}

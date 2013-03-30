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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PiInputAction extends PiParametrizedActionPrefix {
  
  public PiInputAction(PiName channel, PiName... parameters) {
    super(channel, parameters);
  }
  
  public PiInputAction(PiName channel, List<PiName> parameters) {
    super(channel, parameters);
  }
  
  public PiInputAction(PiName channel, PiName parameter) {
    super(channel, parameter);
  }
  


  @Override
  public Object clone() {
    
    List<PiName> newParameters = new LinkedList<PiName>();
    newParameters.addAll(parameters);
    
    return  new PiInputAction(channel, newParameters);
  }

  @Override
  public String toString() {
    String s = "?" + channel.toString() + "(";
    
    Iterator<PiName> it = parameters.iterator();
    while(it.hasNext()){
      PiName n = it.next();
      s = s +  n.toString();
      
      if(it.hasNext()){
        s = s + ", ";
      }
    }
    
    s = s + ")";
    
    return s;
  }
  
  @Override
  public boolean complementary(PiActionPrefix action) {

    // Must have the same channel name and be an output to be complementary 
    // to this input action
    if(action.getChannel().equals(this.getChannel()) &&
        action instanceof PiOutputAction){
      
      return true;
    }
    else{
      return false;
    }

  }

  @Override
  public Set<PiName> boundNames() {
    
    Set<PiName> bound = new HashSet<PiName>();
    bound.addAll(parameters);
    
    return bound;
  }

  @Override
  public Set<PiName> freeNames() {
    
    Set<PiName> free = new HashSet<PiName>();
    
    free.add(this.channel);
    
    return free;
  }

  @Override
  public boolean substitute(Map<PiName, PiName> substitution) {
    
    // Since input action binds its parameters, only the channel name should be
    // substituted
    return this.substituteChannel(substitution);
    
  }


  
  
  

}

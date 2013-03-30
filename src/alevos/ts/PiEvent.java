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
package alevos.ts;

import alevos.expression.picalculus.PiActionPrefix;
import alevos.expression.picalculus.PiInputAction;
import alevos.expression.picalculus.PiOutputAction;

public class PiEvent extends IOEvent {

  /**
   * The action prefix which constitutes this event.
   */
  protected PiActionPrefix prefix;
  
  /**
   * The action that caused this event, if any. For instance, for a tau action to take place,
   * it is necessary that some other two complementary actions react.  
   */
  protected PiActionPrefix cause;


  public PiEvent(PiActionPrefix prefix) {
    super(prefix.getChannel().getIdentifier(), ioType(prefix));
    this.prefix = prefix;
  }
  
  public PiEvent(PiActionPrefix prefix, PiActionPrefix cause) {
    super(prefix.getChannel().getIdentifier(), ioType(prefix));
    this.prefix = prefix;
    this.cause = cause;
  }

  
  private static IOType ioType(PiActionPrefix prefix){
    IOType type;
    
    if(prefix instanceof PiInputAction){
      type = IOType.INPUT;
    }
    else if (prefix instanceof PiOutputAction){
      type = IOType.OUTPUT;
    }
    else{
      type = IOType.OTHER;
    }
    
    return type;
  }
  
  public PiActionPrefix getPrefix() {
    return prefix;
  }
  
  public PiActionPrefix getCause() {
    return cause;
  }

  @Override
  public String toString() {
    
    return prefix.toString();
  }

  @Override
  public boolean equals(Object obj) {
    
    if(obj instanceof PiEvent){
      PiEvent pie = (PiEvent) obj;
      
      if(pie.getPrefix().equals(this.prefix)){
        return true;
      }
    }
    
    return false;
  }

  @Override
  public int hashCode() {
    return prefix.hashCode();
  }
  
  
  
  
}

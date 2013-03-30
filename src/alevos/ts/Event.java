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


/**
 * Represents an event in a transition system. Subclasses may add special capabilities to the events.
 * In particular, ALEVOS' clients may extend <code>Event</code> or its subclasses in order to
 * implement domain specific transition systems. In this way, the transition system
 * may be enriched without loss to the basic ALEVOS mechanisms. 
 * 
 * @author Paulo Salem
 *
 */
public class Event {
  
  /**
   * A name that identifies the event.
   */
  private String name;
  
  public Event(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString(){
    return name;
  }

  @Override
  public boolean equals(Object obj){
    if(obj instanceof Event){
      Event evt = (Event) obj;
      if(this.name.equals(evt.getName())){
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public int hashCode(){
    return name.hashCode();
  }
  
}

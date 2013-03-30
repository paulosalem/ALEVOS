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
 * An event that can be classified as input, output or none.
 * 
 * @author Paulo Salem
 *
 */
public class IOEvent extends Event {
  
  public enum IOType {INPUT, OUTPUT, OTHER, INTERNAL}
  
  private IOType ioType;

  public IOEvent(String name, IOType ioType) {
    super(name);
    this.ioType = ioType;
  }

  public IOType getIoType() {
    return ioType;
  }
  
  public boolean isComplementary(IOEvent e){
    
    if(this.getName().equals(e.getName())){
      
      if(this.getIoType() == IOType.INPUT && e.getIoType() == IOType.OUTPUT){
        return true;
      }
      else if(this.getIoType() == IOType.OUTPUT && e.getIoType() == IOType.INPUT){
        return true;
      }
      else if(this.getIoType() == IOType.OTHER && e.getIoType() == IOType.OTHER){
        return true;
      }
      
    }
    
    return false;
  }
  
  
  
  public boolean isInput(){
    if(ioType.equals(IOType.INPUT)){
      return true;
    }
    
    return false;
  }
  
  public boolean isOutput(){
    if(ioType.equals(IOType.OUTPUT)){
      return true;
    }
    
    return false;
  }
  
  public boolean isOther(){
    if(ioType.equals(IOType.OTHER)){
      return true;
    }
    
    return false;
  }
  
  @Override
  public boolean equals(Object obj){
    if(obj instanceof IOEvent){
      IOEvent evt = (IOEvent) obj;
      if(super.equals(evt) && this.ioType.equals(evt.getIoType())){
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public int hashCode(){
    return super.hashCode() + ioType.hashCode();
  }
  


}

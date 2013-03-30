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

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public abstract class PiParametrizedActionPrefix extends PiActionPrefix {

  protected List<PiName> parameters;
  
  public PiParametrizedActionPrefix(PiName channel, PiName... names) {
    super(channel);
    
    this.parameters = new LinkedList<PiName>();
    for(int i = 0; i < names.length; i++){
      this.parameters.add(names[i]);
    }
    
  }
  
  public PiParametrizedActionPrefix(PiName channel, List<PiName> parameters) {
    super(channel);
    this.parameters = parameters;
  }
  
  public PiParametrizedActionPrefix(PiName channel, PiName parameter) {
    super(channel);
    
    this.parameters = new LinkedList<PiName>();
    this.parameters.add(parameter);
  }


  public boolean substituteParameters(Map<PiName, PiName> substitution){
    
    boolean b = false;
    
    ListIterator<PiName> it = parameters.listIterator();
    while(it.hasNext()){
      PiName name = it.next();
      
      PiName newName = (PiName) substitution.get(name);
      
      if(newName != null){
        // Substitutes the last element returned by it.next(); 
        it.set(newName);
        b = true; 
      }
      
    }
    
    return b;
  }

  public List<PiName> getParameters() {
    return parameters;
  }
  
  public boolean contains(PiName name){
    
    // Searches on the channel name and on the parameters
    if(this.getChannel().equals(name) ||
        parameters.contains(name)){
      return true;
    }
    
    return false;
        
  }
  
  @Override
  public int size() {
     return 1 + parameters.size();
  }

  public abstract Object clone();
  
  @Override
  public boolean equals(Object obj) {
    
    if(obj instanceof PiParametrizedActionPrefix){
      PiParametrizedActionPrefix ppap = (PiParametrizedActionPrefix) obj;
      
      if(ppap.getChannel().equals(this.channel) &&
          ppap.getParameters().equals(this.parameters)){
        return true;
      }
    }
    
    return false;
  }


  @Override
  public int hashCode() {
    return channel.hashCode() + parameters.hashCode();
  }

  

}

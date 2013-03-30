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
package alevos.expression;

import java.util.HashMap;
import java.util.Map;

import alevos.expression.picalculus.PiName;

/**
 * Elements are atomic entities used as the fundamental building blocks of expressions.
 * 
 * @author Paulo Salem
 *
 */
public abstract class Element {

  
  protected static final String FRESH_PREFIX = "FRESH";  
  protected static long freshCounter = 0;
  
  protected String identifier;
  
  /**
   * Elements may have several "decorators" associated with them. This allows the addition
   * of accessory information to the elements which may be later used to compute its semantics.
   */
  protected Map<String, Object> decorators = new HashMap<String, Object>();

  /**
   * Creates a name with the specified identifier.
   * 
   * @param identifier The name's identifier.
   */
  public Element(String identifier) {
    super();
    
    if(identifier.startsWith(FRESH_PREFIX)){
      throw new IllegalArgumentException("The name's identifier cannot start with " + FRESH_PREFIX);
    }
    
    this.identifier = identifier;
  }
  
  
  /**
   * Creates a fresh name. This means that the created name has a unique identifier
   * (chosen automatically).
   */
  public Element(){
    super();
    
    this.identifier = FRESH_PREFIX + freshCounter;
    Element.freshCounter++;
  }

  public Object getDecorator(String decoratorName){
    return decorators.get(decoratorName);
  }
  
  public void setDecorator(String decoratorName, Object decorator){
    decorators.put(decoratorName, decorator);
  }

  @Override
  public String toString() {
    
    String s = identifier;
    
    for(Object o: decorators.values()){
      s = s + "_" + o.toString();
    }
    
    return s;
  }

  /**
   * Two elements are considered equal in one of the following cases:
   * 
   *   (i) both have decorators, both identifiers are equal, and all decorators are equal;
   *   (ii) one of the elements does not have decorators, but their identifiers are equal.
   * 
   * Notice that this implies that a decoratorless element is more "general" than its
   * decorated counterpart. By specifying a decoratorless element, one is specifying all
   * possible elements with that identifier and any decorator.
   */
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Element){
      Element element = (Element) obj;
      
      if(element.getIdentifier().equals(this.getIdentifier())){
        
        // Equal if they have the same identifier ...
        
        if(this.decorators.size() > 0 && element.decorators.size() > 0){
          // Decorators are only taken in account if they exist in both
          // elements. Otherwise, we ignore decorators. 
          // This means that a decoratorless element is more general,
          // since it is equal to any other decorator with the same identifier.
          
          for(String key: decorators.keySet()){
            if(!decorators.get(key).equals(element.getDecorator(key))){
              return false; // Different decorators -> different names!
            }
          }
        }
        
        // ... and if their decorators exist and are all equal as well
        
        return true;
      }
    }
    
    return false;
  }
  
  public int hashCode(){
    
    // We do not include the hash of the decorators because that would
    // violate the hashCode() contrat w.r.t. our implementation of equals()
    
    int h = this.getIdentifier().hashCode();
    
    return h;
    
  }


  public String getIdentifier() {
    return identifier;
  }
  

}

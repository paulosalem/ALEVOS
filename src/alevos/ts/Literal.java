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

public class Literal {
  
  public enum Type {POSITIVE, NEGATIVE};
  
  /**
   * The proposition that concerns this literal.
   */
  protected String proposition;
  
  /**
   * Whether it is a negative literal or not.
   */
  protected Type type = Type.NEGATIVE;
  
  public Literal(String name) {
    super();
    this.proposition = name;
    this.type = Type.NEGATIVE; // By default, it is a positive literal
  }
  

  public Literal(String proposition, Type type) {
    super();
    this.proposition = proposition;
    this.type = type;
  }


  public String getName() {
    return proposition;
  }

  
  


  public Type getType() {
    return type;
  }


  @Override
  public String toString(){
    
    String s = "";
    if(type == Type.NEGATIVE){
      s += "not ";
    }
    
    s += proposition;
    
    return s;
  }
  
  @Override
  public boolean equals(Object obj){
    if(obj instanceof Literal){
      Literal p = (Literal) obj;
      
      if(this.proposition.equals(p.getName()) && this.getType().equals(p.getType())){
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public int hashCode(){
    return proposition.hashCode() + type.hashCode();
  }
  
  

}

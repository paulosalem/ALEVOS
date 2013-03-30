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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import alevos.expression.Definition;
import alevos.expression.Expression;

public class PiDefinition extends Definition {

  protected List<PiName> parameters = new LinkedList<PiName>();
  
  public PiDefinition(String name, List<PiName> parameters, Expression expression) {
    super(name, expression);
    this.parameters = parameters;
  }
  
  

  
  public List<PiName> getParameters() {
    return parameters;
  }




  @Override
  public String toString() {
    String s = name + "(";
    
    Iterator<PiName> it = parameters.iterator();
    while(it.hasNext()){
      s = s + it.next().toString();
      
      if(it.hasNext()){
        s = s + ", ";
      }
    }
    
    s = s + ") = " + this.expression.toString();
    
    return s;
  }
  
}

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import alevos.IllegalSemanticsException;
import alevos.expression.Expression;
import alevos.ts.Event;
import alevos.ts.PiEvent;
import alevos.util.Pair;

public abstract class PiProcess extends Expression {
  
  /**
   * A marker that will be used later to identify this process as being of a certain kind.
   * The default marker is the empty string.
   */
  protected String marker = "";
  
  /**
   * A collection of markers that designate processes that do not communicate with this one.
   */
  protected HashSet<String> incompatibleMarkers = new HashSet<String>();
  
  
  public PiProcess(){
    
    // TODO Remove? this was causing stack overflow... structural eqs have now been transfered
    // to the rules of the several operators
    // this.addApplicableRule(new PiStructRule());
  }
  
  /**
   * Calculates the bound names of the process.
   * 
   * @return A set with the bound names.
   * @throws IllegalSemanticsException TODO
   */
  public abstract Set<PiName> boundNames() throws IllegalSemanticsException;
  
  /**
   * Calculates the free names of the process.
   * 
   * @return A set with the free names.
   */
  public abstract Set<PiName> freeNames();
  
  /**
   * Uses the specified map to substitute names in the process. Concrete subclasses
   * must also clear the cache so that the new names may be taken into account.
   * 
   * @param substitution A map from original names to the ones that should substitute them.
   * @throws IllegalSemanticsException TODO
   */
  public abstract boolean substitute(Map<PiName, PiName> substitution) throws IllegalSemanticsException;
  
  public boolean substitute(PiName originalName, PiName newName) throws IllegalSemanticsException{
    Map<PiName, PiName> sub = new HashMap<PiName, PiName>();
    sub.put(originalName, newName);
    
    return this.substitute(sub);
  }
  
  public abstract void alphaConversion() throws IllegalSemanticsException;
  
  @Override
  protected void cleanUp(){
    // Nothing to do here, only in subclasses
  }
  
  protected void updateCache(Map<PiName, PiName> substitution) throws IllegalSemanticsException{
    
    // TODO No cloning needed here to avoid capturing??
    
    if(successorsCached){
      
      for(Pair<Event, Expression> ee: successorsCache){
        PiEvent piEvent = (PiEvent) ee.getFirst();
        PiProcess piProc = (PiProcess) ee.getSecond();
        
        // Update event
        piEvent.getPrefix().substitute(substitution);
        
        // Update process
        piProc.substitute(substitution);
        
      }
      
    }
    
  }

  protected void defineMarker(PiProcess proc){
    this.marker = proc.marker;
    this.incompatibleMarkers = (HashSet<String>) proc.incompatibleMarkers.clone();
  }
  
  public String getMarker() {
    return marker;
  }

  public void setMarker(String marker) {
    this.marker = marker;
  }

  public HashSet<String> getIncompatibleMarkers() {
    return incompatibleMarkers;
  }

  public void setIncompatibleMarkers(HashSet<String> incompatibleMarkers) {
    this.incompatibleMarkers = incompatibleMarkers;
  }


  public void addIncompatibleMarker(String incompatibleMarker) {
    this.incompatibleMarkers.add(incompatibleMarker);
  }
  
  /**
   * Checks whether the specified process is compatible with this
   * process w.r.t. their markers.
   * 
   * @param proc The process to be compared with this one.
   * @return <code>true</code> if their markers are compatible; <code>false</code> otherwise.
   */
  public boolean isCompatibleWith(PiProcess proc){
    
    if(proc.incompatibleMarkers.contains(this.marker) ||
       this.incompatibleMarkers.contains(proc.marker)){
      
      return false;
    }
    
    return true;
  }

}

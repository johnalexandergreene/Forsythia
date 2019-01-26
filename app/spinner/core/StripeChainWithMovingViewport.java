package org.fleen.forsythia.app.spinner.core;

import java.util.LinkedList;

/*
 * A chain of rectangular stripes
 * chained together forming a ribbon
 * stripes are added and removed
 * we define a viewport upon this thing
 * we move ribbon with an incremented offset, move it past the viewport. 
 *   The viewport incrementally views its arvitrary length.
 * remove stripes when they move out of visibility
 * add stripes when the south edge of the ribbon gets too close 
 *   to the south edge of the viewport.
 *   
 */
public class StripeChainWithMovingViewport{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public StripeChainWithMovingViewport(StripeGenerator stripegenerator,int viewportwidth,int viewportheight){
    this.stripegenerator=stripegenerator;
    this.viewportwidth=viewportwidth;
    this.viewportheight=viewportheight;}
  
  /*
   * ################################
   * STRIPE GENERATOR
   * ################################
   */
  
  StripeGenerator stripegenerator;
  
  /*
   * ################################
   * STRIPES
   * ################################
   */
  
  public LinkedList<Stripe> stripes=new LinkedList<Stripe>();
  
  public void createLastStripe(){
    stripes.addLast(stripegenerator.getStripe());}
  
  public void deleteFirstStripe(){
    stripes.removeFirst();}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  int 
    viewportwidth,
    viewportheight,
    chainoffset=0,//we move the chain in the y- direction, northward
    visibilitymargin=10;//the distance above the top of the viewport beyond which the stripe is considered to be invisible
      //we can specify it or we can use a convervative value or a smart value ???
  
  public int getChainHeight(){
    int a=0;
    for(Stripe s:stripes)
      a+=s.getHeight();
    return a;}
  
  public void incrementViewport(){
    chainoffset--;
    if(viewportIsAboutToRunOffTheEndOfTheChain())
      createLastStripe();
    if(firstStripeIsBeyondVisibility())
      deleteFirstStripe();}

  public boolean viewportIsAboutToRunOffTheEndOfTheChain(){
    Stripe a=stripes.getLast();
    int southedge=a.getY()+a.getHeight();
    boolean isabout=southedge-viewportheight<visibilitymargin;
    return isabout;}
  
  public boolean firstStripeIsBeyondVisibility(){
    Stripe a=stripes.getFirst();
    return a.getY()+chainoffset+visibilitymargin<0;}
  
  /*
   * ################################
   * INITIALIZE
   * ################################
   */
  
  public void initialize(Stripe header){
    if(header!=null)
      stripes.add(header);
    while(getChainHeight()<viewportheight+visibilitymargin)
      createLastStripe();}
    
}
  
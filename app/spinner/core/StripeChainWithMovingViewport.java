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
  
  public StripeChainWithMovingViewport(Spinner spinner){
    this.spinner=spinner;}
  
  /*
   * ################################
   * SPINNER
   * ################################
   */
  
  Spinner spinner;
  
  /*
   * ################################
   * STRIPES
   * ################################
   */
  
  public LinkedList<Stripe> stripes=new LinkedList<Stripe>();
  
  public void createLastStripe(){
    stripes.addLast(spinner.stripegenerator.getStripe());
    System.out.println("++++++CREATE STRIPE");}
  
  public void deleteFirstStripe(){
    Stripe s=stripes.removeFirst();
    chainoffset+=s.getHeight();
    System.out.println(")))))DELETE STRIPE");}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public int 
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
    int southedgeofribbon=getStripeTop(a)+a.getHeight();
    boolean isabout=southedgeofribbon-spinner.viewportheight<visibilitymargin;
    return isabout;}
  
  public boolean firstStripeIsBeyondVisibility(){
    Stripe a=stripes.getFirst();
    return getStripeTop(a)+a.getHeight()+visibilitymargin<0;}
  
  public int getStripeTop(Stripe stripe){
    int a=0;
    SEEK:for(Stripe s:stripes){
      if(s==stripe)break SEEK;
      a+=s.getHeight();}
    a+=chainoffset;
    return a;}
  
  /*
   * ################################
   * INITIALIZE
   * ################################
   */
  
  public void initialize(Stripe header){
    if(header!=null)
      stripes.add(header);
    while(getChainHeight()<spinner.viewportheight+visibilitymargin)
      createLastStripe();}
    
}
  
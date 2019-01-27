package org.fleen.forsythia.app.spinner.core;

import java.awt.Color;
import java.util.Random;

public class Stripe_ColoredBox implements Stripe{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Stripe_ColoredBox(StripeChainWithMovingViewport chain,int height){
    this.chain=chain;
    this.height=height;
    initColor();}
  
  /*
   * ################################
   * CHAIN
   * ################################
   */
  
  public StripeChainWithMovingViewport chain;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  int height;
  
  public int getHeight(){
    return height;}
  
  public int getTop(){
    return chain.getStripeTop(this);}
  
  /*
   * ################################
   * COLOR
   * ################################
   */
  
  private Color color;
  
  public Color getColor(){
    return color;}
  
  private void initColor(){
    Random r=new Random();
    color=new Color(r.nextInt(256),r.nextInt(256),r.nextInt(256));}

}

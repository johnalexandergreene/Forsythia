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
  
  StripeChainWithMovingViewport chain;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  int 
    birthday,
    height;
  
  public int getY(){
    int h=0;
    ADDHEIGHTS:for(Stripe s:chain.stripes){
      if(s==this)break ADDHEIGHTS;
      h+=s.getHeight();
    }
    return inity+(chain.chainoffset-birthday);}

  public int getHeight(){
    return height;}
  
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

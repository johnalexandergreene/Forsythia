package org.fleen.forsythia.app.spinner.core;

import java.awt.image.BufferedImage;
import java.io.File;

/*
 * It's a seamless looping video generator
 * A ribbon of forsythia, flowing by.
 * Probably starts with an optional arbitrary "credits" image
 * Maybe some video effects. 
 *   Maybe a bit of noise. 
 *   Maybe blinking leaf-hexagons or something. 
 *   Maybe the occasional sweeping translucent stripe.
 * It works by chaining together images. Moving a viewport over the images. Creating and discarding as necessary.
 * Probably do it with a long skinny rectangle of uniform size, chopped up arbitrarily.
 * Or mayber different rectangles tho that might be complex.
 * -------------
 * Forget the seamless. 
 *   It makes things overly complicated. 
 *   And video players generally don't do seamless. 
 *   And video processing generally messes up the transition.
 * get the header (optional)
 * Then start creating stripe-images with the stripe generator
 * increment 1 pixel per tick
 * 
 */
public class Spinner{
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  public Spinner(
    int viewportwidth,int viewportheight,
    int flowdir,
    int roughlength,//we stop on a stripe border so this is only approximate
    BufferedImage headerstripe,//it's up to the user to orient this properly. it may be null
    StripeGenerator stripegenerator,
    File workingdir,
    SpinnerObserver observer){
    this.viewportwidth=viewportwidth;
    this.viewportheight=viewportheight;
    this.roughlength=roughlength;
    this.flowdir=flowdir;
    this.headerstripe=headerstripe;
    this.stripegenerator=stripegenerator;
    this.workingdir=workingdir;
    this.observer=observer;}
  
  /*
   * ################################
   * METRICS
   * ################################
   */
  
  public static final int 
    FLOWDIR_NORTH=0,
    FLOWDIR_EAST=1,
    FLOWDIR_SOUTH=2,
    FLOWDIR_WEST=3;
  
  public int 
    viewportwidth,viewportheight,
    flowdir,
    roughlength,
    length=0;
  
  /*
   * ################################
   * HEADER STRIPE
   * ################################
   */
  
  BufferedImage headerstripe;
  
  /*
   * ################################
   * STRIPE GENERATOR
   * ################################
   */
  
  StripeGenerator stripegenerator;
  
  /*
   * ################################
   * WORKING DIR
   * ################################
   */
  
  File workingdir;
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  SpinnerObserver observer;
  
  /*
   * ################################
   * MAIN PROCESS
   * ################################
   */
  
  public void run(){
    while(length<roughlength){
      
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  

}

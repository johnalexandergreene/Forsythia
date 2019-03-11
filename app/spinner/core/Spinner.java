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
    Stripe headerstripe,//for credits or whatever
    StripeGenerator stripegenerator,
    Renderer renderer,
    File workingdir,
    SpinnerObserver observer){
    this.viewportwidth=viewportwidth;
    this.viewportheight=viewportheight;
    this.roughlength=roughlength;
    this.flowdir=flowdir;
    this.headerstripe=headerstripe;
    this.stripegenerator=stripegenerator;
    stripegenerator.setSpinner(this);
    this.renderer=renderer;
    this.workingdir=workingdir;
    this.observer=observer;}
  
  /*
   * ################################
   * METRICS
   * ################################
   */
  
  /*
   * Our default geometry is a viewport that moves in the y+ direction down the ribbon
   *   That is to say, the stripes scroll upward
   * The other 3 geometries are achieved via a transform.
   */
  public static final int 
    FLOWDIR_NORTH=0,
    FLOWDIR_EAST=1,
    FLOWDIR_SOUTH=2,
    FLOWDIR_WEST=3;
  
  public int 
    viewportwidth,viewportheight,
    flowdir,
    roughlength;
  
  /*
   * ################################
   * HEADER STRIPE
   * ################################
   */
  
  Stripe headerstripe;
  
  /*
   * ################################
   * STRIPE GENERATOR
   * ################################
   */
  
  StripeGenerator stripegenerator;
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  SpinnerObserver observer;
  
  /*
   * ################################
   * MAIN PROCESS
   * The composition scrolls upward,
   * or rather, the viewport moves southward
   * 1 pixel per frame
   * ################################
   */
  
  public StripeChainWithMovingViewport chain=new StripeChainWithMovingViewport(this);
  int frameindex=-1;//total length of the image ribbon so far
  
  public void run(){
    while(frameindex<roughlength){
      //
      try{
        Thread.sleep(10);
      }catch(Exception x){}
      //
      frameindex++;
      if(frameindex==0){
        chain.initialize(headerstripe);
      }else{
        renderFrame();
        chain.incrementViewport();}}}
    
  /*
   * ################################
   * FRAME
   * ################################
   */
    
  Renderer renderer;
  public BufferedImage frame=null;
  
  void renderFrame(){
    frame=renderer.getFrame(this);
    export();
    observer.createdFrame(frame);}
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  VideoExporter0 videoexporter=new VideoExporter0();
  File workingdir;
  
  private void export(){
    if(workingdir!=null)
      videoexporter.export(frame,frameindex,workingdir);}
  
  
}

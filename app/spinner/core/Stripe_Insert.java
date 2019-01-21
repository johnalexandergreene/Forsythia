package org.fleen.forsythia.app.spinner.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.Stripe;
import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.StripeChain;

/*
 * load an appropriately sized and oriented image from a PNG file and use it for a stripe
 */
public class Stripe_Insert implements Stripe{
  
  public Stripe_Insert(StripeChain chain,String path){
    this.chain=chain;
    initImage(path);}

  /*
   * ################################
   * CHAIN
   * ################################
   */
  
  StripeChain chain;
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  BufferedImage image;
  
  private void initImage(String path){
    File f=new File(path);
    try{
      image=ImageIO.read(f);
    }catch(IOException x){
      System.out.println("exception in image init");
      x.printStackTrace();}}

  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public int getImageWidth(){
    return image.getWidth();}
  
  public int getImageX(){
    int 
      a=chain.indexOf(this),
      sum=0;
    for(int i=0;i<a;i++)
      sum+=chain.get(i).getImageWidth();
    return sum;}
  
}

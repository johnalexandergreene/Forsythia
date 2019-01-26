package org.fleen.forsythia.app.spinner.core;

/*
 * A horizontal stripe
 * 
 * top=getY()
 * bottom=getY()++getHeight()
 * left=0
 * right=spinner.width-1
 * 
 */
public interface Stripe{
  
  /*
   * location of the top edge of this stripe's image within the stripechain image. 
   */
  int getY();
  
  int getHeight();
  
  
}

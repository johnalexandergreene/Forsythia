package org.fleen.forsythia.app.spinner.spinnerVideoGenerators.spinner0000;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.app.spinner.core.Renderer;
import org.fleen.forsythia.app.spinner.core.Spinner;
import org.fleen.forsythia.app.spinner.core.Stripe;
import org.fleen.forsythia.app.spinner.core.Stripe_ColoredBox;

public class Renderer0 implements Renderer{

  public BufferedImage getFrame(Spinner spinner){
    BufferedImage image=new BufferedImage(
      spinner.viewportwidth,
      spinner.viewportheight,
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g=image.createGraphics();
    for(Stripe s:spinner.chain.stripes){
      if(s instanceof Stripe_ColoredBox)
        renderStripe_ColoredBox((Stripe_ColoredBox)s,spinner,g);}
    return image;}
  
  void renderStripe_ColoredBox(Stripe_ColoredBox stripe,Spinner spinner,Graphics2D g){
    int 
      x=0,
      y=stripe.getTop(),
      w=spinner.viewportwidth,
      h=stripe.getHeight();
    g.setPaint(stripe.getColor());
    g.fillRect(x,y,w,h);}

}

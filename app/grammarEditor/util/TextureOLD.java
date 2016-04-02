package org.fleen.forsythia.app.grammarEditor.util;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/*
 * general purpose transform-friendly texture class
 */
public class TextureOLD extends TexturePaint {

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public TextureOLD(BufferedImage i) {
    super(i,getRectangle(i));}
  
  private static Rectangle getRectangle(BufferedImage i){
    return new Rectangle(0,0,i.getWidth(),i.getHeight());}

  /*
   * ################################
   * PAINTING STUFF
   * ################################
   */
  
  private AffineTransform transform=new AffineTransform();
  
  public void apply(Graphics2D g,Path2D.Double path,float alpha){
    try{
      transform=g.getTransform().createInverse();
    }catch(Exception x){
      x.printStackTrace();}
    g.setPaint(this);
    Composite oldcomposite=g.getComposite();
    AlphaComposite composite=AlphaComposite.getInstance(
      AlphaComposite.SRC_OVER, 
      alpha);
    g.setComposite(composite);
    g.fill(path);
    g.setComposite(oldcomposite);}

  public PaintContext createContext(ColorModel cm,
      Rectangle deviceBounds,
      Rectangle2D userBounds,
      AffineTransform xform,
      RenderingHints hints) {
    AffineTransform newTransform = new AffineTransform(xform);
    newTransform.concatenate(transform);
    return super.createContext(cm, deviceBounds, userBounds, newTransform, hints);}
}

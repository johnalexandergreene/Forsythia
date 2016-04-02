package org.fleen.forsythia.app.grammarEditor.util.textures;

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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/*
 * general purpose transform-friendly texture class
 */
public class Texture extends TexturePaint {

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Texture(BufferedImage i) {
    super(i,getRectangle(i));}
  
  private static Rectangle getRectangle(BufferedImage i){
    return new Rectangle(0,0,i.getWidth(),i.getHeight());}

  /*
   * ################################
   * PAINTING STUFF
   * ################################
   */
  
  private AffineTransform transform=new AffineTransform();
  
  public void apply(Graphics2D g,Path2D path,float alpha){
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
  
  /*
   * ################################
   * STATIC TEXTURES LIST
   * anywhere we need textures, get them here
   * ################################
   */
  
  private static Texture[] TEXTURES=null;
  
  public static final Texture[] getTextures(){
    if(TEXTURES==null)
      TEXTURES=initTextures();
    return TEXTURES;}
  
  private static final Texture[] initTextures(){
    List<BufferedImage> images=new ArrayList<BufferedImage>();
    try{
      images.add(ImageIO.read(Texture.class.getResource("tex_00.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_01.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_02.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_03.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_04.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_05.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_06.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_07.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_08.png")));
      images.add(ImageIO.read(Texture.class.getResource("tex_09.png")));
    }catch(Exception x){
      x.printStackTrace();}
    int s=images.size();
    Texture[] textures=new Texture[s];
    for(int i=0;i<s;i++)
      textures[i]=new Texture(images.get(i));
    return textures;}
}

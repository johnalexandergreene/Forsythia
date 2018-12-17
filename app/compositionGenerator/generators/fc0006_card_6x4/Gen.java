package org.fleen.forsythia.app.compositionGenerator.generators.fc0006_card_6x4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Random;

import org.fleen.forsythia.app.compositionGenerator.FCRIG_Basic;
import org.fleen.forsythia.app.compositionGenerator.Palette;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMapGen;
import org.fleen.forsythia.app.compositionGenerator.composer.ForsythiaCompositionGen;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public class Gen extends FCRIG_Basic{

  public static final Color STROKECOLOR=Color.white;
  public static final float STROKETHICKNESS=0.014f;
  
  protected ForsythiaCompositionGen getComposer(){//TODO ForsythiaCompositionGen?
    ForsythiaCompositionGen c=new Composer003_SplitBoil_DoubleRootEntropy_YAxisDistanceEntropyGradient();
    return c;}

  protected ColorMapGen getColorMapper(){//TODO ColorMapGen?
    ColorMapGen m=new CM_SymmetricChaos_EggLevelTriplePaletteSplit();
    m.setPalette(Palette.P_GRACIE001);
    return m;}
  
  public static final HashMap<RenderingHints.Key,Object> RENDERING_HINTS=
      new HashMap<RenderingHints.Key,Object>();
    
  static{
    RENDERING_HINTS.put(
      RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    RENDERING_HINTS.put(
      RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DEFAULT);
    RENDERING_HINTS.put(
      RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    RENDERING_HINTS.put(
      RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY); 
    RENDERING_HINTS.put(
      RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);}
  
  public BufferedImage getImage(){
    BufferedImage i=new BufferedImage(imagewidth,imageheight,BufferedImage.TYPE_INT_RGB);
    AffineTransform t=getTransform(imagewidth,imageheight,composition);
    Graphics2D g=i.createGraphics();
    //fill background, this also does the border
    g.setPaint(backgroundandborder);
    g.fillRect(0,0,imagewidth,imageheight);
    //
    g.setRenderingHints(RENDERING_HINTS);
    g.setTransform(t);
    //fill composition polygons
    for(FPolygon p:composition.getLeafPolygons()){
      g.setPaint(colormap.getColor(p));
      g.fill(p.getDPolygon().getPath2D());}
    //stroke compsoiton polygons
    g.setPaint(STROKECOLOR);
    g.setStroke(
      new BasicStroke(STROKETHICKNESS,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0));
    for(FPolygon p:composition.getLeafPolygons())
      g.draw(p.getDPolygon().getPath2D());
    //do logo box
    doLogoBox(i); 
    //rotate it
    BufferedImage i0=new BufferedImage(i.getHeight(),i.getWidth(),BufferedImage.TYPE_INT_RGB);
    g=i0.createGraphics();
    t=AffineTransform.getQuadrantRotateInstance(1);
    t.translate(0,-i.getHeight());
    g.setTransform(t);
    g.drawImage(i,null,null);
    return i0;
    //
//    return i;
    }
  
  /*
   * ################################
   * DO LOGO BOX
   * lower righthand corner
   * rotated PI/2 CCW
   * ################################
   */
  
  Random rnd=new Random();
  
  private void doLogoBox(BufferedImage i){
    System.out.println("do logo box");
    BufferedImage b=getLogoBox(i);
    System.out.println("dims : w="+b.getWidth()+" h="+b.getHeight());
    Graphics2D g=i.createGraphics();
    g.drawImage(b,i.getWidth()-b.getWidth(),i.getHeight()-b.getHeight(),null);
  }
  
  private BufferedImage getLogoBox(BufferedImage i){
    BufferedImage box=new BufferedImage(72,326,BufferedImage.TYPE_INT_RGB);
    Graphics2D g=box.createGraphics();
    g.setRenderingHints(RENDERING_HINTS);
    g.setPaint(Color.white);
    g.fillRect(0,0,72,326);
   
    //
    g.setTransform(AffineTransform.getQuadrantRotateInstance(-1));
    //
    g.setPaint(Palette.P_GRACIE001[rnd.nextInt(Palette.P_GRACIE001.length)]);
    g.setFont(getMMFont());
    g.drawString("FLEEN.ORG",-308,52);
    //
    return box;}
  
  private Font getMMFont(){
    try{
      GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
//      ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,new File("/home/john/.fonts/ShareTechMono-Regular.ttf")));
      ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,new File("/home/john/.fonts/Arial_Black.ttf")));
      
    }catch(Exception x){
      x.printStackTrace();}
    Font f=new Font("Arial Black",Font.PLAIN,45);
    return f;}
  
  /*
   * ################################
   * TRANSFORM
   * 
   * flush to top, bottom and left
   * bleed to right
   * 
   * ################################
   */
  
  private AffineTransform getTransform(int width,int height,ForsythiaComposition composition){
    Rectangle2D.Double compositionbounds=composition.getRootPolygon().getDPolygon().getBounds();
    double cbheight=compositionbounds.getHeight();
    AffineTransform transform=new AffineTransform();
    //scale
    double scale=((double)height)/cbheight;
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=0,
      yoff=-height/scale;
    transform.translate(xoff,yoff);
    //
    return transform;}
  
  
  
  
  
  

}

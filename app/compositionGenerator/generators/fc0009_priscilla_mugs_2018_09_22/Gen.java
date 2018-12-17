package org.fleen.forsythia.app.compositionGenerator.generators.fc0009_priscilla_mugs_2018_09_22;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.forsythia.app.compositionGenerator.FCRIG_Basic;
import org.fleen.forsythia.app.compositionGenerator.Palette;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMapGen;
import org.fleen.forsythia.app.compositionGenerator.composer.ForsythiaCompositionGen;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public class Gen extends FCRIG_Basic{

  //VERY PURPLE
  static final Color[] PALETTE0={
      new Color(82, 43,114),
      new Color(148,118,171),
      new Color(113, 76,143),
      new Color(56, 19, 86),
      new Color(33,  4, 57),
      new Color(254,102,238)//ACCENT
  };
  
  //LIGHT PURPLE
  static final Color[] PALETTE1={
      new Color(152,123,180),
      new Color(210,196,224),
      new Color(182,160,204),
      new Color(125, 92,160),
      new Color(102, 64,140),
      new Color(255,169,105)//ACCENT
  };
  
  //LIGHT LAVENDER
  static final Color[] PALETTE2={
      new Color(141,128,183),
      new Color(205,199,225),
      new Color(174,164,206),
      new Color(112, 96,163),
      new Color( 86, 69,142),
      new Color(128,128,255)//ACCENT
  };
  
  //UNNAMED
  static final Color[] PALETTE3={
      new Color( 58, 50,118),
      new Color(132,126,177),
      new Color( 91, 83,147),
      new Color( 32, 24, 88),
      new Color( 14,  8, 59),
      new Color(128,255,128)//ACCENT
  };
  
  
  static Color[] PALETTE=PALETTE3;
  
  
  public static final Color STROKECOLOR=Color.white;
  public static final float STROKETHICKNESS=0.02f;
  
  protected ForsythiaCompositionGen getComposer(){//TODO ForsythiaCompositionGen?
    ForsythiaCompositionGen c=new Composer003_SplitBoil_DoubleRootEntropy_YAxisDistanceEntropyGradient();
    return c;}

  protected ColorMapGen getColorMapper(){//TODO ColorMapGen?
    ColorMapGen m=new CM_SymmetricChaos_EggLevelTriplePaletteSplit();
    m.setPalette(PALETTE);
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
    //
    return i;}
  
  /*
   * ################################
   * TRANSFORM
   * scale and fit the composition to the image
   * ################################
   */
  
  private AffineTransform getTransform(int width,int height,ForsythiaComposition composition){
    //get all the relevant metrics
    Rectangle2D.Double compositionbounds=composition.getRootPolygon().getDPolygon().getBounds();
    double
      cbwidth=compositionbounds.getWidth(),
      cbheight=compositionbounds.getHeight(),
      cbxmin=compositionbounds.getMinX(),
      cbymin=compositionbounds.getMinY();
    //
    int 
      awidth=width-borderthickness*2,
      aheight=height-borderthickness*2;
    AffineTransform transform=new AffineTransform();
    //scale
    double
      scale,
      p0=cbwidth/cbheight,
      p1=awidth/aheight;
    if(p0>p1){
      scale=((double)awidth)/cbwidth;
    }else{
      scale=((double)aheight)/cbheight;}
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=((width/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((height/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff,yoff);
    //
    return transform;}
  
  
  
  
  
  

}

package org.fleen.forsythia.app.compositionGenerator.generators.fc0008_dotty_render_test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.fleen.forsythia.app.compositionGenerator.FCRIG_Basic;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMapGen;
import org.fleen.forsythia.app.compositionGenerator.composer.ForsythiaCompositionGen;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPolygon;

public class Gen extends FCRIG_Basic{
  
  
  public static final Color 
    GRAY0=new Color(0,0,0),
    GRAY1=new Color(32,32,32),
    GRAY2=new Color(64,64,64),
    GRAY3=new Color(92,92,92);
  
  static final Color[] PALETTE={
    GRAY0,GRAY1,GRAY2,GRAY3};

  public static final Color STROKECOLOR=Color.white;
  public static final float STROKETHICKNESS=0.009f;
  
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
    //fill polygons with dots
    Color c;
    int colorindex;
    for(FPolygon p:composition.getLeafPolygons()){
      c=colormap.getColor(p);
      colorindex=getColorIndex(c);
      doDots(p.getDPolygon(),colorindex,g);}
    //
    return i;}
  
  private int getColorIndex(Color c){
    int a=(Arrays.asList(PALETTE)).indexOf(c);
    if(a==-1)throw new IllegalArgumentException("BAD COLOR");
    return a;}
  
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
  
  /*
   * ################################
   * DO DOTS
   * get random interior dot : d0
   * map d0 to dotmap
   * get a random dot that is 
   *   at D distance from random viable dot in the dotmap
   *   inside the polygon
   * if such a dot cannot be gotten then mark that dot as unviable
   * keep doing this until all dots are unviable
   * 
   * 
   * ################################
   */
  
  double dotrange=0.03,edgerange=0.04,radius=0.01;
  Color DOTCOLOR=Color.black;
  
  private void doDots(DPolygon dpolygon,int colorindex,Graphics2D g){
    List<Dot> dots=getDots(dpolygon,colorindex);
    if(dots==null)return;
    for(Dot d:dots)
      drawDot(d,g);}
  
  private void drawDot(Dot d,Graphics2D g){
    Shape c=new Ellipse2D.Double(d.x-radius,d.y-radius,2.0*radius,2.0*radius);
    g.setPaint(DOTCOLOR);
    g.fill(c);}
  
  private List<Dot> getDots(DPolygon dpolygon,int colorindex){
    List<Dot> dots=new ArrayList<Dot>();
    Dot a=getRandomInteriorDot(dpolygon);
    dots.add(a);
    if(a==null)return null;
    //ok we got our first interior dot
//    while(anyViable(dots)){
//      
//    }
    System.out.println("dotcount="+dots.size());
    return dots;}
  
  Random rnd=new Random();
  double MINDIS=0.003;
  
  Dot getRandomInteriorDot(DPolygon dpolygon){
    Rectangle2D.Double b=dpolygon.getBounds();
    double x,y;
    for(int i=0;i<100;i++){
      x=rnd.nextDouble()*(b.getMaxX()-b.getMinX())+b.getMinX();
      y=rnd.nextDouble()*(b.getMaxY()-b.getMinY())+b.getMinY();
      if(dpolygon.containsPoint(x,y)&&dpolygon.getDistance(x,y)>edgerange)
        return new Dot(x,y);}
    return null;}
  
//  Dot getRandomDot(){
//    
//  }
  
  boolean anyViable(List<Dot> dotmap){
    for(Dot d:dotmap){
      if(!d.viable)
        return true;}
    return false;}
  
  class Dot{
    Dot(double x,double y){
      this.x=x;
      this.y=y;}
    double x,y;
    boolean viable=true;}
  
  
  
  
  
  

}

package org.fleen.forsythia.app.compositionGenerator.generators.fc0004_poster_18x30_Stamp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.fleen.forsythia.app.compositionGenerator.FCRIG_Basic;
import org.fleen.forsythia.app.compositionGenerator.Palette;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMapGen;
import org.fleen.forsythia.app.compositionGenerator.composer.ForsythiaCompositionGen;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;

public class Gen extends FCRIG_Basic{

  public static final Color STROKECOLOR=Color.white;
  public static final float STROKETHICKNESS=0.009f;
  
  protected ForsythiaCompositionGen getComposer(){
    ForsythiaCompositionGen c=new Composer003_SplitBoil_DoubleRootEntropy_YAxisDistanceEntropyGradient();
    return c;}

  protected ColorMapGen getColorMapper(){
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
  
  AffineTransform tpolygon;
  
  public BufferedImage getImage(){
    BufferedImage i=new BufferedImage(imagewidth,imageheight,BufferedImage.TYPE_INT_RGB);
    tpolygon=getPolygonTransform(imagewidth,imageheight,composition);
    //fill background, this also does the border
    Graphics2D gclean=i.createGraphics();
    gclean.setRenderingHints(RENDERING_HINTS);
    gclean.setPaint(backgroundandborder);
    gclean.fillRect(0,0,imagewidth,imageheight);
    //fill polygons
    Graphics2D gpolygon=i.createGraphics();
    gpolygon.setTransform(tpolygon);
    gpolygon.setRenderingHints(RENDERING_HINTS);
    for(FPolygon p:composition.getLeafPolygons()){
      gpolygon.setPaint(colormap.getColor(p));
      gpolygon.fill(p.getDPolygon().getPath2D());}
    //do stamps
    for(FPolygon p:composition.getLeafPolygons())
      if(p.hasTags("stamp"))
        renderStamp(p,gpolygon,gclean);
    //stroke polygons
    gpolygon.setPaint(STROKECOLOR);
    gpolygon.setStroke(
      new BasicStroke(STROKETHICKNESS,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0));
    for(FPolygon p:composition.getLeafPolygons())
      gpolygon.draw(p.getDPolygon().getPath2D());
    //
    return i;}
  
  /*
   * ################################
   * TRANSFORM
   * scale and fit the composition to the image
   * ################################
   */
  
  private AffineTransform getPolygonTransform(int width,int height,ForsythiaComposition composition){
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
   * STAMP
   * given a polygon
   * consider the untransformed image
   * get center point for polygon (using image transform)
   * get scale and rotate
   * drawimage onto a fresh untransformed graphics2D
   * we do it this way to get a max quality bitmap that we can do whatever blending function with 
   * 
   *  
   * 
   * 
   * ################################
   */
  
  BufferedImage fly=null;
  
  void renderStamp(FPolygon fhex,Graphics2D gpolygon,Graphics2D gclean){
    DPolygon hex=fhex.getDPolygon();
    AffineTransform tpolygon=gpolygon.getTransform();
    //get real polygon metrics
    double[][] hp=hex.getPointsAsDoubles();
    double[] p0=hp[0],p3=hp[3];
    tpolygon.transform(p0,0,p0,0,1);
    tpolygon.transform(p3,0,p3,0,1);
    //center
    double[] hexcenter=GD.getPoint_Mid2Points(p0[0],p0[1],p3[0],p3[1]);
    //radius
    double hexradius=GD.getDistance_PointPoint(p0[0],p0[1],p3[0],p3[1]);
    //forward (flip for cartesian-screen coor conversion)
    double hexforward=GD.normalizeDirection(GD.getDirection_PointPoint(hexcenter[0],hexcenter[1],p0[0],p0[1])+GD.PI);
    //drawfly
    drawFly(gclean,hexcenter,hexradius,hexforward,fhex);}
  
  static final double FLYSCALE=1.13;
  
  private void drawFly(Graphics2D g,double[] hexcenter,double hexradius,double hexforward,FPolygon fhex){
    BufferedImage i=getScaledAndRotatedImage(hexradius,hexforward);
    AffineTransform t=getCenterTransform(i,hexcenter);
    g.setClip(getClipPath(fhex));
    g.drawImage(i,t,null);
    g.setClip(null);}
  
  private Path2D getClipPath(FPolygon fhex){
    DPolygon dpolygon=fhex.getDPolygon();
    Path2D.Double path=new Path2D.Double();
    double[] p0=new double[2];
    boolean moved=false;
    for(DPoint p:dpolygon){
      p0[0]=p.x;
      p0[1]=p.y;
      tpolygon.transform(p0,0,p0,0,1);
      if(!moved){
        path.moveTo(p0[0],p0[1]);
        moved=true;
      }else{
        path.lineTo(p0[0],p0[1]);}}
    path.closePath();
    return path;}
  
  BufferedImage getScaledAndRotatedImage(double hexradius,double hexforward){
    if(fly==null)initFly();
    int w=fly.getWidth(),h=fly.getHeight();
    double s=(hexradius/w)*FLYSCALE;
    AffineTransform t=AffineTransform.getScaleInstance(s,s);
    t.rotate(hexforward,w/2,h/2);
    //
    BufferedImage i=new BufferedImage((int)(w*s),(int)(h*s),BufferedImage.TYPE_INT_RGB);
    Graphics2D g=i.createGraphics();
    g.drawImage(fly,t,null);
    //
    return i;}
  
  AffineTransform getCenterTransform(BufferedImage i,double[] hexcenter){
    double 
      icx=((double)i.getWidth())/2,
      icy=((double)i.getHeight())/2;
    AffineTransform t=AffineTransform.getTranslateInstance(hexcenter[0]-icx,hexcenter[1]-icy);
    return t;}
  
  void initFly(){
    try{
//      fly=ImageIO.read(Gen.class.getResource("fly000.png"));
      fly=ImageIO.read(Gen.class.getResource("yellowfly.png"));
    }catch(Exception x){
      System.out.println("COULDN'T LOAD FLY");
      x.printStackTrace();}}
  
  
  
  
  
  
  

}

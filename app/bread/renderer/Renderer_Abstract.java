package org.fleen.forsythia.app.bread.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;
import org.fleen.util.tree.TreeNode;

public abstract class Renderer_Abstract implements Renderer{
  
  /*
   * ################################
   * BACKGROUND COLOR
   * ################################
   */
  
  public static final Color BACKGROUNDCOLOR_DEFAULT=new Color(255,255,255);
  
  protected Color backgroundcolor=BACKGROUNDCOLOR_DEFAULT;
  
  public void setBackgroundColor(Color c){
    backgroundcolor=c;}
  
  public Color getBackgroundColor(){
    return backgroundcolor;}
  
  /*
   * ################################
   * MARGIN
   * ################################
   */
  
  public static final int MARGIN_DEFAULT=20;
  
  protected int margin=MARGIN_DEFAULT;
  
  public void setMargin(int m){
    margin=m;}
  
  public int getMargin(){
    return margin;}
  
  /*
   * ################################
   * RENDERING HINTS
   * ################################
   */
  
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

  /*
   * ################################
   * GET IMAGE
   * ################################
   */
  
  BufferedImage image;
  
  public BufferedImage getImage(int width,int height,ForsythiaComposition composition){
    image=getInitImage(width,height);
    AffineTransform transform=getTransform(width,height,composition);
    Graphics2D graphics=(Graphics2D)image.getGraphics();
    graphics.setTransform(transform);
    graphics.setRenderingHints(RENDERING_HINTS);
    render(composition,graphics,transform);
    //reset renderer, clear everything
//    pathbypolygon.clear();
    //
    return image;}
  
  //render the specified composition to the specified graphics
  //pass transform too because it's handy sometimes, like for stroking
  protected abstract void render(ForsythiaComposition composition,Graphics2D graphics,AffineTransform transform);
  
  private BufferedImage getInitImage(int w,int h){
    BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics=image.createGraphics();
    graphics.setPaint(backgroundcolor);
    graphics.fillRect(0,0,w,h);
    return image;}
  
  private AffineTransform getTransform(int imagewidth,int imageheight,ForsythiaComposition composition){
    //get all the relevant metrics
    Rectangle2D.Double compositionbounds=getPolygonBoundingRect(composition.getRootPolygon());
    double
      dmargin=margin,
      cbwidth=compositionbounds.getWidth(),
      cbheight=compositionbounds.getHeight(),
      cbxmin=compositionbounds.getMinX(),
      cbymin=compositionbounds.getMinY();
    AffineTransform transform=new AffineTransform();
    //scale
    double 
      sw=(imagewidth-dmargin*2)/cbwidth,
      sh=(imageheight-dmargin*2)/cbheight;
    double scale=Math.min(sw,sh);
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=((imagewidth/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((imageheight/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff,yoff);
    //
    return transform;}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  /*
   * ================================
   * 2D STUFF
   * ================================
   */
  
  //returns the bounding rectangle2d of the specified NPolygon
  protected Rectangle2D.Double getPolygonBoundingRect(FPolygon polygon){
    List<DPoint> points=polygon.getDPolygon();
    double maxx=Double.MIN_VALUE,maxy=maxx,minx=Double.MAX_VALUE,miny=minx;
    for(DPoint p:points){
      if(minx>p.x)minx=p.x;
      if(miny>p.y)miny=p.y;
      if(maxx<p.x)maxx=p.x;
      if(maxy<p.y)maxy=p.y;}
    return new Rectangle2D.Double(minx,miny,maxx-minx,maxy-miny);}
  
//  Map<FPolygon,Path2D> pathbypolygon=new Hashtable<FPolygon,Path2D>();
//  
//  protected Path2D getPath2D(FPolygon polygon){
//    Path2D path=pathbypolygon.get(polygon);
//    if(path==null){
//      path=createPath2D(polygon);
//      pathbypolygon.put(polygon,path);}
//    return path;}
//  
//  private Path2D createPath2D(FPolygon polygon){
//    Path2D.Double path=new Path2D.Double();
//    List<DPoint> points=polygon.getDPolygon();
//    DPoint p=points.get(0);
//    path.moveTo(p.x,p.y);
//    for(int i=1;i<points.size();i++){
//      p=points.get(i);
//      path.lineTo(p.x,p.y);}
//    path.closePath();
//    return path;}
  
}

package org.fleen.forsythia.app.bread.renderer;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.rasterMap.Cell;
import org.fleen.geom_2D.rasterMap.Presence;
import org.fleen.geom_2D.rasterMap.RasterMap;
import org.fleen.util.tree.TreeNode;

public class Renderer_Rasterizer000 implements Renderer{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Renderer_Rasterizer000(Color[] color0,Color[] color1){
    this.color0=color0;
    this.color1=color1;}
  
  private Color[] color0,color1;
  
  private static final double GLOWSPAN=1.5;
  
  ForsythiaComposition composition;
  Map<DPolygon,FPolygon> fpolygonsbydpolygons=new HashMap<DPolygon,FPolygon>();
  
  public BufferedImage getImage(int width,int height,ForsythiaComposition composition){
    System.out.println("---RENDERING---");
    this.composition=composition;
    initFPolygonsByDPolygons();
    List<DPolygon> polygons=new ArrayList<DPolygon>(fpolygonsbydpolygons.keySet());
    RasterMap prm=new RasterMap(
      width,height,getTransform(width,height,composition),GLOWSPAN,polygons);
    //
    BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    System.out.println("+++rendering cells+++");
    for(Cell c:prm)
      image.setRGB(c.x,c.y,getPixelColor(c).getRGB());
    //
    return image;}
  
  Random rnd=new Random();
  
  Map<FPolygonSignature,Color> polygoncolorbysig=new Hashtable<FPolygonSignature,Color>();
  
  public Color getPolygonColor(FPolygon polygon){
    FPolygonSignature sig=polygon.getSignature();
    Color color=polygoncolorbysig.get(sig);
    if(color==null){
      int 
        eggdepth=getTagDepth(polygon,"egg"),
        colorindex;
      //even level
      if(eggdepth%2==0){
        colorindex=rnd.nextInt(color0.length);
        color=color0[colorindex];
      //odd level
      }else{
        colorindex=rnd.nextInt(color1.length);
        color=color1[colorindex];}
      polygoncolorbysig.put(sig,color);}
    return color;}
  
  protected int getTagDepth(TreeNode node,String tag){
//  return node.getDepth();
  int c=0;
  TreeNode n=node;
  FPolygon p;
  while(n!=null){
    if(n instanceof FPolygon){
      p=(FPolygon)n;
      if(p.hasTags(tag))
        c++;}
    n=n.getParent();}
  return c;
  }
  
  
  
  private void initFPolygonsByDPolygons(){
    fpolygonsbydpolygons.clear();
    DPolygon d;
    for(FPolygon f:composition.getLeafPolygons()){
      d=f.getDPolygon();
      fpolygonsbydpolygons.put(d,f);}}
  
  private static final double margin=20;
  
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
  
  
  /*
   * ################################
   * PIXEL COLOR
   * ################################
   */
  
  private Color getPixelColor(Cell c){
    //get intensity sum
    double intensitysum=0;
    for(Presence p:c.presences){
      intensitysum+=(p.intensity);}
    int r=0,g=0,b=0;
    Color color;
    double normalized;
    for(Presence p:c.presences){
      normalized=(p.intensity)/intensitysum;
      color=getPolygonColor((FPolygon)p.polygon.object);
      r+=(int)(color.getRed()*normalized);
      g+=(int)(color.getGreen()*normalized);
      b+=(int)(color.getBlue()*normalized);}
    //yes
    if(r<0)r=0;
    if(r>255)r=255;
    if(g<0)g=0;
    if(g>255)g=255;
    if(b<0)b=0;
    if(b>255)b=255;
    //
    return new Color(r,g,b);}
  
  
}

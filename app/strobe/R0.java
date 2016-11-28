package org.fleen.forsythia.app.strobe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
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
import org.fleen.geom_2D.GD;
import org.fleen.util.tree.TreeNode;

public class R0{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  R0(Strobe strobe){
    this.strobe=strobe;}
  
  /*
   * ################################
   * STROBE
   * ################################
   */
  
  Strobe strobe;
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  BufferedImage image;
  Graphics2D graphics;
  
  //RENDERING HINTS
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
    OrderedPolygons orderedvisiblepolygons=new OrderedPolygons(strobe.visiblepolygons);
    //render visible polygons
    double
      visiblepolygondetailsizefloor=strobe.getVisiblePolygonDetailSizeFloor(),
      alpha255detailfloor=visiblepolygondetailsizefloor*2;
    ViewportDef viewportdef=strobe.getViewportDef();
    initGraphics(viewportdef);
    fillVisiblePolygons(viewportdef,graphics,orderedvisiblepolygons,visiblepolygondetailsizefloor,alpha255detailfloor);
    strokeVisiblePolygons(viewportdef,graphics,orderedvisiblepolygons,visiblepolygondetailsizefloor,alpha255detailfloor);
    return image;}
  
  private void initGraphics(ViewportDef viewportdef){
    image=new BufferedImage(viewportdef.width,viewportdef.height,BufferedImage.TYPE_INT_ARGB);
    graphics=image.createGraphics();
    graphics.setRenderingHints(RENDERING_HINTS);
    graphics.setPaint(new Color(255,255,255));
    graphics.fillRect(0,0,viewportdef.width,viewportdef.height);
    graphics.setTransform(viewportdef.getTransform());}
  
  private void fillVisiblePolygons(ViewportDef viewportdef,Graphics2D graphics,OrderedPolygons visiblepolygons,double visibledetailfloor,double alpha255detailfloor){
    double detailsize,zz;
    int alpha;
    Path2D path;
    Color fillcolor;
    //fill polygons
    for(List<FPolygon> a:visiblepolygons.getPolygonLists()){
      for(FPolygon polygon:a){
        //set alpha according to detail size relative to visibledetailfloor and alpha255detailfloor
        detailsize=polygon.getDetailSize();
        if(detailsize>alpha255detailfloor){
          alpha=255;
        }else if(detailsize<visibledetailfloor){
          alpha=0;
        }else{
          zz=(detailsize-visibledetailfloor)/(alpha255detailfloor-visibledetailfloor);
          alpha=(int)(zz*255);}
        path=polygon.getDPolygon().getPath2D();
        //get basic color for polygon
        fillcolor=getColor(polygon,frameindex);
        //apply alpha
        fillcolor=new Color(fillcolor.getRed(),fillcolor.getGreen(),fillcolor.getBlue(),alpha);
        graphics.setPaint(fillcolor);
        graphics.fill(path);}}}
  
  private void strokeVisiblePolygons(ViewportDef viewportdef,Graphics2D graphics,OrderedPolygons visiblepolygons,double visibledetailfloor,double alpha255detailfloor){
    //stroke visible polygons
    double detailsize,zz;
    int alpha;
    Path2D path;
    BasicStroke stroke=createStroke(viewportdef,1.5);
    graphics.setStroke(stroke);
    for(List<FPolygon> a:visiblepolygons.getPolygonLists()){
      for(FPolygon polygon:a){
        //set alpha according to detail size relative to visibledetailfloor and alpha255detailfloor
        detailsize=polygon.getDetailSize();
        if(detailsize>alpha255detailfloor){
          alpha=255;
        }else if(detailsize<visibledetailfloor){
          alpha=0;
        }else{
          zz=(detailsize-visibledetailfloor)/(alpha255detailfloor-visibledetailfloor);
          alpha=(int)(zz*255);}
        path=polygon.getDPolygon().getPath2D();
        graphics.setPaint(new Color(0,0,0,alpha));//black
        graphics.draw(path);}}}
  
  private BasicStroke createStroke(ViewportDef viewportdef,double size){
    BasicStroke s=new BasicStroke((float)(size*viewportdef.scale),BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return s;}
  
  /*
   * ################################
   * FORSYTHIA COMPOSITION
   * ################################
   */
  
  ForsythiaComposition composition;
  
  /*
   * ################################
   * VIEWPORT POLYGON
   * ################################
   */
  
  private DPolygon getViewportPolygon(ViewportDef viewportdef){
    
    double 
      scaledwidth=viewportdef.width*viewportdef.scale,
      scaledheight=viewportdef.height*viewportdef.scale,
      vpforward=viewportdef.forward;
    double[] 
      pcenternorth=GD.getPoint_PointDirectionInterval(viewportdef.center.x,viewportdef.center.y,vpforward,scaledheight/2),
      pnw=GD.getPoint_PointDirectionInterval(
        pcenternorth[0],pcenternorth[1],
        GD.normalizeDirection(vpforward-GD.PI/2),
        scaledwidth/2),
      pne=GD.getPoint_PointDirectionInterval(
        pnw[0],pnw[1],
        GD.normalizeDirection(vpforward+GD.PI/2),
        scaledwidth),
      pse=GD.getPoint_PointDirectionInterval(
        pne[0],pne[1],
        GD.normalizeDirection(vpforward+GD.PI),
        scaledheight),
      psw=GD.getPoint_PointDirectionInterval(
        pse[0],pse[1],
        GD.normalizeDirection(vpforward-GD.PI/2),
        scaledwidth);
    DPolygon vp=new DPolygon(
      new DPoint(pnw),
      new DPoint(pne),
      new DPoint(pse),
      new DPoint(psw));
    return vp;}
  
  /*
   * ################################
   * POLYGON COLOR
   * ################################
   */
  
  Random rnd=new Random();
  
  //SPLIT PALETTE
  
  ////that good early palette, use black stroke
  //private Color[] 
  //  color0={new Color(236,208,120),new Color(217,91,67)},
  //  color1={new Color(192,41,66),new Color(83,119,122)};
  
  //red orange yellow white. use black stroke
  //private Color[] 
  //    color0={new Color(255,0,0),new Color(255,255,0)},
  //    color1={new Color(255,128,0),new Color(255,255,255)};
  
  //red blue, yellow white. use black stroke
  private Color[] 
    color0={new Color(255,128,128),new Color(128,128,255)},
    color1={new Color(255,255,128),new Color(255,255,255)};
  
  //straight black and white. use gray stroke
  //private Color[] 
  //    color0={new Color(0,0,0)},
  //    color1={new Color(255,255,255)};
  
  
  public Color getColor(FPolygon polygon,int frameindex){
    BlinkPattern bp=getBlinkPattern(polygon);
    Color c=bp.getColor(frameindex);
    return c;}
  
  Map<FPolygonSignature,BlinkPattern> blinkpatternbysig=new Hashtable<FPolygonSignature,BlinkPattern>();
  
  /*
   * chorus by sig
   * color pair by tag depth
   */
  private BlinkPattern getBlinkPattern(FPolygon polygon){
    BlinkPattern blinkpattern=blinkpatternbysig.get(polygon);
    if(blinkpattern==null){
      blinkpattern=createBlinkPattern(polygon);
      blinkpatternbysig.put(polygon.getSignature(),blinkpattern);}
    return blinkpattern;}
  
  private BlinkPattern createBlinkPattern(FPolygon polygon){
    int eggdepth=getTagDepth(polygon,"egg");
    Color[] colors;
  if(eggdepth%2==0)
    colors=color0;
  else
    colors=color1;
  BlinkPattern bp=new BlinkPattern(colors[0],colors[1]);
  return bp;}
  
  private int getTagDepth(TreeNode node,String tag){
    int c=0;
    TreeNode n=node;
    FPolygon p;
    while(n!=null){
      if(n instanceof FPolygon){
        p=(FPolygon)n;
        if(p.hasTag(tag))
          c++;}
      n=n.getParent();}
    return c;}
  
  
}

package org.fleen.forsythia.app.drifter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
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
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Basic.ForsythiaGrammar_Basic;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.util.tree.TreeNode;

public class DRC0 implements DrifterRendererComposer{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  DRC0(ForsythiaComposition composition){
    this.composition=composition;}
  
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
  
  public BufferedImage getImage(
    ViewportDef viewportdef,//TODO redo that. do it with just center, scale, forward... no transform
    double visibledetailsizefloor,double visibledetailsizeceiling){
    DPolygon viewportpolygon=getViewportPolygon(viewportdef);
    OrderedPolygons visiblepolygons=getVisiblePolygons(viewportpolygon,visibledetailsizefloor);
    //render visible polygons
    double alpha255detailfloor=visibledetailsizefloor*2;
    initGraphics(viewportdef);
    fillVisiblePolygons(viewportdef,graphics,visiblepolygons,visibledetailsizefloor,alpha255detailfloor);
    strokeVisiblePolygons(viewportdef,graphics,visiblepolygons,visibledetailsizefloor,alpha255detailfloor);
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
        fillcolor=getSplitPaletteEggLevelPolygonColor(polygon);
        //apply alpha
        fillcolor=new Color(fillcolor.getRed(),fillcolor.getGreen(),fillcolor.getBlue(),alpha);
        graphics.setPaint(fillcolor);
        graphics.fill(path);}}}
  
  private static final int STROKEMAXALPHA=200;
  
  private void strokeVisiblePolygons(ViewportDef viewportdef,Graphics2D graphics,OrderedPolygons visiblepolygons,double visibledetailfloor,double alpha255detailfloor){
    //stroke visible polygons
    double detailsize,zz;
    int alpha;
    Path2D path;
    BasicStroke stroke=createStroke(viewportdef,1.5);//1.5 is for 1080p
//    BasicStroke stroke=createStroke(viewportdef,6.1);//6.1 is for 5k
    
    graphics.setStroke(stroke);
    for(List<FPolygon> a:visiblepolygons.getPolygonLists()){
      for(FPolygon polygon:a){
        //set alpha according to detail size relative to visibledetailfloor and alpha255detailfloor
        detailsize=polygon.getDetailSize();
        if(detailsize>alpha255detailfloor){
          alpha=STROKEMAXALPHA;
        }else if(detailsize<visibledetailfloor){
          alpha=0;
        }else{
          zz=(detailsize-visibledetailfloor)/(alpha255detailfloor-visibledetailfloor);
          alpha=(int)(zz*STROKEMAXALPHA);}
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
   * VISIBLE POLYGONS
   * Given a viewport polygon and a visibledetailfloor
   *   get all polygons that intersect or are contained within the viewport polygon
   *   AND have detailsize > detailsizefloor
   *   
   * Assuming that the root intersects the viewport
   *   we grab all polygons in the tree that also intersect the viewport
   *   if a polygon has detailsize greater than visibledetailfloor then we cultivate it to get greater detail.
   * 
   * ----------------
   * 
   * get the root
   * if the root is within and root.detailsize> visibledetailfloor then add it to the list of visible polygons
   * if it isn't then we're done
   * get the root's children
   *   for each child 
   *   if the child is visible and then add it to the list of visible 
   *   
   * 
   * ################################
   */
  
  OrderedPolygons getVisiblePolygons(DPolygon viewport,double visibledetailsizefloor){
    List<FPolygon> 
      visiblepolygons=new ArrayList<FPolygon>(),
      //the polygons that might be visible
      prospects=new ArrayList<FPolygon>();
    FPolygon polygon;
    prospects.add(composition.getRootPolygon());
    while(!prospects.isEmpty()){
      polygon=prospects.remove(0);
      if(polygon.getDetailSize()>visibledetailsizefloor){
        if(polygon.getDPolygon().intersect(viewport)){
          visiblepolygons.add(polygon);
          prospects.addAll(getPolygonChildren(polygon));}}}
    return new OrderedPolygons(visiblepolygons);}
  
  /*
   * get the children of the specified polygon
   * if it doesn't have children then create some
   */
  private List<FPolygon> getPolygonChildren(FPolygon polygon){
    if(!polygon.hasChildren())
      cultivateOnce(polygon);
    List<FPolygon> children=polygon.getPolygonChildren();
    return children;}
  
  /*
   * ################################
   * POLYGON CULTIVATION
   * Our basic chorus-by-sig logic
   * ################################
   */
  
  Map<FPolygonSignature,Jig> jigbypolygonsig=new Hashtable<FPolygonSignature,Jig>();
  Random rnd=new Random();
  
  private void cultivateOnce(FPolygon polygon){
    Jig jig=selectJig(polygon);
    jig.createNodes(polygon);}
  
  private Jig selectJig(FPolygon polygon){
    //get a jig by signature
    //polygons with the same sig get the same jig
    Jig j=jigbypolygonsig.get(polygon.getSignature());
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      j=getRandomJig(composition.getGrammar(),polygon);
      if(j==null)return null;
      jigbypolygonsig.put(polygon.getSignature(),j);
      return j;}}
  
  private Jig getRandomJig(ForsythiaGrammar_Basic fg,FPolygon target){
    List<Jig> jigs=fg.getJigs(target);
    if(jigs.isEmpty())return null;
    Jig jig=jigs.get(rnd.nextInt(jigs.size()));
    return jig;}
  
  /*
   * ################################
   * POLYGON COLOR
   * ################################
   */
  
  //SPLIT PALETTE
  
  ////that good early palette, use black stroke
  //private Color[] 
  //  color0={new Color(236,208,120),new Color(217,91,67)},
  //  color1={new Color(192,41,66),new Color(83,119,122)};
  
  //red orange yellow white. use black stroke
  //private Color[] 
  //    color0={new Color(255,0,0),new Color(255,255,0)},
  //    color1={new Color(255,128,0),new Color(255,255,255)};
  
//  //red blue, yellow white. use black stroke
//  private Color[] 
//    color0={new Color(255,128,128),new Color(128,128,255)},
//    color1={new Color(255,255,128),new Color(255,255,255)};
  
  //straight black and white. use gray stroke
  //private Color[] 
  //    color0={new Color(0,0,0)},
  //    color1={new Color(255,255,255)};
  
  //colors
  //michael 2016 08 03
  private Color[] 
    color0={new Color(191,100,45),new Color(221,194,149)},
    color1={new Color(88,171,168),new Color(0,71,138)};
  
  Map<FPolygonSignature,Color> polygoncolorbysig=new Hashtable<FPolygonSignature,Color>();
  
  public Color getSplitPaletteEggLevelPolygonColor(FPolygon polygon){
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
    int c=0;
    TreeNode n=node;
    FPolygon p;
    while(n!=null){
      if(n instanceof FPolygon){
        p=(FPolygon)n;
        if(p.hasTags(tag))
          c++;}
      n=n.getParent();}
    return c;}
  
  
}

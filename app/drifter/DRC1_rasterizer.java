package org.fleen.forsythia.app.drifter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_2D.rasterMap.Cell;
import org.fleen.geom_2D.rasterMap.Presence;
import org.fleen.geom_2D.rasterMap.RasterMap;
import org.fleen.util.tree.TreeNode;

public class DRC1_rasterizer implements DrifterRendererComposer{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  DRC1_rasterizer(ForsythiaComposition composition){
    this.composition=composition;}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  BufferedImage image;
  Graphics2D graphics;
  private static final double GLOWSPAN=1.5;
  
  /*
   * viewport def is the definition of our viewport rectangle. Our window onto the composition
   * visiblepolygondetailsizefloor and visiblepolygondetailsizeceiling define the min and max detail size
   *   of visible polygons. For a polygon to be visible it must have detail size in that range.
   *   polygon color intensity is greatest in the center of that range, least on the edge.
   * 
   */
  public BufferedImage getImage(
    ViewportDef viewportdef,
    double visiblepolygondetailsizefloor,double visiblepolygondetailsizeceiling){
    System.out.println("frame creation started");
    long starttime=System.currentTimeMillis();
    //
    DPolygon viewportpolygon=getViewportPolygon(viewportdef);
    List<DPolygon> visiblepolygons=getVisiblePolygons(viewportpolygon,visiblepolygondetailsizefloor,visiblepolygondetailsizeceiling);
    //
    RasterMap prm=new RasterMap(
      viewportdef.width,viewportdef.height,viewportdef.getTransform(),GLOWSPAN,visiblepolygons);
    //
    Map<DPolygon,Double> detailsizedependentintensityfactors=getDetailSizeDependentIntensityFactors(
      visiblepolygons,visiblepolygondetailsizefloor,visiblepolygondetailsizeceiling);
    //
    image=new BufferedImage(viewportdef.width,viewportdef.height,BufferedImage.TYPE_INT_RGB);
    for(Cell c:prm)
      image.setRGB(c.x,c.y,getPixelColor(c,detailsizedependentintensityfactors).getRGB());
    //
    long finishtime=System.currentTimeMillis();
    System.out.println("frame creation finished. time elapsed : "+(finishtime-starttime));
    return image;}
  
  /*
   * ################################
   * PIXEL COLOR
   * ################################
   */
  
  private Color getPixelColor(Cell c,Map<DPolygon,Double> detailsizedependentintensityfactors){
    //get intensity sum
    double intensitysum=0,factor;
    for(Presence p:c.presences){
      factor=detailsizedependentintensityfactors.get(p.polygon);
      if(factor>1||factor<0)factor=0;
//        throw new IllegalArgumentException("FACTOR OUTSIDE VALID RANGE : factor="+factor);
      intensitysum+=(p.intensity*detailsizedependentintensityfactors.get(p.polygon));}
    //get normalized intensity for each presence and sum weighted r g b color components
    int r=0,g=0,b=0;
    Color color;
    double normalized;
    for(Presence p:c.presences){
      factor=detailsizedependentintensityfactors.get(p.polygon);
      normalized=(p.intensity*factor)/intensitysum;
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
  
  /*
   * ################################
   * POLYGON DETAIL SIZE DEPENDENT INTENSITY
   * polygons at detailsize in the middle of our visible detail size range get max intensity : 1.0
   * towards the edge they get something less
   * ################################
   */
  
  private Map<DPolygon,Double> getDetailSizeDependentIntensityFactors(
    List<DPolygon> polygons,double visiblepolygondetailsizefloor,double visiblepolygondetailsizeceiling){
    Map<DPolygon,Double> detailsizedependentpolygonintensityfactors=new Hashtable<DPolygon,Double>();
    double 
      mean=(visiblepolygondetailsizefloor+visiblepolygondetailsizeceiling)/2,
      range=mean-visiblepolygondetailsizefloor,
      detailsize,deviation,factor;
    for(DPolygon polygon:polygons){
      detailsize=polygon.getDetailSize();
      deviation=Math.abs(mean-detailsize);
      factor=1.0-(deviation/range);
      detailsizedependentpolygonintensityfactors.put(polygon,factor);}
    return detailsizedependentpolygonintensityfactors;}
  
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
   * 
   * Given a viewport polygon, a visibledetailfloor and a visiblepolygondetailsizeceiling
   * 
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
  
  List<DPolygon> getVisiblePolygons(DPolygon viewport,double visiblepolygondetailsizefloor,double visiblepolygondetailsizeceiling){
    List<FPolygon> 
      visiblepolygons=new ArrayList<FPolygon>(),
      //the polygons that might be visible
      prospects=new ArrayList<FPolygon>();
    FPolygon polygon;
    prospects.add(composition.getRootPolygon());
    while(!prospects.isEmpty()){
      polygon=prospects.remove(0);
      if(polygon.getDetailSize()>visiblepolygondetailsizefloor){
        if(polygon.getDPolygon().intersect(viewport)){
          visiblepolygons.add(polygon);
          prospects.addAll(getPolygonChildren(polygon));}}}
    //
    List<DPolygon> visibledpolygons=new ArrayList<DPolygon>();
    double detailsize;
    for(FPolygon p:visiblepolygons){
      detailsize=p.getDetailSize();
      if(detailsize>visiblepolygondetailsizefloor&&detailsize<visiblepolygondetailsizeceiling)
//      if(detailsize>visiblepolygondetailsizefloor)
        visibledpolygons.add(p.getDPolygon());}
    //
    return visibledpolygons;}
  
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
  
  private Jig getRandomJig(ForsythiaGrammar fg,FPolygon target){
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
//  private Color[] 
//    color0={new Color(236,208,120),new Color(217,91,67)},
//    color1={new Color(192,41,66),new Color(83,119,122)};
  
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

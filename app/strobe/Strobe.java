package org.fleen.forsythia.app.strobe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar0;
import org.fleen.geom_2D.DHexagon;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_2D.rasterMap.RasterMap;

/*
 * we're basically doing a drifter with constant viewport and transform
 * so this will look good and also be a good test for strobing drifter 
 * 
 * create composition
 * create random viewport
 * get polygons in viewport
 * create color patterns for all polygon signature groups
 * create raster map
 * create 60 frames (120,180...)
 * (then convert to 60 fps video with ffmpeg) 
 * (then add audio patterns. a drumbeat at every color-switch, or something)
 * 
 * for colors
 *   colorize by layer. do split pallete colorization. 2 palettes
 *   so each polygon has 2 colors that it flips between
 *   patterns go like XXYYXXYYXXYY
 *   or ... XXXXYYXXXXYYXXXXYY
 *   XYXYXYXYXYX
 *   XXXXXXXXYYYYXXXXXXXXYYYY
 *   etc
 *   
 * we render the whole composition, all layers, not just leaves
 * use full strength blend for polygons at all sizes and depths
 * so, for each pixel, we're seeing an intensity=1 blend of polygons of all layers who have presence there
 * some of the colors match, most don't 
 * 
 * 
 * --------------------------
 * 
 * create a blink pattern generator
 * 
 * we could add an element of noise, but that would be repeating noise so maybe not
 * 
 */

public class Strobe{
  
  public Strobe(){
    long t;
    t=System.currentTimeMillis();
    initComposition();
    System.out.println("composition init time = "+(System.currentTimeMillis()-t));
    initViewport();
    t=System.currentTimeMillis();
    initVisiblePolygons();
    System.out.println("visible polygons init time = "+(System.currentTimeMillis()-t));
    System.out.println("visible polygons count = "+visiblepolygons.size());
    t=System.currentTimeMillis();
    initPolygonRasterMap();
    System.out.println("rastermap init time = "+(System.currentTimeMillis()-t));
//    initBlinkPatterns();
    
    }
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  UI ui=null;
  
  private void initUI(){
    ui=new UI(this);}
  
  /*
   * ################################
   * COMPOSITION
   * ################################
   */
  
//  private static final String GRAMMARPATH="/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_05_10/g000_hexroot";
  //private static final String GRAMMARPATH="/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_06_03/g011_hexroot";
  private static final String GRAMMARPATH="/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_06_04/g003_hexroot_simple";
    
  private Composition composition;
  
  private void initComposition(){
    ForsythiaGrammar0 grammar=importGrammar(GRAMMARPATH);
    composition=new Composition(this,grammar);}
  
  private ForsythiaGrammar0 importGrammar(String grammarpath){
    File file=new File(grammarpath);
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar0 fg=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      fg=(ForsythiaGrammar0)ois.readObject();
      ois.close();
    }catch(Exception e){
      System.out.println("#^#^# EXCEPTION IN GRAMMAR FILE IMPORT #^#^#");
      e.printStackTrace();
      return null;}
    return fg;}
  
  /*
   * ################################
   * VIEWPORT
   * ################################
   */
  
  double 
    //viewport center, cartesian
    vpcenterx=0,vpcentery=0,
    //the rotation of the viewport. 
    //radians. 0 is north.
    vpforward=0,
    //a scaling factor. smaller zooms out, bigger zooms in. 1.0 is unscaled.
    //to get the scaled viewport polygon dimensions we multiply
    //the ui window dimensions by this
    vpscale=0.005;
  
  ViewportDef getViewportDef(){
    return new ViewportDef(IMAGEWIDTH,IMAGEHEIGHT,new DPoint(vpcenterx,vpcentery),vpscale,vpforward);
  }
  
  /*
   * remember, the root polygon is a hexagon
   */
  public void centerAndFit(){
    DPoint c=getRootHexagon().getCenter();
    vpcenterx=c.x;
    vpcentery=c.y;
    vpscale=getViewportFittingScale();}
  
  public void setViewportCenter(DPoint p){
    vpcenterx=p.x;
    vpcentery=p.y;}
  
  public void setViewportScale(double s){
    vpscale=s;}
  
  /*
   * random viewport
   */
  private void initViewport(){
    Random rnd=new Random();
    //do random center
    DHexagon roothex=getRootHexagon();
    DPoint roothexcenter=roothex.getCenter();
    double 
      hexradius=roothex.getInnerRadius(),
      centeroffset=hexradius/3;
    centeroffset+=rnd.nextDouble()*centeroffset;
    double centeroffsetdir=rnd.nextDouble()*GD.PI2;
    double[] c=GD.getPoint_PointDirectionInterval(roothexcenter.x,roothexcenter.y,centeroffsetdir,centeroffset);
    vpcenterx=c[0];
    vpcentery=c[1];
    //do random rotation
    vpforward=rnd.nextDouble()*GD.PI2;
    //do random scale
    vpscale=getViewportFittingScale()*(1.0/(rnd.nextInt(5)+2));//
    
    
  }
  
  /*
   * ################################
   * VIEWPORT POLYGON
   * ################################
   */
  
  DPolygon getViewportPolygon(){
    ViewportDef viewportdef=getViewportDef();
    
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
   * GEOMETRY
   * ################################
   */
  
  ////dimensions for a 720p rectangle
//  private static final int 
//    IMAGEWIDTH=1280,
//    IMAGEHEIGHT=720;
  
  //mug image
//  private static final int 
//  IMAGEWIDTH=2363,
//  IMAGEHEIGHT=863;
  
  //1080p
  private static final int 
  IMAGEWIDTH=1920,
  IMAGEHEIGHT=1080;
  
  private int 
    imagewidth=IMAGEWIDTH,
    imageheight=IMAGEHEIGHT;
  
  //private static final int 
  //WIDTH720P=128,
  //HEIGHT720P=72;
  
  //dimensions for a 1080i rectangle
  //private static final int 
  //  WIDTH1080i=1920,
  //  HEIGHT1080i=1080;
  
  DHexagon getRootHexagon(){
    return new DHexagon(composition.getRootPolygon().getDPolygon());}
  
  /*
   * assuming that the hexagon is centered, this is the scale to fit it there
   */
  public double getViewportFittingScale(){
    double hexspan=getRootHexagon().getRadius();
//    Container cp=ui.getContentPane();
    int cpw=IMAGEWIDTH,cph=IMAGEHEIGHT;
    double 
      s0=hexspan/cpw,
      s1=hexspan/cph;
    double scale=Math.min(s0,s1);
    return scale;}
  
  /*
   * ################################
   * RENDERER
   * ################################
   */
  
//  Renderer renderer=new R0(this);
  R1 renderer=new R1(this);
  
  /*
   * ################################
   * VISIBLE DETAIL FLOOR
   * A value compared with the detail size of a polygon
   * (detail size is the diameter of a polygon's incircle. The "size" of the polygon, basically)
   * 
   * We gather visible polygons
   * if a polygon has detail size larger than this then it is visible, 
   *   if the detail size is smaller than this then the polygon is not visible
   * ALSO
   * If a polygon has detail size larger than this then we CULTIVATE it, to create more (child) polygons 
   *   because we want to populate our view with a satisfying level of detail
   * 
   * we render all polygons with a detail size larger than this 
   * 
   * the visible detail size floor is defined in terms of the viewport scale
   * we keep the smallest detail size constant over different scales
   * 
   * 20 is low rez
   * 10 is medium
   * 6 is high
   * 
   * ################################
   */
  
//  private static final double 
//    VISIBLEPOLYGONDETAILSIZEFLOOR_SCALEFACTOR=6,
//    VISIBLEPOLYGONDETAILSIZECEILING_SCALEFACTOR=24;
  
  //lower rez for mug
//  private static final double 
//  VISIBLEPOLYGONDETAILSIZEFLOOR_SCALEFACTOR=12,
//  VISIBLEPOLYGONDETAILSIZECEILING_SCALEFACTOR=48;
  
  private static final double //just right detail size for mug
  VISIBLEPOLYGONDETAILSIZEFLOOR_SCALEFACTOR=16,
  VISIBLEPOLYGONDETAILSIZECEILING_SCALEFACTOR=48;
  
  double getVisiblePolygonDetailSizeFloor(){
    return vpscale*VISIBLEPOLYGONDETAILSIZEFLOOR_SCALEFACTOR;}
  
  double getVisiblePolygonDetailSizeCeiling(){
    return vpscale*VISIBLEPOLYGONDETAILSIZECEILING_SCALEFACTOR;}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  BufferedImage image;
  
  void render(int frameindex){
    long t=System.currentTimeMillis();
    image=renderer.getImage(frameindex);
    if(ui!=null)
      ui.repaint();
    System.out.println("render time = "+(System.currentTimeMillis()-t));}
  
  /*
   * ################################
   * VISIBLE POLYGONS
   * ################################
   */
  
  VisiblePolygons visiblepolygons;
  
  /*
   * get visible polygons up to and including root
   *   this gets us all the polygons that intersect the viewport AND are of detailszie greater than the floor
   * then trim off the big ones
   *   this gets rid of any polygons with detail size greater than the ceiling
   */
  private void initVisiblePolygons(){
    System.out.println("--------------------------------");
    System.out.println("getting visible polygons");
    long t=System.currentTimeMillis();
    visiblepolygons=new VisiblePolygons(composition,getViewportPolygon(),getVisiblePolygonDetailSizeFloor());
    System.out.println("time elapsed : "+(System.currentTimeMillis()-t));
    System.out.println("--------------------------------");
    System.out.println("--------------------------------");
    System.out.println("trimming overlarge polygons");
    Iterator<FPolygon> i=visiblepolygons.iterator();
    FPolygon fp;
    double vpdsc=getVisiblePolygonDetailSizeCeiling();
    t=System.currentTimeMillis();
    while(i.hasNext()){
      fp=i.next();
      if(fp.getDetailSize()>vpdsc)
        i.remove();}
    System.out.println("time elapsed : "+(System.currentTimeMillis()-t));
    System.out.println("--------------------------------");
    }
  
  List<DPolygon> getVisibleDPolygons(){
    List<DPolygon> dpolygons=new ArrayList<DPolygon>();
    for(FPolygon fp:visiblepolygons)
      dpolygons.add(fp.getDPolygon());
    return dpolygons;}
  
  /*
   * ################################
   * RASTER MAP
   * ################################
   */
  
  private static final double GLOWSPAN=1.5;
  RasterMap polygonrastermap;
  
  private void initPolygonRasterMap(){
    List<DPolygon> vdp=getVisibleDPolygons();
    polygonrastermap=new RasterMap(imagewidth,imageheight,getViewportDef().getTransform(),GLOWSPAN,vdp);}
  
  /*
   * ################################
   * EXPORT IMAGE
   * ################################
   */
  
  static final String EXPORTDIRPATH="/home/john/Desktop/strobeexport";
  RasterExporter exporter=new RasterExporter(new File(EXPORTDIRPATH));
  
  void exportImage(int index){
    exporter.export(image,index);}
  
  /*
   * ################################
   * TEST
   * ################################
   */
  
  public static final void main(String[] a){
    Strobe strobe;
    strobe=new Strobe();
    for(int i=0;i<60;i++){
      
//      strobe.initUI();
      System.out.println("render frame #"+i);
      strobe.render(i);
      strobe.exportImage(i);}
    System.out.println("### FRAME SEQUENCE FINISHED ###");}

}

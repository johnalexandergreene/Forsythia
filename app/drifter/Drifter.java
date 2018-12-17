package org.fleen.forsythia.app.drifter;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Random;

import org.fleen.forsythia.core.grammar.ForsythiaGrammar0;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;

/*
 * given
 *   a forsythia composition
 *   a viewport (centerx, centery, width, height, rotation, zoom)
 * cultivate all polygons within the viewport to whatever minimum detail size
 * render to image
 * paint viewport with image 
 * 
 * we could also tesselate the plane with our forsythia root polygon. 
 *   Repeat at edge or whatever. 
 *   Probably a hexagon.
 *   Then we'd never run off the edge of the map.
 *   or we could just stay inside the root polygon. whichever is easier
 *   
 * ----------------
 *   
 * Our root is definitely a hexagon
 * for now our panpath is a circle
 * 
 * ------------------
 * init the composition
 * invoke a frame generating method
 * 
 * foo foo foo
 * 
 */
public class Drifter{
  
  int bullshit;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Drifter(){
    initUI();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  DrifterWindow ui;
  
  private void initUI(){
    ui=new DrifterWindow(this);}
  
  /*
   * ################################
   * RENDERER COMPOSER
   * We build the composition just enough to satisfy the renderer
   *   the renderer renders a specific viewport geometry, location, scale, rotation
   *   it builds the composition down to the smallest visible geometry
   * So the renderer also composes
   * ################################
   */
  
  DrifterRendererComposer renderercomposer=null;
      
  DrifterRendererComposer getRendererComposer(){
    if(renderercomposer==null){
      
//      renderercomposer=new DRC0(composition); 
      renderercomposer=new DRC1_rasterizer(composition);
    
    }
    return renderercomposer;}

  private static final int 
    WIDTH720P=1280,
    HEIGHT720P=720,
    WIDTH1080P=1920,
    HEIGHT1080P=1080,
    WIDTH5K=5120,
    HEIGHT5K=2880;
  
  int visiblepolygonscount;
  BufferedImage image=null;
  
  void render(){
    image=getRendererComposer().getImage(getViewportDef(),getVisiblePolygonDetailSizeFloor(),getVisiblePolygonDetailSizeCeiling());
    ui.repaint();}
  
  /*
   * ################################
   * COMPOSITION
   * Root must be a hexagon
   * ################################
   */
  
  DrifterComposition composition;
  
  public void initComposition(String grammarpath){
    ForsythiaGrammar0 grammar=importForsythiaGrammarFromFile(new File(grammarpath));
    composition=new DrifterComposition(this,grammar);}
  
  private ForsythiaGrammar0 importForsythiaGrammarFromFile(File file){
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
  
  private ViewportDef getViewportDef(){
    return new ViewportDef(WIDTH1080P,HEIGHT1080P,new DPoint(vpcenterx,vpcentery),vpscale,vpforward);}
  
  /*
   * remember, the root polygon is a hexagon
   */
  public void centerAndFit(){
    DPoint c=getCompositionHexagonCenter();
    vpcenterx=c.x;
    vpcentery=c.y;
    vpscale=getViewportFittingScale();}
  
  public void setViewportCenter(DPoint p){
    vpcenterx=p.x;
    vpcentery=p.y;}
  
  public void setViewportScale(double s){
    vpscale=s;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  DPolygon getRootHexagon(){
    return composition.getRootPolygon().getDPolygon();}
  
  public DPoint getCompositionHexagonCenter(){
    DPolygon hex=getRootHexagon();
    DPoint p0=hex.get(0),p1=hex.get(3);
    double[] center=GD.getPoint_Mid2Points(p0.x,p0.y,p1.x,p1.y);
    return new DPoint(center);}
  
  /*
   * point to point
   * multiply by 2/sqrt3 to get the flat to flat span
   */
  public double getCompositionHexagonSpan(){
    DPolygon hex=getRootHexagon();
    DPoint p0=hex.get(0),p1=hex.get(3);
    double hexspan=p0.getDistance(p1);
    return hexspan;}
  
  /*
   * assuming that the hexagon is centered, this is the scale to fit it there
   */
  public double getViewportFittingScale(){
    double hexspan=getCompositionHexagonSpan()*2.0/GD.SQRT3;
    Container cp=ui.getContentPane();
    int cpw=cp.getWidth(),cph=cp.getHeight();
    double 
      s0=hexspan/cpw,
      s1=hexspan/cph;
    double scale=Math.min(s0,s1);
    return scale;}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  /*
   * for debug
   */
  public void printStats(){
//    System.out.println("VISIBLE POLYGONS COUNT = "+visiblepolygonscount);
    System.out.println("SCALE = "+vpscale);}
  
  private void pause(long ms){
    try{
      Thread.sleep(ms,0);
    }catch(Exception x){
      x.printStackTrace();}}
  
  /*
   * ################################
   * EXPORT IMAGE
   * ################################
   */
  
  static final String EXPORTDIRPATH="/home/john/Desktop/drifter_export";
  RasterExporter exporter=new RasterExporter(new File(EXPORTDIRPATH));
  
  void exportImage(int index){
    exporter.export(image,index);}
  
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
  
  /*
   * these go with the image resolution
   */
 //for use with 1080p
  private static final double 
    VISIBLEPOLYGONDETAILSIZEFLOOR_SCALEFACTOR=12,
    VISIBLEPOLYGONDETAILSIZECEILING_SCALEFACTOR=48;
  
  //for use with 5k
//  private static final double 
//    VISIBLEPOLYGONDETAILSIZEFLOOR_SCALEFACTOR=36,
//    VISIBLEPOLYGONDETAILSIZECEILING_SCALEFACTOR=144;
  
  private double getVisiblePolygonDetailSizeFloor(){
    return vpscale*VISIBLEPOLYGONDETAILSIZEFLOOR_SCALEFACTOR;}
  
  private double getVisiblePolygonDetailSizeCeiling(){
    return vpscale*VISIBLEPOLYGONDETAILSIZECEILING_SCALEFACTOR;}
  
  /*
   * ################################
   * ++++++++++++++++++++++++++++++++
   * ################################
   * 
   * FRAME SEQUENCE GENERATION
   * 
   * Each of these methods generates a squence of frames to be compiled into a video
   * We incrementally traverse the geometry; panning, zooming, rotating
   *   grab a frame at every increment
   *   
   * ################################
   * ++++++++++++++++++++++++++++++++
   * ################################
   */
  
  private static final long FRAMEPAUSE=20;//ms
  
  /*
   * ################################
   * CIRCULAR PANNING ROTATING SINE ZOOM
   * ################################
   */
  
  Random rnd=new Random();
  
  /*
   * in theory this is smooth looping, in practice it has a little jump on the end
   */
  public void gFS_CircularPanningRotatingSineZoom(boolean exportframes){
    setViewportCenter(getCompositionHexagonCenter());
    double scale=getViewportFittingScale()/30;
    setViewportScale(scale);
    DPoint cc=getCompositionHexagonCenter();
    double[] vc;
    double
      forward,
      r=getCompositionHexagonSpan()/4;
    
    double 
      initforward=rnd.nextDouble()*GD.PI2,
      zoomsinamplitude=rnd.nextDouble()*0.004+0.002;
    
    int increments=12000;
    for(int i=0;i<500;i++){
      System.gc();
      System.out.println("FRAME#"+i);
      forward=initforward+(((double)i)/((double)increments)*GD.PI2);
      vc=GD.getPoint_PointDirectionInterval(cc.x,cc.y,forward,r);
      //pan
      setViewportCenter(new DPoint(vc));
      //rotate
      vpforward=initforward+(GD.PI2*(((double)i*2)/increments));
      //zoom
      vpscale=getIncrementedScale(i,increments,zoomsinamplitude);
      render();
     if(exportframes)
       exportImage(i);
     else 
       pause(FRAMEPAUSE);}
    System.out.println("===FINISHED===");}
  
  private double getIncrementedScale(double i,double increments,double amplitude){
    //get position in range [0,1]
    double a=i/increments;
    //in terms of 2pi
    double b=a*GD.PI2;
    //multiply by cyclecount, multiple waves
    double c=b*6;
    //get sine
    double d=Math.sin(c);
    
    d=1.0+d*amplitude;//wave about 1.0 at amplitude
    //
    return vpscale*d;}
  
  /*
   * ################################
   * dive rotate
   * ################################
   */
  
  /*
   * find a nice place to start
   *   left of center, midpoint between center and edge, foreward=north, zoom is fitting/3 or something
   * adjust foreward, adjust scale, move foreward a scaled increment, repeat
   */
  public void gFS_RotatingDive(boolean exportframes){
    setViewportScale(getViewportFittingScale()/20);
    DPoint center=getCompositionHexagonCenter();
    double d=getCompositionHexagonSpan();
    double[] initvpcenter=GD.getPoint_PointDirectionInterval(center.x,center.y,GD.PI2*0.75,d/4);
    setViewportCenter(new DPoint(initvpcenter));
    //move incrementally and do whatever
    double 

//    scaleincrementfactor=0.981,//100mph. this becomes choppy upon editing at michael's end. gonna slow it down and try to achieve smoothness
//    scaleincrementfactor=0.986,//80mph
//      scaleincrementfactor=0.993,//60mph
      scaleincrementfactor=0.995,//40mph

//      unscaledforewardincrement=1,
////      rotateincrement=GD.PI2/2000;//cw
//      rotateincrement=-GD.PI2/2000;//ccw
      
      rotateincrement=0.0003*GD.PI2;//cw
//      rotateincrement=-GD.PI2/2000;//ccw
    double[] p;
    for(int i=0;i<500;i++){
      System.out.println("FRAME#"+i);
      vpscale=vpscale*scaleincrementfactor;
      vpforward=GD.normalizeDirection(vpforward+rotateincrement);
//      p=GD.getPoint_PointDirectionInterval(vpcenterx,vpcentery,vpforward,unscaledforewardincrement*vpscale);
//      vpcenterx=p[0];
//      vpcentery=p[1];
      render();
      if(exportframes)
        exportImage(i);
      else 
        pause(FRAMEPAUSE);}
    System.out.println("===FINISHED===");}
  
  /*
   * ################################
   * swoop
   * dive with straight pan. no rotate
   * ################################
   */
  
  /*
   * find a nice place to start
   *   left of center, midpoint between center and edge, foreward=north, zoom is fitting/3 or something
   * adjust foreward, adjust scale, move foreward a scaled increment, repeat
   */
  public void gFS_Swoop(boolean exportframes){
    setViewportScale(getViewportFittingScale()/20);
    DPoint center=getCompositionHexagonCenter();
    double d=getCompositionHexagonSpan();
    double[] initvpcenter=GD.getPoint_PointDirectionInterval(center.x,center.y,GD.PI2*0.75,d/4);
    setViewportCenter(new DPoint(initvpcenter));
    //move incrementally and do whatever
    double 

//    scaleincrementfactor=0.981,//100mph. this becomes choppy upon editing at michael's end. gonna slow it down and try to achieve smoothness
//    scaleincrementfactor=0.986,//80mph
      scaleincrementfactor=0.993,//60mph
      vpforward=GD.HALFPI,
      unscaledforewardincrement=8;
    double[] p;
    for(int i=0;i<500;i++){
      System.out.println("FRAME#"+i);
      vpscale=vpscale*scaleincrementfactor;
      p=GD.getPoint_PointDirectionInterval(vpcenterx,vpcentery,vpforward,unscaledforewardincrement*vpscale);
      vpcenterx=p[0];
      vpcentery=p[1];
      render();
      if(exportframes)
        exportImage(i);
      else 
        pause(FRAMEPAUSE);}
    System.out.println("===FINISHED===");}
  
  /*
   * ################################
   * slide
   * straight pan
   * ################################
   */
  
  /*
   * find a nice place to start
   *   left of center, midpoint between center and edge, foreward=north, zoom is fitting/3 or something
   * adjust foreward, adjust scale, move foreward a scaled increment, repeat
   */
  public void gFS_Slide(boolean exportframes){
    setViewportScale(getViewportFittingScale()/20);
    DPoint center=getCompositionHexagonCenter();
    double d=getCompositionHexagonSpan();
    double[] initvpcenter=GD.getPoint_PointDirectionInterval(center.x,center.y,GD.PI2*0.75,d/4);
    setViewportCenter(new DPoint(initvpcenter));
    //move incrementally and do whatever
    double 
      vpforward=GD.HALFPI,
//      vpforward=0,
      unscaledforewardincrement=3;
    double[] p;
    for(int i=0;i<500;i++){
      System.out.println("FRAME#"+i);
      p=GD.getPoint_PointDirectionInterval(vpcenterx,vpcentery,vpforward,unscaledforewardincrement*vpscale);
      vpcenterx=p[0];
      vpcentery=p[1];
      render();
      if(exportframes)
        exportImage(i);
      else 
        pause(FRAMEPAUSE);}
    System.out.println("===FINISHED===");}
    
  /*
   * ################################
   * TEST
   * GENERATE A FRAME SEQUENCE
   * ################################
   */
  
  //private static final String GRAMMARPATH="/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_04_14/g004";
  private static final String GRAMMARPATH="/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_06_04/g002_hexroot_simple";
  
  public static final void main(String[] a){
    Drifter drifter=new Drifter();
    drifter.initComposition(GRAMMARPATH);
    drifter.gFS_RotatingDive(true);
//    drifter.gFS_CircularPanningRotatingSineZoom(true);
//    drifter.gFS_Swoop(true);
//    drifter.gFS_Slide(true);
    
  }
  
}

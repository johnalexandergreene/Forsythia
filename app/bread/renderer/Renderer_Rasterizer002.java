package org.fleen.forsythia.app.bread.renderer;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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

/*
 * this renders first egg in ancestry
 */
public class Renderer_Rasterizer002 implements Renderer{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Renderer_Rasterizer002(Color[] color0,Color[] color1){
    this.color0=color0;
    this.color1=color1;
    }
  
  /*
   * ################################
   * CREATE IMAGE
   * ################################
   */
  
  private static final double GLOWSPAN=1.5;
  
  private Color[] color0,color1;
  
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
    System.out.println("+++init polygon colors+++");
    initPolygonColors();
    System.out.println("+++render cells+++");
    for(Cell c:prm)
      image.setRGB(c.x,c.y,getPixelColor(c).getRGB());
    //
    return image;}
  
  Random rnd=new Random();
  
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
   * POLYGON COLOR
   * 
   * we init all of the polygon colors at once
   * 
   * start at root
   * 
   * put root in polygons0 list
   * 
   * for each polygon in polygons0
   *   if isbase then set polygon.color=getNewPolygonColorForBase
   *   otherwise get color from parent
   *   put color in colorsbypolygon
   *   add all polygon.getChildren to polygons1
   *   
   * for each polygon in polygons1, repeat and so on
   * 
   * after we're done, randomize a few of the base colors and reset their children, or something, because we need a little chaos
   * 
   * ---
   * 
   * isBase
   *   if a polygon is rootpolygon
   *   if a polygon is first gen child of root
   *   if a polygon is first gen child of egg
   *   if a polygon is leaf egg
   *   
   * ---
   * 
   * getNewPolygonColorForBase
   *   check colorsbysig
   *   if we have a color there then use that
   *   get list of possible colors
   *   remove from that list the color of the base's parent
   *   from that list pick a color at random. that's our color.
   * 
   * ################################
   */
  
  private static final Color[] C2=new Color[]{
      new Color(236,208,120),
      new Color(217,91,67),
      new Color(192,41,66),
      new Color(84,36,55),
      new Color(83,119,122),
    };
  
    Map<FPolygonSignature,Color> colorbysig=new HashMap<FPolygonSignature,Color>();
    Map<FPolygon,Color> colorbypolygon=new HashMap<FPolygon,Color>();
    
    private void initPolygonColors(){
      colorbysig.clear();
      colorbypolygon.clear();
      List<TreeNode> 
        polygons0=new ArrayList<TreeNode>(),
        polygons1=new ArrayList<TreeNode>();
      polygons0.add(composition.getRootPolygon());
      while(!polygons0.isEmpty()){
        for(TreeNode polygon:polygons0){
          if(isBase((FPolygon)polygon))
            initColorForPolygon((FPolygon)polygon);
          else
            colorbypolygon.put((FPolygon)polygon,colorbypolygon.get(((FPolygon)polygon).getPolygonParent()));
          polygons1.addAll(((FPolygon)polygon).getPolygonChildren());}
        polygons0.clear();
        polygons0.addAll(polygons1);
        polygons1.clear();}}
    
    private boolean isBase(FPolygon polygon){
      if(polygon.isRootPolygon())return true;
      if(polygon.getPolygonParent().isRootPolygon())return true;
      if(polygon.getPolygonParent().hasTags("egg"))return true;
      if(polygon.isLeaf()&&polygon.hasTags("egg"))return true;
      return false;}
    
    private void initColorForPolygon(FPolygon polygon){
      FPolygonSignature sig=polygon.getSignature();
      Color color=colorbysig.get(sig);
      if(color==null){
        List<Color> prospects=new ArrayList<Color>(Arrays.asList(C2));
        FPolygon base=getBase(polygon);
        if(base!=polygon)
          prospects.remove(colorbypolygon.get(base));
        color=prospects.get(rnd.nextInt(prospects.size()));}
      colorbypolygon.put(polygon,color);
      colorbysig.put(sig,color);
      
    }
    
    private FPolygon getBase(FPolygon polygon){
      while(!isBase(polygon))
        polygon=polygon.getPolygonParent();
      return polygon;}
    
    
    
    private void initPolygonColor(FPolygon polygon){
      Color c=selectColorForPolygon(polygon);
      colorbypolygon.put(polygon,c);}
    
    private Color selectColorForPolygon(FPolygon polygon){
      //get the list of prospective colors
      List<Color> colors=new ArrayList<Color>(Arrays.asList(C2));
      colors.remove(colorbysig.get(getSecondAncestralEgg(polygon).getSignature()));
      //a little fine chaos
      //leaf color randomness, makes little thingies
      if(rnd.nextDouble()>0.97)return colors.get(rnd.nextInt(colors.size()));
      //we consult the first ancestral egg
      //or first child thereof, for a little particoloredness
      //try the table for same sig, for symmetry
//      polygon=getFirstAncestralEgg(polygon);
      polygon=getFirstChildOfFirstAncestralEgg(polygon);
      FPolygonSignature sig=polygon.getSignature();
      Color color=null;
      //check the table, or not for another kind of chaos. Bigger Thingies
      if(rnd.nextDouble()<0.98)color=colorbysig.get(sig);
      //pick at random from remainder and store it
      if(color==null){
        color=C2[rnd.nextInt(C2.length)];
        colorbysig.put(sig,color);}
      return color;}
    
    private FPolygon getFirstChildOfFirstAncestralEgg(FPolygon p){
      if(p.hasTags("egg"))return p;
      FPolygon prior=null;
      while(!(p.isRootPolygon()||p.hasTags("egg"))){
        prior=p;
        p=p.getPolygonParent();}
      if(prior!=null)return prior;
      return p;}
    
    /*
     * if this is polygon is an egg then it is the first ancestral egg 
     */
    private FPolygon getFirstAncestralEgg(FPolygon p){
      while(!(p.isRootPolygon()||p.hasTags("egg"))){
        p=p.getPolygonParent();
      }
      return p;}
    
    private FPolygon getSecondAncestralEgg(FPolygon p){
      while(!(p.isRootPolygon()||p.hasTags("egg"))){
        p=p.getPolygonParent();}
      //
      if(p.isRootPolygon())return p;
      //
      p=p.getPolygonParent();
      while(!(p.isRootPolygon()||p.hasTags("egg"))){
        p=p.getPolygonParent();}
      return p;}
  
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
      color=colorbypolygon.get((FPolygon)p.polygon.object);
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

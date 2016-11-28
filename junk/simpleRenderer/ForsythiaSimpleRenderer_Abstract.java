package org.fleen.forsythia.junk.simpleRenderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;
import org.fleen.util.tree.TreeNode;

public abstract class ForsythiaSimpleRenderer_Abstract implements ForsythiaSimpleRenderer,Serializable{
  
  private static final long serialVersionUID=-7598234840029071967L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public ForsythiaSimpleRenderer_Abstract(Color backgroundcolor,int margin){
    this.backgroundcolor=backgroundcolor;
    this.margin=margin;}
  
  public ForsythiaSimpleRenderer_Abstract(){
    backgroundcolor=BACKGROUNDCOLOR_DEFAULT;
    margin=MARGIN_DEFAULT;}
  
  /*
   * ################################
   * BACKGROUND COLOR
   * ################################
   */
  
  public static final Color BACKGROUNDCOLOR_DEFAULT=new Color(255,255,255);
  
  protected Color backgroundcolor;
  
  /*
   * ################################
   * MARGIN
   * ################################
   */
  
  public static final int MARGIN_DEFAULT=20;
  
  protected int margin;
  
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
    pathbypolygon.clear();
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
  
  Map<FPolygon,Path2D> pathbypolygon=new Hashtable<FPolygon,Path2D>();
  
  protected Path2D getPath2D(FPolygon polygon){
    Path2D path=pathbypolygon.get(polygon);
    if(path==null){
      path=createPath2D(polygon);
      pathbypolygon.put(polygon,path);}
    return path;}
  
  private Path2D createPath2D(FPolygon polygon){
    Path2D.Double path=new Path2D.Double();
    List<DPoint> points=polygon.getDPolygon();
    DPoint p=points.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}
  
  /*
   * ================================
   * TAG LEVEL
   * 
   * returns the number of times the specified tag is encountered in this polygon's ancestry, including this polygon
   * examples :
   *   if the tag exists only once in the ancestry then the tagdepth is 1
   *   if the tag exists in this polygon and its grandparent's polygon then the tagdepth is 2
   *   if the tag exists nowhere in this polygon's ancestry then the tagdepth is 0
   *   
   * for example we sometimes use it to get "egg" level.
   * An egg is a polygon that has been completely divorced from its paren't polygon's edge.
   *   That is to say, an interior shape.
   *   
   * ================================
   */
  
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
  
  /*
   * ################################
   * SERIALIZATION UTIL
   * export this instance to a file
   * ################################
   */
  
  public void serialize(){
    File f=getExportFile();
    writeExportFile(this,f);}
  
  public void serialize(String dirpath){
    File f=new File(dirpath+"/"+this.getClass().getSimpleName());
    writeExportFile(this,f);}
  
  private File getExportFile(){
    JFileChooser fc=new JFileChooser();
    fc.setCurrentDirectory(null);
    if(fc.showSaveDialog(null)!=JFileChooser.APPROVE_OPTION)return null;
    return fc.getSelectedFile();}
  
  private void writeExportFile(Serializable renderer,File file){
    FileOutputStream fos;
    ObjectOutputStream oot;
    try{
      fos=new FileOutputStream(file);
      oot=new ObjectOutputStream(fos);
      oot.writeObject(renderer);
      oot.close();
    }catch(IOException ex){
      System.out.println("%-%-% EXCEPTION IN SERIALIZE %-%-%");
      ex.printStackTrace();}}
  
}

package org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.geom_Kisrhombille.KYard;

/*
 * KGeometry elements converted to 2d elements for view and interaction
 * cached here for general service
 * also makes things faster  
 */
public class ViewGeometryCache{
  
  /*
   * ################################
   * DATA CONTROL
   * ################################
   */
  
  private Map<KVertex,double[]> p2dbykvertex=new Hashtable<KVertex,double[]>();
  private int 
    viewwidth=-1,
    viewheight=-1;
  private double 
    viewscale=-1,
    viewcenterx=Double.MIN_VALUE,
    viewcentery=Double.MIN_VALUE;
    
  /*
   * check params against presrnt values. If any changed then cache is invalidated
   */
  public void update(int w,int h,double scale,double centerx,double centery){
    if(
      w!=viewwidth||
      h!=viewheight||
      scale!=viewscale||
      centerx!=viewcenterx||
      centery!=viewcentery){
      invalidate();
      viewwidth=w;
      viewheight=h;
      viewscale=scale;
      viewcenterx=centerx;
      viewcentery=centery;}}
  
  private void invalidate(){
    System.out.println("invalidate display geometry cache");
    p2dbykvertex.clear();
    path2dbykpolygon.clear();
    path2dbykyard.clear();}
  
  /*
   * ################################
   * CONVERT KVERTEX TO POINT2D
   * ################################
   */
  
  public double[] getPoint(KVertex v){
    double[] p=p2dbykvertex.get(v);
    if(p==null){
      p=convertGridVertexToViewPoint(v);
      p2dbykvertex.put(v,p);}
    return p;}
  
  private double[] convertGridVertexToViewPoint(KVertex vertex){
    //get basic 2d point for the vertex
    double[] p=GK.getBasicPoint2D_Vertex(vertex.coors);
    //adjust vertex coors for for view center
    p[0]-=viewcenterx;
    p[1]-=viewcentery;
    //adjust for scale
    p[0]*=viewscale;
    p[1]*=viewscale;
    //adjust y for flipped coors
    p[1]=viewheight-p[1];
    //adjust for image center
    p[0]+=viewwidth/2;
    p[1]-=viewheight/2;
    //
    return p;}
  
  /*
   * ################################
   * POLYGON PATH
   * ################################
   */
  
  private Map<KPolygon,Path2D> path2dbykpolygon=new Hashtable<KPolygon,Path2D>();
  
  public Path2D getPath(KPolygon p){
    Path2D path=path2dbykpolygon.get(p);
    if(path==null){
      path=createPath(p);
      path2dbykpolygon.put(p,path);}
    return path;}
  
  private Path2D createPath(KPolygon kp){
    int pointcount=kp.size();
    Path2D.Double path=new Path2D.Double();
    //we set this to be uniform with multiedge path winding rule (yard) (see below)
    //maybe unnecessary, but don't mess with it.
    path.setWindingRule(Path2D.WIND_EVEN_ODD);
    double[] p=getPoint(kp.get(0));
    path.moveTo(p[0],p[1]);
    for(int i=1;i<pointcount;i++){
      p=getPoint(kp.get(i));
      path.lineTo(p[0],p[1]);}
    path.closePath();
    return path;}  
  
  /*
   * ################################
   * YARD PATH
   * ################################
   */
  
  private Map<KYard,Path2D> path2dbykyard=new Hashtable<KYard,Path2D>();
  
  public Path2D getPath(KYard y){
    Path2D path=path2dbykyard.get(y);
    if(path==null){
      path=createPath(y);
      path2dbykyard.put(y,path);}
    return path;}
  
  private Path2D createPath(KYard y){
    Path2D 
      path=new Path2D.Double(),
      ppath;
    //THIS IS IMPORTANT
    //if we don't set the winding rule to even odd then the default, nonzero, makes the 
    //holes not render right. It fills them in.
    path.setWindingRule(Path2D.WIND_EVEN_ODD);
    //
    for(KPolygon p:y){
      ppath=createPath(p);
      path.append(ppath,false);}
//    new TestFrame0(path);
    return path;} 
  
  /*
   * ################################
   * TEST
   * ################################
   */
  
  /*
   * make a polygon and a point
   * create a pointdef
   * leave polygon unaltered. set point location according to pointdef
   * should do nothing to the geometry
   * illustrate this on a JFrame
   */
//  public static final void main(String[] a){
//
//    //create display
//    TestFrame testframe=new TestFrame();
//    testframe.repaint();}
//  
//  @SuppressWarnings("serial")
//  static class TestFrame extends JFrame{
//    
//    TestFrame(){
//      super();
//      
//      setSize(800,800);
//      setVisible(true);
//      setBackground(Color.white);}
//    
//    public void paint(Graphics g){
//      Graphics2D g2d=(Graphics2D)g;
//      
//      Path2D pathsquare=new Path2D.Double();
//      pathsquare.moveTo(40,40);
//      pathsquare.lineTo(500,40);
//      pathsquare.lineTo(500,500);
//      pathsquare.lineTo(40,500);
//      pathsquare.closePath();
//      
//      Path2D pathtri=new Path2D.Double();
//      pathtri.moveTo(100,100);
//      pathtri.lineTo(200,100);
//      pathtri.lineTo(200,200);
//      pathtri.closePath();
//      
//      Path2D path=new java.awt.geom.Path2D.Double();
//      path.setWindingRule(Path2D.WIND_EVEN_ODD);
//      path.append(pathsquare,false);
//      path.append(pathtri,false);
//      
//      g2d.setPaint(new Color(255,0,0));
//      g2d.fill(path);
//      
//    }}
  
  @SuppressWarnings("serial")
  class TestFrame0 extends JFrame{
    Path2D path;
    
    TestFrame0(Path2D path){
      super();
      this.path=path;
      setSize(800,800);
      setVisible(true);
      setBackground(Color.white);}
    
    public void paint(Graphics g){
      Graphics2D g2d=(Graphics2D)g;
      g2d.setPaint(new Color(255,0,0));
      g2d.fill(path);}}

}

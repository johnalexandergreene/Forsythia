package org.fleen.forsythia.app.grammarEditor.project.metagon;

import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * KGeometry elements converted to 2d elements for view and interaction
 * cached here for general service
 * also makes things faster
 * 
 * we cache vertices, segs and polygons
 */
public class MetagonEditorGeometryCache implements Serializable{
  
  private static final long serialVersionUID=-1760731649711575167L;
  
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
  
  public void invalidate(){
    System.out.println("invalidate display geometry cache");
    p2dbykvertex.clear();
    path2dbykpolygon.clear();}
  
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
  
  public DPolygon getDPolygon(KPolygon polygon){
    int s=polygon.size();
    DPolygon dpolygon=new DPolygon(s);
    for(int i=0;i<s;i++)
      dpolygon.add(new DPoint(getPoint(polygon.get(i))));
    return dpolygon;}
  
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
  
  public Path2D getPath(KPolygon polygon){
    Path2D path=path2dbykpolygon.get(polygon);
    if(path==null){
      path=createPath(polygon);
      path2dbykpolygon.put(polygon,path);}
    return path;}
  
  private Path2D createPath(KPolygon kp){
    int pointcount=kp.size();
    Path2D.Double path=new Path2D.Double();
    double[] p=getPoint(kp.get(0));
    path.moveTo(p[0],p[1]);
    for(int i=1;i<pointcount;i++){
      p=getPoint(kp.get(i));
      path.lineTo(p[0],p[1]);}
    path.closePath();
    return path;}  
  

}

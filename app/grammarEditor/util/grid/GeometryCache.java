package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * 2D Geometry Cache for use with the grid
 * invalidates when the viewport changes
 * Maps 
 *   KVertices to Point2Ds
 *   KPolygons to Path2Ds
 * 
 * cached here for general service
 * also makes things faster
 *   
 */
public class GeometryCache{
  
  GeometryCache(Grid grid){
    this.grid=grid;}
  
  /*
   * ################################
   * GRID
   * ################################
   */
  
  Grid grid;  
  
  /*
   * ################################
   * STORAGE CONTROL
   * ################################
   */
  
  public void clear(){
    System.out.println("clear geometry cache");
    p2dbykvertex.clear();
    path2dbykpolygon.clear();}
  
  /*
   * ################################
   * KVERTICES AND POINT2DS
   * ################################
   */
  
  private Map<KVertex,DPoint> p2dbykvertex=new Hashtable<KVertex,DPoint>();
  
  public Iterator<DPoint> getPoint2DIterator(){
    return p2dbykvertex.values().iterator();}
  
  public List<DPoint> getPoint2Ds(KPolygon p){
    List<DPoint> points=new ArrayList<DPoint>();
    for(KVertex v:p)
      points.add(getPoint2D(v));
    return points;}
  
  public List<DPoint> getPoint2Ds(List<KVertex> vertices){
    List<DPoint> points=new ArrayList<DPoint>();
    for(KVertex v:vertices)
      points.add(getPoint2D(v));
    return points;}
  
  public DPoint getPoint2D(KVertex v){
    DPoint p=p2dbykvertex.get(v);
    if(p==null){
      p=createPoint2D(v);
      p2dbykvertex.put(v,p);}
    return p;}
  
  private DPoint createPoint2D(KVertex vertex){
    double 
      viewwidth=grid.getWidth(),
      viewheight=grid.getHeight();
    //get basic 2d point for the vertex
    DPoint p=new DPoint(GK.getBasicPoint2D_Vertex(vertex.coors));
    //adjust vertex coors for for view center
    p.x-=grid.viewcenterx;
    p.y-=grid.viewcentery;
    //adjust for scale
    p.x*=grid.viewscale;
    p.y*=grid.viewscale;
    //adjust y for flipped coors
    p.y=viewheight-p.y;
    //adjust for image center
    p.x+=viewwidth/2;
    p.y-=viewheight/2;
    //
    return p;}
  
  /*
   * ################################
   * POLYGON PATH
   * ################################
   */
  
  private Map<KPolygon,Path2D> path2dbykpolygon=new Hashtable<KPolygon,Path2D>();
  
  public Path2D getPath2D(KPolygon p){
    Path2D path=path2dbykpolygon.get(p);
    if(path==null){
      path=createPath2D(p);
      path2dbykpolygon.put(p,path);}
    return path;}
  
  private Path2D createPath2D(KPolygon kp){
    int pointcount=kp.size();
    Path2D.Double path=new Path2D.Double();
    DPoint p=getPoint2D(kp.get(0));
    path.moveTo(p.x,p.y);
    for(int i=1;i<pointcount;i++){
      p=getPoint2D(kp.get(i));
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}  

}

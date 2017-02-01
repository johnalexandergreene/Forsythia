package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

public class UIGridUtil implements Serializable{
  
  private static final long serialVersionUID=-8363625515185944702L;

  /*
   * ################################
   * CONVERT PORT COORDINATES TO VIEW COORDINATES
   * use the viewer transform params (specified) to get the view point from the panel point
   * TODO this should be done with an affinetransform
   * ################################
   */
  
//  public static final double[] convertPortCoordinatesXYToViewCoordinatesXY(
//      double portwidth,double portheight,//dimensions of the view port rectangle
//      double px,double py,//coordinates of the point
//      double viewscale,//view scale
//      double viewcenterx,double viewcentery){//view center
//    //convert coordinates to standard cartesian with origin at view center
//    px=px-(portwidth/2.0);
//    py=-(py-(portheight/2.0));
//    //adjust for scale and viewcenter
//    px/=viewscale;
//    py/=viewscale;
//    px+=viewcenterx;
//    py+=viewcentery;
//    return new double[]{px,py};
//    }
  
  /*
   * ################################
   * CONVERT PORT COORDINATES TO GRID VERTEX COORDINATES
   * transform port coordinates to view coordinates
   * then convert view coordinates to vertex coordinates
   * ################################
   */
  
//  public static final KVertex getKVertex(
//    double portwidth,double portheight,//dimensions of the port rectangle
//    double px,double py,//coordinates of the point
//    double viewscale,//grid view scale
//    double viewcenterx,double viewcentery,//grid view center offsets
//    double margin){//margin of error, outside which the point is not close enough. Unscaled.
//    double[] p=convertPortCoordinatesXYToViewCoordinatesXY(
//      portwidth,portheight,px,py,viewscale,viewcenterx,viewcentery);
//    KVertex v=convertPointToVertex(p[0],p[1],margin/viewscale);
//    return v;}
  
  /*
   * ################################
   * CONVERT GRID VERTEX COORDINATES TO VIEW COORDINATES
   * ################################
   */
  
  public static final List<double[]> convertGridVerticesToViewPoints(
    List<KVertex> vertices,int viewwidth,int viewheight,double viewscale,double viewcenterx,double viewcentery){
    List<double[]> a=new ArrayList<double[]>(vertices.size());
    for(KVertex vertex:vertices)
      a.add(convertGridVertexToViewPoint(vertex,viewwidth,viewheight,viewscale,viewcenterx,viewcentery));
    return a;}
  
  public static final Map<KVertex,double[]> mapKVerticesToViewPoints(
    Collection<KVertex> vertices,int viewwidth,int viewheight,double viewscale,double viewcenterx,double viewcentery){
    Map<KVertex,double[]> a=new Hashtable<KVertex,double[]>();
    for(KVertex kv:vertices)
      a.put(kv,convertGridVertexToViewPoint(kv,viewwidth,viewheight,viewscale,viewcenterx,viewcentery));
    return a;}
  
  public static final double[] convertGridVertexToViewPoint(
    KVertex vertex,int viewwidth,int viewheight,double viewscale,double viewcenterx,double viewcentery){
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
   * POLYGON
   * ################################
   */
  
  public static final Rectangle2D.Double getPolygonBounds2D(KPolygon polygon){
    DPolygon points=polygon.getDefaultPolygon2D();
    double maxx=Double.MIN_VALUE,maxy=maxx,minx=Double.MAX_VALUE,miny=minx;
    for(DPoint p:points){
      if(minx>p.x)minx=p.x;
      if(miny>p.y)miny=p.y;
      if(maxx<p.x)maxx=p.x;
      if(maxy<p.y)maxy=p.y;}
    return new Rectangle2D.Double(minx,miny,maxx-minx,maxy-miny);}

}

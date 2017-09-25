package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.geom.Path2D;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KPoint;

/*
 * This is a kpolygon with a bunch of validation stuff
 * we use it in the metagon editor and jig editor.
 * It is created by drawing
 * We have a tests for validity and completeness 
 */

public class DrawingPolygon extends KPolygon{
  
  private static final long serialVersionUID=-3518968791640299457L;
  
  public boolean closed=false;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public DrawingPolygon(){
    super();}
  
  public DrawingPolygon(KPolygon p){
    super(p);}
  
  /*
   * ################################
   * CLEAR
   * ################################
   */
  
  public void clear(){
    super.clear();
    closed=false;}
  
  /*
   * ################################
   * TOUCH VERTEX
   * ################################
   */
  
  /*
   * if this polygon does not contain v then add v to this polygon on the condition
   * that the addition does not produce invalid geometry
   * If the polygon has at least 2 vertices and v equals vertex0 then close the polygon
   */
  public void touchVertex(KPoint v){
    if((!closed)&&(v!=null)&&isValid(v)){
      if(size()>=3&&v.equals(get(0))){
        closed=true;
        return;}
      //
      if(contains(v))
        remove(v);
      else
        add(v);}}
  
  /*
   * ################################
   * NEW VERTEX VALIDATION
   * ################################
   */
  
  /*
   * TODO
   * a valid vertex is 
   * colinear with the prior vertex
   * does not create a line that intersect any other line
   */
  boolean isValid(KPoint v){
    return true;
  }
  
  /*
   * summation of all validation tests
   */
//  public boolean isValid(){
//    return 
//      has3Vertices()&&
//      hasNoNoncolinearAdjacentVertices()&&
//      hasNoIntersectingLines();}
  
  /*
   * a polygon must have at least 3 vertices
   */
  public boolean has3Vertices(){
    return size()>2;}
  
  /*
   * all adjacent pairs of vertices must be colinear
   */
  public boolean hasNoNoncolinearAdjacentVertices(){
    //each vertex colinear with it's neighbors
    int vertexcount=size();
    int inext;
    for(int i=0;i<vertexcount;i++){
      inext=i+1;
      if(inext==vertexcount)inext=0;
      if(!get(i).isColinear(get(inext))){
        return false;}}
    return true;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  DPolygon points=null;
  
  public DPolygon getPoints(){
    if(points==null)
      points=getDefaultPolygon2D();
    return points;}
  
  public Path2D.Double getPath(){
    DPolygon points=getPoints();
    Path2D.Double path=new Path2D.Double();
    DPoint p=points.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p.x,p.y);}
    if(closed)path.closePath();
    return path;} 
  
  public boolean isClockwise(){
    return GD.isClockwise(getPoints().getPointsAsDoubles());}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    String s=size()+" vertices. ";
    if(closed)
      s+=" closed";
    else
      s+="open";
    return s;}
  
}

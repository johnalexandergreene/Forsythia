package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.geom_2D.DCircle;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * at each vertex we have a point
 * the point is inward from the vertex
 * at this point we define a circle and may place an icon
 * we may derive other geometries from these circles
 */
class JigSectionDetailsModelVertexIconCircles{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  JigSectionDetailsModelVertexIconCircles(JigSectionDetailsModel m){
    createCircles(m);}
  
  /*
   * ################################
   * CIRCLE ACCESS
   * ################################
   */
  
  List<DCircle> orderedcircles=new ArrayList<DCircle>();
  Map<KVertex,DCircle> circlebyvertex=new Hashtable<KVertex,DCircle>();
  
  //returns ordered circles
  List<DCircle> getCircles(){
    return orderedcircles;}
  
  DCircle getCircle(KVertex v){
    return circlebyvertex.get(v);}
  
  /*
   * ################################
   * CREATE CIRCLES
   * ################################
   */
  
  //TODO stick in UI
  private static final double 
    VERTEXCIRCLEINWARDDISTANCE=48,
    VERTEXCIRCLERADIUS=8;
  
  private void createCircles(JigSectionDetailsModel m){
    KPolygon productpolygon=m.getKPolygon();
    orderedcircles=new ArrayList<DCircle>(productpolygon.size());
    List<DPoint> vertexpoints=GE.editor_editjigdetails.getGrid().getGeometryCache().getPoint2Ds(productpolygon);
    boolean isclockwise=GD.isClockwiseD(vertexpoints);
    int vertexpointcount=vertexpoints.size();
    int iprior,inext;
    DPoint p,pprior,pnext,iconpoint;
    double 
      dirinward,//direction inward from a vertex point
      disinward,
      angle;
    for(int i=0;i<vertexpointcount;i++){
      //get relevant points
      iprior=i-1;
      if(iprior==-1)iprior=vertexpointcount-1;
      inext=i+1;
      if(inext==vertexpointcount)inext=0;
      p=vertexpoints.get(i);
      pprior=vertexpoints.get(iprior);
      pnext=vertexpoints.get(inext);
      //get inward direction from vertex
      if(isclockwise)
        dirinward=GD.getDirection_3Points(pprior.x,pprior.y,p.x,p.y,pnext.x,pnext.y);
      else
        dirinward=GD.getDirection_3Points(pnext.x,pnext.y,p.x,p.y,pprior.x,pprior.y);
      //get distance inward from vertex
      if(isclockwise)
        angle=GD.getAngle_3Points(pprior.x,pprior.y,p.x,p.y,pnext.x,pnext.y);//TODO maybe switch these
      else
        angle=GD.getAngle_3Points(pnext.x,pnext.y,p.x,p.y,pprior.x,pprior.y);
      angle/=2;
      disinward=Math.abs(VERTEXCIRCLEINWARDDISTANCE*Math.cos(angle));
      //translate p inward to get iconpoint
      iconpoint=new DPoint(GD.getPoint_PointDirectionInterval(p.x,p.y,dirinward,disinward));
      createCircle(productpolygon.get(i),iconpoint);}}
  
  private void createCircle(KVertex v,DPoint circlecenter){
    DCircle c=new DCircle(circlecenter,VERTEXCIRCLERADIUS);
    orderedcircles.add(c);
    circlebyvertex.put(v,c);}
  
}

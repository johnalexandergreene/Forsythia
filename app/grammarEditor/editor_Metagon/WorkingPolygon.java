package org.fleen.forsythia.app.grammarEditor.editor_Metagon;

import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * TODO
 * random bug. occasionally it forbids adding of an arbitrary vertex. fix that 
 */
@SuppressWarnings("serial")
public class WorkingPolygon extends KPolygon{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  WorkingPolygon(){
    super(ESTIMATED_MAX_SIZE);}
  
  /*
   * ################################
   * GEOMETRY AND METRICS
   * ################################
   */
  
  private static final int ESTIMATED_MAX_SIZE=200;
  
  boolean 
    finished=false,
    invalid=false;
  
  void touchVertex(KVertex v){
    if(finished)return;
    KVertex vlast=null;
    int s=size();
    if(!isEmpty()){
      vlast=get(s-1);
      if(createsDeviantGeometry(v,vlast))
        return;}
    //remove
    if(vlast!=null&&v.equals(vlast)){
      remove(s-1);
      return;}
    //test for completing
    if(s>2){
      if(createsIntersection(v,vlast))return;
      if(v.equals(get(0))){
        finished=true;
        return;}}
    //not completing and good geom, so add
    add(v);}
  
  /*
   * post production geometry clean-up
   */
  void clean(){
    removeRedundantColinearVertices();}
  
  /*
   * ################################
   * GEOMETRY TESTS
   * ################################
   */
  
  //test for geometry that deviates from the kgrid geodesic
  //this test supposedly has a max distance after which it doesn't work. Not sure.
  private boolean createsDeviantGeometry(KVertex v0,KVertex v1){
    int a=GK.getDirection_VertexVertex(v0,v1);
    //is it a valid direction?
    if(a==GK.DIRECTION_NULL)
      return true;
    //do the involved vertices have liberty in that doirection?
    if(
      (!GK.hasLiberty(v0.getDog(),a))||
      (!GK.hasLiberty(v1.getDog(),a)))
      return true;
    return false;}
  
  //test seg intersection and vpresent between preexisting vertices
  //guaranteed s>2
  //TODO optimize? cache 2d points?
  private boolean createsIntersection(KVertex vpresent,KVertex vlast){
    double[] 
      p0=vpresent.getBasicPointCoor(),
      p1=vlast.getBasicPointCoor(),
      t0,t1;
    KVertex kvt0,kvt1;
    int s=size(),i1;
    for(int i0=0;i0<s-1;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      kvt0=get(i0);
      kvt1=get(i1);
      if(kvt0.equals(vpresent)||kvt1.equals(vlast))continue;
      t0=kvt0.getBasicPointCoor();
      t1=kvt1.getBasicPointCoor();
      if(GD.isBetween(p0[0],p0[1],t0[0],t0[1],t1[0],t1[1]))
        return true;
      if(GD.getIntersection_SegSeg(p0[0],p0[1],p1[0],p1[1],t0[0],t0[1],t1[0],t1[1])!=null)
        return true;}
    return false;}

}

package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;

/*
 * render certain attributes of a jig section polygon/model in terms of glyphs
 * a dot at v0 and a course of arrows running the permeter nicely
 *  
 */
public class GlyphSystemModel{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public GlyphSystemModel(DPolygon polygon,double inset,double gap,int idealarrowlength){
    createInnerPolygon(polygon,inset);
    doValidityTest(gap);
    if(valid);
      createArrowModels(gap,idealarrowlength);}
  
  /*
   * ################################
   * INNER POLYGON
   * ################################
   */
  
  private DPolygon innerpolygon;
  
  public DPolygon getInnerPolygon(){
    return innerpolygon;}
  
  private void createInnerPolygon(DPolygon outerpolygon,double inset){
    boolean clockwise=outerpolygon.getTwist();
    int s=outerpolygon.size(),iprior,inext;
    innerpolygon=new DPolygon(s);
    DPoint p,pprior,pnext,pinner;
    for(int i=0;i<s;i++){
      iprior=i-1;
      if(iprior==-1)iprior=s-1;
      inext=i+1;
      if(inext==s)
      inext=0;
      p=outerpolygon.get(i);
      pprior=outerpolygon.get(iprior);
      pnext=outerpolygon.get(inext);
      pinner=getInnerPoint(pprior,p,pnext,clockwise,inset);
      innerpolygon.add(pinner);}}
  
  private DPoint getInnerPoint(DPoint p0,DPoint p1,DPoint p2,boolean clockwise,double inset){
    double angle,dir;
    if(clockwise){
      angle=GD.getAngle_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
      dir=GD.getDirection_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
    }else{  
      angle=GD.getAngle_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);
      dir=GD.getDirection_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);}
    if(angle>GD.PI)
      angle=GD.PI2-angle;
    double dis=inset/(GD.sin(angle/2));
    DPoint innerpoint=p1.getPoint(dir,dis);
    return innerpoint;}
  
  /*
   * ################################
   * VALIDATION
   * test the length of the longest side of the inner polygon
   * ################################
   */
  private static final double TOOSMALL=2.1;
  //handle degenerate case. inner polygon too small or cramped
  //if one of the sides of the inner polygon is too short then this geometry is invalid
  boolean valid=true;
  
  public boolean isValid(){
    return valid;}
  
  private void doValidityTest(double gap){
    //get longest side length
    double 
      longest=Double.MIN_VALUE, 
      dis;
    int s=innerpolygon.size(),i1;
    DPoint p0,p1;
    for(int i0=0;i0<s;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      p0=innerpolygon.get(i0);
      p1=innerpolygon.get(i1);
      dis=p0.getDistance(p1);
      if(longest<dis)
        longest=dis;}
    //test it
    double limit=gap*TOOSMALL;
    valid=longest>limit;}
  
  /*
   * ################################
   * V0 DOT POINT
   * the location of the dot indicating the first vertex in the param polygon
   * ################################
   */
  
  public DPoint getV0DotPoint(){
    return innerpolygon.get(0);}
  
  /*
   * ################################
   * ARROW MODELS
   * ################################
   */
  
  GlyphPathModel pathmodel;//keep them here for debug and whatever
  ArrowModels arrowmodels;
  
  private void createArrowModels(double gap,int idealarrowlength){
    pathmodel=new GlyphPathModel(innerpolygon,gap,idealarrowlength);
    arrowmodels=new ArrowModels(pathmodel);
  }
 

  
  
}

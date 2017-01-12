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
   * V0 DOT POINT
   * the location of the dot indicating the first vertex in the param polygon
   * ################################
   */
  
  public DPoint getV0DotPoint(){
    return innerpolygon.get(0);}
  
  /*
   * ################################
   * PATH MODEL
   * ################################
   */
  
  GlyphPathModel pathmodel;
  
  private void createArrowModels(double gap,int idealarrowlength){
    pathmodel=new GlyphPathModel(innerpolygon,gap,idealarrowlength);
  }
 

  
  
}

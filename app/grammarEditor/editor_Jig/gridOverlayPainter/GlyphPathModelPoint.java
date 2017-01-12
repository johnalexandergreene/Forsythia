package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DPoint;

/*
 * defined in terms of either xy coors or offset on path
 */
@SuppressWarnings("serial")
class GlyphPathModelPoint extends DPoint{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  GlyphPathModelPoint(GlyphPathModel model,DPoint p,int index,boolean first,boolean last,boolean corner,boolean reticulation){
    super(p);
    this.model=model;
    this.index=index;
    this.first=first;
    this.last=last;
    this.corner=corner;
    this.reticulation=reticulation;}
  
  /*
   * ################################
   * MODEL 
   * ################################
   */
  
  GlyphPathModel model;
  
  /*
   * ################################
   * ATTRIBUTES 
   * ################################
   */
  
  int index;
  boolean
    first=false,
    last=false,
    corner=false,
    reticulation=false,
    arrowhead=false,
    arrowtail=false;
  
  /*
   * ################################
   * GEOMETRY 
   * ################################
   */
  
  GlyphPathModelPoint getNextPoint(){
    if(index==model.size()-1)return null;
    return model.get(index+1);}
  
  GlyphPathModelPoint getPriorPoint(){
    if(index==0)return null;
    return model.get(index-1);}
  
  /*
   * in this next couple of methods we are assuming that we will never have 
   * 2 corner points between a pair of reticulation points
   * so we're gonna have a minimum size or something
   */
  GlyphPathModelPoint getNextNoncornerPoint(){
    GlyphPathModelPoint p=getNextPoint();
    if(p==null)return null;
    if(p.corner)
      p=p.getNextPoint();
    return p;}
  
  GlyphPathModelPoint getPriorNoncornerPoint(){
    GlyphPathModelPoint p=getPriorPoint();
    if(p==null)return null;
    if(p.corner)
      p=p.getPriorPoint();
    return p;}
  
  GlyphPathModelPoint getNextCorner(){
    GlyphPathModelPoint p=getNextPoint();
    if(p==null)
      return null;
    while(!p.corner){
      p=p.getNextPoint();
      if(p==null)
        return null;}
    return p;}
  
  GlyphPathModelPoint getPriorCorner(){
    GlyphPathModelPoint p=getPriorPoint();
    if(p==null)
      return null;
    while(!p.corner){
      p=p.getPriorPoint();
      if(p==null)
        return null;}
    return p;}
  
  /*
   * remove arrow attributes for this point and the closest noncorners
   */
  void removeArrowAttributesForSelfAndNoncornerAdjacents(){
    removeArrowAttributes();
    GlyphPathModelPoint p=getNextNoncornerPoint();
    if(p!=null)p.removeArrowAttributes();
    p=getPriorNoncornerPoint();
    if(p!=null)p.removeArrowAttributes();}
  
  void removeArrowAttributes(){
    arrowhead=false;
    arrowtail=false;}
  
  boolean hasArrowAttributes(){
    return arrowhead||arrowtail;}
  
  List<GlyphPathModelPoint> getPointsInRange(double range){
    List<GlyphPathModelPoint> points=new ArrayList<GlyphPathModelPoint>();
    //check downstream
    GlyphPathModelPoint testpoint=getNextPoint();
    if(testpoint!=null){
      double testrange=getDistance(testpoint);
      while(testrange<range&&testpoint!=null){
        points.add(testpoint);
        testpoint=testpoint.getNextPoint();
        if(testpoint!=null)
          testrange=getDistance(testpoint);}}
    //check upstream
    testpoint=getPriorPoint();
    if(testpoint!=null){
      double testrange=getDistance(testpoint);
      while(testrange<range&&testpoint!=null){
        points.add(testpoint);
        testpoint=testpoint.getPriorPoint();
        if(testpoint!=null)
          testrange=getDistance(testpoint);}}
    //
    return points;}
  
  
  
}

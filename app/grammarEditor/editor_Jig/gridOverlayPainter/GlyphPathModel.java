package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.DSeg;

/*
 * a figure, a path, an open curve, defined by a list of points.
 *   the first point is the v0 dot point. After than are points corrosponding to polygon corners. The final point is at gap distance from v0.
 *    
 * a list of points defined in terms of offset from v0 on the path
 */
@SuppressWarnings("serial")
class GlyphPathModel extends ArrayList<GlyphPathModelPoint>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  GlyphPathModel(DPolygon polygon,double gap,int idealarrowlength){
    super();
    this.polygon=polygon;
    this.gap=gap;
    this.idealarrowlength=idealarrowlength;
    createPoints();
    refinePoints();}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  DPolygon polygon;
  double gap;
  int idealarrowlength;
  
  /*
   * at every vertex except the last, insert a real point
   * at gap intervals insert a reticulation point
   */
  private void createPoints(){
    DPoint dpoint=polygon.get(0);
    int pointindex=0;
    add(new GlyphPathModelPoint(this,dpoint,pointindex,true,false,false,false));
    pointindex++;
    List<DSeg> segs=polygon.getSegs();
    int segcount=segs.size(),segindex=0;
    DSeg seg=segs.get(segindex);
    double 
      seglength=seg.getLength(),
      offsetinseg=0;
    while(true){
      offsetinseg+=gap;
      if(offsetinseg>seglength){
        add(new GlyphPathModelPoint(this,seg.p1,pointindex,false,false,true,false));
        pointindex++;
        offsetinseg-=seglength;
        segindex++;
        if(segindex==segcount)break;
        seg=segs.get(segindex);
        seglength=seg.getLength();}
      dpoint=seg.getDPointAtRealOffset(offsetinseg);
      add(new GlyphPathModelPoint(this,dpoint,pointindex,false,false,false,true));
      pointindex++;}
    remove(size()-1);
    remove(size()-1);
    get(size()-1).last=true;}
 
  /*
   * appropriately marked points define the ends of our arrows
   * create raw marks then refine them so they look nice
   */
  private void refinePoints(){
    doRawArrowIntervals();
    fixCorners();
    fixShortStart();
//    fixShortLastLeg();
//    cleanEnd();
    
  }
 
  /*
   * crawl the path
   * mark first point and appropriate reticulation points to indicate arrow lengths
   * marks go 
   *   arrowtail - nomark - nomark - nomark - arrowhead - arrowtail - nomark - nomark - nomark - arrowhead - nomark - etc...
   *   until we run out of points
   */
  private void doRawArrowIntervals(){
    GlyphPathModelPoint point=get(0);
    point.arrowtail=true;
    int length=0;
    while(!point.last){
      point=point.getNextNoncornerPoint();
      length++;
      if(length==idealarrowlength)
        point.arrowhead=true;
      if(length==idealarrowlength+1){
        point.arrowtail=true;
        length=0;}}
    get(size()-1).arrowhead=true;}
 
  static final double CORNERZONE=0.6;
  
  /*
   * given corner point
   * check all points within range idealarrowlength*gap*CORNERZONE
   * if a point is an arrowhead or arrowtail then set that flag to false
   * get its neighboring arrowhead or arrowtail and set that to false too
   */
  private void fixCorners(){
    double range=idealarrowlength*gap*CORNERZONE;
    List<GlyphPathModelPoint> pointsinrange;
    for(GlyphPathModelPoint p0:this){
      if(p0.corner){
        pointsinrange=p0.getPointsInRange(range);
        for(GlyphPathModelPoint p1:pointsinrange){
            if(p1.hasArrowAttributes())
              p1.removeArrowAttributesForSelfAndNoncornerAdjacents();}}}
    //fix first and last just in case;
    get(0).arrowtail=true;
    get(size()-1).arrowhead=true;}
  
  static final double STARTTOOSHORT=2.1;
  
  /*
   * if the firstpoint is too close to the first corner then remove all 
   * arrow marks from the first point to the corner, except for the first point.
   */
  private void fixShortStart(){
    GlyphPathModelPoint 
      p0=get(0),
      p1=p0.getNextCorner();
    double
      dis=p0.getDistance(p1),
      minrange=idealarrowlength*gap*STARTTOOSHORT;
    if(dis<minrange){
      while(p0!=p1){
        p0=p0.getNextPoint();
        p0.removeArrowAttributes();}}}
  
  static final double ENDTOOSHORT=1.5;
  
  /*
   * if the lastpoint is too close to the last corner then remove all 
   * arrow marks from lastpoint to lastcorner, except for lastpoint
   */
  private void fixShortLastLeg(){
    GlyphPathModelPoint 
      p0=get(size()-1),
      p1=p0.getPriorCorner();
    double
      dis=p0.getDistance(p1),
      minrange=idealarrowlength*gap*ENDTOOSHORT;
    if(dis<minrange){
      while(p0!=p1){
        p0=p0.getPriorPoint();
        p0.removeArrowAttributes();}}}
  
  private void cleanEnd(){
    GlyphPathModelPoint pend=get(size()-1),p0=pend;
    int c=0;
    while(c<idealarrowlength){
      p0=p0.getPriorNoncornerPoint();
      p0.removeArrowAttributesForSelfAndNoncornerAdjacents();
      c++;}
    pend.arrowhead=true;}
  
 
    
}

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
    markArrowEnds();}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  static final int 
    PTYPE_FIRST=0,
    PTYPE_LAST=1,
    PTYPE_CORNER=2,
    PTYPE_RETICULATION=3;
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
    add(new GlyphPathModelPoint(this,dpoint,pointindex,PTYPE_FIRST));
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
        add(new GlyphPathModelPoint(this,seg.p1,pointindex,PTYPE_CORNER));
        pointindex++;
        offsetinseg-=seglength;
        segindex++;
        if(segindex==segcount)break;
        seg=segs.get(segindex);
        seglength=seg.getLength();}
      dpoint=seg.getDPointAtRealOffset(offsetinseg);
      add(new GlyphPathModelPoint(this,dpoint,pointindex,PTYPE_RETICULATION));
      pointindex++;}
    remove(size()-1);
    remove(size()-1);
    get(size()-1).type=PTYPE_LAST;}
  
  /*
   * crawl the path
   */
  private void markArrowEnds(){
    GlyphPathModelPoint point=get(0);
    int length=0;
    while(point.type!=PTYPE_LAST){
      if(point.type==PTYPE_FIRST){
        point.arrowstart=true;
        point=point.getNextPoint();
        length++;
      }else if(point.type==PTYPE_CORNER){
        point=point.getNextPoint();
      }else{//point.type==PTYPE_RETICULATION
        point=point.getNextPoint();
        length++;}
      //
      if(point.type!=PTYPE_CORNER){
        if(length==idealarrowlength)
          point.arrowend=true;
        if(length==idealarrowlength+1){
          point.arrowstart=true;
          length=0;}}}
    
    
  }
    
}

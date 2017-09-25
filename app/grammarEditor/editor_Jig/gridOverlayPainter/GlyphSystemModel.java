package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.DSeg;
import org.fleen.geom_2D.GD;

/*
 * render certain attributes of a jig section polygon/model in terms of glyphs
 * an inner polygon gives us our basic geometry
 * a dot at v0 and a wrap-around arrow
 * the arrow starts at the dot and ends at E
 * e is a point in the middle of a side of the inner polygon
 * we use the first side that is long enough, going backwards from the last side
 *  
 */
public class GlyphSystemModel implements Serializable{
  
  private static final long serialVersionUID=1958965206775431418L;

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public GlyphSystemModel(DPolygon polygon,double inset){
    this.inset=inset;
    createInnerPolygon(polygon);
    doValidityTest();
    if(valid);
      createGlyphPath();}
  
  /*
   * ################################
   * INSET
   * we use this as our unit interval
   * ################################
   */
  
  double inset;
  
  /*
   * ################################
   * INNER POLYGON
   * ################################
   */
  
  private DPolygon innerpolygon;
  
  public DPolygon getInnerPolygon(){
    return innerpolygon;}
  
  private void createInnerPolygon(DPolygon outerpolygon){
    boolean clockwise=outerpolygon.getChirality();
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
   * test the length of the shortest side of the inner polygon
   * ################################
   */
  
  //length of a polygon side in terms of inset
  private static final double TOOSMALL=2.2;
  //handle degenerate case. inner polygon too small or cramped
  //if one of the sides of the inner polygon is too short then this geometry is invalid
  boolean valid=true;
  
  public boolean isValid(){
    return valid;}
  
  private void doValidityTest(){
    //get longest side length
    List<DSeg> segs=innerpolygon.getSegs();
    double limit=inset*TOOSMALL;
    for(DSeg seg:segs){
      if(seg.getLength()<limit){
        valid=false;
        return;}}}
  
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
   * GLYPH PATH
   * ################################
   */
  
  //minimum length of the last side, where the head of the glyph arrow is
  //in terms of gap
  private static final int SHORTLASTSIDELIMIT=6;
  List<DPoint> glyphpath;
  
  /*
   * get last side. test lengths starting at last side in inner polygon and go backwards
   * from last side get last point
   * assemble glyph path from inner polygon points and last point
   */
  private void createGlyphPath(){
    List<DSeg> segs=innerpolygon.getSegs();
    glyphpath=new ArrayList<DPoint>(innerpolygon.size());
    int lastside=getLastSide(segs);
    for(int i=0;i<lastside+1;i++)
      glyphpath.add(innerpolygon.get(i));
    glyphpath.add(new DPoint(segs.get(lastside).getPointAtProportionalOffset(0.5)));}
  
  private int getLastSide(List<DSeg> segs){
    double shortsidelimit=SHORTLASTSIDELIMIT*inset;
    int s=segs.size();
    DSeg seg;
    for(int i=s-1;i>-1;i--){
      seg=segs.get(i);
      if(seg.getLength()>shortsidelimit)
        return i;}
    throw new IllegalArgumentException("couldn't get the last side seg index");}
 
}

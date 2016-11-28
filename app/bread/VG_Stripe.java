package org.fleen.forsythia.app.bread;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.forsythia.core.composition.FGrid;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_2D.DCircle;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.DSeg;
import org.fleen.geom_2D.GD;

/*
 * draw stripes within domain of polygon
 * stripes run east to west, perpendicular to the ns axis
 * 
 * stripes are rectangles, clipped to polygon using java 2d area stuff
 * 
 * get a direction. polygon.parentgrid.foreward : north
 * get a bounding rectangle for the polygon. The rectangle must be aligned with north.
 *   the ns axis is referred to as the bounding rectangle's axis
 * get stripes
 *   define a number of rectangles on the bounding rectangle, cutting it across the axis
 * clip stripe rectangles to polygon. Fill each of those clippedstripepolygons.
 * 
 */
public class VG_Stripe implements Voice_Graphics2D{
  
  //TODO BETTER
  double time;
  Color color=null;
//  double stripethickness=0.2;

  public void paint(FPolygon polygon,Graphics2D graphics,double time){
    this.time=time;
//    Path2D p=getPath(polygon.getDPolygon());
//    graphics.setPaint(new Color(255,255,255,64));
//    graphics.fill(p);
    //
    
    FGrid parentgrid=(FGrid)polygon.getParent();
    north=parentgrid.getLocalKGrid().getForeward();
    initBoundingSquare(polygon);
    List<DPolygon> rawstripes=getRawStripes(polygon);
    List<Area> clippedstripes=getClippedStripes(polygon,rawstripes);
    //
    Area clippedstripe;
    for(int i=0;i<clippedstripes.size();i++){
      if(i%2==0){
        if(color!=null)
          graphics.setPaint(color);
        else
          graphics.setPaint(Color.black);}
      else
        graphics.setPaint(new Color(255,255,255,64));
//        graphics.setPaint(Color.white);
      clippedstripe=clippedstripes.get(i);
      graphics.fill(clippedstripe);}}
  
  double north;
  DPolygon boundingsquare;
  DPoint boundingsquarecenter;
  double boundingsquareradius,boundingsquarespan;
  
  /*
   * relative to north
   *   rectangle points are NW, NE, SE, SW
   * foreward==dir(p3,p0) 
   */
  void initBoundingSquare(FPolygon fpolygon){
    boundingsquare=new DPolygon(4);
    DPolygon dpolygon=fpolygon.getDPolygon();
    boundingsquarecenter=dpolygon.get(0);
    boundingsquareradius=getBoundingSquareRadius(dpolygon);
    boundingsquarespan=(boundingsquareradius/GD.SQRT2)*2;
    double[] a=GD.getPoint_PointDirectionInterval(boundingsquarecenter.x,boundingsquarecenter.y,north-GD.PI/4,boundingsquareradius);
    boundingsquare.add(new DPoint(a));
    a=GD.getPoint_PointDirectionInterval(boundingsquarecenter.x,boundingsquarecenter.y,north+GD.PI/4,boundingsquareradius);
    boundingsquare.add(new DPoint(a));
    a=GD.getPoint_PointDirectionInterval(boundingsquarecenter.x,boundingsquarecenter.y,north+GD.PI-GD.PI/4,boundingsquareradius);
    boundingsquare.add(new DPoint(a));
    a=GD.getPoint_PointDirectionInterval(boundingsquarecenter.x,boundingsquarecenter.y,north+GD.PI+GD.PI/4,boundingsquareradius);
    boundingsquare.add(new DPoint(a));}
  
  /*
   * it's the distance from p0 to the furthest other point in the polygon
   */
  private double getBoundingSquareRadius(DPolygon dpolygon){
    DPoint 
      p0=dpolygon.get(0),
      ptest;
    double dtest,dfurthest=Double.MIN_VALUE;
    for(int i=1;i<dpolygon.size();i++){
      ptest=dpolygon.get(i);
      dtest=ptest.getDistance(p0);
      if(dtest>dfurthest)
        dfurthest=dtest;}
    return dfurthest*2;}
  
  /*
   * our square looks like this 
   * 
   *   p0 o----------------o p1
   *      |                |
   *      |                |
   *      |                |
   *      |                |    ^ north
   *    s0|       c        |s1
   *      |                |
   *      |                |
   *      |                |
   *   p3 o----------------o p2
   *   
   * the polygon's p0 is c
   *   
   * consider 2 north-oriented side-segs of our square
   * seg(p3,p0) and seg(p2,p1)
   * 
   * This gives us the base for our stripe geometry
   */
  
  double relativestripethickness=0.15;
  
  List<DPolygon> getRawStripes(FPolygon polygon){
    List<DPolygon> rawstripes=new ArrayList<DPolygon>();
    DSeg 
      s0=new DSeg(boundingsquare.get(3),boundingsquare.get(0)),
      s1=new DSeg(boundingsquare.get(2),boundingsquare.get(1));
    //stripe thickness is based on local fish
    double stripethickness=((FGrid)polygon.getParent()).getLocalKGrid().getFish()*relativestripethickness;
    int stripecount=(int)(boundingsquarespan/stripethickness)+1;
    double offsetlower,offsetupper;
    //create stripes
    DPolygon rawstripe;
    for(int i=0;i<stripecount;i++){
      rawstripe=new DPolygon(4);
      offsetlower=i*stripethickness+(time*stripethickness);
      offsetupper=offsetlower+stripethickness+(time*stripethickness);
      rawstripe.add(new DPoint(s0.getPointAtRealOffset(offsetupper)));
      rawstripe.add(new DPoint(s1.getPointAtRealOffset(offsetupper)));
      rawstripe.add(new DPoint(s1.getPointAtRealOffset(offsetlower)));
      rawstripe.add(new DPoint(s0.getPointAtRealOffset(offsetlower)));
      rawstripes.add(rawstripe);}
    //
    return rawstripes;}
  
  /*
   * ############################################################
   */
  
  List<Area> getClippedStripes(FPolygon polygon,List<DPolygon> rawstripes){
    List<Area> clippedstripes=new ArrayList<Area>(rawstripes.size());
    Area clip=new Area(getPath(polygon.getDPolygon())),stripe;
    Path2D rawstripepath;
    for(DPolygon dpolygon:rawstripes){
      rawstripepath=getPath(dpolygon);
      stripe=new Area(rawstripepath);
      stripe.intersect(clip);
      clippedstripes.add(stripe);}
    return clippedstripes;}
  
  private Path2D getPath(DPolygon polygon){
    Path2D.Double path=new Path2D.Double();
    DPoint p=polygon.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<polygon.size();i++){
      p=polygon.get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}

}

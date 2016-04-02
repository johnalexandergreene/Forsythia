package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.fleen.geom_2D.DCircle;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;

/*
 * Create TC icon circles
 * TC = Type and Chorus
 * 
 * inside the area of a section polygon we have 2 circular buttons
 * one displays the section type (a letter : P=peel, E=egg, H=shard )
 * the other displays the section chorus index (0..sectioncount)
 * touching either button incrementally cycles its associated value through the set of possibilities
 * 
 * These circles should be inside the polygon
 * not too close to each other but not too far either. They should be grouped without touching.
 * they should not be too close to any of the vertex circles 
 * 
 * given CIRCLERADIUS, CIRCLEMARGIN
 * 
 * get inpoint (center of the incircle) : pin
 * get 6 points at 2*CIRCLERADIUS + CIRCLEMARGIN distance from pin
 * now we have 7 points.
 * cull any that lay outside the polygon
 * if we have <2 remaining then fail
 * if we have 2 remaining then we have our 2
 * if we have >2 remaining then 
 * get their distance from the polygon edges and the vertex icon circles
 * sort by that distance
 * keep the 2 with the furthest distance
 * 
 * from those 2 point create 2 circles of radius CIRCLERADIUS
 * the one on the left is the type, the one on the right is the chorusindex
 * 
 */
public class JigSectionDetailsModelTCIconCircles{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  JigSectionDetailsModelTCIconCircles(JigSectionDetailsModel m){
    createCircles(m);}
  
  /*
   * ################################
   * CIRCLE ACCESS
   * ################################
   */
  
  private static final double 
    CIRCLERADIUS=16,
    CIRCLEMARGIN=2;

  private DCircle 
    circle_producttype=null,
    circle_chorusindex=null;
  
  public boolean failed(){
    return circle_producttype==null;}
  
  public DCircle getCircle_ProductType(){
    return circle_producttype;}
  
  public DCircle getCircle_ChorusIndex(){
    return circle_chorusindex;}
  
  /*
   * ################################
   * CREATE CIRCLES
   * ################################
   */
  
  private void createCircles(JigSectionDetailsModel m){
    List<double[]> p=get2Points(m);
    if(p==null)return;//fail
    double[] 
      p0=p.get(0),
      p1=p.get(1);
    //product type goes on the left
    if(p0[0]<p1[0]){
      circle_producttype=new DCircle(p0[0],p0[1],CIRCLERADIUS);  
      circle_chorusindex=new DCircle(p1[0],p1[1],CIRCLERADIUS);  
    }else{
      circle_producttype=new DCircle(p1[0],p1[1],CIRCLERADIUS);  
      circle_chorusindex=new DCircle(p0[0],p0[1],CIRCLERADIUS);}}
  
  private List<double[]> get2Points(JigSectionDetailsModel m){
    DPolygon mpolygon=m.getPolygon2D();
    double[] pin=mpolygon.getInPoint();
    //our prospects are double[3]. x,y,d
    List<double[]> prospects=getProspects(pin);
    cullOutsiders(prospects,mpolygon);
    int psize=prospects.size();
    if(psize<2)return null;//fail
    if(prospects.size()==2)return prospects;
    doDistances(prospects,mpolygon,m.getVertexIconCircles().getCircles());
    Collections.sort(prospects,prospectcomparator);
    List<double[]> a=new ArrayList<double[]>(2);
    a.add(prospects.get(prospects.size()-1));
    a.add(prospects.get(prospects.size()-2));
    return a;}
  
  private Comparator<double[]> prospectcomparator=new Comparator<double[]>(){

    public int compare(double[] p0,double[] p1){
      if(p0[2]==p1[2]){
        return 0;
      }else if(p0[2]>p1[2]){
        return 1;
      }else{
        return -1;}}};
  
  private List<double[]> getProspects(double[] pin){
    double 
      dis=2*CIRCLERADIUS+CIRCLEMARGIN,
      pi2=2.0*GD.PI;
    List<double[]> prospects=new ArrayList<double[]>(7);
    prospects.add(new double[]{pin[0],pin[1],-1});
    double[] a;
    for(int i=0;i<6;i++){
      a=GD.getPoint_PointDirectionInterval(pin[0],pin[1],pi2*((double)i)/6,dis);
      prospects.add(new double[]{a[0],a[1],-1});}
    return prospects;}
  
  private void cullOutsiders(List<double[]> prospects,DPolygon mpolygon){
    Iterator<double[]> i=prospects.iterator();
    double[] p;
    while(i.hasNext()){
      p=i.next();
      if(!mpolygon.containsPoint(p[0],p[1]))
        i.remove();}}
  
  private void doDistances(List<double[]> prospects,DPolygon mpolygon,List<DCircle> vertexiconcircles){
    for(double[] p:prospects)
      p[2]=getDistance(p,mpolygon,vertexiconcircles);}
  
  private double getDistance(double[] p,DPolygon mpolygon,List<DCircle> vertexiconcircles){
    double 
      distance=GD.getDistance_PointPolygon(p[0],p[1],mpolygon),
      dtest;
    for(DCircle c:vertexiconcircles){
      dtest=c.getDistance(p);
      if(dtest<distance)distance=dtest;}
    return distance;}
  
}

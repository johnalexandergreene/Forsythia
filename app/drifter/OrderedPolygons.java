package org.fleen.forsythia.app.drifter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.fleen.forsythia.composition.FPolygon;

/*
 * 1..n lists of polygons
 * each list is associated with a tree-depth value, an integer
 */
@SuppressWarnings("serial")
public class OrderedPolygons extends Hashtable<Integer,List<FPolygon>>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  OrderedPolygons(List<FPolygon> unorderedpolygons){
    polygoncount=unorderedpolygons.size();
    for(FPolygon p:unorderedpolygons)
      addPolygon(p);}
  
  /*
   * ################################
   * POLYGON COUNT
   * ################################
   */
  
  int polygoncount;
  
  public int getPolygonCount(){
    return polygoncount;}
  
  /*
   * ################################
   * POLYGON MANAGEMENT
   * ################################
   */
  
  private void addPolygon(FPolygon polygon){
    Integer d=polygon.getPolygonDepth();
    List<FPolygon> polygons=get(d);
    if(polygons==null){
      polygons=new ArrayList<FPolygon>();
      put(d,polygons);}
    polygons.add(polygon);}
  
  /*
   * returns the lists of polygons ordered by depth
   * deepest to shallowest (reverse natural order)
   */
  List<List<FPolygon>> getPolygonLists(){
    List<Integer> levels=new ArrayList<Integer>(keySet());
    Collections.sort(levels);//TODO yes we could make our own comparator and speed things up a little
    int s=size();
    List<List<FPolygon>> polygonlists=new ArrayList<List<FPolygon>>(s);
//    for(int i=s-1;i>-1;i--)//deep to shallow
    for(int i=0;i<s;i++)//shallow to deep
      polygonlists.add(get(levels.get(i)));
    return polygonlists;}

}

package org.fleen.forsythia.app.strobe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Simple.ForsythiaGrammar_Simple;
import org.fleen.geom_2D.DPolygon;

/*
 * ################################
 * VISIBLE POLYGONS
 * Given a viewport polygon and a visibledetailfloor
 *   get all polygons that intersect the viewport polygon
 *   AND have detailsize > detailsizefloor
 *   
 * Assuming that the root intersects the viewport
 *   we grab all polygons in the tree that also intersect the viewport
 *   if a polygon has detailsize greater than visibledetailfloor then we cultivate it to get greater detail untill we hit the floor.
 * 
 * ----------------
 * 
 * get the root
 * if the root is within and root.detailsize> visibledetailfloor then add it to the list of visible polygons
 * if it isn't then we're done
 * get the root's children
 *   for each child 
 *   if the child is visible and then add it to the list of visible 
 *   
 * 
 * ################################
 */
@SuppressWarnings("serial")
public class VisiblePolygons extends ArrayList<FPolygon>{
  
  //a little speed
  private static final int PROSPECTS_LIST_SIZE_INIT=30000;
  
  VisiblePolygons(Composition composition,DPolygon viewport,double visibledetailsizefloor){
    List<FPolygon> 
      //the polygons that might be visible
      prospects=new ArrayList<FPolygon>(PROSPECTS_LIST_SIZE_INIT);
    FPolygon polygon;
    prospects.add(composition.getRootPolygon());
    ForsythiaGrammar_Simple grammar=composition.getGrammar();
    int testedpolygons=0;
    while(!prospects.isEmpty()){
      polygon=prospects.remove(0);
//      if(polygon.getDPolygon().roughlyIntersect(viewport)){
      if(polygon.getDPolygon().intersect(viewport)){
        testedpolygons++;
        add(polygon);
        if(polygon.getDetailSize()>visibledetailsizefloor){
          prospects.addAll(getPolygonChildren(polygon,grammar));}}}
    System.out.println("testedpolygons="+testedpolygons);
    }
  
  /*
   * get the children of the specified polygon
   * if it doesn't have children then create some
   */
  private List<FPolygon> getPolygonChildren(FPolygon polygon,ForsythiaGrammar_Simple grammar){
    if(!polygon.hasChildren())
      cultivateOnce(polygon,grammar);
    List<FPolygon> children=polygon.getPolygonChildren();
    return children;}
  
  /*
   * ################################
   * POLYGON CULTIVATION
   * Our basic random-selection-chorus-by-sig logic
   * ################################
   */
  
  Map<FPolygonSignature,Jig> jigbypolygonsig=new HashMap<FPolygonSignature,Jig>();
  Random rnd=new Random();
  
  private void cultivateOnce(FPolygon polygon,ForsythiaGrammar_Simple grammar){
    Jig jig=selectJig(polygon,grammar);
    jig.createNodes(polygon);}
  
  private Jig selectJig(FPolygon polygon,ForsythiaGrammar_Simple grammar){
    //get a jig by signature
    //polygons with the same sig get the same jig
    Jig j=jigbypolygonsig.get(polygon.getSignature());
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      j=getRandomJig(grammar,polygon);
      if(j==null)return null;
      jigbypolygonsig.put(polygon.getSignature(),j);
      return j;}}
  
  private Jig getRandomJig(ForsythiaGrammar_Simple fg,FPolygon target){
    List<Jig> jigs=fg.getJigs(target);
    if(jigs.isEmpty())return null;
    Jig jig=jigs.get(rnd.nextInt(jigs.size()));
    return jig;}

}

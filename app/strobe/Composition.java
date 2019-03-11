package org.fleen.forsythia.app.strobe;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.FJig;
import org.fleen.geom_2D.DPolygon;

public class Composition extends ForsythiaComposition{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Composition(Strobe strobe,ForsythiaGrammar grammar){
    this.strobe=strobe;
    setGrammar(grammar);
    initRoot();}
  
  /*
   * ################################
   * STROBE
   * ################################
   */
  
  Strobe strobe;
  
  /*
   * ################################
   * TREE
   * ################################
   */
  
  /*
   * find root.
   * we want a hexagon. It needs to be a hexagon. Our system uses a hexagon for the root.
   */
  private void initRoot(){
    FMetagon rootpolygonmetagon=null;
    List<FMetagon> metagons=grammar.getMetagons();
    for(FMetagon m:metagons)
      if(m.hasTag("root"))
        rootpolygonmetagon=m;
    if(rootpolygonmetagon==null)
      rootpolygonmetagon=metagons.get(new Random().nextInt(metagons.size()));
    initTree(rootpolygonmetagon);}
  
  /*
   * ################################
   * VISIBLE POLYGONS
   * Given a viewport polygon and a visibledetailfloor
   *   get all polygons that intersect or are contained within the viewport polygon
   *   AND have detailsize > detailsizefloor
   *   
   * Assuming that the root intersects the viewport
   *   we grab all polygons in the tree that also intersect the viewport
   *   if a polygon has detailsize greater than visibledetailfloor then we cultivate it to get greater detail.
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
  
  OrderedPolygons getVisiblePolygons(DPolygon viewport,double visibledetailfloor){
    List<FPolygon> 
      visiblepolygons=new ArrayList<FPolygon>(),
      //the polygons that might be visible
      prospects=new ArrayList<FPolygon>();
    FPolygon polygon;
    prospects.add(getRootPolygon());
    while(!prospects.isEmpty()){
      polygon=prospects.remove(0);
      if(polygon.getDetailSize()>visibledetailfloor){
        if(polygon.getDPolygon().intersect(viewport)){
          visiblepolygons.add(polygon);
          prospects.addAll(getPolygonChildren(polygon));}}}
    return new OrderedPolygons(visiblepolygons);}
  
  /*
   * get the children of the specified polygon
   * if it doesn't have children then create some
   */
  private List<FPolygon> getPolygonChildren(FPolygon polygon){
    if(!polygon.hasChildren())
      cultivateOnce(polygon);
    List<FPolygon> children=polygon.getPolygonChildren();
    return children;}
  
  /*
   * ################################
   * POLYGON CULTIVATION
   * 
   * Our basic chorus-by-sig logic
   * 
   * When we are getting visible polygons and the visible polygons that we have so far are 
   *   bigger than the detail size floor then we cultivate those polygons to get more polygons, more smaller details.
   * ################################
   */
  
  Map<FPolygonSignature,FJig> jigbypolygonsig=new Hashtable<FPolygonSignature,FJig>();
  Random rnd=new Random();
  
  private void cultivateOnce(FPolygon polygon){
    FJig jig=selectJig(polygon);
    jig.createNodes(polygon);}
  
  private FJig selectJig(FPolygon polygon){
    //get a jig by signature
    //polygons with the same sig get the same jig
    FJig j=jigbypolygonsig.get(polygon.getSignature());
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      j=getRandomJig(grammar,polygon);
      if(j==null)return null;
      jigbypolygonsig.put(polygon.getSignature(),j);
      return j;}}
  
  private FJig getRandomJig(ForsythiaGrammar fg,FPolygon target){
    List<FJig> jigs=fg.getJigs(target);
    if(jigs.isEmpty())return null;
    FJig jig=jigs.get(rnd.nextInt(jigs.size()));
    return jig;}
  
}

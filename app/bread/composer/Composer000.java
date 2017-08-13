package org.fleen.forsythia.app.bread.composer;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.util.tree.TreeNodeIterator;

public class Composer000 extends Composer_Abstract{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Composer000(double detailsizefloor,double symmetry){
    this.detailsizefloor=detailsizefloor;
    this.symmetry=symmetry;}
  
  public Composer000(){
    detailsizefloor=DETAILSIZEFLOOR_DEFAULT;
    symmetry=SYMMETRY_DEFAULT;}
  
  /*
   * ################################
   * DETAIL SIZE FLOOR
   * We test a jig and its prospective target
   * if any of the resulting polygons will have detailsize smaller 
   *   than detail size floor then we don't use that jig
   * ################################
   */
  
  public static final double DETAILSIZEFLOOR_DEFAULT=0.012;
//  public static final double DETAILSIZEFLOOR_DEFAULT=0.012;
  private double detailsizefloor;
  
  /*
   * ################################
   * SYMMETRY
   * range [0,1]
   * 1 is max symmetry
   * 0 is max asymmetry
   * ################################
   */
  
  public static final double SYMMETRY_DEFAULT=1.0;
  private double symmetry;
  
  /*
   * ################################
   * CREATE NODES
   * ################################
   */
  
  protected boolean createNodes(ForsythiaComposition composition){
    Jig jig;
    boolean creatednodes=false;
    TreeNodeIterator i=composition.getLeafPolygonIterator();
    FPolygon leaf;
    ForsythiaGrammar grammar=composition.getGrammar();
    while(i.hasNext()){
      leaf=(FPolygon)i.next();
      if(isCapped(leaf))continue;
      jig=selectJig(grammar,leaf);
      if(jig==null){
        cap(leaf);
      }else{
        jig.createNodes(leaf);
        creatednodes=true;}}
    jigbypolygonsig.clear();
    return creatednodes;}
  
  /*
   * ################################
   * JIG SELECTOR
   * ################################
   */
  
  Map<FPolygonSignature,Jig> jigbypolygonsig=new Hashtable<FPolygonSignature,Jig>();
  Random rnd=new Random();
  
  private Jig selectJig(ForsythiaGrammar forsythiagrammar,FPolygon polygon){
    //get a jig by signature
    //polygons with the same sig get the same jig
    Jig j=jigbypolygonsig.get(polygon.getSignature());
    //-------------------------------
    //induce asymmetry
    if(rnd.nextDouble()>symmetry)j=null;
    //-------------------------------
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      j=getRandomJig(forsythiagrammar,polygon);
      if(j==null)return null;
      jigbypolygonsig.put(polygon.getSignature(),j);
      return j;}}
  
  private Jig getRandomJig(ForsythiaGrammar fg,FPolygon target){
    List<Jig> jigs=fg.getJigsAboveDetailSizeFloor(target,detailsizefloor);
    if(jigs.isEmpty())return null;
    Jig jig=jigs.get(new Random().nextInt(jigs.size()));
    return jig;}
  
  
}

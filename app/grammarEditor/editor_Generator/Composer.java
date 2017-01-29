package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.util.tree.TreeNodeIterator;

public class Composer{
  
  /*
   * ################################
   * COMPOSE
   * ################################
   */
  
  public ForsythiaComposition compose(ForsythiaGrammar grammar,double detailfloor){
    this.detailfloor=detailfloor;
    ForsythiaComposition composition=initComposition(grammar);
    build(composition);
    return composition;}
  
  /*
   * ################################
   * BUILD
   * ################################
   */
  
  //handy reference
  public int buildcycleindex;
  
  private void build(ForsythiaComposition composition){
    boolean creatednodes=true;
    buildcycleindex=0;
    while(creatednodes){
      System.out.println("created nodes");
      buildcycleindex++;
      creatednodes=createNodes(composition);}}
   
  /*
   * ################################
   * DETAIL SIZE FLOOR
   * We test a jig and its prospective target
   * if any of the resulting polygons will have detailsize smaller 
   *   than detail size floor then we don't use that jig
   * ################################
   */
  
  private double detailfloor;
  
  /*
   * ################################
   * CREATE NODES
   * ################################
   */
  
  /*
   * try to create nodes.
   * if nodes were created then return true
   * if nodes were not created then return false
   */
  private boolean createNodes(ForsythiaComposition composition){
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
    List<Jig> jigs=fg.getJigsAboveDetailSizeFloor(target,detailfloor);
    if(jigs.isEmpty())return null;
    Jig jig=jigs.get(new Random().nextInt(jigs.size()));
    return jig;}
  
  
  /*
   * ################################
   * INIT COMPOSITION
   * ################################
   */
  
  private ForsythiaComposition initComposition(ForsythiaGrammar grammar){
    ForsythiaComposition composition=new ForsythiaComposition();
    composition.setGrammar(grammar);
    FPolygon rootpolygon=createRootPolygon(grammar);
    composition.initTree(rootpolygon);
    return composition;}
  
  /*
   * look for metagons tagged root
   * if we can't find one then pick any metagon
   */
  private FPolygon createRootPolygon(ForsythiaGrammar grammar){
    List<FMetagon> metagons=grammar.getMetagons();
    if(metagons.isEmpty())
      throw new IllegalArgumentException("this grammar has no metagons");
    List<FMetagon> rootmetagons=new ArrayList<FMetagon>();
    for(FMetagon m:metagons)
      if(m.hasTags("root"))
        rootmetagons.add(m);
    FMetagon m;
    if(!rootmetagons.isEmpty())
      m=rootmetagons.get(new Random().nextInt(rootmetagons.size()));
    else
      m=metagons.get(new Random().nextInt(metagons.size()));
    FPolygon p=new FPolygon(m);
    return p;}
  
  /*
   * ################################
   * POLYGON CAPPING UTILITY
   * mark a polygon as capped
   * this means that the polygon will not be further cultivated
   *   if encountered in the cultivation cycle we skip it
   * ################################
   */
  
  private Set<FPolygonSignature> capped=new HashSet<FPolygonSignature>();
  
  protected void cap(FPolygon polygon){
    capped.add(polygon.getSignature());}
  
  protected boolean isCapped(FPolygon polygon){
    return capped.contains(polygon.getSignature());}
  
  protected void flushCappedSet(){
    capped.clear();}
  

}

package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.fleen.forsythia.core.composition.FGrid;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.FJig;
import org.fleen.util.tree.TreeNodeIterator;

public class Composer implements Serializable{
  
  private static final long serialVersionUID=3399571378455322265L;

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
    FJig jig;
    boolean creatednodes=false;
    TreeNodeIterator i=composition.getLeafPolygonIterator();
    FPolygon leaf;
    ForsythiaGrammar grammar=(ForsythiaGrammar)composition.getGrammar();
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
  
  Map<FPolygonSignature,FJig> jigbypolygonsig=new Hashtable<FPolygonSignature,FJig>();
//  Random rnd=new Random();
  
  private FJig selectJig(ForsythiaGrammar fg,FPolygon polygon){
    //get a jig by signature
    //polygons with the same sig get the same jig
    FJig j=jigbypolygonsig.get(polygon.getSignature());
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      //
      FGrid g=(FGrid)polygon.getFirstAncestorGrid();
      double normalizeddetialfloor=detailfloor/g.getLocalKGrid().getFish();//TODO ?? does this work?? Apparently it does.
      //
      j=fg.getRandomJig(polygon.metagon,null,normalizeddetialfloor);
      if(j==null)return null;
      jigbypolygonsig.put(polygon.getSignature(),j);
      return j;}}
  
  /*
   * ################################
   * INIT COMPOSITION
   * ################################
   */
  
  private ForsythiaComposition initComposition(ForsythiaGrammar fg){
    ForsythiaComposition composition=new ForsythiaComposition();
    composition.setGrammar(fg);
    FPolygon rootpolygon=createRootPolygon(fg);
    composition.initTree(rootpolygon);
    return composition;}
  
  /*
   * look for metagons tagged root
   * if we can't find one then pick any metagon
   * if there are no metagons then exception
   */
  private FPolygon createRootPolygon(ForsythiaGrammar fg){
    //try root tag
    FMetagon m=fg.getRandomMetagon(new String[]{"root"});
    //if nope then try no tag
    if(m==null)
      m=fg.getRandomMetagon(null);
    //if still nope then
    if(m==null)
      throw new IllegalArgumentException("this grammar has no metagons");
    //
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

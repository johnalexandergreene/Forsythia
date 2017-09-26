package org.fleen.forsythia.app.bread.composer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

/*
 * given a grammar and a listener, compose forsythia structure.
 * We can create a new composition from scratch and we can also add to an existing forsythia composition
 * 
 * The composition process goes like this :
 * 
 * for each leaf polygon : target
 *   get a jig
 *   create new nodes (grid and polygons) using jig and target
 *  
 * 
 */
public abstract class Composer_Abstract implements Composer{
  
  /*
   * ################################
   * PROCESS CONTROL
   * Note that the way we've got it going here we can create a new composition from scratch and we can also build upon an existing composition
   * ################################
   */
  
  public ForsythiaComposition compose(ForsythiaGrammar grammar,double detaillimit){
    ForsythiaComposition composition=initComposition(grammar);
    build(composition,detaillimit);
    return composition;}
  
  /*
   * ################################
   * BUILD
   * ################################
   */
  
  //handy reference
  protected int buildcycleindex;
  
  private void build(ForsythiaComposition composition,double detaillimit){
    boolean creatednodes=true;
    buildcycleindex=0;
    while(creatednodes){
      System.out.println("buildcycleindex="+buildcycleindex);
      buildcycleindex++;
      creatednodes=createNodes(composition,detaillimit);}}
    
  /*
   * try to create nodes.
   * if nodes were created then return true
   * if nodes were not created then return false
   */
  protected abstract boolean createNodes(ForsythiaComposition composition,double detaillimit);
  
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

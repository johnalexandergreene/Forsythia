package org.fleen.forsythia.app.compositionGenerator.composer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.FJig;
import org.fleen.forsythia.core.grammar.FJigSection;
import org.fleen.util.tree.TreeNodeIterator;

public class Composer001_SplitBoil extends Composer_Abstract{
  
  /*
   * ################################
   * CREATE NODES
   * ################################
   */
  
  protected boolean createNodes(ForsythiaComposition composition,double detaillimit){
    FJig jig;
    boolean creatednodes=false;
    TreeNodeIterator i=composition.getLeafPolygonIterator();
    //
    FPolygon leaf;
    ForsythiaGrammar grammar=composition.getGrammar();
    while(i.hasNext()){
      leaf=(FPolygon)i.next();
      if(isCapped(leaf))continue;
      jig=selectJig(grammar,leaf,detaillimit);
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
  Random rnd=new Random();
  
  private FJig selectJig(ForsythiaGrammar forsythiagrammar,FPolygon polygon,double detaillimit){
    //get a jig by signature
    //polygons with the same sig get the same jig
    FJig j=jigbypolygonsig.get(polygon.getSignature());
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      j=getRandomJigUsingSplitBoilLogic(forsythiagrammar,polygon,detaillimit);
      if(j==null)return null;
      jigbypolygonsig.put(polygon.getSignature(),j);
      return j;}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * SPLIT BOIL LOGIC
   * get the set of prospective jigs
   * separate them into 2 lists : splitters and boilers
   * if the target is an egg then pick a splitter
   * otherwise the target is a shard, pick a boiler
   * in either case, if there is nonesuch then pick from the other list
   * 
   * ++++++++++++++++++++++++++++++++
   */
  
  private List<FJig> 
    boilers=new ArrayList<FJig>(),
    splitters=new ArrayList<FJig>();
  
  private FJig getRandomJigUsingSplitBoilLogic(ForsythiaGrammar fg,FPolygon target,double detaillimit){
    List<FJig> jigs=fg.getJigsAboveDetailSizeFloor(target,detaillimit);
    if(jigs.isEmpty())return null;
    //
    createBoilersAndSplittersLists(jigs);
    FJig jig;
//    if(target.isRootPolygon()||(rnd.nextDouble()>0.5&&target.hasTags("egg"))){
    if(target.isRootPolygon()||target.hasTags("egg")){
      jig=getRandomSplitter();
      if(jig==null)jig=getRandomBoiler();
    }else{
      jig=getRandomBoiler();
      if(jig==null)jig=getRandomSplitter();}
    return jig;}
  
  
  private FJig getRandomBoiler(){
    if(boilers.isEmpty())return null;
    FJig jig=boilers.get(rnd.nextInt(boilers.size()));
    return jig;}
  
  private FJig getRandomSplitter(){
    if(splitters.isEmpty())return null;
    FJig jig=splitters.get(rnd.nextInt(splitters.size()));
    return jig;}
  
  private void createBoilersAndSplittersLists(List<FJig> jigs){
    boilers.clear();
    splitters.clear();
    for(FJig jig:jigs){
      if(isBoiler(jig))
        boilers.add(jig);
      else
        splitters.add(jig);}}
  
  /*
   * If a jig has a section tagged "egg" then that jig is a boiler
   * and if it isn't a boiler then it's a splitter
   */
  private boolean isBoiler(FJig jig){
    for(FJigSection s:jig.sections)
      if(s.tags.hasTag("egg"))
        return true;
    return false;}


  @Override
  public ForsythiaComposition compose(){
    // TODO Auto-generated method stub
    return null;
  }
  
  
}

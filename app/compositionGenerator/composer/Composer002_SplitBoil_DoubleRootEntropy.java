package org.fleen.forsythia.app.compositionGenerator.composer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar0;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.forsythia.core.grammar.JigSection;
import org.fleen.util.tree.TreeNodeIterator;

public class Composer002_SplitBoil_DoubleRootEntropy extends Composer_Abstract{
  
  Random rnd=new Random();
  

  
  /*
   * ################################
   * COMPOSE
   * at buildcycleindex 1 and 2 (ie polygon tree level 1 and 2) we entropize the chorus indices
   * ################################
   */
  
  protected int buildcycleindex;
  
  protected void build(ForsythiaComposition composition,double detaillimit){
    boolean creatednodes=true;
    buildcycleindex=0;
    while(creatednodes){
      System.out.println("buildcycleindex="+buildcycleindex);
      if(buildcycleindex==0)entropize(composition);
      if(buildcycleindex==1)entropize(composition);
      buildcycleindex++;
      creatednodes=createNodes(composition,detaillimit);}}
  
  private void entropize(ForsythiaComposition composition){
    int a=0;
    for(FPolygon p:composition.getPolygons()){
      p.chorusindex=a;
      a++;}}
  
  /*
   * ################################
   * CREATE NODES
   * ################################
   */
  
  protected boolean createNodes(ForsythiaComposition composition,double detaillimit){
    Jig jig;
    boolean creatednodes=false;
    TreeNodeIterator i=composition.getLeafPolygonIterator();
    //
    FPolygon leaf;
    ForsythiaGrammar0 grammar=composition.getGrammar();
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
  
  Map<FPolygonSignature,Jig> jigbypolygonsig=new Hashtable<FPolygonSignature,Jig>();
  
  private Jig selectJig(ForsythiaGrammar0 forsythiagrammar,FPolygon polygon,double detaillimit){
    //get a jig by signature
    //polygons with the same sig get the same jig
    Jig j=jigbypolygonsig.get(polygon.getSignature());
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
  
  private List<Jig> 
    boilers=new ArrayList<Jig>(),
    splitters=new ArrayList<Jig>();
  
  private Jig getRandomJigUsingSplitBoilLogic(ForsythiaGrammar0 fg,FPolygon target,double detaillimit){
    List<Jig> jigs=fg.getJigsAboveDetailSizeFloor(target,detaillimit);
    if(jigs.isEmpty())return null;
    //
    createBoilersAndSplittersLists(jigs);
    Jig jig;
//    if(target.isRootPolygon()||(rnd.nextDouble()>0.5&&target.hasTags("egg"))){
    if(target.isRootPolygon()||target.hasTags("egg")){
      jig=getRandomSplitter();
      if(jig==null)jig=getRandomBoiler();
    }else{
      jig=getRandomBoiler();
      if(jig==null)jig=getRandomSplitter();}
    return jig;}
  
  
  private Jig getRandomBoiler(){
    if(boilers.isEmpty())return null;
    Jig jig=boilers.get(rnd.nextInt(boilers.size()));
    return jig;}
  
  private Jig getRandomSplitter(){
    if(splitters.isEmpty())return null;
    Jig jig=splitters.get(rnd.nextInt(splitters.size()));
    return jig;}
  
  private void createBoilersAndSplittersLists(List<Jig> jigs){
    boilers.clear();
    splitters.clear();
    for(Jig jig:jigs){
      if(isBoiler(jig))
        boilers.add(jig);
      else
        splitters.add(jig);}}
  
  /*
   * If a jig has a section tagged "egg" then that jig is a boiler
   * and if it isn't a boiler then it's a splitter
   */
  private boolean isBoiler(Jig jig){
    for(JigSection s:jig.sections)
      if(s.tags.hasTag("egg"))
        return true;
    return false;}

  @Override
  public ForsythiaComposition compose(){
    // TODO Auto-generated method stub
    return null;
  }
  
}

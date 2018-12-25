package org.fleen.forsythia.app.compositionGenerator.generators.fc0001_18hexposter;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.fleen.forsythia.app.compositionGenerator.composer.ForsythiaCompositionGen;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.forsythia.core.grammar.JigSection;
import org.fleen.util.tree.TreeNodeIterator;

public class FCGen003_SplitBoil_TotalSymmetry implements ForsythiaCompositionGen{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  FCGen003_SplitBoil_TotalSymmetry(){
    initGrammar();}
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  private static final String GRAMMARNAME="hexmandala001.grammar";
  ForsythiaGrammar grammar;
  
  private void initGrammar(){
    try{
      InputStream a=FCGen003_SplitBoil_TotalSymmetry.class.getResourceAsStream(GRAMMARNAME);
      ObjectInputStream b=new ObjectInputStream(a);
      grammar=(ForsythiaGrammar)b.readObject();
      b.close();
    }catch(Exception e){
      System.out.println("Load grammar failed.");
      e.printStackTrace();}}
  
  /*
   * ################################
   * DETAIL LIMIT
   * ################################
   */
  
  private static final double DETAILLIMIT=0.075;
  
  /*
   * ################################
   * BUILD
   * overriding the abstract here
   * at buildcycleindex 1 and 2 (ie polygon tree level 1 and 2) we entropize the chorus indices
   * ################################
   */
  
  Random rnd=new Random();
  protected int buildcycleindex;
  
  public ForsythiaComposition compose(){
    ForsythiaComposition composition=initComposition();
    boolean creatednodes=true;
    buildcycleindex=0;
    while(creatednodes){
      System.out.println("buildcycleindex="+buildcycleindex);
      buildcycleindex++;
      creatednodes=createNodes(composition,DETAILLIMIT);}
    return composition;}
  
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
  
  Map<FPolygonSignature,Jig> jigbypolygonsig=new Hashtable<FPolygonSignature,Jig>();
  
  private Jig selectJig(ForsythiaGrammar forsythiagrammar,FPolygon polygon,double detaillimit){
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
  
  private Jig getRandomJigUsingSplitBoilLogic(ForsythiaGrammar fg,FPolygon target,double detaillimit){
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
  
  /*
   * ################################
   * INIT COMPOSITION
   * ################################
   */
  
  private ForsythiaComposition initComposition(){
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

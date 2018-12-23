package org.fleen.forsythia.app.compositionGenerator.generators.fc0023_testConeflower;

import java.awt.geom.Rectangle2D;
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
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Simple.ForsythiaGrammar_Simple;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.GD;
import org.fleen.util.tree.TreeNodeIterator;

public class Composer003_SplitBoil_DoubleRootEntropy_YAxisDistanceEntropyGradient implements ForsythiaCompositionGen{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Composer003_SplitBoil_DoubleRootEntropy_YAxisDistanceEntropyGradient(){
    initGrammar();}
  
  /*
   * ################################
   * GAP
   * detail limit, boiler fat span, foambubblegap
   * different specific functions but all amount to the same. An optimal span
   * ################################
   */
  
//  static final double GAP=0.047;
  static final double GAP=0.09;
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  private static final String FORSYTHIAGRAMMARSIMPLENAME="a002.grammar";
  Coneflower_Test coneflowertestgrammar;
  
  private void initGrammar(){
    //get the forsythia grammar from local file
    ForsythiaGrammar_Simple fgs=null;
    System.out.println("LOAD FORSYTHIA GRAMMAR : "+FORSYTHIAGRAMMARSIMPLENAME);
    try{
      InputStream a=Composer003_SplitBoil_DoubleRootEntropy_YAxisDistanceEntropyGradient.class.getResourceAsStream(FORSYTHIAGRAMMARSIMPLENAME);
      ObjectInputStream b=new ObjectInputStream(a);
      fgs=(ForsythiaGrammar_Simple)b.readObject();
      b.close();
    }catch(Exception e){
      System.out.println("LOAD FORSYTHIA GRAMMAR failed.");
      e.printStackTrace();}
    //create coneflower
    coneflowertestgrammar=new Coneflower_Test(fgs);}
  
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
    
    jigbypolygonsig.clear();//????
    
    ForsythiaComposition composition=initComposition();
    initArbitraryEntropy(composition);
    boolean creatednodes=true;
    buildcycleindex=0;
    while(creatednodes){
      System.out.println("buildcycleindex="+buildcycleindex);
      if(buildcycleindex==0)entropize(composition);
      if(buildcycleindex==1)entropize(composition);
      if(buildcycleindex==2)entropize(composition);
      buildcycleindex++;
      creatednodes=createNodes(composition,GAP);}
    return composition;}
  
  private void entropize(ForsythiaComposition composition){
    int a=0;
    for(FPolygon p:composition.getPolygons()){
      p.chorusindex=a;
      a++;}}
  
  static final int ARBITRARYENTROPYINDEXSTARTVAL=1000000;
  int arbitraryentropychorusindex;
  double arbitraryentropyyaxis,arbitraryentropydistancemax;
  
  private void initArbitraryEntropy(ForsythiaComposition composition){
    arbitraryentropychorusindex=ARBITRARYENTROPYINDEXSTARTVAL;
    Rectangle2D.Double b=composition.getRootPolygon().getDPolygon().getBounds();
    arbitraryentropyyaxis=(b.getMaxY()+b.getMinY())/2;
    arbitraryentropydistancemax=b.getMaxY()-b.getMinY();}
  
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
      doArbitraryEntropy(leaf);
      if(isCapped(leaf))continue;
      jig=selectJig(grammar,leaf,detaillimit/leaf.getGrid().getLocalKGrid().getFish());
      if(jig==null){
        cap(leaf);
      }else{
        jig.createNodes(leaf);
        creatednodes=true;}}
    jigbypolygonsig.clear();
    return creatednodes;}
  
  /*
   * do entropy at probability proportional to distance from yaxis
   */
  private void doArbitraryEntropy(FPolygon leaf){
    DPoint p0=GD.getPoint_Mean(leaf.getDPolygon());
    double 
      dis=Math.abs(p0.y-arbitraryentropyyaxis),
      ndis=dis/arbitraryentropydistancemax;
    
//    System.out.println("------------------------");
//    System.out.println("arbitraryentropyyaxis="+arbitraryentropyyaxis);
//    System.out.println("p0.y="+p0.y);
//    System.out.println("dis="+dis);
//    System.out.println("ndis="+ndis);
    
//    ndis*=0.5;//looks pretty good. good constant. gonna try lower, for a skinnier entropy band
    ndis*=0.25;
      //go with natural probability first
    if(rnd.nextDouble()<ndis){
      leaf.chorusindex=arbitraryentropychorusindex;
      arbitraryentropychorusindex++;}}
  
  
  
  /*
   * ################################
   * JIG SELECTOR
   * ################################
   */
  
  Map<FPolygonSignature,Jig> jigbypolygonsig=new Hashtable<FPolygonSignature,Jig>();
  
  private Jig selectJig(ForsythiaGrammar grammar,FPolygon polygon,double detaillimit){
    //get a jig by signature
    //polygons with the same sig get the same jig
    Jig j=jigbypolygonsig.get(polygon.getSignature());
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      j=getRandomJigUsingSplitBoilLogic(grammar,polygon,detaillimit);
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
  
  private Jig getRandomJigUsingSplitBoilLogic(ForsythiaGrammar fg,FPolygon target,double gap){
    Jig jig=null;
    if(target.isRootPolygon()){
      jig=fg.getRandomJig(target.metagon,null,gap);
      return jig;}
    //
    if(target.hasTag("egg")){
      jig=fg.getRandomJig(target.metagon,new String[]{"split"},gap);
      if(jig==null)
        jig=fg.getRandomJig(target.metagon,new String[]{"boil"},gap);
    }else{
      jig=fg.getRandomJig(target.metagon,new String[]{"boil"},gap);
      if(jig==null)
        jig=fg.getRandomJig(target.metagon,new String[]{"split"},gap);}
    return jig;}
  
  
  /*
   * ################################
   * INIT COMPOSITION
   * ################################
   */
  
  private ForsythiaComposition initComposition(){
    ForsythiaComposition composition=new ForsythiaComposition();
    composition.setGrammar(coneflowertestgrammar);
    FPolygon rootpolygon=createRootPolygon(coneflowertestgrammar);
    composition.initTree(rootpolygon);
    return composition;}
  
  /*
   * look for metagons tagged root
   * if we can't find one then pick any metagon
   */
  private FPolygon createRootPolygon(ForsythiaGrammar grammar){
    FMetagon m=grammar.getRandomMetagon(new String[]{"root"});
    if(m==null)
      m=grammar.getRandomMetagon(null);
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

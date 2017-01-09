package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
import org.fleen.geom_Kisrhombille.KPolygon;

public class JigEditingModel{
  
  JigEditingModel(){
    System.out.println("init jig editing model");
    initGraph();
    initViewGeometryCache();
  }
  
  /*
   * ################################
   * GRID DENSITY
   * When we change this we re-init the graph and clear all section tags
   * ################################
   */
  
  private static final int GRIDDENSITYLOWERLIMIT=1;
  
  int griddensity=GRIDDENSITYLOWERLIMIT;
  
  void incrementGridDensity(){
    griddensity++;
    initGraph();
    initViewGeometryCache();
    chorusindexbypolygon.clear();}
  
  void decrementGridDensity(){
    if(griddensity>GRIDDENSITYLOWERLIMIT){
      griddensity--;
      initGraph();
      initViewGeometryCache();
      chorusindexbypolygon.clear();}}
  
  String getGridDensityString(){
    String s="Grid Density = "+String.format("%03d",griddensity);
    return s;}
  
  /*
   * ################################
   * JIG SECTION POLYGONS
   * ################################
   */
  
  
  
  
  
  /*
   * ################################
   * GRAPH
   * add, remove and connect vertices
   * interpret as disconnectedgraph
   * ################################
   */
  
  public RawGraph rawgraph;
  
  void initGraph(){
    rawgraph=new RawGraph(GE.focusmetagon.kpolygon,griddensity);}
  
  
  
  /*
   * ################################
   * JIG TAGS
   * ################################
   */
  
  //space delimited
  String jigtagstring="";
  
  List<String> getJigTags(){
    List<String> t=Arrays.asList(jigtagstring.split(" "));
    return t;}
  
  /*
   * ################################
   * SECTION TAGS
   * ################################
   */
  
  /*
   * ################################
   * SECTION ANCHORS
   * ################################
   */
  
  /*
   * ################################
   * SECTION POLYGON CHORUS INDICES
   * TODO this should be done in the section model
   * ################################
   */
  
  private Map<KPolygon,Integer> chorusindexbypolygon=new HashMap<KPolygon,Integer>();
  
  boolean hasChorusIndex(KPolygon p){
    return chorusindexbypolygon.get(p)!=null;}
  
  void initChorusindex(KPolygon p){
    chorusindexbypolygon.put(p,getLowestUnusedChorusIndex());}
  
  int getChorusIndex(KPolygon p){
    return chorusindexbypolygon.get(p);}
  
  void setChorusIndex(KPolygon p,int i){
    chorusindexbypolygon.put(p,i);}
  
  private int getLowestUnusedChorusIndex(){
    Integer i=0;
    while(!chorusindexbypolygon.values().contains(i))
      i++;
    return i;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public ViewGeometryCache viewgeometrycache;
  
  //TODO a clear method instead?
  void initViewGeometryCache(){
    viewgeometrycache=new ViewGeometryCache();
  }
  
  /*
   * ################################
   * JIG SECTION MODELS
   * ################################
   */
  
  public Map<KPolygon,JigSectionEditingModel> sectionmodelbypolygon=new HashMap<KPolygon,JigSectionEditingModel>();
  
  public List<KPolygon> getSectionPolygons(){
    return rawgraph.getDisconnectedGraph().getUndividedPolygons();}
  
  public void clearSectionPolygons(){
    sectionmodelbypolygon.clear();}
  
  public JigSectionEditingModel getSectionModel(KPolygon sectionpolygon){
    JigSectionEditingModel m=sectionmodelbypolygon.get(sectionpolygon);
    if(m==null){
      m=new JigSectionEditingModel(this,sectionpolygon);
      sectionmodelbypolygon.put(sectionpolygon,m);}
    return m;}
  
  public int getMaxChorus(){
    int m=rawgraph.getDisconnectedGraph().getUndividedPolygons().size();
    return m;}
  
  public void setSectionTags(KPolygon sectionpolygon,String tags){
    JigSectionEditingModel m=sectionmodelbypolygon.get(sectionpolygon);
    m.tags=tags;}
  
  /*
   * ################################
   * CREATE JIG
   * ################################
   */
  
  ProjectJig getProjectJig(){
    List<KPolygon> sectionpolygons=rawgraph.getDisconnectedGraph().getUndividedPolygons();
    ProjectJig j=new ProjectJig(
      GE.focusmetagon,
      griddensity,
      sectionpolygons);
    return j;}
  
  
  
  
  
  
  
  

}

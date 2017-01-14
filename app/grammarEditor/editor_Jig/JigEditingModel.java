package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;

public class JigEditingModel{
  
  JigEditingModel(){
    initGraph();
    initViewGeometryCache();}
  
  /*
   * ################################
   * GRID DENSITY
   * When we change this we re-init the graph and clear all section data
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
    return String.format("%03d",griddensity);}
  
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
   * JIG SECTION MODELS AND SECTION EDITING MODE
   * Our editor has 2 modes
   *   geometry editing mode
   *   section editing mode
   * ################################
   */
  
  public List<JigSectionEditingModel> sections=new ArrayList<JigSectionEditingModel>();
  List<ProjectMetagon> localsectionmetagons=new ArrayList<ProjectMetagon>();
  
  /*
   * for each undivided polygon in the graph (rawpolygons)
   *   get a metagon
   *     metagons are singular within the grammar. 
   *     There is exactly 1 metagon for each shape. One triangle, hexagon, 2x4 rectangle, 2x4x2x6 T-shape, etc. 
   */
  public void initSections(){
    sections.clear();
    localsectionmetagons.clear();
    List<KPolygon> rawpolygons=rawgraph.getDisconnectedGraph().getUndividedPolygons();
    List<KPolygon> cleanpolygons=getCleanedSectionPolygons(rawpolygons);
    List<JigSectionModelComponents> sectionmodelcomponents=getJigSectionModelComponents(cleanpolygons);
    for(JigSectionModelComponents smc:sectionmodelcomponents)
      sections.add(new JigSectionEditingModel(this,smc));}
  
  /*
   * cull redundant colinear vertices
   */
  List<KPolygon> getCleanedSectionPolygons(List<KPolygon> rawpolygons){
    List<KPolygon> cleanpolygons=new ArrayList<KPolygon>(rawpolygons.size());
    KPolygon clean;
    for(KPolygon raw:rawpolygons){
      clean=new KPolygon(raw);
      clean.removeRedundantColinearVertices();
      cleanpolygons.add(clean);}
    return cleanpolygons;}
  
  /*
   * for each polygon
   *   get a metagon and a list of anchors
   *   test the polygon against every metagon in the grammar
   *     if a metagon is found, we can fit it to the polygon and we have some working anchors, then use that.
   *   test the polygon against every metagon that we have created in this jig
   *     if a metagon is found then use that and the associated anchors.
   *   if no metagon has been found then create one.
   * 
   */
  private List<JigSectionModelComponents> getJigSectionModelComponents(List<KPolygon> polygons){
    List<JigSectionModelComponents> components=new ArrayList<JigSectionModelComponents>(polygons.size());
    for(KPolygon polygon:polygons)
      components.add(getJigSectionModelComponents(polygon));
    return components;}
  
  private JigSectionModelComponents getJigSectionModelComponents(KPolygon polygon){
    JigSectionModelComponents c=getComponentsFromGrammar(polygon);
    if(c!=null)return c;
    c=getComponentsLocally(polygon);
    if(c!=null)return c;
    c=createComponents(polygon);
    return c;}
  
  /*
   * check the metagons in the project grammar 
   * see if one of them fits the specified polygon. 
   * If a fit is found then return that metagon and anchors. 
   * Otherwise return null.
   */
  private JigSectionModelComponents getComponentsFromGrammar(KPolygon polygon){
    List<KAnchor> anchors;
    for(ProjectMetagon metagon:GE.focusgrammar.metagons){
      anchors=metagon.kmetagon.getAnchorOptions(polygon);
      if(anchors!=null)
        return new JigSectionModelComponents(metagon,anchors);}
    return null;}
  
  /*
   * check the metagons in the list of metagons created within this jig
   * see if one of them fits the specified polygon. 
   * If a fit is found then return that metagon and anchors. 
   * Otherwise return null.
   */
  private JigSectionModelComponents getComponentsLocally(KPolygon polygon){
    List<KAnchor> anchors;
    for(ProjectMetagon metagon:localsectionmetagons){
      anchors=metagon.kmetagon.getAnchorOptions(polygon);
      if(anchors!=null)
        return new JigSectionModelComponents(metagon,anchors);}
    return null;}
  
  private JigSectionModelComponents createComponents(KPolygon polygon){
    KMetagon m=polygon.getKMetagon();
    List<KAnchor> anchors=m.getAnchorOptions(polygon);
    ProjectMetagon pm=new ProjectMetagon(GE.focusgrammar,polygon,"");
    localsectionmetagons.add(pm);
    JigSectionModelComponents c=new JigSectionModelComponents(pm,anchors);
    return c;}
  
//  public List<KPolygon> getSectionPolygons(){
//    List<KPolygon> polygons=new ArrayList<KPolygon>(sections.size());
//    for(JigSectionEditingModel m:sections)
//      polygons.add(m.getPolygon());
//    return polygons;}
  
  public int getMaxChorus(){
    int m=rawgraph.getDisconnectedGraph().getUndividedPolygons().size();
    return m;}
  
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

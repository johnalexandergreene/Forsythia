package org.fleen.forsythia.app.grammarEditor.editor_Jig.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.ViewGeometryCache;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * A model of an edited jig
 */
public class JigEditingModel{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public JigEditingModel(ProjectMetagon targetmetagon){
    this.targetmetagon=targetmetagon;
    initGraph();
    initViewGeometryCache();}
  
  /*
   * ################################
   * TARGET METAGON
   * ################################
   */
  
  public ProjectMetagon targetmetagon;
  
  /*
   * ################################
   * GRID DENSITY
   * When we change this we re-init the graph and clear all section data
   * ################################
   */
  
  private static final int GRIDDENSITYLOWERLIMIT=1;
  
  public int griddensity=GRIDDENSITYLOWERLIMIT;
  
  public void incrementGridDensity(){
    griddensity++;
    initGraph();
    initViewGeometryCache();}
  
  public void decrementGridDensity(){
    if(griddensity>GRIDDENSITYLOWERLIMIT){
      griddensity--;
      initGraph();
      initViewGeometryCache();}}
  
  public String getGridDensityString(){
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
  
  public String jigtagstring="";
  
  public List<String> getJigTags(){
    List<String> t=Arrays.asList(jigtagstring.split(" "));
    return t;}
  
  /*
   * ################################
   * VIEW GEOMETRY CACHE
   * all the polygons and whatever that we use to create our UI stuff
   * ################################
   */
  
  public ViewGeometryCache viewgeometrycache=new ViewGeometryCache();
  
  void initViewGeometryCache(){
    viewgeometrycache.invalidate();}
  
  /*
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   * JIG SECTION MODELS
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   */
  
  //all of our sections
  public List<JigSectionEditingModel> sections=new ArrayList<JigSectionEditingModel>();
  //metagons that got created indirectly via section polygon creation
  public List<ProjectMetagon> localsectionmetagons=new ArrayList<ProjectMetagon>();
  
  /*
   * ################################
   * SECTION CHORUS INDICES
   * A little management
   * we have a max chorus index
   * We disallow nonisomorphic sections having the same chorus
   * ################################
   */
  
  public int getMaxSectionChorusIndex(){
    return sections.size();}
  
  private ProjectMetagon getMetagonByChorusIndex(int chorusindex){
    for(JigSectionEditingModel section:sections)
      if(section.chorusindex==chorusindex)
        return section.metagon;
    return null;}
  
  /*
   * cycle through the range of chorus indices until one is found that is not 
   * presently in use by a section with a differing metagon
   */
  public int getNextValidChorusIndex(JigSectionEditingModel model,int chorusindex){
    boolean valid=false;
    ProjectMetagon test;
    int maxtries=getMaxSectionChorusIndex()*2,tries=0;
    while(!valid){
      //to avoid an infinite loop
      maxtries++;
      tries++;
      if(tries==maxtries)
        throw new IllegalArgumentException("infinite loop");
      //  
      chorusindex++;
      if(chorusindex>getMaxSectionChorusIndex())
        chorusindex=0;
      test=getMetagonByChorusIndex(chorusindex);
      if(test==null||test==model.metagon)
        valid=true;}
    return chorusindex;}
  
  /*
   * ################################
   * INIT SECTIONS
   * We are shifting modes, from geometry editing to section editing
   * From the geometry we derive our section polygons
   * Then we init metagons, anchors and chorus indices
   * ################################
   */
  
  /*
   * for each undivided polygon in the graph (rawpolygons)
   * get a metagon, a list of anchors and a chorus index 
   */
  public void initSections(){
    sections.clear();
    localsectionmetagons.clear();
    List<KPolygon> rawpolygons=rawgraph.getDisconnectedGraph().getUndividedPolygons();
    List<KPolygon> cleanpolygons=getCleanedSectionPolygonsForSectionsInit(rawpolygons);
    List<JigSectionModelMetagonAndAnchors> metagonandanchors=getJigSectionModelComponentsForSectionsInit(cleanpolygons);
    List<Integer> sectionmodelchorusindices=getJigSectionModelChorusIndicesForSectionsInit(metagonandanchors);
    //
    int sectioncount=rawpolygons.size();
    JigSectionModelMetagonAndAnchors ma;
    int chorusindex;
    for(int i=0;i<sectioncount;i++){
      ma=metagonandanchors.get(i);
      chorusindex=sectionmodelchorusindices.get(i);
      sections.add(new JigSectionEditingModel(this,ma,chorusindex));}}
  
  /*
   * cleaning the polygon means culling redundant colinear vertices
   */
  List<KPolygon> getCleanedSectionPolygonsForSectionsInit(List<KPolygon> rawpolygons){
    List<KPolygon> cleanpolygons=new ArrayList<KPolygon>(rawpolygons.size());
    KPolygon clean;
    for(KPolygon raw:rawpolygons){
      clean=new KPolygon(raw);
      clean.removeRedundantColinearVertices();
      cleanpolygons.add(clean);}
    return cleanpolygons;}
  
  /*
   * ################################
   * INIT JIG SECTION MODEL CHORUS INDICES
   * specify max chorus index (section count)
   * set init chorus indices for sections
   * 
   * at init, isomorphic sections (same metagon) get the same index and nonisomorphic get differing indices.
   * ################################ 
   */
  
  private int highestindexsofar;//because we can't pass ints
  
  private List<Integer> getJigSectionModelChorusIndicesForSectionsInit(List<JigSectionModelMetagonAndAnchors> sectionmodelmetagonandanchors){
    List<Integer> chorusindices=new ArrayList<Integer>();
    Map<ProjectMetagon,Integer> indicesbymetagon=new HashMap<ProjectMetagon,Integer>();
    int chorusindex;
    highestindexsofar=0;
    for(JigSectionModelMetagonAndAnchors ma:sectionmodelmetagonandanchors){
      chorusindex=getChorusIndexForSectionsInit(ma.metagon,indicesbymetagon);
      chorusindices.add(chorusindex);}
    return chorusindices;}
  
  private int getChorusIndexForSectionsInit(ProjectMetagon m,Map<ProjectMetagon,Integer> indicesbymetagon){
    Integer i=indicesbymetagon.get(m);
    if(i!=null)return i;
    i=highestindexsofar;
    indicesbymetagon.put(m,highestindexsofar);
    highestindexsofar++;
    return i;}
  
  /*
   * ################################
   * JIG SECTION MODEL COMPONENTS FOR SECTION INIT
   * Specifically, the metagon and the list of anchors
   * Get them for init here
   * 
   * for each polygon
   *   get a metagon and a list of anchors
   *   test the polygon against every metagon in the grammar
   *     if a metagon is found, we can fit it to the polygon and we have some working anchors, then use that.
   *   test the polygon against every metagon that we have created in this jig
   *     if a metagon is found then use that and the associated anchors.
   *   if no metagon has been found then create one.
   * ################################
   */
  
  private List<JigSectionModelMetagonAndAnchors> getJigSectionModelComponentsForSectionsInit(List<KPolygon> polygons){
    List<JigSectionModelMetagonAndAnchors> components=new ArrayList<JigSectionModelMetagonAndAnchors>(polygons.size());
    for(KPolygon polygon:polygons)
      components.add(getJigSectionModelComponents(polygon));
    return components;}
  
  private JigSectionModelMetagonAndAnchors getJigSectionModelComponents(KPolygon polygon){
    JigSectionModelMetagonAndAnchors c=getComponentsFromGrammar(polygon);
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
  private JigSectionModelMetagonAndAnchors getComponentsFromGrammar(KPolygon polygon){
    List<KAnchor> anchors;
    for(ProjectMetagon metagon:GE.focusgrammar.metagons){
      anchors=metagon.kmetagon.getAnchorOptions(polygon);
      if(anchors!=null)
        return new JigSectionModelMetagonAndAnchors(metagon,anchors);}
    return null;}
  
  /*
   * check the metagons in the list of metagons created within this jig
   * see if one of them fits the specified polygon. 
   * If a fit is found then return that metagon and anchors. 
   * Otherwise return null.
   */
  private JigSectionModelMetagonAndAnchors getComponentsLocally(KPolygon polygon){
    List<KAnchor> anchors;
    for(ProjectMetagon metagon:localsectionmetagons){
      anchors=metagon.kmetagon.getAnchorOptions(polygon);
      if(anchors!=null)
        return new JigSectionModelMetagonAndAnchors(metagon,anchors);}
    return null;}
  
  private JigSectionModelMetagonAndAnchors createComponents(KPolygon polygon){
    KMetagon m=polygon.getKMetagon();
    List<KAnchor> anchors=m.getAnchorOptions(polygon);
    ProjectMetagon pm=new ProjectMetagon(GE.focusgrammar,polygon,"");
    localsectionmetagons.add(pm);
    JigSectionModelMetagonAndAnchors c=new JigSectionModelMetagonAndAnchors(pm,anchors);
    return c;}
  
}

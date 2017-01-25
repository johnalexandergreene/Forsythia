package org.fleen.forsythia.app.grammarEditor.project.jig;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.metagon.ProjectMetagon;
import org.fleen.forsythia.app.grammarEditor.util.ElementMenuItem;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.forsythia.core.grammar.JigSection;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.graph.Graph;

/*
 * A Forsythia Jig in an editor-useful form
 * easy to edit
 * cached graphics for rendering in grammar editor
 * cached geometry for rendering in jig editor
 * stuff for import and export
 */
public class ProjectJig implements ElementMenuItem{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //create
  public ProjectJig(
    ProjectMetagon jiggedmetagon){
    this.jiggedmetagon=jiggedmetagon;
    initGraph();}
  
  //import
  public ProjectJig(ProjectMetagon jiggedmetagon,Jig jig){
    this.jiggedmetagon=jiggedmetagon;
    initForImport(jig);}
  
  /*
   * ################################
   * INIT FOR IMPORT
   * ################################
   */
  
  /*
   * At this point the project metagons list should be fully populated
   * Any metagon found in a jig (target or product) should be found in that list
   * if it isn't then we have an exception 
   */
  public void initForImport(Jig jig){
    this.griddensity=jig.griddensity;
    setTags(jig.getTags());
    //
    ProjectJigSection pjspolygon;
    for(JigSection js:jig.sections){
      if(js instanceof JigSection){
         pjspolygon=new ProjectJigSection(this,(JigSection)js);
         sections.add(pjspolygon);}}
    ((ArrayList<ProjectJigSection>)sections).trimToSize();}
  
  /*
   * ################################
   * JIGGED METAGON
   * The metagon that this Jig jigs
   * (well, specifically, the class of polygon that this Jig jigs, 
   * class==metagon in this case)
   * ################################
   */ 
  
  public ProjectMetagon jiggedmetagon;
  
  /*
   * ################################
   * GRID DENSITY
   * ################################
   */ 
  
  private static final int GRIDDENSITYLOWERLIMIT=1;
  
  public int griddensity=GRIDDENSITYLOWERLIMIT;
  
  public void incrementGridDensity(){
    griddensity++;
    initGraph();
    invalidateJigEditorGeometryCache();}
  
  public void decrementGridDensity(){
    if(griddensity>GRIDDENSITYLOWERLIMIT){
      griddensity--;
      initGraph();
      invalidateJigEditorGeometryCache();}}
  
  public String getGridDensityString(){
    return String.format("%03d",griddensity);}
  
  public double getFishFactor(){
    return 1.0/(double)griddensity;}
  
  /*
   * ################################
   * GRAPH
   * add, remove and connect vertices
   * interpret as disconnectedgraph
   * used in jig editor
   * ################################
   */
  
  public Graph graph;
  
  void initGraph(){
    graph=new Graph(jiggedmetagon.kmetagon.getPolygon(griddensity,true));}
  
  /*
   * ################################
   * TAGS
   * Space delimited strings
   * The descriptive tags of this jig
   * used for filtering jigs
   * ################################
   */
  
  public String tags="";
  
  private void setTags(List<String> tags){
    if(tags==null||tags.isEmpty()){
      this.tags="";
      return;}
    StringBuffer a=new StringBuffer();
    int s=tags.size();
    String tag;
    for(int i=0;i<s;i++){
      tag=tags.get(i);
      a.append(tag);
      if(i<s-1)
        a.append(" ");}
    this.tags=a.toString();}
  
  public String[] getTagsForExport(){
    if(tags.equals(""))return new String[0];
    String[] a=tags.split(" ");
    return a;}
  
  /*
   * ################################
   * SECTIONS
   * ################################
   */
  
  public List<ProjectJigSection> sections=new ArrayList<ProjectJigSection>();

  //returns true of any of our sections uses the specified metagon for its product
  public boolean usesInSection(ProjectMetagon m){
    for(ProjectJigSection pjs:sections)
      if(pjs.metagon==m)
        return true;
    return false;}
  
  /*
   * ################################
   * SECTION CHORUS INDICES MANAGEMENT
   * we have a max chorus index
   * We disallow nonisomorphic sections from having the same chorus
   * ################################
   */
  
  public int getMaxSectionChorusIndex(){
    return sections.size();}
  
  private ProjectMetagon getMetagonByChorusIndex(int chorusindex){
    for(ProjectJigSection section:sections)
      if(section.chorusindex==chorusindex)
        return section.metagon;
    return null;}
  
  /*
   * cycle through the range of chorus indices until one is found that is not 
   * presently in use by a section with a differing metagon
   */
  int getNextValidChorusIndex(ProjectJigSection section,int chorusindex){
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
      if(test==null||test==section.metagon)
        valid=true;}
    return chorusindex;}
  
  /*
   * ################################
   * GRAMMAR EDITOR ICON IMAGE CACHE
   * This is the image for that jig menu at the bottom of the
   * Grammar Editor UI.
   * It is invalidated on ui resize.
   * ################################
   */
  
  BufferedImage grammareditoriconimage=null;
  
  //implementation of UIElementMenuElement interface
  public BufferedImage getGrammarEditorIconImage(int span){
    if(grammareditoriconimage==null)
      grammareditoriconimage=new GrammarEditorIconImage(this,span);
    return grammareditoriconimage;}
  
  public void invalidateGrammarEditorIconImage(){
    grammareditoriconimage=null;}
  
  /*
   * ################################
   * JIG EDITOR 2D GEOMETRY CACHE
   * This is the 2d geometry upon which is based the jig graphics
   * in the jig editor
   * ################################
   */
  
  public JigEditorGeometryCache jigeditorgeometrycache=new JigEditorGeometryCache();
  
  void invalidateJigEditorGeometryCache(){
    jigeditorgeometrycache.invalidate();}
  
  /*
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   * DERIVE SECTIONS FROM GRAPH
   * From the graph derive section polygons
   * For each section get metagon, chorus index,anchor and tags
   * 
   * This is used in the jig editor
   * first we draw our graph then from that graph we derive polygons and then weuse those polygons for sections 
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################ 
   */
  
  //metagons that got created locally, indirectly, via section polygon creation
  public List<ProjectMetagon> localsectionmetagons=new ArrayList<ProjectMetagon>();
  
  /*
   * for each undivided polygon in the graph (rawpolygons)
   * get a metagon, a list of anchors and a chorus index 
   */
  public void deriveSectionsFromGraph(){
    sections.clear();
    localsectionmetagons.clear();
    List<KPolygon> rawpolygons=graph.getDisconnectedGraph().getUndividedPolygons();
    List<KPolygon> cleanpolygons=getCleanedSectionPolygonsForSectionsInit(rawpolygons);
    List<MetagonAndAnchors> metagonandanchors=getJigSectionModelComponentsForSectionsInit(cleanpolygons);
    List<Integer> sectionmodelchorusindices=getJigSectionModelChorusIndicesForSectionsInit(metagonandanchors);
    //
    int sectioncount=rawpolygons.size();
    MetagonAndAnchors ma;
    int chorusindex;
    for(int i=0;i<sectioncount;i++){
      ma=metagonandanchors.get(i);
      chorusindex=sectionmodelchorusindices.get(i);
      sections.add(new ProjectJigSection(this,ma,chorusindex));}}
  
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
   * ++++++++++++++++++++++++++++++++
   * INIT JIG SECTION CHORUS INDICES
   * specify max chorus index (section count)
   * set init chorus indices for sections
   * 
   * at init, isomorphic sections (same metagon) get the same index and nonisomorphic get differing indices.
   * ++++++++++++++++++++++++++++++++
   */
  
  private int highestindexsofar;//because we can't pass ints
  
  private List<Integer> getJigSectionModelChorusIndicesForSectionsInit(List<MetagonAndAnchors> sectionmodelmetagonandanchors){
    List<Integer> chorusindices=new ArrayList<Integer>();
    Map<ProjectMetagon,Integer> indicesbymetagon=new HashMap<ProjectMetagon,Integer>();
    int chorusindex;
    highestindexsofar=0;
    for(MetagonAndAnchors ma:sectionmodelmetagonandanchors){
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
   * ++++++++++++++++++++++++++++++++
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
   * ++++++++++++++++++++++++++++++++
   */
  
  private List<MetagonAndAnchors> getJigSectionModelComponentsForSectionsInit(List<KPolygon> polygons){
    List<MetagonAndAnchors> components=new ArrayList<MetagonAndAnchors>(polygons.size());
    for(KPolygon polygon:polygons)
      components.add(getJigSectionModelComponents(polygon));
    return components;}
  
  private MetagonAndAnchors getJigSectionModelComponents(KPolygon polygon){
    MetagonAndAnchors c=getComponentsFromGrammar(polygon);
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
  private MetagonAndAnchors getComponentsFromGrammar(KPolygon polygon){
    List<KAnchor> anchors;
    for(ProjectMetagon metagon:GE.ge.focusgrammar.metagons){
      anchors=metagon.kmetagon.getAnchorOptions(polygon);
      if(anchors!=null)
        return new MetagonAndAnchors(metagon,anchors);}
    return null;}
  
  /*
   * check the metagons in the list of metagons created within this jig
   * see if one of them fits the specified polygon. 
   * If a fit is found then return that metagon and anchors. 
   * Otherwise return null.
   */
  private MetagonAndAnchors getComponentsLocally(KPolygon polygon){
    List<KAnchor> anchors;
    for(ProjectMetagon metagon:localsectionmetagons){
      anchors=metagon.kmetagon.getAnchorOptions(polygon);
      if(anchors!=null)
        return new MetagonAndAnchors(metagon,anchors);}
    return null;}
  
  private MetagonAndAnchors createComponents(KPolygon polygon){
    KMetagon m=polygon.getKMetagon();
    List<KAnchor> anchors=m.getAnchorOptions(polygon);
    ProjectMetagon pm=new ProjectMetagon(GE.ge.focusgrammar,polygon,"");
    localsectionmetagons.add(pm);
    MetagonAndAnchors c=new MetagonAndAnchors(pm,anchors);
    return c;}
  
}

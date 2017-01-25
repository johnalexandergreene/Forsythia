package org.fleen.forsythia.app.grammarEditor.project.metagon;

import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.project.ProjectGrammar;
import org.fleen.forsythia.app.grammarEditor.project.jig.JigEditorGeometryCache;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.util.ElementMenuItem;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.geom_Kisrhombille.graph.Graph;

/*
 * This is
 *   a metagon definition
 *   tags 
 *   protojigs
 *   project stuff
 * 
 * the geometry is immutable
 * we get them at import, via the metagon creator or via the jig creator
 * associations with jigs and jigsections are defined in the grammar
 */
public class ProjectMetagon implements Serializable,ElementMenuItem{
  
  private static final long serialVersionUID=-238762044761078848L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //create in metagon editor
  public ProjectMetagon(ProjectGrammar grammar){
    this.grammar=grammar;
    initGraph();}
  
  //create in jig editor section creation 
  public ProjectMetagon(ProjectGrammar grammar,List<KVertex> vertices,String tags){
    this.grammar=grammar;
    initGeometry(vertices);
    this.tags=tags;}
  
  //import
  public ProjectMetagon(ProjectGrammar grammar,FMetagon fm){
    this.grammar=grammar;
    initGeometry(fm.getPolygon());
    setTagsForImport(fm.getTags());}
  
  /*
   * ################################
   * METAGON EDITOR 2D GEOMETRY CACHE
   * This is the 2d geometry upon which is based the metagon graphics
   * in the metagon editor
   * ################################
   */
  
  public MetagonEditorGeometryCache metagoneditorgeometrycache=new MetagonEditorGeometryCache();
  
  void invalidateJigEditorGeometryCache(){
    metagoneditorgeometrycache.invalidate();}
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  ProjectGrammar grammar;
  
  /*
   * ################################
   * GRAPH
   * add, remove and connect vertices
   * interpret as disconnectedgraph
   * used in metagon editor
   * ################################
   */
  
  private Graph graph=null;
  
  private void initGraph(){
    graph=new Graph();}
  
  public Graph getGraph(){
    if(graph==null)graph=new Graph(kpolygon);
    return graph;}
  
  /*
   * a valid graph describes 1 polygon
   */
  public boolean getGraphValidity(){
    int a=graph.getDisconnectedGraph().getPolygons().size();
    if(a!=1)return false;
    a=graph.getDisconnectedGraph().getOpenKVertexSequences().size();
    if(a!=0)return false;
    return true;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  //we hold all this stuff because we'll need it eventually
  //we init it asap
  public KMetagon kmetagon;
  public KPolygon kpolygon;
  public DPolygon polygon2d;
  
  private void initGeometry(List<KVertex> vertices){
    kmetagon=new KMetagon(vertices);
    kpolygon=kmetagon.getPolygon();
    polygon2d=kpolygon.getDefaultPolygon2D();}
  
  /*
   * derive the geometry from the graph
   * we drew the graph in the metagon editor
   */
  public void initGeometryForMetagonEditorCreate(){
    kpolygon=graph.getDisconnectedGraph().getPolygons().get(0);
    kmetagon=new KMetagon(kpolygon);
    polygon2d=kpolygon.getDefaultPolygon2D();}
  
  /*
   * ################################
   * TAGS
   * ################################
   */
  
  public String tags="";
  
  private void setTagsForImport(List<String> tags){
    if(tags.isEmpty()){
      this.tags="";
      return;}
    StringBuffer a=new StringBuffer();
    for(String b:tags)
      a.append(b+" ");
    a.delete(a.length()-1,a.length());
    this.tags=a.toString();}
  
  public String[] getTagsForExport(){
    if(tags.equals(""))return new String[0];
    String[] a=tags.split(" ");
    return a;}
  
  /*
   * ################################
   * JIGS
   * ################################
   */
  
  public List<ProjectJig> jigs=new ArrayList<ProjectJig>();
  
  Comparator<ProjectJig> ProjectJigComparator=new Comparator<ProjectJig>(){
    public int compare(ProjectJig j0,ProjectJig j1){
      int h0=j0.hashCode(),h1=j1.hashCode();
      if(h0==h1){
        return 0;
      }else if(h0<h1){
        return -1;}
      return 1;}};
  
  public List<ProjectJig> getJigs(){
    return jigs;}
      
  public ProjectJig getJig(int i){
    if(jigs.isEmpty())return null;
    return jigs.get(i);}
  
  public boolean addJig(ProjectJig j){
    if(jigs.contains(j))return false;
    jigs.add(j);
    Collections.sort(jigs,ProjectJigComparator);
    return true;}
  
  public boolean discardJig(ProjectJig j){
    return jigs.remove(j);}
  
  public int getJigCount(){
    return jigs.size();}
  
  public boolean hasJigs(){
    return !jigs.isEmpty();}
  
  public int getJigIndex(ProjectJig j){
    return jigs.indexOf(j);}
  
  /*
   * ################################
   * INFO
   * ################################
   */
  
  /*
   * An isolated metagon is a metagon for which no jig.jigsection in this grammar has a reference
   * That is, there's no way for such a metagon to arise in a conversation except by direct instantiation.
   * 
   * check every section of every jig of every metagon in the grammar
   * if none of them has this metagon for its product then this metagon is isolated
   */
  public boolean isIsolated(){
    for(ProjectMetagon m:grammar.metagons)
      for(ProjectJig j:m.jigs)
        if(j.usesInSection(this))
          return false;
    return true;}
  
  public boolean isJigless(){
    return jigs.isEmpty();}
  
  /*
   * ################################
   * GRAPHICS
   * projectmetagon gets graphically rendered in one place: Overview top element menu. As an icon.
   * It shares rendering components with the metagoneditor.
   * imagepaths is cached and immutable.
   * overviewiconimage is cached. Invalidated on ui resize.
   * ################################
   */
  
  //simple, a closed polygonal path.
  //using the metagon's default polygon.
  //It describes a metagon well enough.
  Path2D.Double imagepath;
  //this is the metagon icon in the top menu
  BufferedImage iconimage=null;
  
  public Path2D.Double getImagePath(){
    if(imagepath==null)
      imagepath=UI.getClosedPath(polygon2d);
    return imagepath;}
  
  //implementation of UIElementMenuElement interface
  public BufferedImage getGrammarEditorIconImage(int span){
    if(iconimage==null)
      iconimage=new ProjectMetagonEditGrammarIconImage(this,span);
    return iconimage;}
  
  public void invalidateOverviewIconImage(){
    iconimage=null;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public int hashCode(){
    return kmetagon.hashCode();}
  
  public boolean equals(Object a){
    ProjectMetagon b=(ProjectMetagon)a;
    return kmetagon.equals(b.kmetagon);}

}

package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.model.JigSectionEditingModel;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.ui.EJ_UI;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJigSection;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * A jig is defined by it's grid density, graph, jig tags, jigsections, jigsection tags, jigsection anchors and jigsection chorus indices
 * 
 * For editing purposes we employ a jig editing model
 *   The model contains all that
 *   
 * 
 *   
 * 
 * For every created section we compare the resulting metagon with our existing set of metagons
 * If a match is found then we use that metagon to define a section.
 * If no match is found then we create a new metagon.
 * 
 * 
 * ---------------------------
 * given an empty grid
 * click a vertex. this adds a vertex : v0
 * click another vertex, v1. this draws a line from v0 to v1
 * (if you click a vertex again it turns white. 
 *   this means that clicking the next vertex will not connect. 
 *   click a white vertex and the vertex is deleted)
 * click and connect vertices until we have a polygon
 * split the polygon to get 2 polygons and so on
 * 
 * whenever a polygon is created we register it and initialize tags, anchor and chorusindex
 * 
 * 
 * 
 * 
 */
public class Editor_Jig extends Editor{
  
  private static final long serialVersionUID=5782373205247413080L;

  private static final String NAME="Jig";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_Jig(){
    super(NAME);}
  
  /*
   * ################################
   * CONFIGURE
   * for editor open and close
   * ################################
   */

  public void configureForOpen(){
    createEditingObjects();
    setModeEditGeometry();
    refreshUI();}
  
  public void configureForClose(){
    discardEditingObjects();}
  
  /*
   * ################################
   * MODE
   * We have 2 modes
   *   edit geometry
   *   edit sections
   * We start in edit geometry
   * When we are finished editing geometry we hit the mode button, thus setting the mode to edit sections
   * If, while editing sections, we decide that we don't like the geometry, we can hit the mode button again,
   *   thus discarding all section data and returning to the edit geometry mode.
   * ################################
   */
  
  public static final int
    MODE_UNDEFINED=0,
    MODE_EDITGEOMETRY=1,
    MODE_EDITSECTIONS=2;
  
  public int mode=MODE_UNDEFINED;
  
  void setModeEditGeometry(){
    mode=MODE_EDITGEOMETRY;
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangriddensity.setEnabled(true);
    ui.btnsectionanchor.setVisible(false);
    ui.btnsectionchorus.setVisible(false);
    ui.pansectiontags.setVisible(false);
    //
    setFocusSection(null);
    //
    refreshGridGeometryAndImage();
    initGridPerspective();
    refreshGridGeometryAndImage();
    refreshButtons();}
  
  void setModeEditSections(){
    mode=MODE_EDITSECTIONS;
    jig.initSections();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangriddensity.setEnabled(false);
    ui.btnsectionanchor.setVisible(true);
    ui.btnsectionchorus.setVisible(true);
    ui.pansectiontags.setVisible(true);
    focussection=jig.sections.get(0);
    //
    refreshGridGeometryAndImage();
    initGridPerspective();
    refreshGridGeometryAndImage();
    refreshButtons();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  protected JPanel createUI(){
    return new EJ_UI();}
  
  void initGridPerspective(){
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.centerAndFit();}
  
  public void refreshUI(){
    refreshGridGeometryAndImage();
    refreshButtons();}

  public void refreshGridImage(){
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.repaint();}
  
  public void refreshGridGeometryAndImage(){
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.repaint();}
  
  void refreshButtons(){
    EJ_UI ui=(EJ_UI)getUI();
    //refresh jig stuff
    ui.pangriddensity.lblgriddensity.setText("Grid Density = "+jig.getGridDensityString());
    ui.panjigtag.txtjigtag.setText(jig.tags);
    //refresh section
    refreshSectionAnchorButton();
    refreshSectionChorusButton();
    refreshSectionTags();
    //refresh mode button
    refreshModeButton();}
  
  private void refreshModeButton(){
    EJ_UI ui=(EJ_UI)getUI();
    if(mode==MODE_EDITGEOMETRY)
      ui.btnmode.setText("Mode=EditGeometry");
    else//mode= MODE_EDITSECTIONS
      ui.btnmode.setText("Mode=EditSections");}
  
  private void refreshSectionAnchorButton(){
    EJ_UI ui=(EJ_UI)getUI();
    if(focussection==null)
      ui.btnsectionanchor.setText("Section Anchor = ---");
    else
      ui.btnsectionanchor.setText("Section Anchor = "+focussection.getAnchorIndexString());}
  
  private void refreshSectionChorusButton(){
    EJ_UI ui=(EJ_UI)getUI();
    if(focussection==null)
      ui.btnsectionchorus.setText("Section Chorus = ---");
    else
      ui.btnsectionchorus.setText("Section Chorus = "+focussection.getChorusString());}
  
  private void refreshSectionTags(){
    EJ_UI ui=(EJ_UI)getUI();
    if(focussection==null)
      ui.pansectiontags.txttag.setText("---");
    else
      ui.pansectiontags.txttag.setText(focussection.tags);}
  
  /*
   * ################################
   * JIG EDITING OBJECTS
   * The objects that we manipulate via this editor
   *   that is, the jig editing model and associated convenient stuff
   * Then, when we hit "save", the model gets converted into a project jig
   * ################################
   */
  
  //defines our jig to be
  
//  public JigEditingModelForCreate model;
  public ProjectJig jig;
  
  //in the course of defining our geometry we have a "last vertex indicated"
  //if we click it once it is connected, twice and it is unconnected
  public KVertex connectedhead,unconnectedhead;
  //the section polygon that we are presently focused upon.
//  public JigSectionEditingModel focussection;
  public ProjectJigSection focussection;
  
  private void createEditingObjects(){
    
//    model=new JigEditingModelForCreate(GE.ge.focusmetagon);
    jig=new ProjectJig(GE.ge.focusmetagon);
    
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;}
  
  private void discardEditingObjects(){
    jig=null;
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;}
  
  public void setFocusSection(ProjectJigSection m){
    focussection=m;}
  
  public ProjectJigSection getFocusSection(){
    if(focussection==null)
      focussection=jig.sections.get(0);
    return focussection;}
  
  /*
   * ################################
   * INTERFACE TO UI
   * These are all methods that get invoked via the UI
   * ################################
   */
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRID
   * ++++++++++++++++++++++++++++++++
   */
  
  public void touchVertex(KVertex v){
    if(mode==MODE_EDITSECTIONS)return;
    EJ_UI ui=(EJ_UI)getUI();
    if(v==null){
      System.out.println("null vertex");
      jig.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch the connectedhead then convert connectedhead to unconnectedhead
    if(connectedhead!=null&&v.equals(connectedhead)){
      unconnectedhead=connectedhead;
      connectedhead=null;
      jig.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch the unconnectedhead then delete unconnectedhead
    if(unconnectedhead!=null&&v.equals(unconnectedhead)){
      jig.rawgraph.removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
      jig.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch a vertex that's already in the model
    if(jig.rawgraph.contains(v)){
      //if connectedhead is nonnull then connect connectedhead to v
      if(connectedhead!=null)
        jig.rawgraph.connect(v,connectedhead);
      //v becomes connectedhead. unconnectedhead is nulled.
      connectedhead=v;
      unconnectedhead=null;
      jig.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch a vertex that is crossed by an edge (between but not on the edge's vertices)
    GEdge edge=jig.rawgraph.getCrossingEdge(v);
    if(edge!=null){
      //add v, inserting it between the edge vertices. adjust connections appropriately
      jig.rawgraph.disconnect(edge.v0.kvertex,edge.v1.kvertex);
      jig.rawgraph.addVertex(v);
      jig.rawgraph.connect(edge.v0.kvertex,v);
      jig.rawgraph.connect(edge.v1.kvertex,v);
      //if connectedhead is nonnull then connect that too
      if(connectedhead!=null)
        jig.rawgraph.connect(connectedhead,v);
      //v is new connectedhead
      connectedhead=v;
      unconnectedhead=null;
      jig.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch an unused vertex
    //(at this point we know that we touched an unused vertex that is not crossed by an edge)
    //if connectedhead is nonnull then add vertex and connect
    jig.rawgraph.addVertex(v);
    if(connectedhead!=null)
      jig.rawgraph.connect(v,connectedhead);
    connectedhead=v;
    unconnectedhead=null;
    refreshButtons();
    jig.rawgraph.invalidateDisconnectedGraph();
    ui.pangrid.repaint();}
  
  public void touchSection(ProjectJigSection m){
    if(m==null)return;
    if(mode==MODE_EDITGEOMETRY)return;
    System.out.println("touch section");
    focussection=m;
    refreshGridGeometryAndImage();
    refreshButtons();}

  
  /*
   * ++++++++++++++++++++++++++++++++
   * BUTTONS
   * ++++++++++++++++++++++++++++++++
   */

  public void reset(){
    configureForOpen();}
  
  public void save(){
    GE.ge.focusgrammar.addMetagons(jig.localsectionmetagons);
//    ProjectJig j=new ProjectJig(model);
    GE.ge.focusmetagon.invalidateOverviewIconImage();
    GE.ge.focusmetagon.addJig(jig);
    GE.ge.focusjig=jig;
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void quit(){
    //TODO null everything
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void gridDensity_Increment(){
    System.out.println("grid density increment");
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;
    jig.incrementGridDensity();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    initGridPerspective();
    refreshUI();}
  
  public void gridDensity_Decrement(){
    System.out.println("grid density decrement");
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;
    jig.decrementGridDensity();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    initGridPerspective();
    refreshUI();}
  
  public void toggleMode(){
    System.out.println("toggle geometry lock");
    if(mode==MODE_EDITSECTIONS)
      setModeEditGeometry();
    else
      setModeEditSections();
    initGridPerspective();
    refreshUI();} 
  
  public void setJigTags(String a){
    System.out.println("set jig tags : "+a);
    jig.tags=a;}
  
  public void incrementSectionAnchor(){
    System.out.println("increment section anchor");
    focussection.incrementAnchor();
    refreshButtons();
    refreshGridImage();}
  
  public void incrementSectionChorus(){
    System.out.println("increment section chorus");
    focussection.incrementChorus();
    refreshButtons();
    refreshGridImage();}
  
  public void setSectionTags(String a){
    System.out.println("set jig section tags "+a);
    focussection.tags=a;}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  public KPolygon getScaledHostPolygon(){
    int d=1;
    if(jig!=null)d=jig.griddensity;
    return GE.ge.focusmetagon.kmetagon.getScaledPolygon(d);}
  
}

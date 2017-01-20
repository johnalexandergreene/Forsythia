package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.model.JigEditingModelForCreate;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.model.JigSectionEditingModel;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.ui.EJ_UI;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
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
    model.initSections();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangriddensity.setEnabled(false);
    ui.btnsectionanchor.setVisible(true);
    ui.btnsectionchorus.setVisible(true);
    ui.pansectiontags.setVisible(true);
    focussection=model.sections.get(0);
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
    ui.pangriddensity.lblgriddensity.setText("Grid Density = "+model.getGridDensityString());
    ui.panjigtag.txtjigtag.setText(model.getJigTags());
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
  public JigEditingModelForCreate model;
  //in the course of defining our geometry we have a "last vertex indicated"
  //if we click it once it is connected, twice and it is unconnected
  public KVertex connectedhead,unconnectedhead;
  //the section polygon that we are presently focused upon.
  public JigSectionEditingModel focussection; 
  
  private void createEditingObjects(){
    model=new JigEditingModelForCreate(GE.ge.focusmetagon);
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;}
  
  private void discardEditingObjects(){
    model=null;
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;}
  
  public void setFocusSection(JigSectionEditingModel m){
    focussection=m;}
  
  public JigSectionEditingModel getFocusSection(){
    if(focussection==null)
      focussection=model.sections.get(0);
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
      model.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch the connectedhead then convert connectedhead to unconnectedhead
    if(connectedhead!=null&&v.equals(connectedhead)){
      unconnectedhead=connectedhead;
      connectedhead=null;
      model.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch the unconnectedhead then delete unconnectedhead
    if(unconnectedhead!=null&&v.equals(unconnectedhead)){
      model.rawgraph.removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
      model.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch a vertex that's already in the model
    if(model.rawgraph.contains(v)){
      //if connectedhead is nonnull then connect connectedhead to v
      if(connectedhead!=null)
        model.rawgraph.connect(v,connectedhead);
      //v becomes connectedhead. unconnectedhead is nulled.
      connectedhead=v;
      unconnectedhead=null;
      model.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch a vertex that is crossed by an edge (between but not on the edge's vertices)
    GEdge edge=model.rawgraph.getCrossingEdge(v);
    if(edge!=null){
      //add v, inserting it between the edge vertices. adjust connections appropriately
      model.rawgraph.disconnect(edge.v0.kvertex,edge.v1.kvertex);
      model.rawgraph.addVertex(v);
      model.rawgraph.connect(edge.v0.kvertex,v);
      model.rawgraph.connect(edge.v1.kvertex,v);
      //if connectedhead is nonnull then connect that too
      if(connectedhead!=null)
        model.rawgraph.connect(connectedhead,v);
      //v is new connectedhead
      connectedhead=v;
      unconnectedhead=null;
      model.rawgraph.invalidateDisconnectedGraph();
      ui.pangrid.repaint();
      return;}
    //if we touch an unused vertex
    //(at this point we know that we touched an unused vertex that is not crossed by an edge)
    //if connectedhead is nonnull then add vertex and connect
    model.rawgraph.addVertex(v);
    if(connectedhead!=null)
      model.rawgraph.connect(v,connectedhead);
    connectedhead=v;
    unconnectedhead=null;
    refreshButtons();
    model.rawgraph.invalidateDisconnectedGraph();
    ui.pangrid.repaint();}
  
  public void touchSection(JigSectionEditingModel m){
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
    GE.ge.focusgrammar.addMetagons(model.localsectionmetagons);
    ProjectJig j=new ProjectJig(model);
    GE.ge.focusmetagon.invalidateOverviewIconImage();
    GE.ge.focusmetagon.addJig(j);
    GE.ge.focusjig=j;
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void quit(){
    //TODO null everything
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void gridDensity_Increment(){
    System.out.println("grid density increment");
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;
    model.incrementGridDensity();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    initGridPerspective();
    refreshUI();}
  
  public void gridDensity_Decrement(){
    System.out.println("grid density decrement");
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;
    model.decrementGridDensity();
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
    model.setJigTags(a);}
  
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
    if(model!=null)d=model.griddensity;
    return GE.ge.focusmetagon.kmetagon.getScaledPolygon(d);}
  
}

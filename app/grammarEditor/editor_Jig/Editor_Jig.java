package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.ui.UIEditJig;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJigSection;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.geom_Kisrhombille.graph.GEdge;

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
  
  private static final long serialVersionUID=-8986352947770948434L;

  private static final String NAME="JIG";
  
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
    modelocked=false;
    if(GE.ge.focusjig==null){
      initEditingObjectsForCreate();
      setMode_CREATE_A();
    }else{
      initEditingObjectsForRetouch();
      setMode_RETOUCH();}
    refreshUI();}
  
  public void configureForClose(){
    discardEditingObjects();}
  
  /*
   * ################################
   * MODE
   * 
   * We have 3 modes
   *   MODE_CREATE_A
   *     create geometry
   *   MODE_CREATE_B
   *     edit sections with the option to return to MODE_CREATE_A. 
   *     That is, the option to clear section data and go back to editing geometry 
   *   MODE_RETOUCH
   *     edit sections, but without the option to go to MODE_CREATE_A.
   *     
   * When we are creating a jig we start in MODE_CREATE_A
   * When we are retouching a jig we start in MODE_RETOUCH
   * ################################
   */
  
  public static final int
    MODE_CREATE_A=0,
    MODE_CREATE_B=1,
    MODE_RETOUCH=2;
  
  public int mode;
  //mode retouch is mode create b with the mode locked
  boolean modelocked=false;
  
  void setMode_CREATE_A(){
    mode=MODE_CREATE_A;
    UIEditJig ui=(UIEditJig)getUI();
    ui.btnsave.setVisible(false);
    ui.pangriddensity.setVisible(true);
    ui.btnsectionanchor.setVisible(false);
    ui.btnsectionchorus.setVisible(false);
    ui.pansectiontags.setVisible(false);
    //
    setFocusSection(null);
    //
    initGridPerspective();
    refreshUI();}
  
  
  void setMode_CREATE_B(){
    mode=MODE_CREATE_B;
    editedjig.deriveSectionsFromGraph();
    UIEditJig ui=(UIEditJig)getUI();
    ui.btnsave.setVisible(true);
    ui.pangriddensity.setVisible(false);
    ui.btnsectionanchor.setVisible(true);
    ui.btnsectionchorus.setVisible(true);
    ui.pansectiontags.setVisible(true);
    focussection=editedjig.sections.get(0);
    //
    initGridPerspective();
    refreshGridGeometryAndImage();
    refreshButtons();}
  
  void setMode_RETOUCH(){
    mode=MODE_RETOUCH;
    modelocked=true;
    UIEditJig ui=(UIEditJig)getUI();
    ui.btnsave.setVisible(true);
    ui.pangriddensity.setVisible(false);
    ui.btnsectionanchor.setVisible(true);
    ui.btnsectionchorus.setVisible(true);
    ui.pansectiontags.setVisible(true);
    focussection=editedjig.sections.get(0);
    //
    initGridPerspective();
    refreshGridGeometryAndImage();
    refreshButtons();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  protected JPanel createUI(){
    return new UIEditJig();}
  
  void initGridPerspective(){
    UIEditJig ui=(UIEditJig)getUI();
    ui.pangrid.centerAndFit();}
  
  public void refreshUI(){
    refreshGridGeometryAndImage();
    refreshButtons();}

  public void refreshGridImage(){
    UIEditJig ui=(UIEditJig)getUI();
    ui.pangrid.repaint();}
  
  public void refreshGridGeometryAndImage(){
    UIEditJig ui=(UIEditJig)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.repaint();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * REFRESH BUTTONS
   * ++++++++++++++++++++++++++++++++
   */
  
  void refreshButtons(){
    UIEditJig ui=(UIEditJig)getUI();
    ui.pangriddensity.lblgriddensity.setText("Grid Density = "+editedjig.getGridDensityString());
    ui.panjigtag.txtjigtag.setText(editedjig.tags);
    refreshSectionAnchorButton();
    refreshSectionChorusButton();
    refreshSectionTags();
    refreshModeButton();
    refreshInfo();}
  
  private void refreshModeButton(){
    UIEditJig ui=(UIEditJig)getUI();
    if(modelocked){
      ui.btnmode.setVisible(false);
    }else{
      ui.btnmode.setVisible(true);}
    //
    if(mode==MODE_CREATE_A)
      ui.btnmode.setText("Edit Sections");
    else//mode= MODE_EDITSECTIONS
      ui.btnmode.setText("Edit Geometry");}
  
  private void refreshSectionAnchorButton(){
    UIEditJig ui=(UIEditJig)getUI();
    if(focussection==null)
      ui.btnsectionanchor.setText("Section Anchor = ---");
    else
      ui.btnsectionanchor.setText("Section Anchor = "+focussection.getAnchorIndexString());}
  
  private void refreshSectionChorusButton(){
    UIEditJig ui=(UIEditJig)getUI();
    if(focussection==null)
      ui.btnsectionchorus.setText("Section Chorus = ---");
    else
      ui.btnsectionchorus.setText("Section Chorus = "+focussection.getChorusString());}
  
  private void refreshSectionTags(){
    UIEditJig ui=(UIEditJig)getUI();
    if(focussection==null)
      ui.pansectiontags.txttag.setText("---");
    else
      ui.pansectiontags.txttag.setText(focussection.tags);}
  
  private void refreshInfo(){
    UIEditJig ui=(UIEditJig)getUI();
    ui.lblinfo.setText(getInfoString());}
  
  /*
   * ################################
   * INFO STRING
   * A little analysis of the state of the edited thing, for that info bar at the bottom
   * ################################
   */
  
  private String getInfoString(){
    if(mode==MODE_CREATE_A){
      int 
        opensequencecount=editedjig.graph.getDisconnectedGraph().getOpenKVertexSequences().size(),
        undividedpolygoncount=editedjig.graph.getDisconnectedGraph().getUndividedPolygons().size();
      return "opensequences="+opensequencecount+" polygons="+undividedpolygoncount;
    }else{//mode==MODE_CREATE_B || mode==MODE_RETOUCH
      int sectioncount=editedjig.sections.size();
      return "sections="+sectioncount;}}
  
  /*
   * ################################
   * JIG EDITING OBJECTS
   * ################################
   */
  
  //the jig we're editing
  public ProjectJig editedjig;
  //the section that we are presently focused upon.
  public ProjectJigSection focussection;
  //in the course of defining our geometry we have a "last vertex indicated"
  //if we click it once it is connected, twice and it is unconnected
  public KVertex connectedhead,unconnectedhead;
  
  private void initEditingObjectsForCreate(){
    editedjig=new ProjectJig(GE.ge.focusmetagon);
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;}
  
  private void initEditingObjectsForRetouch(){
    editedjig=GE.ge.focusjig;
    focussection=null;}
  
  private void discardEditingObjects(){
    editedjig=null;
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;}
  
  public void setFocusSection(ProjectJigSection m){
    focussection=m;}
  
  public ProjectJigSection getFocusSection(){
    if(focussection==null)
      focussection=editedjig.sections.get(0);
    return focussection;}
  
  /*
   * ################################
   * COMMANDS
   * The top level jig-editing etc methods invoked in this editor via the UI or whatever
   * ################################
   */
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRID
   * ++++++++++++++++++++++++++++++++
   */
  
  public void touchVertex(KVertex v){
    //if we are in the wrong mode for vertex-touching the we're done
    //probably already handled further up the logic tree? TODO
    if(mode==MODE_CREATE_B||mode==MODE_RETOUCH)return;
    //
    //if we touched the connectedhead then convert the connectedhead to unconnectedhead
    if(connectedhead!=null&&v.equals(connectedhead)){
      unconnectedhead=connectedhead;
      connectedhead=null;  
    //if we touch the unconnectedhead then delete unconnectedhead
    }else if(unconnectedhead!=null&&v.equals(unconnectedhead)){
      editedjig.graph.removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
    //if we touch a vertex that's already in the graph
    }else if(editedjig.graph.contains(v)){
      //if connectedhead is nonnull then connect connectedhead to v
      if(connectedhead!=null)
        editedjig.graph.connect(v,connectedhead);
      //v becomes connectedhead. unconnectedhead is nulled.
      connectedhead=v;
      unconnectedhead=null;
    //at this point we know that we touched an unused vertex
    //did we touch a vertex on an existing graph edge (between but not on the edge's vertices)?
    }else{
      GEdge edge=editedjig.graph.getCrossingEdge(v);
      //## Yes, we are on an edge
      if(edge!=null){
        //add v, inserting it between the edge vertices. adjust connections appropriately
        editedjig.graph.disconnect(edge.v0.kvertex,edge.v1.kvertex);
        editedjig.graph.addVertex(v);
        editedjig.graph.connect(edge.v0.kvertex,v);
        editedjig.graph.connect(edge.v1.kvertex,v);
        //if connectedhead is nonnull then connect that too
        if(connectedhead!=null)
          editedjig.graph.connect(connectedhead,v);
        //v is new connectedhead
        connectedhead=v;
        unconnectedhead=null;
      //## No, we are not on an edge
      //add the vertex to the graph. maybe connect.
      }else{
        editedjig.graph.addVertex(v);
        if(connectedhead!=null)
          editedjig.graph.connect(v,connectedhead);
        connectedhead=v;
        unconnectedhead=null;
      }}
    //
    editedjig.graph.invalidateDisconnectedGraph();
    refreshUI();}
  
  public void touchSection(ProjectJigSection m){
    if(m==null)return;
    //we only touch sections in MODE_CREATE_A and MODE_RETOUCH
    //this is probably already handled further up the logic tree but just in case
    if(mode==MODE_CREATE_A)return;
    //
    System.out.println("touch section");
    focussection=m;
    refreshUI();}

  /*
   * ++++++++++++++++++++++++++++++++
   * BUTTONS
   * ++++++++++++++++++++++++++++++++
   */
  
  public void save(){
    //if we are in create mode then create the jig
    if(mode!=MODE_RETOUCH){
      editedjig.graph=null;//discard that. TODO what else can we discard?
      GE.ge.focusgrammar.addMetagons(editedjig.localsectionmetagons);
      GE.ge.focusmetagon.invalidateOverviewIconImage();
      GE.ge.focusmetagon.addJig(editedjig);
      GE.ge.focusjig=editedjig;}
    //
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void quit(){
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void gridDensity_Increment(){
    System.out.println("grid density increment");
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;
    editedjig.incrementGridDensity();
    UIEditJig ui=(UIEditJig)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    initGridPerspective();
    refreshUI();}
  
  public void gridDensity_Decrement(){
    System.out.println("grid density decrement");
    connectedhead=null;
    unconnectedhead=null;
    focussection=null;
    editedjig.decrementGridDensity();
    UIEditJig ui=(UIEditJig)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    initGridPerspective();
    refreshUI();}
  
  public void toggleMode(){
    System.out.println("toggle geometry lock");
    if(mode==MODE_CREATE_B)
      setMode_CREATE_A();
    else
      setMode_CREATE_B();
    initGridPerspective();
    refreshUI();} 
  
  public void setJigTags(String a){
    System.out.println("set jig tags : "+a);
    editedjig.tags=a;}
  
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
  
}

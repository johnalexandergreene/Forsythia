package org.fleen.forsythia.app.grammarEditor.editor_Metagon;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Metagon.ui.EditMetagonUI;
import org.fleen.forsythia.app.grammarEditor.project.metagon.ProjectMetagon;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.geom_Kisrhombille.graph.GEdge;

/*
 * Create a metagon
 * draw a figure on a kisrhombille grid.
 * optionally, create some tags
 * 
 * 2 modes
 * MODE_A
 *   create geometry
 *   edit tags
 * MODE_B
 *   edit tags
 * 
 */
public class Editor_Metagon extends Editor{
  
  private static final long serialVersionUID=-7639109793852882162L;

  private static final String NAME="METAGON";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_Metagon(){
    super(NAME);}
  
  /*
   * ################################
   * CONFIGURE
   * for editor open and close
   * ################################
   */

  public void configureForOpen(){
    if(GE.ge.focusmetagon==null){
      initEditingObjectsForCreate();
      setMode_Create();
    }else{
      initEditingObjectsForRetouch();
      setMode_Retouch();}
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
    MODE_CREATE=0,
    MODE_RETOUCH=1;
  
  public int mode;
  
  void setMode_Create(){
    mode=MODE_CREATE;
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.btnsave.setVisible(false);
    //
    initGridPerspective();
    refreshUI();}
  
  void setMode_Retouch(){
    mode=MODE_RETOUCH;
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.btnsave.setVisible(true);
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
    return new EditMetagonUI();}
  
  void initGridPerspective(){
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.pangrid.centerAndFit();}
  
  public void refreshUI(){
    refreshGridGeometryAndImage();
    refreshButtons();}

  public void refreshGridImage(){
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.pangrid.repaint();}
  
  public void refreshGridGeometryAndImage(){
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.repaint();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * REFRESH BUTTONS
   * ++++++++++++++++++++++++++++++++
   */
  
  void refreshButtons(){
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.panmetagontag.txtmetagontags.setText(editedmetagon.tags);
    if(graphisvalid){
      ui.btnsave.setVisible(true);
    }else{
      ui.btnsave.setVisible(false);}
    refreshInfo();}
  
  private void refreshInfo(){
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.lblinfo.setText(getInfoString());}
  
  /*
   * ################################
   * INFO STRING
   * A little analysis of the state of the edited thing, for that info bar at the bottom
   * ################################
   */

  private String getInfoString(){
    return "vertex count="+editedmetagon.getGraph().vertices.size();}
  
  /*
   * ################################
   * METAGON EDITING OBJECTS
   * ################################
   */
  
  //the jig we're editing
  public ProjectMetagon editedmetagon;
  //in the course of defining our geometry we have a "last vertex indicated"
  //if we click it once it is connected, twice and it is unconnected
  public KVertex connectedhead,unconnectedhead;
  //this is for if we are doing MODE_RETOUCH and we quit
  //it's so we can reset the tags.
  private String tagsbackup;
  //flag graph describing metagon validity
  boolean graphisvalid;
  
  private void initEditingObjectsForCreate(){
    editedmetagon=new ProjectMetagon(GE.ge.focusgrammar);
    connectedhead=null;
    unconnectedhead=null;
    graphisvalid=false;}
  
  private void initEditingObjectsForRetouch(){
    editedmetagon=GE.ge.focusmetagon;
    tagsbackup=GE.ge.focusmetagon.tags;
    graphisvalid=true;}
  
  private void discardEditingObjects(){
    editedmetagon=null;
    connectedhead=null;
    unconnectedhead=null;}
  
  private void validateGraph(){
    graphisvalid=editedmetagon.getGraphValidity();}
  
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
    if(mode==MODE_RETOUCH)return;
    //
    //if we touched the connectedhead then convert the connectedhead to unconnectedhead
    if(connectedhead!=null&&v.equals(connectedhead)){
      unconnectedhead=connectedhead;
      connectedhead=null;  
    //if we touch the unconnectedhead then delete unconnectedhead
    }else if(unconnectedhead!=null&&v.equals(unconnectedhead)){
      editedmetagon.getGraph().removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
    //if we touch a vertex that's already in the graph
    }else if(editedmetagon.getGraph().contains(v)){
      //if connectedhead is nonnull then connect connectedhead to v
      if(connectedhead!=null)
        editedmetagon.getGraph().connect(v,connectedhead);
      //v becomes connectedhead. unconnectedhead is nulled.
      connectedhead=v;
      unconnectedhead=null;
    //at this point we know that we touched an unused vertex
    //did we touch a vertex on an existing graph edge (between but not on the edge's vertices)?
    }else{
      GEdge edge=editedmetagon.getGraph().getCrossingEdge(v);
      //## Yes, we are on an edge
      if(edge!=null){
        //add v, inserting it between the edge vertices. adjust connections appropriately
        editedmetagon.getGraph().disconnect(edge.v0.kvertex,edge.v1.kvertex);
        editedmetagon.getGraph().addVertex(v);
        editedmetagon.getGraph().connect(edge.v0.kvertex,v);
        editedmetagon.getGraph().connect(edge.v1.kvertex,v);
        //if connectedhead is nonnull then connect that too
        if(connectedhead!=null)
          editedmetagon.getGraph().connect(connectedhead,v);
        //v is new connectedhead
        connectedhead=v;
        unconnectedhead=null;
      //## No, we are not on an edge
      //add the vertex to the graph. maybe connect.
      }else{
        editedmetagon.getGraph().addVertex(v);
        if(connectedhead!=null)
          editedmetagon.getGraph().connect(v,connectedhead);
        connectedhead=v;
        unconnectedhead=null;}}
    //
    editedmetagon.getGraph().invalidateDisconnectedGraph();
    validateGraph();
    refreshUI();}

  /*
   * ++++++++++++++++++++++++++++++++
   * BUTTONS
   * ++++++++++++++++++++++++++++++++
   */
  
  /*
   * SAVE THE METAGON
   * if mode == MODE_CREATE 
   *   init editedmetagon.polygon
   *   add the metagon to the focus grammar
   *   set ge.focusmetagon=editedmetagon
   * if mode == MODE_RETOUCH
   *   do nothing
   * exit to grammar editor
   */
  public void save(){
    if(mode==MODE_CREATE){
      editedmetagon.initGeometryForMetagonEditorCreate();
      GE.ge.focusgrammar.addMetagon(editedmetagon);
      GE.ge.focusmetagon=editedmetagon;
    }else{
      //
    }
    //
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  /*
   * QUIT, DISCARDING ANY CREATION OR CHANGES
   * if mode == MODE_CREATE 
   *   set editedmetagon to null
   *   set the focus metagon to the first metagon in the focus grammar's list
   * if mode == MODE_RETOUCH
   *   set editedmetagon.tags to tags backup string 
   * exit to grammar editor
   */
  public void quit(){
    if(mode==MODE_CREATE){
      editedmetagon=null;
      if(GE.ge.focusgrammar.metagons.isEmpty())
        GE.ge.focusmetagon=null;
      else
        GE.ge.focusmetagon=GE.ge.focusgrammar.metagons.get(0);
    }else{//MODE RETOUCH
      GE.ge.focusmetagon.tags=tagsbackup;}
    //
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  
  public void setMetagonTags(String a){
    System.out.println("set jig tags : "+a);
    editedmetagon.tags=a;}
  
}

package org.fleen.forsythia.app.grammarEditor.editor_Metagon;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Metagon.ui.EditMetagonUI;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJigSection;
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
    modelocked=false;
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
  //mode retouch is mode create b with the mode locked
  boolean modelocked=false;
  
  void setMode_Create(){
    mode=MODE_CREATE;
    EditMetagonUI ui=(EditMetagonUI)getUI();
    ui.btnsave.setVisible(false);
    //
    initGridPerspective();
    refreshUI();}
  
  void setMode_Retouch(){
    mode=MODE_RETOUCH;
    modelocked=true;
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
    ui.panjigtag.txtjigtag.setText(editedmetagon.tags);
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
    return "foo bar";}
  
//  private String getInfoString(){
//    if(mode==MODE_CREATE){
//      int 
//        opensequencecount=editedmetagon.graph.getDisconnectedGraph().getOpenKVertexSequences().size(),
//        undividedpolygoncount=editedmetagon.graph.getDisconnectedGraph().getUndividedPolygons().size();
//      return "opensequences="+opensequencecount+" polygons="+undividedpolygoncount;
//    }else{//mode==MODE_CREATE_B || mode==MODE_RETOUCH
//      int sectioncount=editedmetagon.sections.size();
//      return "sections="+sectioncount;}}
  
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
  
  private void initEditingObjectsForCreate(){
    editedmetagon=new ProjectMetagon(GE.ge.focusgrammar);
    connectedhead=null;
    unconnectedhead=null;}
  
  private void initEditingObjectsForRetouch(){
    editedmetagon=GE.ge.focusmetagon;}
  
  private void discardEditingObjects(){
    editedmetagon=null;
    connectedhead=null;
    unconnectedhead=null;}
  
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
      editedmetagon.graph.removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
    //if we touch a vertex that's already in the graph
    }else if(editedmetagon.graph.contains(v)){
      //if connectedhead is nonnull then connect connectedhead to v
      if(connectedhead!=null)
        editedmetagon.graph.connect(v,connectedhead);
      //v becomes connectedhead. unconnectedhead is nulled.
      connectedhead=v;
      unconnectedhead=null;
    //at this point we know that we touched an unused vertex
    //did we touch a vertex on an existing graph edge (between but not on the edge's vertices)?
    }else{
      GEdge edge=editedmetagon.graph.getCrossingEdge(v);
      //## Yes, we are on an edge
      if(edge!=null){
        //add v, inserting it between the edge vertices. adjust connections appropriately
        editedmetagon.graph.disconnect(edge.v0.kvertex,edge.v1.kvertex);
        editedmetagon.graph.addVertex(v);
        editedmetagon.graph.connect(edge.v0.kvertex,v);
        editedmetagon.graph.connect(edge.v1.kvertex,v);
        //if connectedhead is nonnull then connect that too
        if(connectedhead!=null)
          editedmetagon.graph.connect(connectedhead,v);
        //v is new connectedhead
        connectedhead=v;
        unconnectedhead=null;
      //## No, we are not on an edge
      //add the vertex to the graph. maybe connect.
      }else{
        editedmetagon.graph.addVertex(v);
        if(connectedhead!=null)
          editedmetagon.graph.connect(v,connectedhead);
        connectedhead=v;
        unconnectedhead=null;
      }}
    //
    editedmetagon.graph.invalidateDisconnectedGraph();
    refreshUI();}

  /*
   * ++++++++++++++++++++++++++++++++
   * BUTTONS
   * ++++++++++++++++++++++++++++++++
   */
  
  public void save(){
    //if we are in create mode then create the jig
    if(mode!=MODE_RETOUCH){
      editedmetagon.graph=null;//discard that. TODO what else can we discard?
      GE.ge.focusgrammar.addMetagon(editedmetagon);
      GE.ge.focusmetagon=editedmetagon;}
    //
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void quit(){
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  
  public void setJigTags(String a){
    System.out.println("set jig tags : "+a);
    editedmetagon.tags=a;}
  
}

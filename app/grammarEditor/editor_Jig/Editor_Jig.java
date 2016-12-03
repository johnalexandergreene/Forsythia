package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
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
  
  private static final String NAME="Edit Jig";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_Jig(){
    super(NAME);}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  protected JPanel createUI(){
    return new EJ_UI();}
  
  public void refreshUI(){
    refreshGrid();
    refreshControls();}

  public void refreshGrid(){
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.repaint();}

  void refreshJigDetailsUI(){
    
  }
  
  void refreshSectionDetailsUI(){
    
  }
  
  void refreshGeneralDetailsUI(){
    
  }
  

  

  
  void refreshControls(){
    EJ_UI ui=(EJ_UI)getUI();
    ui.lblgriddensity.setText(model.getGridDensityString());
    refreshFocusSectionInfo();}
  
  void refreshFocusSectionInfo(){
    EJ_UI ui=(EJ_UI)getUI();
//    ui.lblinfo.setText(getFocusElementInfo());
    
  }

  /*
   * ################################
   * CONFIGURE
   * ################################
   */

  public void configureForOpen(){
    initJigEditingObjects();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.centerAndFit();
    refreshUI();}
  
  public void configureForClose(){
    discardEditingObjects();}
  
  /*
   * ################################
   * JIG EDITING OBJECTS
   * ################################
   */
  
  //defines our jig to be
  JigEditingModel model;
  //in the course of defining our geometry we have a "last vertex indicated"
  //if we click it once it is connected, twice and it is unconnected
  KVertex connectedhead,unconnectedhead;
  //the section polygon that we are presently focused upon.
  //highlighted in the grid, its details displayed up top in that second bar
  KPolygon focussectionpolygon;
  
  private void initJigEditingObjects(){
    model=new JigEditingModel();
    connectedhead=null;
    unconnectedhead=null;
    focussectionpolygon=null;}
  
  private void discardEditingObjects(){
    model=null;
    connectedhead=null;
    unconnectedhead=null;
    focussectionpolygon=null;}
  
  /*
   * ################################
   * MANIPULATE GRID
   * ################################
   */
  
  KPolygon getHostPolygon(){
    return GE.focusmetagon.kmetagon.getScaledPolygon(model.griddensity);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * TOUCH VERTEX
   * ++++++++++++++++++++++++++++++++
   */
  
  void touchVertex(final KVertex v){
//    focuselement=null;
    if(v==null){
      System.out.println("null vertex");
      model.rawgraph.invalidateDisconnectedGraph();
      ((EJ_UI)getUI()).pangrid.repaint();
      return;}
    //if we touch the connectedhead then convert connectedhead to unconnectedhead
    if(connectedhead!=null&&v.equals(connectedhead)){
      unconnectedhead=connectedhead;
      connectedhead=null;
      model.rawgraph.invalidateDisconnectedGraph();
      ((EJ_UI)getUI()).pangrid.repaint();
      return;}
    //if we touch the unconnectedhead then delete unconnectedhead
    if(unconnectedhead!=null&&v.equals(unconnectedhead)){
      model.rawgraph.removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
      model.rawgraph.invalidateDisconnectedGraph();
      ((EJ_UI)getUI()).pangrid.repaint();
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
      ((EJ_UI)getUI()).pangrid.repaint();
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
      ((EJ_UI)getUI()).pangrid.repaint();
      return;}
    //if we touch an unused vertex
    //(at this point we know that we touched an unused vertex that is not crossed by an edge)
    //if connectedhead is nonnull then add vertex and connect
    model.rawgraph.addVertex(v);
    if(connectedhead!=null)
      model.rawgraph.connect(v,connectedhead);
    connectedhead=v;
    unconnectedhead=null;
    refreshControls();
    model.rawgraph.invalidateDisconnectedGraph();
    ((EJ_UI)getUI()).pangrid.repaint();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * TOUCH SECTION
   * or anything other than a vertex. unspecified territory I guess
   * ++++++++++++++++++++++++++++++++
   */
  
  void touchSection(final double[] p){
    
    //TODO
    
    refreshFocusSectionInfo();
    ((EJ_UI)getUI()).pangrid.repaint();
  }

  
  /*
   * ################################
   * BUTTONS
   * ################################
   */
  
  public void incrementGridDensity(){
    connectedhead=null;
    unconnectedhead=null;
//    focuselement=null;
    System.out.println("increment");
    model.incrementGridDensity();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.centerAndFit();
    ui.pangrid.repaint();
    refreshUI();}
  
  public void decrementGridDensity(){
    connectedhead=null;
    unconnectedhead=null;
//    focuselement=null;
    System.out.println("decrement");
    model.decrementGridDensity();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.centerAndFit();
    ui.pangrid.repaint();
    refreshUI();}
  
  public void saveJig(){
    ProjectJig j=model.getProjectJig();
    GE.focusmetagon.invalidateOverviewIconImage();
    GE.focusmetagon.addJig(j);
    GE.focusjig=j;
    GE.setEditor(GE.editor_grammar);}
  
  public void discardJig(){
    //TODO null everything
    GE.setEditor(GE.editor_grammar);}
  
}

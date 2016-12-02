package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * A jig is defined by it's grid density, graph, jig tags, jigsections and jigsection tags
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
  
  void refreshControls(){
    EJ_UI ui=(EJ_UI)getUI();
    ui.lblgriddensity.setText(model.getGridDensityString());
    refreshFocusSectionInfo();}
  
  void refreshFocusSectionInfo(){
    EJ_UI ui=(EJ_UI)getUI();
    ui.lblinfo.setText(getFocusElementInfo());}

  /*
   * ################################
   * CONFIGURE
   * ################################
   */

  public void configureForOpen(){
    initModel();
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.centerAndFit();
    refreshUI();}
  
  public void configureForClose(){
    discardEditingObjects();}
  
  /*
   * ################################
   * JIG EDITING MODEL
   * ################################
   */
  
  //
  KVertex connectedhead,unconnectedhead;
  //tags for this protojig
  public String protojigtags;
  //tags for each section
  public Map<KPolygon,String> sectiontagsbypolygon;
  //either a section polygon or null.
  //section polygon means focus in on a particular section
  //null means focus is on the protojig as a whole
  KPolygon focuselement=null;
  //fast and convenient
  ViewGeometryCache viewgeometrycache;
  
  JigEditingModel model;
  
  private void initModel(){
    
    model=new JigEditingModel();
    
//    rawgraph=new RawGraph(GE.focusmetagon.kpolygon);
    protojigtags="";
    sectiontagsbypolygon=new Hashtable<KPolygon,String>();
    connectedhead=null;
    unconnectedhead=null;
    focuselement=null;
    viewgeometrycache=new ViewGeometryCache();}
  
  KPolygon getHostPolygon(){
    return GE.focusmetagon.kmetagon.getScaledPolygon(model.griddensity);}
  
  private void discardEditingObjects(){
    model=null;
    protojigtags=null;
    sectiontagsbypolygon=null;
    connectedhead=null;
    unconnectedhead=null;
    focuselement=null;
    viewgeometrycache=null;}
  
  /*
   * touch a vertex on the kgrid
   * if we touch the connectedhead then the connectedhead is turned into the unconnectedhead
   */
  private void touchVertex(KVertex v){
    focuselement=null;
    if(v==null){
      System.out.println("null vertex");
      return;}
    //if we touch the connectedhead then convert connectedhead to unconnectedhead
    if(connectedhead!=null&&v.equals(connectedhead)){
      unconnectedhead=connectedhead;
      connectedhead=null;
      return;}
    //if we touch the unconnectedhead then delete unconnectedhead
    if(unconnectedhead!=null&&v.equals(unconnectedhead)){
      model.rawgraph.removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
      return;}
    //if we touch a used vertex
    if(model.rawgraph.contains(v)){
      //if connectedhead is nonnull then connect connectedhead to v
      if(connectedhead!=null)
        model.rawgraph.connect(v,connectedhead);
      //v becomes connectedhead. unconnectedhead is nulled.
      connectedhead=v;
      unconnectedhead=null;
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
      return;}
    //if we touch an unused vertex
    //(at this point we know that we touched an unused vertex that is not crossed by an edge)
    //if connectedhead is nonnull then add vertex and connect
    model.rawgraph.addVertex(v);
    if(connectedhead!=null)
      model.rawgraph.connect(v,connectedhead);
    connectedhead=v;
    unconnectedhead=null;
    refreshControls();}
  
  private String getFocusElementInfo(){
    if(focuselement==null)
      return model.rawgraph.getInfo();
    else
      return focuselement.toString();}
  
  private void touchSection(KVertex v){
    
  }
  
  /*
   * ################################
   * MOUSE MODE 
   * We monitor the distance of the mouse from any vertices
   * when we're close we're in touch vertices mode
   * when we're far we're in touch sections mode
   * ################################
   */
  
  private static final int 
    MOUSEMODE_TOUCHVERTEX=0,
    MOUSEMODE_TOUCHSECTION=1;
  
  int mousemode;
  
  public void initMouseMode_TouchVertex(){
    if(mousemode==MOUSEMODE_TOUCHVERTEX)return;
    mousemode=MOUSEMODE_TOUCHVERTEX;
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.setCursorCircle();}
  
  public void initMouseMode_TouchSection(){
    if(mousemode==MOUSEMODE_TOUCHSECTION)return;
    mousemode=MOUSEMODE_TOUCHSECTION;
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.setCursorSquare();}

  /*
   * ################################
   * MOUSE
   * ################################
   */
  
  /*
   * if the mode is draw vertex then we convert the point to a vertex and so on
   * if the mode is select section then we do that 
   */
  public void touchGrid(final double[] p,final KVertex v){
    if(mousemode==MOUSEMODE_TOUCHVERTEX){
      touchVertex(v);
      model.rawgraph.invalidateDisconnectedGraph();
    }else{
      touchSection(v);
      refreshFocusSectionInfo();}
    ((EJ_UI)getUI()).pangrid.repaint();}
  
  /*
   * ################################
   * BUTTONS
   * ################################
   */
  
  public void incrementGridDensity(){
    connectedhead=null;
    unconnectedhead=null;
    focuselement=null;
    System.out.println("increment");
    model.incrementGridDensity();
    model.rawgraph=new RawGraph(getHostPolygon());
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.centerAndFit();
    ui.pangrid.repaint();
    refreshUI();}
  
  public void decrementGridDensity(){
    connectedhead=null;
    unconnectedhead=null;
    focuselement=null;
    System.out.println("decrement");
    model.decrementGridDensity();
    model.rawgraph=new RawGraph(getHostPolygon());
    EJ_UI ui=(EJ_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.centerAndFit();
    ui.pangrid.repaint();
    refreshUI();}
  
  public void saveJig(){
    //get the polygons and yards for the jig
    //from the polygons list remove any outer polygons of yards
    //TODO I dunno, maybe find a more elegant way to do this
    //maybe do it elsewhere
    List<KPolygon> sectionpolygons=model.rawgraph.getDisconnectedGraph().getUndividedPolygons();
    //create the new jig
    ProjectJig j=new ProjectJig(
      GE.focusmetagon,
      model.griddensity,
      sectionpolygons);
    //clean up
    GE.focusmetagon.invalidateOverviewIconImage();
    GE.focusmetagon.addJig(j);
    GE.focusjig=j;
    //got to jig details editor
    GE.setEditor(GE.editor_editjigdetails);}
  
  public void discardJig(){
    //TODO null everything
    GE.setEditor(GE.editor_grammar);}
  
}

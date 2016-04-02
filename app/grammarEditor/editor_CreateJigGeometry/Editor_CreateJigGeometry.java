package org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry.graph.RawGraph;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.geom_Kisrhombille.KYard;

/*
 * A protojig is defined by it's grid density, graph, protojig tags and protojigsection tags
 * 
 * For every created section we compare the resulting metagon with our existing set of metagons
 * If a match is found then we use that metagon to define a section.
 * If no match is found then we create a new metagon.
 * 
 */
public class Editor_CreateJigGeometry extends Editor{
  
  private static final String NAME="Create Jig Geometry";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_CreateJigGeometry(){
    super(NAME);}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  protected JPanel createUI(){
    return new CJG_UI();}

  /*
   * ################################
   * CONFIGURE
   * ################################
   */

  public void configureForOpen(){
    initEditingObjects();
    CJG_UI ui=(CJG_UI)getUI();
    ui.pangrid.centerAndFit();
    refreshAll();}
  
  public void configureForClose(){
    discardEditingObjects();}
  
  /*
   * ################################
   * JIG MODEL
   * These are the things getting edited. 
   * When we're done we either convert them to a protojig (and whatever) or we discard them
   * ################################
   */
  
  private static final int INITGRIDDENSITY=1;
  public int griddensity;
  public RawGraph rawgraph;
  public int jigtype=1;//TODO
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
  
  private void initEditingObjects(){
    griddensity=INITGRIDDENSITY;
    rawgraph=new RawGraph(GE.focusmetagon.kpolygon);
    protojigtags="";
    sectiontagsbypolygon=new Hashtable<KPolygon,String>();
    connectedhead=null;
    unconnectedhead=null;
    focuselement=null;
    viewgeometrycache=new ViewGeometryCache();}
  
  KPolygon getHostPolygon(){
    return GE.focusmetagon.kmetagon.getScaledPolygon(griddensity);}
  
  private void discardEditingObjects(){
    rawgraph=null;
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
      rawgraph.removeVertex(v);
      connectedhead=null;
      unconnectedhead=null;
      return;}
    //if we touch a used vertex
    if(rawgraph.contains(v)){
      //if connectedhead is nonnull then connect connectedhead to v
      if(connectedhead!=null)
        rawgraph.connect(v,connectedhead);
      //v becomes connectedhead. unconnectedhead is nulled.
      connectedhead=v;
      unconnectedhead=null;
      return;}
    //if we touch a vertex that is crossed by an edge (between but not on the edge's vertices)
    GEdge edge=rawgraph.getCrossingEdge(v);
    if(edge!=null){
      //add v, inserting it between the edge vertices. adjust connections appropriately
      rawgraph.disconnect(edge.v0.kvertex,edge.v1.kvertex);
      rawgraph.addVertex(v);
      rawgraph.connect(edge.v0.kvertex,v);
      rawgraph.connect(edge.v1.kvertex,v);
      //if connectedhead is nonnull then connect that too
      if(connectedhead!=null)
        rawgraph.connect(connectedhead,v);
      //v is new connectedhead
      connectedhead=v;
      unconnectedhead=null;
      return;}
    //if we touch an unused vertex
    //(at this point we know that we touched an unused vertex that is not crossed by an edge)
    //if connectedhead is nonnull then add vertex and connect
    rawgraph.addVertex(v);
    if(connectedhead!=null)
      rawgraph.connect(v,connectedhead);
    connectedhead=v;
    unconnectedhead=null;
    refreshControls();}
  
  private String getFocusElementInfo(){
    if(focuselement==null)
      return rawgraph.getInfo();
    else
      return focuselement.toString();}
  
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
    MOUSEMODE_TOUCHNOTHING=1;
  
  int mousemode;
  
  public void initMouseMode_VERTEXNEAR(){
    if(mousemode==MOUSEMODE_TOUCHVERTEX)return;
    mousemode=MOUSEMODE_TOUCHVERTEX;
    CJG_UI ui=(CJG_UI)getUI();
    ui.pangrid.setCursorCircle();}
  
  public void initMouseMode_VERTEXFAR(){
    if(mousemode==MOUSEMODE_TOUCHNOTHING)return;
    mousemode=MOUSEMODE_TOUCHNOTHING;
    CJG_UI ui=(CJG_UI)getUI();
    ui.pangrid.setCursorSquare();}

  /*
   * ################################
   * REFRESH
   * ################################
   */
  
  public void refreshAll(){
    refreshGrid();
    refreshControls();}
  
  public void refreshGrid(){
    CJG_UI ui=(CJG_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.repaint();}
  
  void refreshControls(){
    CJG_UI ui=(CJG_UI)getUI();
    //grid density
    ui.lblgriddensity.setText(String.format("%03d",griddensity));
    //
    refreshFocusElementInfo();}
  
  void refreshFocusElementInfo(){
    CJG_UI ui=(CJG_UI)getUI();
    ui.lblinfo.setText(getFocusElementInfo());}
  
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
    if(mousemode==MOUSEMODE_TOUCHVERTEX)touchVertex(v);
    refreshFocusElementInfo();
    ((CJG_UI)getUI()).pangrid.repaint();
    
    //TODO debug
    
    System.out.println("---------------------------");
    System.out.println(rawgraph.getDisconnectedGraph());
    System.out.println("---------------------------");
    rawgraph.invalidateDisconnectedGraph();
    
  }
  
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
    griddensity++;
    rawgraph=new RawGraph(getHostPolygon());
    CJG_UI ui=(CJG_UI)getUI();
    ui.pangrid.gridrenderer.invalidateTileImage();
    ui.pangrid.centerAndFit();
    ui.pangrid.repaint();
    refreshAll();}
  
  public void decrementGridDensity(){
    connectedhead=null;
    unconnectedhead=null;
    focuselement=null;
    if(griddensity>1){
      System.out.println("decrement");
      griddensity--;
      rawgraph=new RawGraph(getHostPolygon());
      CJG_UI ui=(CJG_UI)getUI();
      ui.pangrid.gridrenderer.invalidateTileImage();
      ui.pangrid.centerAndFit();
      ui.pangrid.repaint();
      refreshAll();}}
  
  public void saveJig(){
    //get the polygons and yards for the jig
    //from the polygons list remove any outer polygons of yards
    //TODO I dunno, maybe find a more elegant way to do this
    //maybe do it elsewhere
    List<KPolygon> sectionpolygons=rawgraph.getDisconnectedGraph().getUndividedPolygons();
    List<KYard> sectionyards=rawgraph.getDisconnectedGraph().getYards();
    for(KYard y:sectionyards)
      sectionpolygons.remove(y.getOuterEdge());
    //create the new jig
    ProjectJig j=new ProjectJig(
      GE.focusmetagon,
      griddensity,
      sectionpolygons,
      sectionyards);
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

package org.fleen.forsythia.app.grammarEditor.editor_CreateMetagon;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * Create a metagon
 * 
 * Open with a blank grid-canvas
 * Draw metagon polygon
 * Specify tags (space-delimited strings)
 */
public class Editor_CreateMetagon extends Editor{
  
  public static final String NAME="Create Metagon";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_CreateMetagon(){
    super(NAME);}

  /*
   * ################################
   * UI
   * ################################
   */

  protected JPanel createUI(){
    return new ECMUI();}
  
  /*
   * ################################
   * WORKING OBJECTS
   * ################################
   */
  
  WorkingPolygon workingpolygon=null;
  
  String getTags(){
    ECMUI ui=(ECMUI)getUI();
    return ui.txttags.getText();}
  
  /*
   * ################################
   * CONFIGURE
   * ################################
   */
  
  protected void configureForOpen(){
    ECMUI ui=(ECMUI)getUI();
    workingpolygon=new WorkingPolygon();
    ui.txttags.setText("");
    ui.grid.setTransformDefaults();
    ui.grid.setCursorCircle();}
  
  protected void configureForClose(){
    workingpolygon=null;}
  
  /*
   * ################################
   * REFRESH UI
   * ################################
   */
  
  public void refreshAll(){
    refreshGrid();
    refreshButtons();
    getUI().repaint();}
  
  public void refreshGrid(){
    ECMUI ui=(ECMUI)getUI();
    ui.grid.gridrenderer.invalidateTileImage();
    ui.grid.repaint();}
  
  private void refreshButtons(){
    ECMUI ui=(ECMUI)getUI();
    if(workingpolygon.finished){
      ui.btnsavemetagon.setVisible(true);
      ui.btndiscardmetagon.setVisible(true);
    }else{
      ui.btnsavemetagon.setVisible(false);
      ui.btndiscardmetagon.setVisible(true);}
    ui.revalidate();}
  
  /*
   * ################################
   * CONTROLS
   * ################################
   */
  
  public void touchVertex(final KVertex v){
    workingpolygon.touchVertex(v);
    refreshButtons();
    ((ECMUI)getUI()).grid.repaint();}
  
  public void saveMetagonAndReturnToGrammarEditor(){
    //invalidate cached forsythiagrammar in projectgrammar because we changed the grammar
    //update focus metagon
    workingpolygon.clean();
    ProjectMetagon pm=new ProjectMetagon(
      GE.focusgrammar,new KPolygon(workingpolygon),getTags());
    GE.focusgrammar.addMetagon(pm);
    GE.focusmetagon=pm;
    GE.setEditor(GE.editor_grammar);}
  
  public void discardMetagonAndReturnToGrammarEditor(){
    GE.setEditor(GE.editor_grammar);}
  
}

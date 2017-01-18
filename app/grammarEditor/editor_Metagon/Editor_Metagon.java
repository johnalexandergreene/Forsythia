package org.fleen.forsythia.app.grammarEditor.editor_Metagon;

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
public class Editor_Metagon extends Editor{
  
  public static final String NAME="Create Metagon";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_Metagon(){
    super(NAME);}

  /*
   * ################################
   * UI
   * ################################
   */

  protected JPanel createUI(){
    return new EMUI();}
  
  /*
   * ################################
   * WORKING OBJECTS
   * ################################
   */
  
  WorkingPolygon workingpolygon=null;
  
  String getTags(){
    EMUI ui=(EMUI)getUI();
    return ui.txttags.getText();}
  
  /*
   * ################################
   * CONFIGURE
   * ################################
   */
  
  protected void configureForOpen(){
    EMUI ui=(EMUI)getUI();
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
  
  public void refreshUI(){
    refreshGrid();
    refreshButtons();
    getUI().repaint();}
  
  public void refreshGrid(){
    EMUI ui=(EMUI)getUI();
    ui.grid.gridrenderer.invalidateTileImage();
    ui.grid.repaint();}
  
  private void refreshButtons(){
    EMUI ui=(EMUI)getUI();
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
    ((EMUI)getUI()).grid.repaint();}
  
  public void saveMetagonAndReturnToGrammarEditor(){
    //invalidate cached forsythiagrammar in projectgrammar because we changed the grammar
    //update focus metagon
    workingpolygon.clean();
    ProjectMetagon pm=new ProjectMetagon(
      GE.ge.focusgrammar,new KPolygon(workingpolygon),getTags());
    GE.ge.focusgrammar.addMetagon(pm);
    GE.ge.focusmetagon=pm;
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
  public void discardMetagonAndReturnToGrammarEditor(){
    GE.ge.setEditor(GE.ge.editor_grammar);}
  
}

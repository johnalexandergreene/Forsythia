package org.fleen.forsythia.app.grammarEditor.editor_ViewMetagonEditTags;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.Editor;

/*
 * View metagon and edit tags
 * 
 * Specify tags (space-delimited strings)
 */
public class Editor_EditMetagonDetails extends Editor{
  
  public static final String NAME="View Metagon & Edit Tags";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_EditMetagonDetails(){
    super(NAME);}

  /*
   * ################################
   * UI
   * ################################
   */

  protected JPanel createUI(){
    return new EVMETUI();}
  
  /*
   * ################################
   * CONFIGURE
   * ################################
   */
  
  protected void configureForOpen(){
    EVMETUI ui=(EVMETUI)getUI();
    ui.txttags.setText(GE.focusmetagon.tags);
    ui.grid.centerAndFit();
    ui.grid.setCursorCircle();}
  
  protected void configureForClose(){}
  
  /*
   * ################################
   * REFRESH UI
   * ################################
   */
  
  public void refreshUI(){
    refreshGrid();
    getUI().repaint();}
  
  public void refreshGrid(){
    EVMETUI ui=(EVMETUI)getUI();
    ui.grid.gridrenderer.invalidateTileImage();
    ui.grid.repaint();}
  
  /*
   * ################################
   * CONTROLS
   * ################################
   */
  
  public void saveModificationsAndReturnToGrammarEditor(){
    //update focus metagon
    EVMETUI ui=(EVMETUI)getUI();
    GE.focusmetagon.tags=ui.txttags.getText();
    GE.setEditor(GE.editor_grammar);}
  
  public void discardModificationsAndReturnToGrammarEditor(){
    GE.setEditor(GE.editor_grammar);}
  
}

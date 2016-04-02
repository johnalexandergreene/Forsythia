package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.forsythia.app.grammarEditor.util.grid.Grid;
import org.fleen.geom_Kisrhombille.KVertex;

public class Editor_EditJigDetails extends Editor{

  private static final String NAME="Edit Jig Details";

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_EditJigDetails(){
    super(NAME);}
  
  /*
   * ################################
   * UI
   * ################################
   */ 
  
  protected JPanel createUI(){
    EJD_UI ui=new EJD_UI();
    ui.pangrid.setCursorSquare();
    return ui;}
  
  public Grid getGrid(){
    EJD_UI ui=(EJD_UI)getUI();
    return ui.pangrid;}
  
  /*
   * ################################
   * CONFIGURE
   * ################################
   */

  public void configureForOpen(){
    model=new JigDetailsModel(GE.focusjig);
    EJD_UI ui=(EJD_UI)getUI();
    ui.pangrid.centerAndFit();
    refreshAll();}
  
  public void configureForClose(){
    model=null;
    getGrid().clearGeometryCache();}
  
  /*
   * ################################
   * REFRESH
   * ################################
   */
  
  public void refreshAll(){
    refreshText();
    refreshGrid();} 
  
  void refreshText(){
    EJD_UI ui=(EJD_UI)getUI();
    ui.lblfocuselementid.setText(model.getFocusElement().getElementIDString());
    ui.txttags.setText(model.getFocusElement().getTags());
    ui.lblinfo.setText(model.getFocusElement().toString());}
  
  void refreshGrid(){
    Grid g=getGrid();
    g.gridrenderer.invalidateTileImage();
    g.repaint();}
  
  void refreshGridOverlay(){
    getGrid().clearGeometryCache();
    getGrid().repaint();}

  /*
   * ################################
   * MODEL
   * ################################
   */
  
  JigDetailsModel model;
  
  /*
   * ################################
   * MOUSE CLICKED ON GRID
   * ################################
   */
  
  /*
   * get touched element from model
   * if old touched element and new touched element are the same section then test for touch element action area
   * if we touched a control area then execute action
   */
  public void touchGrid(double[] p){
    FocusableModelElement 
      focus=model.updateFocusElement(p),
      priorfocus=model.getPriorFocusElement();
    //if a new element is indicated then set that element to focus and refresh
    if(focus!=priorfocus){
      priorfocus.setTags(((EJD_UI)getUI()).txttags.getText());//copy tags text box contents to element tags
      refreshText();
      refreshGridOverlay();
    //if the new elelemnt is the same as the old element then test icon circles for touch
    }else{
      if(focus instanceof JigSectionDetailsModel)
        retouchSection((JigSectionDetailsModel)focus,p);}
    
    }
  
  private void retouchSection(JigSectionDetailsModel m,double[] p){
    if(m.touchedTIconCircle(p)){
      System.out.println("touched t icon");
      boolean a=m.incrementProductType();
      if(a){
        refreshText();
        refreshGridOverlay();}
    }else if(m.touchedCIconCircle(p)){
      System.out.println("touched c icon");
      m.incrementProductChorusIndex();
      refreshText();
      refreshGridOverlay();
    }else{
      System.out.println("touched vertex icon");
      KVertex v=m.getTouchedVIconCircleVertex(p);
      if(v==null)return;
      if(v.equals(m.productanchoroptions.get(m.productanchorindex).v0))
        m.flipAnchorTwist();
      else
        m.setAnchorV0(v);
      refreshText();
      refreshGridOverlay();}}
  
  /*
   * ################################
   * BUTTONS
   * ################################
   */
  
  public void save(){
    model.getFocusElement().setTags(((EJD_UI)getUI()).txttags.getText());
    model.export(GE.focusjig);
    GE.setEditor(GE.editor_grammar);}
  
  public void discard(){
    GE.setEditor(GE.editor_grammar);}

}

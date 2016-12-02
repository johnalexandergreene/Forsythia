package org.fleen.forsythia.app.grammarEditor.editor_EditGrammar;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.GrammarImportExport;
import org.fleen.forsythia.app.grammarEditor.project.ProjectGrammar;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.forsythia.app.grammarEditor.util.Editor;

public class Editor_Grammar extends Editor{

  public static final String NAME="Grammar";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_Grammar(){
    super(NAME);}

  /*
   * ################################
   * UI
   * ################################
   */

  protected JPanel createUI(){
    return new G_UI();}
  
  /*
   * ################################
   * TASK INTERFACE
   * ################################
   */

  public void configureForOpen(){
    //if focus metagon is null then try to make it nonnull
    if(GE.focusmetagon==null)
      if(GE.focusgrammar.hasMetagons())
        GE.focusmetagon=GE.focusgrammar.getMetagon(0);
    //if focus metagon is nonnull and jig is null 
    //then try to make focus jig nonnull
    if(GE.focusmetagon!=null&&GE.focusjig==null)
      if(GE.focusmetagon.hasProtoJigs())
        GE.focusjig=GE.focusmetagon.getJig(0);
    //
    refreshUI();}
  
  public void createMetagon(){
    ((G_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    GE.setEditor(GE.editor_createmetagon);}
  
  public void viewMetagonAndEditTags(){
    GE.setEditor(GE.editor_editmetagondetails);}
  
  public void discardMetagon(){
    ((G_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    ((G_UI)GE.editor_grammar.getUI()).panjigmenu.invalidateIconArrayMetrics();
    int a=GE.focusgrammar.getIndex(GE.focusmetagon)-1;
    GE.focusgrammar.discardMetagon(GE.focusmetagon);
    if(a<0)a=0;
    GE.focusmetagon=GE.focusgrammar.getMetagon(a);
    refreshInfo();
    getUI().repaint();}
  
  public void createProtoJig(){
    ((G_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    GE.setEditor(GE.editor_jig);}
  
  public void viewProtoJigEditTags(){
    ((G_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    GE.setEditor(GE.editor_editjigdetails);}
  
  public void discardProtoJig(){
    ((G_UI)GE.editor_grammar.getUI()).panjigmenu.invalidateIconArrayMetrics();
    int a=GE.focusmetagon.getProtoJigIndex(GE.focusjig)-1;
    GE.focusmetagon.discardProtoJig(GE.focusjig);
    if(a<0)a=0;
    GE.focusjig=GE.focusmetagon.getJig(a);
    refreshInfo();
    getUI().repaint();}

  public void refreshUI(){
    G_UI ui=(G_UI)getUI();
    ui.panmetagonmenu.invalidateIconArrayMetrics();
    ui.panjigmenu.invalidateIconArrayMetrics();
    ui.repaint();
    refreshInfo();}
  
  public void setFocusMetagon(final ProjectMetagon m){
    GE.focusmetagon=m;
    G_UI ui=(G_UI)getUI();
    ui.panjigmenu.invalidateIconArrayMetrics();
    refreshInfo();
    ui.panmetagonmenu.repaint();
    ui.panjigmenu.repaint();}
  
  public void setFocusJig(final ProjectJig m){
    GE.focusjig=m;
    G_UI ui=(G_UI)getUI();
    refreshInfo();
    ui.panjigmenu.repaint();}
  
  public void exportGrammar(){
    GrammarImportExport.exportGrammar();}
  
  public void importGrammar(){
    GrammarImportExport.importGrammar();
    refreshUI();}
  
  //TODO
  private void refreshInfo(){
    G_UI ui=(G_UI)getUI();
//    ui.lblinfo_grammarpath.setText(
//      FQ.focusgrammar.grammarname+" "+
//      focusmetagonname+" "+
//      focusjigname);
    ui.lblmetagoninfo.setText(GE.focusgrammar.getMetagonInfo());
//    ui.lbljiginfo.setText(FQ.focusgrammar.getJigInfo());
    }

  public void configureForClose(){}
  
  public void createNewGrammar(){
    GE.focusgrammar=new ProjectGrammar();
    GE.focusmetagon=null;
    GE.focusjig=null;
    refreshUI();}

}

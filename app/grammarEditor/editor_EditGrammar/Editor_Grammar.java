package org.fleen.forsythia.app.grammarEditor.editor_EditGrammar;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_EditGrammar.ui.EG_UI;
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
    return new EG_UI();}
  
  public void refreshUI(){
    EG_UI ui=(EG_UI)getUI();
    ui.panmetagonmenu.invalidateIconArrayMetrics();
    ui.panjigmenu.invalidateIconArrayMetrics();
    ui.repaint();
    refreshButtons();}
  
  private void refreshButtons(){
    EG_UI ui=(EG_UI)getUI();
    ui.lblgrammarname.setText("Grammar = "+GE.ge.focusgrammar.getName());
    ui.lblmetagonscount.setText("Count="+GE.ge.focusgrammar.getMetagonCount());
    ui.lblmetagonjiglesscount.setText("Jigless="+GE.ge.focusgrammar.getJiglessMetagonsCount());
    ui.lblmetagonsisolatedcount.setText("Isolated="+GE.ge.focusgrammar.getIsolatedMetagonsCount());
    ui.lbljigscount.setText("Count="+getJigCount());}
  
  private int getJigCount(){
    if(GE.ge.focusmetagon==null)
      return 0;
    else
      return GE.ge.focusmetagon.getJigCount();}
  
  /*
   * ################################
   * CONFIGURE
   * ################################
   */

  public void configureForOpen(){
    //if focus metagon is null then try to make it nonnull
    if(GE.ge.focusmetagon==null)
      if(GE.ge.focusgrammar.hasMetagons())
        GE.ge.focusmetagon=GE.ge.focusgrammar.getMetagon(0);
    //if focus metagon is nonnull and jig is null 
    //then try to make focus jig nonnull
    if(GE.ge.focusmetagon!=null&&GE.ge.focusjig==null)
      if(GE.ge.focusmetagon.hasJigs())
        GE.ge.focusjig=GE.ge.focusmetagon.getJig(0);
    //
    refreshUI();}

  public void configureForClose(){}
  
  /*
   * ################################
   * COMMAND
   * ################################
   */
  
  /*
   * ++++++++++++++++++++++++++++++++
   * METAGON
   * ++++++++++++++++++++++++++++++++
   */
  
  public void createMetagon(){
    EG_UI ui=(EG_UI)getUI();
    ui.panmetagonmenu.invalidateIconArrayMetrics();
    GE.ge.setEditor(GE.ge.editor_metagon);
    refreshUI();}
  
  public void editMetagon(){
    GE.ge.setEditor(GE.ge.editor_metagon);
    refreshUI();}
  
  public void discardMetagon(){
    EG_UI ui=(EG_UI)getUI();
    ui.panmetagonmenu.invalidateIconArrayMetrics();
    ui.panjigmenu.invalidateIconArrayMetrics();
    int a=GE.ge.focusgrammar.getIndex(GE.ge.focusmetagon)-1;
    GE.ge.focusgrammar.discardMetagon(GE.ge.focusmetagon);
    if(a<0)a=0;
    GE.ge.focusmetagon=GE.ge.focusgrammar.getMetagon(a);
    refreshUI();}
  
  public void setFocusMetagon(final ProjectMetagon m){
    GE.ge.focusmetagon=m;
    EG_UI ui=(EG_UI)getUI();
    ui.panjigmenu.invalidateIconArrayMetrics();
    refreshUI();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * JIG
   * we use the same editor for creating jigs and editing existent jigs
   * if focusjig is null then we are creating a new jig, if it isn't null then we are editing and existent jig 
   * ++++++++++++++++++++++++++++++++
   */
  
  public void createJig(){
    EG_UI ui=(EG_UI)getUI();
    ui.panmetagonmenu.invalidateIconArrayMetrics();
    GE.ge.focusjig=null;
    GE.ge.setEditor(GE.ge.editor_jig);}
  
  public void editJig(){
    ((EG_UI)GE.ge.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    GE.ge.setEditor(GE.ge.editor_jig);}
  
  public void discardJig(){
    ((EG_UI)GE.ge.editor_grammar.getUI()).panjigmenu.invalidateIconArrayMetrics();
    int a=GE.ge.focusmetagon.getJigIndex(GE.ge.focusjig)-1;
    GE.ge.focusmetagon.discardJig(GE.ge.focusjig);
    if(a<0)a=0;
    GE.ge.focusjig=GE.ge.focusmetagon.getJig(a);
    refreshUI();
    getUI().repaint();}
  
  public void setFocusJig(final ProjectJig m){
    GE.ge.focusjig=m;
    EG_UI ui=(EG_UI)getUI();
    refreshUI();
    ui.panjigmenu.repaint();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRAMMAR
   * ++++++++++++++++++++++++++++++++
   */

  public void createNewGrammar(){
    GE.ge.focusgrammar=new ProjectGrammar();
    GE.ge.focusmetagon=null;
    GE.ge.focusjig=null;
    refreshUI();}
  
  public void exportGrammar(){
    GrammarImportExport.exportGrammar();}
  
  public void importGrammar(){
    GrammarImportExport.importGrammar();
    refreshUI();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * ETC
   * ++++++++++++++++++++++++++++++++
   */
  
  public void generate(){
    GE.ge.setEditor(GE.ge.editor_generator);}

}

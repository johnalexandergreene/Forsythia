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
    ui.lblgrammarname.setText("Grammar = "+GE.focusgrammar.getName());
    ui.lblmetagonscount.setText("Count="+GE.focusgrammar.getMetagonCount());
    ui.lblmetagonjiglesscount.setText("Jigless="+GE.focusgrammar.getJiglessMetagonsCount());
    ui.lblmetagonsisolatedcount.setText("Isolated="+GE.focusgrammar.getIsolatedMetagonsCount());
    ui.lbljigscount.setText("Count="+getJigCount());

    }
  
  private int getJigCount(){
    if(GE.focusmetagon==null)
      return 0;
    else
      return GE.focusmetagon.getJigCount();}
  
  /*
   * ################################
   * CONFIGURE
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
      if(GE.focusmetagon.hasJigs())
        GE.focusjig=GE.focusmetagon.getJig(0);
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
    ((EG_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    GE.setEditor(GE.editor_createmetagon);
    refreshUI();}
  
  public void editMetagon(){
    GE.setEditor(GE.editor_editmetagondetails);
    refreshUI();}
  
  public void discardMetagon(){
    ((EG_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    ((EG_UI)GE.editor_grammar.getUI()).panjigmenu.invalidateIconArrayMetrics();
    int a=GE.focusgrammar.getIndex(GE.focusmetagon)-1;
    GE.focusgrammar.discardMetagon(GE.focusmetagon);
    if(a<0)a=0;
    GE.focusmetagon=GE.focusgrammar.getMetagon(a);
    refreshUI();}
  
  public void setFocusMetagon(final ProjectMetagon m){
    GE.focusmetagon=m;
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
    ((EG_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    GE.focusjig=null;
    GE.setEditor(GE.editor_jig);}
  
  public void editJig(){
    ((EG_UI)GE.editor_grammar.getUI()).panmetagonmenu.invalidateIconArrayMetrics();
    GE.setEditor(GE.editor_jig);}
  
  public void discardJig(){
    ((EG_UI)GE.editor_grammar.getUI()).panjigmenu.invalidateIconArrayMetrics();
    int a=GE.focusmetagon.getJigIndex(GE.focusjig)-1;
    GE.focusmetagon.discardJig(GE.focusjig);
    if(a<0)a=0;
    GE.focusjig=GE.focusmetagon.getJig(a);
    refreshUI();
    getUI().repaint();}
  
  public void setFocusJig(final ProjectJig m){
    GE.focusjig=m;
    EG_UI ui=(EG_UI)getUI();
    refreshUI();
    ui.panjigmenu.repaint();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRAMMAR
   * ++++++++++++++++++++++++++++++++
   */

  public void createNewGrammar(){
    GE.focusgrammar=new ProjectGrammar();
    GE.focusmetagon=null;
    GE.focusjig=null;
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
    GE.setEditor(GE.editor_generator);}

}

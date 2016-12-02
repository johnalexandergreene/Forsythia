package org.fleen.forsythia.app.grammarEditor.editor_ExportCompositionConfig;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.compositionExporter.CompositionExportConfig;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.forsythia.app.grammarEditor.util.Task;

/*
 * This is a composition streamer.
 * We use this for testing grammars.
 * We run the streamer, getting compositions, looking at them, tweaking them, saving them to images aand trees, etc.
 */
public class Editor_CompositionExportConfig extends Editor{

  public static final String NAME="Export Configurator";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_CompositionExportConfig(){
    super(NAME);}

  /*
   * ################################
   * UI
   * ################################
   */

  protected JPanel createUI(){
    return new CompositionExportConfigEditorUI();}
  
  /*
   * ################################
   * CONFIGURE
   * ################################
   */
  
  public void configureForOpen(){
    GE.tasksequencer.add(new Task(){
      public void doTask(){
        CompositionExportConfig c=GE.config.getCompositionExportConfig();
        CompositionExportConfigEditorUI ui=(CompositionExportConfigEditorUI)getUI();
        ui.txtexportdirpath.setText(c.exportdir.getPath());
        ui.chkboxraster.setSelected(c.exportraster);
        ui.chkboxvector.setSelected(c.exportvector);
        ui.txtsize.setText(c.rasterimagepreferredwidth+"x"+c.rasterimagepreferredheight);}});}
  
  public void configureForClose(){}

  /*
   * ################################
   * CONTROLS
   * ################################
   */
  
  public void refreshUI(){}
  
  void saveChanges(){
    CompositionExportConfig c=GE.config.getCompositionExportConfig();
    CompositionExportConfigEditorUI ui=(CompositionExportConfigEditorUI)getUI();
    c.exportdir=new File(ui.txtexportdirpath.getText());
    c.exportraster=ui.chkboxraster.isSelected();
    c.exportvector=ui.chkboxvector.isSelected();
    int[] size=getRasterImageSize();
    if(size!=null){
      c.rasterimagepreferredwidth=size[0];
      c.rasterimagepreferredheight=size[1];}
    GE.setEditor(GE.editor_generator);}
  
  int[] getRasterImageSize(){
    CompositionExportConfigEditorUI ui=(CompositionExportConfigEditorUI)getUI();
    String t=ui.txtsize.getText();
    String[] a=t.split("x");
    if(a.length!=2)return null;
    int w=-1,h=-1;
    try{
      w=new Integer(a[0]);
      h=new Integer(a[1]);
    }catch(Exception x){
      return null;}
    return new int[]{w,h};}
  
  void discardChanges(){
    GE.setEditor(GE.editor_generator);}
  
  void gleanExportPathDir(){
    CompositionExportConfigEditorUI ui=(CompositionExportConfigEditorUI)getUI();
    JFileChooser fc=new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setCurrentDirectory(new File(ui.txtexportdirpath.getText()));
    int r=fc.showOpenDialog(GE.uimain);
    if(r==JFileChooser.APPROVE_OPTION)
      ui.txtexportdirpath.setText(fc.getSelectedFile().getPath());}
  
}

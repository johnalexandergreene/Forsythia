package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.compositionExporter.CompositionRasterExporter;
import org.fleen.forsythia.app.grammarEditor.generator.Generator;
import org.fleen.forsythia.app.grammarEditor.util.Editor;
import org.fleen.forsythia.app.grammarEditor.util.HasViewer;

/*
 * This is the front end for a forsythia composition generator
 * it displays the composition and controls the generation process
 * we also provide ui for exportcomposition and configure generator, exportcomposition and whatever
 * We use this for viewing art and testing grammars.
 */
public class Editor_Generator extends Editor implements HasViewer{

  public static final String NAME="Generator";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Editor_Generator(){
    super(NAME);
    generator=new Generator();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  protected JPanel createUI(){
    return new EGUI();}
  
  public JPanel getViewer(){
    EGUI a=(EGUI)getUI();
    return a.viewer;}
  
  /*
   * ################################
   * CONFIGURE
   * ################################
   */

  public void configureForOpen(){
    refreshUI();}
  
  public void configureForClose(){
    generator.stop();}

  /*
   * ################################
   * REFRESH
   * ################################
   */
  
  //update center and fit params
  public void refreshUI(){
    refreshViewer();
    refreshGeneratorModeButtonImage();
    refreshGeneratorStartStopButtonImage();
    refreshGeneratorStateInfoLabelText();}
  
  void refreshViewer(){
    EGUI ui=(EGUI)getUI();
    ui.viewer.repaint();}
  
  void refreshGeneratorModeButtonImage(){
    EGUI ui=(EGUI)getUI();
    if(generatormode==GENERATORMODE_CONTINUOUS){
      ui.btnmode.setText("+CONTINUOUS+");
    }else{//generatormode==GENERATORMODE_INTERMITTANT
      ui.btnmode.setText("INTERMITTANT");}}
  
  //if the generator is stopped then indicate the option to start.
  //if the generator is running then indicate the option to stop.
  void refreshGeneratorStartStopButtonImage(){
    EGUI ui=(EGUI)getUI();
    if(!generator.isRunning()){
      ui.btnstartstop.setText(">>");
    }else{
      ui.btnstartstop.setText("||");}}
  
  public void refreshGeneratorStateInfoLabelText(){
    EGUI ui=(EGUI)getUI();
    ui.lblgeneratorstateinfo.setText(generator.getStateInfo());}
  
  /*
   * ################################
   * GENERATOR
   * ################################
   */

  static final int 
    GENERATORMODE_CONTINUOUS=0,
    GENERATORMODE_INTERMITTANT=1;
  
  public Generator generator;
  int generatormode=GENERATORMODE_CONTINUOUS;  
  
  /*
   * ################################
   * CONTROL
   * ################################
   */
  
  void toggleMode(){
    if(generatormode==GENERATORMODE_CONTINUOUS){
      generatormode=GENERATORMODE_INTERMITTANT;
      generator.stop();
    }else{
      generatormode=GENERATORMODE_CONTINUOUS;
      generator.stop();}
    refreshGeneratorModeButtonImage();}
  
  void generatorStartStop(){
    
    System.out.println("generator is running = "+generator.isRunning());
    System.out.println("generator.stop = "+generator.stop);
    
    if(generator.isRunning()){
      System.out.println("generator stop---");
      generator.stop();
    }else{
      System.out.println("generator start+++");
      if(generatormode==GENERATORMODE_CONTINUOUS){
        generator.startContinuous();
      }else{
        generator.startIntermittant();}}
    refreshGeneratorStartStopButtonImage();}
  
  void exportComposition(){
    CompositionRasterExporter.export();}
  
  void openGeneratorConfigurator(){
    
  }
  
  void openCompositionExporterConfigurator(){
//    GE.setEditor(ge.editor_compositionexportconfig);
  }
  
  void openGrammarConfigurator(){
//    GE.setEditor(ge.editor_grammar);
    }
  
  public void startForQInit(){
//    generatormode=GENERATORMODE_CONTINUOUS;
//    generator.startContinuous();
    
    
    refreshUI();
    System.out.println("start for Q init");}
  
}

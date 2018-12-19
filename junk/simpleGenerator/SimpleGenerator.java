package org.fleen.forsythia.junk.simpleGenerator;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JTextField;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Simple.ForsythiaGrammar_Simple;
import org.fleen.forsythia.junk.simpleComposer.ForsythiaSimpleComposer;
import org.fleen.forsythia.junk.simpleRenderer.ForsythiaSimpleRenderer;

public class SimpleGenerator{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public SimpleGenerator(){
    initUI();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  private static final String TITLE="Fleen Simple Generator 0.3";
  
  private static final int ERRORMESSAGEFONTSIZE=24;
  private static final Font ERRORMESSAGEFONT=new Font("Sans",Font.PLAIN,ERRORMESSAGEFONTSIZE);
  
  public UI ui;
  
  private void initUI(){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new UI(SimpleGenerator.this);
          ui.setDefaultWindowBounds();
          ui.setVisible(true);
          ui.setTitle(TITLE);
          ui.txtinterval.setText(String.valueOf(CREATION_INTERVAL_DEFAULT));
          ui.txtgrammar.setText(GRAMMAR_FILE_PATH_DEFAULT);
          ui.txtcomposer.setText(COMPOSER_FILE_PATH_DEFAULT);
          ui.txtrenderer.setText(RENDERER_FILE_PATH_DEFAULT);
          ui.txtexportdir.setText(EXPORT_DIR_PATH_DEFAULT);
         }catch(Exception e){
           e.printStackTrace();}}});}

  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  private static final String GRAMMAR_FILE_PATH_DEFAULT=
    "/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_06_05/g000_hexroot_kindasimple";
  
  private ForsythiaGrammar_Simple getGrammar(){
    ForsythiaGrammar_Simple grammar=null;
    try{
      File f=new File(ui.txtgrammar.getText());
      grammar=importGrammarFromFile(f);
    }catch(Exception x){}
    if(grammar==null)
      printGrammarImportFailed();
    return grammar;}
  
  private ForsythiaGrammar_Simple importGrammarFromFile(File file){
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar_Simple g=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      g=(ForsythiaGrammar_Simple)ois.readObject();
      ois.close();
    }catch(Exception x){}
    return g;}
  
  private void printGrammarImportFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("GRAMMAR IMPORT FAILED!",20,60);
    ui.panimage.repaint();}
  
  /*
   * ################################
   * COMPOSER
   * ################################
   */
  
  private static final String COMPOSER_FILE_PATH_DEFAULT="/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/composers/FSC_Basic";
  
  private ForsythiaSimpleComposer getComposer(){
    ForsythiaSimpleComposer composer=null;
    try{
      File f=new File(ui.txtcomposer.getText());
      composer=importComposerFromFile(f);
    }catch(Exception x){}
    if(composer==null)
      printComposerImportFailed();
    return composer;}
  
  private ForsythiaSimpleComposer importComposerFromFile(File file){
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaSimpleComposer c=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      c=(ForsythiaSimpleComposer)ois.readObject();
      ois.close();
    }catch(Exception x){}
    return c;}
  
  private void printComposerImportFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("COMPOSER IMPORT FAILED!",20,60);
    ui.panimage.repaint();}
  
  /*
   * ################################
   * RENDERER
   * ################################
   */
  
  private static final String RENDERER_FILE_PATH_DEFAULT=
    "/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/renderers/FSR_EggLevelSplitPaletteWithStrokes";
  
  //returns null on fail
  private ForsythiaSimpleRenderer getRenderer(){
    ForsythiaSimpleRenderer renderer=null;
    try{
      File f=new File(ui.txtrenderer.getText());
      renderer=importRendererFromFile(f);
    }catch(Exception x){}
    if(renderer==null)
      printRendererImportFailed();
    return renderer;}
  
  private ForsythiaSimpleRenderer importRendererFromFile(File file){
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaSimpleRenderer r=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      r=(ForsythiaSimpleRenderer)ois.readObject();
      ois.close();
    }catch(Exception x){}
    return r;}
  
  private void printRendererImportFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("RENDERER IMPORT FAILED!",20,60);
    ui.panimage.repaint();}
  
  /*
   * ################################
   * CREATION MODE
   * continuous or intermittant
   * ################################
   */
  
  static final boolean MODE_CONTINUOUS=false,MODE_INTERMITTANT=true; 
  boolean creationmode=MODE_INTERMITTANT;
  
  void toggleCreationMode(){
    if(creating)startStopCreation();
    creationmode=!creationmode;
    if(creationmode==MODE_CONTINUOUS){
      ui.lblmode.setText("CON");
    }else{
      ui.lblmode.setText("INT");}}
  
  /*
   * ################################
   * START-STOP CREATION
   * ################################
   */
  
  private boolean creating=false;
  
  void startStopCreation(){
    if(creationmode==MODE_INTERMITTANT){
      if(!creating){
        ui.lblstartstop.setText("||");
        doIntermittantCreation();
        ui.lblstartstop.setText(">>");}
    }else{
      if(creating){
        stopContinuousCreation();
        ui.lblstartstop.setText(">>");
        creating=false;
      }else{
        startContinuousCreation();
        ui.lblstartstop.setText("||");
        creating=true;}}}
  
  /*
   * ################################
   * CREATION INTERVAL
   * minimum interval between images in continuous creation mode
   * ################################
   */
  
  private static final long CREATION_INTERVAL_DEFAULT=1000;
  
  private long creationinterval=CREATION_INTERVAL_DEFAULT;
  
  void setCreationInterval(JTextField t){
    try{
      long a=Long.valueOf(t.getText());
      creationinterval=a;
    }catch(Exception x){
      if(t.getText().equals(""))creationinterval=0;
      t.setText(String.valueOf(creationinterval));}}
  
  /*
   * ################################
   * CREATION PROCESS
   * ################################
   */
  
  //keep this stuff class-level because it's useful
  ForsythiaGrammar_Simple grammar;
  ForsythiaSimpleComposer composer;
  //TODO renderer needs to persist the colors
  //we should deal with the strokewidth in a nonabsolute way too, so it isn't skinny for big images
  ForsythiaSimpleRenderer renderer;
  ForsythiaComposition composition;
  
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INTERMITTANT
   * ++++++++++++++++++++++++++++++++
   */
  
  private void doIntermittantCreation(){
    grammar=getGrammar();
    if(grammar==null)return;
    ForsythiaSimpleComposer composer=getComposer();
    if(composer==null)return;
    renderer=getRenderer();
    if(renderer==null)return;
    composition=composer.compose(grammar);
    image=renderer.getImage(ui.panimage.getWidth(),ui.panimage.getHeight(),composition);
    ui.panimage.repaint();
    //maybe export
    if(isExportModeAuto())
      export();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CONTINUOUS
   * ++++++++++++++++++++++++++++++++
   */
  
  private boolean stopcontinuouscreation;
  
  private void startContinuousCreation(){
    grammar=getGrammar();
    if(grammar==null)return;
    composer=getComposer();
    if(composer==null)return;
    renderer=getRenderer();
    if(renderer==null)return;
    //
    stopcontinuouscreation=false;
    new Thread(){
      public void run(){
        long 
          starttime,
          elapsedtime,
          pausetime;
        while(!stopcontinuouscreation){
          starttime=System.currentTimeMillis();
          //compose and render
          composition=composer.compose(grammar);
          image=renderer.getImage(ui.panimage.getWidth(),ui.panimage.getHeight(),composition);
          //pause if necessary
          elapsedtime=System.currentTimeMillis()-starttime;
          pausetime=creationinterval-elapsedtime;
          try{
            if(pausetime>0)Thread.sleep(pausetime,0);
          }catch(Exception x){x.printStackTrace();}
          //paint
          ui.panimage.repaint();
          //maybe export
          if(isExportModeAuto())
            export();}}
    }.start();}
  
  private void stopContinuousCreation(){
    stopcontinuouscreation=true;}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  BufferedImage image=null;
  
  private Graphics2D initImageForError(){
    int 
      w=ui.panimage.getWidth(),
      h=ui.panimage.getHeight();
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics=image.createGraphics();
    graphics.setPaint(Color.red);
    graphics.fillRect(0,0,w,h);
    return graphics;}
  
  /*
   * ################################
   * EXPORT
   * render the current composition using the current composer and renderer
   * use the image size specified in the ui
   * ################################
   */
  
  private static final String EXPORT_DIR_PATH_DEFAULT="/home/john/Desktop/sgexport";
  
  private static final int 
    EXPORTMODE_MANUAL=0,
    EXPORTMODE_AUTO=1;
  
  private static final int 
    EXPORT_DEFAULT_WIDTH=1000,
    EXPORT_DEFAULT_HEIGHT=1000;
  
  private int getExportMode(){
    if(ui.chkautoexport.isSelected())
      return EXPORTMODE_AUTO;
    else
      return EXPORTMODE_MANUAL;}
  
  boolean isExportModeManual(){
    return getExportMode()==EXPORTMODE_MANUAL;}
  
  boolean isExportModeAuto(){
    return getExportMode()==EXPORTMODE_AUTO;}
  
  void export(){
    File exportdir=getExportDir();
    if(exportdir==null||!exportdir.isDirectory()){
      printGetExportDirFailed();
      return;}
    //
    int w=EXPORT_DEFAULT_WIDTH,h=EXPORT_DEFAULT_HEIGHT;
    try{
      String a=ui.txtexportsize.getText();
      String[] b=a.split("x");
      w=Integer.valueOf(b[0]);
      h=Integer.valueOf(b[1]);
    }catch(Exception x){
      printGetExportImageSizeFailed();}
    //
    export(exportdir,w,h);}
  
  private void printGetExportDirFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("GET EXPORT DIR FAILED!",20,60);
    ui.panimage.repaint();}
  
  private void printGetExportImageSizeFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("%^&$ EXPORT IMAGE SIZE GARBLED!",20,ERRORMESSAGEFONTSIZE*2);
    g.drawString("Proper format is 123x456",20,ERRORMESSAGEFONTSIZE*4);
    g.drawString("Using default size : "+EXPORT_DEFAULT_WIDTH+"x"+EXPORT_DEFAULT_HEIGHT,20,ERRORMESSAGEFONTSIZE*6);
    ui.panimage.repaint();}
  
  private File getExportDir(){
    File exportdir=null;
    try{
      exportdir=new File(ui.txtexportdir.getText());
    }catch(Exception x){}
    return exportdir;}
  
  RasterExporter rasterexporter=new RasterExporter();
  
  private void export(File exportdir,int w,int h){
    System.out.println("export");
    BufferedImage exportimage=renderer.getImage(w,h,composition);
    rasterexporter.setExportDir(exportdir);
    rasterexporter.export(exportimage);}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new SimpleGenerator();}
  
}

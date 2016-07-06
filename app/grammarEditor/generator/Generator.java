package org.fleen.forsythia.app.grammarEditor.generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.compositionExporter.CompositionExportConfig;
import org.fleen.forsythia.composition.ForsythiaComposition;
import org.fleen.forsythia.util.simpleComposer.FSC_Basic;
import org.fleen.forsythia.util.simpleComposer.ForsythiaSimpleComposer;
import org.fleen.forsythia.util.simpleRenderer.FSR_EggLevelSplitPaletteWithStrokes;
import org.fleen.forsythia.util.simpleRenderer.ForsythiaSimpleRenderer;


/*
 * COMPOSITION GENERATOR
 * Generate a composition
 * colorize, render to bitmap
 * repeat, or not, depending on mode
 */
public class Generator{
  
  public boolean stop=true;
  
  /*
   * ################################
   * INTERFACE
   * ################################
   */
  
  public void startContinuous(){
    new Thread(){
      public void run(){
        
        setParamsForTest();
        GeneratorConfig gc=GE.config.getGeneratorConfig();
        
        //--------------------------------
        
        long 
          ccmi=gc.getContinuousCycleMinInterval(),
          starttime,
          elapsedtime,
          pausetime;
        stop=false;
        System.out.println("###START CYCLE###");
        while(!stop){
          starttime=System.currentTimeMillis();
          ForsythiaSimpleComposer composer=gc.getComposer();
          System.out.println("composing");
          composition=composer.compose(GE.focusgrammar.getForsythiaGrammar());
          if((composition!=null)&&!stop){
            ForsythiaSimpleRenderer renderer=gc.getRenderer();
            System.out.println("rendering");
            JPanel p=GE.editor_generator.getViewer();
            int 
              w=p.getWidth(),
              h=p.getHeight();
            BufferedImage image=renderer.getImage(w,h,composition);
            if(image!=null)
              viewerimage=image;}
          //pause if necessary
          elapsedtime=System.currentTimeMillis()-starttime;
          pausetime=ccmi-elapsedtime;
          try{
            if(pausetime>0){
              Thread.sleep(pausetime,0);
            }
          }catch(Exception x){
            x.printStackTrace();}
          //report 
          reportContinuousGenerationCycleFinished();}
        stop=true;
        reportStopped();}
    }.start();}
  
  public void startIntermittant(){
    new Thread(){
      public void run(){
        setPriority(MIN_PRIORITY);
        setParamsForTest();
        GeneratorConfig gc=GE.config.getGeneratorConfig();
        ForsythiaSimpleComposer composer=gc.getComposer();
        composition=composer.compose(GE.focusgrammar.getForsythiaGrammar());
        ForsythiaSimpleRenderer renderer=gc.getRenderer();
        JPanel p=GE.editor_generator.getViewer();
        int 
          w=p.getWidth(),
          h=p.getHeight();
        BufferedImage image=renderer.getImage(w,h,composition);
        if(image!=null)
          viewerimage=image;
        reportIntermittantGenerationCycleFinished();}
    }.start();}
  
  public void stop(){
    stop=true;}
  
  /*
   * SET PARAMS FOR TEST
   * set the various config stuff here
   * later we will use nice ui
   */
  private void setParamsForTest(){
    //composition export
    CompositionExportConfig ec=GE.config.getCompositionExportConfig();
    
    ec.setCompositionExportDir(new File("/home/john/Desktop/quasarcompositionexport"));
    ec.setRasterImagePreferredDimensions(860,980);
    
    //generator
    GeneratorConfig gc=GE.config.getGeneratorConfig();
    
    gc.setComposer(new FSC_Basic());
//    gc.setComposer(new FC_ChorusedVaryingDetailAreas());
    
    
    
//    Color[] 
//      color0={new Color(89,200,223),new Color(43,148,100)},
//      color1={new Color(245,223,101),new Color(209,77,40)};
    
//    //most of mystery machine
//  Color[] 
//      color0={new Color(247,120,37),new Color(211,206,61)},
//      color1={new Color(241,239,165),new Color(96,185,154)};
    
    //strong rainbow
    Color[] 
      color0={new Color(255,128,0),new Color(255,255,0)},
      color1={new Color(255,0,0),new Color(255,0,255)};
    
    Color strokecolor=Color.black;
    float strokewidth=0.008f;
    
    gc.setRenderer(new FSR_EggLevelSplitPaletteWithStrokes(color0,color1,strokecolor,strokewidth));
    
    
    
    
    
//    gc.setRenderer(new FSR_JustStrokes());
//    gc.setRenderer(new FSR_SymmetricRandomColorWithBlackStrokes());
    
    
    
    gc.setContinuousCycleMinInterval(3000);
  }
  
  /*
   * ################################
   * WORKING ELEMENTS
   * ################################
   */
  
  ForsythiaComposition composition=null;
  BufferedImage 
    viewerimage=null,
    exportimage=null;
  
  public BufferedImage getViewerImage(){
    return viewerimage;}
  
  public void invalidateWorkingElements(){
    composition=null;
    viewerimage=null;}
  
  public void flush(){
    composition=null;
    viewerimage=null;
  }
  
  /*
   * ################################
   * COMPOSITION EXPORT
   * ################################
   */
  
  //use existing composition and colormap if such exist
  //create new ones if necessary
  //need some kind of stop-override or something, because this is not going to be stoppable 
  //gotta specify scale too
  //I guess we disable the generator controls while we're exporting
  //this is run from the exporter thread
  public BufferedImage getImageForCompositionExport(){
    setParamsForTest();
    stop=false;
    GeneratorConfig gc=GE.config.getGeneratorConfig();
    ForsythiaComposition composition=Generator.this.composition;
    if(composition==null)
      composition=gc.getComposer().compose(GE.focusgrammar.getForsythiaGrammar());
    CompositionExportConfig ec=GE.config.getCompositionExportConfig();
    
    
    //TODO
  //for hexagon
//  ec.rasterimagepreferredwidth=2500;
//  ec.rasterimagepreferredheight=2000;
  
  //for 12x36 poster
  //3600x10800
//    ec.rasterimagepreferredwidth=10800;
//    ec.rasterimagepreferredheight=3600;
  
  
  
    
//    //for big hexagon
//    ec.rasterimagepreferredwidth=4000;
//    ec.rasterimagepreferredheight=4600;
    
    
    BufferedImage image=gc.getRenderer().getImage(ec.rasterimagepreferredwidth,ec.rasterimagepreferredheight,composition);
    stop=true;
    return image;}
  
  /*
   * ################################
   * EVENTS
   * ################################
   */
  
  private void reportContinuousGenerationCycleFinished(){
    System.out.println("continuous generation cycle finished");
    GE.editor_generator.refreshAll();}
  
  private void reportIntermittantGenerationCycleFinished(){
    System.out.println("intermittant generation cycle finished");
    GE.editor_generator.refreshAll();}
  
  public void reportProgress(GProgress p){
    System.out.println("progress");
    GE.editor_generator.refreshGeneratorStateInfoLabelText();}
  
  public void reportStarted(){
    System.out.println("started");
    GE.editor_generator.refreshAll();}
  
  public void reportStopped(){
    System.out.println("stopped");
    GE.editor_generator.refreshAll();}
  
  /*
   * ################################
   * STATE INFO
   * ################################
   */
  
  public boolean isRunning(){
    return !stop;}
  
  public String getStateInfo(){//TODO
    return "flooping";
  }
  
}

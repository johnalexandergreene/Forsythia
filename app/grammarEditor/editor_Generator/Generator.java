package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import java.awt.image.BufferedImage;

public class Generator{
  
  /*
   * ################################
   * STOP AND GO
   * main generator switch
   * stop generating or go generating
   * ################################
   */
  
  private static final boolean 
    STOPGO_STOP=false,
    STOPGO_GO=true;
  
  private boolean stopgo=STOPGO_STOP;
  
  void toggleStopGo(){
    stopgo=!stopgo;}
  
  void stop(){
    stopgo=STOPGO_STOP;}
  
  void go(){
    stopgo=STOPGO_GO;}
  
  boolean isStop(){
    return stopgo==STOPGO_STOP;}
  
  boolean isGo(){
    return stopgo==STOPGO_GO;}
  
  /*
   * ################################
   * MODE
   * we have 2 generation modes
   * continuous
   *   every foo milliseconds generate and display a new composition
   * intermittant
   *   at invocation of toggleStopGo(), toggling from stop to go, we 
   *   create and display a composition and then we toggle stopgo back to stop.
   * ################################
   */
  
  private static final boolean 
    MODE_INTERMITTANT=false,
    MODE_CONTINUOUS=true;
  
  private boolean mode=MODE_INTERMITTANT;
  
  void toggleMode(){
    mode=!mode;}
  
  void setModeIntermittant(){
    mode=MODE_INTERMITTANT;}
  
  void setModeContinuous(){
    mode=MODE_CONTINUOUS;}
  
  boolean isIntermittant(){
    return mode=MODE_INTERMITTANT;}
  
  boolean isContinuous(){
    return mode=MODE_CONTINUOUS;}
  
  /*
   * ################################
   * CONTINUOUS MODE INTERVAL
   * In continuous mode we have a time interval between compositions
   * it's a number of milliseconds
   * ################################
   */
  
  private static final long INTERVAL_DEFAULT=1000;
  
  private long interval=INTERVAL_DEFAULT;
  

  void setInterval(long i){
    interval=i;}
  
  long getInterval(){
    return interval;}
  
  /*
   * ################################
   * COMPOSITION DETAIL FLOOR
   * The floor on smallest polygon detail size. 
   * A polygon with detail size smaller than this does not get cultivated.
   * This value controls detail level in our compositions
   * ################################  
   */
  
  private static final double DETAILFLOOR=0.5;
  
  private double detailfloor=DETAILFLOOR;
  
  void setDetailFloor(double f){
    detailfloor=f;}
  
  double getDetailFloor(){
    return detailfloor;}
  
  /*
   * ################################
   * GENERATION
   * ################################
   */
  
  public BufferedImage getViewerImage(){
    return null;
  }
  
  
  
  
  
  

}

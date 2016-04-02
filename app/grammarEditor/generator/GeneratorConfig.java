package org.fleen.forsythia.app.grammarEditor.generator;

import java.io.Serializable;

import org.fleen.forsythia.util.simpleComposer.FSC_Basic;
import org.fleen.forsythia.util.simpleComposer.ForsythiaSimpleComposer;
import org.fleen.forsythia.util.simpleRenderer.FSR_SymmetricRandomColorWithBlackStrokes;
import org.fleen.forsythia.util.simpleRenderer.ForsythiaSimpleRenderer;

public class GeneratorConfig implements Serializable{
  
  private static final long serialVersionUID=-304607776743866253L;
  
  /*
   * ################################
   * COMPOSER
   * ################################
   */
  
  private ForsythiaSimpleComposer composer=null;
  
  public ForsythiaSimpleComposer getComposer(){
    if(composer==null)initComposer();
    return composer;}
  
  public void setComposer(ForsythiaSimpleComposer c){
    composer=c;}
  
  private void initComposer(){
    composer=new FSC_Basic();}
  
  /*
   * ################################
   * RENDERER
   * ################################
   */
  
  private ForsythiaSimpleRenderer renderer=null;
  
  public ForsythiaSimpleRenderer getRenderer(){
    if(renderer==null)initRenderer();
    return renderer;}
  
  public void setRenderer(ForsythiaSimpleRenderer r){
    renderer=r;}
  
  private void initRenderer(){
    renderer=new FSR_SymmetricRandomColorWithBlackStrokes();}
  
  /*
   * ################################
   * CONTINUOUS CYCLE INTERVAL
   * ################################
   */
  
  private static final long CONTINUOUSCYCLEMININTERVALDEFAULT=3000;//milliseconds
  private Long continuouscyclemininterval=null;
  
  public long getContinuousCycleMinInterval(){
    if(continuouscyclemininterval==null)
      continuouscyclemininterval=CONTINUOUSCYCLEMININTERVALDEFAULT;
    return continuouscyclemininterval;}
  
  public void setContinuousCycleMinInterval(long i){
    continuouscyclemininterval=i;}

}

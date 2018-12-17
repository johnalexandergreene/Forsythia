package org.fleen.forsythia.app.compositionGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMap;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMapGen;
import org.fleen.forsythia.app.compositionGenerator.composer.ForsythiaCompositionGen;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public abstract class FCRIG_Basic implements ForsythiaCompositionRasterImageGenerator{

  /*
   * ################################
   * COMPOSITION
   * ################################
   */
  
  protected ForsythiaComposition composition=null;
  
  protected abstract ForsythiaCompositionGen getComposer();

  public void regenerateComposition(){
    composition=getComposer().compose();}

  /*
   * ################################
   * COLOR MAP
   * ################################
   */
  
  protected ColorMap colormap=null;

  protected abstract ColorMapGen getColorMapper();

  public void regenerateColorMap(){
    ColorMapGen m=getColorMapper();
    m.setComposition(composition);
    colormap=m.getColorMap();}

  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  protected int imagewidth,imageheight,borderthickness;
  protected Color backgroundandborder;
  
  public abstract BufferedImage getImage();

  public int[] getImageDimensions(){
    return new int[]{imagewidth,imageheight};}
  
  public void setImageDimensions(int w,int h){
    imagewidth=w;
    imageheight=h;}

  public int getBorderThickness(){
    return borderthickness;}
  
  public void setBorderThickness(int t){
    borderthickness=t;}

  public Color getBackgroundAndBorderColor(){
    return backgroundandborder;}
  
  public void setBackgroundAndBorderColor(Color c){
    backgroundandborder=c;}
  
}

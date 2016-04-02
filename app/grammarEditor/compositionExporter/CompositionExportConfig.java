package org.fleen.forsythia.app.grammarEditor.compositionExporter;

import java.io.File;
import java.io.Serializable;

import org.fleen.forsythia.app.grammarEditor.GE;

public class CompositionExportConfig implements Serializable{

  private static final long serialVersionUID=-4966208715102378203L;
  
  //options : what format to export
  //raster, vector or both
  public boolean 
    exportraster=true,
    exportvector=false;
  
  /*
   * ################################
   * EXPORT DIR
   * ################################
   */
  
  private static final String DEFAULTEXPORTDIR="composition_export";
  public File exportdir=null;
  
  public void setCompositionExportDir(File path){
    exportdir=path;}
  
  public File getExportDir(){
    if(exportdir==null)
      initExportDir();
    return exportdir;}
  
  private void initExportDir(){
    exportdir=new File(GE.getLocalDir().getAbsolutePath()+"/"+DEFAULTEXPORTDIR);}
  
  /*
   * ################################
   * RASTER IMAGE PREFERRED DIMENSIONS
   * preferred dimensions for the exported raster image
   * we fit the image to the specified dimensions as best we can, eschewing unnecessary margin
   * then we shrink the loose dimension to fit, if necessary
   * ################################
   */
  
  private static final int 
    RASTERIMAGEPREFERREDWIDTH_DEFAULT=3000,
    RASTERIMAGEPREFERREDHEIGHT_DEFAULT=2000;
      
  public int 
    rasterimagepreferredwidth=RASTERIMAGEPREFERREDWIDTH_DEFAULT,
    rasterimagepreferredheight=RASTERIMAGEPREFERREDHEIGHT_DEFAULT;
  
  public void setRasterImagePreferredDimensions(int w,int h){
    rasterimagepreferredwidth=w;
    rasterimagepreferredheight=h;}
  
  public int[] getRasterImagePreferredDimensions(){
    return new int[]{
      rasterimagepreferredwidth,
      rasterimagepreferredheight};}
  
}

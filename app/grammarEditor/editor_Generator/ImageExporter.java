package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import java.io.File;

import javax.swing.JFileChooser;

import org.fleen.forsythia.app.grammarEditor.GE;

public class ImageExporter{
  
  /*
   * ################################
   * EXPORT DIRECTORY
   * ################################
   */
  
  private File exportdirectory=null;
  
  void setExportDirectory(File f){
    exportdirectory=f;}
  
  File getExportDirectory(){
    if(exportdirectory==null)
      exportdirectory=GE.getLocalDir();
    return exportdirectory;}
  
  /*
   * ################################
   * IMAGE SIZE
   * The longest side of the image, including margin
   * ################################
   */
  
  private static final int IMAGESIZE_DEFAULT=1000;
  
  int imagesize=IMAGESIZE_DEFAULT;
  
  void setImageSize(int s){
    imagesize=s;}
  
  int getImageSize(){
    return imagesize;}
  
  /*
   * ################################
   * IMAGE FILE CREATION AND EXPORT
   * ################################
   */
  
  

}

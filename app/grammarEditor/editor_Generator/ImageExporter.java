package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.fleen.forsythia.app.grammarEditor.GE;

import com.sun.imageio.plugins.png.PNGMetadata;

public class ImageExporter implements Serializable{
  
  private static final long serialVersionUID=-5177659741259613723L;

  /*
   * ################################
   * EXPORT DIRECTORY
   * ################################
   */
  
  private File exportdirectory=null;
  
  public void setExportDirectory(File f){
    exportdirectory=f;}
  
  public File getExportDirectory(){
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
  
  private int imagesize=IMAGESIZE_DEFAULT;
  
  public void setImageSize(int s){
    imagesize=s;}
  
  public int getImageSize(){
    return imagesize;}
  
  /*
   * ################################
   * WRITE PNG IMAGE FILE
   * ################################
   */
  
  //To get our pixelsPerUnitXAxis value for the PNG image metadata we multiply this by our 
  //specified DPI value.
  private static final double INCHES_IN_A_METER=39.3700787;
  private static final int DPI=300;
  private static final String IMAGEFILEPREFIX="i";
  
  public void writePNGImageFile(BufferedImage image){
    System.out.println("write image");
    File file=getExportFile();
    write(image,file);}
  
  private File getExportFile(){
    File test=null;
    boolean nameIsUsed=true;
    int index=0;
    while(nameIsUsed){
      test=new File(getExportDirectory().getPath()+"/"+IMAGEFILEPREFIX+index+".png");
      if(test.exists()){
        index++;
      }else{
        nameIsUsed=false;}}
    return test;}
  
  private void write(BufferedImage image,File file){
    Iterator<ImageWriter> i=ImageIO.getImageWritersBySuffix("png");
    ImageWriter writer=(ImageWriter)i.next();
    ImageOutputStream imageOutputstream=null;
    try{
      imageOutputstream=ImageIO.createImageOutputStream(file);
    }catch(Exception e){
      e.printStackTrace();}
    writer.setOutput(imageOutputstream);
    PNGMetadata metaData=
      (PNGMetadata)writer.getDefaultImageMetadata(new ImageTypeSpecifier(image),null);
    metaData.pHYs_pixelsPerUnitXAxis=(int)(DPI*INCHES_IN_A_METER);
    metaData.pHYs_pixelsPerUnitYAxis=(int)(DPI*INCHES_IN_A_METER);
    metaData.pHYs_present=true;
    metaData.pHYs_unitSpecifier=PNGMetadata.PHYS_UNIT_METER;
    try{
      writer.write(null,new IIOImage(image,null,metaData),null);
      imageOutputstream.flush();
      imageOutputstream.close();
    }catch(Exception e){
      e.printStackTrace();}}

}

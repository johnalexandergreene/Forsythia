package org.fleen.forsythia.app.bread.SpinnerFrameGleaner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.sun.imageio.plugins.png.PNGMetadata;

public class RasterExporter{
  
  public RasterExporter(String a){
    setExportDir(new File(a));}
  
  /*
   * ################################
   * EXPORT DIR
   * ################################
   */
  
  File exportdir;
  
  void setExportDir(File exportdir){
    this.exportdir=exportdir;}
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  //To get our pixelsPerUnitXAxis value for the PNG image metadata we multiply this by our 
  //specified DPI value.
  private static final double INCHES_IN_A_METER=39.3700787;
  private static final int DPI=300;
  private static final String IMAGEFILEPREFIX="i";
  
  File export(BufferedImage image){
    File file=getExportFile(exportdir);
    write(image,file);
    return file;}
  
  private File getExportFile(File exportdir){
    File f=null;
    boolean nameisused=true;
    int index=0;
    while(nameisused){
      f=new File(exportdir.getPath()+"/"+IMAGEFILEPREFIX+index+".png");
      if(f.exists()){
        index++;
      }else{
        nameisused=false;}}
    return f;}
  
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

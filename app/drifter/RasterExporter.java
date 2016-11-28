package org.fleen.forsythia.app.drifter;

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
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  RasterExporter(File exportdir){
    this.exportdir=exportdir;}
  
  /*
   * ################################
   * EXPORT DIR
   * ################################
   */
  
  File exportdir;
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  //To get our pixelsPerUnitXAxis value for the PNG image metadata we multiply this by our 
  //specified DPI value.
  private static final double INCHES_IN_A_METER=39.3700787;
  private static final int DPI=300;
  
  File export(BufferedImage image,int index){
    File file=getExportFile(exportdir,index);
    write(image,file);
    return file;}
  
  private File getExportFile(File exportdir,int index){
    String s = String.format("%1$05d",index);
    File test=new File(exportdir.getPath()+"/"+s+".png");
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

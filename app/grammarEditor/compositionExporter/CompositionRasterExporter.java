package org.fleen.forsythia.app.grammarEditor.compositionExporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.fleen.forsythia.app.grammarEditor.GE;

import com.sun.imageio.plugins.png.PNGMetadata;

public class CompositionRasterExporter{
  
  //To get our pixelsPerUnitXAxis value for the PNG image metadata we multiply this by our 
  //specified DPI value.
  private static final double INCHES_IN_A_METER=39.3700787;
  private static final int DPI=300;
  
  public static void export(){
    BufferedImage image=GE.ge.editor_generator.generator.getImageForCompositionExport();
    writeImage(image);
    System.out.println("COMPOSITION EXPORT FINISHED");}

  static BufferedImage image;
  static File file;
  static int dpi;
  
  private static void writeImage(BufferedImage image){
    System.out.println("write image");
    File exportdir=new CompositionExportConfig().getExportDir();
    CompositionRasterExporter.image=image;
    CompositionRasterExporter.file=getExportFile(exportdir);
    CompositionRasterExporter.dpi=DPI;
    write();}
  
  private static final String IMAGEFILEPREFIX="i";
  
  static File getExportFile(File exportdir){
    File test=null;
    boolean nameIsUsed=true;
    int index=0;
    while(nameIsUsed){
      test=new File(exportdir.getPath()+"/"+IMAGEFILEPREFIX+index+".png");
      if(test.exists()){
        index++;
      }else{
        nameIsUsed=false;}}
    return test;}
  
  static void write(){
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
    metaData.pHYs_pixelsPerUnitXAxis=(int)(dpi*INCHES_IN_A_METER);
    metaData.pHYs_pixelsPerUnitYAxis=(int)(dpi*INCHES_IN_A_METER);
    metaData.pHYs_present=true;
    metaData.pHYs_unitSpecifier=PNGMetadata.PHYS_UNIT_METER;
    try{
      writer.write(null,new IIOImage(image,null,metaData),null);
      imageOutputstream.flush();
      imageOutputstream.close();
    }catch(Exception e){
      e.printStackTrace();}}

}

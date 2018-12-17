package org.fleen.forsythia.app.compositionGenerator.generators.fc0007_5ishHexStainedGlass;

import org.fleen.forsythia.app.compositionGenerator.head.Head_Single;

/*
 * A simple hexagon
 * black stroke
 * white background
 * for windowcling
 * a hexagon about 6" across
 */
public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
  EXPORTIMAGEWIDTH=1900,
  EXPORTIMAGEHEIGHT=1900,
  BORDERTHICKNESS=64;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}

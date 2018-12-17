package org.fleen.forsythia.app.compositionGenerator.generators.fc0002_poster_18x30_VariableDensity;

import org.fleen.forsythia.app.compositionGenerator.head.Head_Single;

public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
    EXPORTIMAGEWIDTH=9095,
    EXPORTIMAGEHEIGHT=5274,
    BORDERTHICKNESS=32;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}

package org.fleen.forsythia.app.compositionGenerator.generators.fc0018_poster_18x30_BW_hexmandala;

import org.fleen.forsythia.app.compositionGenerator.HeadAbstract;

public class Head extends HeadAbstract{
  
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

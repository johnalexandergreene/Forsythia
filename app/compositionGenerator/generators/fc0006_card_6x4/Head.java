package org.fleen.forsythia.app.compositionGenerator.generators.fc0006_card_6x4;

import org.fleen.forsythia.app.compositionGenerator.head.Head_Single;

public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
  EXPORTIMAGEWIDTH=1800,
  EXPORTIMAGEHEIGHT=1200,
  BORDERTHICKNESS=0;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}

package org.fleen.forsythia.app.compositionGenerator.generators.fc0000_poster_18x30_forpublic;

public class Head0 extends Head_Single{
  
//  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
    EXPORTIMAGEWIDTH=9095,
    EXPORTIMAGEHEIGHT=5274,
    BORDERTHICKNESS=32;
  
  Head0(Gen g){
    super(g);
//    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head0(new Gen());}

}

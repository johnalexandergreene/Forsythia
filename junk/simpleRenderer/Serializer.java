package org.fleen.forsythia.junk.simpleRenderer;

import java.awt.Color;

public class Serializer{
  
  static final String SERIALIZEDRENDEREREXPORTDIRPATH="/home/john/projects/serialized_fleen_things";
  
  
  
//  static final Color[] 
//    COLOR0={new Color(239,250,180),new Color(255,196,140)},
//    COLOR1={new Color(255,159,128),new Color(245,105,145)};
//  static final Color COLOR_STROKE=new Color(64,64,64);
  
//  static final Color[] 
//    COLOR0={new Color(247,120,37),new Color(211,206,61)},
//    COLOR1={new Color(241,239,165),new Color(96,185,154)};
//  static final Color COLOR_STROKE=new Color(85,66,54);
  
//  static final Color[] 
//      COLOR0={new Color(153,184,152),new Color(254,206,168)},
//      COLOR1={new Color(255,132,124),new Color(232,74,95)};
//    static final Color COLOR_STROKE=new Color(42,54,59);
  
//  static final Color[] 
//      COLOR0={new Color(255,78,80),new Color(252,145,58)},
//      COLOR1={new Color(249,212,35),new Color(225,245,196)};
//    static final Color COLOR_STROKE=new Color(42,54,59);
  
  static final Color[] 
      COLOR0={new Color(255,141,0),new Color(208,255,138)},
      COLOR1={new Color(255,13,219),new Color(232,197,12)};
    static final Color COLOR_STROKE=new Color(64,64,64);
  
  static final float STROKEWIDTH=0.005f;
  
  public static final void main(String[] a){
    new FSR_EggLevelSplitPaletteWithStrokes_RasterMapped(COLOR0,COLOR1).serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
    
//    new FSR_EggLevelSplitPaletteWithStrokes(COLOR0,COLOR1,COLOR_STROKE,STROKEWIDTH).serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
//    new FSR_EggLevelWithStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
//    new FSR_JustStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
//    new FSR_SymmetricRandomColorWithBlackStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
//    new FSR_SymmetricRandomEggLevelColorWithBlackStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
  }

}

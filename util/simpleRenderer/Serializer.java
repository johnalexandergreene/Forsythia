package org.fleen.forsythia.util.simpleRenderer;

import java.awt.Color;

public class Serializer{
  
  static final String SERIALIZEDRENDEREREXPORTDIRPATH="/home/john/projects/code/serialized_fleen_renderers";
  
  static final Color[] 
    COLOR0={},
    COLOR1={};
  static final Color COLOR_STROKE=Color.black;
  static final float STROKEWIDTH=2.0f;
  
  public static final void main(String[] a){
    new FSR_EggLevelSplitPaletteWithStrokes(COLOR0,COLOR1,COLOR_STROKE,STROKEWIDTH).serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
    new FSR_EggLevelWithStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
    new FSR_JustStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
    new FSR_SymmetricRandomColorWithBlackStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
    new FSR_SymmetricRandomEggLevelColorWithBlackStrokes().serialize(SERIALIZEDRENDEREREXPORTDIRPATH);
  }

}

package org.fleen.forsythia.util.simpleComposer;

import java.awt.Color;

public class Serializer{
  
  static final String SERIALIZEDCOMPOSERXPORTDIRPATH="/home/john/projects/code/serialized_fleen_composers";
  
  static final Color[] 
    COLOR0={},
    COLOR1={};
  static final Color COLOR_STROKE=Color.black;
  static final float STROKEWIDTH=2.0f;
  
  public static final void main(String[] a){
    new FSC_ChorusedVaryingDetailAreas().serialize(SERIALIZEDCOMPOSERXPORTDIRPATH);
    new FSC_Basic().serialize(SERIALIZEDCOMPOSERXPORTDIRPATH);
  }

}

package org.fleen.forsythia.util.simpleComposer;

import java.awt.Color;

public class Serializer{
  
  static final String SERIALIZEDCOMPOSERXPORTDIRPATH="/home/john/projects/serialized_fleen_things";
  
  static final Color[] 
    COLOR0={},
    COLOR1={};
  static final Color COLOR_STROKE=Color.black;
  static final float STROKEWIDTH=2.0f;
  
  //composer
  public static final double DETAILSIZEFLOOR=0.02;
  public static final double SYMMETRY=1.0;
  
  public static final void main(String[] a){
//    new FSC_ChorusedVaryingDetailAreas().serialize(SERIALIZEDCOMPOSERXPORTDIRPATH);
    new FSC_Basic(DETAILSIZEFLOOR,SYMMETRY).serialize(SERIALIZEDCOMPOSERXPORTDIRPATH);
  }

}

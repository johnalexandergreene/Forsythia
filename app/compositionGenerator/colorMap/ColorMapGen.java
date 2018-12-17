package org.fleen.forsythia.app.compositionGenerator.colorMap;

import java.awt.Color;

import org.fleen.forsythia.core.composition.ForsythiaComposition;

public interface ColorMapGen{
  
  ColorMap getColorMap();
  
  void setPalette(Color[] p);
  
  Color[] getPalette();
  
  void setComposition(ForsythiaComposition c);
  
  ForsythiaComposition getComposition();

}

package org.fleen.forsythia.app.compositionGenerator.colorMap;

import java.awt.Color;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

/*
 * map colors to polygons prettily
 */
public interface ColorMap{
  
  Color getColor(FPolygon p);
  
  void map(ForsythiaComposition composition,Color[] palette);
  
  void regenerate();

}

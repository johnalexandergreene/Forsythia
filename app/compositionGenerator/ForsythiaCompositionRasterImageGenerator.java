package org.fleen.forsythia.app.compositionGenerator;

import java.awt.image.BufferedImage;

public interface ForsythiaCompositionRasterImageGenerator{
  
  void regenerateComposition();
  
  void regenerateColorMap();
  
  BufferedImage getImage();
  
  void setImageDimensions(int w,int h);
  
  void setBorderThickness(int t);

}

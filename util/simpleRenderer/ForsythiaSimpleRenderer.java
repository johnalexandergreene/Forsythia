package org.fleen.forsythia.util.simpleRenderer;

import java.awt.image.BufferedImage;

import org.fleen.forsythia.composition.ForsythiaComposition;

public interface ForsythiaSimpleRenderer{
  
  BufferedImage getImage(int width,int height,ForsythiaComposition composition);

}

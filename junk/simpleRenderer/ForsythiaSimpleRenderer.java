package org.fleen.forsythia.junk.simpleRenderer;

import java.awt.image.BufferedImage;

import org.fleen.forsythia.core.composition.ForsythiaComposition;

public interface ForsythiaSimpleRenderer{
  
  BufferedImage getImage(int width,int height,ForsythiaComposition composition);

}

package org.fleen.forsythia.app.bread.renderer;

import java.awt.image.BufferedImage;

import org.fleen.forsythia.core.composition.ForsythiaComposition;

public interface Renderer{
  
  BufferedImage getImage(int width,int height,ForsythiaComposition composition);

}

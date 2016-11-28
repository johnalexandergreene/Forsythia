package org.fleen.forsythia.app.strobe;

import java.awt.image.BufferedImage;

/*
 * get the polygons
 *   within the transformed rectangle
 *   develop the composition if necessary
 * render those polygons to an image
 */
public interface Renderer{
  
  BufferedImage getImage();

}

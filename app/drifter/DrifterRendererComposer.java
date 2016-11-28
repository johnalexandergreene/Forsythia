package org.fleen.forsythia.app.drifter;

import java.awt.image.BufferedImage;

/*
 * get the polygons
 *   within the transformed rectangle
 *   develop the composition if necessary
 * render those polygons to an image
 */
public interface DrifterRendererComposer{
  
  BufferedImage getImage(
    ViewportDef viewportdef,
    double visibledetailsizefloor,double visibledetailsizeceiling);

}

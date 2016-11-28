package org.fleen.forsythia.app.bread;

import java.awt.Graphics2D;

import org.fleen.forsythia.composition.FPolygon;

/*
 * 
 * Paints a polygon's expression to a Java Graphics2D object
 * A polygon may have several of these
 */
public interface Voice_Graphics2D{
  
  void paint(FPolygon polygon,Graphics2D graphics, double time);

}

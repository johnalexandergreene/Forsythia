package org.fleen.forsythia.app.compositionGenerator.generators.fc0004_poster_18x30_Stamp;

import org.fleen.forsythia.app.compositionGenerator.HeadAbstract;

/*
 * STAMP
 * It's a tweak to the composer and renderer
 * 
 * Given a couple of metagons (hexagon, triangle, or whatever) tagged appropriately : stampgons
 * in cultivation process, cap a few polygons of type stampgon at the appropriate scale, tag too
 * at render, at some point, render a fitted image to each stampgon. Skulls or butterflies or whatever
 * probably stroke over that
 * 
 * ---
 * 
 * tag hexagon in grammar editor : stampgon_hexagon
 * at composer test tagged polygons. Probablistically cap and tag
 * at renderer, at fill, paint provided image
 * then do stroke. Ya, probably in that order
 * 
 * ----
 * 
 * A couple of way to do it
 * 
 * 1) render onto a leaf polygon. Simple.
 * 
 * 2)render onto the parent of a leaf, or the grandparent, 
 *   and then render colored polygons over that using a multiply blend or something.
 *   This would be a little more complex. It might be a nice effect.
 *   
 * ----
 * rendering will probable be in "dark". For nice contrast with the pastel colors and the white strokes.
 * Or maybe we could use a few different colors 
 *   
 * 
 * 
 * 
 */
public class Head extends HeadAbstract{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
//  EXPORTIMAGEWIDTH=9095,
//  EXPORTIMAGEHEIGHT=5274,
  EXPORTIMAGEWIDTH=2000,
  EXPORTIMAGEHEIGHT=1000,
  BORDERTHICKNESS=32;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}

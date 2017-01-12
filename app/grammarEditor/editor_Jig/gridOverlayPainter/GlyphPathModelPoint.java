package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import org.fleen.geom_2D.DPoint;

/*
 * defined in terms of either xy coors or offset on path
 */
@SuppressWarnings("serial")
class GlyphPathModelPoint extends DPoint{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  GlyphPathModelPoint(GlyphPathModel model,DPoint p,int index,int type){
    super(p);
    this.model=model;
    this.index=index;
    this.type=type;}
  
  /*
   * ################################
   * MODEL 
   * ################################
   */
  
  GlyphPathModel model;
  
  /*
   * ################################
   * ATTRIBUTES 
   * ################################
   */
  
  int type,index;
  boolean
    arrowstart=false,
    arrowend=false;
  
  /*
   * ################################
   * GEOMETRY 
   * ################################
   */
  
  GlyphPathModelPoint getNextPoint(){
    return model.get(index+1);}
  
  GlyphPathModelPoint getPriorPoint(){
    return model.get(index-1);}
  
  
  
}

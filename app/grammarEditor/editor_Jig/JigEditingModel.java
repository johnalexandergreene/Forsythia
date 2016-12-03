package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;

class JigEditingModel{
  
  JigEditingModel(){
    initGraph();
  }
  
  /*
   * ################################
   * GRID DENSITY
   * When we change the grid density we re-initialize the geometry and clear everything
   * ################################
   */
  
  private static final int GRIDDENSITYLOWERLIMIT=1;
  
  int griddensity=GRIDDENSITYLOWERLIMIT;
  
  void incrementGridDensity(){
    griddensity++;
    initGraph();}
  
  void decrementGridDensity(){
    if(griddensity>GRIDDENSITYLOWERLIMIT){
      griddensity--;
      initGraph();}}
  
  String getGridDensityString(){
    String s="GD="+String.format("%03d",griddensity);
    return s;}
  
  /*
   * ################################
   * GRAPH
   * We add, remove and connect vertices
   * ################################
   */
  
  RawGraph rawgraph;
  
  void initGraph(){
    rawgraph=new RawGraph(GE.focusmetagon.kpolygon);
    
  }
  
  /*
   * ################################
   * JIG TAGS
   * ################################
   */
  
  
  /*
   * ################################
   * SECTION TAGS
   * ################################
   */
  
  /*
   * ################################
   * SECTION ANCHORS
   * ################################
   */
  
  /*
   * ################################
   * SECTION CHORUS INDICES
   * ################################
   */
  
  
  
  
  
  
  
  

}

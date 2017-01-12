package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DPoint;

@SuppressWarnings("serial")
public class ArrowModels extends ArrayList<ArrowModel>{
  
  public ArrowModels(GlyphPathModel pathmodel){
    createArrowModels(pathmodel);
  }
  
  private void createArrowModels(GlyphPathModel pathmodel){
    List<DPoint> shaftpoints=new ArrayList<DPoint>();
    for(GlyphPathModelPoint p:pathmodel){
      if(p.arrowhead||p.arrowtail||p.corner)
        shaftpoints.add(p);
      if(p.arrowhead){
        add(new ArrowModel(shaftpoints));
        shaftpoints=new ArrayList<DPoint>();}}}
  

}

package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.util.List;

import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;

class JigSectionModelMetagonAndAnchors{
  
  JigSectionModelMetagonAndAnchors(ProjectMetagon metagon,List<KAnchor> anchors){
    this.metagon=metagon;
    this.anchors=anchors;}
  
  ProjectMetagon metagon;
  List<KAnchor> anchors;

}

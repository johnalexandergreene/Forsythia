package org.fleen.forsythia.app.grammarEditor.project.jig;

import java.util.List;

import org.fleen.forsythia.app.grammarEditor.project.metagon.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;

class MetagonAndAnchors{
  
  MetagonAndAnchors(ProjectMetagon metagon,List<KAnchor> anchors){
    this.metagon=metagon;
    this.anchors=anchors;}
  
  ProjectMetagon metagon;
  List<KAnchor> anchors;

}

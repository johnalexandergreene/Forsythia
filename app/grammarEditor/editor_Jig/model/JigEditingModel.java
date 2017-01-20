package org.fleen.forsythia.app.grammarEditor.editor_Jig.model;

import java.util.List;

import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;

public interface JigEditingModel{
  
  /*
   * ################################
   * JIGS
   * ################################
   */
  
  void incrementGridDensity();
  
  void decrementGridDensity();
  
  int getGridDensity();
  
  RawGraph getRawGraph();
  
  void setJigTags(String tags);
  
  String getJigTags();
  
  /*
   * ################################
   * SECTIONS
   * ################################
   */
  
  int getSectionCount();
  
  List<KPolygon> getSectionPolygons();
  
  void incrementSectionChorus(int sectionindex);
  
  int getSectionChorus(int sectionindex);
  
  void incrementSectionAnchor(int sectionindex);
  
  int getSectionAnchorIndex(int sectionindex);
  
  void setSectionTags(int sectionindex,String tags);
  
  String getSectionTags(int sectionindex);
  
  

}

package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.awt.geom.Path2D;

import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * id for jig model primary elements
 * implemented by JigDetailsModel and JigSectionDetailsModel
 */
public interface FocusableModelElement{
  
  KPolygon getKPolygon();
  
  Path2D getPolygonPath();
  
  void setTags(String t);
  
  String getTags();
  
  String getElementIDString();
  
  String toString();
  
}

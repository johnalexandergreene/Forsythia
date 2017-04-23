package org.fleen.forsythia.app.grammarEditor.util.grid;

import org.fleen.geom_Kisrhombille.KPoint;

public interface GridMouseListener{
  
  //the point is transformed to grid 
  void touch(double[] p,KPoint v);
  
  void movedCloseToVertex(KPoint v);
  
  void movedFarFromVertex(double[] p);

}

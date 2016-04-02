package org.fleen.forsythia.app.grammarEditor.util.grid;

import org.fleen.geom_Kisrhombille.KVertex;

public interface GridMouseListener{
  
  //the point is transformed to grid 
  void touch(double[] p,KVertex v);
  
  void movedCloseToVertex(KVertex v);
  
  void movedFarFromVertex(double[] p);

}

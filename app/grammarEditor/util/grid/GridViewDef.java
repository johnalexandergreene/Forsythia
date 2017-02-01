package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.io.Serializable;

public class GridViewDef implements Serializable{
  
  private static final long serialVersionUID=5014696378909507829L;

  //dimension of viewport
  int w,h;
  
  double 
    scale,
    //center in unscaled terms
    centerx,
    centery;

}

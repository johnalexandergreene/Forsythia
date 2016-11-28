package org.fleen.forsythia.core.composition;

import org.fleen.geom_Kisrhombille.KGrid;

/*
 * forsythia grid node
 * comes in 2 flavors : root and transform
 */
public abstract class FGrid extends ForsythiaTreeNode{
  
  private static final long serialVersionUID=-2082766167662596276L;

  /*
   * ################################
   * LOCAL KGRID
   * The summation of root grid and any grid transforms at this grid node rendered as a kgrid
   * ################################
   */
  
  protected KGrid localkgrid=null;

  public KGrid getLocalKGrid(){
    if(localkgrid==null)localkgrid=initLocalKGrid();
    return localkgrid;}
  
  protected abstract KGrid initLocalKGrid();

}

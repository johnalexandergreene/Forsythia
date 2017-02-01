package org.fleen.forsythia.core.composition;

import java.io.Serializable;

import org.fleen.geom_Kisrhombille.KGrid;

/*
 * forsythia grid node
 * comes in 2 flavors : root and transform
 */
public abstract class FGrid extends ForsythiaTreeNode implements Serializable{
  
  private static final long serialVersionUID=3593560887876552589L;
  
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

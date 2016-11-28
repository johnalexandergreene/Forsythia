package org.fleen.forsythia.core.composition;

import org.fleen.geom_Kisrhombille.KGrid;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * NODE GRID TRANSFORM
 * One way or another we glean the params of this transform
 * We might do params, we might derive from an ancestor polygon 
 * We might do a little of both (like we do in pGrammatic), 
 */
public class FGridTransform extends FGrid{
  
  private static final long serialVersionUID=-220936433111379307L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FGridTransform(
    KVertex origintransform,
    int forewardtransform,
    boolean twisttransform,
    double fishtransform){
    this.origintransform=origintransform;
    this.forewardtransform=forewardtransform;
    this.twisttransform=twisttransform;
    this.fishtransform=fishtransform;}
  
  /*
   * ################################
   * TRANSFORM PARAMS
   * ################################
   */
  
  //a kvertex relative to origin of the uptree kgrid 
  public KVertex origintransform;
  //a direction offset in terms of the foreward and twist of the uptree kgrid 
  public int forewardtransform;
  //twist relative to twist of uptree kgrid.
  //either same twist as prior element (true, aka 'positive') or opposite (false, aka 'negative').
  public boolean twisttransform;
  //a factor. Simply multiply by fish of the uptree kgrid
  public double fishtransform;
  
  /*
   * ################################
   * GRID
   * ################################
   */
  
  protected void flushLocalGeometryCache(){
    localkgrid=null;}
  
  protected KGrid initLocalKGrid(){
    KGrid priorgrid=getFirstAncestorGrid().getLocalKGrid();
    //
    double[] gridorigin=priorgrid.getPoint2D(origintransform);
    //
    double gridforeward=priorgrid.getDirection2D(forewardtransform);
    //
    boolean gridtwist=priorgrid.getTwist();
    if(!twisttransform)gridtwist=!gridtwist;
    //
    double gridfish=priorgrid.getFish()*fishtransform;
    //
    return new KGrid(
      gridorigin,
      gridforeward,
      gridtwist,
      gridfish);}

}

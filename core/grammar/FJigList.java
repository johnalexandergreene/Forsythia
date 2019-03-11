package org.fleen.forsythia.core.grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fleen.forsythia.core.Forsythia;

/*
 * a list of Jigs
 * sorted in ascending order by detail size preview value
 * 
 * so when we're gathering the list of all jigs that are above the detailsizelimit 
 *   we start big and go down the list until we hit the limit.  
 * 
 * with handy access methods
 */
public class FJigList extends ArrayList<FJig> implements Forsythia{

  private static final long serialVersionUID=866462351907868545L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FJigList(Collection<FJig> jigs){
    super(jigs.size());
    init(jigs);}
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  private void init(Collection<FJig> jigs){
    addAll(jigs);
    Collections.sort(this,new DetailSizeComparator());}
  
  private class DetailSizeComparator implements Comparator<FJig>{
    public int compare(FJig j0,FJig j1){
      double 
        bds0=j0.getDetailSize(),
        bds1=j1.getDetailSize();
      if(bds0==bds1){
        return 0;
      }else if(bds0>bds1){
        return 1;}
      return -1;}}
  
  /*
   * ################################
   * JIGS ACCESS
   * various jig filters
   * ################################
   */
  
  List<FJig> getJigsAboveFloorWithTags(String[] tags,double floor){
    List<FJig> 
      a=getJigsAboveDetailSizeFloor(floor),
      b=new ArrayList<FJig>();
    for(FJig j:a)
      if(j.hasTags(tags))
        b.add(j);
    return b;}
  
  /*
   * Return a sublist of this list containing all jigs that return a 
   *   detailsize above a specified floor
   * we start at the end of the list (where the biggest detail sizes are) and 
   *   move backewards until we hit one beneath the floor, at which point we stop.
   * If all of the jigs in the jist have a detail size beneath the floor then 
   *   we return an empty list 
   */
  public List<FJig> getJigsAboveDetailSizeFloor(double floor){
    List<FJig> jigs=new ArrayList<FJig>();
    FJig jig;
    SEEK:for(int i=size()-1;i>-1;i--){
      jig=get(i);
      if(jig.getDetailSize()<floor)
        break SEEK;
      jigs.add(jig);}
    return jigs;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return "["+getClass().getSimpleName()+" "+"size="+size()+"]";}
  
}

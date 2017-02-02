package org.fleen.forsythia.core.grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fleen.forsythia.core.Forsythia;
import org.fleen.forsythia.core.composition.FPolygon;

/*
 * a list of Jigs
 * sorted in ascending order by detail size preview value
 * with handy access methods
 */
public class JigList extends ArrayList<Jig> implements Forsythia{

  private static final long serialVersionUID=866462351907868545L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public JigList(Collection<Jig> jigs){
    super(jigs.size());
//    System.out.println("CREATING JIG LIST");
//    System.out.println("param jigs size:"+jigs.size());
    init(jigs);
//    System.out.println("post init size:"+size());
    }
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  private void init(Collection<Jig> jigs){
    addAll(jigs);
    Collections.sort(this,new DetailSizeComparator());}
  
  private class DetailSizeComparator implements Comparator<Jig>{
    public int compare(Jig j0,Jig j1){
      double 
        bds0=j0.getDetailSizePreviewBaseDetailSize(),
        bds1=j1.getDetailSizePreviewBaseDetailSize();
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
  
  /*
   * Return a sublist of this list containing all jigs that return a 
   *   detailsizepreview above a specified floor
   * we start at the end of the list (where the biggest detail sizes are) and 
   *   move backewards until we hit one beneath the floor, at which point we stop.
   * If all of the jigs in the jist have a detail size beneath the floor then 
   *   we return an empty list 
   */
  public List<Jig> getJigsAboveDetailSizeFloor(FPolygon target,double floor){
    List<Jig> jigs=new ArrayList<Jig>();
    Jig jig;
    SEEK:for(int i=size()-1;i>-1;i--){
      jig=get(i);
      if(jig.getDetailSizePreview(target)<floor)
        break SEEK;
      jigs.add(jig);}
    return jigs;}
  
  public List<Jig> getJigsAboveDetailSizeFloorWithTags(FPolygon target,double floor,String[] tags){
    List<Jig> 
      a=getJigsAboveDetailSizeFloor(target,floor),
      b=new ArrayList<Jig>();
    for(Jig j:a)
      if(j.hasTags(tags))
        b.add(j);
    return b;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return "["+getClass().getSimpleName()+" "+"size="+size()+"]";}
  
}

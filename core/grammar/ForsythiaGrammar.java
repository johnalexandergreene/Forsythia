package org.fleen.forsythia.core.grammar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_Kisrhombille.KMetagon;

/*
 * A collection of metagons and jigs
 * access methods
 */
public class ForsythiaGrammar implements Serializable{
  
  private static final long serialVersionUID=3018836034565752313L;

  /*
   * ################################
   * CONSTRUCTOR
   * init with a map of metagons and jig collections
   * ################################
   */
  
  public ForsythiaGrammar(Map<FMetagon,? extends Collection<FJig>> metagonjigs){
    Collection<FJig> c;
    for(FMetagon a:metagonjigs.keySet()){
      c=metagonjigs.get(a);
      this.metagonjigs.put(a,new FJigList(c));}}
 
  /*
   * ################################
   * METAGONS AND JIGS
   * Implementation of ForsythiaGrammar interface
   * ################################
   */

  private Map<FMetagon,FJigList> metagonjigs=new Hashtable<FMetagon,FJigList>();
  
  public FMetagon getRandomMetagon(String[] tags){
    List<FMetagon> a=new ArrayList<FMetagon>();
    if(tags!=null&&tags.length!=0){
      for(FMetagon m:metagonjigs.keySet())
        if(m.hasTags(tags))
          a.add(m);
    }else{
      a.addAll(metagonjigs.keySet());}
    if(a.isEmpty())return null;
    Random random=new Random();
    FMetagon b=a.get(random.nextInt(a.size()));
    return b;}
  
  public FJig getRandomJig(FMetagon m,String[] tags,double detailfloor){
    Random random=new Random();//randomm was spitting out zeros. Bug?
    //so we're instantiating it in method instead of class
    FJigList a=metagonjigs.get(m);
    List<FJig> b=a.getJigsAboveFloorWithTags(tags,detailfloor);
    if(b.isEmpty())return null;
    FJig j=b.get(random.nextInt(b.size()));
    return j;}
  
  /*
   * ################################
   * MORE ACCESS METHODS
   * not part of ForsythiaGrammar interface
   * used in the grammar editor
   * ################################
   */
  
  /*
   * returns the map upon which this grammar is based
   * used for wrapping and extending, like with coneflower
   */
  public Map<FMetagon,FJigList> getMetagonJigsMap(){
    return metagonjigs;}
  
  public int getMetagonCount(){
    return metagonjigs.keySet().size();}
  
  public Iterator<FMetagon> getMetagonIterator(){
    return metagonjigs.keySet().iterator();}
  
  public List<FMetagon> getMetagons(){
    List<FMetagon> m=new ArrayList<FMetagon>(metagonjigs.keySet());
    return m;}
  
  public List<FJig> getJigs(FMetagon metagon){
    List<FJig> a=new ArrayList<FJig>(metagonjigs.get(metagon));
    return a;}
  
  public List<FJig> getJigs(FPolygon polygon){
    List<FJig> a=new ArrayList<FJig>(metagonjigs.get(polygon.metagon));
    return a;}

  public List<FJig> getJigs(KMetagon kmetagon){
    FMetagon fm=null;
    SEEK:for(FMetagon sm:metagonjigs.keySet())
      if(sm.equals(kmetagon)){
        fm=sm;
        break SEEK;}
    if(fm==null)return new ArrayList<FJig>(0);
    return getJigs(fm);}
  
  public List<FJig> getJigsAboveDetailSizeFloor(FPolygon target,double detailsizefloor){
    FJigList a=metagonjigs.get(target.metagon);
    List<FJig> b=a.getJigsAboveDetailSizeFloor(detailsizefloor);
     return b;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    StringBuffer a=new StringBuffer();
    a.append("\n\n");
    a.append("#########################\n");
    a.append("### FORSYTHIA GRAMMAR ###\n\n");
    a.append("metagoncount="+metagonjigs.keySet().size()+"\n\n");
    FJigList jiglist;
    for(FMetagon m:metagonjigs.keySet()){
      a.append("+++ METAGON +++\n");
      a.append(m.toString()+"\n");
      a.append("+++ JIGS +++\n");
      jiglist=metagonjigs.get(m);
      a.append("jigcount="+jiglist.size()+"\n");
      for(FJig jig:jiglist)
        a.append(jig.toString()+"\n");}
    a.append("### FORSYTHIA GRAMMAR ###\n");
    a.append("#########################\n");
    return a.toString();}
 
}

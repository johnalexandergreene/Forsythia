package org.fleen.forsythia.core.grammar.forsythiaGrammar_Basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.core.Forsythia;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.geom_Kisrhombille.KMetagon;

/*
 * A simple library of Metagons and Jigs
 * it is constructed with an even simpler library, a Map<FMetagon,? extends Collection<Jig>>
 * 
 * For each Metagon we have 0..n associated Jigs. 
 * Each Jig is associated with 1 Metagon
 */
public class ForsythiaGrammar_Basic_OLD implements ForsythiaGrammar,Serializable{
  
  private static final long serialVersionUID=3018836034565752313L;

  /*
   * ################################
   * CONSTRUCTOR
   * init with a map of metagons and jig collections
   * each metagon is unique
   * each metagon has an associated collection of unique jigs
   * ################################
   */
  
  public ForsythiaGrammar_Basic_OLD(Map<FMetagon,? extends Collection<Jig>> metagonjigs){
    Collection<Jig> c;
    for(FMetagon a:metagonjigs.keySet()){
      c=metagonjigs.get(a);
      this.metagonjigs.put(a,new JigList(c));}}
 
  /*
   * ################################
   * METAGONS AND JIGS
   * ################################
   */

  //for each metagon a jigset
  private Map<FMetagon,JigList> metagonjigs=new Hashtable<FMetagon,JigList>();
  
  /*
   * ++++++++++++++++++++++++++++++++
   * METAGONS (OPERANDS IN THE ABSTRACT)
   * ++++++++++++++++++++++++++++++++
   */

  public int getMetagonCount(){
    return metagonjigs.keySet().size();}
  
  public Iterator<FMetagon> getMetagonIterator(){
    return metagonjigs.keySet().iterator();}
  
  public List<FMetagon> getMetagons(){
    List<FMetagon> m=new ArrayList<FMetagon>(metagonjigs.keySet());
    return m;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * OPERATORS
   * TODO rename jig to operator for all of forsythia
   * reserve jig for maximilian, it has broader meaning. Forsythia is just "splitters". Just operators.
   * Forsythia is a pure parameterless shape grammar, unlike maximilian
   * ++++++++++++++++++++++++++++++++
   */
  
  public List<Jig> getJigs(FMetagon metagon){
    List<Jig> a=new ArrayList<Jig>(metagonjigs.get(metagon));
    return a;}
  
  public List<Jig> getJigs(FPolygon polygon){
    List<Jig> a=new ArrayList<Jig>(metagonjigs.get(polygon.metagon));
    return a;}

  public List<Jig> getJigs(KMetagon kmetagon){
    FMetagon fm=null;
    SEEK:for(FMetagon sm:metagonjigs.keySet())
      if(sm.equals(kmetagon)){
        fm=sm;
        break SEEK;}
    if(fm==null)return new ArrayList<Jig>(0);
    return getJigs(fm);}
  
  public List<Jig> getJigsAboveDetailSizeFloor(FPolygon target,double detailsizefloor){
    JigList a=metagonjigs.get(target.metagon);
    List<Jig> b=a.getJigsAboveDetailSizeFloor(target,detailsizefloor);
    return b;}
  
  public List<Jig> getJigsAboveDetailSizeFloorWithTags(FPolygon target,double detailsizefloor,String[] tags){
    JigList a=metagonjigs.get(target.metagon);
    List<Jig> b=a.getJigsAboveDetailSizeFloorWithTags(target,detailsizefloor,tags);
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
    a.append("metagoncount="+getMetagonCount()+"\n\n");
    JigList jiglist;
    for(FMetagon m:metagonjigs.keySet()){
      a.append("+++ METAGON +++\n");
      a.append(m.toString()+"\n");
      a.append("+++ JIGS +++\n");
      jiglist=metagonjigs.get(m);
      a.append("jigcount="+jiglist.size()+"\n");
      for(Jig jig:jiglist)
        a.append(jig.toString()+"\n");}
    a.append("### FORSYTHIA GRAMMAR ###\n");
    a.append("#########################\n");
    return a.toString();}

  @Override
  public Jig getRandomJig(FMetagon m,String[] tags,double detaillimit){
    // TODO Auto-generated method stub
    return null;
  }
 
}

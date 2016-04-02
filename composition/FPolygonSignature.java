package org.fleen.forsythia.composition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.grammar.FMetagon;


/*
 * Describes a polygon in a contexty way.
 * We consider the polygon's metagon and it's chorus index.
 * We consider those for it's ancestor polygon
 * and so on, all the way to the root.
 */
public class FPolygonSignature implements Serializable{
  
  private static final long serialVersionUID=2037581696842008653L;

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FPolygonSignature(FPolygon p){
    FPolygon node=p;
    while(node!=null){
      components.add(node.getSignatureComponent());
      node=node.getFirstAncestorPolygon();}}
  
  //empty sig
  public FPolygonSignature(){}
  
  /*
   * ################################
   * SIGNATURE DATA
   * ################################
   */
  
  private List<SigComponent> components=new ArrayList<SigComponent>();
  
  public static class SigComponent implements Serializable{
    
    private static final long serialVersionUID=1335327793096896698L;
    
    FMetagon metagon;
    int chorusindex;
    
    public SigComponent(FPolygon p){
      metagon=p.metagon;
      chorusindex=p.chorusindex;}
    
    public boolean equals(Object a){
      SigComponent b=(SigComponent)a;
      boolean e=
        b.chorusindex==chorusindex&&
        b.metagon.equals(metagon);
      return e;}}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  //hash code is summation of chorus indices
  public Integer hashcode=null;
  
  public int hashCode(){
    if(hashcode==null)initHashCode();
    return hashcode;}
  
  //sum of component chorus indices
  private void initHashCode(){
    hashcode=new Integer(0);
    for(SigComponent c:components)
      hashcode+=c.chorusindex;}
  
  public boolean equals(Object a){
    FPolygonSignature s0=(FPolygonSignature)a;
    if(s0.hashCode()!=hashCode())return false;
    int 
      c0=s0.components.size(),
      c1=components.size();
    if(c0!=c1)return false;
    SigComponent g0,g1;
    for(int i=0;i<c0;i++){
      g0=s0.components.get(i);
      g1=components.get(i);
      if(!g0.equals(g1))return false;}
    return true;}
  
  private String objectstring=null;
  
  public String toString(){
    if(objectstring==null)initObjectString();
    return objectstring;}
  
  private void initObjectString(){
    objectstring="BubbleSignature[";
    int s=components.size()-1;
    for(int i=0;i<s;i++)
      objectstring+=components.get(i)+",";
    objectstring+=components.get(s)+"]";}
  
}

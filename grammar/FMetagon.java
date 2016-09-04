package org.fleen.forsythia.grammar;

import java.io.Serializable;

import org.fleen.forsythia.Forsythia;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KMetagonVector;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.util.tag.TagManager;
import org.fleen.util.tag.Tagged;

/*
 * KMetagon that implements Serializable and Tagged.
 * Used in Forsythia process
 */
public class FMetagon extends KMetagon implements Serializable,Tagged,Forsythia{
  
  private static final long serialVersionUID=2763461150931052809L;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public FMetagon(double baseinterval,KMetagonVector[] vectors){
    super(baseinterval,vectors);}
  
  public FMetagon(KVertex... vertices){
    super(vertices);}
  
  public FMetagon(KPolygon polygon){
    super(polygon);}
  
  public FMetagon(KMetagon km,String[] tags){
    super(km);
    setTags(tags);}
  
  /*
   * ################################
   * TAGS
   * ################################
   */
  
  private TagManager tagmanager=new TagManager();
  
  public String[] getTags(){
    return tagmanager.getTags();}
  
  public void setTags(String[] tags){
    tagmanager.setTags(tags);}
  
  public boolean hasTag(String tag){
    return tagmanager.hasTag(tag);}
  
  public boolean hasTags(String... tags){
    return tagmanager.hasTags(tags);}
  
  /*
   * ################################
   * GENERAL PURPOSE OBJECT
   * ################################
   */
  
  public Object gpobject;
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    StringBuffer a=new StringBuffer();
    a.append("["+getClass().getSimpleName()+" ");
    a.append("tags="+tagmanager.toString()+" ");
    a.append("baseinterval="+baseinterval+" ");
    a.append("vectors=[");
    for(int i=0;i<vectors.length-1;i++)
      a.append(vectors[i].toString()+" ");
    a.append(vectors[vectors.length-1].toString()+"]]");
    return a.toString();}
  
  
  
  
}

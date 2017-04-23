package org.fleen.forsythia.core.grammar;

import java.io.Serializable;
import java.util.List;

import org.fleen.forsythia.core.Forsythia;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KMetagonVector;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KPoint;
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
  
  public FMetagon(KPoint... vertices){
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
  
  public void setTags(String... tags){
    tagmanager.setTags(tags);}
  
  public void setTags(List<String> tags){
    tagmanager.setTags(tags);}
  
  public List<String> getTags(){
    return tagmanager.getTags();}
  
  public boolean hasTags(String... tags){
    return tagmanager.hasTags(tags);}
  
  public boolean hasTags(List<String> tags){
    return tagmanager.hasTags(tags);}
  
  public void addTags(String... tags){
    tagmanager.addTags(tags);}
  
  public void addTags(List<String> tags){
    tagmanager.addTags(tags);}
  
  public void removeTags(String... tags){
    tagmanager.removeTags(tags);}
  
  public void removeTags(List<String> tags){
    tagmanager.removeTags(tags);}
  
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

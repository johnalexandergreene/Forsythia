package org.fleen.forsythia.app.grammarEditor.editor_Jig.model;

import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.RawGraph;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJigSection;
import org.fleen.forsythia.app.grammarEditor.project.metagon.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * for retouch we wrap the focus jig
 */
public class JigEditingModelForRetouch implements JigEditingModel{

  /*
   * ################################
   * JIGS
   * ################################
   */
  
  public void incrementGridDensity(){
    throw new IllegalArgumentException("not implemented");}
  
  public void decrementGridDensity(){
    throw new IllegalArgumentException("not implemented");}
  
  public int getGridDensity(){
    return GE.ge.focusjig.getGridDensity();}
  
  public RawGraph getRawGraph(){
    throw new IllegalArgumentException("not implemented");}
  
  public void setJigTags(String tags){
    GE.ge.focusjig.tags=tags;}

  public String getJigTags(){
    return GE.ge.focusjig.tags;}
  
  /*
   * ################################
   * SECTIONS
   * ################################
   */

  public int getSectionCount(){
    return GE.ge.focusjig.sections.size();}

  public List<KPolygon> getSectionPolygons(){
    List<KPolygon> p=new ArrayList<KPolygon>(GE.ge.focusjig.sections.size());
    for(ProjectJigSection s:GE.ge.focusjig.sections)
      p.add(s.metagon.kmetagon.getPolygon(s.getAnchor()));
    return p;}

  /*
   * ################################
   * SECTION CHORUS
   * ################################
   */
  
  public void incrementSectionChorus(int sectionindex){
    ProjectJigSection section=GE.ge.focusjig.sections.get(sectionindex);
    int chorusindex=section.getChorusIndex();
    chorusindex=getNextValidChorusIndex(section,chorusindex);
    section.setChorusIndex(chorusindex);}

  public int getSectionChorus(int sectionindex){
    return GE.ge.focusjig.sections.get(sectionindex).getChorusIndex();}
  
  public int getMaxSectionChorusIndex(){
    return GE.ge.focusjig.sections.size();}
  
  private ProjectMetagon getMetagonByChorusIndex(int chorusindex){
    for(ProjectJigSection section:GE.ge.focusjig.sections)
      if(section.getChorusIndex()==chorusindex)
        return section.metagon;
    return null;}
  
  /*
   * cycle through the range of chorus indices until one is found that is not 
   * presently in use by a section with a differing metagon
   */
  public int getNextValidChorusIndex(ProjectJigSection section,int chorusindex){
    boolean valid=false;
    ProjectMetagon test;
    int maxtries=getMaxSectionChorusIndex()*2,tries=0;
    while(!valid){
      //to avoid an infinite loop
      maxtries++;
      tries++;
      if(tries==maxtries)
        throw new IllegalArgumentException("infinite loop");
      //  
      chorusindex++;
      if(chorusindex>getMaxSectionChorusIndex())
        chorusindex=0;
      test=getMetagonByChorusIndex(chorusindex);
      if(test==null||test==section.metagon)
        valid=true;}
    return chorusindex;}
  
  /*
   * ################################
   * SECTION ANCHOR
   * ################################
   */
  
  public void incrementSectionAnchor(int sectionindex){
    ProjectJigSection s=GE.ge.focusjig.sections.get(sectionindex);
    s.incrementAnchorIndex();}

  
  public int getSectionAnchorIndex(int sectionindex){
    return GE.ge.focusjig.sections.get(sectionindex).anchorindex;}

  /*
   * ################################
   * SECTION TAGS
   * ################################
   */
  
  public void setSectionTags(int sectionindex,String tags){
    ProjectJigSection s=GE.ge.focusjig.sections.get(sectionindex);
    s.tags=tags;}

  public String getSectionTags(int sectionindex){
    return GE.ge.focusjig.sections.get(sectionindex).tags;}
  
}

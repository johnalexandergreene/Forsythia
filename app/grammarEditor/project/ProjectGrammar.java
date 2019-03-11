package org.fleen.forsythia.app.grammarEditor.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJigSection;
import org.fleen.forsythia.app.grammarEditor.project.metagon.ProjectMetagon;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.FJig;
import org.fleen.forsythia.core.grammar.FJigSection;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KMetagon;

/*
 * all stuff associated with editing a grammar
 * 
 * We hold a list of projectmetagons
 * each projectmetagon holds a list of projectprotojigs
 * each projectjig holds a list of projectprotojigsections
 *   
 */
public class ProjectGrammar implements Serializable{
  
  private static final long serialVersionUID=-8136928537083671983L;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //for create grammar
  //an empty grammar
  public ProjectGrammar(){}
  
  //for import
  //convert forsythia grammar to project grammar
  public ProjectGrammar(ForsythiaGrammar fg,String name){
    this.name=name;
    init(this,fg);}
  
  /*
   * ################################
   * NAME
   * ################################
   */
  
  public String name;
  
  /*
   * ################################
   * METAGONS
   * ################################
   */
  
  //projectmetagons ordered by hashcode. TODO something better
  public List<ProjectMetagon> metagons=new ArrayList<ProjectMetagon>();
  
  private class MetagonComparator implements Comparator<ProjectMetagon>,Serializable{
    
    private static final long serialVersionUID=-529984029519619386L;

    public int compare(ProjectMetagon pm0,ProjectMetagon pm1){
      int h0=pm0.hashCode(),h1=pm1.hashCode();
      if(h0==h1){
        return 0;
      }else if(h0<h1){
        return -1;}
      return 1;}};
  
  public ProjectMetagon getMetagon(int index){
    if(index<0||index>=metagons.size())return null;
    return metagons.get(index);}
  
  /*
   * get projectmetagon by kmetagon
   */
  public ProjectMetagon getMetagon(KMetagon kmetagon){
    for(ProjectMetagon pm:metagons)
      if(pm.kmetagon.equals(kmetagon))
        return pm;
    return null;}
  
  /*
   * get projectmetagon by projectprotojig
   * returns the projectmetagon that owns the specified projectprotojig
   * returns null on fail
   */
  public ProjectMetagon getMetagon(ProjectJig j){
    for(ProjectMetagon m:metagons)
      for(ProjectJig ppj:m.jigs)
        if(ppj==j)
          return m;
    return null;}
  
  //returns true if a ProjectMetagon with the specified geometry is already contained within this grammar
  public boolean containsMetagon(KMetagon metagon){
    for(ProjectMetagon m:metagons)
      if(m.kmetagon.equals(metagon))
        return true;
    return false;}
  
  /*
   * Add a new metagon
   * resort the metagon list
   */
  public void addMetagon(ProjectMetagon m){
    //fail if specified metagon is null or dupe
    if(m==null||metagons.contains(m))return;
    metagons.add(m);
    Collections.sort(metagons,new MetagonComparator());}
  
  public void addMetagons(Collection<ProjectMetagon> m){
    for(ProjectMetagon pm:m)
      addMetagon(pm);}
  
  /*
   * remove the metagon from the metagons list
   * this also discards the metagon's associated jigs
   * discard, for all metagons, any jig that contains this metagon
   * TODO a user warning 
   */
  public void discardMetagon(ProjectMetagon m){
    if(m==null)return;
    metagons.remove(m);
    Iterator<ProjectJig> i;
    ProjectJig pj;
    for(ProjectMetagon pm:metagons){
      i=pm.jigs.iterator();
      while(i.hasNext()){
        pj=i.next();
        if(pj.usesInSection(m))i.remove();}}}
  
  public void discardMetagons(List<ProjectMetagon> pms){
    for(ProjectMetagon a:pms)
      discardMetagon(a);}
  
  public int getMetagonCount(){
    return metagons.size();}
  
  public Iterator<ProjectMetagon> getMetagonIterator(){
    return metagons.iterator();}
  
  public int getIndex(ProjectMetagon m){
    if(m==null||metagons.isEmpty())return -1;
    return metagons.indexOf(m);}
  
  public boolean hasMetagons(){
    return !metagons.isEmpty();}
  
  /*
   * ################################
   * INFO
   * ################################
   */
  
  public int getIsolatedMetagonsCount(){
    if(metagons.isEmpty())return 0;
    int c=0;
    for(ProjectMetagon m:metagons)
      if(m.isIsolated())c++;
    return c;}
  
  public int getJiglessMetagonsCount(){
    if(metagons.isEmpty())return 0;
    int c=0;
    for(ProjectMetagon m:metagons)
      if(m.isJigless())c++;
    return c;}
  
  public String getMetagonInfo(){
    return 
      "MET:"+getMetagonCount()+
      " ISO:"+getIsolatedMetagonsCount()+
      " JIGLESS:"+getJiglessMetagonsCount();}
  
  /*
   * ################################
   * INIT THIS PROJECT GRAMMAR WITH A FORSYTHIA GRAMMAR
   * Used for Forsythia Grammar import
   * ################################
   */
  
  /*
   * given an empty projectgrammar and a (assumedly nonempty) forsythiagrammar
   * convert forsythia grammar elements to project grammar elements and fill project grammar.
   */
  private static final void init(ProjectGrammar projectgrammar,ForsythiaGrammar forsythiagrammar){
    GE.ge.focusgrammar=projectgrammar;
    if(forsythiagrammar==null)return;
    //convert all of the metagons in the forsythia grammar into 
    //projectmetagons and put them in a set (to cull dupes)
    Set<ProjectMetagon> projectmetagons=new HashSet<ProjectMetagon>();
    Iterator<FMetagon> i=forsythiagrammar.getMetagonIterator();
    while(i.hasNext()){
      projectmetagons.add(new ProjectMetagon(projectgrammar,i.next()));}
    //put those projectmetagons in our projectgrammar
    projectgrammar.addMetagons(projectmetagons);
    //for every projectmetagon in the project grammar, 
    //get each of its associated jigs from the forsythia grammar and convert it
    ProjectJig projectjig;
    List<FJig> jigs;
    for(ProjectMetagon projectmetagon:projectgrammar.metagons){
      jigs=forsythiagrammar.getJigs(projectmetagon.kmetagon);
      for(FJig jig:jigs){
        projectjig=new ProjectJig(projectmetagon,jig);
        projectmetagon.addJig(projectjig);}}}
  
  /*
   * ################################
   * DERIVE FORSYTHIA GRAMMAR FROM THIS PROJECT GRAMMAR
   * Used for export and in generator
   * ################################
   */
  
  public ForsythiaGrammar getForsythiaGrammar(){
    //create fmetagons.
    Map<KMetagon,FMetagon> fmetagonbykmetagon=new Hashtable<KMetagon,FMetagon>();
    FMetagon fm;
    for(ProjectMetagon pm:metagons){
      fm=new FMetagon(pm.kmetagon,pm.getTagsForExport());
      fmetagonbykmetagon.put(pm.kmetagon,fm);}
    //create jigs
    Map<FMetagon,List<FJig>> metagonjigs=new Hashtable<FMetagon,List<FJig>>();
    List<FJig> jigs;
    for(ProjectMetagon pm:metagons){
      jigs=getJigs(pm,fmetagonbykmetagon);
      fm=fmetagonbykmetagon.get(pm.kmetagon);
      metagonjigs.put(fm,jigs);}
    //
    ForsythiaGrammar fg=new ForsythiaGrammar(metagonjigs);
    //DEBUG
//    System.out.println("########################");
//    System.out.println("### CREATED FGRAMMAR ###");
//    System.out.println(fg);
//    System.out.println("########################");
    //
    return fg;}
  
  //convert all projectjigs to jigs for specified projectmetagon
  //return the jigs in a list
  private List<FJig> getJigs(ProjectMetagon pm,Map<KMetagon,FMetagon> fmetagonbykmetagon){
    List<FJig> jigs=new ArrayList<FJig>();
    FJig jig;
    int jiggriddensity;
    List<FJigSection> jigsections;
    String[] jigtags;
    for(ProjectJig projectjig:pm.jigs){
      jiggriddensity=projectjig.griddensity;
      jigsections=getJigSections(projectjig,fmetagonbykmetagon);
      jigtags=projectjig.getTagsForExport();
      jig=new FJig(jiggriddensity,jigsections,jigtags);
      jigs.add(jig);}
    return jigs;}
  
  private List<FJigSection> getJigSections(ProjectJig projectjig,Map<KMetagon,FMetagon> fmetagonbykmetagon){
    List<FJigSection> jigsections=new ArrayList<FJigSection>();
    jigsections.addAll(getPolygonJigSections(projectjig,fmetagonbykmetagon));
    return jigsections;}
  
  private List<FJigSection> getPolygonJigSections(ProjectJig projectjig,Map<KMetagon,FMetagon> fmetagonbykmetagon){
    List<FJigSection> jigsections=new ArrayList<FJigSection>();
    FJigSection js;
    FMetagon jsproductmetagon;
    int jsproductchorusindex;
    KAnchor jsproductanchor;
    String[] jstags;
    for(ProjectJigSection projectjigsection:projectjig.sections){
      jsproductchorusindex=projectjigsection.chorusindex;
      jsproductmetagon=fmetagonbykmetagon.get(projectjigsection.metagon.kmetagon);
      jsproductanchor=projectjigsection.getAnchor();
      jstags=projectjigsection.getTagsForExport();
      js=new FJigSection(
        jsproductmetagon,jsproductanchor,
        jsproductchorusindex,jstags);
      jigsections.add(js);}
    return jigsections;}
  
}

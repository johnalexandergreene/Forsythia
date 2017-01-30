package org.fleen.forsythia.app.grammarEditor.sampleGrammars;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.project.ProjectGrammar;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

public class SampleGrammars{
  
  private static final String[] GRAMMARNAMES={
    "triangley.grammar",
    "triangley2.grammar",
    "boxy.grammar",
    "mixy.grammar"};
  
  private static List<ProjectGrammar> grammars;
  
  private static ProjectGrammar loadGrammar(String name){
    ProjectGrammar g=null;  
    try{
      InputStream a=SampleGrammars.class.getResourceAsStream(name);
      ObjectInputStream b=new ObjectInputStream(a);
      g=new ProjectGrammar((ForsythiaGrammar)b.readObject(),null);
      b.close();
    }catch(Exception e){
      System.out.println("Load sample grammar failed.");
      e.printStackTrace();}
    return g;}
  
  static{
    grammars=new ArrayList<ProjectGrammar>(GRAMMARNAMES.length);
    for(String name:GRAMMARNAMES)
      grammars.add(loadGrammar(name));}
  
  public static final List<ProjectGrammar> getGrammars(){
    return grammars;}

  public static ProjectGrammar getPreferredGrammar(){
//    Random r=new Random();
    ProjectGrammar g=grammars.get(3);
    return g;}

}

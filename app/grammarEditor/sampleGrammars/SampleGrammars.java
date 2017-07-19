package org.fleen.forsythia.app.grammarEditor.sampleGrammars;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.ProjectGrammar;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

public class SampleGrammars implements Serializable{
  
  private static final long serialVersionUID=-5614469697980598416L;
  
  /*
   * these are the names of our serialized sample grammars in the samplegrammars package
   * yes, we don't just get a list. That's difficult apparently.
   */
  private static final String[] GRAMMARNAMES={
    "nice.grammar",
    "boxy.grammar",
    "triangley.grammar",
    "fancy.grammar"};
  
  /*
   * load each of our sample grammars from resource
   * export to local directory
   * import the nicest one
   */
  public void init(){
    for(String name:GRAMMARNAMES)
      loadAndExportResourceGrammar(name);
    File path=GE.getLocalDir();
    path=new File(path.getAbsolutePath()+"/"+GRAMMARNAMES[0]);//nice1.grammar
    GE.ge.grammarimportexport.importGrammar(path);}
  
  private ForsythiaGrammar loadAndExportResourceGrammar(String name){
    ForsythiaGrammar g=null;
    //load it from resource
    try{
      InputStream a=SampleGrammars.class.getResourceAsStream(name);
      ObjectInputStream b=new ObjectInputStream(a);
      g=(ForsythiaGrammar)b.readObject();
      b.close();
    }catch(Exception e){
      System.out.println("Load sample grammar failed.");
      e.printStackTrace();}
    //create project grammar and export it to local dir
    ProjectGrammar pg=new ProjectGrammar(g,name);
    File path=GE.getLocalDir();
    path=new File(path.getAbsolutePath()+"/"+pg.name);
    GE.ge.grammarimportexport.exportGrammar(pg,path);
    return g;}

}

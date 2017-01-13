package org.fleen.forsythia.app.grammarEditor;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.io.File;
import java.net.URLDecoder;

import org.fleen.forsythia.app.grammarEditor.editor_CreateMetagon.Editor_CreateMetagon;
import org.fleen.forsythia.app.grammarEditor.editor_EditGrammar.Editor_Grammar;
import org.fleen.forsythia.app.grammarEditor.editor_Generator.Editor_Generator;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.Editor_Jig;
import org.fleen.forsythia.app.grammarEditor.editor_ViewMetagonEditTags.Editor_EditMetagonDetails;
import org.fleen.forsythia.app.grammarEditor.project.ProjectGrammar;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.forsythia.app.grammarEditor.util.Editor;

/*
 * FLEEN QUASAR
 * Create and edit shape grammars for use in Forsythia production process
 * Sample the product of a simple production process based on working grammar.
 * 
 * This is the main class
 * references to main objects
 * init and term 
 */
public class GE{
  
  public static final String APPNAME="Fleen Forsythia Grammar Editor 0.2A";
  
  /*
   * ################################
   * MAIN OBJECTS
   * ################################
   */
  
  //this flag is true after init, false just before terminate
  public static boolean runmain=false;
  //config object for this app 
  public static GEConfig config;
  //main ui. A frame. Holds editor uis
  public static UIMain uimain;
  //EDITORS
  //each has an associated UI for interacting with the thing it edits
  //We show the UIs one at a time, cardlayoutwise, on the main window
  public static Editor[] editors;
  public static Editor presenteditor=null;
  public static Editor_Generator editor_generator;
  public static Editor_Grammar editor_grammar;
  public static Editor_CreateMetagon editor_createmetagon;
  public static Editor_EditMetagonDetails editor_editmetagondetails;
  public static Editor_Jig editor_jig;
  //FOCUS GRAMMAR ELEMENTS
  //these are the grammar elements that we are focussing upon at any particular moment
  public static ProjectGrammar focusgrammar=null;
  public static ProjectMetagon focusmetagon=null;
  public static ProjectJig focusjig=null;
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  public static final void main(String[] a){
    System.out.println("#### Q INIT ####");
    //init main ui, subeditors and their associated uis
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          uimain=new UIMain();
          initEditors();
          runmain=true;
        }catch(Exception e){
          e.printStackTrace();}}});
    //wait a sec for the ui to finish initing
    while(!runmain){
     try{ 
      Thread.sleep(1000,0);
     }catch(Exception x){
       x.printStackTrace();}}
    //init everything else
//    tasksequencer=new TaskSequencer();
    GEConfig.load();
    //open an editor
    setEditor(editor_generator);
    editor_generator.startForQInit();}
  
  /*
   * ################################
   * TERM
   * ################################
   */
  
  public static final void term(){
    //wait for everything to finish up
    while(!clearForTerm()){
      try{ 
        Thread.sleep(200,0);
       }catch(Exception x){
         x.printStackTrace();}}
    //TODO get rid of the task sequencer. it's only used by the resize thingy
    runmain=false;
//    tasksequencer.term();
    GEConfig.save();
    System.out.println("#### Q TERM ####");
    System.exit(0);}
  
  /*
   * TODO
   * test for 
   *   generator.idle
   *   exporter.idle
   *   qconfig.saved
   */
  private static final boolean clearForTerm(){
     return true;
      
  }
  
  /*
   * ################################
   * EDITOR CONTROL
   * init editors, switch between editors
   * ################################
   */
  
  private static final void initEditors(){
    editor_generator=new Editor_Generator();
    editor_grammar=new Editor_Grammar();
    editor_createmetagon=new Editor_CreateMetagon();
    editor_editmetagondetails=new Editor_EditMetagonDetails();
    editor_jig=new Editor_Jig();
    editors=new Editor[]{
      editor_generator,
      editor_grammar,
      editor_createmetagon,
      editor_editmetagondetails,
      editor_jig};
    //init dialog associated uis
    for(Editor a:editors)
      uimain.paneditor.add(a.getUI(),a.getName());}
  
  public static final void setEditor(final Editor editor){
    if(presenteditor!=null)presenteditor.close();
      presenteditor=editor;
      CardLayout a=(CardLayout)uimain.paneditor.getLayout();
      String n=editor.getName();
      a.show(uimain.paneditor,n);
      GE.uimain.setTitle(GE.APPNAME+" :: "+n);
      presenteditor.open();}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  public static final File getLocalDir(){
    String path=GE.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedpath;
    try{
      decodedpath=URLDecoder.decode(path,"UTF-8");
    }catch(Exception x){
      throw new IllegalArgumentException(x);}
    File f=new File(decodedpath);
    if(!f.isDirectory())f=f.getParentFile();
    return f;}
  
}

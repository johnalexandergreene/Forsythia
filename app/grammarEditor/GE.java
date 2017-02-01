package org.fleen.forsythia.app.grammarEditor;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URLDecoder;

import org.fleen.forsythia.app.grammarEditor.editor_Generator.Editor_Generator;
import org.fleen.forsythia.app.grammarEditor.editor_Grammar.Editor_Grammar;
import org.fleen.forsythia.app.grammarEditor.editor_Grammar.GrammarImportExport;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.Editor_Jig;
import org.fleen.forsythia.app.grammarEditor.editor_Metagon.Editor_Metagon;
import org.fleen.forsythia.app.grammarEditor.project.ProjectGrammar;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.metagon.ProjectMetagon;
import org.fleen.forsythia.app.grammarEditor.sampleGrammars.SampleGrammars;
import org.fleen.forsythia.app.grammarEditor.util.Editor;

/*
 * ################################
 * ################################
 * ################################
 * 
 * FLEEN FORSYTHIA GRAMMAR EDITOR
 * Create and edit grammars for use in Forsythia production process
 * Generate sample Forsythia compostions
 * 
 * The app is an instance of GE
 * This class is the main class
 * It contains 
 *   references to subsystems
 *   methods for init and term
 *   utilities
 * We serialize the instance of this at exit, so that's our config
 * 
 * ################################
 * ################################
 * ################################
 */
public class GE implements Serializable{
  
  private static final long serialVersionUID=4971596440965502787L;

  public static final String APPNAME="FFGE20170131";
  
  public static final String ABOUT=
    "Fleen Forsythia Grammar Editor Version 2017_01_31\n"+
    "\n"+
    "Create shape grammars for use in Forsythia geometry production processes.\n"+
    "Sample compositions based on grammars.\n"+
    "Export pretty images.\n"+
    "\n"+
    "Author : John Greene\n"+
    "Project : fleen.org\n"+
    "\n"+
    "This program is free software: you can redistribute it and/or modify\n"+
    "it under the terms of the GNU General Public License as published by\n"+
    "the Free Software Foundation, either version 3 of the License, or\n"+
    "(at your option) any later version.\n"+
    "\n"+
    "This program is distributed in the hope that it will be useful,\n"+
    "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"+
    "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"+
    "GNU General Public License for more details.\n"+
    "\n"+
    "You should have received a copy of the GNU General Public License\n"+
    "along with this program.  If not, see <http://www.gnu.org/licenses/>.";
  
  /*
   * ################################
   * INIT
   * ################################
   */
 
  private void init(){
    initFocusGrammar();
    initUI();}
  
  /*
   * ################################
   * UI
   * ################################
   */

  //main ui. A frame. Holds editor uis
  public UIMain uimain;
  
  private boolean uiinitialized;
  
  private void initUI(){
    System.out.println("#### Q INIT ####");
    //init ui
    uiinitialized=false;
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          uimain=new UIMain();
          editor_grammar=new Editor_Grammar();
          editor_metagon=new Editor_Metagon();
          editor_jig=new Editor_Jig();
          editor_generator=new Editor_Generator();
          editors=new Editor[]{editor_grammar,editor_metagon,editor_jig,editor_generator};
          for(Editor a:editors)
            uimain.paneditor.add(a.getUI(),a.getName());
          uiinitialized=true;
        }catch(Exception e){
          e.printStackTrace();}}});
    //wait a sec for the ui to finish initing
    while(!uiinitialized){
     try{ 
       Thread.sleep(1000,0);
     }catch(Exception x){
       x.printStackTrace();}}
    //open an editor
    setEditor(editor_grammar);}
  
  /*
   * ################################
   * EDITORS
   * One editor in use at a time
   * One editor for each aspect of GE
   * We show the Editor UIs one at a time, cardlayoutwise, in uimain
   * ################################
   */

  public Editor presenteditor=null;
  public Editor_Grammar editor_grammar;
  public Editor_Metagon editor_metagon;
  public Editor_Jig editor_jig;
  public Editor_Generator editor_generator;
  public Editor[] editors;
  
  public void setEditor(final Editor editor){
    if(presenteditor!=null)presenteditor.close();
      presenteditor=editor;
      CardLayout a=(CardLayout)uimain.paneditor.getLayout();
      String n=editor.getName();
      a.show(uimain.paneditor,n);
      uimain.setTitle(GE.APPNAME+" :: "+n);
      presenteditor.open();}
  
  /*
   * ################################
   * GRAMMAR ELEMENTS
   * The focus stuff is the grammar elements that we are focussing upon at any particular moment
   *   they are basically ez to edit versions of the Forsythia classes + misc editor-related services
   * grammar is never null
   * metagon and jig might be null
   * ################################
   */
  
  public ProjectGrammar focusgrammar=null;
  public ProjectMetagon focusmetagon=null;
  public ProjectJig focusjig=null;
  public GrammarImportExport grammarimportexport=new GrammarImportExport();
  
  /*
   * the focus grammar will be null at first init, thereafter it will never be null
   * if it's null then we init samplegrammars and get one
   *   that means 
   *     load them from resource, 
   *     export them to the local directory 
   *     import one. That's our focus grammar 
   */
  private void initFocusGrammar(){
    if(focusgrammar==null){
      SampleGrammars sg=new SampleGrammars();
      sg.init();}}
  
  /*
   * ################################
   * TERMINATE APP
   * write serialized instance of this class to local dir then exit
   * ################################
   */
  
  public void term(){
    System.out.println("GE TERMINATE");
    saveInstance(this);
    System.exit(0);}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  static final String GEINSTANCEFILENAME="GE.instance";
  
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
  
  //load instance of this class from the local dir
  private static final GE loadInstance(){
    String pathtoconfig=GE.getLocalDir().getPath()+"/"+GEINSTANCEFILENAME;
    System.out.println("Loading instance : "+pathtoconfig);
    GE instance=null;
    try{
      FileInputStream a=new FileInputStream(pathtoconfig);
      ObjectInputStream b=new ObjectInputStream(a);
      instance=(GE)b.readObject();
      b.close();
    }catch(Exception e){
      System.out.println("Load instance failed.");}
    return instance;}
  
  //save instance of this class to local dir
  private static final void saveInstance(GE instance){
    String pathtoconfig=GE.getLocalDir().getPath()+"/"+GEINSTANCEFILENAME;
    System.out.println("saving instance : "+pathtoconfig);
    FileOutputStream fos;
    ObjectOutputStream oos;
    File file=new File(pathtoconfig);
    try{
      fos=new FileOutputStream(file);
      oos=new ObjectOutputStream(fos);
      oos.writeObject(instance);
      oos.close();
    }catch(IOException x){
      System.out.println("Save instance failed.");
      x.printStackTrace();}}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static GE ge;
  
  /*
   * get local dir
   * load serialized instance of GE
   * if serialized instance fails to load then create a new one
   */
  public static final void main(String[] a){
    ge=loadInstance();
    if(ge==null){
      System.out.println("constructing instance of GE");
      ge=new GE();
      ge.init();}}
  
}

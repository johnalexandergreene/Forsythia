package org.fleen.forsythia.app.grammarEditor.util;

import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;



/*
 * A viewer and/or manipulator of something
 */
public abstract class Editor{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  public Editor(String name){
    this.name=name;
    ui=createUI();
    ui.addHierarchyBoundsListener(hbl0);}

  /*
   * ################################
   * UI, NAME AND BASIC INTERFACE
   * ################################
   */
  
  private String name;
  private JPanel ui;
  private boolean isopen=false;
  
  public String getName(){
    return name;}
  
  public boolean isOpen(){
    return isopen;}
  
  public JPanel getUI(){
    return ui;}
  
  protected abstract JPanel createUI();
  
  public void open(){
    ui.setVisible(true);
    configureForOpen();
    isopen=true;}
  
  public void close(){
    ui.setVisible(false);
    configureForClose();
    isopen=false;}
  
  protected abstract void configureForOpen();
  
  protected abstract void configureForClose();
  
  public abstract void refreshAll();
  
  /*
   * ################################
   * REFRESH ALL ON RESIZE 
   * ################################
   */
  
  private static final long RESIZE_REFRESH_DELAY=300; 
  @SuppressWarnings("rawtypes")
  ScheduledFuture scheduledresizerefresh=null;
  
  /*
   * on resize event
   * if a refreshall is scheduled then cancel it
   * schedule a refreshall at refreshdelay time in the future 
   */
  private HierarchyBoundsListener hbl0=new HierarchyBoundsListener(){

    public void ancestorMoved(HierarchyEvent e){}
    
    public void ancestorResized(HierarchyEvent e){
      if(isOpen()){
        if(scheduledresizerefresh!=null)
          scheduledresizerefresh.cancel(false);
        scheduledresizerefresh=TaskSequencer.SCHEDULEDEXECUTOR.schedule(
          new ScheduleResizeRefresh(),RESIZE_REFRESH_DELAY,TimeUnit.MILLISECONDS);}}};
    
  private class ScheduleResizeRefresh extends Thread{
    public void run(){
      refreshAll();
      scheduledresizerefresh=null;}}
    
}

package org.fleen.forsythia.app.grammarEditor.util;

import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;



/*
 * A viewer and/or manipulator of something
 */
public abstract class Editor implements Serializable{
  
  private static final long serialVersionUID=1108647719698212710L;

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  public Editor(String name){
    this.name=name;}

  /*
   * ################################
   * UI, NAME AND BASIC INTERFACE
   * ################################
   */
  
  private String name;
  transient private JPanel ui;
  private boolean isopen=false;
  
  public String getName(){
    return name;}
  
  public boolean isOpen(){
    return isopen;}
  
  public JPanel getUI(){
    if(ui==null){
      ui=createUI();
      ui.addHierarchyBoundsListener(new HBL0());}
    return ui;}
  
  protected abstract JPanel createUI();
  
  public void open(){
    getUI().setVisible(true);
    configureForOpen();
    isopen=true;}
  
  public void close(){
    getUI().setVisible(false);
    configureForClose();
    isopen=false;}
  
  protected abstract void configureForOpen();
  
  protected abstract void configureForClose();
  
  /*
   * refresh all ui components
   */
  public abstract void refreshUI();
  
  /*
   * ################################
   * REFRESH ALL ON RESIZE 
   * ################################
   */
  
  private static final long RESIZE_REFRESH_DELAY=300; 
  private static final ScheduledExecutorService SCHEDULEDEXECUTOR=Executors.newSingleThreadScheduledExecutor();
  @SuppressWarnings("rawtypes")
  ScheduledFuture scheduledresizerefresh=null;
  
  /*
   * on resize event
   * if a refreshall is scheduled then cancel it
   * schedule a refreshall at refreshdelay time in the future 
   */  
  private class HBL0 implements HierarchyBoundsListener,Serializable{
    
    private static final long serialVersionUID=6250653239681551733L;

    public void ancestorMoved(HierarchyEvent e){}
    
    public void ancestorResized(HierarchyEvent e){
      if(isOpen()){
        if(scheduledresizerefresh!=null)
          scheduledresizerefresh.cancel(false);
        scheduledresizerefresh=SCHEDULEDEXECUTOR.schedule(
          new ScheduleResizeRefresh(),RESIZE_REFRESH_DELAY,TimeUnit.MILLISECONDS);}}}
    
  private class ScheduleResizeRefresh extends Thread implements Serializable{
 
    private static final long serialVersionUID=-7548194239693359381L;

    public void run(){
      refreshUI();
      scheduledresizerefresh=null;}}
    
}

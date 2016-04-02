package org.fleen.forsythia.app.grammarEditor.util;


/*
 * Tasks get put in the command queue
 * get handled in the order that they are received
 * never run simultaneously
 * Task queue is emptied before this application exits
 * Subclasses of Task get params via their constructor.
 */
public abstract class Task{
  
  private Thread taskthread=new Thread(){
    public void run(){
      TaskSequencer.taskisrunning=true;
      doTask();
      TaskSequencer.taskisrunning=false;}};
  
  public void startTask(){
    taskthread.start();}
  
  public abstract void doTask();
  
}

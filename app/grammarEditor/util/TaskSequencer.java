package org.fleen.forsythia.app.grammarEditor.util;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fleen.forsythia.app.grammarEditor.GE;



/*
 * We have multiple threads in this application. 
 * In these threads some processes risk happening out of sequence
 * So we force sequence them
 * 
 * We periodically 
 *    check to see if a task is running 
 *      If no task is running then we check the queue to see if it contains any Tasks
 *        If the queue has 1 or more Tasks in it then we remove the one on top and 
 *        invoke it's run method. Now we have a task running.
 *        
 * We randomly add new Tasks to the queue
 * 
 * we use this whole TaskQueue system to make sure that our tasks get 
 * run in the order that they are invoked, sequentially and nonsimultaneously
 */
public class TaskSequencer{
  
  //the task scheduler uses this, so do a couple other things.
  public static final ScheduledExecutorService SCHEDULEDEXECUTOR=Executors.newSingleThreadScheduledExecutor();
  private static final long 
    TASK_CHECKER_INIT_DELAY=500,
    CHECKER_PERIODIC_DELAY=20;
  private static Task task;
  private static Deque<Task> tasks=new LinkedList<Task>();
  static boolean taskisrunning=false;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public TaskSequencer(){
    init();}
  
  /*
   * ################################
   * INIT AND TERM
   * ################################
   */
  
  public void init(){
    SCHEDULEDEXECUTOR.scheduleWithFixedDelay(
      new TaskMonitor(),
      TASK_CHECKER_INIT_DELAY,
      CHECKER_PERIODIC_DELAY, 
      TimeUnit.MILLISECONDS);}
  
  public void term(){
    System.out.println("exiting");
    //flush task queue
//    while(!isEmpty()){};
    //carefully shut down scheduled executor
    try{
      SCHEDULEDEXECUTOR.shutdown();
    }catch(Exception x){
      x.printStackTrace();
      System.exit(1);}}
  
  /*
   * ################################
   * TASK CONTROL
   * ################################
   */
  
  //stick a new command on the end of the queue
  public void add(Task t){
    if(GE.runmain)tasks.addLast(t);}
  
  //do we have any commands in the queue?
  public boolean isEmpty(){
    return tasks.isEmpty();}
  
  private static class TaskMonitor extends Thread{
    public void run(){
      if((!taskisrunning)&&(!tasks.isEmpty())){
        task=tasks.removeFirst();
        try{
          task.startTask();
        }catch(Throwable e){
          e.printStackTrace();}}}}

}

package org.fleen.forsythia.app.grammarEditor.generator;

//data for engine progress report event
public class GProgress{
  
  public GProgress(String description,double progress){
    this.description=description;
    this.progress=progress;}
  
  public String description;
  public double progress=0.5;//0..1
   
}

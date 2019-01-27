package org.fleen.forsythia.app.spinner.spinnerVideoGenerators.spinner0000;

import java.awt.image.BufferedImage;
import java.io.File;

import org.fleen.forsythia.app.spinner.core.Spinner;
import org.fleen.forsythia.app.spinner.core.SpinnerObserver;
import org.fleen.forsythia.app.spinner.core.Stripe;
import org.fleen.forsythia.app.spinner.spinnerVideoGenerators.UI;

public class Main{
  
  public static final int 
    VIEWPORTWIDTH=1080,
    VIEWPORTHEIGHT=720,
    FLOWDIR=Spinner.FLOWDIR_NORTH,
    ROUGHLENGTH=3000;//72000 is 20min
  
  private static UI ui;
  
  private static final String WORKINGDIR="/home/john/Desktop/spinnerexport"; 
  
  private static SpinnerObserver observer=new SpinnerObserver(){
    public void createdFrame(BufferedImage image){
      ui.viewer.image=image;
      ui.repaint();}};
    
  private static Stripe getHeaderStripe(){
    return null;}
  
  public static final void main(String[] a){
    Spinner s=new Spinner(
      VIEWPORTWIDTH,VIEWPORTHEIGHT,
      FLOWDIR,
      ROUGHLENGTH,
      getHeaderStripe(),
      new SG_ColoredBoxes(),
      new Renderer0(),
      new File(WORKINGDIR),
      observer);
    ui=new UI();
    s.run();}

}

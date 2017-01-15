package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.fleen.forsythia.app.grammarEditor.util.UI;

public class CursorX{

  private static final String NAME="gridxcursor";
  private static BufferedImage image=null;
  
  private static final int
    WHITE=-1,
    TRANSPARENT=new Color(0,0,0,0).getRGB();
  
  static{
    try{
      image=ImageIO.read(CursorX.class.getResource("cursorx.png"));    
    }catch(Exception x){
      x.printStackTrace();}
    //convert from rgb to argb
    BufferedImage image2=new BufferedImage(UI.GRID_CURSORXSIZE,UI.GRID_CURSORXSIZE,BufferedImage.TYPE_INT_ARGB);
    image2.createGraphics().drawImage(image,null,null);
    image=image2;
    //convert black and white to white and transparent
    int rgb;
    for(int x=0;x<UI.GRID_CURSORXSIZE;x++){
      for(int y=0;y<UI.GRID_CURSORXSIZE;y++){
        rgb=image.getRGB(x,y);
        if(rgb==WHITE){
          image.setRGB(x,y,TRANSPARENT);
        }else{
          if((x+y)%2==0){
            image.setRGB(x,y,TRANSPARENT);
          }else{
            image.setRGB(x,y,WHITE);}}}}}
  
  static void setCursor(Grid grid){
    Toolkit tk=Toolkit.getDefaultToolkit();
    Dimension cdim=tk.getBestCursorSize(UI.GRID_CURSORXSIZE,UI.GRID_CURSORXSIZE);
    Point hotspot=new Point(cdim.width/2,cdim.height/2);
    Cursor c=tk.createCustomCursor(image,hotspot,NAME);
    grid.setCursor(c);}

}

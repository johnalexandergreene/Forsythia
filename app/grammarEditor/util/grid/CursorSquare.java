package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.app.grammarEditor.util.UI;

public class CursorSquare{

  private static final String NAME="gridsquarecursor";
  private static final double CURSOR_INNER_RADIUS=0.15;
  private static BufferedImage image=null;
  
  static{
    image=new BufferedImage(UI.GRID_CURSORSQUARESIZE,UI.GRID_CURSORSQUARESIZE,BufferedImage.TYPE_INT_ARGB);
    int 
      is0=(int)((0.5-CURSOR_INNER_RADIUS)*UI.GRID_CURSORSQUARESIZE),
      is1=(int)((0.5+CURSOR_INNER_RADIUS)*UI.GRID_CURSORSQUARESIZE),
      rgb=new Color(255,255,255,255).getRGB();
    for(int x=0;x<UI.GRID_CURSORSQUARESIZE;x++){
      for(int y=0;y<UI.GRID_CURSORSQUARESIZE;y++){
        if((x<is0||x>is1||y<is0||y>is1)&&(x+y)%2==0)image.setRGB(x,y,rgb);}}}
  
  static void setCursor(Grid grid){
    Toolkit tk=Toolkit.getDefaultToolkit();
    Dimension cdim=tk.getBestCursorSize(UI.GRID_CURSORSQUARESIZE,UI.GRID_CURSORSQUARESIZE);
    Point hotspot=new Point(cdim.width/2,cdim.height/2);
    Cursor c=tk.createCustomCursor(image,hotspot,NAME);
    grid.setCursor(c);}

}

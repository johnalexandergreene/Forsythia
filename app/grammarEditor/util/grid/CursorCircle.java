package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.app.grammarEditor.util.UI;

public class CursorCircle{

  private static final String NAME="gridroundcursor";
  private static final double 
    CURSOR_INNER_RADIUS=0.15,
    CURSOR_OUTER_RADIUS=0.48;
  private static BufferedImage image=null;
  
  static{
    image=new BufferedImage(UI.GRID_CURSORCIRCLESIZE,UI.GRID_CURSORCIRCLESIZE,BufferedImage.TYPE_INT_ARGB);
    int d,dx,dy,rgb=new Color(255,255,255,255).getRGB(),
      ri=(int)(UI.GRID_CURSORCIRCLESIZE*CURSOR_INNER_RADIUS),
      ro=(int)(UI.GRID_CURSORCIRCLESIZE*CURSOR_OUTER_RADIUS);
    for(int x=0;x<UI.GRID_CURSORCIRCLESIZE;x++){
      for(int y=0;y<UI.GRID_CURSORCIRCLESIZE;y++){
        dx=x-UI.GRID_CURSORCIRCLESIZE/2;
        dy=y-UI.GRID_CURSORCIRCLESIZE/2;
        d=(int)Math.sqrt(dx*dx+dy*dy);
        if(d>ri&&d<ro&&(x+y)%2==0)image.setRGB(x,y,rgb);}}}
  
  static void setCursor(Grid grid){
    Toolkit tk=Toolkit.getDefaultToolkit();
    Dimension cdim=tk.getBestCursorSize(UI.GRID_CURSORCIRCLESIZE,UI.GRID_CURSORCIRCLESIZE);
    Point hotspot=new Point(cdim.width/2,cdim.height/2);
    Cursor c=tk.createCustomCursor(image,hotspot,NAME);
    grid.setCursor(c);}

}

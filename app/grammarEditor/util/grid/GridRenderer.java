package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_2D.GD;

/*
 * This is a rendering of a kisrhombille tessellation. 
 * We fill it with a background color and then draw a bunch of lines.
 * We define a kgeom hexagon slightly larger than the viewport
 * draw lines between all colinear vertices on the hexagon edge
 * this is our base image for the canvas, upon which we draw our translucent details image
 * 
 * NEW WAY TO DRAW GRID IMAGE
 * fill and draw unit box tile image. 2sqrt3 x 6 with unit hexagon in the middle. scaled. use scale for fish.
 * tile it across view, adjusting for view offset
 * invalidate unit box image on scale change
 * 
 * on scale change invalidate unit box image
 * 
 * 
 */
public class GridRenderer implements Serializable{
  
  private static final long serialVersionUID=2408218887813661894L;

  public void render(
    Graphics2D graphics,double viewwidth,double viewheight,double viewscale,
    double viewoffsetx,double viewoffsety){
    //get our minimal rectangular kisrhombille grid tile
    int tilewidth=(int)(viewscale*2.0*Math.sqrt(3.0))+1;  
    int tileheight=(int)(viewscale*6.0);
    //calculate offset for putting grid origin at view center
    double 
      littleoffsetx=((viewwidth/2.0)%(double)tilewidth),
      littleoffsety=((viewheight/2.0)%(double)tileheight);
    //calculate scaled offset for view offset
    double 
      scaledviewoffsetx=-((viewoffsetx*viewscale)%tilewidth),
      scaledviewoffsety=(viewoffsety*viewscale)%tileheight;
    //set the transform, holding the old transform so we can restore it later
    AffineTransform oldtransform=graphics.getTransform();
    graphics.transform(AffineTransform.getTranslateInstance(
      scaledviewoffsetx+littleoffsetx,
      scaledviewoffsety+littleoffsety));
    //paint tiles
    BufferedImage itile=getTileImage(tilewidth,tileheight,viewscale);
    int 
      tilexcount=(int)(viewwidth/tilewidth)+5,
      tileycount=(int)(viewheight/tileheight)+5;
    for(int x=0;x<tilexcount;x++){
      for(int y=0;y<tileycount;y++){
        graphics.drawImage(itile,null,x*tilewidth-tilewidth*2,y*tileheight-tileheight*2);}}
    //restore old transform
    graphics.setTransform(oldtransform);}
  
  /*
   * ################################
   * TILE IMAGE
   * ################################
   */
  
  BufferedImage tileimage=null;
  
  private BufferedImage getTileImage(int tilewidth,int tileheight,double viewscale){
    if(tileimage==null)
      tileimage=createTileImage(tilewidth,tileheight,viewscale);
    return tileimage;}
  
  public final void invalidateTileImage(){
    tileimage=null;}
  
  private BufferedImage createTileImage(int tilewidth,int tileheight,double viewscale){
    BufferedImage tile=new BufferedImage(tilewidth,tileheight,BufferedImage.TYPE_INT_RGB);
    double[] 
      p0={0,0},
      p2={tilewidth,0},
      p16={0,tileheight},
      p18={tilewidth,tileheight},
      p1=GD.getPoint_Mid2Points(p0[0],p0[1],p2[0],p2[1]),
      p8=GD.getPoint_Mid2Points(p0[0],p0[1],p16[0],p16[1]),
      p10=GD.getPoint_Mid2Points(p2[0],p2[1],p18[0],p18[1]),
      p17=GD.getPoint_Mid2Points(p16[0],p16[1],p18[0],p18[1]),
      //p9=G2D.getPoint_Mid2Points(p1[0],p1[1],p17[0],p17[1]), //unused, left for completeness
      //p4=G2D.getPoint_Mid2Points(p0[0],p0[1],p9[0],p9[1]),
      //p6=G2D.getPoint_Mid2Points(p2[0],p2[1],p9[0],p9[1]),
      //p12=G2D.getPoint_Mid2Points(p16[0],p16[1],p9[0],p9[1]),
      //p14=G2D.getPoint_Mid2Points(p18[0],p18[1],p9[0],p9[1]),
      p3=GD.getPoint_Between2Points(p0[0],p0[1],p8[0],p8[1],2.0/3.0),
      //p5=G2D.getPoint_Between2Points(p1[0],p1[1],p9[0],p9[1],1.0/3.0),
      p7=GD.getPoint_Between2Points(p2[0],p2[1],p10[0],p10[1],2.0/3.0),
      p11=GD.getPoint_Between2Points(p8[0],p8[1],p16[0],p16[1],1.0/3.0),    
      //p13=G2D.getPoint_Between2Points(p9[0],p9[1],p17[0],p17[1],2.0/3.0),
      p15=GD.getPoint_Between2Points(p10[0],p10[1],p18[0],p18[1],1.0/3.0);
    Path2D path=new Path2D.Double();
    path.moveTo(p0[0],p0[1]);
    path.lineTo(p16[0],p16[1]);
    path.lineTo(p18[0],p18[1]);
    path.lineTo(p2[0],p2[1]);
    path.lineTo(p0[0],p0[1]);
    path.lineTo(p18[0],p18[1]);
    path.moveTo(p1[0],p1[1]);
    path.lineTo(p17[0],p17[1]);
    path.moveTo(p2[0],p2[1]);
    path.lineTo(p16[0],p16[1]);
    path.moveTo(p7[0],p7[1]);
    path.lineTo(p11[0],p11[1]);
    path.moveTo(p10[0],p10[1]);
    path.lineTo(p8[0],p8[1]);
    path.moveTo(p15[0],p15[1]);
    path.lineTo(p3[0],p3[1]);
    path.moveTo(p0[0],p0[1]);
    path.lineTo(p7[0],p7[1]);
    path.moveTo(p2[0],p2[1]);
    path.lineTo(p3[0],p3[1]);
    path.moveTo(p11[0],p11[1]);
    path.lineTo(p18[0],p18[1]);
    path.moveTo(p15[0],p15[1]);
    path.lineTo(p16[0],p16[1]);
    Graphics2D g=tile.createGraphics();
    
    g.setPaint(UI.GRID_KGRIDBACKGROUNDCOLOR);
//    g.setPaint(new Color(255,255,255));
    
    g.fillRect(0,0,tilewidth,tileheight);
    g.setRenderingHints(UI.RENDERING_HINTS);
    
    g.setStroke(new BasicStroke(3.0f));
//    g.setStroke(new BasicStroke(6.0f));
    
    g.setPaint(UI.GRID_KGRIDLINECOLOR);
//    g.setPaint(new Color(64,192,64));
    
    g.draw(path);
    return tile;}
  
}

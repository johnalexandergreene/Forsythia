package org.fleen.forsythia.app.bread.SpinnerFrameGleaner;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/*
 * 
 * A spinner is a simple animation. An image that scrolls by, seamlessly looping.
 * It is made of frames
 * The frames are gleaned from an image and a few other params
 * 
 * given
 *
 * source image : string path to png file
 * direction : NESW
 * thickness : integer
 * increment : the number of pixels traversed per frame 
 *
 * create a directory full of frames
 * 
 * direction N means that the image move northwards, ie upwards
 * E means eastwards, ie from the left to the right
 * etc
 * 
 * if the direction is N or S then frame width is the width 
 *   of the source image and thickness specifies the frame height
 * 
 * if the direction is E or W then frame height is the height of the source image and
 *   thickness specifies the frame width
 *   
 * the scrolled dimension should be a multiple of the pixel increment otherwise we might get a stutter
 * 
 * ---
 * 
 * load image
 * 
 * 
 */
public class SpinnerFrameGleaner{
  
  static final String SOURCEIMAGEPATH="/home/john/Desktop/spinner/i22.png";
  static final String EXPORTDIRPATH="/home/john/Desktop/spinner/frames";
  static final int 
    THICKNESS=400,
    INCREMENT=2;
  /*
   * import image
   * for(0 to imagelength)
   *   create frame
   *   export frame
   */
  public void createFrames(){
    System.out.println("### START");
    BufferedImage source=importImage(),frame;
    Graphics2D g=(Graphics2D)source.getGraphics();
    int 
      framecount=source.getHeight()/INCREMENT,
      framewidth=source.getWidth(),
      frameheight=THICKNESS;
    System.out.println("framecount="+framecount);
    for(int i=0;i<framecount;i++){
      System.out.println("frame#"+i);
      frame=createFrame(source,i*INCREMENT,framewidth,frameheight);
      exportFrame(frame);}
    System.out.println("### END");}
  
  private BufferedImage importImage(){
    System.out.println("import : "+SOURCEIMAGEPATH);
    BufferedImage i=null;
    try{
      i=ImageIO.read(new File(SOURCEIMAGEPATH));
    }catch(Exception x){
      x.printStackTrace();}
    return i;}
  
  //TODO
  private BufferedImage createFrame(BufferedImage source,int lineindex,int framewidth,int frameheight){
    BufferedImage i;
    if(lineindex+frameheight<source.getHeight()){
      i=source.getSubimage(0,lineindex,framewidth,frameheight);  
    }else{
      i=new BufferedImage(framewidth,frameheight,BufferedImage.TYPE_INT_RGB);
      BufferedImage 
        i0=source.getSubimage(0,lineindex,framewidth,source.getHeight()-lineindex),
        i1=source.getSubimage(0,0,framewidth,lineindex+frameheight-source.getHeight()+1);
      Graphics g=i.getGraphics();
      g.drawImage(i0,0,0,null);
      g.drawImage(i1,0,i0.getHeight(),null);
      }
    return i;}
  
  RasterExporter rasterexporter=new RasterExporter(EXPORTDIRPATH);
  
  private void exportFrame(BufferedImage i){
    rasterexporter.export(i);
  }
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    SpinnerFrameGleaner g=new SpinnerFrameGleaner();
    g.createFrames();
  }

}

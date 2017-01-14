package org.fleen.forsythia.app.grammarEditor.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.util.textures.Texture;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;

/*
 * STUFF FOR ALL UI IN THE APP
 * graphics constants
 * Layout params
 * graphics utilities? 
 * misc utilities
 */
public class UI{
  
  /*
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   * UTILITIES
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   */
  
  //--------------------------------
  //RENDERING HINTS
  
  public static final HashMap<RenderingHints.Key,Object> RENDERING_HINTS=
      new HashMap<RenderingHints.Key,Object>();
    
  static{
    RENDERING_HINTS.put(
      RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    RENDERING_HINTS.put(
      RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DEFAULT);
    RENDERING_HINTS.put(
      RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    RENDERING_HINTS.put(
      RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY); 
    RENDERING_HINTS.put(
      RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);}
  
  public static final double VERTEX_CLOSENESS_MARGIN=22;
  
  /*
   * ################################
   * DISTINGUISHABLE RAINBOW COLOR SEQUENCE
   * pure colors. probably wanna pastelize them for actual use
   * for when we need a rainbow type sequence of colors that are easy to distinguish from one another
   * we store them as integers because it's more useful that way, and we'll have to 
   * translate to android eventually anyway
   * ################################
   */
  
  public static final int[] RAINBOW={
    new Color(255,0,0).getRGB(),
    new Color(255,128,0).getRGB(),
    new Color(255,210,1).getRGB(),
    new Color(255,255,0).getRGB(),
    new Color(128,255,0).getRGB(),
    new Color(0,255,255).getRGB(),
    new Color(0,128,255).getRGB(),
    new Color(57,57,255).getRGB(),
    new Color(171,89,255).getRGB(),
    new Color(255,0,255).getRGB()};
  
  /*
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   * UI PARAM CONSTANTS FOR EDITOR
   * EDIT GRAMMAR 
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   */
  
  /*
   * ++++++++++++++++++++++++++++++++
   * ELEMENT MENUS
   * ++++++++++++++++++++++++++++++++
   */
    
  public static final int 
    ELEMENTMENU_OVERVIEWMETAGONSROWS=3,
    ELEMENTMENU_OVERVIEWJIGSROWS=1,
    ELEMENTMENU_ARRAYEDGEMARGIN=8,
    ELEMENTMENU_ICONMARGIN=6,
    ELEMENTMENU_ICONGEOMETRYINSET=8,
    ELEMENTMENU_ICON_OUTLINEWIDTH=6;
  
  public static final float 
    //we use an odd value because an even value gives us the shaved-right-edge artifact
    ELEMENTMENU_ICONPATHSTROKETHICKNESS=3.0f;
  
  public static final Color 
    ELEMENTMENU_BACKGROUND=new Color(220,220,220),
    ELEMENTMENU_ICONBACKGROUND=new Color(220,220,220),
    ELEMENTMENU_ICON_FILL=new Color(148,212,148),
    ELEMENTMENU_METAGONWITHOUTJIGS_ICON_FILL=new Color(255,255,255),
    ELEMENTMENU_METAGONWITHJIGS_ICON_FILL=new Color(255,128,255),
    ELEMENTMENU_ICON_STROKE=new Color(110,110,110),
    ELEMENTMENU_ICON_OUTLINEDEFAULT=new Color(220,220,220),
    ELEMENTMENU_ICON_OUTLINEFOCUS=new Color(240,240,240);
  
  //easy to distinguish
  //simplified rainbow
  //color indicates jig count
  //white==0, red==1, etc
  public static final Color[] GRAMMAR_EDITOR_METAGON_ICONS_FILLCOLOR={
    new Color(255,255,255),
    new Color(225,81,81),
    new Color(231,184,117),
    new Color(229,231,117),
    new Color(99,211,110),
    new Color(126,201,254),
    new Color(207,141,235)};
  
  /*
   * ################################
   * GRID IMAGE
   * used by create metagon, view metagon, create jig, view jig
   * ################################
   */
  
  public static final Color 
    GRID_KGRIDBACKGROUNDCOLOR=new Color(197,197,197),
    GRID_KGRIDLINECOLOR=new Color(175,175,175),
    GRID_DRAWINGSTROKECOLOR=new Color(32,32,32);

  public static final float GRID_KGRIDLINETHICKNESS=1.0f;

  public static final int 
    GRID_CURSORCIRCLESIZE=32,
    GRID_CURSORSQUARESIZE=28;

  public static final int GRID_CENTERANDFITVIEWMARGIN=36;
  
  public static final float GRID_DRAWINGSTROKETHICKNESS=4.0f;
  
  public static final Stroke 
    GRID_DRAWINGSTROKE=
      new BasicStroke(GRID_DRAWINGSTROKETHICKNESS,
        BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
  
  public static final int GRID_DEFAULTVERTEXSPAN=12;
  
  /*
   * ################################
   * CREATE METAGON
   * ################################
   */
  
  public static final Color
    EDITORCREATEMETAGON_FINISHEDMETAGONFILLCOLOR=new Color(0,255,0,64);
  
  /*
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   * UI PARAM CONSTANTS FOR EDITOR
   * EDIT JIG
   * ################################
   * #  #  #  #  #  #  #  #  #  #  #
   * ################################
   */
  
  //--------------------------------
  //EDIT GEOMETRY
  
  //colors for edit geometry ui
  public static final Color
    EDITJIG_EDITGEOMETRY_STROKECOLOR=new Color(32,32,32),
    EDITJIG_EDITGEOMETRY_HOSTMETAGONFILLCOLOR=new Color(255,255,255,64),
    EDITJIG_EDITGEOMETRY_CONNECTEDHEADVERTEXDECORATIONCOLOR=new Color(255,255,64),
    EDITJIG_EDITGEOMETRY_UNCONNECTEDHEADVERTEXDECORATIONCOLOR=new Color(255,64,64);;

  public static final int
    EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSPAN=32;
  
  public static final float 
    EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSTROKETHICKNESS=8.0f;

  public static final Stroke 
    EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSTROKE=
      new BasicStroke(EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSTROKETHICKNESS,
      BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
  
  //--------------------------------
  //EDIT SECTIONS
  
  //colors for edit sections ui
  public static final Color
    EDITJIG_EDITSECTIONS_UNFOCUSSTROKECOLOR=new Color(244,244,244),
    EDITJIG_EDITSECTIONS_FOCUSSTROKECOLOR=new Color(48,48,48);
  
  //rainbow fill for sections
  //color by chorus index. 0=red, 1=orange, etc
  private static final int EDITJIG_EDITSECTIONS_SECTIONFILLALPHA=130;
  private static final float 
    EDITJIG_EDITSECTIONS_SECTIONFILLBRIGHTNESS=1.0f,
    EDITJIG_EDITSECTIONS_SECTIONFILLSATURATION=0.5f;
  
  //pastel translucent rainbow 
  public static Color[] EDITJIG_EDITSECTIONS_SECTIONFILL;
  
  static{
    Color c;
    float[] hsbvals=new float[3];
    EDITJIG_EDITSECTIONS_SECTIONFILL=new Color[RAINBOW.length];
    for(int i=0;i<RAINBOW.length;i++){
      c=new Color(RAINBOW[i]);
      Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),hsbvals);
      hsbvals[1]=EDITJIG_EDITSECTIONS_SECTIONFILLSATURATION;
      hsbvals[2]=EDITJIG_EDITSECTIONS_SECTIONFILLBRIGHTNESS;
      c=new Color(Color.HSBtoRGB(hsbvals[0],hsbvals[1],hsbvals[2]));
      EDITJIG_EDITSECTIONS_SECTIONFILL[i]=new Color(c.getRed(),c.getGreen(),c.getBlue(),EDITJIG_EDITSECTIONS_SECTIONFILLALPHA);}}
  
  /*
   * ################################
   * POPUP MENU
   * ################################
   */
  
  public static final Font POPUPMENUFONT=new Font("Dialog",Font.BOLD,16);
  
  /*
   * ################################
   * GEOMETRY METHODS FOR GRAPHICS
   * ################################
   */
  
  public static final Path2D.Double getClosedPath(DPolygon points){
    Path2D.Double path=new Path2D.Double();
    DPoint p=points.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}
  
  public static final Path2D.Double getClosedPath(List<double[]> points){
    Path2D.Double path=new Path2D.Double();
    double[] p=points.get(0);
    path.moveTo(p[0],p[1]);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p[0],p[1]);}
    path.closePath();
    return path;}
  
  public static final Path2D.Double getOpenPath(List<double[]> points){
    Path2D.Double path=new Path2D.Double();
    double[] p=points.get(0);
    path.moveTo(p[0],p[1]);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p[0],p[1]);}
    return path;}
  
}

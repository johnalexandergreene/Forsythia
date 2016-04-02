package org.fleen.forsythia.app.grammarEditor.project;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.app.grammarEditor.util.UI;

/*
 * a large square icon image rendering a project jig 
 */
class ProjectMetagonEditGrammarIconImage extends BufferedImage{
  
  ProjectMetagonEditGrammarIconImage(ProjectMetagon pm,int span){
    super(span,span,BufferedImage.TYPE_INT_RGB);
    //init image
    Path2D path=pm.getImagePath();
    Graphics2D g=createGraphics();
    g.setRenderingHints(UI.RENDERING_HINTS);
    //fill background
    g.setColor(UI.ELEMENTMENU_ICONBACKGROUND);
    g.fillRect(0,0,span,span);
    //glean metrics and transform
    Rectangle2D pbounds=path.getBounds2D();
    double pw=pbounds.getWidth(),ph=pbounds.getHeight(),scale;
    int maxpolygonimagespan=span-(UI.ELEMENTMENU_ICONGEOMETRYINSET*2);
    scale=(pw>ph)?maxpolygonimagespan/pw:maxpolygonimagespan/ph;
    AffineTransform t=new AffineTransform();
    t.scale(scale,-scale);//note y flip
    double 
      xoffset=-pbounds.getMinX()+(((span-(pw*scale))/2)/scale),
      yoffset=-pbounds.getMaxY()-(((span-(ph*scale))/2)/scale);
    t.translate(xoffset,yoffset);
    g.transform(t);
    //fill metagon
    //use color to distinguish protojig counts
    int pjcount=pm.getProtoJigCount();
    if(pjcount<UI.GRAMMAR_EDITOR_METAGON_ICONS_FILLCOLOR.length)
      g.setColor(UI.GRAMMAR_EDITOR_METAGON_ICONS_FILLCOLOR[pjcount]);
    else
      g.setColor(UI.GRAMMAR_EDITOR_METAGON_ICONS_FILLCOLOR[UI.GRAMMAR_EDITOR_METAGON_ICONS_FILLCOLOR.length-1]);
    g.fill(path);
    //stroke it
    g.setColor(UI.ELEMENTMENU_ICON_STROKE);
    g.setStroke(new BasicStroke(
      (float)(UI.ELEMENTMENU_ICONPATHSTROKETHICKNESS/scale),
      BasicStroke.CAP_SQUARE,
      BasicStroke.JOIN_ROUND,
      0,null,0));
    g.draw(path);}

}

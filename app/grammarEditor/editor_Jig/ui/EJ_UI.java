package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class EJ_UI extends JPanel{
//  EJ_Grid pangrid;
//  private JPanel panbottom;
//  private JLabel lblinfo;

  public EJ_UI(){
    setLayout(new BorderLayout(0, 0));
    
    JPanel pantop = new JPanel();
    add(pantop, BorderLayout.NORTH);
    WrapLayout layouttop=new WrapLayout();
    layouttop.setAlignment(FlowLayout.LEFT);
    pantop.setLayout(layouttop);
    
    JPanel panresetsavequit = new PanResetSaveQuit();
    pantop.add(panresetsavequit);
    
    JPanel pangriddensity = new PanGridDensity();
    pantop.add(pangriddensity);
    
    JPanel pangeometrylock = new PanGeometryLock();
    pantop.add(pangeometrylock);    
    
    JPanel panjigtag = new PanJigTag();
    pantop.add(panjigtag);
    
    JPanel pansectionchorus = new PanSectionChorus();
    pantop.add(pansectionchorus);
    
    JPanel pansectionanchor = new PanSectionAnchor();
    pantop.add(pansectionanchor);
    
    JPanel pansectiontag = new PanSectionTag();
    pantop.add(pansectiontag);
    
    JPanel pangrid = new EJ_Grid();
    add(pangrid, BorderLayout.CENTER);
    
    JPanel panbot = new JPanel();
    add(panbot, BorderLayout.SOUTH);
    
    JLabel lblinfo = new JLabel("polygon count, geometry validity, etc... foo foo foo");
    panbot.add(lblinfo);
    
    
    
    


  }
}

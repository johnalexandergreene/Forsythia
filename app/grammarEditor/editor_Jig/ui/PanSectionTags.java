package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fleen.forsythia.app.grammarEditor.GE;

public class PanSectionTags extends JPanel{
  
  private static final long serialVersionUID=3262174629447498277L;
  
  public JTextField txttag;

  public PanSectionTags(){
    
    setBackground(new Color(204, 255, 51));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Component horizontalStrut = Box.createHorizontalStrut(4);
    add(horizontalStrut);
    
    JLabel lbljigtag = new JLabel("SectionTags=");
    add(lbljigtag);
    lbljigtag.setFont(new Font("Dialog", Font.BOLD, 14));
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    add(horizontalStrut_3);
    
    txttag = new JTextField("foo",20);
    add(txttag);
    txttag.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 18));
    txttag.setBorder(null);
    txttag.addKeyListener(new KeyAdapter(){
      public void keyReleased(KeyEvent e){
        GE.ge.editor_jig.setSectionTags(txttag.getText());}});
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(4);
    add(horizontalStrut_1);
    
  }
  
}

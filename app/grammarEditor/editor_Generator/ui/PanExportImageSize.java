package org.fleen.forsythia.app.grammarEditor.editor_Generator.ui;

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
import org.fleen.forsythia.app.grammarEditor.util.UI;

public class PanExportImageSize extends JPanel{
  
  private static final long serialVersionUID=-1484832312439244645L;
  
  public JTextField txtsize;

  public PanExportImageSize(){
    
    setBackground(UI.BUTTON_ORANGE);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Component horizontalStrut = Box.createHorizontalStrut(8);
    add(horizontalStrut);
    
    JLabel lbljigtag = new JLabel("ExportSize=");
    add(lbljigtag);
    lbljigtag.setFont(new Font("Dialog", Font.BOLD, 14));
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(8);
    add(horizontalStrut_3);
    
    txtsize = new JTextField("1234",6);
    txtsize.setBackground(UI.BUTTON_YELLOW);
    add(txtsize);
    txtsize.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 18));
    txtsize.setBorder(null);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    add(horizontalStrut_1);
    txtsize.addKeyListener(new KeyAdapter(){
      public void keyReleased(KeyEvent e){
        GE.ge.editor_generator.setExportSize(txtsize.getText());}});
    
  }
}

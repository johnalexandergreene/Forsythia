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

public class PanDetailFloor extends JPanel{
  
  private static final long serialVersionUID=4200492407974666454L;
  
  public JTextField txtfloor;

  public PanDetailFloor(){
    
    setBackground(UI.BUTTON_BLUE);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Component horizontalStrut = Box.createHorizontalStrut(8);
    add(horizontalStrut);
    
    JLabel lbljigtag = new JLabel("DetailFloor = ");
    add(lbljigtag);
    lbljigtag.setFont(new Font("Dialog", Font.BOLD, 14));
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(8);
    add(horizontalStrut_3);
    
    txtfloor = new JTextField("12.34",8);
    txtfloor.setBackground(UI.BUTTON_YELLOW);
    add(txtfloor);
    txtfloor.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 18));
    txtfloor.setBorder(null);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    add(horizontalStrut_1);
    txtfloor.addKeyListener(new KeyAdapter(){
      public void keyReleased(KeyEvent e){
        GE.ge.editor_generator.setDetailFloor(txtfloor.getText());}});
    
  }
}

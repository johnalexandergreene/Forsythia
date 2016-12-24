package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JSpinner;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;

public class EJ_Geometry extends JPanel{

  /**
   * Create the panel.
   */
  public EJ_Geometry(){
    
    JLabel lblLblgriddensity = new JLabel("000");
    lblLblgriddensity.setFont(new Font("Dialog", Font.BOLD, 21));
    
    JButton button = new JButton("+");
    button.setFont(new Font("Dialog", Font.BOLD, 16));
    setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    
    JCheckBox chklock = new JCheckBox("LOCK");
    add(chklock);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    add(horizontalStrut);
    add(lblLblgriddensity);
    add(button);
    
    JButton button_1 = new JButton("-");
    button_1.setFont(new Font("Dialog", Font.BOLD, 16));
    add(button_1);

  }
}

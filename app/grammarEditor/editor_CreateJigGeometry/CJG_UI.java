package org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CJG_UI extends JPanel{
  private JTextField txtJigtags;
  private JTextField txtSectiontags;

  /**
   * Create the panel.
   */
  public CJG_UI(){
    
    JPanel panel_1 = new JPanel();
    
    JLabel lblInfo = new JLabel("info");
    
    Box boxtopmain = Box.createVerticalBox();
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.TRAILING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
            .addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addComponent(lblInfo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addComponent(boxtopmain, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(boxtopmain, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(lblInfo)
          .addContainerGap())
    );
    
    Box boxtop0 = Box.createHorizontalBox();
    boxtop0.setOpaque(true);
    boxtop0.setBackground(new Color(255, 255, 51));
    boxtopmain.add(boxtop0);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_1);
    
    JLabel label_1 = new JLabel("000");
    label_1.setForeground(new Color(102, 51, 255));
    label_1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 24));
    boxtop0.add(label_1);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_2);
    
    JButton btngriddensityincrease = new JButton("+");
    btngriddensityincrease.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btngriddensityincrease.setBackground(new Color(255, 153, 0));
    boxtop0.add(btngriddensityincrease);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_3);
    
    JButton btngriddensitydecrease = new JButton("-");
    btngriddensitydecrease.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btngriddensitydecrease.setBackground(new Color(255, 0, 153));
    boxtop0.add(btngriddensitydecrease);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut);
    
    txtJigtags = new JTextField();
    txtJigtags.setFont(new Font("Dialog", Font.PLAIN, 18));
    txtJigtags.setText("jigtags");
    boxtop0.add(txtJigtags);
    txtJigtags.setColumns(10);
    
    Component horizontalStrut_6 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_6);
    
    JButton btnsave = new JButton("save");
    btnsave.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnsave.setBackground(new Color(51, 204, 102));
    boxtop0.add(btnsave);
    
    Component horizontalStrut_4 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_4);
    
    JButton btnDiscard = new JButton("discard");
    btnDiscard.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnDiscard.setBackground(new Color(255, 204, 0));
    boxtop0.add(btnDiscard);
    
    Component horizontalStrut_5 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_5);
    
    Box boxtop2 = Box.createHorizontalBox();
    boxtopmain.add(boxtop2);
    
    Component horizontalStrut_8 = Box.createHorizontalStrut(20);
    boxtop2.add(horizontalStrut_8);
    
    JButton btnanchor = new JButton("anchor=0");
    btnanchor.setBackground(Color.GREEN);
    btnanchor.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    boxtop2.add(btnanchor);
    
    Component horizontalStrut_7 = Box.createHorizontalStrut(20);
    boxtop2.add(horizontalStrut_7);
    
    JButton btnchorusindex = new JButton("chorusindex=0");
    btnchorusindex.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnchorusindex.setBackground(Color.GREEN);
    boxtop2.add(btnchorusindex);
    
    Component horizontalStrut_10 = Box.createHorizontalStrut(20);
    boxtop2.add(horizontalStrut_10);
    
    txtSectiontags = new JTextField();
    txtSectiontags.setFont(new Font("Dialog", Font.PLAIN, 18));
    txtSectiontags.setText("sectiontags");
    boxtop2.add(txtSectiontags);
    txtSectiontags.setColumns(10);
    
    Component horizontalStrut_9 = Box.createHorizontalStrut(20);
    boxtop2.add(horizontalStrut_9);
    setLayout(groupLayout);

  }
}

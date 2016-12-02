package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
class EJ_UI extends JPanel{
  
  JTextField txtjigtags,txtsectiontags;
  EJ_Grid pangrid;
  JLabel lblgriddensity,lblinfo;

  /**
   * Create the panel.
   */
  EJ_UI(){
    
    pangrid = new EJ_Grid();
    
    lblinfo = new JLabel("info");
    
    Box boxtopmain = Box.createVerticalBox();
    boxtopmain.setOpaque(true);
    boxtopmain.setBackground(new Color(255, 255, 0));
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.TRAILING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
            .addComponent(pangrid, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addComponent(lblinfo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addComponent(boxtopmain, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(boxtopmain, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(pangrid, GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(lblinfo)
          .addContainerGap())
    );
    
    Box boxtop0 = Box.createHorizontalBox();
    boxtop0.setBackground(new Color(255, 255, 51));
    boxtopmain.add(boxtop0);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_1);
    
    lblgriddensity = new JLabel("GD=000");
    lblgriddensity.setToolTipText("Grid Density");
    lblgriddensity.setForeground(new Color(102, 51, 255));
    lblgriddensity.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 24));
    boxtop0.add(lblgriddensity);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_2);
    
    JButton btnincrementgriddensity = new JButton("+");
    btnincrementgriddensity.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.incrementGridDensity();}});
    btnincrementgriddensity.setToolTipText("Increment Grid Density");
    btnincrementgriddensity.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnincrementgriddensity.setBackground(new Color(255, 153, 0));
    boxtop0.add(btnincrementgriddensity);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_3);
    
    JButton btndecrementgriddensity = new JButton("-");
    btndecrementgriddensity.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.decrementGridDensity();}});
    btndecrementgriddensity.setToolTipText("Decrement Grid Density");
    btndecrementgriddensity.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btndecrementgriddensity.setBackground(new Color(244, 164, 96));
    boxtop0.add(btndecrementgriddensity);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut);
    
    txtjigtags = new JTextField();
    txtjigtags.setToolTipText("Jig Tags");
    txtjigtags.setFont(new Font("Dialog", Font.PLAIN, 18));
    txtjigtags.setText("jigtags");
    boxtop0.add(txtjigtags);
    txtjigtags.setColumns(10);
    
    Component horizontalStrut_6 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_6);
    
    JButton btnsave = new JButton("save");
    btnsave.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.saveJig();}});
    btnsave.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnsave.setBackground(new Color(51, 204, 102));
    boxtop0.add(btnsave);
    
    Component horizontalStrut_4 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_4);
    
    JButton btndiscard = new JButton("discard");
    btndiscard.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.discardJig();}});
    btndiscard.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btndiscard.setBackground(new Color(205, 92, 92));
    boxtop0.add(btndiscard);
    
    Component horizontalStrut_5 = Box.createHorizontalStrut(20);
    boxtop0.add(horizontalStrut_5);
    
    Box boxtop1 = Box.createHorizontalBox();
    boxtop1.setBackground(new Color(255, 255, 0));
    boxtopmain.add(boxtop1);
    
    Component horizontalStrut_8 = Box.createHorizontalStrut(20);
    boxtop1.add(horizontalStrut_8);
    
    JButton btnanchor = new JButton("A=0");
    btnanchor.setToolTipText("Anchor");
    btnanchor.setBackground(new Color(135, 206, 250));
    btnanchor.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    boxtop1.add(btnanchor);
    
    Component horizontalStrut_7 = Box.createHorizontalStrut(20);
    boxtop1.add(horizontalStrut_7);
    
    JButton btnchorusindex = new JButton("CI=0");
    btnchorusindex.setToolTipText("Chorus Index");
    btnchorusindex.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnchorusindex.setBackground(new Color(255, 182, 193));
    boxtop1.add(btnchorusindex);
    
    Component horizontalStrut_10 = Box.createHorizontalStrut(20);
    boxtop1.add(horizontalStrut_10);
    
    txtsectiontags = new JTextField();
    txtsectiontags.setToolTipText("Section Tags");
    txtsectiontags.setFont(new Font("Dialog", Font.PLAIN, 18));
    txtsectiontags.setText("sectiontags");
    boxtop1.add(txtsectiontags);
    txtsectiontags.setColumns(10);
    
    Component horizontalStrut_9 = Box.createHorizontalStrut(20);
    boxtop1.add(horizontalStrut_9);
    setLayout(groupLayout);

  }
}

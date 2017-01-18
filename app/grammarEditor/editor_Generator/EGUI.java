package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
public class EGUI extends JPanel{

  public EGUI_Viewer viewer;
  JLabel lblgeneratorstateinfo;
  JButton btneditgrammar,btnstartstop,btnmode;
  EGUI_ConfigMenu popupconfigure=new EGUI_ConfigMenu();
  
  public EGUI(){
    
    viewer = new EGUI_Viewer();
    viewer.setBackground(new Color(128,128,128));
    
    lblgeneratorstateinfo = new JLabel("fo ofoo foo fooo");
    lblgeneratorstateinfo.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 14));
    
    Box horizontalBox = Box.createHorizontalBox();
    horizontalBox.setOpaque(true);
    horizontalBox.setBackground(new Color(255, 204, 255));
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.TRAILING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
            .addComponent(viewer, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
            .addComponent(lblgeneratorstateinfo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
          .addContainerGap())
        .addComponent(horizontalBox, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addComponent(horizontalBox, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(viewer, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(lblgeneratorstateinfo, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
    );
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(20);
    horizontalBox.add(horizontalStrut_1);
    
    btnmode = new JButton("+CONTINUOUS+");
    btnmode.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });
    btnmode.setForeground(Color.YELLOW);
    btnmode.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.toggleMode();}});
    btnmode.setToolTipText("Toggle intermittant/continuous");
    btnmode.setBackground(new Color(204, 51, 255));
    btnmode.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    horizontalBox.add(btnmode);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(8);
    horizontalBox.add(horizontalStrut_3);
    
    btnstartstop = new JButton(">>");
    btnstartstop.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.generatorStartStop();}});
    btnstartstop.setToolTipText("Generate the next composition");
    btnstartstop.setBackground(new Color(153, 255, 255));
    btnstartstop.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    horizontalBox.add(btnstartstop);
    
    Component horizontalStrut_5 = Box.createHorizontalStrut(32);
    horizontalBox.add(horizontalStrut_5);
    
    JButton btnexportcomposition = new JButton("Export");
    horizontalBox.add(btnexportcomposition);
    btnexportcomposition.setBackground(new Color(204, 153, 102));
    btnexportcomposition.setToolTipText("Export composition");
    btnexportcomposition.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnexportcomposition.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.exportComposition();}});
    
    btneditgrammar = new JButton("Configure");
    btneditgrammar.setToolTipText("edit grammar");
    btneditgrammar.setBackground(new Color(255, 204, 0));
    btneditgrammar.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btneditgrammar.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        popupconfigure.show(btneditgrammar,0,btneditgrammar.getHeight());}});
    
    Component horizontalStrut = Box.createHorizontalStrut(32);
    horizontalBox.add(horizontalStrut);
    horizontalBox.add(btneditgrammar);
    
    Component horizontalGlue = Box.createHorizontalGlue();
    horizontalBox.add(horizontalGlue);
    setLayout(groupLayout);

  }
}

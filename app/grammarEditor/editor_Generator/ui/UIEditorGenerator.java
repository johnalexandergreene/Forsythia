package org.fleen.forsythia.app.grammarEditor.editor_Generator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.util.ui.WrapLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIEditorGenerator extends JPanel{

  private static final long serialVersionUID=-7926505214597124551L;

  public PanInterval pangenerateinterval;
  public PanDetailFloor pandetailfloor;
  public PanViewer panviewer;
  
  public UIEditorGenerator(){
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    JPanel pantop = new JPanel();
    WrapLayout wl_pantop = new WrapLayout();
    wl_pantop.setAlignment(FlowLayout.LEFT);
    pantop.setLayout(wl_pantop);
    GridBagConstraints gbc_pantop = new GridBagConstraints();
    gbc_pantop.insets = new Insets(0, 0, 5, 0);
    gbc_pantop.fill = GridBagConstraints.BOTH;
    gbc_pantop.gridx = 0;
    gbc_pantop.gridy = 0;
    add(pantop, gbc_pantop);
    
    JButton btngeneratestopgo = new JButton("Stop");
    btngeneratestopgo.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.toggleStopGo();}});
    pantop.add(btngeneratestopgo);
    
    JButton btngeneratemode = new JButton("Continuous");
    btngeneratemode.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.toggleMode();}});
    pantop.add(btngeneratemode);
    
    pangenerateinterval = new PanInterval();
    pantop.add(pangenerateinterval);
    
    Component horizontalStrut = Box.createHorizontalStrut(8);
    pantop.add(horizontalStrut);
    
    pandetailfloor = new PanDetailFloor();
    pantop.add(pandetailfloor);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    pantop.add(horizontalStrut_1);
    
    JButton btnexportdir = new JButton("ExportDir=~/fleen/export");
    btnexportdir.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.setExportDir();}});
    pantop.add(btnexportdir);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    pantop.add(horizontalStrut_2);
    
    JButton btngrammar = new JButton("Grammar");
    btngrammar.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.openGrammarEditor();}});
    pantop.add(btngrammar);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(8);
    pantop.add(horizontalStrut_3);
    
    JButton btnabout = new JButton("About");
    btnabout.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.openAboutPopup();}});
    pantop.add(btnabout);
    
    panviewer = new PanViewer();
    panviewer.setBackground(Color.YELLOW);
    GridBagConstraints gbc_panimage = new GridBagConstraints();
    gbc_panimage.insets = new Insets(0, 0, 5, 0);
    gbc_panimage.fill = GridBagConstraints.BOTH;
    gbc_panimage.gridx = 0;
    gbc_panimage.gridy = 1;
    add(panviewer, gbc_panimage);
    
    JPanel paninfo = new JPanel();
    GridBagConstraints gbc_paninfo = new GridBagConstraints();
    gbc_paninfo.fill = GridBagConstraints.BOTH;
    gbc_paninfo.gridx = 0;
    gbc_paninfo.gridy = 2;
    add(paninfo, gbc_paninfo);
    
    JLabel lblinfo = new JLabel("info");
    paninfo.add(lblinfo);

  }

}

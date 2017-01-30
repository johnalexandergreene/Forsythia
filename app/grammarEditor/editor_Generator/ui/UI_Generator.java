package org.fleen.forsythia.app.grammarEditor.editor_Generator.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.util.ui.WrapLayout;

public class UI_Generator extends JPanel{

  private static final long serialVersionUID=-7926505214597124551L;

  public JButton 
    btngeneratestopgo,
    btngeneratemode;
  public PanInterval pangenerateinterval;
  public PanDetailFloor pandetailfloor;
  public PanViewer panviewer;
  public JButton btnexportdir;
  public PanExportImageSize panexportsize;
  public JLabel lblinfo;
  
  public UI_Generator(){
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
    
    btngeneratestopgo = new JButton("stopgo foo");
    btngeneratestopgo.setBackground(UI.BUTTON_PURPLE);
    btngeneratestopgo.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.toggleStopGo();}});
    pantop.add(btngeneratestopgo);
    
    btngeneratemode = new JButton("mode foo");
    btngeneratemode.setBackground(UI.BUTTON_PURPLE);
    btngeneratemode.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.toggleMode();
        }});
    pantop.add(btngeneratemode);
    
    pangenerateinterval = new PanInterval();
    pantop.add(pangenerateinterval);
    
    Component horizontalStrut = Box.createHorizontalStrut(12);
    pantop.add(horizontalStrut);
    
    pandetailfloor = new PanDetailFloor();
    pantop.add(pandetailfloor);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(12);
    pantop.add(horizontalStrut_1);
    
    JButton btnexport = new JButton("Export Image");
    btnexport.setBackground(UI.BUTTON_ORANGE);
    btnexport.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.exportImage();}});
    pantop.add(btnexport);
    
    btnexportdir = new JButton("ExportDir=~/fleen/export");
    btnexportdir.setBackground(UI.BUTTON_ORANGE);
    btnexportdir.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.setExportDir();}});
    pantop.add(btnexportdir);
    
    panexportsize = new PanExportImageSize();
    pantop.add(panexportsize);
    
    Component horizontalStrut_4 = Box.createHorizontalStrut(12);
    pantop.add(horizontalStrut_4);
    
    JButton btngrammar = new JButton("Grammar");
    btngrammar.setBackground(UI.BUTTON_RED);
    btngrammar.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_generator.openGrammarEditor();}});    
    pantop.add(btngrammar);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(12);
    pantop.add(horizontalStrut_3);
    
    JButton btnabout = new JButton("About");
    btnabout.setBackground(UI.BUTTON_GREEN);
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
    
    lblinfo = new JLabel("info");
    paninfo.add(lblinfo);

  }

}

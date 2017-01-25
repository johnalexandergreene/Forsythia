package org.fleen.forsythia.app.grammarEditor.editor_Grammar.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.util.ui.WrapLayout;

@SuppressWarnings("serial")
public class UIEditMetagon extends JPanel{
  
  public JLabel lblgrammarname;

  private JButton btngrammarimport;
  private JButton btngrammarexport;
  private JButton btngrammarnew;
  private JButton btngenerate;

  public JLabel lblmetagonstitle;
  public JLabel lblmetagonscount;
  public JLabel lblmetagonjiglesscount;
  public JLabel lblmetagonsisolatedcount;
  public JButton btnmetagonscreate;
  public  JButton btnmetagonsedit;
  public JButton btnmetagonsdiscard;  
  
  public PanMetagonMenu panmetagonmenu;
  
  public JLabel lbljigstitle;
  public JLabel lbljigscount;
  public JButton btnjigscreate;
  public JButton btnjigsedit;
  public JButton btnjigsdiscard;
  
  public PanJigMenu panjigmenu;
  
  public UIEditMetagon(){
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    JPanel pantop = new JPanel();
    pantop.setBackground(SystemColor.control);
    WrapLayout wl_pantop = new WrapLayout();
    wl_pantop.setAlignment(FlowLayout.LEFT);
    pantop.setLayout(wl_pantop);
    GridBagConstraints gbc_pantop = new GridBagConstraints();
    gbc_pantop.insets = new Insets(0, 0, 5, 0);
    gbc_pantop.fill = GridBagConstraints.BOTH;
    gbc_pantop.gridx = 0;
    gbc_pantop.gridy = 0;
    add(pantop, gbc_pantop);
    
    lblgrammarname = new JLabel("Grammar = foo");
    lblgrammarname.setFont(new Font("Dialog", Font.BOLD, 18));
    pantop.add(lblgrammarname);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(20);
    pantop.add(horizontalStrut_1);
    
    btngrammarimport = new JButton("Import");
    btngrammarimport.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.importGrammar();}});
    pantop.add(btngrammarimport);
    
    btngrammarexport = new JButton("Export");
    btngrammarexport.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.exportGrammar();}});
    pantop.add(btngrammarexport);
    
    btngrammarnew = new JButton("New");
    btngrammarnew.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.createNewGrammar();}});
    pantop.add(btngrammarnew);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    pantop.add(horizontalStrut);
    
    btngenerate = new JButton("Generate");
    btngenerate.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.generate();}});
    pantop.add(btngenerate);
    
    JPanel panmetagonbuttons = new JPanel();
    panmetagonbuttons.setBackground(SystemColor.control);
    WrapLayout wl_panmetagonbuttons = new WrapLayout();
    wl_panmetagonbuttons.setAlignment(FlowLayout.LEFT);
    panmetagonbuttons.setLayout(wl_panmetagonbuttons);
    GridBagConstraints gbc_panmetagonbuttons = new GridBagConstraints();
    gbc_panmetagonbuttons.insets = new Insets(0, 0, 5, 0);
    gbc_panmetagonbuttons.fill = GridBagConstraints.BOTH;
    gbc_panmetagonbuttons.gridx = 0;
    gbc_panmetagonbuttons.gridy = 1;
    add(panmetagonbuttons, gbc_panmetagonbuttons);
    
    lblmetagonstitle = new JLabel("Metagons");
    lblmetagonstitle.setFont(new Font("Dialog", Font.BOLD, 18));
    panmetagonbuttons.add(lblmetagonstitle);
    
    Component horizontalStrut_5 = Box.createHorizontalStrut(20);
    panmetagonbuttons.add(horizontalStrut_5);
    
    lblmetagonscount = new JLabel("Count=foo");
    lblmetagonscount.setFont(new Font("Dialog", Font.BOLD, 16));
    panmetagonbuttons.add(lblmetagonscount);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(20);
    panmetagonbuttons.add(horizontalStrut_2);
    
    lblmetagonjiglesscount = new JLabel("Jigless=foo");
    lblmetagonjiglesscount.setFont(new Font("Dialog", Font.BOLD, 16));
    panmetagonbuttons.add(lblmetagonjiglesscount);
    
    Component horizontalStrut_4 = Box.createHorizontalStrut(20);
    panmetagonbuttons.add(horizontalStrut_4);
    
    btnmetagonscreate = new JButton("Create");
    btnmetagonscreate.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.createMetagon();}});
    
    lblmetagonsisolatedcount = new JLabel("Isolated=foo");
    lblmetagonsisolatedcount.setFont(new Font("Dialog", Font.BOLD, 16));
    panmetagonbuttons.add(lblmetagonsisolatedcount);
    
    Component horizontalStrut_7 = Box.createHorizontalStrut(20);
    panmetagonbuttons.add(horizontalStrut_7);
    panmetagonbuttons.add(btnmetagonscreate);
    
    btnmetagonsedit = new JButton("Edit");
    btnmetagonsedit.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.editMetagon();}});
    panmetagonbuttons.add(btnmetagonsedit);
    
    btnmetagonsdiscard = new JButton("Discard");
    btnmetagonsdiscard.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.discardMetagon();}});
    panmetagonbuttons.add(btnmetagonsdiscard);
    
    panmetagonmenu = new PanMetagonMenu();
    panmetagonmenu.setBackground(Color.MAGENTA);
    GridBagConstraints gbc_panmetagonmenu = new GridBagConstraints();
    gbc_panmetagonmenu.insets = new Insets(0, 0, 5, 0);
    gbc_panmetagonmenu.fill = GridBagConstraints.BOTH;
    gbc_panmetagonmenu.gridx = 0;
    gbc_panmetagonmenu.gridy = 2;
    add(panmetagonmenu, gbc_panmetagonmenu);
    
    JPanel panjigbuttons = new JPanel();
    panjigbuttons.setBackground(SystemColor.control);
    GridBagConstraints gbc_panjigbuttons = new GridBagConstraints();
    gbc_panjigbuttons.insets = new Insets(0, 0, 5, 0);
    gbc_panjigbuttons.fill = GridBagConstraints.BOTH;
    gbc_panjigbuttons.gridx = 0;
    gbc_panjigbuttons.gridy = 3;
    add(panjigbuttons, gbc_panjigbuttons);
    
    panjigmenu = new PanJigMenu();
    panjigmenu.setBackground(Color.YELLOW);
    WrapLayout wl_panjigbuttons = new WrapLayout();
    wl_panjigbuttons.setAlignment(FlowLayout.LEFT);
    panjigbuttons.setLayout(wl_panjigbuttons);
    
    lbljigstitle = new JLabel("Jigs");
    lbljigstitle.setFont(new Font("Dialog", Font.BOLD, 18));
    panjigbuttons.add(lbljigstitle);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(20);
    panjigbuttons.add(horizontalStrut_3);
    
    lbljigscount = new JLabel("Count=foo");
    lbljigscount.setFont(new Font("Dialog", Font.BOLD, 16));
    panjigbuttons.add(lbljigscount);
    
    Component horizontalStrut_6 = Box.createHorizontalStrut(20);
    panjigbuttons.add(horizontalStrut_6);
    
    btnjigscreate = new JButton("Create");
    btnjigscreate.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.createJig();}});
    panjigbuttons.add(btnjigscreate);
    
    btnjigsedit = new JButton("Edit");
    btnjigsedit.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.editJig();}});
    panjigbuttons.add(btnjigsedit);
    
    btnjigsdiscard = new JButton("Discard");
    btnjigsdiscard.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.ge.editor_grammar.discardJig();}});
    panjigbuttons.add(btnjigsdiscard);
    GridBagConstraints gbc_panjigmenu = new GridBagConstraints();
    gbc_panjigmenu.fill = GridBagConstraints.BOTH;
    gbc_panjigmenu.gridx = 0;
    gbc_panjigmenu.gridy = 4;
    add(panjigmenu, gbc_panjigmenu);
    
  }
}

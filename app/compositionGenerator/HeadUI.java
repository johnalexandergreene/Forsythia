package org.fleen.forsythia.app.compositionGenerator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class HeadUI extends JFrame {

	private static final long serialVersionUID = -2749846443106819716L;
	
	public HeadAbstract head;
	
	JPanel contentPane;
	JPanel pancontrol;
  HeadViewer panimage;
	JTextField 
	  txtinterval,txtexportsize;
	JButton lblmode,lblstartstop;
	JCheckBox chkautoexport;
	
	boolean fullscreenmode=false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HeadUI frame = new HeadUI(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public HeadUI(HeadAbstract h){
	  head=h;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 709);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		pancontrol = new JPanel();
		pancontrol.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel pangenerate = new JPanel();
		pangenerate.setBackground(new Color(250, 250, 210));
		
		JButton btnfullscreen = new JButton("F");
		btnfullscreen.setToolTipText("Fullscreen");
		btnfullscreen.setForeground(new Color(255, 0, 255));
		btnfullscreen.addMouseListener(new MouseAdapter() {
		  public void mouseClicked(MouseEvent e) {
		    enterFullScreenMode();}});
		btnfullscreen.setOpaque(true);
		btnfullscreen.setBackground(Color.ORANGE);
		btnfullscreen.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 255, 204));
		
		GroupLayout gl_pancontrol = new GroupLayout(pancontrol);
		gl_pancontrol.setHorizontalGroup(
		  gl_pancontrol.createParallelGroup(Alignment.LEADING)
		    .addGroup(gl_pancontrol.createSequentialGroup()
		      .addContainerGap()
		      .addComponent(pangenerate, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
		      .addPreferredGap(ComponentPlacement.RELATED)
		      .addComponent(panel, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
		      .addPreferredGap(ComponentPlacement.RELATED)
		      .addComponent(btnfullscreen, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
		      .addContainerGap())
		);
		gl_pancontrol.setVerticalGroup(
		  gl_pancontrol.createParallelGroup(Alignment.TRAILING)
		    .addGroup(gl_pancontrol.createSequentialGroup()
		      .addContainerGap()
		      .addGroup(gl_pancontrol.createParallelGroup(Alignment.LEADING)
		        .addComponent(panel, GroupLayout.PREFERRED_SIZE, 57, Short.MAX_VALUE)
		        .addComponent(btnfullscreen, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
		        .addComponent(pangenerate, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
		      .addGap(134))
		);
		
		txtexportsize = new JTextField();
		txtexportsize.setToolTipText("Export image size");
		txtexportsize.setBackground(new Color(255, 255, 0));
		txtexportsize.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
		txtexportsize.setText("700x700");
		txtexportsize.setColumns(10);
		
		JButton btnExport = new JButton("exp");
		btnExport.setToolTipText("Export");
		btnExport.addMouseListener(new MouseAdapter() {
		  public void mouseClicked(MouseEvent e) {
		    if(head.isExportModeManual())
		      head.export();}});
		btnExport.setForeground(new Color(135, 206, 250));
		btnExport.setBackground(new Color(30, 144, 255));
		btnExport.setFont(new Font("Dialog", Font.BOLD, 16));
		
		chkautoexport = new JCheckBox("aut");
		chkautoexport.setToolTipText("Auto export");
		chkautoexport.setForeground(new Color(184, 134, 11));
		chkautoexport.setBackground(new Color(255, 153, 255));
		chkautoexport.setFont(new Font("Dialog", Font.BOLD, 16));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
		  gl_panel.createParallelGroup(Alignment.TRAILING)
		    .addGroup(gl_panel.createSequentialGroup()
		      .addContainerGap()
		      .addComponent(txtexportsize, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
		      .addGap(6)
		      .addComponent(chkautoexport)
		      .addPreferredGap(ComponentPlacement.RELATED)
		      .addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
		      .addContainerGap())
		);
		gl_panel.setVerticalGroup(
		  gl_panel.createParallelGroup(Alignment.LEADING)
		    .addGroup(gl_panel.createSequentialGroup()
		      .addContainerGap()
		      .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
		        .addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
		        .addComponent(chkautoexport, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
		        .addComponent(txtexportsize, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
		      .addContainerGap(13, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		lblmode = new JButton("INT");
		lblmode.setBackground(new Color(143, 188, 143));
		lblmode.setForeground(new Color(255, 255, 255));
		lblmode.setToolTipText("Intermittant or Continuous");
		lblmode.addMouseListener(new MouseAdapter() {
		  public void mouseClicked(MouseEvent e) {
		    head.toggleCreationMode();}});
		lblmode.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
		
		txtinterval = new JTextField();
		txtinterval.setToolTipText("Generation Interval");
		txtinterval.setBackground(new Color(175, 238, 238));
		txtinterval.addKeyListener(new KeyAdapter() {
		  public void keyReleased(KeyEvent e){
		    head.setCreationInterval(txtinterval);}});
		txtinterval.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
		txtinterval.setText("500");
		txtinterval.setColumns(10);
		
		lblstartstop = new JButton(">>");
		lblstartstop.setFocusable(false);
		lblstartstop.setToolTipText("Start/Stop Generation");
		lblstartstop.setBackground(new Color(220, 20, 60));
		lblstartstop.setForeground(new Color(255, 255, 255));
		lblstartstop.addMouseListener(new MouseAdapter() {
		  public void mouseClicked(MouseEvent e) {
		    head.startStopCreation();}});
		lblstartstop.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 28));
		GroupLayout gl_pangenerate = new GroupLayout(pangenerate);
		gl_pangenerate.setHorizontalGroup(
		  gl_pangenerate.createParallelGroup(Alignment.LEADING)
		    .addGroup(gl_pangenerate.createSequentialGroup()
		      .addGap(6)
		      .addComponent(lblstartstop, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
		      .addPreferredGap(ComponentPlacement.UNRELATED)
		      .addComponent(lblmode)
		      .addPreferredGap(ComponentPlacement.RELATED)
		      .addComponent(txtinterval, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
		      .addContainerGap())
		);
		gl_pangenerate.setVerticalGroup(
		  gl_pangenerate.createParallelGroup(Alignment.LEADING)
		    .addGroup(gl_pangenerate.createSequentialGroup()
		      .addContainerGap()
		      .addGroup(gl_pangenerate.createParallelGroup(Alignment.TRAILING, false)
		        .addComponent(lblstartstop, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
		        .addGroup(Alignment.LEADING, gl_pangenerate.createParallelGroup(Alignment.BASELINE)
		          .addComponent(txtinterval, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
		          .addComponent(lblmode, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)))
		      .addContainerGap(10, Short.MAX_VALUE))
		);
		pangenerate.setLayout(gl_pangenerate);
		pancontrol.setLayout(gl_pancontrol);
		
		panimage = new HeadViewer(head);
		panimage.setBorder(null);
		panimage.setBackground(new Color(154, 205, 50));
		panimage.addKeyListener(new KeyAdapter() {
		  public void keyTyped(KeyEvent e) {
		    exitFullScreenMode();}});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
		  gl_contentPane.createParallelGroup(Alignment.TRAILING)
		    .addGroup(gl_contentPane.createSequentialGroup()
		      .addGap(12)
		      .addComponent(pancontrol, GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
		      .addGap(12))
		    .addComponent(panimage, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
		  gl_contentPane.createParallelGroup(Alignment.TRAILING)
		    .addGroup(gl_contentPane.createSequentialGroup()
		      .addComponent(panimage, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
		      .addPreferredGap(ComponentPlacement.UNRELATED)
		      .addComponent(pancontrol, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
		      .addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	 /*
   * ################################
   * DEFAULT WINDOW SIZE
   * ################################
   */
	
	private static final double DEFAULTWINDOWSIZE_PROPORTIONALWIDTH=0.75;
	
	void setDefaultWindowBounds(){
	  setExtendedState(NORMAL);
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    int 
      h=(int)(d.height*0.85),
      w=(int)(h*DEFAULTWINDOWSIZE_PROPORTIONALWIDTH),
      x=(d.width-w)/2,
      y=(d.height-h)/2;
    setBounds(x,y,w,h);
    validate();}
	
	/*
	 * ################################
	 * SCREEN MODE
	 * ################################
	 */
	
	private GraphicsDevice device;
	DisplayMode originaldm;
	
  private void enterFullScreenMode(){
    fullscreenmode=true;
    GraphicsEnvironment env = GraphicsEnvironment.
        getLocalGraphicsEnvironment();
    GraphicsDevice[] devices = env.getScreenDevices();
    device=devices[0];
    originaldm = device.getDisplayMode();
    boolean isfullscreenable = device.isFullScreenSupported();
    //get rid of the frame decor
    setVisible(false);
    dispose();
    setUndecorated(isfullscreenable);
    setVisible(true);
    //
    setResizable(!isfullscreenable);
    //hide controls
    pancontrol.setVisible(false);
    //get the focus on the image panel so we can exit on keypress
    panimage.requestFocus();
    //do fullscreen
    if(isfullscreenable){
      device.setFullScreenWindow(this);
      pancontrol.setVisible(false);
    //can't do fullscreen so do this other thing
    }else{
      setVisible(true);}
    validate();
    hideMouse();}
  
  private void exitFullScreenMode(){
    fullscreenmode=false;
    device.setDisplayMode(originaldm);
    //put the frame decor back
    setVisible(false);
    dispose();
    setUndecorated(false);
    setVisible(true);
    //show the controls
    pancontrol.setVisible(true);
    //
    setDefaultWindowBounds();
    //
    showMouse();
    //
    setResizable(true);}
  
  //-----------------------------------------------
  //MOUSE CURSOR
  
  Cursor originalmousecursor;
  
  private void hideMouse(){
    originalmousecursor=getContentPane().getCursor();
    BufferedImage cursorimage=new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
    //create blank cursor.
    Cursor blankCursor= 
      Toolkit.getDefaultToolkit().createCustomCursor(
      cursorimage, new Point(0, 0), "blank cursor");
    //blank cursor
    getContentPane().setCursor(blankCursor);}
  
  private void showMouse(){
    getContentPane().setCursor(originalmousecursor);}
}

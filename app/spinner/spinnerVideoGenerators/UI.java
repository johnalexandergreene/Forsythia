package org.fleen.forsythia.app.spinner.spinnerVideoGenerators;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class UI extends JFrame {

  public Viewer viewer;

	public UI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50,50,1200,800);
		viewer=new Viewer();
		setContentPane(viewer);
		setVisible(true);}
	
}

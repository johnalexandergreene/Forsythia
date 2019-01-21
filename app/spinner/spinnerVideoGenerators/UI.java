package org.fleen.forsythia.app.spinner.spinnerVideoGenerators;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.fleen.forsythia.app.spinner.FSLAFGenerator;

public class UI extends JFrame {

	private static final long serialVersionUID = -2749846443106819716L;
	
	FSLAFGenerator generator;
  public Viewer viewer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI frame = new UI(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public UI(FSLAFGenerator generator){
	  this.generator=generator;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setExtendedState(MAXIMIZED_BOTH);
		setBounds(50,50,1500,800);
		viewer=new Viewer(generator);
		setContentPane(viewer);
		setVisible(true);}
	
}

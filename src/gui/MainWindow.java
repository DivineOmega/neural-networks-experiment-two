package gui;

import javax.swing.JFrame;

public class MainWindow extends JFrame
{
	public DrawPane drawPane;

	public MainWindow() 
	{
		drawPane = new DrawPane();
		add(drawPane);
		
		setResizable(false);
		setSize(600, 600);
		setTitle("Neural Networks - Experiment One");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
}

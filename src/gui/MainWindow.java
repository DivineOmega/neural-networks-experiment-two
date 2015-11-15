package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.Main;

public class MainWindow extends JFrame implements KeyListener
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
		
		addKeyListener(this);
	}
	
	public void keyPressed(KeyEvent e)
	{
	    if (e.getKeyChar()=='+')
	    {
	    	Main.simulationSpeedUp();
	    }
	    else if (e.getKeyChar()=='-')
	    {
	    	Main.simulationSpeedDown();
	    }
	    else if (e.getKeyChar()=='0')
	    {
	    	Main.simulationSpeedReset();
	    }
	}

	public void keyReleased(KeyEvent arg0) 
	{
		
	}

	public void keyTyped(KeyEvent arg0) 
	{
		
	}
	
}

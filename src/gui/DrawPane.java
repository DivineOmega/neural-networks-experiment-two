package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class DrawPane extends JPanel
{
	public Image image;

	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.blue);
        
        g2d.drawImage(image, 0, 0, this);
	}
}

package main;

import java.util.Random;

public class Creature 
{
	public double x;
	public double y;
	public double angle;
	public double diameter = 15.0;
	public double moveRate = 1.5;
	
	public Creature() 
	{
		Random random = new Random();
		
		x = random.nextInt(600);
		y = random.nextInt(600);
		angle = random.nextDouble()*(Math.PI*2);
	}
	
	public void checkPosition()
	{
		if (x>600) {
			x -= 600;
		}
		
		if (x<0) {
			x += 600;
		}
		
		if (y<0) {
			y += 600;
		}
		
		if (y>600) {
			y -= 600;
		}
	}
	
	public void testRandomAngle()
	{
		Random random = new Random();
		
		angle += -((Math.PI*2)*0.02) + (random.nextDouble()*((Math.PI*2)*0.04));
	}
	
	public void testMoveForward()
	{
		x += (moveRate * Math.sin(angle));
		y += (moveRate * Math.cos(angle));
		
		checkPosition();
	}
}


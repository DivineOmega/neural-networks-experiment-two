package worldObjects;

import java.util.Random;

public class Creature 
{
	public double x;
	public double y;
	public double angle;
	public double diameter = 15.0;
	public double moveRate = 1.5;
	public double energy = 1;
	
	public Creature() 
	{
		Random random = new Random();
		
		x = random.nextInt(600);
		y = random.nextInt(600);
		angle = random.nextDouble()*(Math.PI*2);
	}
	
	public void randomizeMoveRate()
	{
		Random random = new Random();
		moveRate = random.nextDouble() * 3.0;
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
	
	public void adjustAngleRandomly()
	{
		Random random = new Random();
		
		double randomOffset = -((Math.PI*2)*0.02) + (random.nextDouble()*((Math.PI*2)*0.04));
		adjustAngle(randomOffset);
	}
	
	public void adjustAngle(double offset)
	{
		angle += offset;
	}
	
	public void testMoveForward()
	{
		x += (moveRate * Math.sin(angle));
		y += (moveRate * Math.cos(angle));
		
		checkPosition();
	}

	public void reduceEnergy()
	{
		energy -= 0.001;
	}
	
	public boolean isDead()
	{
		return (energy<=0);
	}
}


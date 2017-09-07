package worldObjects;

import java.util.Random;

public class PlayerBullet
{
	public double x;
	public double y;
	public double diameter = 6.0;
	public double angle;
	public double moveRate = 0.2;
	
	public PlayerBullet(Creature targetCreature) 
	{
		Random random = new Random();
		
		x = random.nextInt(800);
		y = random.nextInt(800);
		
		angle = Math.atan2(targetCreature.x - x, targetCreature.y - y);
		
		//angle = random.nextDouble()*(Math.PI*2);
	}
	
	public void checkPosition()
	{
		while (x>800) 
		{
			x -= 800;
		}
		
		while (x<0) 
		{
			x += 800;
		}
		
		while (y>800) 
		{
			y -= 800;
		}
		
		while (y<0) 
		{
			y += 800;
		}
	}
	
	public void moveForward()
	{
		x += (moveRate * Math.sin(angle));
		y += (moveRate * Math.cos(angle));
		
		checkPosition();
	}
	
	public void tick()
	{	
		moveForward();
	}
}

package main;

import java.util.Random;

public class Creature 
{
	public int x;
	public int y;
	
	public Creature() 
	{
		Random random = new Random();
		
		x = random.nextInt(600);
		y = random.nextInt(600);
	}
	
	public void testRandomMovement()
	{
		Random random = new Random();
		
		x += -1 + random.nextInt(3);
		y += -1 + random.nextInt(3);
	}
}


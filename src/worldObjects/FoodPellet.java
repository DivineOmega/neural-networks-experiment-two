package worldObjects;

import java.util.Random;

public class FoodPellet
{
	public double x;
	public double y;
	public double diameter = 6.0;
	
	public FoodPellet() 
	{
		Random random = new Random();
		
		x = random.nextInt(600);
		y = random.nextInt(600);
	}
}

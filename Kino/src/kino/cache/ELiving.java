package kino.cache;

public class ELiving extends Entity {
	public static int maxDeathTicks = 20;
	public int deathTicks = -1;
	public int health = 10;
	public int maxHealth = 10;
	public void tickELiving() {
		if(deleted)
			return;
		if(health<=0)
		{
			if(deathTicks>=maxDeathTicks)
				deleted = true;
			deathTicks++;
		}
		else
			deathTicks = -1;
		
	}
	@Override
	public void tick() {
		tickELiving();
		super.tick();
	}
}

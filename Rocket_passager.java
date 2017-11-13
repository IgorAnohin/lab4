public class Rocket_passager extends Human implements Movable
{
	private RocketKnowledge knowledge;
	public Rocket_passager(String name, RocketKnowledge knowledge, Status status, Room place)
	{
	super(name, status);
	this.knowledge = knowledge;
	this.place = place;
	}

	public RocketKnowledge getRocketKnowledge()
	{
	return knowledge; 
	} 

	public Room getPlace()
	{
	return place;
	}

	@Override
	public void move()
	{
		place = place.getNextRoom();
		System.out.printf("%s перемещается\nТекущее местоположение: %s\n", name, place.getPlaceName());		
	}

	@Override
	public void up_stairs()
	{
		if (place.check_item("stairs"))
			System.out.printf("%s поднимается по леcтнице\n", name);		
	}
		
}

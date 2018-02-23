public class Lift extends Room
{
	final boolean UP = true;
	final boolean DOWN = false;
	Boolean status_move = DOWN;
	public Lift(String name, Room next_room, double chance_go_out, String  ... items)
	{
		super(name, next_room, chance_go_out, items); 
	}

	public Lift(String name, String ... items)
	{
		super(name, items);
	}

	public Lift(String name, Room next_room, Room next_room2, double chance_go_out, String  ... items)
	{
		super(name, next_room, next_room2, chance_go_out, items);
	}


	@Override
	public Room getNextRoom()
	{
		if (status_move == DOWN) {
			if (next_room == null)
				System.out.println("Ехать некуда.");
				return next_room;
		} else {
			if (next_room2 == null)
				System.out.println("Ехать некуда.");
			return next_room2;
		}
	}

	@Override
	public void room_activity(Rocket_passager Passager)
	{
		System.out.printf("%s нажимает кнопку лифта.",Passager.getName());
		if (status_move == DOWN)
		{
			System.out.println("(Лифт поедет вверх)");
			status_move = UP;
		} else
		{
			System.out.println("(Лифт поедет вниз)");
			status_move = DOWN;
		}
		if (Passager.getKnowledge() == RocketKnowledge.BAD)
			System.out.printf("%s не замечает куда поедет лифт...\n",Passager.getName());
		else
			System.out.printf("%s знает куда поедет лифт...\n",Passager.getName());
	}
} 
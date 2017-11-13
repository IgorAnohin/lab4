public class Rocket_engine extends Device
{
	Rocket_engine(String name) {super(name);}
	Rocket_engine(String name,Room room) {super(name, room);}

	@Override
	public void Set_power()
	{
		if (power == Power.OFF)
		{
			power = Power.ON;
			System.out.printf("%s в комнате %s начал издавать мерный шум...\n", name, room.getPlaceName());
		}
		else {
			power = Power.OFF;
			System.out.printf("%s в %s выключился\n", name,room.getPlaceName());
		}
	}
	
	@Override
	public String toString()
	{
		return ("Это реактивный двигатель");
	}

} 

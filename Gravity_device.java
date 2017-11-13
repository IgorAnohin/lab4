public class Gravity_device extends Device
{
	Gravity_device(String name) {super(name); }
	Gravity_device(String name, Room room) {super(name, room); }

	@Override
	public void Set_power()
	{
		if (power == Power.OFF)
		{
			System.out.printf("%s сейчас работает\n" ,name);
			System.out.printf("Гравитация в %s выключена\n", room.getPlaceName());
			power = Power.ON;
			//выключить гравитацию на ракете
		} else {
			System.out.printf("Гравитация в %s включена\n" , room.getPlaceName());
			power = Power.OFF;
		}
	}

	public String Get_gravity()
	{
		if (power == Power.OFF)
			return ("Гравитация здесь включена");
		else
			return("Гравитация здесь выключена");
	}

}

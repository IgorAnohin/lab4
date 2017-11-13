public class Device implements Power_interface
{
	protected Power power;
	protected String name;
	protected Room room;

	public Device(String name, Room whereis)
	{
		this.name = name;
		room = whereis;
		System.out.printf("Устройство: \"%s\" создано! Оно расположено в %s\n", name, whereis.getPlaceName());
		power = Power.OFF;
	}
	public Device(String name)
	{
		this.name = name;
		room = null;
		System.out.printf("Устройство: \"%s\" создано!\n", name);
		power = Power.OFF;
	}

	public Power Get_power()
	{
		return power;
	}

	public Room Get_room()
	{
		return room;
	}

	public void Set_room(Room new_room)
	{
		room = new_room;	
	}
	
	@Override
	public void Set_power()
	{
		if (power == Power.OFF)
		{
			System.out.printf("%s в %s сейчас работает\n" ,name, room.getPlaceName());
			power = Power.ON;
		}
		else
		{
			power = Power.OFF;
			System.out.printf("%s выключено\n" ,name);
		}
	}
	
	@Override
	public String toString()
	{
		return("Этот девайс - " + name);
	}

}

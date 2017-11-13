import java.util.ArrayList;
public class Rocket
{

	private ArrayList<Rocket_passager> Rocket_passageres;
        private ArrayList<Room> Rooms;
        private ArrayList<Device> Devices;

	Rocket(ArrayList<Rocket_passager> Rocket_passageres, ArrayList<Room> Rooms, ArrayList<Device> Devices)
	{
		this.Rooms = Rooms;
		this.Devices = Devices;
		this.Rocket_passageres = Rocket_passageres;
	}	

	@Override
	public String toString()
	{
		return "Rocket";
	}

	
}

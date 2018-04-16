import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.LinkedList;

@XmlRootElement
public class Rocket implements Serializable
{

	private LinkedList<Rocket_passager> Rocket_passageres;
	private LinkedList<Room> Rooms;
    private LinkedList<Device> Devices;

    Rocket()
	{}

	Rocket(LinkedList<Rocket_passager> Rocket_passageres, LinkedList<Room> Rooms, LinkedList<Device> Devices)
	{
		this.Rooms = Rooms;
		this.Devices = Devices;
		this.Rocket_passageres = Rocket_passageres;
	}

	@XmlElement(name = "passager")
	public void setRocket_passageres(LinkedList<Rocket_passager> rocket_passageres)
	{
		Rocket_passageres = rocket_passageres;
	}
	public LinkedList<Rocket_passager> getRocket_passageres()
	{
		return Rocket_passageres;
	}

	public LinkedList<Room> getRooms() {return Rooms;}

	@Override
	public String toString()
	{
		return "Rocket";
	}
}

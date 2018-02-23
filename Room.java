import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
public class Room
{
	protected double chance_go_out;
	protected String name;
	protected Room next_room;
	protected Room next_room2;
	protected HashSet<Room> more_next_rooms;
	protected Bottom bottom;
	protected Map<String, Boolean> items = new HashMap<> ();
	static Random random = new Random();
{
	next_room2 = null;
	items.put("stairs", false);
	items.put("botton", false);
	items.put("table", false);
	items.put("tumbler_light",false);
}
	public void addBottom(Bottom bottom)
	{
		this.bottom = bottom;
	}

	public void room_activity(Rocket_passager Passager)
	{
		if (bottom != null)
		{
			System.out.printf("%s Нажимает кнопку\n", Passager.getName());
			bottom.TouchBottom();
			System.out.println("Дверь открывается!");		
		}
		System.out.println("Делать в комнате нечего");
	}

	public boolean check_item(String item)
	{
		return items.get(item);
	}

	public Room getNextRoom()
	{
	    int pick = random.nextInt(more_next_rooms.size());
	    for (Room try_to_find : more_next_rooms) {
	    	pick--;
	    	if (pick <= 0)
				return try_to_find;
		}
		return null;
	}

	@XmlElement
	public String getPlaceName()
	{
		return name;
	}
	public void setPlaceName(String name)
	{
		this.name = name;
	}

	public Room()
	{}

	public Room(String name, Room next_room, Room next_room2, double chance_go_out, String  ... items)
	{
	    more_next_rooms = new HashSet<>();
		this.name = name;
		System.out.printf("Создаю помещение: %s\n" , name);
		this.next_room = next_room;
		this.next_room2 = next_room2;
		if (next_room != null) {
			System.out.println("комната соединена с " + next_room.getPlaceName());
			more_next_rooms.add(next_room);
		}
		if (next_room2 != null && !next_room.equals(next_room2)) {
			System.out.println("Из этой комнаты можно попасть " + next_room2.getPlaceName());
			more_next_rooms.add(next_room2);
		}
		if (next_room == null && next_room2 == null)
			System.out.println("Попасть из этой комнаты никуда нельзя");
		System.out.println();
		this.chance_go_out = chance_go_out;
		for (String item : items)
			if (this.items.containsKey(item) )
				this.items.put(item, true);
	}

	public Room(String name, Room next_room, Room next_room2, double chance_go_out, Bottom bottom, String  ... items)
	{
		this.name = name;
		System.out.printf("Создаю помещение: %s\n" , name);
		this.next_room = next_room;
		this.next_room2 = next_room2;
		this.chance_go_out = chance_go_out;
		this.bottom = bottom;
		for (String item : items)
			if (this.items.containsKey(item) )
				this.items.put(item, true);
	}


	public Room(String name, Room next_room, double chance_go_out, String  ... items)
	{
		this.name = name;
		System.out.printf("Создаю помещение: %s\n" , name);
		this.next_room = next_room;
		this.chance_go_out = chance_go_out;
		for (String item : items)
			if (this.items.containsKey(item) )
				this.items.put(item, true);
		more_next_rooms.add(next_room);
	}

	public Room(String name, String ... items)
	{
		chance_go_out = 1;
		this.name = name;
		System.out.printf("Создаю помещение: %s\n" , name);
		for (String item : items)
			if (this.items.containsKey(item) )
				this.items.put(item, true);
	}
	@XmlElement
	public double getChance_go_out()
	{
		return chance_go_out;
	}
	public void setChance_go_out(double chance)
	{
		this.chance_go_out = chance;
	}

	public boolean go_out()
	{
		if (Math.random() > chance_go_out)
			return false;
		else
			return true;
	}

	@Override
	public int hashCode()
	{
		int hashcode = 1;
		if (items.get("table") )
			hashcode *= 2;
		if (items.get("stairs") )
			hashcode *= 3;
		if (items.get("botton") )
			hashcode *= 5;
		if (items.get("tumbler_light") )
			hashcode *= 7;
		return hashcode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass() )
			return false;
		Room other = (Room) obj;
		if (!items.equals(other.items) )
			return false;
		return true;
	}

	public void set_new_next_room(Room room)
	{
		more_next_rooms.add(room);
	}

	@Override
	public String toString()
	{
		return "Это команата!!! " + this.name;
	}

	public boolean comparebyname(String name) {return (this.name == name);}
	public String getName() {return this.name;}

	@XmlElement
	public Room getNext_room1() {return next_room;}
	public void setNext_room1(Room next_room) {this.next_room = next_room;}
	@XmlElement
	public Room getNext_room2() {return next_room2;}
	public void setNext_room2(Room next_room) {this.next_room2 = next_room;}

}
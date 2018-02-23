import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;

@XmlRootElement
class Room_list
{
    LinkedList<Room> rooms;
	Room_list() {}

	@XmlElement
	public LinkedList<Room> getRooms() {return rooms;}
	public void setRooms(LinkedList<Room> rooms){this.rooms = rooms;}
}

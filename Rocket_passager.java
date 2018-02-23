import com.sun.org.apache.xpath.internal.operations.Equals;
import com.sun.tracing.dtrace.FunctionName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;

@XmlRootElement(name = "passager")
public class Rocket_passager extends Human implements Movable
{
	private HashSet<Room> be_here;
	private RocketKnowledge knowledge;

	public Rocket_passager(String name, RocketKnowledge knowledge, Status status, Room place)
	{
		super(name, status);
		be_here = new HashSet<>();
		this.knowledge = knowledge;
		this.place = place;
	}

	public Rocket_passager()
	{}

	public RocketKnowledge getKnowledge()
	{
	return knowledge; 
	}
	public void setKnowledge(RocketKnowledge knowledge)
	{
		this.knowledge = knowledge;
	}

	@Override
	public void move()
	{
		be_here.add(place);
		place = place.getNextRoom();
		System.out.printf("%s перемещается\nТекущее местоположение: %s\n", name, place.getPlaceName());
	}

	@Override
	public void up_stairs()
	{
		if (place.check_item("stairs"))
			System.out.printf("%s поднимается по леcтнице\n", name);		
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
		Rocket_passager other = (Rocket_passager) obj;
		if (!(this.name==other.getName() && this.knowledge == other.getKnowledge() &&
				this.status == other.getStatus() && this.place == other.getPlace()) )
			return false;
		return true;
	}
	public boolean already_be_here()
	{
		return be_here.contains(place);
	}
}

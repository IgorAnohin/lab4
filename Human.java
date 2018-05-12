import java.io.Serializable;
import java.time.LocalTime;

public class Human extends Animal implements Serializable
{
	protected Size size;
	protected Room place;
	protected LocalTime create_time;
	protected Color color;

	Human(){
		this.size = Size.NORMAL;
		this.color = Color.GREEN;
	}

	Human(String name, Status status, Room place)
	{
		this.place = place;
		this.name = name;
		this.status = status;
		double chance = Math.random();
		if (chance < 0.33)
			this.size = Size.NORMAL;
		else if (chance < 0.66)
			this.size = Size.BIG;
		else
			this.size = Size.LITTLE;
		create_time = LocalTime.now();
		System.out.printf("Персонаж: \"%s\" создан\n", name);
		System.out.println("Его размер " + size);
		System.out.println("Время создания: ");
		System.out.println();
	}

	Human(String name, Status status)
	{
		this.name = name;
		this.status = status;
		this.size = Size.NORMAL;
		create_time = LocalTime.now();
		System.out.printf("Персонаж: \"%s\" создан\n", name);
		System.out.println("Его размер " + size);
		System.out.println("Время создания: ");
		System.out.println();
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name) {this.name = name;}

	public Status getStatus()
	{
		return status;
	}
	public void setStatus(Status status)
	{	
		this.status = status;
	}

	public Room getPlace()
	{
		return place;
	}
	public void setPlace(Room place)
	{
		this.place = place;
	}

	public Size getSize() {return size;}
	public void setSize(Size size) {this.size = size;}
	public Color getColor() {return color;}
	public void setColor(Color color) {this.color = color;}

}


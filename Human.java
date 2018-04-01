import java.time.LocalTime;

public class Human extends Animal
{
	protected Size size;
	protected Room place;
	protected LocalTime create_time;

	Human(){
		this.size = Size.NORMAL;
		create_time = java.time.LocalTime.now();
	}

	Human(String name, Status status, Room place)
	{
		this.place = place;
		this.name = name;
		this.status = status;
		this.size = Size.NORMAL;
		create_time = java.time.LocalTime.now();
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
		create_time = java.time.LocalTime.now();
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
	public LocalTime getCreate_time() {return create_time;}

}


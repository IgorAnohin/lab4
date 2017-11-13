public class Human extends Animal
{
	protected Room place;
	Human(){}

	Human(String name, Status status, Room place)
	{
		this.place = place;
		this.name = name;
		this.status = status;
		System.out.printf("Персонаж: \"%s\" создан\n", name);
	}

	Human(String name, Status status)
	{
		this.name = name;
		this.status = status;
		System.out.printf("Персонаж: \"%s\" создан\n", name);
	}
	public String getName()
	{
		return name;
	}

	public void setStatus(Status status)
	{	
		this.status = status;
	}

	public Room getRoom()
	{
		return place;
	}

	public Status getStatus()
	{	
		return status;
	}
}


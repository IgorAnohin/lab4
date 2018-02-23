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

}


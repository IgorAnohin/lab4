import java.util.ArrayList;

class SilentHill extends Exception{

	SilentHill(String name) {
		super(name);
		System.out.println("С людьми в городе что-то не так");
	}

}


public class Situation
{
	static long passager_number = Math.round(Math.random()*4 + 1);
	static long room_count = Math.round(Math.random()*5 + 5);
	static long device_count = Math.round(Math.random()*room_count);

	static Space_town town;
	static Rocket Rockkket;

	static ArrayList<Rocket_passager> Rocket_passageres = new ArrayList<>();
	static ArrayList<Room> Rooms = new ArrayList<>();
	static ArrayList<Device> Devices = new ArrayList<>();


	public static void main(String args[])
	{
		//Anonim class add
		Bottom start = new Bottom(
				new Power_interface()
				{
					@Override
					public void Set_power() {
						System.out.println("Это ракета например(анонимная ракета между прочим)");
						System.out.println("Кнопка запуска модуляции нажата, можно начинать...");
						System.out.println();
					}
				}

		);

		start.TouchBottom();

		for (int i = 0; i < passager_number; i++)
		{
			//Local exeption Class
			class nadoelo_xodit extends NullPointerException {
				private Room stop_room;
				nadoelo_xodit(Room stop_room)
				{
					super();
					this.stop_room = stop_room;
				}

				public void where_stop()
				{
					System.out.printf("Путь закончился в локации: %s\n",stop_room.getPlaceName() );
				}
			}

			//Do nothing if passanger sleep
			if (Rocket_passageres.get(i).getStatus() == Status.SLEEP) {
				System.out.println(Rocket_passageres.get(i).getName() + " спит и некуда не пойдёт");
				continue;
			}

			System.out.printf("Затаив дыхание, подопытный номер %s отправляется в свой нелегкий путь.\n",
					Rocket_passageres.get(i).getName());
			System.out.printf("Текущее местоположение: %s\n\n", Rocket_passageres.get(i).getPlace().getPlaceName());
			try
			{
				while (true)
				{
					if (Rocket_passageres.get(i).getPlace().getNextRoom() == null) throw new nadoelo_xodit(Rocket_passageres.get(i).getPlace());

					else
						//if Knowledge Bad have chance stay this some time
						if (Rocket_passageres.get(i).getRocketKnowledge() == RocketKnowledge.BAD)
					    {
					    	//While can't go from this room
							while (!Rocket_passageres.get(i).getPlace().go_out()) {
								Rocket_passageres.get(i).setStatus(Status.NERVOUS);
								System.out.printf("%s не может найти двери и обходить комнату вокруг\n", Rocket_passageres.get(i).getName());

								System.out.printf("%s теряет самообладание. Опять...\n", Rocket_passageres.get(i).getName());
							}
							if (Rocket_passageres.get(i).getPlace().getChance_go_out() != 1)
								System.out.printf("%s наконец-то находит дверь\n", Rocket_passageres.get(i).getName());
						}
						//up_stairs if room have stairs
						Rocket_passageres.get(i).up_stairs();
						//activity with Bottom in Room
						Rocket_passageres.get(i).getPlace().room_activity(Rocket_passageres.get(i));
						if (Rocket_passageres.get(i).getPlace().getNextRoom() == null) throw new nadoelo_xodit(Rocket_passageres.get(i).getPlace());

						//GO to the next room
						Rocket_passageres.get(i).move();
						System.out.println();
					}
			} catch (nadoelo_xodit e) //Если некуда идти
			{
				e.where_stop();
				System.out.println("Комнат, куда можно пойти, больше нет. А возвращаться назад неохото...");
				System.out.println("Посмотрим, пойдёт ли куда-нибудь кто-то ещё");
				System.out.println();
			}
		}

	}

	static {
		System.out.println();
		System.out.println("Генерируем карту:");

		//create space town with random people
		town = new Space_town();

		//check, that people in town > 0 else exeption
		int last_men;
		try {
			last_men = town.civil_count() - 1;
			if (last_men == -1) throw new SilentHill(town.toString());
			Space_town.town_civilian x = town.getpeople(last_men);
			System.out.println("Номер " + x.getNumber() + " знал что в ракете что-то происходит");
		} catch (SilentHill e)
		{
			System.out.println(e);
			System.out.println("Никто тут не живёт, город пуст");
		}

	System.out.println();

	for (int i = 0; i < room_count; i++)
	{
		//Random next room count (0..2) and create this rooms
		long next_room_count = Math.round(Math.random()*2);
		Room RandomRoom1,RandomRoom2;
		if (next_room_count == 0)
			RandomRoom1 = RandomRoom2 = null;
		else if (next_room_count == 1){
			RandomRoom1 = getRandomRoom(Rooms); RandomRoom2 = null;
		} else {
			RandomRoom1 = getRandomRoom(Rooms);
			RandomRoom2 = getRandomRoom(Rooms);
		}

		//create Random room
		double chance = Math.random();
		if (chance < 0.2)
			Rooms.add(new Lift("Лифт "+i, RandomRoom1, RandomRoom2, Math.random()));
		else
			Rooms.add(new Room("Комната "+i,RandomRoom1, RandomRoom2, Math.random()) );
	}

			System.out.println();
	System.out.println("Готовим героев:");

	//create Random count passanger with random location, knowledge and status
	for (int i = 0; i < passager_number; i++)
	{
		RocketKnowledge knowledge = (Math.random() > 0.5) ? RocketKnowledge.BAD : RocketKnowledge.GOOD;
		Status stat = (Math.random() > 0.3) ? Status.AWAKE : Status.SLEEP;
		int location = (int)Math.round(Math.random() * (room_count-1) );
		Room location_this = Rooms.get(location);
		Rocket_passageres.add(new Rocket_passager(String.valueOf(i), knowledge ,stat, location_this) );
	}


	System.out.println("Еще немного устройтв:");

	//Create random device count
	for (int i = 0; i < device_count; i++)
	{
		//Create Random Device in random location
		double chance = Math.random();
		if (chance < 0.4)
			Devices.add(new Device("Устройство " + i, getRandomRoom(Rooms)));
		 else if (chance < 0.6)
			Devices.add(new Rocket_engine("Двигатель " + i, getRandomRoom(Rooms)));
		 else if (chance < 0.8)
			Devices.add(new Gravity_device("Прибор невесомости " + i, getRandomRoom(Rooms)));
		 else
			Devices.add(new Gravity_device("Выключатель света " + i, getRandomRoom(Rooms)));

		//each Divese have Bottom in Random Room
		getRandomRoom(Rooms).addBottom(new Bottom(Devices.get(i)));
	}
	Rockkket = new Rocket(Rocket_passageres, Rooms, Devices);

	System.out.printf("Иницилизация закончена.\n\n");
	}

	static private Room getRandomRoom(ArrayList<Room> rooms)
	{
		if (rooms.size() < 1 ) return null;
		int i = (int)Math.round (Math.random() * (rooms.size()-1) );
		return  rooms.get(i);
	}
}

import java.util.LinkedList;
import java.util.Random;

class Situation
{
	static long passager_number = Math.round(Math.random()*4 + 1);
	static long room_count = Math.round(Math.random()*5 + 5);
	static long device_count = Math.round(Math.random()*room_count);

	static Rocket rocket;

	static LinkedList<Rocket_passager> Rocket_passageres = new LinkedList<>();
	static LinkedList<Room> Rooms = new LinkedList<>();
	static LinkedList<Device> Devices = new LinkedList<>();


	public Situation(String path_to_passagers, String path_to_rooms)
	{
		rocket = initialize(path_to_passagers, path_to_rooms);
		print_passageres();
		start_simulation();
	}
	public Situation()
	{
		rocket = initialize();
		print_passageres();
		start_simulation();
	}


	public static void print_passageres()
	{
		LinkedList<Rocket_passager> list = rocket.getRocket_passageres();
		for (Rocket_passager  passager: list) {
			System.out.println("Passager: "+ passager);
		}
	}

	public static void start_simulation()
	{

		//Anonim class add
		Bottom start = new Bottom(
				new Power_interface() {
					@Override
					public void Set_power() {
						System.out.println("Это ракета например(анонимная ракета между прочим)");
						System.out.println("Кнопка запуска модуляции нажата, можно начинать...");
						System.out.println();
					}
				}
		);

		start.TouchBottom();

		for (int current_passager = 0; current_passager < passager_number; current_passager++)
		{
            //Do nothing if passanger sleep
            if (Rocket_passageres.get(current_passager).getStatus() == Status.SLEEP) {
                System.out.println(Rocket_passageres.get(current_passager).getName() + " спит и некуда не пойдёт");
            } else {
				new Thread(Rocket_passageres.get(current_passager)).start();
            }
		}
	}


	private Rocket initialize() {
		System.out.println();
		System.out.println("Генерируем карту:");

		show_little_town();

		create_rooms();
		create_heroes();
		create_devices();
		System.out.printf("Иницилизация закончена.\n\n");
		Interactive_mode.writer_passageres(Rocket_passageres);
		Room_list rooms = new Room_list();
		rooms.setRooms(Rooms);
		Interactive_mode.write_to_xml(rooms, Interactive_mode.collection_rooms_path);
		return new Rocket(Rocket_passageres, Rooms, Devices);
	}


	private Rocket initialize(String path_to_passageres, String path_to_romms) {
		System.out.println();
		System.out.println("Генерируем карту:");

		show_little_town();

		Room_list rooms = Interactive_mode.read_from_xml(Room_list.class, path_to_romms);
		Rooms = rooms.getRooms();
		room_count = Rooms.size();
		for(Room room : Rooms) {
			for (Room nextroom : Rooms) {
				if ((room.getNext_room1() != null) && (room.getNext_room1().getName().equals(nextroom.getName()))) {
					room.setNext_room1(nextroom);
					room.set_new_next_room(nextroom);
				}
				if ((room.getNext_room2() != null) && (room.getNext_room2().getName().equals(nextroom.getName()))) {
					room.setNext_room2(nextroom);
					room.set_new_next_room(nextroom);
				}
			}
			System.out.println(room +" " + room.getNext_room1());

			if (room.getNext_room1() != null) room.getNext_room1().set_new_next_room(room);
			if (room.getNext_room2() != null) room.getNext_room2().set_new_next_room(room);
		}

		Rocket rocket = Interactive_mode.read_from_xml(Rocket.class, path_to_passageres);

		Rocket_passageres = rocket.getRocket_passageres();
		for(Rocket_passager passager : Rocket_passageres)
			for(Room room : Rooms)
				if (room.getName().equals(passager.getPlace().getName())) {
					passager.setPlace(room);
				}

		passager_number = Rocket_passageres.size();

		create_devices();
		System.out.printf("Иницилизация закончена.\n\n");
		return new Rocket(Rocket_passageres, Rooms, Devices);
	}

	private void show_little_town() {
		try {
			get_indofmation_about_rocket_in_town(new Space_town());
		} catch (SilentHill e) {
			System.out.println(e);
			System.out.println("Никто тут не живёт, город пуст");
			System.out.println();
		}
	}

	static void get_indofmation_about_rocket_in_town( Space_town town) throws SilentHill
	{
        int last_men = town.civil_count() - 1;
		if (last_men == -1) throw new SilentHill(town.toString());

		Space_town.town_civilian civilian = town.getpeople(last_men);
		System.out.println("Номер " + civilian.getNumber() + " знал что в ракете что-то происходит");

		System.out.println();
	}

	private static void create_rooms()
	{
		for (int room_number = 0; room_number < room_count; room_number++)
		{
			Room [] Next_rooms = generate_next_rooms();
			Room new_room = init_room(room_number, Next_rooms[0], Next_rooms[1]);
			Rooms.add(new_room);
			if (Next_rooms[0] != null) Next_rooms[0].set_new_next_room(new_room);
			if (Next_rooms[1] != null) Next_rooms[1].set_new_next_room(new_room);
		}
		System.out.println();
	}

	private static Room [] generate_next_rooms()
	{
		Room [] Next_rooms = new Room[2];
		Random next_rooms = new Random();
		switch (next_rooms.nextInt(4))
		{
			case 0:
				Next_rooms[0] = Next_rooms[1] = null;
				break;
			case 1:
				Next_rooms[0] = getRandomRoom(Rooms); Next_rooms[1] = null;
				break;
			default:
				Next_rooms[0] = getRandomRoom(Rooms);
				Next_rooms[1] = getRandomRoom(Rooms);
		}
		return Next_rooms;
	}

	private static Room init_room(int room_number, Room NextRoom1, Room NextRoom2)
	{
		return Math.random() < 0.2 ? new Lift("Лифт "+room_number, NextRoom1, NextRoom2, Math.random()) :
				new Room("Комната "+room_number,NextRoom1, NextRoom2, Math.random());
	}

	private static void create_heroes()
	{
		System.out.println("Готовим героев:");
		//create Random count passanger with random location, knowledge and status
		for (int i = 0; i < passager_number; i++)
		{
			RocketKnowledge knowledge = (Math.random() > 0.5) ? RocketKnowledge.BAD : RocketKnowledge.GOOD;
			Status stat = (Math.random() > 0.3) ? Status.AWAKE : Status.SLEEP;
			int location = new Random().nextInt((int)(room_count-1));
			Room location_this = Rooms.get(location);
			Rocket_passageres.add(new Rocket_passager(String.valueOf(i), knowledge ,stat, location_this) );
		}
		System.out.println();
	}

	private static void create_devices()
	{
		System.out.println("Еще немного устройтв:");

		//Create random device count
		for (int device_number = 0; device_number < device_count; device_number++)
		{
			//Create Random Device in random location
			Devices.add(get_device(device_number));
			//each Divese have Bottom in Random Room
			getRandomRoom(Rooms).addBottom(new Bottom(Devices.get(device_number)));
		}
	}

	private static Device get_device(int device_number)
	{
		double chance = Math.random();
		if (chance < 0.4)
			return(new Device("Устройство " + device_number, getRandomRoom(Rooms)));
		else if (chance < 0.6)
			return(new Rocket_engine("Двигатель " + device_number, getRandomRoom(Rooms)));
		else if (chance < 0.8)
			return(new Gravity_device("Прибор невесомости " + device_number, getRandomRoom(Rooms)));
		else
			return(new Gravity_device("Выключатель света " + device_number, getRandomRoom(Rooms)));
	}

	static private Room getRandomRoom(LinkedList<Room> rooms)
	{
		if (rooms.size() < 1 ) return null;
		int i = (int)Math.round (Math.random() * (rooms.size()-1) );
		return  rooms.get(i);
	}
}

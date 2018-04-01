import com.sun.org.apache.xpath.internal.operations.Equals;
import com.sun.tracing.dtrace.FunctionName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@XmlRootElement(name = "passager")
public class Rocket_passager extends Human implements Movable, Runnable
{
	private HashSet<Room> be_here;
	private RocketKnowledge knowledge;


	public Rocket_passager(String name, RocketKnowledge knowledge, Status status, Room place)
	{
		super(name, status, place);
		be_here = new HashSet<>();
		this.knowledge = knowledge;
		//this.place = place;
	}

	public Rocket_passager()
	{
		super();
	}

	public RocketKnowledge getKnowledge()
	{
	return knowledge; 
	}
	public void setKnowledge(RocketKnowledge knowledge)
	{
		this.knowledge = knowledge;
	}

	@Override
	public synchronized void move()
	{
		be_here.add(place);
		place.go_from_this_room(this.getName());
		place = place.getNextRoom();

		int cannot_enter = 10;
		while (!place.can_add_people()) {
			System.out.println(name + " вынужден ждать");
			try {
				Thread.sleep(1000);

				cannot_enter--;
				if (cannot_enter <= 0)
					throw new nadoelo_walk(this.getPlace());

			} catch (InterruptedException e) {
				System.out.println(name + " не смог дождаться освобождения комнаты");
			}
		}

		System.out.printf("%s перемещается\nТекущее местоположение: %s\n", name, place.getPlaceName());

		ArrayList<String> passagers_in_room = place.here();
		if (passagers_in_room != null)
			System.out.println("!!!!!В этой комнате ещё есть " + passagers_in_room + "!!!!!");
		place.go_into_this_room(this.getName());

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
		if (!(this.name.equals(other.getName()) && this.knowledge == other.getKnowledge() &&
				this.status == other.getStatus() && this.place == other.getPlace()) )
			return false;
		return true;
	}
	public boolean already_be_here()
	{
	    if (be_here == null)
			be_here = new HashSet<>();
		return be_here.contains(place);
	}

	@Override
	public void run() {
		try {
			int cannot_enter = 10;
		    while (!place.can_add_people())
				try {
					Thread.sleep(1000);
					System.out.println(name + " пока не может появиться");
					cannot_enter--;
					if (cannot_enter <= 0) {
						System.out.println("Ну нафиг появлятья");
						return;
					}
				} catch (Exception e) {
				}

			place.go_into_this_room(name) ;
			System.out.println(name + " появился в комнате");
			this.walk();
		} catch (nadoelo_walk e) //Если некуда идти
		{
			e.where_stop();
			System.out.println("Комнат, куда можно пойти, больше нет. А возвращаться назад неохото...");
			System.out.println(name + " закончил свой путь");
			System.out.println("Посмотрим, пойдёт ли куда-нибудь кто-то ещё");
			System.out.println();
		}
	}

	private synchronized void walk() throws nadoelo_walk
    {
        System.out.printf("Затаив дыхание, подопытный номер %s отправляется в свой нелегкий путь.\n",
                this.getName());
        System.out.printf("Текущее местоположение: %s\n\n", this.getPlace().getPlaceName());

        while (true) {
			try {
				wait(2000);
			} catch (Exception e) {
				System.out.println("Человек вот не может подождать");
			}

            if (this.getPlace().getNextRoom() == null ||
                    this.already_be_here()) {
                throw new nadoelo_walk(this.getPlace());
            }
            //if Knowledge Bad have chance stay this some time
            if (this.getKnowledge() == RocketKnowledge.BAD) {
                //While can't go from this room
                this.find_output();
            }

            this.room_activities();

            //GO to the next room
            this.move();
            System.out.println();

		}

    }

    private void find_output()
    {
        while (!this.getPlace().go_out()) {
            this.setStatus(Status.NERVOUS);
            System.out.printf("%s не может найти двери и обходить комнату вокруг\n", this.getName());

            System.out.printf("%s теряет самообладание. Опять...\n", this.getName());
        }
        if (this.getPlace().getChance_go_out() != 1)
            System.out.printf("%s наконец-то находит дверь\n", this.getName());
    }

    private void room_activities()
    {
        //up_stairs if room have stairs
        this.up_stairs();
        //activity with Bottom in Room
        this.getPlace().room_activity(this);
    }

    @Override
    public String toString () {
		return "\nИмя: " + name +", знания: "+ knowledge + ", местоположение: " + place +
				"\nРазмер "+ size + ", время создания: " + create_time + "\n";
	}
}

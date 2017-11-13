import java.util.ArrayList;

public class Space_town {

    class town_civilian extends Human{
        private int number;
        town_civilian(int number){
            super();
            System.out.println("Житель городка номер, "+ number);
            this.number = number + 1;
        }

        public int getNumber()
        { return number; }

    }

    private ArrayList<town_civilian> civilians = new ArrayList<>();

    public Space_town(){
        System.out.println("Космисечкий городок создан");
        for (int i = 0; i < Math.round(Math.random()*5); i++)
            civilians.add(new town_civilian(i));
        System.out.println("В нём живёт "+civilians.size()+" человек");
    }

    public int civil_count()
    {
        return civilians.size();
    }
    public town_civilian getpeople(int i)
    {
        return civilians.get(i);
    }

    @Override
    public String toString() {
        return "Космический городок ";
    }
}

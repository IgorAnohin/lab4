class nadoelo_walk extends NullPointerException {
    private Room stop_room;
    nadoelo_walk(Room stop_room)
    {
        super();
        this.stop_room = stop_room;
    }

    public void where_stop()
    {
        System.out.printf("Путь закончился в локации: %s\n",stop_room.getPlaceName() );
    }
}

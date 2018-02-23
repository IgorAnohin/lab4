class SilentHill extends Exception{

	SilentHill(String name) {
		super(name);
		System.out.println("С людьми в городе что-то не так");
	}

}

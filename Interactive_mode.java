import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;

import java.util.Scanner;

import com.google.gson.*;
import java.util.LinkedList;

/**
 * Class for communicate with command line
 * @autor Anokhin Igor
 * @version 1.0.0
 */
public class Interactive_mode {
	private static boolean DEBUG = false;
	private static int LAST_INDEX = -1;

	private static String collection_passagers_path = "C:\\Users\\810298\\Desktop\\Documents\\pasageres.xml";
	//private static String collection_passagers_path = "passageres.xml";
	private static String collection_rooms_path = "C:\\Users\\810298\\Desktop\\Documents\\rooms.xml";
	//private static String collection_rooms_path = "rooms.xml";


	private static boolean exit = false;
	private static String help = "Please, use following command:\n" +
			"'add {element}' for add new element in collection\n" +
			"'remove {element}' for remove element from collection\n" +
			"'remove_last' for remove last element from collection\n" +
			"'remove_first' for remove first element from collection\n" +
			"'import {path}' for add new collection path\n" +
			"'start' for start simulation\n" +
			"'exit' for exit from program\n" +
			"'show' for show list with passageres\n" +
			"'save_to {path}' save list to new file\n" +
			"'debug' for add bedugging messages";

	/**
	 * Start loop, while user don't enter 'exit'
	 * @param args
	 */
	public static void main(String [] args)
	{
		Scanner input = new Scanner(System.in);
		while (!exit)
		{
			String argument = input.next();
			parse_input(input, argument);
		}
	}

	/**
	 * @param input - scanner for subparse arguments
	 * @param argument - main argument
	 */
	static void parse_input(Scanner input, String argument)
	{
		switch (argument)
		{
			case "debug":
				System.out.println("Debug enabled");
				DEBUG = true;
				break;
			case "exit":
				exit = true;
				System.out.println("Program will be close");
				break;
			case "remove_last":
			    try {
					remove_last();
					System.out.println("Last element was removed");
				} catch (NullPointerException e) {
					System.out.println("Can't open file, may be you should change path");
				}
				break;
			case "remove_first":
				try {
					remove_first();
					System.out.println("First element was removed");
				} catch (NullPointerException e) {
					System.out.println("Can't open file, may be you should change path");
				}
				break;
			case "add":
			    try {
					add_element(get_json_element(input));
					System.out.println("Element was successfully added");
				} catch (IllegalStateException e) {
					System.out.println("Incorrect input");
				} catch (NullPointerException e) {
					System.out.println("Can't open file, may be you should change path");
				}
				break;
			case "remove":
				try {
					remove_element(get_json_element(input));
					System.out.println("Element was successfully removed");
				} catch (IllegalStateException e) {
						System.out.println("Incorrect input");
				} catch (NullPointerException e) {
						System.out.println("Can't open file, may be you should change path");
				}
				break;
			case "import":
				import_path(input);
				System.out.println("Path was changed");
				break;
			case "save_to":
				try {
			    	save_to(input);
					System.out.println("Collection was successfully saved");
				} catch (NullPointerException e) {
					System.out.println("Can't open file, may be you should change path");
				}
			    break;
			case "start":
				new Situation(collection_passagers_path, collection_rooms_path);
				break;
			case "random_start":
				exit = true;
				new Situation();
				break;
			case "show":
			    try {
					System.out.println("Try to show collection");
					show_list(new Rocket(), collection_passagers_path);
				} catch (NullPointerException e) {
					System.out.println("Can't open file, may be you should change path");
				}
			default:
				System.out.println(help);
				break;
		}
	}

	/**
	 * wrapper remove method
	 * call remove with last index of array
	 */
	static void remove_last()
	{
		remove(LAST_INDEX);
	}

	/**
	 * wrapper remove method
	 * call remove with first index of array
	 */
	static void remove_first()
	{
		remove(0);
	}

	/**
	 * wrapper remove method
	 * Try to find index element.
	 * After that call remove with index this element
	 * @param passager - element for delete
	 */
	static void remove_element(Rocket_passager passager)
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		for (int i = 0; i <= rocket.getRocket_passageres().size(); i++)
			if (passager == rocket.getRocket_passageres().get(i)) {
				remove(i);
				break;
			}
	}

	/**
	 * delete elements
	 * @param index - element number
	 */
	static void remove(int index)
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		if (rocket.getRocket_passageres().size() > 0 && rocket.getRocket_passageres().size()> index) {
			if (index == LAST_INDEX) index = rocket.getRocket_passageres().size() - 1;
			rocket.getRocket_passageres().remove(index);
		} else
			System.out.println("Can't remove. Contaener empty");
		write_to_xml(rocket, collection_passagers_path);
	}

	/**
	 * Add new element into collection
	 * @param passager - element, that will be add
	 */
	static void add_element(Rocket_passager passager)
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		rocket.getRocket_passageres().add(passager);
		write_to_xml(rocket, collection_passagers_path);
	}

	/**
	 * Change path to file with collection
	 * @param input - scanner for read new path
	 */
	static void import_path(Scanner input)
	{
		String new_path = input.nextLine();
		collection_passagers_path = new_path;
	}

	static void save_to(Scanner input)
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		write_to_xml(rocket, input.nextLine());
	}

	/**
	 * Parse json input and build new passager
	 * @param input - scanner for read passager
	 *              paremetres from command line
	 * @return redy for action passager
	 */
	static Rocket_passager get_json_element(Scanner input)
	{
		Gson gson = new Gson();
		String JSON = input.nextLine();
		System.out.println(JSON);
		Rocket_passager passager = gson.fromJson(JSON, Rocket_passager.class);

		Room_list Container = read_from_xml(Room_list.class, collection_rooms_path);
		for(Room room : Container.getRooms())
			if (room.comparebyname(passager.getPlace().getName()))
				passager.setPlace(room);

		return passager;
	}

	/**
	 * Write object into file(may be new file)
	 * @param object_to_write - object for writting into file
	 * @param path - path to collection
	 * @param <T> - class, which will be write to file
	 */
	static <T> void write_to_xml( T object_to_write, String path)
	{

		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(object_to_write.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(object_to_write, file);

			if (DEBUG)
				jaxbMarshaller.marshal(object_to_write, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read information from xml file
	 * @param type - type object
	 * @param path - path to filr
	 * @param <T> type for read object
	 * @return new object from file
	 */
	static <T> T read_from_xml(Class <T> type, String path) {
		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(type);
			Unmarshaller um = jaxbContext.createUnmarshaller();

			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));

			return (T) um.unmarshal(file);

		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e){
			System.out.println();
			return null;
		}
	}

	/**
	 * Write objedct into the xml file
	 * @param passageres - this jbject will be write to file
	 */
	public static void writer_passageres(LinkedList<Rocket_passager> passageres){
		try {
			XMLOutputFactory output = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter(collection_passagers_path));

			writer.writeStartDocument("1.0");
			writer.writeStartElement("rocket");
			for (Rocket_passager passager : passageres) {
				writer.writeStartElement("passager");

				writer.writeStartElement("name");
				writer.writeCharacters(passager.getName());
				writer.writeEndElement();
				// location
				writer.writeStartElement("place");
				//place name
				writer.writeStartElement("placeName");
				writer.writeCharacters(passager.getPlace().getName());
				writer.writeEndElement();
				writer.writeEndElement();
				// Status
				writer.writeStartElement("status");
				writer.writeCharacters(passager.getStatus().toString());
				writer.writeEndElement();
				// Knowledge
				writer.writeStartElement("knowledge");
				writer.writeCharacters(passager.getKnowledge().toString());
				writer.writeEndElement();

				writer.writeEndElement();
			}
			// Закрываем корневой элемент
			writer.writeEndElement();
			// Закрываем XML-документ
			writer.writeEndDocument();
			writer.flush();
		} catch (XMLStreamException | IOException ex) {
			ex.printStackTrace();
		}
	}

	static <T> void show_list( T object_to_write, String path)
	{
		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(object_to_write.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(object_to_write, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Алгоритм,  список комнат, в которых был
 */


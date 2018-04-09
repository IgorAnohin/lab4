
import javax.annotation.processing.SupportedSourceVersion;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;

import java.util.Scanner;
import java.net.*;

import com.google.gson.*;
import java.util.LinkedList;

/**
 * Class for communicate with command line
 * @autor Anokhin Igor
 * @version 1.0.0
 */

public class Interactive_mode {
	private boolean DEBUG = false;
	private int LAST_INDEX = -1;
	private static String end_message = "It's all.";

	public static String collection_passagers_path = "passageres.xml";
	public static String collection_rooms_path = "rooms.xml";
	OutputStream out;


	private boolean exit;
	private String help = "Please, use following command:\n" +
			"'add {element}' for add new element in collection\n" +
			"'remove {element}' for remove element from collection\n" +
			"'remove_last' for remove last element from collection\n" +
			"'remove_first' for remove first element from collection\n" +
			"'import {path}' for add new collection path\n" +
			"'start' for start simulation\n" +
			"'exit' for exit from program\n" +
			"'show' for show list with passageres\n" +
			"'save_to {path}' save list to new file\n" +
			"'debug' for add bedugging messages\n";

	/**
	 * Start loop, while user don't enter 'exit'
	 * @param in, out
	 */
	Interactive_mode(ObjectInputStream in, ObjectOutputStream out) {
		this.out = out;
		exit = false;
		String inputLine;
		System.out.println("HERE");
		try {
			while ((inputLine = (String) in.readObject()) != null) {
				System.out.println(inputLine);
				parse_input(inputLine);

				out.write(end_message.getBytes());
				out.flush();
			}

		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ 8080 + " or listening for a connection");
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Непонятный Объект");
		}
	}
	/**
	 * @param inputLine - main argument with commands
	 */
	private void parse_input(String inputLine) throws IOException
	{
	    String [] arguments = inputLine.split(" ",2);
	    String main_argument = arguments[0];

	    String sub_argument  = "";
	    if (arguments.length > 1)
	    	sub_argument = arguments[1];

		switch (main_argument)
		{
			case "debug":
				out.write("Debug enabled".getBytes());
				DEBUG = true;
				break;
			case "exit":
				exit = true;
				out.write("Program will be close".getBytes());
				break;
			case "remove_last":
			    try {
					remove_last();
					out.write("Last element was removed\n".getBytes());
				} catch (NullPointerException e) {
					out.write("Can't open file, may be you should change path".getBytes());
				}
				break;
			case "remove_first":
				try {
					remove_first();
					out.write("First element was removed\n".getBytes());
				} catch (NullPointerException e) {
					out.write("Can't open file, may be you should change path".getBytes());
				}
				break;
			case "add":
			    try {
					add_element(get_json_element(sub_argument));
					out.write("Element was successfully added\n".getBytes());
				} catch (IllegalStateException e) {
					out.write("Incorrect input".getBytes());
				} catch (NullPointerException e) {
					out.write("Can't open file, may be you should change path".getBytes());
				}
				break;
			case "remove":
				try {
					remove_element(get_json_element(sub_argument));
					out.write("Element was successfully removed\n".getBytes());
				} catch (IllegalStateException e) {
						out.write("Incorrect input\n".getBytes());
				} catch (NullPointerException e) {
						out.write("Can't open file, may be you should change path\n".getBytes());
				}
				break;
			case "import":
				import_path(sub_argument);
				out.write("Path was changed\n".getBytes());
				break;
			case "save_to":
				try {
			    	save_to(sub_argument);
					out.write("Collection was successfully saved\n".getBytes());
				} catch (NullPointerException e) {
					out.write("Can't open file, may be you should change path\n".getBytes());
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
					out.write("Try to show collection\n".getBytes());
					show_list(new Rocket(), collection_passagers_path);
				} catch (NullPointerException e) {
					out.write("Can't open file, may be you should change path".getBytes());
				}
				break;
			default:
				out.write(help.getBytes());
				break;
		}
	}

	/**
	 * wrapper remove method
	 * call remove with last index of array
	 */
	private void remove_last() throws IOException
	{
		remove(LAST_INDEX);
	}

	/**
	 * wrapper remove method
	 * call remove with first index of array
	 */
	private void remove_first() throws IOException
	{
		remove(0);
	}

	/**
	 * wrapper remove method
	 * Try to find index element.
	 * After that call remove with index this element
	 * @param passager - element for delete
	 */
	private void remove_element(Rocket_passager passager) throws IOException
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		for (int i = 0; i < rocket.getRocket_passageres().size(); i++)
			if (passager == rocket.getRocket_passageres().get(i)) {
				remove(i);
				break;
			}
	}

	/**
	 * delete elements
	 * @param index - element number
	 */
	private synchronized void remove(int index) throws IOException
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		if (rocket.getRocket_passageres().size() > 0 && rocket.getRocket_passageres().size()> index) {
			if (index == LAST_INDEX) index = rocket.getRocket_passageres().size() - 1;
			rocket.getRocket_passageres().remove(index);
		} else
			out.write("Can't remove. Container empty".getBytes());
		write_to_xml(rocket, collection_passagers_path);
	}

	/**
	 * Add new element into collection
	 * @param passager - element, that will be add
	 */
	private synchronized void add_element(Rocket_passager passager)
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		rocket.getRocket_passageres().add(passager);
		write_to_xml(rocket, collection_passagers_path);
	}

	/**
	 * Change path to file with collection
	 * @param input - scanner for read new path
	 */
	private void import_path(String path)
	{
		String new_path;
			new_path = path;
			collection_passagers_path = new_path;
	}

	private void save_to(String path)
	{
		Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
		write_to_xml(rocket, path);
	}

	/**
	 * Parse json input and build new passager
	 * @return redy for action passager
	 */
	private Rocket_passager get_json_element(String element) throws IOException
	{
		Gson gson = new Gson();
		String JSON;
		Rocket_passager passager;
		try {
			JSON = element;
			out.write(JSON.getBytes());
		    passager = gson.fromJson(JSON, Rocket_passager.class);
		    if (passager.getPlace() == null)
		    	throw new Exception();
		} catch (Exception e)
		{
			out.write("Не самое правильное значение".getBytes());
			out.write("Сначала имя, потом знания, затем статус и местоположение".getBytes());
			throw  new IllegalStateException();
		}

		Room_list Container = read_from_xml(Room_list.class, collection_rooms_path);
		boolean room_exist = false;
		for(Room room : Container.getRooms()) {
			if (room.comparebyname(passager.getPlace().getName())) {
				passager.setPlace(room);
				room_exist = true;
			}
		}
		if (!room_exist) {
			out.write("Такой комнаты не существует".getBytes());
			throw new IllegalStateException();
		}

		return passager;
	}

	/**
	 * Write object into file(may be new file)
	 * @param object_to_write - object for writting into file
	 * @param path - path to collection
	 * @param <T> - class, which will be write to file
	 */
	public static synchronized <T> void write_to_xml( T object_to_write, String path)
	{

		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(object_to_write.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(object_to_write, file);

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
	public static <T> T read_from_xml(Class <T> type, String path) {
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
			System.out.println("File not found");
			return null;
		}
	}

	/**
	 * Write objedct into the xml file
	 * @param passageres - this jbject will be write to file
	 */
	public synchronized static void writer_passageres(LinkedList<Rocket_passager> passageres){
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

	private <T> void show_list( T object_to_write, String path) throws IOException
	{
	    	Rocket rocket = read_from_xml(Rocket.class, collection_passagers_path);
			out.write("Passagers:\n".getBytes());
	    	for (Rocket_passager auto : rocket.getRocket_passageres()) {
				out.write(auto.toString().getBytes());
	    	}
	}
}

//add more friendly json add
//add html server

//add logger

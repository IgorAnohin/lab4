import java.io.Serializable;

public abstract class Animal implements Serializable
{
	protected String name;
	protected Status status;
	protected String gender;
	protected boolean isAlive;
}

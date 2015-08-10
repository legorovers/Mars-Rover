package ui;

public class IntItem
{
	private String name;
	private int value;
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public IntItem(String _name, int _value)
	{
		name = _name;
		value = _value;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getInt()
	{
		return value;
	}
	
	public static IntItem[] allDelayItems()
	{
		return new IntItem[]{new IntItem("Instant", 0),
				  new IntItem("Satellite Phone Call to Australia (500ms)", 500),
				  new IntItem("Radio Signal to the Moon (1.3s)", 1300),
				  new IntItem("Signal to Mars (3mins)", 180000)};
	}
	
	public static IntItem[] allSpeedItems()
	{
		return new IntItem[]{new IntItem("Slow", 5),
				  new IntItem("Normal", 10),
				  new IntItem("Fast", 15),
				  new IntItem("Faster", 20),
				  new IntItem("Fastest", 30)};
	}
}
	
	
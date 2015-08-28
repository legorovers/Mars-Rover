package ev3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Manages the EV3 Connection
 * 
 * Created by joecollenette on 02/07/2015.
 */
public abstract class BluetoothRobot implements Runnable
{	
	public enum ConnectStatus {CONNECTED, DISCONNECTED, CONNECTING, DISCONNECTING}
	
	public enum BeliefStates
	{
		OBSTACLE(0),
		WATER(1),
		PATH(2);

		static BeliefStates[] a = BeliefStates.values();

		int value;
		BeliefStates(int i)
		{
			value = i;
		}

		public int toInt()
		{
			return value;
		}

		public static BeliefStates fromInt(int i)
		{
			for (int j = 0; j < a.length; j++)
			{
				if (a[j].toInt() == i)
				{
					return a[j];
				}
			}
			return OBSTACLE;
		}

	}

	public enum RobotMode {MANUAL, AVOID, WATER, LINE}

	public enum RobotAction
	{
		NOTHING(0),
		FORWARD(1),
		FORWARD_A_BIT(2),
		STOP(3),
		BACK_A_BIT(4),
		BACKWARD(5),
		LEFT(6),
		LEFT_A_BIT(7),
		RIGHT(8),
		RIGHT_A_BIT(9);

		int value;
		RobotAction(int i)
		{
			value = i;
		}

		public int toInt()
		{
			return value;
		}

		public static RobotAction fromInt(int i)
		{
			for (int j = 0; j < a.length; j++)
			{
				if (a[j].toInt() == i)
				{
					return a[j];
				}
			}
			return NOTHING;
		}
	}

	private static RobotAction[] a = RobotAction.values();
	
	private static class TimedAction
	{
		private RobotAction action;
		private long time;
		
		public RobotAction getAction()
		{
			return action;
		}
		
		public long getTime()
		{
			return time;
		}
		
		public TimedAction(RobotAction _action, long _time)
		{
			action = _action;
			time = _time;
		}
	}
	
	public static class BeliefSet
	{
		public float distance;
		public Color colour = Color.BLACK;
		public ArrayList<BeliefStates> states = new ArrayList<BeliefStates>();
		
		@Override
		public String toString()
		{
			StringBuilder toReturn = new StringBuilder();
			toReturn.append("Beliefs - [");
			for (int i = 0; i < states.size(); i++)
			{
				toReturn.append(states.get(i).toString());
				if (i < states.size() - 1)
				{
					toReturn.append(", ");
				}
			}
			toReturn.append("]");
			return toReturn.toString();
		}
	}

	public static class RobotRule
	{
		private boolean on;
		private BeliefStates type;
		private RobotAction[] actions;
		private int onAppeared;

		public RobotRule()
		{
			on = false;
			type = BeliefStates.OBSTACLE;
			onAppeared = 0;
			actions = new RobotAction[]{RobotAction.NOTHING, RobotAction.NOTHING, RobotAction.NOTHING};
		}
		
		public RobotRule(boolean _on, BeliefStates _type, int appeared, RobotAction action1, RobotAction action2, RobotAction action3)
		{
			type = _type;
			on = _on;
			onAppeared = appeared;
			actions = new RobotAction[]{action1, action2, action3};
		}

		public boolean getEnabled()
		{
			return on;
		}

		public int getOnAppeared()
		{
			return onAppeared;
		}

		public RobotAction getAction(int pos)
		{
			return actions[pos];
		}

		public BeliefStates getType()
		{
			return type;
		}
	}

    private Robot robot;
    private Exception generatedException;
	private String btAddress;
	private LinkedBlockingDeque<TimedAction> actions;
	private ConnectStatus status = ConnectStatus.DISCONNECTED;

	private RobotRule[] rules;
	private RobotMode mode;
	private boolean running;
	
	private BeliefSet state;
	private boolean obstacleChanged;
	private boolean pathChanged;
	private boolean waterChanged;
	private boolean pathFound;

	private float objectDetected = 0.4f;
	private int blackMax = 50;
	private int waterMax = 100;
	private Random rTurn = new Random();
	//private float pathLight = 0.09f;
	//private PointF waterLightRange = new PointF(0.06f, 0.09f);
	private int speed = 10;
	private long delay = 0;
	private long untilAction = 0;

	private void updateBeliefs(float distance, Color colour)
	{
		boolean curObs = state.states.contains(BeliefStates.OBSTACLE);
		if (Float.compare(distance, objectDetected) < 0)
		{
			if (!state.states.contains(BeliefStates.OBSTACLE))
			{
				state.states.add(BeliefStates.OBSTACLE);
			}
		}
		else
		{
			if (state.states.contains(BeliefStates.OBSTACLE))
			{
				state.states.remove(BeliefStates.OBSTACLE);
			}
		}
		obstacleChanged = curObs != state.states.contains(BeliefStates.OBSTACLE);
		int red = colour.getRed();
		int blue = colour.getBlue();
		int green = colour.getGreen();

		//pathChanged = state.states.contains(BeliefStates.PATH) != (Float.compare(light, pathLight) < 0);
		boolean curPath = state.states.contains(BeliefStates.PATH);
		if ((red < blackMax) && (blue < blackMax) && (green < blackMax))
		{
			if (!state.states.contains(BeliefStates.PATH))
			{
				state.states.add(BeliefStates.PATH);
			}
		}
		else
		{
			if (state.states.contains(BeliefStates.PATH))
			{
				state.states.remove(BeliefStates.PATH);
			}
		}
		pathChanged = curPath != state.states.contains(BeliefStates.PATH);

		//waterChanged = state.states.contains(BeliefStates.WATER) != ((Float.compare(light, waterLightRange.x) > 0) && (Float.compare(light, waterLightRange.y) < 0));
		boolean curWater = state.states.contains(BeliefStates.WATER);
		if (((blue > green) && (blue > red)) && ((red < waterMax) && (blue < waterMax) && (green < waterMax)))
		{
			if (!state.states.contains(BeliefStates.WATER))
			{
				state.states.add(BeliefStates.WATER);
			}
		}
		else
		{
			if (state.states.contains(BeliefStates.WATER))
			{
				state.states.remove(BeliefStates.WATER);
			}
		}
		waterChanged = curWater != state.states.contains(BeliefStates.WATER);
	}

	private void checkRules()
	{
		for (int i = 0; i < rules.length; i++)
		{
			RobotRule rule = rules[i];
			boolean doActions = false;
			if (rule.getEnabled())
			{
				boolean onAppeared = (rule.getOnAppeared() == 0);
				switch (rule.getType())
				{
					case WATER:
						doActions = waterChanged && (onAppeared == state.states.contains(BeliefStates.WATER));
						break;
					case OBSTACLE:
						doActions = obstacleChanged && (onAppeared == state.states.contains(BeliefStates.OBSTACLE));
						break;
					case PATH:
						doActions = pathChanged && (onAppeared == state.states.contains(BeliefStates.OBSTACLE));
						break;
				}
				if (doActions)
				{
					//Check the rules in reverse order so they are placed in queue in order.
					for (int j = rule.actions.length - 1; j >= 0; j--)
					{
						//Remove the delay on the action.
						actions.addFirst(new TimedAction(rule.getAction(j), System.currentTimeMillis() + delay));
					}
				}
			}
		}
	}

	private void doAction()
	{
		if (actions.peek() != null)
		{
			TimedAction actionPair = actions.peek();
			if((actionPair.getTime() + delay > System.currentTimeMillis() && status != ConnectStatus.DISCONNECTING))
			{
				untilAction = (actionPair.getTime() + delay) - System.currentTimeMillis();
				updateTimeTil(untilAction);
			}
			else
			{
				actionPair = actions.poll();
				untilAction = 0;
				if (status == ConnectStatus.CONNECTED)
				{
					switch (actionPair.getAction())
					{
						case FORWARD:
							robot.forward();
							break;
						case FORWARD_A_BIT:
							robot.short_forward();
							break;
						case STOP:
							robot.stop();
							break;
						case BACK_A_BIT:
							robot.short_backward();
							break;
						case BACKWARD:
							robot.backward();
							break;
						case LEFT:
							robot.left();
							break;
						case LEFT_A_BIT:
							robot.short_left();
							break;
						case RIGHT:
							robot.right();
							break;
						case RIGHT_A_BIT:
							robot.short_right();
							break;
						case NOTHING:
							break;
						default:
							break;
					}
				}
			}			
		}
	}

	private void doAvoid()
	{
		if (running)
		{
			if (state.states.contains(BeliefStates.OBSTACLE))
			{
				robot.stop();
				robot.short_left();
			}
			else
			{
				if (!robot.isMoving())
				{
					robot.forward();
				}
			}
		}
		else
		{
			robot.stop();
		}
	}

	private void doLine()
	{
		if (running)
		{
			if (!state.states.contains(BeliefStates.PATH))
			{
				robot.forward_right();
			}
			else
			{
				robot.forward_left();
			}
		}
		else
		{
			robot.stop();
		}
	}

	private void doWater()
	{
		if (running)
		{
			if (!state.states.contains(BeliefStates.WATER))
			{
				if (!pathFound && state.states.contains(BeliefStates.PATH))
				{
					pathFound = true;
				}
				else if (pathFound)
				{
					doLine();
				}
				else if (!state.states.contains(BeliefStates.OBSTACLE))
				{
					robot.forward();
				}
				else
				{
					robot.turn(45 + rTurn.nextInt(135));
				}
			}
			else
			{
				running = false;
				robot.stop();
			}
		}
		else
		{
			robot.stop();
		}
	}

    @Override
    public void run()
    {
        try
        {
        	//Connect
			generatedException = null;
			status = ConnectStatus.CONNECTING;
			robot.connectToRobot(btAddress);
			connected();
			status = ConnectStatus.CONNECTED;
			float disInput;
			float[] rgb;
			int curSpeed = speed;
			int red;
			int blue;
			int green;
			
			//While connected and no disconnect signalled
			while (status == ConnectStatus.CONNECTED)
			{
				//Update Beliefs
				disInput = robot.getuSensor().getSample();
				rgb = robot.getRGBSensor().getRGBSample();
				red = Math.min(255, (int)(rgb[0] * 850));
				green = Math.min(255, (int)(rgb[1] * 1026));
				blue = Math.min(255, (int)(rgb[2] * 1815));
				state.colour = new Color(red, green, blue);
				state.distance = disInput;
				updateBeliefs(disInput, state.colour);
				
				//Update Speed and check if actions are needed
				if (curSpeed != speed)
				{
					robot.setTravelSpeed(speed);
					curSpeed = speed;
				}
				switch(mode)
				{
					case MANUAL:
						checkRules();
						doAction();
						break;
					case LINE:
						doLine();
						break;
					case AVOID:
						doAvoid();
						break;
					case WATER:
						doWater();
						break;
				}
				
			}
			//Disconnect
			robot.close();	    	
			status = ConnectStatus.DISCONNECTED;
			state.states.clear();
			state.colour = Color.BLACK;
			state.distance = 0;
			disconnected();
        }
        catch (Exception e)
        {
        	status = ConnectStatus.DISCONNECTING;
			if (robot != null && robot.isConnected())
			{
				robot.close();
			}
			status = ConnectStatus.DISCONNECTED;
			error();
            generatedException = e;
            e.printStackTrace();
        }
    }

	public BluetoothRobot()
	{
		actions = new LinkedBlockingDeque<TimedAction>();

		rules = new RobotRule[]{
				new RobotRule(),
				new RobotRule(),
				new RobotRule(),
				new RobotRule()
		};
		
		state = new BeliefSet();
		state.colour = Color.BLACK;
		robot = new Robot();
		mode = RobotMode.MANUAL;
		running = false;
	}

	public void addAction(RobotAction action)
	{
		actions.add(new TimedAction(action, System.currentTimeMillis()));
	}

	public RobotRule[] getAllRules()
	{
		return rules;
	}

    public Exception getGeneratedException()
    {
        return generatedException;
    }

	public void setBTAddress(String address)
	{
		btAddress = address;
	}

	public ConnectStatus connectionStatus()
	{
		return status;
	}

	public void disconnect()
	{
		setRunning(false);
		if (status != ConnectStatus.DISCONNECTED)
		{
			status = ConnectStatus.DISCONNECTING;
		}
	}

	public void close()
	{
		if (robot != null)
		{
			robot.close();
		}
	}

	public void changeSettings(float objectRange, int blackMaximum, int waterMaximum)
	{
		objectDetected = objectRange;
		blackMax = blackMaximum;
		waterMax = waterMaximum;
		//waterLightRange = new PointF(waterLower, waterUpper);
		//pathLight = pathRange;
	}

	public void changedRule(int pos, RobotRule rule)
	{
		rules[pos] = rule;
	}

	public BeliefSet getBeliefSet()
	{
		return state;
	}

	public void setMode(RobotMode _mode)
	{
		running = false;
		pathFound = false;
		mode = _mode;
	}

	public long getTimeToAction()
	{
		return untilAction;
	}

	public void setRunning(boolean _running)
	{
		actions.clear();
		running = _running;
	}

	public void updateManual(long delayMills, int _speed)
	{
		delay = delayMills;
		speed = _speed;
	}
	
	public void setSpeed(int _speed)
	{
		speed = _speed;
	}
	
	public void setDelay(int _delay)
	{
		delay = _delay;
	}

	public boolean getRunning()
	{
		return running;
	}

	public Color getColourFound()
	{
		return state.colour;
	}
	
	/* Change from android version due to threading issues
	 *  
	 * These events are called instead of having methods. 
	 */
	
	public abstract void update(BeliefSet state);
	
	public abstract void updateTimeTil(long time);
	
	public abstract void connected();
	
	public abstract void disconnected();
	
	//Error connecting is the most usual cause
	public abstract void error();
	
}

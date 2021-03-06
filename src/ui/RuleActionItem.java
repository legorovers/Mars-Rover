package ui;

import ev3.BluetoothRobot.RobotAction;

/*
 * Creates items for the robot action combobox and the Appeared, Disappeared combobox.
 * This also stores the relevant data for that item. 
 */
public class RuleActionItem 
{
	private RobotAction action;
	private String title;
	private int ruleNo;
	
	@Override
	public String toString()
	{
		return title;
	}
	
	public RobotAction getAction()
	{
		return action;
	}
	
	public int getRuleNo()
	{
		return ruleNo;
	}
	
	public RuleActionItem(String _title, RobotAction _action, int _ruleNo)
	{
		title = _title;
		action = _action;
		ruleNo = _ruleNo;
	}
	
	public static RuleActionItem[] getActions(int num)
	{
		return new RuleActionItem[]{new RuleActionItem("Nothing", RobotAction.NOTHING, num),
									new RuleActionItem("Forward", RobotAction.FORWARD, num),
									new RuleActionItem("Forward a bit", RobotAction.FORWARD_A_BIT, num),
									new RuleActionItem("Reverse", RobotAction.BACKWARD, num),
									new RuleActionItem("Reverse a bit", RobotAction.BACK_A_BIT, num),
									new RuleActionItem("Left", RobotAction.LEFT, num),
									new RuleActionItem("Left 90°", RobotAction.LEFT_A_BIT, num),
									new RuleActionItem("Right", RobotAction.RIGHT, num),
									new RuleActionItem("Right 90°", RobotAction.RIGHT_A_BIT, num),
									new RuleActionItem("Stop", RobotAction.STOP, num)};
	}
	
	public static RuleActionItem[] getObstacleStates(int num)
	{
		return new RuleActionItem[]{new RuleActionItem("Appeared", RobotAction.NOTHING, num),
									new RuleActionItem("Disappeared", RobotAction.NOTHING, num)};
	}
}

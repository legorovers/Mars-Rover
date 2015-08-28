package ui;

import ev3.BluetoothRobot.BeliefStates;

/*
 * Creates the items in the Belief Combobox and stores the object data needed
 * to pass to the bluetooth thread
 */
public class BeliefItem
{	
	private String title;
	private int ruleNo;
	private BeliefStates belief;
	
	public BeliefItem(String _title, BeliefStates _belief, int _ruleNo)
	{
		title = _title;
		belief = _belief;
		ruleNo = _ruleNo;
	}
	
	@Override
	public String toString()
	{
		return title;
	}
	
	public BeliefStates getBelief()
	{
		return belief;
	}
	
	public int getRuleNo()
	{
		return ruleNo;
	}
	
	public static BeliefItem[] getBeliefsItems(int num)
	{
		return new BeliefItem[]{new BeliefItem("An obstacle", BeliefStates.OBSTACLE, num),
								new BeliefItem("Water", BeliefStates.WATER, num),
								new BeliefItem("A path", BeliefStates.PATH, num)};
	}

}

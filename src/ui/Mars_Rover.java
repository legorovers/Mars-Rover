package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.JButton;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import ev3.BluetoothRobot;
import ev3.BluetoothRobot.BeliefStates;
import ev3.BluetoothRobot.ConnectStatus;
import ev3.BluetoothRobot.RobotAction;
import ev3.BluetoothRobot.RobotMode;
import ev3.BluetoothRobot.RobotRule;

import javax.swing.SpinnerNumberModel;

import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

public class Mars_Rover 
{
	class ActionButton implements ActionListener
	{
		private BluetoothRobot.RobotAction action;
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (btRobot.connectionStatus() == BluetoothRobot.ConnectStatus.CONNECTED)
			{
				btRobot.addAction(action);
			}
		}
		
		public ActionButton(BluetoothRobot.RobotAction _action)
		{
			action = _action;
		}
	}
	
	class RuleActionChanged implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e) 
		{
			changeRule(((RuleActionItem)e.getItem()).getRuleNo());
		}
	}
	
	class BeliefItemChanged implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e) 
		{
			changeRule(((BeliefItem)e.getItem()).getRuleNo());
		}
	}
	
	class RuleChkChanged implements ChangeListener
	{
		private int ruleNo;
		
		@Override
		public void stateChanged(ChangeEvent e) 
		{
			changeRule(ruleNo);
		}
		
		public RuleChkChanged(int _ruleNo)
		{
			ruleNo = _ruleNo;
		}
	}
	
	class SettingsChanged implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			btRobot.changeSettings(((Double)spnObs.getValue()).floatValue(), (int)spnPath.getValue(), (int)spnWater.getValue());
			JOptionPane.showMessageDialog(frmMarsRover, "Settings Changed", "Mars Rover", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(imgIcon.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		}
	}

	
	private JFrame frmMarsRover;
	private BufferedImage imgIcon;
	private BluetoothRobot btRobot;
	
	private Thread robotThread;
	
	private JLabel lblBeliefs;
	private JLabel lblDistance;
	private JLabel lblStatus;
	private JLabel lblRGB;
	
	private JProgressBar prgConnect;
	private JButton btnConnect;
	private JSpinner spnBT1;
	private JSpinner spnBT2;
	private JSpinner spnBT3;
	private JSpinner spnBT4;
	private JLabel lblTimeUntil;
	
	private JTabbedPane mainTabPane;
	private JPanel pnlRules;
	
	private JComboBox<IntItem> cboDelay;
	
	private JCheckBox chkR1;
	private JComboBox<RuleActionItem> cboR1Obs;
	private JComboBox<BeliefItem> cboR1Type;
	private JComboBox<RuleActionItem> cboR1A1;
	private JComboBox<RuleActionItem> cboR1A2;
	private JComboBox<RuleActionItem> cboR1A3;
	
	private JCheckBox chkR2;
	private JComboBox<RuleActionItem> cboR2Obs;
	private JComboBox<BeliefItem> cboR2Type;
	private JComboBox<RuleActionItem> cboR2A1;
	private JComboBox<RuleActionItem> cboR2A2;
	private JComboBox<RuleActionItem> cboR2A3;
	
	private JCheckBox chkR3;
	private JComboBox<RuleActionItem> cboR3Obs;
	private JComboBox<BeliefItem> cboR3Type;
	private JComboBox<RuleActionItem> cboR3A1;
	private JComboBox<RuleActionItem> cboR3A2;
	private JComboBox<RuleActionItem> cboR3A3;
	
	private JCheckBox chkR4;
	private JComboBox<RuleActionItem> cboR4Obs;
	private JComboBox<BeliefItem> cboR4Type;
	private JComboBox<RuleActionItem> cboR4A1;
	private JComboBox<RuleActionItem> cboR4A2;
	private JComboBox<RuleActionItem> cboR4A3;
	
	private JButton btnTask;
	private ButtonGroup rdbTaskGroup;
	private JRadioButton rdbLine;
	private JRadioButton rdbObs;
	private JRadioButton rdbWater;
	
	private JSpinner spnObs;
	private JSpinner spnPath;
	private JSpinner spnWater;
	private JButton btnSettings;
	
	private JButton btnForward;
	private JButton btnFABit;
	private JButton btnLeft;
	private JButton btnLABit;
	private JButton btnStop;
	private JButton btnRABit;
	private JButton btnBABit;
	private JButton btnBack;
	private JButton btnRight;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Mars Rover");
					Mars_Rover window = new Mars_Rover();
					window.frmMarsRover.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Mars_Rover() {
		initialize();
	}
	
	private void changeRule(int ruleNo)
	{
		boolean checked = false;
		int obs = 0;
		RobotAction a1 = null;
		RobotAction a2 = null;
		RobotAction a3 = null;
		BeliefStates b = null;
		switch (ruleNo)
		{
			case 1:
				checked = chkR1.isSelected();
				obs = cboR1Obs.getSelectedIndex();
				b = ((BeliefItem)cboR1Type.getSelectedItem()).getBelief();
				a1 = ((RuleActionItem)cboR1A1.getSelectedItem()).getAction();
				a2 = ((RuleActionItem)cboR1A2.getSelectedItem()).getAction();
				a3 = ((RuleActionItem)cboR1A3.getSelectedItem()).getAction();
				break;
			case 2:
				checked = chkR2.isSelected();
				obs = cboR2Obs.getSelectedIndex();
				b = ((BeliefItem)cboR2Type.getSelectedItem()).getBelief();
				a1 = ((RuleActionItem)cboR2A1.getSelectedItem()).getAction();
				a2 = ((RuleActionItem)cboR2A2.getSelectedItem()).getAction();
				a3 = ((RuleActionItem)cboR2A3.getSelectedItem()).getAction();
				break;
			case 3:
				checked = chkR3.isSelected();
				obs = cboR3Obs.getSelectedIndex();
				b = ((BeliefItem)cboR3Type.getSelectedItem()).getBelief();
				a1 = ((RuleActionItem)cboR3A1.getSelectedItem()).getAction();
				a2 = ((RuleActionItem)cboR3A2.getSelectedItem()).getAction();
				a3 = ((RuleActionItem)cboR3A3.getSelectedItem()).getAction();
				break;
			case 4:
				checked = chkR4.isSelected();
				obs = cboR4Obs.getSelectedIndex();
				b = ((BeliefItem)cboR4Type.getSelectedItem()).getBelief();
				a1 = ((RuleActionItem)cboR4A1.getSelectedItem()).getAction();
				a2 = ((RuleActionItem)cboR4A2.getSelectedItem()).getAction();
				a3 = ((RuleActionItem)cboR4A3.getSelectedItem()).getAction();
				break;
		}
		RobotRule r = new RobotRule(checked, b, obs, a1, a2, a3);
		btRobot.changedRule(ruleNo - 1, r);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		btRobot = new BluetoothRobot()
		{
			@Override
			public void update(BeliefSet state) 
			{
				lblBeliefs.setText(state.toString());
				lblDistance.setText(String.format("Distance - %f", state.distance));
				lblRGB.setText(String.format("RGB - %d %d %d", state.colour.getRed(), state.colour.getGreen(), state.colour.getBlue()));
			}

			@Override
			public void updateTimeTil(long time) 
			{
				lblTimeUntil.setText("Time until next action - " + new SimpleDateFormat("mm:ss").format(time));
				lblTimeUntil.repaint();
			}

			@Override
			public void connected() 
			{
				lblStatus.setText("Connected");
				btnConnect.setText("Disconnect");
				prgConnect.setVisible(false);
				btnConnect.setEnabled(true);
			}

			@Override
			public void disconnected() 
			{
				lblStatus.setText("Disconnected");
				btnConnect.setText("Connect");
				prgConnect.setVisible(false);
				btnConnect.setEnabled(true);
			}

			@Override
			public void error() 
			{
				lblStatus.setText("Error Connecting");
				btnConnect.setText("Connect");
				prgConnect.setVisible(false);
				btnConnect.setEnabled(true);				
			}
	
		};
		robotThread = new Thread(btRobot);
				
		frmMarsRover = new JFrame();
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run()
			{
				btRobot.disconnect();
				while (btRobot.connectionStatus() != ConnectStatus.DISCONNECTED)
				{
					System.out.println("Waiting for robot to disconnect");
				}
			}
		});
		
		
		try {
			imgIcon = ImageIO.read(Mars_Rover.class.getClass().getResourceAsStream("/logo.png"));
			
		} catch (IOException e) {
			imgIcon = new BufferedImage(0, 0, 0);
			System.out.println(e.getMessage());
			
		}
		frmMarsRover.setIconImage(imgIcon);
		frmMarsRover.setResizable(false);
		frmMarsRover.setTitle("Mars Rover");
		frmMarsRover.setMinimumSize(new Dimension(800, 600));
		frmMarsRover.setBounds(100, 100, 800, 600);
		frmMarsRover.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMarsRover.getContentPane().setLayout(new BorderLayout(0, 0));
		
		//GUI
		setUpTopPanel();
		setUpMiddlePanel();
		setUpBottomPanel();
		
		mainTabPane.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				if (mainTabPane.getSelectedIndex() == 0)
				{
					btRobot.setMode(RobotMode.MANUAL);
				}
			}	
		});
		
		//Events
		btnConnect.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				btnConnect.setEnabled(false);
				prgConnect.setVisible(true);
				if (!robotThread.isAlive())
				{
					btRobot.setBTAddress(String.format("%d.%d.%d.%d", spnBT1.getValue(), spnBT2.getValue(), spnBT3.getValue(), spnBT4.getValue()));
					lblStatus.setText("Connecting");
					robotThread = new Thread(btRobot);
					robotThread.start();
				}
				else
				{
					lblStatus.setText("Disconnecting");
					btRobot.disconnect();
				}
			}
		});
		
		chkR1.addChangeListener(new RuleChkChanged(1));
		cboR1Obs.addItemListener(new RuleActionChanged());
		cboR1Type.addItemListener(new BeliefItemChanged());
		cboR1A1.addItemListener(new RuleActionChanged());
		cboR1A2.addItemListener(new RuleActionChanged());
		cboR1A3.addItemListener(new RuleActionChanged());
		
		chkR2.addChangeListener(new RuleChkChanged(2));
		cboR2Obs.addItemListener(new RuleActionChanged());
		cboR2Type.addItemListener(new BeliefItemChanged());
		cboR2A1.addItemListener(new RuleActionChanged());
		cboR2A2.addItemListener(new RuleActionChanged());
		cboR2A3.addItemListener(new RuleActionChanged());
		
		chkR3.addChangeListener(new RuleChkChanged(3));
		cboR3Obs.addItemListener(new RuleActionChanged());
		cboR3Type.addItemListener(new BeliefItemChanged());
		cboR3A1.addItemListener(new RuleActionChanged());
		cboR3A2.addItemListener(new RuleActionChanged());
		cboR3A3.addItemListener(new RuleActionChanged());
		
		chkR4.addChangeListener(new RuleChkChanged(4));
		cboR4Obs.addItemListener(new RuleActionChanged());
		cboR4Type.addItemListener(new BeliefItemChanged());
		cboR4A1.addItemListener(new RuleActionChanged());
		cboR4A2.addItemListener(new RuleActionChanged());
		cboR4A3.addItemListener(new RuleActionChanged());
		
		cboDelay.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				btRobot.setDelay(((IntItem)e.getItem()).getInt());
			}
		});
		
		btnTask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(rdbLine.isSelected())
				{
					btRobot.setMode(RobotMode.LINE);
				}
				else if (rdbObs.isSelected())
				{
					btRobot.setMode(RobotMode.AVOID);
				}
				else
				{
					btRobot.setMode(RobotMode.WATER);
				}
				
				if (btnTask.getText().equals("Start"))
				{
					btnTask.setText("Stop");
					mainTabPane.setEnabled(false);
					btRobot.setRunning(true);
				}
				else
				{
					btnTask.setText("Start");
					btRobot.setRunning(false);
					mainTabPane.setEnabled(true);
				}
			}
		});
		
		btnSettings.addActionListener(new SettingsChanged());
		
	}
	
	public void setUpTopPanel()
	{
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frmMarsRover.getContentPane().add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel_4 = new JLabel("Robot Address");
		panel_1.add(lblNewLabel_4);
		
		spnBT1 = new JSpinner(new SpinnerNumberModel(10, 0, 255, 1));
		spnBT1.setPreferredSize(new Dimension(55, 28));
		panel_1.add(spnBT1);
		
		spnBT2 = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		spnBT2.setPreferredSize(new Dimension(55, 28));
		panel_1.add(spnBT2);
		
		spnBT3 = new JSpinner(new SpinnerNumberModel(1, 0, 255, 1));
		spnBT3.setPreferredSize(new Dimension(55, 28));
		panel_1.add(spnBT3);
		
		spnBT4 = new JSpinner(new SpinnerNumberModel(1, 0, 255, 1));
		spnBT4.setPreferredSize(new Dimension(55, 28));
		panel_1.add(spnBT4);
		
		btnConnect = new JButton("Connect");
		panel_1.add(btnConnect);
		
		JLabel lblNewLabel = new JLabel("Robot Speed");
		panel_1.add(lblNewLabel);
		
		JComboBox<IntItem> cboSpeed = new JComboBox<IntItem>();
		cboSpeed.setModel(new DefaultComboBoxModel<IntItem>(IntItem.allSpeedItems()));
		cboSpeed.setSelectedIndex(1);
		cboSpeed.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				btRobot.setSpeed(((IntItem)e.getItem()).getInt());
			}
		});
		panel_1.add(cboSpeed);
	}
	
	public void setUpMiddlePanel()
	{
		mainTabPane = new JTabbedPane(JTabbedPane.TOP);
		frmMarsRover.getContentPane().add(mainTabPane, BorderLayout.CENTER);
		setUpNavPage();
		setUpTaskPage();
		setUpSettingsPage();
		setUpAboutPage();
	}
	
	public void setUpNavPage()
	{
		Dimension dimCmdNav = new Dimension(50,50);
		//Navigation Buttons
		JPanel pnl_Nav = new JPanel();
		pnl_Nav.setBackground(new Color(229, 229, 229));
		mainTabPane.addTab("Navigation", null, pnl_Nav, null);
		pnl_Nav.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(229, 229, 229));
		pnl_Nav.add(panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_5.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		btnForward = new JButton("\u25b2\u25b2");
		btnForward.addActionListener(new ActionButton(RobotAction.FORWARD));
		btnForward.setPreferredSize(dimCmdNav);
		btnForward.setToolTipText("Go forward until stop pressed");
		GridBagConstraints gbc_btnFoward = new GridBagConstraints();
		gbc_btnFoward.insets = new Insets(0, 0, 5, 5);
		gbc_btnFoward.gridx = 2;
		gbc_btnFoward.gridy = 0;
		panel_5.add(btnForward, gbc_btnFoward);
		
		
		btnFABit = new JButton("\u25b2");
		btnFABit.addActionListener(new ActionButton(RobotAction.FORWARD_A_BIT));
		btnFABit.setPreferredSize(dimCmdNav);
		btnFABit.setToolTipText("Go forward slightly");
		GridBagConstraints gbc_btnFABit = new GridBagConstraints();
		gbc_btnFABit.insets = new Insets(0, 0, 5, 5);
		gbc_btnFABit.gridx = 2;
		gbc_btnFABit.gridy = 1;
		panel_5.add(btnFABit, gbc_btnFABit);
		
		btnLeft = new JButton("\u25c0\u25c0");
		btnLeft.addActionListener(new ActionButton(RobotAction.LEFT));
		btnLeft.setPreferredSize(dimCmdNav);
		btnLeft.setToolTipText("Turn left until stop pressed");
		GridBagConstraints gbc_btnLeft = new GridBagConstraints();
		gbc_btnLeft.insets = new Insets(0, 0, 5, 5);
		gbc_btnLeft.gridx = 0;
		gbc_btnLeft.gridy = 2;
		panel_5.add(btnLeft, gbc_btnLeft);
		
		btnLABit = new JButton("\u25c0");
		btnLABit.addActionListener(new ActionButton(RobotAction.LEFT_A_BIT));
		btnLABit.setPreferredSize(dimCmdNav);
		btnLABit.setToolTipText("Left 90°");
		GridBagConstraints gbc_btnLABit = new GridBagConstraints();
		gbc_btnLABit.insets = new Insets(0, 0, 5, 5);
		gbc_btnLABit.gridx = 1;
		gbc_btnLABit.gridy = 2;
		panel_5.add(btnLABit, gbc_btnLABit);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionButton(RobotAction.STOP));
		btnStop.setPreferredSize(dimCmdNav);
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.insets = new Insets(0, 0, 5, 5);
		gbc_btnStop.gridx = 2;
		gbc_btnStop.gridy = 2;
		panel_5.add(btnStop, gbc_btnStop);
		
		btnRABit = new JButton("\u25b6");
		btnRABit.addActionListener(new ActionButton(RobotAction.RIGHT_A_BIT));
		btnRABit.setPreferredSize(dimCmdNav);
		btnRABit.setToolTipText("Right 90°");
		GridBagConstraints gbc_btnRABit = new GridBagConstraints();
		gbc_btnRABit.insets = new Insets(0, 0, 5, 5);
		gbc_btnRABit.gridx = 3;
		gbc_btnRABit.gridy = 2;
		panel_5.add(btnRABit, gbc_btnRABit);
		
		btnBABit = new JButton("\u25bc");
		btnBABit.addActionListener(new ActionButton(RobotAction.BACK_A_BIT));
		btnBABit.setPreferredSize(dimCmdNav);
		btnBABit.setToolTipText("Reverse a bit");
		GridBagConstraints gbc_btnBABit = new GridBagConstraints();
		gbc_btnBABit.insets = new Insets(0, 0, 5, 5);
		gbc_btnBABit.gridx = 2;
		gbc_btnBABit.gridy = 3;
		panel_5.add(btnBABit, gbc_btnBABit);
		
		btnBack = new JButton("\u25bc\u25bc");
		btnBack.addActionListener(new ActionButton(RobotAction.BACKWARD));
		btnBack.setPreferredSize(dimCmdNav);
		btnBack.setToolTipText("Reverse until stop pressed");
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.insets = new Insets(0, 0, 5, 5);
		gbc_btnBack.gridx = 2;
		gbc_btnBack.gridy = 4;
		panel_5.add(btnBack, gbc_btnBack);
		
		btnRight = new JButton("\u25b6\u25b6");
		btnRight.addActionListener(new ActionButton(RobotAction.RIGHT));
		btnRight.setPreferredSize(dimCmdNav);
		btnRight.setToolTipText("Turn right until stop pressed");
		GridBagConstraints gbc_btnRight = new GridBagConstraints();
		gbc_btnRight.insets = new Insets(0, 0, 5, 5);
		gbc_btnRight.gridx = 4;
		gbc_btnRight.gridy = 2;
		panel_5.add(btnRight, gbc_btnRight);
		
		//Delay and rules
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setPreferredSize(new Dimension(10, 445));
		pnl_Nav.add(separator_4);
		
		pnlRules = new JPanel();
		pnlRules.setBackground(new Color(229, 229, 229));
		pnl_Nav.add(pnlRules);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBackground(new Color(229, 229, 229));
		
		JLabel lblNewLabel_5 = new JLabel("Delay");
		panel_8.add(lblNewLabel_5);
		
		cboDelay = new JComboBox<IntItem>();
		cboDelay.setModel(new DefaultComboBoxModel<IntItem>(IntItem.allDelayItems()));
		cboDelay.setSelectedIndex(0);
		
		panel_8.add(cboDelay);
		
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.fill = GridBagConstraints.BOTH;
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 0;
		pnlRules.add(panel_8, gbc_panel_8);
		
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[] {0, 0};
		gbl_panel_7.rowHeights = new int[] {0, 0, 0};
		gbl_panel_7.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[]{0.0, 0.0, 0.0};
		pnlRules.setLayout(gbl_panel_7);
		
		setUpRulesPanel();
	}
	
	public void setUpRulesPanel()
	{
		
		lblTimeUntil = new JLabel("Time until next action - 00:00");
		lblTimeUntil.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTimeUntil = new GridBagConstraints();
		gbc_lblTimeUntil.insets = new Insets(0, 0, 5, 0);
		gbc_lblTimeUntil.gridx = 0;
		gbc_lblTimeUntil.gridy = 1;
		pnlRules.add(lblTimeUntil, gbc_lblTimeUntil);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane_1 = new GridBagConstraints();
		gbc_tabbedPane_1.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane_1.gridx = 0;
		gbc_tabbedPane_1.gridy = 2;
		pnlRules.add(tabbedPane_1, gbc_tabbedPane_1);
		
		chkR1 = new JCheckBox("");
		cboR1Obs = new JComboBox<RuleActionItem>();
		cboR1Type = new JComboBox<BeliefItem>();
		cboR1A1 = new JComboBox<RuleActionItem>();
		cboR1A2 = new JComboBox<RuleActionItem>();
		cboR1A3 = new JComboBox<RuleActionItem>();
		tabbedPane_1.addTab("Rule 1", null, RulePanel.createRulePanel(1, chkR1, cboR1Obs, cboR1Type, cboR1A1, cboR1A2, cboR1A3), null);
		
		chkR2 = new JCheckBox("");
		cboR2Obs = new JComboBox<RuleActionItem>();
		cboR2Type = new JComboBox<BeliefItem>();
		cboR2A1 = new JComboBox<RuleActionItem>();
		cboR2A2 = new JComboBox<RuleActionItem>();
		cboR2A3 = new JComboBox<RuleActionItem>();
		tabbedPane_1.addTab("Rule 2", null, RulePanel.createRulePanel(2, chkR2, cboR2Obs, cboR2Type, cboR2A1, cboR2A2, cboR2A3), null);
		
		chkR3 = new JCheckBox("");
		cboR3Obs = new JComboBox<RuleActionItem>();
		cboR3Type = new JComboBox<BeliefItem>();
		cboR3A1 = new JComboBox<RuleActionItem>();
		cboR3A2 = new JComboBox<RuleActionItem>();
		cboR3A3 = new JComboBox<RuleActionItem>();
		tabbedPane_1.addTab("Rule 3", null, RulePanel.createRulePanel(3, chkR3, cboR3Obs, cboR3Type, cboR3A1, cboR3A2, cboR3A3), null);
		
		chkR4 = new JCheckBox("");
		cboR4Obs = new JComboBox<RuleActionItem>();
		cboR4Type = new JComboBox<BeliefItem>();
		cboR4A1 = new JComboBox<RuleActionItem>();
		cboR4A2 = new JComboBox<RuleActionItem>();
		cboR4A3 = new JComboBox<RuleActionItem>();
		tabbedPane_1.addTab("Rule 4", null, RulePanel.createRulePanel(4, chkR4, cboR4Obs, cboR4Type, cboR4A1, cboR4A2, cboR4A3), null);
		
	}
	
	public void setUpTaskPage()
	{
		JPanel pnlTasks = new JPanel();
		pnlTasks.setBackground(new Color(229, 229, 229));
		mainTabPane.addTab("Task Demonstrations", null, pnlTasks, null);
		pnlTasks.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	
		rdbTaskGroup = new ButtonGroup(); 
		
		
		
		JPanel panel_9 = new JPanel();
		panel_9.setBackground(new Color(229, 229, 229));
		pnlTasks.add(panel_9);
		GridBagLayout gbl_panel_9 = new GridBagLayout();
		gbl_panel_9.columnWidths = new int[] {0, 0, 0, 0};
		gbl_panel_9.rowHeights = new int[] {0, 0, 0, 0};
		gbl_panel_9.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_9.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_9.setLayout(gbl_panel_9);
		
		rdbLine = new JRadioButton("Follow Line");
		GridBagConstraints gbc_rdbLine = new GridBagConstraints();
		gbc_rdbLine.insets = new Insets(0, 0, 5, 5);
		gbc_rdbLine.gridx = 0;
		gbc_rdbLine.gridy = 0;
		rdbTaskGroup.add(rdbLine);
		panel_9.add(rdbLine, gbc_rdbLine);
		
		rdbLine.setSelected(true);
		
		rdbObs = new JRadioButton("Avoid Obstacle");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 1;
		gbc_rdbtnNewRadioButton_1.gridy = 0;
		rdbTaskGroup.add(rdbObs);
		panel_9.add(rdbObs, gbc_rdbtnNewRadioButton_1);
		
		rdbWater = new JRadioButton("Find Water");
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton_2.fill = GridBagConstraints.BOTH;
		gbc_rdbtnNewRadioButton_2.gridx = 2;
		gbc_rdbtnNewRadioButton_2.gridy = 0;
		rdbTaskGroup.add(rdbWater);
		panel_9.add(rdbWater, gbc_rdbtnNewRadioButton_2);

		btnTask = new JButton("Start");
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_3.gridx = 1;
		gbc_btnNewButton_3.gridy = 1;
		panel_9.add(btnTask, gbc_btnNewButton_3);
	}
	
	public void setUpSettingsPage()
	{
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(229, 229, 229));
		mainTabPane.addTab("Settings", null, panel_4, null);
		
		JPanel panel_13 = new JPanel();
		panel_13.setBackground(new Color(229, 229, 229));
		panel_4.add(panel_13);
		GridBagLayout gbl_panel_13 = new GridBagLayout();
		gbl_panel_13.columnWidths = new int[] {0, 0, 0};
		gbl_panel_13.rowHeights = new int[] {0, 0, 0, 0, 0};
		gbl_panel_13.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_13.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_13.setLayout(gbl_panel_13);
		
		JLabel lblNewLabel_10 = new JLabel("Obstacle Detection");
		GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
		gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_10.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_10.gridx = 0;
		gbc_lblNewLabel_10.gridy = 0;
		panel_13.add(lblNewLabel_10, gbc_lblNewLabel_10);
		
		spnObs = new JSpinner(new SpinnerNumberModel(0.4, 0.0, 1.0, 0.1));
		DecimalFormat dFormat = ((JSpinner.NumberEditor)spnObs.getEditor()).getFormat();
		dFormat.setMinimumFractionDigits(1);
		
		spnObs.setPreferredSize(new Dimension(100, 28));
		
		GridBagConstraints gbc_spinner_4 = new GridBagConstraints();
		gbc_spinner_4.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_4.gridx = 2;
		gbc_spinner_4.gridy = 0;
		panel_13.add(spnObs, gbc_spinner_4);
		
		JLabel lblNewLabel_11 = new JLabel("Path Maximum");
		GridBagConstraints gbc_lblNewLabel_11 = new GridBagConstraints();
		gbc_lblNewLabel_11.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_11.gridx = 0;
		gbc_lblNewLabel_11.gridy = 1;
		panel_13.add(lblNewLabel_11, gbc_lblNewLabel_11);
		
		spnPath = new JSpinner();
		spnPath.setValue(50);
		GridBagConstraints gbc_spinner_5 = new GridBagConstraints();
		gbc_spinner_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_5.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_5.gridx = 2;
		gbc_spinner_5.gridy = 1;
		panel_13.add(spnPath, gbc_spinner_5);
		
		JLabel lblNewLabel_12 = new JLabel("Water Maximum");
		GridBagConstraints gbc_lblNewLabel_12 = new GridBagConstraints();
		gbc_lblNewLabel_12.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_12.gridx = 0;
		gbc_lblNewLabel_12.gridy = 2;
		panel_13.add(lblNewLabel_12, gbc_lblNewLabel_12);
		
		spnWater = new JSpinner();
		spnWater.setValue(100);
		GridBagConstraints gbc_spinner_6 = new GridBagConstraints();
		gbc_spinner_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_6.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_6.gridx = 2;
		gbc_spinner_6.gridy = 2;
		panel_13.add(spnWater, gbc_spinner_6);
		
		btnSettings = new JButton("Set");
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.gridwidth = 3;
		gbc_btnNewButton_4.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_4.gridx = 0;
		gbc_btnNewButton_4.gridy = 4;
		panel_13.add(btnSettings, gbc_btnNewButton_4);
	}
	
	public void setUpAboutPage()
	{
		JPanel panel_12 = new JPanel();
		panel_12.setBackground(new Color(229, 229, 229));
		GridBagLayout gbl_panel_12 = new GridBagLayout();
		gbl_panel_12.columnWidths = new int[] {0, 0};
		gbl_panel_12.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_panel_12.columnWeights = new double[]{0.0, 0.0};
		gbl_panel_12.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel_12.setLayout(gbl_panel_12);
		
		JImage pnlLivLogo = new JImage(Mars_Rover.class.getClass().getResourceAsStream("/uol.png"), new Dimension(-1, 150));
		pnlLivLogo.setBackground(new Color(229, 229, 229));
		GridBagConstraints gbc_pnlLivLogo = new GridBagConstraints();
		gbc_pnlLivLogo.gridwidth = 2;
		gbc_pnlLivLogo.gridx = 0;
		gbc_pnlLivLogo.gridy = 0;
		panel_12.add(pnlLivLogo, gbc_pnlLivLogo);
		
		JLabel lblCreated = new JLabel("Created By");
		GridBagConstraints gbc_lblCreated = new GridBagConstraints();
		gbc_lblCreated.gridwidth = 2;
		gbc_lblCreated.gridx = 0;
		gbc_lblCreated.gridy = 1;
		panel_12.add(lblCreated, gbc_lblCreated);
		
		JImage pnlCARLiv = new JImage(Mars_Rover.class.getClass().getResourceAsStream("/uol.png"), new Dimension(-1, 100));
		pnlCARLiv.setBackground(new Color(229, 229, 229));
		GridBagConstraints gbc_pnlCARLiv = new GridBagConstraints();
		gbc_pnlCARLiv.gridx = 0;
		gbc_pnlCARLiv.gridy = 2;
		panel_12.add(pnlCARLiv, gbc_pnlCARLiv);
		
		JImage pnlCAR = new JImage(Mars_Rover.class.getClass().getResourceAsStream("/autonomous.png"), new Dimension(-1, 40));
		pnlCAR.setBackground(new Color(229, 229, 229));
		GridBagConstraints gbc_pnlCAR = new GridBagConstraints();
		gbc_pnlCAR.gridx = 1;
		gbc_pnlCAR.gridy = 2;
		panel_12.add(pnlCAR, gbc_pnlCAR);
		
		JLabel lblSupported = new JLabel("Supported By");
		GridBagConstraints gbc_lblSupported = new GridBagConstraints();
		gbc_lblSupported.gridwidth = 2;
		gbc_lblSupported.gridx = 0;
		gbc_lblSupported.gridy = 3;
		panel_12.add(lblSupported, gbc_lblSupported);
		
		JImage pnlSTFC = new JImage(Mars_Rover.class.getClass().getResourceAsStream("/stfc.png"), new Dimension(-1, 100));
		pnlSTFC.setBackground(new Color(17, 57, 101));
		GridBagConstraints gbc_pnlSTFC = new GridBagConstraints();
		gbc_pnlSTFC.gridwidth = 2;
		gbc_pnlSTFC.gridx = 0;
		gbc_pnlSTFC.gridy = 4;
		panel_12.add(pnlSTFC, gbc_pnlSTFC);
		
		JLabel lblImg = new JLabel("Mars Image provided by: NASA/JPL/University of Arizona");
		GridBagConstraints gbc_lblImg = new GridBagConstraints();
		gbc_lblImg.gridwidth = 2;
		gbc_lblImg.gridx = 0;
		gbc_lblImg.gridy = 5;
		panel_12.add(lblImg, gbc_lblImg);
		
		mainTabPane.addTab("About", null, panel_12, null);
	}
	
	public void setUpBottomPanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frmMarsRover.getContentPane().add(panel, BorderLayout.SOUTH);
		
		lblStatus = new JLabel("Disconnected");
		panel.add(lblStatus);
		
		prgConnect = new JProgressBar();
		prgConnect.setVisible(false);
		prgConnect.setIndeterminate(true);
		panel.add(prgConnect);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(10, 25));
		panel.add(separator);
		
		lblDistance = new JLabel("Distance - 0.000000");
		panel.add(lblDistance);
		
		JSeparator separator_3 = new JSeparator();
		panel.add(separator_3);		
		
		lblRGB = new JLabel("RGB - 255 255 255");
		lblRGB.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblRGB);
		
		JSeparator separator_1 = new JSeparator();
		panel.add(separator_1);
		
		lblBeliefs = new JLabel("Beliefs - []");
		panel.add(lblBeliefs);	
	}

}

package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Creates a JPanel for a rule
 */
public class RulePanel 
{
	public static JPanel createRulePanel(int i, JCheckBox chkRule, JComboBox<RuleActionItem> cboObs, JComboBox<BeliefItem> cboType, 
			JComboBox<RuleActionItem> cboAction1, JComboBox<RuleActionItem> cboAction2, JComboBox<RuleActionItem> cboAction3)
	{
		JPanel panel_10 = new JPanel();
		panel_10.setBackground(new Color(220, 220, 220));
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{0, 0};
		gbl_panel_10.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_10.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_10.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
		
		JLabel label = new JLabel("Do rule");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_10.add(label, gbc_label);
		
		GridBagConstraints gbc_chkR3 = new GridBagConstraints();
		gbc_chkR3.fill = GridBagConstraints.BOTH;
		gbc_chkR3.insets = new Insets(0, 0, 5, 0);
		gbc_chkR3.gridx = 1;
		gbc_chkR3.gridy = 0;
		panel_10.add(chkRule, gbc_chkR3);
		
		JLabel label_1 = new JLabel("If ");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		panel_10.add(label_1, gbc_label_1);
		
		cboType.setModel(new DefaultComboBoxModel<BeliefItem>(BeliefItem.getBeliefsItems(i)));
		GridBagConstraints gbc_cboType = new GridBagConstraints();
		gbc_cboType.fill = GridBagConstraints.BOTH;
		gbc_cboType.insets = new Insets(0, 0, 5, 0);
		gbc_cboType.gridx = 1;
		gbc_cboType.gridy = 1;
		panel_10.add(cboType, gbc_cboType);
		
		JLabel label_2 = new JLabel("has");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		panel_10.add(label_2, gbc_label_2);
		
		cboObs.setModel(new DefaultComboBoxModel<RuleActionItem>(RuleActionItem.getObstacleStates(i)));
		GridBagConstraints gbc_cboR3Obs = new GridBagConstraints();
		gbc_cboR3Obs.fill = GridBagConstraints.BOTH;
		gbc_cboR3Obs.insets = new Insets(0, 0, 5, 0);
		gbc_cboR3Obs.gridx = 1;
		gbc_cboR3Obs.gridy = 2;
		panel_10.add(cboObs, gbc_cboR3Obs);
		
		JLabel label_5 = new JLabel("then do");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.EAST;
		gbc_label_5.insets = new Insets(0, 0, 5, 5);
		gbc_label_5.gridx = 0;
		gbc_label_5.gridy = 3;
		panel_10.add(label_5, gbc_label_5);
		
		cboAction1.setModel(new DefaultComboBoxModel<RuleActionItem>(RuleActionItem.getActions(i)));
		GridBagConstraints gbc_cboR3A1 = new GridBagConstraints();
		gbc_cboR3A1.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboR3A1.insets = new Insets(0, 0, 5, 0);
		gbc_cboR3A1.gridx = 1;
		gbc_cboR3A1.gridy = 3;
		panel_10.add(cboAction1, gbc_cboR3A1);
		
		JLabel label_3 = new JLabel("then");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 4;
		panel_10.add(label_3, gbc_label_3);
		
		cboAction2.setModel(new DefaultComboBoxModel<RuleActionItem>(RuleActionItem.getActions(i)));
		GridBagConstraints gbc_cboR3A2 = new GridBagConstraints();
		gbc_cboR3A2.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboR3A2.insets = new Insets(0, 0, 5, 0);
		gbc_cboR3A2.gridx = 1;
		gbc_cboR3A2.gridy = 4;
		panel_10.add(cboAction2, gbc_cboR3A2);
		
		JLabel label_4 = new JLabel("then");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.insets = new Insets(0, 0, 0, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 5;
		panel_10.add(label_4, gbc_label_4);
		
		cboAction3.setModel(new DefaultComboBoxModel<RuleActionItem>(RuleActionItem.getActions(i)));
		GridBagConstraints gbc_cboR3A3 = new GridBagConstraints();
		gbc_cboR3A3.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboR3A3.gridx = 1;
		gbc_cboR3A3.gridy = 5;
		panel_10.add(cboAction3, gbc_cboR3A3);
		
		return panel_10;
	}
}

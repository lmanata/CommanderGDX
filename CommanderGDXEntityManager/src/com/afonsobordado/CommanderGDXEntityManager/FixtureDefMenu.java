package com.afonsobordado.CommanderGDXEntityManager;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.FixtureDefFile;
import com.afonsobordado.CommanderGDX.vars.B2DVars;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FixtureDefMenu extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JCheckBox cb_checkbox;
	private JCheckBox cb_checkbox_6;
	private JCheckBox cb_checkbox_5;
	private JCheckBox cb_checkbox_4;
	private JCheckBox cb_checkbox_3;
	private JCheckBox cb_checkbox_1;
	private JCheckBox cb_checkbox_2;
	private JCheckBox checkBox_7;
	private JCheckBox checkBox_6;
	private JCheckBox checkBox_5;
	private JCheckBox checkBox_3;
	private JCheckBox checkBox_2;
	private JCheckBox checkBox_1;
	private JCheckBox checkBox;
	private JCheckBox checkBox_4;
	private JCheckBox cb_checkbox_7;
	private FDefExtender parent;

	/**
	 * Create the frame.
	 */
	public FixtureDefMenu(FDefExtender fde) {
		this.parent = fde;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 61, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblDensity = new JLabel("Density:");
		GridBagConstraints gbc_lblDensity = new GridBagConstraints();
		gbc_lblDensity.anchor = GridBagConstraints.EAST;
		gbc_lblDensity.insets = new Insets(0, 0, 5, 5);
		gbc_lblDensity.gridx = 0;
		gbc_lblDensity.gridy = 0;
		contentPane.add(lblDensity, gbc_lblDensity);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 8;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Friction:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 8;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		contentPane.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Restituion:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.gridwidth = 8;
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		contentPane.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("categoryBits:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 3;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		cb_checkbox = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox = new GridBagConstraints();
		gbc_cb_checkbox.insets = new Insets(0, 0, 5, 5);
		gbc_cb_checkbox.gridx = 1;
		gbc_cb_checkbox.gridy = 3;
		contentPane.add(cb_checkbox, gbc_cb_checkbox);
		
		cb_checkbox_6 = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox_6 = new GridBagConstraints();
		gbc_cb_checkbox_6.insets = new Insets(0, 0, 5, 5);
		gbc_cb_checkbox_6.gridx = 2;
		gbc_cb_checkbox_6.gridy = 3;
		contentPane.add(cb_checkbox_6, gbc_cb_checkbox_6);
		
		cb_checkbox_5 = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox_5 = new GridBagConstraints();
		gbc_cb_checkbox_5.insets = new Insets(0, 0, 5, 5);
		gbc_cb_checkbox_5.gridx = 3;
		gbc_cb_checkbox_5.gridy = 3;
		contentPane.add(cb_checkbox_5, gbc_cb_checkbox_5);
		
		cb_checkbox_4 = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox_4 = new GridBagConstraints();
		gbc_cb_checkbox_4.insets = new Insets(0, 0, 5, 5);
		gbc_cb_checkbox_4.gridx = 4;
		gbc_cb_checkbox_4.gridy = 3;
		contentPane.add(cb_checkbox_4, gbc_cb_checkbox_4);
		
		cb_checkbox_3 = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox_3 = new GridBagConstraints();
		gbc_cb_checkbox_3.insets = new Insets(0, 0, 5, 5);
		gbc_cb_checkbox_3.gridx = 5;
		gbc_cb_checkbox_3.gridy = 3;
		contentPane.add(cb_checkbox_3, gbc_cb_checkbox_3);
		
		cb_checkbox_2 = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox_2 = new GridBagConstraints();
		gbc_cb_checkbox_2.insets = new Insets(0, 0, 5, 5);
		gbc_cb_checkbox_2.gridx = 6;
		gbc_cb_checkbox_2.gridy = 3;
		contentPane.add(cb_checkbox_2, gbc_cb_checkbox_2);
		
		cb_checkbox_1 = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox_1 = new GridBagConstraints();
		gbc_cb_checkbox_1.insets = new Insets(0, 0, 5, 5);
		gbc_cb_checkbox_1.gridx = 7;
		gbc_cb_checkbox_1.gridy = 3;
		contentPane.add(cb_checkbox_1, gbc_cb_checkbox_1);
		
		cb_checkbox_7 = new JCheckBox("");
		GridBagConstraints gbc_cb_checkbox_7 = new GridBagConstraints();
		gbc_cb_checkbox_7.insets = new Insets(0, 0, 5, 0);
		gbc_cb_checkbox_7.gridx = 8;
		gbc_cb_checkbox_7.gridy = 3;
		contentPane.add(cb_checkbox_7, gbc_cb_checkbox_7);
		
		
		
		JLabel lblNewLabel_3 = new JLabel("maskBits:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 4;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		checkBox_7 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_7 = new GridBagConstraints();
		gbc_checkBox_7.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_7.gridx = 1;
		gbc_checkBox_7.gridy = 4;
		contentPane.add(checkBox_7, gbc_checkBox_7);
		
		checkBox_6 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_6 = new GridBagConstraints();
		gbc_checkBox_6.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_6.gridx = 2;
		gbc_checkBox_6.gridy = 4;
		contentPane.add(checkBox_6, gbc_checkBox_6);
		
		checkBox_5 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_5 = new GridBagConstraints();
		gbc_checkBox_5.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_5.gridx = 3;
		gbc_checkBox_5.gridy = 4;
		contentPane.add(checkBox_5, gbc_checkBox_5);
		
		checkBox_4 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_4 = new GridBagConstraints();
		gbc_checkBox_4.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_4.gridx = 4;
		gbc_checkBox_4.gridy = 4;
		contentPane.add(checkBox_4, gbc_checkBox_4);
		
		checkBox_3 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_3 = new GridBagConstraints();
		gbc_checkBox_3.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_3.gridx = 5;
		gbc_checkBox_3.gridy = 4;
		contentPane.add(checkBox_3, gbc_checkBox_3);
		
		checkBox_2 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_2 = new GridBagConstraints();
		gbc_checkBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_2.gridx = 6;
		gbc_checkBox_2.gridy = 4;
		contentPane.add(checkBox_2, gbc_checkBox_2);
		
		checkBox_1 = new JCheckBox("");
		GridBagConstraints gbc_checkBox_1 = new GridBagConstraints();
		gbc_checkBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1.gridx = 7;
		gbc_checkBox_1.gridy = 4;
		contentPane.add(checkBox_1, gbc_checkBox_1);
		
		checkBox = new JCheckBox("");
		GridBagConstraints gbc_checkBox = new GridBagConstraints();
		gbc_checkBox.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox.gridx = 8;
		gbc_checkBox.gridy = 4;
		contentPane.add(checkBox, gbc_checkBox);
		
		
		checkBox_7.setToolTipText("Undefined");
		cb_checkbox.setToolTipText("Undefined");
		checkBox_6.setToolTipText("Undefined");
		cb_checkbox_6.setToolTipText("Undefined");
		checkBox_5.setToolTipText("Undefined");
		cb_checkbox_5.setToolTipText("Undefined");
		checkBox_4.setToolTipText("Undefined");
		cb_checkbox_4.setToolTipText("Undefined");
		checkBox_3.setToolTipText("Undefined");
		cb_checkbox_3.setToolTipText("Undefined");
		checkBox_2.setToolTipText("GROUND");
		cb_checkbox_2.setToolTipText("GROUND");
		checkBox_1.setToolTipText("PLAYER");
		cb_checkbox_1.setToolTipText("PLAYER");
		checkBox.setToolTipText("Undefined");
		cb_checkbox_7.setToolTipText("Undefined");
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 4;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 5;
		contentPane.add(btnNewButton, gbc_btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFields();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridwidth = 5;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 4;
		gbc_btnNewButton_1.gridy = 5;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);

		resetFields();
		
	}
	
	void resetFields(){
			FixtureDefFile fdf = parent.getFdf();
			if(fdf == null){
				System.err.println("No FDF");
				return;
			}
			textField.setText(Float.toString(fdf.getDensity()));
			textField_1.setText(Float.toString(fdf.getFriction()));
			textField_2.setText(Float.toString(fdf.getRestitution()));
			
			cb_checkbox.setSelected	 ((fdf.getCb() & 0x01) != 0);
			cb_checkbox_1.setSelected((fdf.getCb() & 0x02) != 0);
			cb_checkbox_2.setSelected((fdf.getCb() & 0x04) != 0);
			cb_checkbox_3.setSelected((fdf.getCb() & 0x08) != 0);
			cb_checkbox_4.setSelected((fdf.getCb() & 0x10) != 0);
			cb_checkbox_5.setSelected((fdf.getCb() & 0x20) != 0);
			cb_checkbox_6.setSelected((fdf.getCb() & 0x40) != 0);
			cb_checkbox_7.setSelected((fdf.getCb() & 0x80) != 0);
			
			checkBox.setSelected  ((fdf.getMb() & 0x01) != 0);
			checkBox_1.setSelected((fdf.getMb() & 0x02) != 0);
			checkBox_2.setSelected((fdf.getMb() & 0x04) != 0);
			checkBox_3.setSelected((fdf.getMb() & 0x08) != 0);
			checkBox_4.setSelected((fdf.getMb() & 0x10) != 0);
			checkBox_5.setSelected((fdf.getMb() & 0x20) != 0);
			checkBox_6.setSelected((fdf.getMb() & 0x40) != 0);
			checkBox_7.setSelected((fdf.getMb() & 0x80) != 0);
	}
	
	void save(){

			short cb = (short) ((cb_checkbox.isSelected()   ? 0x01 : 0x00) +
							    (cb_checkbox_1.isSelected() ? 0x02 : 0x00) +
						 	    (cb_checkbox_2.isSelected() ? 0x04 : 0x00) +
							    (cb_checkbox_3.isSelected() ? 0x08 : 0x00) +
						 	    (cb_checkbox_4.isSelected() ? 0x10 : 0x00) +
							    (cb_checkbox_5.isSelected() ? 0x20 : 0x00) +
							    (cb_checkbox_6.isSelected() ? 0x40 : 0x00) +
							    (cb_checkbox_7.isSelected() ? 0x80 : 0x00));
			
			short mb = (short) ((checkBox.isSelected()   ? 0x01 : 0x00) +
							    (checkBox_1.isSelected() ? 0x02 : 0x00) +
						 	    (checkBox_2.isSelected() ? 0x04 : 0x00) +
							    (checkBox_3.isSelected() ? 0x08 : 0x00) +
						 	    (checkBox_4.isSelected() ? 0x10 : 0x00) +
							    (checkBox_5.isSelected() ? 0x20 : 0x00) +
							    (checkBox_6.isSelected() ? 0x40 : 0x00) +
							    (checkBox_7.isSelected() ? 0x80 : 0x00));
			
			FixtureDefFile fdf = new FixtureDefFile(Float.parseFloat(textField.getText()),
													Float.parseFloat(textField_1.getText()),
													Float.parseFloat(textField_2.getText()),
													cb,
													mb);
			parent.setFdf(fdf);
			this.dispose();
	}
}

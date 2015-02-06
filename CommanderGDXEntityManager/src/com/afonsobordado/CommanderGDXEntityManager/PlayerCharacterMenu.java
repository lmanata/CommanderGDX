package com.afonsobordado.CommanderGDXEntityManager;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JTextField;
import javax.swing.JButton;

import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.PlayerCharacterFile;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PlayerCharacterMenu extends FDefExtender {

	private String file;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JButton btnNewButton;
	private JButton btnNewButton_1;

	/**
	 * Create the frame.
	 */
	public PlayerCharacterMenu(String file) {
		this.file = file;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 267);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Name:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;

		
		JLabel lblNewLabel_1 = new JLabel("legsIdle:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;

		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;		

		
		JLabel lblNewLabel_2 = new JLabel("legsJump:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;

		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.gridwidth = 2;
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;

		
		JLabel lblNewLabel_3 = new JLabel("Legs:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;

		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.gridwidth = 2;
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 3;

		
		JLabel lblNewLabel_4 = new JLabel("Torso:");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 4;
		
		
		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.gridwidth = 2;
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 4;

		
		JLabel lblNewLabel_5 = new JLabel("Arm:");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 5;
		contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.gridwidth = 2;
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 5;

		
		JLabel lblNewLabel_6 = new JLabel("TorsoPin:");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 6;

		textField_6 = new JTextField();
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 5);
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 1;
		gbc_textField_6.gridy = 6;

		
		textField_7 = new JTextField();
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.insets = new Insets(0, 0, 5, 0);
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.gridx = 2;
		gbc_textField_7.gridy = 6;
		
		JLabel lblNewLabel_7 = new JLabel("ArmPin:");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 0;
		gbc_lblNewLabel_7.gridy = 7;
		contentPane.add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		textField_8 = new JTextField();
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.insets = new Insets(0, 0, 5, 5);
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.gridx = 1;
		gbc_textField_8.gridy = 7;

		
		textField_9 = new JTextField();
		GridBagConstraints gbc_textField_9 = new GridBagConstraints();
		gbc_textField_9.insets = new Insets(0, 0, 5, 0);
		gbc_textField_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_9.gridx = 2;
		gbc_textField_9.gridy = 7;

		
		btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 8;
		
		
		btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFields();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 8;
		
		resetFields();
		
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		contentPane.add(textField, gbc_textField);//name
		textField.setColumns(10);
		
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		contentPane.add(textField_1, gbc_textField_1);//legsidle
		textField_1.setColumns(10);
		
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		contentPane.add(textField_2, gbc_textField_2);//legsJump
		textField_2.setColumns(10);
		
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
		contentPane.add(textField_3, gbc_textField_3);//legs
		textField_3.setColumns(10);
		
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);
		contentPane.add(textField_4, gbc_textField_4);//torso
		textField_4.setColumns(10);
		
		contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);
		contentPane.add(textField_5, gbc_textField_5);//arm
		textField_5.setColumns(10);
		
		contentPane.add(lblNewLabel_6, gbc_lblNewLabel_6);
		contentPane.add(textField_6, gbc_textField_6);//torso pinx
		textField_6.setColumns(10);
		contentPane.add(textField_7, gbc_textField_7);//torso piny
		textField_7.setColumns(10);
		
		contentPane.add(lblNewLabel_7, gbc_lblNewLabel_7);
		contentPane.add(textField_8, gbc_textField_8);//armpinx
		textField_8.setColumns(10);
		contentPane.add(textField_9, gbc_textField_9);//armpiny
		textField_9.setColumns(10);
		
		contentPane.add(btnNewButton, gbc_btnNewButton);
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);

	}

	
	
	
	void resetFields(){
		Input input = null;
		try{
			input = new Input(new FileInputStream(file));
			PlayerCharacterFile pcf = Main.kryo.readObject(input, PlayerCharacterFile.class);
			textField.setText(pcf.getName());
			textField_1.setText(pcf.getLegsIdle());
			textField_2.setText(pcf.getLegsJump());
			textField_3.setText(pcf.getLegs());
			textField_4.setText(pcf.getTorso());
			textField_5.setText(pcf.getArm());
			textField_6.setText(Float.toString(pcf.getTorsoPin().x));
			textField_7.setText(Float.toString(pcf.getTorsoPin().y));
			textField_8.setText(Float.toString(pcf.getArmPin().x));
			textField_9.setText(Float.toString(pcf.getArmPin().y));
		}catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}catch (KryoException ke) {
			System.err.println("Found old version, starting new!");
		}finally{
			input.close();
		}
	}
	
	void save(){
		
		Output output = null;
		try{
			output = new Output(new FileOutputStream(file));
			PlayerCharacterFile pcf = new PlayerCharacterFile(textField.getText(),
															  textField_1.getText(),
															  textField_2.getText(),
															  textField_3.getText(),
															  textField_4.getText(),
															  textField_5.getText(),
															  new Vector2(Float.parseFloat(textField_6.getText()),
																	  	  Float.parseFloat(textField_7.getText())),
															  new Vector2(Float.parseFloat(textField_8.getText()),
																		  Float.parseFloat(textField_9.getText()))
															  );
			Main.kryo.writeObject(output, pcf);
			output.flush();
		}catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}finally{
			output.flush();
			output.close();
		}
	}
}

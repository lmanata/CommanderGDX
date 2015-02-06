package com.afonsobordado.CommanderGDXEntityManager;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.afonsobordado.CommanderGDX.files.WeaponFile;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WeaponMenu extends FDefExtender{

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JLabel lblNewLabel_5;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JCheckBox chckbxNewCheckBox;
	private String file;

	/**
	 * Create the frame.
	 */
	public WeaponMenu(String file) {
		this.file = file;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 307, 219);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, -1, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Name:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 2;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;

		
		JLabel lblNewLabel_1 = new JLabel("Animation:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;

		
		JLabel lblNewLabel_2 = new JLabel("Bullets/S:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.gridwidth = 2;
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		
		JLabel lblNewLabel_3 = new JLabel("SOP:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		
		chckbxNewCheckBox = new JCheckBox("");
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox.gridx = 1;
		gbc_chckbxNewCheckBox.gridy = 3;
		
		JLabel lblNewLabel_4 = new JLabel("weaponPin:");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 4;

		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 4;

		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 2;
		gbc_textField_4.gridy = 4;
		
		lblNewLabel_5 = new JLabel("Bullet:");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 5;
		
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.gridwidth = 2;
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 5;
		
		btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 6;
		
		
		btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFields();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 6;
		
		

		resetFields();
		
		
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		contentPane.add(textField, gbc_textField); //name
		textField.setColumns(10);
		
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		contentPane.add(textField_1, gbc_textField_1);//animation
		textField_1.setColumns(10);
		
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		contentPane.add(textField_2, gbc_textField_2); //bullets/s
		textField_2.setColumns(10);
		
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
		contentPane.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);//SOP
		
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);
		contentPane.add(textField_3, gbc_textField_3);//weaponpin x
		textField_3.setColumns(10);
		contentPane.add(textField_4, gbc_textField_4);//weaponpin y
		textField_4.setColumns(10);
		
		contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);
		contentPane.add(textField_5, gbc_textField_5); //bullet
		textField_5.setColumns(10);
		
		contentPane.add(btnNewButton, gbc_btnNewButton);
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		
	}
	
	void resetFields(){
		Input input = null;
		try{
			input = new Input(new FileInputStream(file));
			WeaponFile wf = Main.kryo.readObject(input, WeaponFile.class);
			textField.setText(wf.getName());
			textField_1.setText(wf.getAnimation());
			textField_2.setText(Float.toString(wf.getBulletsPerSec()));
			chckbxNewCheckBox.setSelected(wf.isShootOnPress());
			textField_3.setText(Float.toString(wf.getWeaponPin().x));
			textField_4.setText(Float.toString(wf.getWeaponPin().y));
			textField_5.setText(wf.getBullet());
			System.out.println("S: " + wf.toString() + 
					   "\nName: " + wf.getName() +
					   "\nAnimation: " + wf.getAnimation() +
					   "\nbulletsPerSec: "+ wf.getBulletsPerSec()+
					   "\nshootOnPress:"+wf.isShootOnPress()+
					   "\nweaponPin: "+wf.getWeaponPin()+
					   "\nbullet: " + wf.getBullet());
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
			WeaponFile wf = new WeaponFile(textField.getText(),
										   textField_1.getText(),
										   Float.parseFloat(textField_2.getText()),
										   chckbxNewCheckBox.isSelected(),
										   new Vector2(
												   		Float.parseFloat(textField_3.getText()),
												   		Float.parseFloat(textField_4.getText())
												   	  ),
										   textField_5.getText()
										   );
			
			Main.kryo.writeObject(output, wf);
			output.flush();
			System.out.println("S: " + wf.toString() + 
							   "\nName: " + wf.getName() +
							   "\nAnimation: " + wf.getAnimation() +
							   "\nbulletsPerSec: "+ wf.getBulletsPerSec()+
							   "\nshootOnPress:"+wf.isShootOnPress()+
							   "\nweaponPin: "+wf.getWeaponPin()+
							   "\nbullet: " + wf.getBullet());
		}catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}finally{
			output.flush();
			output.close();
		}
	}

}

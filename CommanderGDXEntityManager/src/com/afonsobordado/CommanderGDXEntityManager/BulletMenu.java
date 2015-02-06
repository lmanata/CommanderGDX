package com.afonsobordado.CommanderGDXEntityManager;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javafx.scene.Parent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JButton;

import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.FixtureDefFile;
import com.afonsobordado.CommanderGDX.files.WeaponFile;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BulletMenu extends FDefExtender {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private String file;
	private JTextField textField_5;
	private BulletFile bf;

	/**
	 * Create the frame.
	 */
	public BulletMenu(String file) {
		this.file = file;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 283, 274);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 102, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;

		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;

		
		JLabel lblNewLabel_2 = new JLabel("Speed");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;

		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.gridwidth = 2;
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;

		
		JLabel lblNewLabel_3 = new JLabel("Effects:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;

		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.gridwidth = 2;
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 3;

		
		JLabel lblNewLabel_4 = new JLabel("Lifespan:");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 4;

		
		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridwidth = 2;
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 4;

		
		JButton btnNewButton_1 = new JButton("Save");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		JLabel lblBodyscale = new JLabel("BodyScale:");
		GridBagConstraints gbc_lblBodyscale = new GridBagConstraints();
		gbc_lblBodyscale.anchor = GridBagConstraints.EAST;
		gbc_lblBodyscale.insets = new Insets(0, 0, 5, 5);
		gbc_lblBodyscale.gridx = 0;
		gbc_lblBodyscale.gridy = 5;
		
		
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.gridwidth = 2;
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 5;
		
		JLabel lblFdf = new JLabel("FixtureDef:");
		GridBagConstraints gbc_lblFdf = new GridBagConstraints();
		gbc_lblFdf.anchor = GridBagConstraints.EAST;
		gbc_lblFdf.insets = new Insets(0, 0, 5, 5);
		gbc_lblFdf.gridx = 0;
		gbc_lblFdf.gridy = 6;
		contentPane.add(lblFdf, gbc_lblFdf);
		
		JButton btnNewButton_2 = new JButton("Edit");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openMenu();
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.gridwidth = 2;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 6;
		contentPane.add(btnNewButton_2, gbc_btnNewButton_2);

		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 7;
		
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		
		
		JButton btnNewButton = new JButton("Reset");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFields();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 7;
		contentPane.add(btnNewButton, gbc_btnNewButton);

		
		
		
		resetFields();
		
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		contentPane.add(textField, gbc_textField);//name
		textField.setColumns(10);
		
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		contentPane.add(textField_1, gbc_textField_1); // animation
		textField_1.setColumns(10);
		
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		contentPane.add(textField_2, gbc_textField_2);//speed
		textField_2.setColumns(10);
		
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
		contentPane.add(textField_3, gbc_textField_3);//effects
		textField_3.setColumns(10);
		
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);
		contentPane.add(textField_4, gbc_textField_4);//lifespan
		textField_4.setColumns(10);
		
		contentPane.add(lblBodyscale, gbc_lblBodyscale);
		contentPane.add(textField_5, gbc_textField_5); // body scale
		textField_5.setColumns(10);
		

		
	}

	void resetFields(){
		Input input = null;
		try{
			input = new Input(new FileInputStream(file));
			bf = Main.kryo.readObject(input, BulletFile.class);
			this.setFdf(bf.getFdf());
			textField.setText(bf.getName());
			textField_1.setText(bf.getAnimation());
			textField_2.setText(Float.toString(bf.getSpeed()));
			textField_3.setText(Short.toString(bf.getEffects()));
			textField_4.setText(Float.toString(bf.getLifespan()));
			textField_5.setText(Float.toString(bf.getBodyScale()));
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
		if(this.getFdf() == null){
			System.err.println("No FixtureDef!\nNOT SAVED!");
			return;
		}
		try{
			output = new Output(new FileOutputStream(file));
			bf = new BulletFile(textField.getText(),
							   textField_1.getText(),
							   Float.parseFloat(textField_2.getText()),
							   Short.parseShort(textField_3.getText()),
							   Float.parseFloat(textField_4.getText()),
							   Float.parseFloat(textField_5.getText()),
							   this.getFdf());
			
			Main.kryo.writeObject(output, bf);
			output.flush();
		}catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}finally{
			output.flush();
			output.close();
		}
	}
	
	void openMenu(){
		FixtureDefMenu fdm = new FixtureDefMenu(this);
		fdm.setVisible(true);
	}
	
	
}

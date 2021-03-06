package com.afonsobordado.CommanderGDXEntityManager;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import com.afonsobordado.CommanderGDX.files.FixtureDefFile;
import com.afonsobordado.CommanderGDX.files.PlayerCharacterFile;
import com.afonsobordado.CommanderGDX.files.BulletFile;
import com.afonsobordado.CommanderGDX.files.WeaponFile;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Main {
	private boolean classSelectMenu = false;
	private JFrame frame;
	private JList list;
	public static Kryo kryo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		kryo = new Kryo();
		kryo.register(Vector2.class);
		kryo.register(WeaponFile.class);
		kryo.register(BulletFile.class);
		kryo.register(PlayerCharacterFile.class);
		kryo.register(FixtureDefFile.class);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 216, 258);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				updateList();
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		resetList();

		list.setVisibleRowCount(-1);
		frame.getContentPane().add(list, BorderLayout.CENTER);
		
		JButton back = new JButton("< BACK");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetList();
			}
		});
		frame.getContentPane().add(back, BorderLayout.SOUTH);
	}
	
	void resetList(){
		if(classSelectMenu == true)
			System.exit(0);
		
		classSelectMenu = true;
		String[] sa = new String[3];
		sa[0] = "WeaponClass";
		sa[1] = "BulletClass";
		sa[2] = "PlayerCharacterClass";
		list.setListData(sa);
	}
	
	void updateList(){
		int index = list.getSelectedIndex();
		if(classSelectMenu){
			String dir = null;
			switch(index){
				case 0:
					dir = "../res/weapons";
					break;
				case 1:
					dir = "../res/bullets";
					break;
				case 2:
					dir = "../res/playerCharacter";
					break;
				default:
					break;
			}
			if(dir == null){
				System.err.println("No dir could be found");
			}
			
			File folder = new File(dir);
			File[] fileList = folder.listFiles();
			String[] stringList = new String[(int) fileList.length];
			
			for(int i = 0; i<fileList.length; i++){
				stringList[i] = fileList[i].getPath();
			}
			
			list.setListData(stringList);
			classSelectMenu = false;
		}else{
			String fileName = (String) list.getSelectedValue();
			System.out.println(fileName);

			if(fileName.endsWith(".weapon")){
				WeaponMenu wm = new WeaponMenu(fileName);
				wm.setVisible(true);
			}else if(fileName.endsWith(".bullet")){
				BulletMenu bm = new BulletMenu(fileName);
				bm.setVisible(true);
			}else if(fileName.endsWith(".pcf")){
				PlayerCharacterMenu pcm = new PlayerCharacterMenu(fileName);
				pcm.setVisible(true);
			}else{
				System.err.println("Unrecognized Option!");
				System.exit(1);
			}
		}
	}

}

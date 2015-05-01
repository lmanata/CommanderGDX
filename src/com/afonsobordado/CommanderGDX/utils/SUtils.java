package com.afonsobordado.CommanderGDX.utils;

import java.util.ArrayList;

import com.afonsobordado.CommanderGDX.files.HashFileMap;
import com.badlogic.gdx.files.FileHandle;

public class SUtils {

	public static ArrayList<HashFileMap> checkHash(HashFileMap[] orighfm, HashFileMap[] newhfm) {
		if(newhfm == null || orighfm == null) return null;
		
		ArrayList<HashFileMap> failList = new ArrayList<HashFileMap>();
		
		
		outerLoop: for(HashFileMap hfmo: orighfm){
			for(HashFileMap nhfm: newhfm){
				if(hfmo.equals(nhfm)){
					continue outerLoop;
				}
			}
			//if we reach here that means that a file that is on the servers list isn't on the clients list
			//or the hash check failed
			failList.add(hfmo);
		}
		return failList;
	}
	
	public static HashFileMap[] genHashFileMapList(FileHandle dir){
		ArrayList<FileHandle> fhal = getSubDirFiles(dir);
		HashFileMap[] hfm = new HashFileMap[fhal.size()];
		int hfmCounter = 0;
		for(FileHandle fh: fhal){
			HashFileMap ll = new HashFileMap();
			ll.setFile(fh.path());
			ll.setHash(FNVHash(fh.readBytes()));
			hfm[hfmCounter++] = ll;
		}
		return hfm;
	}
	
	public static ArrayList<FileHandle> getSubDirFiles(FileHandle begin){
		
	    FileHandle[] dirList = begin.list();
	    ArrayList<FileHandle> ret = new ArrayList<FileHandle>();
	    
	    for (FileHandle f : dirList){
	        if (f.isDirectory()){
	            for(FileHandle d: getSubDirFiles(f)){
	            	ret.add(d);
	            }
	        }else{
	            ret.add(f);
	        }
	    }
	    
		return ret;
	}
	
	//slightly modified copy of the fnvhash function found on the website below
	//https://github.com/manuelbua/libgdx-contribs/blob/master/utils/src/main/java/com/bitfire/utils/Hash.java
	public static long FNVHash( byte[] b ) {
		long fnv_prime = 0x811C9DC5;
		long hash = 0;
		
		for( int i = 0; i < b.length; i++ ) {
			hash *= fnv_prime;
			hash ^= b[i];
		}

		return hash;
	}

}

package com.afonsobordado.CommanderGDX.files;

public class HashFileMap {
	private String file;
	private long hash;

	public HashFileMap(String file, long hash){
		this.file = file;
		this.hash = hash;
	}
	
	public HashFileMap() {}

	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}

	public long getHash() {
		return hash;
	}

	public void setHash(long hash) {
		this.hash = hash;
	}
	
	public boolean equals(Object o){
		if(o == null || !(o instanceof HashFileMap)) return false;
		HashFileMap newf = (HashFileMap) o;
		return (this.hash == newf.hash);
	}

	
}

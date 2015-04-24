package com.afonsobordado.CommanderGDX.handlers;

public class ActionStatus{
	private boolean down;
	private boolean press;
	
	public ActionStatus(boolean down, boolean press){
		this.down = down;
		this.press = press;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof ActionStatus)) return false;
		ActionStatus as = (ActionStatus) o;
		return (as.down == this.down &&
			    as.press == this.press);
	}
	
	public void update(ActionStatus newStatus){
		this.down = newStatus.down;
		this.press = newStatus.press;
	}
	
	public boolean isDown() {
		return down;
	}

	public boolean isPress() {
		return press;
	}

}
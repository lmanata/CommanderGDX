package com.afonsobordado.CommanderGDX.handlers;

import java.util.HashMap;

import com.afonsobordado.CommanderGDX.vars.Action;


public class ActionList {
	private HashMap<Action,ActionStatus> ActionList;
	public ActionList(){
		ActionList = new HashMap<Action,ActionStatus>(); 
	}

	public ActionStatus get(Action a){
		return ActionList.get(a);
	}
	
	public void update(Action a, boolean down, boolean press){
		if(ActionList.get(a) == null){
			ActionList.put(a, new ActionStatus(down,press));
			return;
		}
		ActionList.get(a).update(new ActionStatus(down,press));
	}

	public boolean needsUpdate(Action a, boolean down, boolean press) {
		if(ActionList.get(a) == null) return true;
		return !ActionList.get(a).equals(new ActionStatus(down,press));
	}
	
}

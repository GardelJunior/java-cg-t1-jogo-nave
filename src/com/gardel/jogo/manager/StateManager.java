package com.gardel.jogo.manager;

import java.util.HashMap;

import com.gardel.jogo.events.IKeyListener;
import com.gardel.jogo.states.IState;

public class StateManager implements IKeyListener{
	
	private static StateManager instance;

	public static StateManager getInstance() {
		if (instance == null) {
			instance = new StateManager();
		}
		return instance;
	}
	
	private HashMap<String, IState> stateMap = new HashMap<>();
	private IState current;
	
	public void registerState(String stateName, IState state) {
		stateMap.put(stateName, state);
	}
	
	public void setState(String stateName) {
		current = stateMap.get(stateName);
		current.init();
	}
	
	public void update() {
		if(current != null) current.update();
	}

	@Override
	public void onKeyEvent(int key, int action) {
		if(current instanceof IKeyListener) {
			((IKeyListener)current).onKeyEvent(key, action);
		}
	}
}

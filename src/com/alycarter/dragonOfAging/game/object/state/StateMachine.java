package com.alycarter.dragonOfAging.game.object.state;

import java.util.Stack;

public class StateMachine {
	//states currently held in game memory
	private Stack<State> states;
	
	public StateMachine() {
		states = new Stack<State>();
	}
	
	public State getCurrentState(){
		//if there is a state
		if(!states.isEmpty()){
			//return the state at the top of the stack
			return states.lastElement();
		}else{
			return null;
		}
	}
	
	public void update(){
		//get the current state
		State state = getCurrentState();
		//while we arnt at the bottom of the stack and the state needs removing
		while(state != null && state.needsRemoving()){
			//remove the current state
			states.pop();
			//get the next state
			state = getCurrentState();
		}
	}

	public void pushState(State state){
		//add the state to the top of the stack
		states.push(state);
	}
	
	public void removeAllStates(){
		//remove all states and mark them as being removed
		for(int i = states.size(); i > 0; i--){
			states.pop().markForRemoval();
		}
	}
}

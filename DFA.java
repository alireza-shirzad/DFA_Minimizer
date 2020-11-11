import java.util.ArrayList;
import java.util.Collections;

public class DFA {
	private int numOfStates;
	private int numOfAlphabet;
	private ArrayList<Integer> finalStates;
	private ArrayList<State> states;

	public DFA(int numOfStates, int numOfAlphabet, ArrayList<Integer> finalStates, ArrayList<State> states) {
		this.numOfStates = numOfStates;
		this.numOfAlphabet = numOfAlphabet;
		this.finalStates = finalStates;
		this.states = states;
	}

	public int getNumOfStates() {
		return numOfStates;
	}

	public void setNumOfStates(int numOfStates) {
		this.numOfStates = numOfStates;
	}

	public int getNumOfAlphabet() {
		return numOfAlphabet;
	}

	public void setNumOfAlphabet(int numOfAlphabet) {
		this.numOfAlphabet = numOfAlphabet;
	}

	public ArrayList<Integer> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(ArrayList<Integer> finalStates) {
		this.finalStates = finalStates;
	}

	public ArrayList<State> getStates() {
		return states;
	}

	public void setStates(ArrayList<State> states) {
		this.states = states;
	}


	public void reduceStateNum(ArrayList<Integer> deletedStates){
		ArrayList<Integer> currentStatesArray = new ArrayList<>() ;
		for (int i = 1; i < numOfStates+deletedStates.size()+1; i++) {
			currentStatesArray.add(i);
		}
		currentStatesArray.removeAll(deletedStates);
		Collections.sort(currentStatesArray);
		for (State state : states) {
			ArrayList<Integer> stateTransition = new ArrayList<>();
			for (Integer transition : state.getTranstitionFunction()) {
				stateTransition.add(currentStatesArray.indexOf(transition)+1);
			}
			state.setTranstitionFunction(stateTransition);
			state.setName(currentStatesArray.indexOf(state.getName())+1);
		}
		ArrayList<Integer> newFinalState = new ArrayList<>();
		for (Integer finalState : finalStates) {
			newFinalState.add(currentStatesArray.indexOf(finalState)+1);
		}
		finalStates = newFinalState;
	}


	@Override
	public String toString() {
		String output = "";
		output = output + numOfStates + " " + numOfAlphabet + "\n";
		if (finalStates.size()!=0) {
			for (int i = 0; i < finalStates.size(); i++) {
				if (i != finalStates.size() - 1) output = output + finalStates.get(i) + " ";
				else output = output + finalStates.get(i) + "\n";
			}
		} else output = output + "\n";

		for (int i = 0; i < states.size(); i++) {
			for (int j = 0; j <numOfAlphabet ; j++) {
				if (j!= numOfAlphabet-1) output = output + states.get(i).getTranstitionFunction().get(j) + " ";
				else output = output + states.get(i).getTranstitionFunction().get(j) ;
			}
			if (i!=states.size()-1) output = output + "\n";
		}
		return output;
	}
}

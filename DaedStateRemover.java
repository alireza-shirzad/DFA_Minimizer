import java.util.ArrayList;
import java.util.HashSet;

public class DaedStateRemover {
	private DFA dfa;
	public DaedStateRemover(DFA dfa){
		this.dfa = dfa;
	}
	public boolean remove(){
		ArrayList<State> states = dfa.getStates();
		ArrayList<Integer> deadStates = new ArrayList<>();
		for (int i = 0; i < states.size() ; i++) {
			HashSet<Integer> transitions = new HashSet<Integer> (states.get(i).getTranstitionFunction());
			if (transitions.size()==1 & states.get(i).getTranstitionFunction().get(0)==i+1 & !states.get(i).isFinal()) {
				deadStates.add(i+1);
			}
		}
		boolean output = false;
		if (deadStates.size()>1) {
			ArrayList<State> updatedStates = new ArrayList<>();
			dfa.setNumOfStates(dfa.getNumOfStates()-deadStates.size()+1);
			output = true;
			for (State state : states) {
				if (!deadStates.contains(state.getName()) | deadStates.get(0)==state.getName()) updatedStates.add(state);
			}
			dfa.setStates(updatedStates);
			for (State state : states) {
				ArrayList<Integer> transitions = new ArrayList<>();
				for (Integer transition : state.getTranstitionFunction()) {
					if (deadStates.contains(transition)) transitions.add(deadStates.get(0));
					else transitions.add(transition);
				}
				state.setTranstitionFunction(transitions);
			}
			//dfa.setNumOfStates(updatedStates.size());
			deadStates.remove(0);
			dfa.reduceStateNum(deadStates);
		}
		return output;
	}
}

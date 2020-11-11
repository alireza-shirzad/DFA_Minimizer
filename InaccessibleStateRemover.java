import java.util.ArrayList;
import java.util.Arrays;

public class InaccessibleStateRemover {
	private DFA dfa;
	private Accessable[] accessibilityStatus;
	public InaccessibleStateRemover(DFA dfa) {
		this.dfa = dfa;
		accessibilityStatus = new Accessable[dfa.getNumOfStates()];
		// Initial state is accessible by default
		accessibilityStatus[0] = Accessable.TRUE;
		for (int i = 1; i <accessibilityStatus.length ; i++) {
			accessibilityStatus[i] = Accessable.UNKNOWN;
		}
	}

	public void remove(){
		ArrayList<State> states = dfa.getStates();
		// Check each states Accessibility
		for (int stateNum = 1; stateNum < dfa.getNumOfStates(); stateNum++) {
			boolean[] crossed = new boolean[dfa.getNumOfStates()];
			Arrays.fill(crossed, Boolean.FALSE);
			accessibilityStatus[stateNum] = isAccessible(states.get(stateNum), crossed);
		}
		ArrayList<Integer> removedStates = new ArrayList<>();
		ArrayList<State> newStates = new ArrayList<>();


		for (int i = 0; i <accessibilityStatus.length ; i++) {
			if (accessibilityStatus[i]==Accessable.FALSE) {
				this.dfa.setNumOfStates(this.dfa.getNumOfStates() - 1);
				this.dfa.getFinalStates().remove(new Integer(i+1));
				removedStates.add(i+1);
			}else{
				newStates.add(states.get(i));
			}
		}
		dfa.setStates(newStates);
		dfa.reduceStateNum(removedStates);
	}

	public Accessable isAccessible(State state, boolean[] crossed){
		if (accessibilityStatus[state.getName()-1]==Accessable.TRUE){
			return Accessable.TRUE;
		} else if (accessibilityStatus[state.getName()-1]==Accessable.FALSE | crossed[state.getName()-1]){
			return Accessable.FALSE;
		} else {

			// If we don't have any information about the current states accessibility,
			/// we should check its previous states
			Accessable result = Accessable.FALSE;
			ArrayList<State> previousStates = getPreviousStates(state);
			crossed[state.getName()-1]=true;
			for (State previousState : previousStates) {
				// If any of the previous states were accessible return true
				if (isAccessible(previousState, crossed)==Accessable.TRUE){
					result = Accessable.TRUE;
					break;
				}
			}
			return result;
		}
	}

	public ArrayList<State> getPreviousStates(State state){
		ArrayList<State> previousStates = new ArrayList<>();
		for (State dfaState : dfa.getStates()) {
			if (dfaState!=state & dfaState.getTranstitionFunction().contains(state.getName())){
				previousStates.add(dfaState);
			}
		}
		return previousStates;
	}

	private enum Accessable{
		UNKNOWN,
		TRUE,
		FALSE;
	}
}

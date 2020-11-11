import java.util.*;

public class Minimizer {
	private DaedStateRemover daedStateRemover;
	private InaccessibleStateRemover inaccessibleStateRemover;
	private DFA dfa;

	public Minimizer(DFA dfa) {
		this.dfa = dfa;
		this.daedStateRemover = new DaedStateRemover(this.dfa);
	}
	public DFA minimize(){
		if (dfa.getFinalStates().size()!=0) {
			daedStateRemover.remove();
			this.inaccessibleStateRemover = new InaccessibleStateRemover(this.dfa);
			inaccessibleStateRemover.remove();
			ArrayList<Integer> deletedStates = createDFA(createNewStates(completeTable(initilizeTable())));
			dfa.reduceStateNum(deletedStates);
		}
		else {
			ArrayList<Integer> transitoins = new ArrayList<>();
			for (int i = 0; i <dfa.getNumOfAlphabet() ; i++) {
				transitoins.add(1);
			}
			ArrayList<State> states = new ArrayList<>();
			states.add(new State(1,false,transitoins));
			DFA dfa = new DFA(1,this.dfa.getNumOfAlphabet(),new ArrayList<>(),states);
			return dfa;
		}
		return dfa;
	}
	public ArrayList<ArrayList<HashSet<State>>> initilizeTable(){
		ArrayList<HashSet<State>> markedLsit = new ArrayList<>();
		ArrayList<HashSet<State>> unmarkedList = new ArrayList<>();
		ArrayList<ArrayList<HashSet<State>>> output = new ArrayList<>();
		for (int i = 0; i <dfa.getNumOfStates() ; i++) {
			for (int j = 0; j <i ; j++) {
				HashSet<State> pair = new HashSet<>();
				pair.add(dfa.getStates().get(i));
				pair.add(dfa.getStates().get(j));
				if (dfa.getFinalStates().contains(i+1) & !dfa.getFinalStates().contains(j+1)
						| dfa.getFinalStates().contains(j+1) & !dfa.getFinalStates().contains(i+1))
					markedLsit.add(pair);
				else
					unmarkedList.add(pair);
			}
		}
		output.add(markedLsit);
		output.add(unmarkedList);
		return output;
	}

	public ArrayList<ArrayList<HashSet<State>>> completeTable(ArrayList<ArrayList<HashSet<State>>> tables){
		ArrayList<HashSet<State>> markedLsit = tables.get(0);
		ArrayList<HashSet<State>> unmarkedList = tables.get(1);
		int newSize = 0;
		int currentSize = 1;

		State r, s, p, q;
		Iterator<State> iterator;
		Iterator<HashSet<State>> listIterator;
		HashSet<State> checkedPair, switchedPair;
		while (newSize != currentSize){
			currentSize = markedLsit.size();
			listIterator = unmarkedList.iterator();
			while (listIterator.hasNext()) {
				iterator = listIterator.next().iterator();
				r = iterator.next();
				s = iterator.next();
				for (int input = 0; input <dfa.getNumOfAlphabet() ; input++) {
					p = dfa.getStates().get(r.getTranstitionFunction().get(input)-1);
					q = dfa.getStates().get(s.getTranstitionFunction().get(input)-1);
					checkedPair = new HashSet<>();
					checkedPair.add(p);
					checkedPair.add(q);
					switchedPair = new HashSet<>();
					switchedPair.add(r);
					switchedPair.add(s);
					if (markedLsit.contains(checkedPair)) {
						listIterator.remove();
						markedLsit.add(switchedPair);
						break;
					}
				}
			}
			newSize = markedLsit.size();
		}
		ArrayList<ArrayList<HashSet<State>>> completeTable = new ArrayList<>();
		completeTable.add(markedLsit);
		completeTable.add(unmarkedList);
		return  completeTable;
	}


	public HashMap<Integer, HashSet<Integer>> createNewStates(ArrayList<ArrayList<HashSet<State>>> completeTable){
		ArrayList<HashSet<State>> unmarkedList = completeTable.get(1);
		ArrayList<HashSet<State>> newUnmarkedList = new ArrayList<>();
		Iterator<State> stateIterator;
		Iterator<HashSet<State>> newStateSetIterator;
		State state1, state2;
		boolean found;
		for (HashSet<State> states : unmarkedList) {
			stateIterator = states.iterator();
			state1 = stateIterator.next();
			state2 = stateIterator.next();
			newStateSetIterator = newUnmarkedList.iterator();
			found = false;
			while (newStateSetIterator.hasNext()){
				HashSet<State> thisStateSet = newStateSetIterator.next();
				if (thisStateSet.contains(state1) | thisStateSet.contains(state2)){
					thisStateSet.addAll(states);
					found = true;
					break;
				}
			}
			if (!found) newUnmarkedList.add(states);
		}

		for (State state : dfa.getStates()) {
			found = false;
			newStateSetIterator = newUnmarkedList.iterator();
			while (newStateSetIterator.hasNext()){
				HashSet<State> thisStateSet = newStateSetIterator.next();
				if (thisStateSet.contains(state)){
					found = true;
					break;
				}
			}
			if (!found) {
				HashSet<State> stateSet = new HashSet<>();
				stateSet.add(state);
				newUnmarkedList.add(stateSet);
			}
		}
		HashMap<Integer, HashSet<Integer>> stateMaping = new HashMap<>();

		for (HashSet<State> states : newUnmarkedList) {
			HashSet<Integer> stateNumbers = new HashSet<>();
			for (State state : states) {
				stateNumbers.add(state.getName());
			}
			stateMaping.put(Collections.min(stateNumbers), stateNumbers);
		}

		return stateMaping;
	}


	public ArrayList<Integer> createDFA(HashMap<Integer,HashSet<Integer>> stateMaping){
		List<Integer> stateNames = new ArrayList<Integer>(stateMaping.keySet());
		Collections.sort(stateNames);
		ArrayList<State> dfaStates = new ArrayList<>();
		ArrayList<Integer> dfaFinalStates = new ArrayList<>();
		int name;
		boolean finality;
		ArrayList<Integer> transitions;
		for (int i = 0; i <stateNames.size() ; i++) {
			name = stateNames.get(i);
			finality = this.dfa.getStates().get(name-1).isFinal();
			if (finality) dfaFinalStates.add(name);
			transitions = new ArrayList<>();
			for (int j = 0; j < this.dfa.getNumOfAlphabet(); j++) {
				int toState = this.dfa.getStates().get(name-1).getTranstitionFunction().get(j);
				Iterator hmIterator = stateMaping.entrySet().iterator();
				while (hmIterator.hasNext()){
					Map.Entry mapElement = (Map.Entry) hmIterator.next();
					HashSet<Integer> toStates = (HashSet<Integer>) mapElement.getValue();
					if (toStates.contains(toState)){
						toState = (int) mapElement.getKey();
						break;
					}
				}
				transitions.add(toState);
			}
			State newState = new State(name,finality,transitions);
			dfaStates.add(newState);
		}
		ArrayList<Integer> deletedStates = new ArrayList<>();
		for (int i = 1; i <dfa.getNumOfStates()+1 ; i++) {
			deletedStates.add(i);
		}
		dfa = new DFA(stateNames.size(),this.dfa.getNumOfAlphabet(), dfaFinalStates, dfaStates);
		deletedStates.removeAll(stateNames);
		return deletedStates;
	}

}

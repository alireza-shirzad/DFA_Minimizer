import java.util.ArrayList;
import java.util.Scanner;

public class InputParser {
	private Scanner scanner;


	public InputParser(){
		scanner = new Scanner(System.in);
	}

	public DFA parse(){

		String[] firstLine = scanner.nextLine().split("\\s+");
		String originialSecondLine = scanner.nextLine();
		String[] secondLine = originialSecondLine.split("\\s+");
		String[] transitionFunctionLine;

		int numOfStates = Integer.parseInt(firstLine[0]);
		int numOfAlphabet = Integer.parseInt(firstLine[1]);

		ArrayList<Integer> finalStates = new ArrayList<>();

		if (!originialSecondLine.equals("")) {
			for (String s : secondLine) {
				finalStates.add(Integer.parseInt(s));
			}
		}

		ArrayList<State> states = new ArrayList<>();
		boolean finality;
		int name;
		ArrayList<Integer> toStates;
		for (int stateNum = 0; stateNum < numOfStates; stateNum++) {
			finality = finalStates.contains(stateNum+1);
			name = stateNum + 1;
			toStates = new ArrayList<>();
			transitionFunctionLine = scanner.nextLine().split("\\s+");
			for (int input = 0; input < numOfAlphabet ; input++) {
				toStates.add(Integer.parseInt(transitionFunctionLine[input]));
			}
			states.add(new State(name,finality,toStates));
		}

		return new DFA(numOfStates, numOfAlphabet, finalStates, states);
	}
}

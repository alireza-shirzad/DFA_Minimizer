import java.util.ArrayList;

public class State {
	private int name;
	private boolean finality;
	private ArrayList<Integer> transtitionFunction;

	public State(int name, boolean finality, ArrayList<Integer> transtitionFunction) {
		this.name = name;
		this.finality = finality;
		this.transtitionFunction = transtitionFunction;
	}

	public boolean isFinal() {
		return finality;
	}

	public void setFinality(boolean finality) {
		this.finality = finality;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public ArrayList<Integer> getTranstitionFunction() {
		return transtitionFunction;
	}

	public void setTranstitionFunction(ArrayList<Integer> transtitionFunction) {
		this.transtitionFunction = transtitionFunction;
	}

	@Override
	public String toString() {
		return "State{" +
				"name=" + name +
				'}';
	}

}

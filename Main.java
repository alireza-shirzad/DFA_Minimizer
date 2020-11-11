import java.util.*;

public class Main {

    public static void main(String[] args) {
	    InputParser parser = new InputParser();
	    DFA dfa = parser.parse();
	    Minimizer minimizer = new Minimizer(dfa);
	    System.out.println(minimizer.minimize());
    }
}


import java.util.Scanner;
import java.io.File;

public class Main {
	static Scanner in;
	public static void main (String [] args) {
		
		try {
			File f = new File ("zad1.txt");
			in = new Scanner (f);
		}
		catch (Exception e) {
			System.out.println ("There is no such file.");
		}
		Automaton a = new Automaton (in);
		a.showAllWords(8);
		System.out.println(a.isDetermined ());
		a.generateDetermined();
	}
}
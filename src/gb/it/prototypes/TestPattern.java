package gb.it.prototypes;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern {
	// pattern da controllare
	// 00:00:05,243 --> 00:00:08,583
	private static final String mainString = "\\d\\d+[:]+\\d\\d+[:]+\\d\\d+[,]+\\d\\d\\d\\s+-->+\\s\\d\\d+[:]+\\d\\d+[:]+\\d\\d+[,]+\\d\\d\\d";

	public static void main(String[] args) {
		Scanner kybrd = new Scanner(System.in);
		System.out.println("Inserisci una stringa: ");
		String inserted = kybrd.nextLine();

		Pattern mainPattern = Pattern.compile(mainString);
		Matcher mainMatcher = mainPattern.matcher(inserted);

		System.out.print("\nLa stringa inserita");
		if (!mainMatcher.matches())
			System.out.print(" non");
		System.out.println(" corrisponde al pattern!");
		
		//System.out.println(mainMatcher.group());
		
		while (mainMatcher.find())
			System.out.println(mainMatcher.group());
	}
}

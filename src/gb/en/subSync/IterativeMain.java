package gb.en.subSync;

import java.io.*;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IterativeMain {

	private static String entireInputFileModified = null;
	private static BufferedReader inputFromFile = null;
	private static int secondDelay;
	private static int msSecondDelay;

	public static void main(String[] args) {
		Scanner kybrd = new Scanner(System.in);
		System.out.println("Insert file name:");

		String fileName = kybrd.nextLine();

		if (!fileName.substring(fileName.lastIndexOf(".") + 1).equals("srt")) {
			System.out.println("Sono supportati solo file .srt!");
			System.exit(1);
		}

		File srtFile = new File(fileName);

		if (!srtFile.exists()) {
			System.out.println("File not found!");
			System.exit(1);
		}

		try {
			inputFromFile = new BufferedReader(new FileReader(srtFile));
			// mi salvo l'intero file su una stringa
			entireInputFileModified = new String(Files.readAllBytes(srtFile.toPath()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Fatal error: " + e.getMessage());
			System.exit(1);
		}
		
		do {
			System.out.println("(Negative values for anticipation)");
			System.out.println("Seconds?");
			secondDelay = kybrd.nextInt();

			System.out.println("Milliseconds?");
			msSecondDelay = kybrd.nextInt();
		} while (msSecondDelay >= 1000);

		elaborateMatchedStrings();
				
		try {
			// stampo la stringa modificata in tutto il file originale
			PrintWriter finalOutput = new PrintWriter(new FileWriter(srtFile), false);
			finalOutput.print(entireInputFileModified);
			finalOutput.close();
		} catch (Exception e) {
			System.out.println("Fatal error: " + e.getMessage());
			System.exit(1);
		}

		kybrd.close();

	}

	private static void elaborateMatchedStrings() {

		String mainString = "\\d\\d+[:]+\\d\\d+[:]+\\d\\d+[,]+\\d\\d\\d\\s+-->+\\s\\d\\d+[:]+\\d\\d+[:]+\\d\\d+[,]+\\d\\d\\d";
		Pattern mainPattern = Pattern.compile(mainString);

		String line = "";
		try {
			while ((line = inputFromFile.readLine()) != null) {

				Matcher tempMatcher = mainPattern.matcher(line);

				// se si trova una sottostringa corrispondente al pattern la si
				// inserisce nell'arraylist
				while (tempMatcher.find()) {
					changeLine(tempMatcher.group());
				}

			}
		} catch (Exception e) {
			System.out.println("Fatal error: " + e.getMessage());
			System.exit(1);
		}
	}

	private static void changeLine(String rawLine) {
		String[] splittedLine = rawLine.split(" --> ");

		// prima l'orario iniziale
		String begin = elaborateDelay(splittedLine[0]);

		// poi l'orario finale
		String end = elaborateDelay(splittedLine[1]);

		// 00:00:08,686 --> 00:00:10,286
		String result = begin + " --> " + end;
		
		
		// sostituisco tutte le occorrenze nella stringa che poi inserirò nel
		// file di output
		entireInputFileModified = entireInputFileModified.replaceFirst(rawLine, result);
	}

	private static String elaborateDelay(String time) {

		// imposto il formato della data nel modo che mi interessa
		SimpleDateFormat srtDataFormat = new SimpleDateFormat("HH:mm:ss,SSS");
		srtDataFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = null;

		try {
			// creo una data secondo il formato dato, dalla stringa passata
			date = srtDataFormat.parse(time);
		} catch (Exception e) {
			System.out.println("Fatal error" + e.getMessage());
			System.exit(1);
		}

		// uso Calendar per spostarmi più facilmente nel tempo
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// aggiungo secondi e millisecondi
		calendar.add(Calendar.MILLISECOND, msSecondDelay);
		calendar.add(Calendar.SECOND, secondDelay);

		// stampo la nuova data secondo il formato definito prima
		String result = srtDataFormat.format(calendar.getTime());

		return result;
	}

}

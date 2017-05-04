package gb.en.subSyncGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingConstants;

public class SubSyncGUI {

	private static String entireInputFileModified = null;
	private static BufferedReader inputFromFile = null;

	public static int seconds;
	public static int mSeconds;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SSFrame mainFrame = new SSFrame();
		mainFrame.statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public static void syncronizeFile() {

		SSFrame.statusLbl.setText("Working... checking file");

		String fileName = SSFrame.barraRicerca.getText();

		if (!fileName.substring(fileName.lastIndexOf(".") + 1).equals("srt")) {
			SSFrame.statusLbl.setText("Only .srt files supported!");
			return;
		}

		File srtFile = new File(fileName);

		if (!srtFile.exists()) {
			SSFrame.statusLbl.setText("File not found!");
			return;
		}

		try {
			inputFromFile = new BufferedReader(new FileReader(srtFile));
			// mi salvo l'intero file su una stringa
			entireInputFileModified = new String(Files.readAllBytes(srtFile.toPath()), StandardCharsets.UTF_8);
			SSFrame.statusLbl.setText("Working... file readed");
		} catch (IOException e) {
			SSFrame.statusLbl.setText("Fatal error: " + e.getMessage());
			return;
		}

		elaborateMatchedStrings();

		SSFrame.statusLbl.setText("Working... time set");



		String endFileName = "";

		try

		{
			SSFrame.statusLbl.setText("Working... writing output");

			// creo un file con il nome originale più "_subSync.srt"
			endFileName = srtFile.getName().substring(0, srtFile.getName().lastIndexOf('.'));
			endFileName += "_subSync.srt";
			// recupero il path vecchio
			endFileName = srtFile.getParent() + "\\" + endFileName;

			// stampo la stringa modificata in tutto il file originale
			PrintWriter finalOutput = new PrintWriter(new FileWriter(endFileName), false);
			finalOutput.print(entireInputFileModified);
			finalOutput.close();
		} catch (Exception e) {
			SSFrame.statusLbl.setText("Fatal error: " + e.getMessage());
			return;
		}

		SSFrame.statusLbl.setText("Ended successfully: saved on " + endFileName);

	}

	private static void elaborateMatchedStrings() {

		String mainString = "\\d\\d+[:]+\\d\\d+[:]+\\d\\d+[,]+\\d\\d\\d\\s+-->+\\s\\d\\d+[:]+\\d\\d+[:]+\\d\\d+[,]+\\d\\d\\d";
		Pattern mainPattern = Pattern.compile(mainString);

		String line = "";
		try {

			while ((line = inputFromFile.readLine()) != null) {

				SSFrame.statusLbl.setText("Working... finding time lines");

				Matcher tempMatcher = mainPattern.matcher(line);

				// se si trova una sottostringa corrispondente al pattern la si
				// inserisce nell'arraylist
				while (tempMatcher.find()) {
					changeLine(tempMatcher.group());
				}

			}
		} catch (Exception e) {
			System.out.println("Fatal error: " + e.getMessage());
			return;
		}
	}

	private static void changeLine(String rawLine) {

		SSFrame.statusLbl.setText("Working... modifying time line");
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
		// 00:01:46,227
		int ore = 0, minuti = 0, secondi = 0, ms = 0;

		String[] splittedTime = time.split("[:,]");

		try {
			ore = Integer.parseInt(splittedTime[0]);
			minuti = Integer.parseInt(splittedTime[1]);
			secondi = Integer.parseInt(splittedTime[2]);
			ms = Integer.parseInt(splittedTime[3]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fatal error: " + e.getMessage());
			return null;
		}

		int differenceMillis = (seconds * 1000) + mSeconds;
		int firstMillis = fromDateToMillis(ore, minuti, secondi, ms);
		int resultMillis = firstMillis + differenceMillis;
		return fromMillisToDate(resultMillis);

	}

	private static int fromDateToMillis(int ore, int minuti, int secondi, int ms) {
		return (int) ((secondi * 1000) + (minuti * 60000) + (ore * 3600000) + ms);
	}

	private static String fromMillisToDate(long millis) {
		return (String.format("%02d:%02d:%02d,%03d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
				TimeUnit.MILLISECONDS.toMillis(millis) % TimeUnit.SECONDS.toMillis(1)));

	}

}

package gb.it.prototypes;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class TestCalcSecond {
	public static void main(String[] args) throws ParseException {
		int ore = 1, minuti = 59, secondi = 59, ms = 500;
		int msSecondDelay = 1, secondDelay = -100;
		int differenceMillis = (secondDelay * 1000) + msSecondDelay;
		int firstMillis = fromDateToMillis(ore, minuti, secondi, ms);
		int resultMillis = firstMillis + differenceMillis;
		System.out.println(fromMillisToDate(resultMillis));

	}

	private static int fromDateToMillis(int ore, int minuti, int secondi, int ms) {
		return (int) ((secondi * 1000) + (minuti * 60000) + (ore * 3600000) + ms);
	}
	
	private static String fromMillisToDate(long millis){
		return (String.format("%02d:%02d:%02d,%03d", TimeUnit.MILLISECONDS.toHours(millis),
			TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
			TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
			TimeUnit.MILLISECONDS.toMillis(millis) % TimeUnit.SECONDS.toMillis(1)));
		
	}
}

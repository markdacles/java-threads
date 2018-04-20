import java.util.*;

public class HorseUtil {

	private static Scanner input = new Scanner(System.in).useDelimiter("\n");
	private static Random rand = new Random(); 

	public static int getInt(int min){
		boolean redo = true;
		int num = 0;
		while(redo) {
		    if (!input.hasNextInt()) {
		        input.next(); // Clears the invalid input
		        System.out.print("Invalid input!\nInput "+ (min==5?"Number of Horses":"Race Distance")+ ": ");
		    }
		    num = input.nextInt();
		    redo = false;
		    if(!(num >= min)) {
		        System.out.print("Invalid input!\nInput "+ (min==5?"Number of Horses":"Race Distance")+ ": ");
		        redo = true;
		    }
		}
		return num;
	}

	public static int getRandom(int min, int max){
		return rand.nextInt((max - min) + 1) + min;
	}

	public static boolean getBoolean() {
		return rand.nextBoolean();
	}

	public static String getNumberSuffix(int num) {
    if (num >= 11 && num <= 13) {
        return "th";
	}
	switch (num % 10) {
	    case 1:
	        return "st";
	    case 2:
	        return "nd";
	    case 3:
	        return "rd";
	    default:
	        return "th";
	    }
	}

}
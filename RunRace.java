public class RunRace {

	public static void main(String[] args) {

		boolean redo = true;
		HorseRace horseRace = new HorseRace();

		System.out.println("WELCOME TO THE HORSE RACE!!!");

		while(redo){
		
			System.out.print("Input Number of Horses: ");
		
			int numOfHorses = Util.getInt(5);
			redo = horseRace.setHealthyHorses(numOfHorses);
		}
		
		System.out.print("Input Race Distance: ");

		int raceDist = Util.getInt(100);

		horseRace.setRaceDistance(raceDist);

		horseRace.initializeHorses();
	}
}
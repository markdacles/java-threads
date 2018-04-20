import java.util.*;
import java.util.concurrent.*;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;
import java.util.Arrays;

public class HorseRace {

	static int raceDistance = 0;
	private int numberOfHorses = 0;
	static CyclicBarrier barrier;
	static CyclicBarrier newBarrier;
	static CyclicBarrier placeBarrier;
	static CyclicBarrier finishBarrier;
	static long seconds = 0;
	private List<String> horseNames, horseBC;
	static Horse[] horses;
	static int places = 1;
	static boolean goRace = true;
	static int lastPlace;

	public void setRaceDistance(int dist) {
		raceDistance = dist;
	}

	public boolean setHealthyHorses(int noh) {
		numberOfHorses = noh;
		horses = new Horse[noh];
		horseNames = Arrays.asList(HorseList.horseNames);
		horseBC = Arrays.asList(HorseList.horseBC);
		Collections.shuffle(horseNames);
		Collections.shuffle(horseBC);
		for(int i = 0; i < numberOfHorses; i++) {
			if(i < 20) {
				horses[i] = new Horse(horseNames.get(i),horseBC.get(i), HorseUtil.getBoolean(), 1, 0);
			}
			else {
				horses[i] = new Horse("horse"+String.valueOf(i),"battle cry"+String.valueOf(i), HorseUtil.getBoolean(), 1, 0);
			}
		}

		horses = Arrays.stream(horses).filter(x -> x.getHH()).toArray(Horse[]::new);

		if(horses.length < 5){
			System.out.println("Not enough healthy horses to race. Please try again.");
			return true;
		}

		return false;

	}

	public void initializeHorses() {

		barrier = new CyclicBarrier(horses.length);

		Runnable barrierAction = new Runnable() {

			@Override
		    public void run() {
		    	try {
					System.out.println("=================================================================================================================="); 
			        System.out.println("|| n/a\t|| -----\t\t|| -    || -    ||  -       || HORSES AT THE GATE");
			    	Thread.sleep(1000);
			    	System.out.println("|| n/a\t|| -----\t\t|| -    || -    ||  -       || READY!");
			    	Thread.sleep(1000);
			    	System.out.println("|| n/a\t|| -----\t\t|| -    || -    ||  -       || GET SET!!");
			    	Thread.sleep(1000);
			    	System.out.println("|| n/a\t|| -----\t\t|| -    || -    ||  -       || GO!!!");
					System.out.println("=================================================================================================================="); 
			    	Thread.sleep(1000);
			    	for(int i = 0; i < horses.length; i++) {
			    		System.out.println("|| 0"+ "\t|| " + horses[i].getName() + "\t\t|| 0" + "\t|| 0" + "\t||  - \t    || " + horses[i].getBC());
			    	}
			    	seconds++;
			    	Thread.sleep(1000);
			   	} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}

		    }
		};

		newBarrier = new CyclicBarrier(horses.length, barrierAction);
		
		Thread[] threads = new Thread[horses.length];
		System.out.println("\nHorse Race Line Up: ");
		for(int i = 0; i < horses.length; i++) {
			threads[i] = new Thread(horses[i]);
			System.out.print(horses[i].getName());
			if( i < horses.length - 1) {
					System.out.print(" █ ");
			}
		}

		try{
			Thread.sleep(1000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}


		System.out.println("\n");
		System.out.println("|| TIME\t|| HORSE NAME\t\t||STEPS || DIST || POSITION || \t\tSTATUS"); 
		for(int i = 0; i < horses.length; i++) {
			threads[i].start();
		}

		placeBarrier = new CyclicBarrier(horses.length, new Runnable() {

			public void run() {
				System.out.println("=================================================================================================================="); 
				for(int i = 0; i < horses.length; i++) {
					int count = places;
					int count2 = 1;
					for(int j = 0; j < horses.length; j++) {
						if(horses[i].getDistance() < horses[j].getDistance()) {
							count++;
							count2++;
						}
					}
					horses[i].setPlace(count,count2);
				} 
				if(isRaceDone()){
					goRace = false;
				}

				lastPlace = getLastPlace();
			}
		});

		finishBarrier = new CyclicBarrier(horses.length, new Runnable() {

			public void run() {
				try{
					System.out.print("\n\nLOADING RESULTS.");
					Thread.sleep(1000);
					System.out.print(".");
					Thread.sleep(1000);
					System.out.println(".\n");
					Thread.sleep(1000);
					showResults();
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
		});
	}

	public static boolean isRaceDone() {
		int j = 1;
		for(int i = 0; i < horses.length; i++) {
			if(horses[i].getDistance() < raceDistance) {
				return false;
			}
		}
		return true;
	}

	public static int getLastPlace() {
		int last = 0;
		for(int i = 0; i < horses.length; i++) {
			if(horses[i].getPPlace() > last) {
				last = horses[i].getPPlace();
			}
		}
		return last;
	}

	public void showResults() {

		List<Horse> horseList = Arrays.asList(horses);
		horseList.sort((Horse h1 , Horse h2) -> Integer.compare(h1.getFPlace(), h2.getFPlace()));

		for (Horse horse: horseList) {
        	System.out.println(horse.getName() + " finished in " + horse.getFPlace() + HorseUtil.getNumberSuffix(horse.getFPlace()) + " place in "+ horse.getFTime() + " seconds, " + horse.getBC());
        }

        System.out.println("\nThank you for playing, please race again!!!");
	}
}
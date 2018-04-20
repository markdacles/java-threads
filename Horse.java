import java.util.concurrent.*;
import java.io.*;

public class Horse implements Runnable {

	private int distance = 0;
	private String horseName = "";
	private String horseBC = "";
	private boolean horseHealth = false;
	private int horsePlace = 0;
	private int partialPlace = 0;
	private int finalPlace = 0;
	private long finishTime = 0;
	private boolean goRace = true;
	private boolean horseDone = false;

	public Horse(String hName, String hBC, boolean hh, int place, long fTime){
		horseName = hName;
		horseBC = hBC;
		horseHealth = hh;
		horsePlace = place;
	}

	public boolean getHH() { return horseHealth; }

	public long getFTime() { return finishTime; }

	public int getFPlace() { return finalPlace; }

	public boolean checkHorse() { return horseDone; }

	public int getPPlace() { return partialPlace; }

	public int getDistance() { return distance; }

	public String getName() { return horseName; }

	public String getBC() { return horseBC; }

	public int getPlace() { return horsePlace; }

	public boolean isHorseDone() { return horseDone; }


	public void setRace(boolean go) { goRace = go; }

	public void setPlace(int place, int place2) {
		horsePlace = place;
		partialPlace = place2;
	}

	public void run() {
		try {
			long taskTime = 0;
			long seconds = 0;
			int steps = 0;
			String status = "";
		    HorseRace.barrier.await(); 
			while (distance < 10) {
				taskTime = System.currentTimeMillis();
			  	steps = HorseUtil.getRandom(1,10);
			  	if(distance+steps > 10) {
			  		steps = 10-distance;
			  	}
			  	distance += steps;
			  	if(distance >= 10) {
			  		distance = 10;
			  		System.out.println("|| n/a\t|| " + horseName + "\t\t|| " + steps + "\t|| " + distance + "\t|| " + " GATE    || ready at the gate.");
			  	}
			  	else if(distance >= 1) {
			  		System.out.println("|| n/a\t|| " + horseName + "\t\t|| " + steps + "\t|| " + distance + "\t|| " + " -       || walking to the gate...");
			  	}
			  	taskTime = System.currentTimeMillis()-taskTime;
			  	if (1000-taskTime > 0 ) {
			    	try {
					    Thread.sleep(1000-taskTime);                //1000 milliseconds is one second.
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
			  	}
			}
			HorseRace.seconds = seconds;
			steps = 0;
			distance = 0;

			HorseRace.newBarrier.await();
			seconds = HorseRace.seconds;
			while(goRace) {
				taskTime = System.currentTimeMillis();
				steps = HorseUtil.getRandom(1,10);
				if(partialPlace == HorseRace.lastPlace && partialPlace > 0) {
					steps = HorseUtil.getRandom(1,20);
				}
				if(distance+steps > HorseRace.raceDistance) {
			  		steps = HorseRace.raceDistance-distance;
			  	}
			  	distance += steps;
			  	if(distance >= HorseRace.raceDistance) {
			  		distance = HorseRace.raceDistance;
			  	}
			  	HorseRace.placeBarrier.await();
			  	if(distance >= HorseRace.raceDistance && horseDone == false) {
			  		horseDone = true;
			  		distance = HorseRace.raceDistance;
			  		System.out.println("|| "+ seconds +"\t|| " + horseName + "\t\t|| " + steps + "\t|| " + distance + "\t||  " + horsePlace +"\t    || " +horseName+" CROSSED THE FINISH LINE IN " + horsePlace + HorseUtil.getNumberSuffix(horsePlace) + " place, " + horseBC);
			  		finalPlace = horsePlace;
			  		finishTime = seconds;
			  		HorseRace.places++;
			  	} else if (distance < HorseRace.raceDistance) {
			  		if (partialPlace == HorseRace.places) {
			  			status = "leading the race at "+ partialPlace + HorseUtil.getNumberSuffix(horsePlace) +" place";
			  		} else if (partialPlace == HorseRace.lastPlace) {
			  		 	status = "trailing at last place... booost!!!";
			  		}

			  		System.out.println("|| "+ seconds +"\t|| " + horseName + "\t\t|| " + steps + "\t|| " + distance + "\t||  " + partialPlace +"\t    || " + status + " MILLI: " + System.currentTimeMillis());
			  		status = "";
			  	}
			  	if(HorseRace.goRace == false) {
			  		break;
			  	}
			  	seconds++;
			  	taskTime = System.currentTimeMillis()-taskTime;
			  // 	if (1000-taskTime > 0 ) {
			  //   	try {
					//     Thread.sleep(1000-taskTime);                //1000 milliseconds is one second.
					// } catch(InterruptedException ex) {
					//     Thread.currentThread().interrupt();
					// }
			  // 	}
			}

			HorseRace.finishBarrier.await();

		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		} catch (BrokenBarrierException e) {
                e.printStackTrace();
        }
	}
}
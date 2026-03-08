import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


public class Hammurabi {

	static int year        = 1;
    static int population  = 100;   // people
    static int grain       = 2800;  // bushels in storage
    static int acres       = 1000;  // acres of land owned
    static int totalStarved = 0;
	static int currentPrice = 0;

    static Scanner scanner = new Scanner(System.in);
    static Random  random  = new Random();
	
	static void printWelcome() {
        System.out.println("=== HAMMURABI ===");
        System.out.println("You are ruler of ancient Wilmington for 10 years.");
        System.out.println("Manage your grain, land, and people wisely.\n");
    }
	
	static void printStatus() {
		System.out.println("\n--- Year " + year + " ---");
		System.out.println("Population " + population);
		System.out.println("Grain " + grain + " bushels");
		System.out.println("Land " + acres + " acres");
		System.out.println("Land price " + landPrice() + " bushels/acre");
		
	}
	
	public static void main(String[] args) {
			printWelcome();
	
			for (year = 1; year <= 10; year++) {
				currentPrice = landPrice();
				printStatus();
			
				int acresToBuy = askAcresToBuy();
				int grainToFeed = askGrainToFeed();
				int acresToPlant = askAcresToPlant();

				processYear(acresToBuy, grainToFeed, acresToPlant);

				if (gameOver()) break;
			}
			
			printFinalScore();
			
		}
	static int askAcresToBuy(){
		while (true) {
			try {
				System.err.println("\nHow many acres would you like to buy?");
				int amount = scanner.nextInt();

            	int cost = amount * currentPrice;

            // Buying: make sure we have enough grain
            if (amount > 0 && cost > grain) {
            System.out.println("  Not enough grain! You can afford "
            + (grain / currentPrice) + " acres.");               continue;
		}
		// Selling: make sure we own that many acres
		if (amount < 0 && Math.abs(amount) > acres) {
			System.out.println("  You only own " + acres + " acres.");
			continue;
		}

		return amount;
		
	} catch (InputMismatchException e) {
                System.out.println("  Please enter a whole number.");
                scanner.next(); // clear bad input
            }
        }
    }

    static int askGrainToFeed() {
        while (true) {
            try {
                System.out.print("How many bushels to feed your people? ");
                int amount = scanner.nextInt();

                if (amount < 0) {
                    System.out.println("  Can't feed a negative amount.");
                    continue;
                }
                if (amount > grain) {
                    System.out.println("  You only have " + grain + " bushels.");
                    continue;
                }

                return amount;

            } catch (InputMismatchException e) {
                System.out.println("  Please enter a whole number.");
                scanner.next();
            }
        }
    }

    static int askAcresToPlant() {
        while (true) {
            try {
                System.out.print("How many acres to plant? ");
                int amount = scanner.nextInt();

                if (amount < 0) {
                    System.out.println("  Can't plant negative acres.");
                    continue;
                }
                if (amount > acres) {
                    System.out.println("  You only own " + acres + " acres.");
                    continue;
                }
                // Each person can farm 10 acres
                if (amount > population * 10) {
                    System.out.println("  Not enough people! Your " + population
                        + " people can only farm " + (population * 10) + " acres.");
                    continue;
                }
                // Planting costs 1 bushel of seed per 2 acres
                int seedNeeded = amount / 2;
                if (seedNeeded > grain) {
                    System.out.println("  Not enough seed grain! You can plant "
                        + (grain * 2) + " acres with your current supply.");
                    continue;
                }

                return amount;

            } catch (InputMismatchException e) {
                System.out.println("  Please enter a whole number.");
                scanner.next();
            }
        }
    }

	static void processYear(int acresToBuy, int grainToFeed, int acresToPlant) {
		System.err.println("\n-- Results for Year " + year + " --");
		
		acres += acresToBuy;
		grain -= acresToBuy * currentPrice;

		grain -= acresToPlant / 2;

		int harvest = acresToPlant * harvestRate();
		grain += harvest;
		System.out.println("Harvest : " + harvest + " bushels");

		int ratsAte = ratsEat();
		grain -= ratsAte;
		if (ratsAte > 0)
			System.err.println("Rats ate: " + ratsAte + " bushels");
		else
			System.err.println("No rats this year!");

		grain -= grainToFeed;
        int starved = calcStarved(grainToFeed);
        int newborn = calcNewborn(grainToFeed);
        totalStarved += starved;
        population    = population - starved + newborn;

        System.out.println("Starved    : " + starved);
        System.out.println("Newborn    : " + newborn);
        System.out.println("Population : " + population);
        System.out.println("Grain left : " + grain + " bushels");

		if (plagueHits()) {
            population /= 2;
            System.out.println("*** A PLAGUE struck! Population halved to " + population + " ***");
        }
    }
	
	static int landPrice() {
		return 17 + random.nextInt(10);
	}

	static int harvestRate() {
		return 1 + random.nextInt(6);
	}

	static int ratsEat() {
		if (random.nextInt(100) < 40) {
			return grain / (3 + random.nextInt(3));
		}
		return 0;
	}

	static int calcStarved(int grainToFeed) {
		int peopleFed = grainToFeed / 20;
		return Math.max(0, population - peopleFed);
	}

	static int calcNewborn(int grainToFeed) {
		int peopleFed = grainToFeed / 20;
		if (peopleFed >= population) {
			return 1 + random.nextInt(6);
		} else {
			return random.nextInt(3);
		}
	}

	static boolean plagueHits() {
		return random.nextInt(100) < 15;
	}

	static boolean gameOver() {
        if (population <= 0) {
            System.out.println("\n*** Everyone has died. Your rule is over. ***");
            return true;
        }
        if (grain < 0) {
            System.out.println("\n*** You ran out of grain. Your people revolt! ***");
            return true;
        }
        // If over 45% of total people starved across all years
        double starveRate = (double) totalStarved / (totalStarved + population);
        if (starveRate > 0.45) {
            System.out.println("\n*** Over 45% of your people have starved. You are impeached! ***");
            return true;
        }
        return false;
    }

	static void printFinalScore() {
        System.out.println("\n════════════════════════");
        System.out.println("     FINAL REPORT       ");
        System.out.println("════════════════════════");
        System.out.println("Years ruled    : " + year);
        System.out.println("Population     : " + population);
        System.out.println("Acres owned    : " + acres);
        System.out.println("Grain stored   : " + grain);
        System.out.println("Total starved  : " + totalStarved);
        System.out.println("Acres/person   : " + (acres / Math.max(1, population)));
        System.out.println();

        // Rating based on acres per person and starvation
        int acresPerPerson = acres / Math.max(1, population);
        if      (totalStarved == 0 && acresPerPerson > 10) System.out.println("Rating: LEGENDARY - A true Hammurabi!");
        else if (totalStarved < 50  && acresPerPerson > 7)  System.out.println("Rating: NOBLE     - Well ruled.");
        else if (totalStarved < 100 && acresPerPerson > 4)  System.out.println("Rating: FAIR      - Could be better.");
        else                                                  System.out.println("Rating: POOR      - Your people suffered.");
    }
}


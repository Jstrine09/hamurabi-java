import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.util.InputMismatchException;


public class Hammurabi {

	static int year        = 1;
    static int population  = 100;   // people
    static int grain       = 2800;  // bushels in storage
    static int acres       = 1000;  // acres of land owned
    static int totalStarved = 0;

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

	static int askAcresToBuy(){
		while (true) {
			System.err.println("\nHow many acres would you like to buy?");
			

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
			System.err.println("Rats at: " + ratsAte + " bushels");
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

	


	public static void main(String[] args) throws IOException {
			printWelcome();
	
			for (year = 1; year <= 10; year++) {
				printStatus();
			
				int acresToBuy = askAcresToBuy();
				int grainToFeed = askGrainToFeed();
				int acresToPlant = askAcresToPlant();

				process(acresToBuy, grainToFeed, acresToPlant);

				if (gameOver()) break;
			}
			
			printFinalScore();
			
		}


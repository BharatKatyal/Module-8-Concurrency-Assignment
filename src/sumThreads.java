import java.util.Random;

public class sumThreads extends Thread {

	private int[] array; // Initialize the array
	private int low, high, partial; // Initialize high low and partial sum for adding 
	static int min = 1; // Min number range for integers 
	static int max = 10; // Max number range for integers 

	public sumThreads(int[] array, int low, int high) {
		this.array = array;
		this.low = low;
		this.high = Math.min(high, array.length);
	}

	public int getPartialSum() {
		return partial;
	}

	public void run() {
		partial = sum(array, low, high);
	}

	public static int sum(int[] array) {
		return sum(array, 0, array.length);
	}

	public static int sum(int[] array, int low, int high) {
		int total = 0;

		for (int i = low; i < high; i++) {
			total += array[i];
		}

		return total;
	}

	public static int parallelSum(int[] array) {
//    	System.out.println(Runtime.getRuntime().availableProcessors()); Get the number of threads available 
		return parallelSum(array, Runtime.getRuntime().availableProcessors());
	}

	public static int parallelSum(int[] array, int threads) {
		int size = (int) Math.ceil(array.length * 1.0 / threads); // Distributes evenly between the threads

//		System.out.println("This is the size "+ size);
		sumThreads[] sums = new sumThreads[threads]; // Creates sumThreads based off the number of treads

		for (int i = 0; i < threads; i++) { // Runs instances and leverages all processes/threads
			sums[i] = new sumThreads(array, i * size, (i + 1) * size);
			sums[i].start();
		}

		try {
			for (sumThreads sum : sums) { // Join method to  join threads 
				sum.join();
			}
		} catch (InterruptedException e) {
		}

		int total = 0;

		for (sumThreads sum : sums) {
			total += sum.getPartialSum();
		}

		return total;
	}

	public static void main(String[] args) {
		Random rand = new Random(); // Generates Random Numbers
		int count = 0;

		int[] array = new int[20000000]; // Generates an Array of 20 million numbers

		for (int i = 0; i < array.length; i++) { // Adds to the array, using the array length
			array[i] = rand.nextInt(max + 1 - min) + min;// Uses the Min and Max on line 10/11 as range
			count++;
		}
		System.out.println(count + " Numbers Added Successfully");

		long startTime = System.currentTimeMillis(); // Log Current time for single thread

//        sumThreads.sum(array);

		System.out.println("Single Thread Sum Is   : " + sumThreads.sum(array) + " Single Thread Time  : "
				+ (System.currentTimeMillis() - startTime) + " milliseconds."); // Single: 44

		startTime = System.currentTimeMillis(); // Log Current time for running multiple threads

//        sumThreads.parallelSum(array);

		System.out.println("Parallel Thread Sum is : " + sumThreads.parallelSum(array) + " Parallel Tread Time : "
				+ (System.currentTimeMillis() - startTime) + " milliseconds."); // Parallel: 25
	}

}
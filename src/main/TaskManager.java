package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Used to calculate max value between int values
 * 
 * @author Sitnikov
 */
public abstract class TaskManager {
	private final static int MAX_COUNT_OF_THREADS_DEFAULT_VALUE = 10;
	private final static String INPUT_FILE_NAME_DEAFAULT_VALUE = "input.txt";

	public static void main(String[] args) {	
		try {	
			// if input file name is not specified , then program uses INPUT_FILE_NAME_DEAFAULT_VALUE by default
			 String inputFileName = args.length > 0 ?  args[0] : INPUT_FILE_NAME_DEAFAULT_VALUE;
			 File input = new File(inputFileName);;
			// if threads number is not specified , then program uses MAX_COUNT_OF_THREADS_DEFAULT_VALUE by default
			int threadsNumber = args.length > 1 ? Integer.parseInt(args[1]) : MAX_COUNT_OF_THREADS_DEFAULT_VALUE; 			
			
			int result = calculateMax(threadsNumber, input);
			System.out.println("The max value is " + Integer.toString(result));
		} catch (NumberFormatException e) {
			System.err.println("Can't parse to int");
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println("Can't find input file.");
		} catch (IOException e) {
			System.err.println("Can't read from this file.");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static int calculateMax(int threadsNumber, File input) throws InterruptedException, ExecutionException, IOException {
		ExecutorService exec = Executors.newFixedThreadPool(threadsNumber);
		BufferedReader br = null;
		List<Future<Integer>> resultList = new ArrayList<Future<Integer>>();
		
		try {
			br = new BufferedReader(new FileReader(input));
			if (!br.ready()) throw new IOException(); 
			while (br.ready()) {
				String inputData = br.readLine();
				if (inputData == null || inputData.isEmpty()) throw new IllegalStateException("Can't correctly read from input file");						
				resultList.add(exec.submit(new Worker(inputData)));
			}
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Executor will not accept new threads and will finish all existing threads in the queue
		exec.shutdown();
		//calculate
		int result = Integer.MIN_VALUE;	
		for (Future<Integer> f : resultList) {
			result = Math.max(f.get(), result);
		}
		return result;	
	}
}

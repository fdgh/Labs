package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskManager {
	private final static int MAX_COUNT_OF_THREADS = 4;
	
	public static void main(String[] args) {	
		try {	
			ExecutorService exec = Executors.newFixedThreadPool(MAX_COUNT_OF_THREADS);
			
			String inputFileName;
			// if input file name is not specified , then program uses 'input.txt' by default
			inputFileName = args.length == 0 ? "input.txt" : args[0];
			final String[] inputData = getInputDataFromFile(inputFileName);	
			if (inputData == null || inputData.length == 0) throw new IllegalStateException("Can't correctly read from input file");		
			List<Future<Integer>> resultList = new ArrayList<Future<Integer>>();
			final int sizeOfPortion = inputData.length / MAX_COUNT_OF_THREADS;
			if (sizeOfPortion == 0) {
				resultList.add(exec.submit(new Worker(inputData)));
			} else {
				for ( int i = 0; i < MAX_COUNT_OF_THREADS - 1; i++) {
					resultList.add(exec.submit(new Worker(inputData, i * sizeOfPortion, (i + 1) * sizeOfPortion)));
				}
				resultList.add(exec.submit(new Worker(inputData, (MAX_COUNT_OF_THREADS - 1) * sizeOfPortion, inputData.length - 1)));
			}
			//Executor will not accept new threads
			//and will finish all existing threads in the queue
			exec.shutdown();
			// wait until all threads are finish
			while(!exec.isTerminated()) {				
			}
			int result = Integer.MIN_VALUE;	
			for (int i = 0; i < resultList.size(); i++) {
				result = Math.max(resultList.get(i).get(), result);
			}
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

	private static String[] getInputDataFromFile(String fileName) throws IOException {
		if (fileName == null) throw new NullPointerException("File name can't be null");
		BufferedReader br = null;
		String[] inputData = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			inputData = br.readLine().split(" ");			
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				System.err.print("Can't close input file.");
			}
		}

		return inputData;
	}
}

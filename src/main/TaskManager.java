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
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<int[]> data = null;
		try {
			// if input file name is not specified , then program uses 'input.txt' by default
			data = args.length == 0 ? getInputDataFromFile("input.txt") : getInputDataFromFile(args[0]);
			
			if (data == null) throw new IllegalStateException();
			List<Future<Integer>> resultList = new ArrayList<Future<Integer>>(data.size());
			for (int[] inputMas : data) {
				 resultList.add(exec.submit(new Worker(inputMas)));
			}
			int result = exec.submit(new Worker(resultList)).get();
			System.out.print("The max value is " + Integer.toString(result));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			exec.shutdown();
		}
	}

	private static List<int[]> getInputDataFromFile(String fileName) {
		if (fileName == null) throw new NullPointerException("File name can't be null");
		BufferedReader br = null;
		List<int[]> list = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			list = new ArrayList<int[]>();
			String[] inputData = br.readLine().split(" ");
			//TODO to know how to find it
			int sizeOfPortion = inputData.length;
			if (sizeOfPortion / 4 > 0) sizeOfPortion /= 4;
			int j = 0;
			while (j < inputData.length) {
				//size of tempMas equals sizeOfPotrion or less 
				int[] tempMas = new int[Math.min(sizeOfPortion, inputData.length - j)];
				for (int i = 0; i < sizeOfPortion && j < inputData.length; i++) {
					tempMas[i] = Integer.parseInt(inputData[j]);
					j++;
				}
				list.add(tempMas);
			}
		} catch (FileNotFoundException e) {
			System.err.print("Can't find input file.");
		} catch (IOException e) {
			System.err.print("Can't read from this file.");
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				System.err.print("Can't close input file.");
			}
		}

		return list;
	}
}

package main;

import java.util.concurrent.Callable;

public class Worker implements Callable<Integer>{
	int max;

	@Override
	public Integer call() throws Exception {
		return max;
	}
	
	/**
	 * Parse Strings and find max number between them
	 * @param mas contains mas of numbers, can't be empty or null
	 * 
	 */
	public Worker(String[] mas) throws IllegalArgumentException {
		this(mas, 0, mas.length - 1);		
	}
	
	/**
	 * Parse Strings from start position of mas to end and find max number between them
	 * @param mas contains mas of numbers, can't be empty or null
	 * @param start position of mas from which we start to parse and find max
	 * @param end position of mas until which we parse and find max
	 * @throws IllegalArgumentException
	 */	 
	public Worker(String[] mas, int start, int end) throws IllegalArgumentException {
		if (start > end) throw new IllegalArgumentException();
		max = Integer.parseInt(mas[start]);
		for (int i = start; i <= end; i++) {
			max = Math.max(Integer.parseInt(mas[i]), max);
		}
	}
	
}

package main;

import java.util.concurrent.Callable;

public class Worker implements Callable<Integer> {
	String data;

	@Override
	public Integer call() throws Exception {
		String[] mas = data.split(" ");
		int max = Integer.parseInt(mas[0]);
		for (String s : mas) {
			max = Math.max(Integer.parseInt(s), max);
		}
		return max;
	}

	public Worker(String data) throws IllegalArgumentException {
		this.data = data;
	}
}

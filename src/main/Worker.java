package main;

import java.util.concurrent.Callable;

public class Worker implements Callable<Integer>{
	int[] mas;

	@Override
	public Integer call() throws Exception {
		return getMax();
	}
	
	private int getMax() {
		int max = Integer.MIN_VALUE;
		for (int t : mas) {
			max = Math.max(t, max);
		}
		return max;
	}

	public Worker(int[] mas) {
		this.mas = mas;
	}
	
	public Worker(String[] mas) {
		this(mas, 0, mas.length - 1);
	}
	
	public Worker(String[] mas, int start, int end) {
		this.mas = new int[end - start + 1];
		//TODO ask about it
		for (int i = start, j = 0 ; i <= end; i++, j++) {
			this.mas[j] = Integer.parseInt(mas[i]);
		}
	}
	
}

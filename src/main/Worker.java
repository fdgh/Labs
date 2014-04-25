package main;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
	
	public Worker(List<Future<Integer>> list) {
		this.mas = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			try {
				this.mas[i] = list.get(i).get();
			} catch (InterruptedException | ExecutionException e) {			
				e.printStackTrace();
			}
		}
	}
}

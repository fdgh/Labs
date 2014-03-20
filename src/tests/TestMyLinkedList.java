package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import main.MyLinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class TestMyLinkedList {
	MyLinkedList<Integer> tester;
	
	@Rule 
	public final Timeout timeout = new Timeout(1000);
	
	@Before
	public void runBeforeEveryTest(){
		 tester = new MyLinkedList<Integer>();
		 tester.addLast(25);
		 tester.addLast(14);
		 tester.addLast(25);
		 tester.addLast(null);
		 tester.addLast(10);
		 tester.addLast(-5);
		 tester.addLast(null);
	}
	
	@After
	public void runAfterEveryTest(){
		tester = null;
	}
	
	@Test
	public void size() {
		assertEquals("expected size == 7", 7, tester.size());
	}
	
	@Test
	public void sizeAfterClear() {
		tester.clear();
		assertEquals("expected size == 0", 0, tester.size());
	}
	
	@Test
	public void sizeAfterRemove() {
		Integer i = 25;
		tester.remove(i);
		assertEquals("expected size == 6", 6, tester.size());
	}
	
	public void Remove() {
		Boolean removeResult = tester.remove(null);
		assertEquals("expected size true", true, removeResult);
	}
	
	@Test
	public void getElement() {
		Integer i = 25;
		assertEquals("expected element == 25", i, tester.get(0));
	}
	
	@Test
	public void getLast() {	
		Integer i = 45;
		tester.addLast(i);
		assertEquals("expected element == null", i, tester.getLast());
	}
	
	@Test
	public void getFirst() {
		Integer i = 25;
		assertEquals("expected element == 14", i, tester.getFirst());
	}
	
	@Test
	public void addFirst() {
		Integer i = 99;
		tester.addFirst(99);
		assertEquals("expected element == 14", i, tester.getFirst());
	}
	
	@Test
	public void addLast() {
		Integer i = 77;
		tester.addLast(77);
		assertEquals("expected element == 14", i, tester.getLast());
	}
	
	@Test
	public void contains() {
		Integer i = 14;
		Boolean containsResult = tester.contains(i);
		assertEquals("expected true", true, containsResult);
	}
	
	@Ignore
	@Test
	public void retainAll() {
		List<Integer> retainResult = new ArrayList<Integer>();
		retainResult.add(-5);
		tester.retainAll(retainResult);
		assertEquals("wrong retain", retainResult, tester);
	}
	
}

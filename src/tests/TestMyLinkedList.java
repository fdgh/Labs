package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import main.MyLinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestMyLinkedList {
	 MyLinkedList<Integer> tester;
	 
	@Before
	public void runBeforeEveryTest(){
		 tester = new MyLinkedList<Integer>();
		 tester.add(25);
		 tester.add(14);
		 tester.add(25);
		 tester.add(null);
		 tester.add(10);
		 tester.add(-5);
		 tester.add(null);
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
		Integer i = 14;
		assertEquals("expected element == 14", i, tester.getElement(1));
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
		List<Integer> myList = new ArrayList<Integer>();
		myList.add(-5);
		MyLinkedList<Integer> retainResult = new MyLinkedList<Integer>();
		retainResult.add(-5);
		tester.retainAll(myList);
		assertEquals("wrong retain", retainResult, tester);
	}
	
}

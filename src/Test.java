import java.util.ArrayList;
import java.util.List;


public class Test {

	public static void main(String[] args) {
		MyLinkedList<Integer> a = new MyLinkedList<Integer>();
		Integer b = new Integer(4);
		a.add(b);
		a.add(b);
		a.add(b);
		a.add(99, 1);
		a.add(6);
		a.add(7);
		a.add(8);
		a.add(9);
		List<Integer> mas = new ArrayList<Integer>();
		mas.add(b);
		System.out.println(a.size());		
		System.out.println(a.retainAll(mas));
		System.out.println(a.size());
		System.out.println(a.contains(99));
	}

}

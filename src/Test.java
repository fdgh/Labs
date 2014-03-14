
public class Test {

	public static void main(String[] args) {
		MyLinkedList<Integer> a = new MyLinkedList<Integer>();
		Integer b = new Integer(4);
		a.add(b);
		a.add(b);
		a.add(b);
		a.add(6);
		System.out.println(a.size());		
		System.out.println(a.remove(b));
		System.out.println(a.size());
	}

}

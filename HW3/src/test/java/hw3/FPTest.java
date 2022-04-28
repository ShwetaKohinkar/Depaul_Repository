package hw3;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.*;


public class FPTest {

	@Test
	public void testMap() {
		LinkedList<Integer> l = new LinkedList<>();
		FunctionMap fpMap = new FunctionMap();

		List<String> testSetS = Arrays.asList("Mary", "Isla", "Sam", "Shweta");
		List<Integer> testSetI = new ArrayList<>();
		testSetS.stream().forEach(x -> testSetI.add(x.hashCode()));

		assertArrayEquals(testSetI.toArray(), FP.map(testSetS, (String x) -> {return x.hashCode();}).toArray());

		for (int i=0; i < 5; i++) l.add(i+1); // [5,4,3,2,1] list(INTEGER)
		
	     // Integer ==> Boolean
		 // static <U,V> List<V> map(Iterable<U> l, Function<U,V> f) 
		 // List(Boolean)
		
		List<Boolean> b = FP.map(l,
				(Integer x) -> { return x%2 ==0;});
		Boolean[] s = {false,true,false,true,false};
		assertArrayEquals(b.toArray(),s);
		
		List<Integer> u = FP.map(l, (Integer x) -> {return x+1;});
		Integer[] r = {2,3,4,5,6};
		assertArrayEquals(u.toArray(),r);
	}


	@Test
	public void testFoldLeft() {
		
		LinkedList<Integer> l = new LinkedList<>();
		assertTrue(0 == FP.foldLeft(0, l, (Integer x, Integer y) -> {return x+y;}));
		for (int i=0; i < 10; i++) l.addLast(i+1); // [1,2,3,4,5,6,7,8,9,10]
		assertTrue(55 == FP.foldLeft(0, l, (Integer x, Integer y) -> {return x+y;}));
		// add the numbers of the list
//	          0 + 1
//				1 + 2
//				   3 + 3
//				      6 + 4 .... 55
		  
		LinkedList<String> s = new LinkedList<>();
		String r = "";
		for (int i=0; i < 4; i++) {
			s.addLast(String.valueOf(i));
			r = r.concat(String.valueOf(i));
		}
		assertTrue(r.equals(FP.foldLeft("", s, (String x, String y) -> {return x.concat(y);})));
		              // concatenate the strings
	}


	@Test
	public void testFoldRight()
	{
		
		
		LinkedList<String> s = new LinkedList<>();
		String r = "";
		for (int i=4; i > 0; i--) {
			s.addLast(String.valueOf(i));
			r = String.valueOf(i).concat(r);
		}
		assertTrue(r.equals(FP.foldRight("", s, (String x, String y) -> {return y.concat(x);})));
		
		LinkedList<Integer> l1 = new LinkedList<>();
		for (int j=0; j < 3; j++) 
			l1.addLast(j+1);
		// [1,2,3]
		assertTrue(2 == FP.foldRight(0, l1, (Integer x, Integer y) -> {return x-y;}));
		// 3 -0 =3. 2-3 =-1.  1-(-1) = 2

		LinkedList<Integer> l = new LinkedList<>();
		assertTrue(0 == FP.foldRight(0, l, (Integer x, Integer y) -> {return x+y;}));
		for (int i=0; i < 10; i++) l.addLast(i+1); // [1,2,3,4,5,6,7,8,9,10]
		assertTrue(55 == FP.foldRight(0, l, (Integer x, Integer y) -> {return x+y;}));
	}

	@Test
	public void testFilter()
	{
		LinkedList<Integer> l = new LinkedList<>();
		for (int i=0; i < 5; i++) l.addFirst(i+1); //[1,2,3,4,5]
		Iterable<Integer> i = FP.filter(l, (Integer x ) -> {return x%2 != 0;});
		  // [1,3,5]
		int u = 0;
		for (Integer a: i) u++;
		assertTrue(u==3);
		
		Iterable<Integer> j = FP.filter(l, (Integer x ) -> {return x%2 == 0;});
		  // [2,4]
		int u1 = 0;
		for (Integer a: j) { u1++; }
		assertTrue(u1==2);

		List<Person> p = new ArrayList<>();

		p.add( new Person(120000, "test1"));
		p.add( new Person(50000, "test2"));
		p.add( new Person(200000, "test3"));
		p.add( new Person(110000, "test4"));
		p.add( new Person(10000, "test5"));

		Iterable<Person> names = FP.filter(p, (Person x) -> {return  x.getSalary()>100000; } );

		int k = 0;
		for (Person a: names) { k++; }
		assertTrue(k==3);


	}

	@Test
	public  void testminVal(){
		List<Integer> l =  Arrays.asList(100, 30, 45, 25, 15, 20,10 , 35);


		//assertTrue(1 == );
		assertTrue( 10 == FP.minVal(l, Integer::compare));

	}


	@Test
	public  void testminPos(){
		List<Integer> l =  Arrays.asList(100, 30, 45, 25, 15, 20,10, 35);

		assertTrue( 6 == FP.minPos(l));

	}

}

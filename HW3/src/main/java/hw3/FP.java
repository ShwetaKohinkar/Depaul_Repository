package hw3;

import java.util.*;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Class FP
 */
public class FP {
	
// f takes U to V.. ie. f.apply(U): V

	/**
	 * Method to concatinate given list of strings
	 * @param l
	 * @param f
	 * @param <U>
	 * @param <V>
	 * @return
	 */
	static <U,V> List<V> map(Iterable<U> l, Function<U,V> f) {
	List<V> listV = new ArrayList<>();
	// walk through the U's
	// use f at every stage f.apply
	// construct list of V's
	for (U x: l)
	{
		listV.add(f.apply(x));
	}
	return listV;
}

	/**
	 * Method to find sum of integers using foldleft
	 * @param e
	 * @param l
	 * @param f
	 * @param <U>
	 * @param <V>
	 * @return
	 */
	static <U,V> V foldLeft(V e, Iterable<U>l, BiFunction<V,U,V> f){
	// walk through the U's [u1,u2, ..,un]
	//                       e
	// use f at every stage v1= f.apply(e,u1)
	//                         v2= f.apply(v1,u2)
	//						    v3= f.apply(v2,u3)..
	// return the last v

		for(U x:l){
			e = f.apply(e,x);
		}
	return e;
}

// similar to above
// but from the right
//     vn=  f(un,e)
//          vn-1 = f(un-1,vn)
// ..
// return the first v

	/**
	 * Method to find sum of integers using foldright
	 * @param e
	 * @param l
	 * @param f
	 * @param <U>
	 * @param <V>
	 * @return
	 */
	static <U,V> V foldRight(V e, List<U>l, BiFunction<U,V,V> f){
	V temp = e;
	for (int i = l.size() - 1 ; i >= 0 ; i--)
	{
		temp = f.apply(l.get(i),temp);
	}
	return temp;
}


	/**
	 * Method to get Person with salary >= 100000
	 * @param l
	 * @param p
	 * @param <U>
	 * @return
	 */
	static <U> Iterable<U> filter(Iterable<U> l, Predicate<U> p)
{
    List<U> temp = new ArrayList<>();
	for(U x : l)
	{
		if(p.test(x))
		 temp.add(x);
	}
	return temp;
}

	/**
	 * Method to find min value from the given list
	 * @param l
	 * @param c
	 * @param <U>
	 * @return
	 */
	static <U> U minVal(Iterable<U> l, Comparator<U> c){
	// write using fold.  No other loops or recursion permitted
	return foldLeft(l.iterator().next(), l, (U u0, U u1)  -> c.compare(u0, u1) < 0 ? u0 : u1);
}

	/**
	 * Method to find position of lowest integer
	 * @param l
	 * @param <U>
	 * @return
	 */
	static <U extends Comparable<U>> int minPos(Iterable<U> l){
	// write using fold.  No other loops or recursion permitted. 
	List<U> myList = new ArrayList<>();

	l.iterator().forEachRemaining(myList::add);

	return myList.indexOf( foldLeft(l.iterator().next(), l, (U u1, U u2) -> u1.compareTo(u2) < 0 ? u1 : u2));


}


	public static void main(String[] args) {
		// (1) Use map to implement the following behavior (described in Python).  i.e given a List<T> create a List<Integer> of the hashes of the objects.
		// names = ['Mary', 'Isla', 'Sam']
		// for i in range(len(names)):
		       // names[i] = hash(names[i]
		
		// (2) Use foldleft to calculate the sum of a list of integers.
		// i.e write a method: int sum(List<Integer> l)
		
		// (3) Use foldRight to concatenate a list of strings i.e write a method
		// String s (List<String> l)
		
		// (4) consider an array of Persons. Use filter to 
		// print the names of the Persons whose salary is
		// greater than 100000
		
		// (5) Use minVal to calculate the minimum of a List of 
		//       Integers
		
        // (6) Use minVal to calculate the maximum of a List of 
		//       Integers
		
		// (7)  Use minPos to calculate the position of the
		// minimum in  a List of  Integers

		// (8)  Use minPos to calculate the position of the
		// minimum in  a List of  String
	}

}

/**
 * Class Person
 */
class Person {
	final int salary;
	final String name;
	
	Person(int salary, String name){
		this.salary = salary;
		this.name = name;
	}
	
	int getSalary() { return salary; }
	String name() { return name;}
	
}
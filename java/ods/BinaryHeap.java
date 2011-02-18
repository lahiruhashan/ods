package ods;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * This class implements a priority queue as a class binary heap
 * stored implicitly in an array
 * @author morin
 *
 * @param <T>
 */
public class BinaryHeap<T extends Comparable<T>> extends AbstractQueue<T> {
	Factory<T> f;
	
	/**
	 * Our backing array
	 */
	protected T[] a;
	
	/**
	 * The number of elements in the priority queue
	 */
	protected int n;

	/**
	 * Grow the backing array a
	 */
	protected void grow() {
		T[] b = f.newArray(a.length*2);
		System.arraycopy(a, 0, b, 0, n);
		a = b;
	}
	
	/**
	 * Shrink the backing array a if necessary
	 */
	protected void shrink() {
		if (n > 0 && 3*n < a.length) {
			T[] b = f.newArray(n*2);
			System.arraycopy(a, 0, b, 0, n);
			a = b;
 		}
	}
	
	/**
	 * Create a new empty binary heap
	 * @param c
	 */
	public BinaryHeap(Class<T> c) {
		f = new Factory<T>(c);
		a = f.newArray(1);
		n = 0;
	}


	/**
	 * Create a new binary heap by heapifying a
	 * @param ia
	 */
	public BinaryHeap(T[] ia) {
		a = ia;
		n = a.length;
		for (int i = n/2; i >= 0; i--) {
			trickleDown(i);
		}
	}

	/**
	 * @param i
	 * @return the index of the left child of the value at index i
	 */
	protected int left(int i) {
		return 2*i + 1;
	}

	/**
	 * @param i
	 * @return the index of the left child of the value at index i
	 */
	protected int right(int i) {
		return 2*i + 2;
	}

	/**
	 * @param i
	 * @return the index of the parent of the value at index i
	 */
	protected int parent(int i) {
		return (i-1)/2;
	}
	

	public int size() {
		return n;
	}
	
	/**
	 * Swap the two values a[i] and a[j]
	 * @param i
	 * @param j
	 */
	final protected void swap(int i, int j) {
		T x = a[i];
		a[i] = a[j];
		a[j] = x;
	}
	
	/**
	 * Run the bubbleUp routine at position i
	 * @param i
	 */
	protected void bubbleUp(int i) {
		int p = parent(i);
		while (i > 0 && a[i].compareTo(a[p]) < 0) {
			swap(i,p);
			i = p;
			p = parent(i);
		}
	}
	
	/**
	 * Move element i down in the heap until the heap
	 * property is restored
	 * @param i
	 */
	protected void trickleDown(int i) {
		do {
			int j = -1;
			int r = right(i);
			if (r < n && a[r].compareTo(a[i]) < 0) {
				int l = left(i);
				if (a[l].compareTo(a[r]) < 0) {
					j = l;
				} else {
					j = r;
				}
			} else {
				int l = left(i);
				if (l < n && a[l].compareTo(a[i]) < 0) {
					j = l;
				}
			}
			if (j >= 0)	swap(i, j);
			i = j;
		} while (i >= 0);
	}
	
	public boolean offer(T x) {
		if (n + 1 > a.length)
			grow();
		a[n++] = x;
		bubbleUp(n-1);
		return true;
	}
	
	public T peek() {
		return a[0];
	}
	
	public T poll() {
		T x = a[0];
		swap(0, --n);
		trickleDown(0);
		return x;
	}
	
	public Iterator<T> iterator() {
		class PQI implements Iterator<T> {
			int i;
			public PQI() {
				i = 0;
			}
			public boolean hasNext() {
				return i < n;
			}
			public T next() {
				return a[i++];
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new PQI();
	}

	/**
	 * An implementation of the heapsort algorithm
	 * @param <T>
	 * @param a
	 */
	public static <T extends Comparable<T>> void sort(T[] a) {
		BinaryHeap<T> h = new BinaryHeap<T>(a);
		while (h.n > 1) {
			h.swap(--h.n, 0);
			h.trickleDown(0);
		}
		Collections.reverse(Arrays.asList(a));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BinaryHeap<Integer> h = new BinaryHeap<Integer>(Integer.class);
		Random r = new Random();
		int n = 20;
		for (int i = 0; i < n; i++) {
			h.add(r.nextInt(2500));
		}
		System.out.println(h);
		while (!h.isEmpty()) {
			System.out.print("" + h.remove() + ",");
		}
		System.out.println("");
		
		Integer[] a = new Integer[n];
		for (int i = 0; i < n; i++) {
			a[i] = r.nextInt(2500);
		}
		BinaryHeap.sort(a);
		for (Integer x : a) {
			System.out.print("" + x + ",");
		}
		System.out.println("");
		
		n = 100000;
		long start, stop;
		double elapsed;
		System.out.print("performing " + n + " adds...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			h.add(r.nextInt());
		}
		stop = System.nanoTime();
		elapsed = 1e-9*(stop-start);
		System.out.println("(" + elapsed + "s [" 
				+ (int)(((double)n)/elapsed) + "ops/sec])");

		n *= 10;
		System.out.print("performing " + n + " add/removes...");
		start = System.nanoTime();
		for (int i = 0; i < n; i++) {
			if (r.nextBoolean()) {
				h.add(r.nextInt());
			} else {
				h.remove();
			}
		}
		stop = System.nanoTime();
		elapsed = 1e-9*(stop-start);
		System.out.println("(" + elapsed + "s [" 
				+ (int)(((double)n)/elapsed) + "ops/sec])");
	}

}

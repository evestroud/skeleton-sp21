package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> ad = new ArrayDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", ad.isEmpty());
		ad.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad.size());
        assertFalse("lld1 should now contain 1 item", ad.isEmpty());

		ad.addLast("middle");
		assertEquals(2, ad.size());

		ad.addLast("back");
		assertEquals(3, ad.size());

		System.out.println("Printing out deque: ");
		ad.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create ArrayDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String>  lld1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();

        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    public void randomizedAddAndRemoveSizeTest() {

        ArrayDeque<Integer> lld = new ArrayDeque<Integer>();
        int N = 1000000;

        int size = 0;
        for (int n = 0; n < N; n += 1) {
            int action = StdRandom.uniform(5);
            switch (action) {
                case 0:
                    lld.addFirst(n);
                    size += 1;
                    break;
                case 1:
                    lld.addLast(n);
                    size += 1;
                    break;
                case 2:
                    Integer removedFirst = lld.removeFirst();
                    if (removedFirst != null) {
                        size -= 1;
                    }
                    break;
                case 3:
                    Integer removedLast = lld.removeLast();
                    if (removedLast != null) {
                        size -= 1;
                    }
                    break;
                case 4:
                    assertEquals(size, lld.size());
                    break;
            }
        }
    }

    @Test
    public void getTest() {

        ArrayDeque<Integer> lld = new ArrayDeque<Integer>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            lld.addLast(i);
        }

        for (int i = 0; i < N; i += 1) {
            assertEquals(i, (int) lld.get(i));
        }
    }

    public ArrayDeque<Integer> arrayDequeFromArray(Integer[] values) {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (Integer item : values) {
            ad.addLast(item);
        }
        return ad;
    }

    @Test
    public void iteratorTest() {
        Integer[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayDeque<Integer> lld = arrayDequeFromArray(values);

        int i = 0;
        for (int v : lld) {
            assertEquals(i, v);
            i += 1;
        }
    }

    @Test
    public void equalsTest() {
        Integer[] equalsValues = {0, 1, 2};
        Integer[] notEqualsValues = {1, 2, 3};
        Integer[] notEqualsShorter = {0, 1};
        Integer[] notEqualsLonger = {0, 1, 2, 3};
        ArrayDeque<Integer> ad = arrayDequeFromArray(equalsValues);
        LinkedListDeque<Integer> lld = new LinkedListDequeTest().linkedListDequeFromArray(equalsValues);

        assertEquals(ad, arrayDequeFromArray(equalsValues));
        assertEquals(lld, arrayDequeFromArray(equalsValues));
        assertNotEquals(ad, arrayDequeFromArray(notEqualsValues));
        assertNotEquals(lld, arrayDequeFromArray(notEqualsValues));
        assertNotEquals(ad, arrayDequeFromArray(notEqualsShorter));
        assertNotEquals(lld, arrayDequeFromArray(notEqualsShorter));
        assertNotEquals(ad, arrayDequeFromArray(notEqualsLonger));
        assertNotEquals(lld, arrayDequeFromArray(notEqualsLonger));
    }
}

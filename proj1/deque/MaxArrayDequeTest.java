package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    private static class LongestString implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }

    private static class AlphabeticString implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            // String.compareTo returns the later alphabetic string as having the higher value, we want the reverse
            return s2.compareTo(s1);
        }
    }

    private static class LargestInteger implements Comparator<Integer> {
        @Override
        public int compare(Integer i1, Integer i2) {
            return i1 - i2;
        }
    }

    private static class SmallestInteger implements Comparator<Integer> {
        @Override
        public int compare(Integer i1, Integer i2) {
            return i2 - i1;
        }
    }

    @Test
    public void testDefaultComparator() {
        Comparator<String> longestStringComparator = new LongestString();
        MaxArrayDeque<String> longestStringDeque = new MaxArrayDeque<>(longestStringComparator);
        String[] testValues = {"hi", "bye", "trie"};

        for (String s : testValues) {
            longestStringDeque.addFirst(s);
        }

        assertEquals("trie", longestStringDeque.max());
    }

    @Test
    public void testCustomComparator() {
        Comparator<String> longestStringComparator = new LongestString();
        Comparator<String> alphabeticComparator = new AlphabeticString();
        MaxArrayDeque<String> longestStringDeque = new MaxArrayDeque<>(longestStringComparator);
        String[] testValues = {"hi", "bye", "trie"};

        for (String s : testValues) {
            longestStringDeque.addFirst(s);
        }

        assertEquals("bye", longestStringDeque.max(alphabeticComparator));
    }

    @Test
    public void testDefaultComparatorMultipleAddAndRemove() {
        Comparator<Integer> largestIntegerComparator = new LargestInteger();
        MaxArrayDeque<Integer> largestIntegerDeque = new MaxArrayDeque<>(largestIntegerComparator);

        // Add 0-1023
        int ops = 1024;
        for (int i = 0; i < ops; i += 1) {
            largestIntegerDeque.addLast(i);
        }
        assertEquals(ops - 1, (int) largestIntegerDeque.max());

        // Remove all but the first item (0)
        for (int i = 0; i < ops - 1; i += 1) {
            largestIntegerDeque.removeLast();
        }
        assertEquals(0, (int) largestIntegerDeque.max());
    }

    @Test
    public void testCustomComparatorRandomAddAndRemove() {
        Comparator<Integer> largestIntegerComparator = new LargestInteger();
        Comparator<Integer> smallestIntegerComparator = new SmallestInteger();
        MaxArrayDeque<Integer> largestIntegerDeque = new MaxArrayDeque<>(largestIntegerComparator);

        // Add 0-1023 in reverse order
        int ops = 1024;
        for (int i = ops - 1; i >= 0; i -= 1) {
            largestIntegerDeque.addLast(i);
        }
        assertEquals(0, (int) largestIntegerDeque.max(smallestIntegerComparator));

        // Remove all but the first item (1023)
        for (int i = 0; i < ops - 1; i += 1) {
            largestIntegerDeque.removeLast();
        }
        assertEquals(1023, (int) largestIntegerDeque.max(smallestIntegerComparator));
    }
}

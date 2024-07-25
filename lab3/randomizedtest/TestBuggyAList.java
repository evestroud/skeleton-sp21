package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> noResizing = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();

        for (int i = 0; i < 3; i += 1) {
            noResizing.addLast(i);
            buggy.addLast(i);
        }

        for (int i = 0; i < 3; i += 1) {
            int nRRemoved = noResizing.removeLast();
            int bRemoved = buggy.removeLast();
            assertEquals(nRRemoved, bRemoved);
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            switch (operationNumber) {
                case 0:
                    // addLast
                    int randVal = StdRandom.uniform(0, 100);
                    L.addLast(randVal);
                    B.addLast(randVal);
                    break;
                case 1:
                    // size
                    int LSize = L.size();
                    int BSize = B.size();
                    assertEquals(LSize, BSize);
                    break;
                case 2:
                    //getLast
                    if (L.size() > 0) {
                        int LLast = L.getLast();
                        int BLast = B.getLast();
                        assertEquals(LLast, BLast);
                    }
                    break;
                case 3:
                    // removeLast
                    if (L.size() > 0) {
                        int LRemovedLast = L.removeLast();
                        int BRemovedLast = B.removeLast();
                        assertEquals(LRemovedLast, BRemovedLast);
                    }
                    break;
            }
        }
    }
}

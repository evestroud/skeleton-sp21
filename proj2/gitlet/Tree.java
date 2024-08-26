package gitlet;

import java.util.Iterator;
import java.util.Map;

/** Stores the file tree of a Commit.
 *
 * @author Eve Stroud
 * */
public class Tree implements Dumpable, Iterable<String> {

    /** Maps file names to the hash codes of their version in the commit. */
    private final Map<String, String> files;
    /** The hash code of this Tree. */
    final String hash;

    /** Creates a tree out of a map of file names and their staged versions. */
    public Tree(Map<String, String> staged) {
        files = staged;
        hash = Utils.sha1(files.toString());
    }

    /** Return a string representation of the information stored in the Tree. */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String file: files.keySet()) {
            s.append(file);
            s.append(":\t");
            s.append(files.get(file));
        }
        return s.toString();
    }

    @Override
    public void dump() {
        System.out.println(this);
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }
}

package gitlet;

import java.util.Date;

/** Represents a gitlet commit object.
 *  Stores a tree pointing to versions of the files that make up the commit
 *  and useful metadata about it.
 *
 *  @author Eve Stroud
 */
public class Commit implements Dumpable {

    /** The parent Commit of this Commit. */
    final String parent;
    /** The file tree of this Commit. */
    final String tree;
    /** The timestamp of this Commit. */
    final String timestamp;
    /** The message of this Commit. */
    final String message;
    /** The SHA-1 hash of this commit. */
    final String hash;

    /** Create a new Commit out of a parent Commit, a commit Tree, and a commit message. */
    public Commit(String parent, String tree, String message) {
        this.parent = parent;
        this.tree = tree;
        this.message = message;
        timestamp = String.format("%1$ta %1$tb %1$td %1$tH:%1$tM:%1$tS %1$tY %1$tz", new Date());
        hash = Utils.sha1(parent, tree, message, timestamp);
    }

    /** Create the initial commit for a repository. */
    public Commit() {
        parent = "";
        tree = "";
        message = "initial commit";
        timestamp = String.format("%1$ta %1$tb %1$td %1$tH:%1$tM:%1$tS %1$tY %1$tz", new Date(0));
        hash = Utils.sha1(parent, tree, message, timestamp);
    }

    /** Return a string representation of the information stored in the commit. */
    @Override
    public String toString() {
        return "Parent: " + parent + "\n" +
                "Tree: " + tree + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Message: " + message + "\n";
    }

    /** Print a string representation of the information stored in the commit. */
    @Override
    public void dump() {
        System.out.println(this);
    }
}

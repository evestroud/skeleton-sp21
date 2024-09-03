package gitlet;

import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/** Represents the index of the repository, which keeps track of the files and
 * their versions in the working directory and staging area.
 *
 * @author strbytes
 * */
public class Index implements Dumpable {
    /** Stores file names as the key and an array of SHA-1 hashes of the working
     * version and the saved versions in the repository.
     *
     * Stored versions: {working directory, staged, previous commit}
     * */
    private final Map<String, String[]> files;
    /** Headers for the array of versions (used in toString). */

    /** Initializes the index. */
    public Index() {
        files = new TreeMap<>();
    }

    /** Returns a set of all the files currently tracked. */
    public Set<String> getFiles() {
        return new HashSet<>(files.keySet());
    }

    /** Returns whether the index contains a file with the name fileName. */
    public boolean contains(String fileName) {
        return files.containsKey(fileName);
    }

    /** Adds a new file to the index, or updates the index if the file is already present.
     *
     * @param fileName Name of the file to add.
     * @param hash Hash of the file contents.
     */
    public void addFile(String fileName, String hash) {
        if (contains(fileName)) {
            String[] versions = files.get(fileName);
            versions[1] = versions[0];
        } else {
            files.put(fileName, new String[] {hash, hash, ""});
        }
    }

    public boolean changesStaged() {
        for (String file : files.keySet()) {
            if (!files.get(file)[1].equals(files.get(file)[2])) {
                return true;
            }
        }
        return false;
    }

    /** Update the version of a tracked file in the working directory. Called
     * with every Gitlet command except init.
     *
     * @param fileName Name of the file to add.
     * @param hash Hash of the file contents.
     */
    public void updateFile(String fileName, String hash) {
        if (!contains(fileName)) {
            files.put(fileName, new String[] {hash, "", ""});
        } else if (hash.isEmpty() && !isTracked(fileName)) {
            files.remove(fileName);
        } else {
            files.get(fileName)[0] = hash;
        }
    }

    public void rmFile(String fileName) {
        assert contains(fileName);
        files.get(fileName)[0] = "";
        files.get(fileName)[1] = "";
    }

    /** Returns a map of staged modifications to files in the repository, with file names as keys and the staged
     *  versions of those files in the repository as values. Used when committing. */
    public Map<String, String> getStagedChanges() {
        Map<String, String> staged = new TreeMap<>();
        for (String file: files.keySet()) {
            if (isStaged(file)) {
                staged.put(file, files.get(file)[1]);
            }
        }
        return staged;
    }

    /** Returns whether a file has been staged. */
    public boolean isStaged(String file) {
        String stagedHash = files.get(file)[1];
        String commitHash = files.get(file)[2];
        return !stagedHash.isEmpty() && !stagedHash.equals(commitHash);
    }

    /** Returns a map of staged removals of files in the repository, with file names as keys and the staged
     *  versions (all empty strings in this case) of those files in the repository as values. Used when committing. */
    public Map<String, String> getStagedRemovals() {
        Map<String, String> removed = new TreeMap<>();
        for (String file: files.keySet()) {
            if (isRemoved(file)) {
                removed.put(file, files.get(file)[1]);
            }
        }
        return removed;
    }

    /** Returns whether a file has had its removal staged. */
    public boolean isRemoved(String file) {
        String stagedHash = files.get(file)[1];
        String commitHash = files.get(file)[2];
        return stagedHash.isEmpty() && !stagedHash.equals(commitHash);
    }

    /** Returns a map of unstaged modifications to files in the repository, with file names as keys and the unstaged
     *  versions of those files in the repository as values. Used when committing. */
    public Map<String, String> getUnstagedChanges() {
        Map<String, String> unstaged = new TreeMap<>();
        for (String file: files.keySet()) {
            if (isTracked(file) && hasUnstagedChanges(file)) {
                unstaged.put(file, files.get(file)[0]);
            }
        }
        return unstaged;
    }

    /** Returns whether a file has unstaged changes. */
    public boolean hasUnstagedChanges(String file) {
        String workingHash = files.get(file)[0];
        String stagedHash = files.get(file)[1];
        return !workingHash.equals(stagedHash);
    }

    /** Returns a map of untracked files to files in the repository, with file names as keys and the working directory
     *  versions of those files in the repository as values. Used when committing. */
    public Map<String, String> getUntrackedFiles() {
        Map<String, String> untracked = new TreeMap<>();
        for (String file: files.keySet()) {
            if (!isTracked(file)) {
                untracked.put(file, files.get(file)[0]);
            }
        }
        return untracked;
    }

    /** Returns whether fileName is tracked (staged or committed) by the current repository. */
    public boolean isTracked(String fileName) {
        return contains(fileName) && (!files.get(fileName)[1].isEmpty() || !files.get(fileName)[2].isEmpty());
    }

    /** Updates the repo version of the file to match the staged version.
     * Used when committing. */
    void commitStaged() {
        for (String file: files.keySet()) {
            files.get(file)[2] = files.get(file)[1];
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        String[] versionHeaders = new String[] {"wdir", "stage", "repo"};

        for (String f: files.keySet()) {
            s.append(f);
            s.append("\n");
            for (int i = 0; i < 3; i++) {
                s.append("\t");
                s.append(versionHeaders[i]);
                s.append("\t");
                s.append(files.get(f)[i]);
                s.append("\n");
            }
        }
        return s.toString();
    }

    @Override
    public void dump() {
        System.out.println(this);
    }
}

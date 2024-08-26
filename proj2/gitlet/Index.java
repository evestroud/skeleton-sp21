package gitlet;

import java.util.Map;
import java.util.HashMap;
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
        files = new HashMap<>();
    }

    /** Returns a set of all the files currently tracked. */
    public Set<String> getFiles() {
        return files.keySet();
    }

    /** Returns whether the index contains a file with the name fileName. */
    public boolean contains(String fileName) {
        return files.containsKey(fileName);
    }

    /** Returns whether fileName is tracked (staged or committed) by the current repository. */
    public boolean isTracked(String fileName) {
        return contains(fileName) && !files.get(fileName)[1].isEmpty() && !files.get(fileName)[2].isEmpty();
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

    /** Returns whether the working directory version of a file has been modified
     * relative to the staged and repository versions. */
    public boolean modified(String fileName) {
        assert contains(fileName);
        String[] versions = files.get(fileName);
        return !versions[0].equals(versions[1]) || !versions[0].equals(versions[2]);
    }

    /** Update the version of a tracked file in the working directory. Called
     * with every Gitlet command except init.
     *
     * @param fileName Name of the file to add.
     * @param hash Hash of the file contents.
     */
    public void updateFile(String fileName, String hash) {
        assert contains(fileName);
        files.get(fileName)[0] = hash;
    }

    public void rmFile(String fileName) {
        assert contains(fileName);
        files.get(fileName)[0] = "";
        files.get(fileName)[1] = "";
    }

    /** Returns a map with file names as keys and the staged versions of those files
     * in the repository as values. Used when committing. */
    public Map<String, String> getStaged() {
        Map<String, String> staged = new HashMap<>();
        for (String file: files.keySet()) {
            staged.put(file, files.get(file)[1]);
        }
        return staged;
    }


    /** Updates the repo version of the file to match the staged version.
     * Used when committing. */
    void commitStaged() {
        // TODO figure out how rm is gonna work.
        for (String file: files.keySet()) {
            if (!files.get(file)[1].isEmpty()) {
                files.get(file)[2] = files.get(file)[1];
            }
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

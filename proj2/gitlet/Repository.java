package gitlet;

import java.io.File;
import java.util.Set;

import static gitlet.Utils.*;

/** Represents a gitlet repository. Contains static methods for performing Gitlet
 * commands as well as shortcuts to the files and directories used by Gitlet to
 * maintain the repository.
 *
 *  @author Eve Stroud
 */
public class Repository {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    /** References directory. */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The index file to keep track of the working directory. */
    public static final File INDEX = join(GITLET_DIR, "index");
    /** HEAD pointer. Keeps track of current position in the commit tree. */
    public static final File HEAD = join(GITLET_DIR, "head");

   public static void init() {
       if (GITLET_DIR.exists()) {
           Utils.message("A Gitlet version-control system already exists in the current directory.");
           System.exit(0);
       }
       GITLET_DIR.mkdir();
       OBJECTS_DIR.mkdir();
       REFS_DIR.mkdir();
       Commit initialCommit = new Commit();
       File initialCommitFile = getFileFromHash(initialCommit.hash);
       writeObject(initialCommitFile, initialCommit);
       String branch = "master";
       newBranch(branch, initialCommit.hash);
       Utils.createFile(HEAD);
       Utils.writeContents(HEAD, branch);
       Index index = new Index();
       Utils.writeObject(INDEX, index);
   }

    /** Create a new branch.
     *
     * @param name Name of the branch to be created.
     * @param commit Hash of the commit to use as the head commit for this branch.
     */
    public static void newBranch(String name, String commit) {
        File branch = join(REFS_DIR, name);
        if (branch.exists()) {
            System.out.println("Branch already exists.");
            System.exit(0);
        }
        Utils.createFile(branch);
        Utils.writeContents(branch, commit);
    }

    /** Add a file to the staging area.
     *
     * @param fileName Name of the file to be added.
     */
    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            Utils.message("File does not exist.");
            System.exit(0);
        }

        String fileContents = Utils.readContentsAsString(file);
        String hash = Utils.sha1(fileContents);
        File blob = getFileFromHash(hash);
        Index index = Utils.readObject(INDEX, Index.class);

        index.addFile(fileName, hash);
        writeContents(blob, fileContents);
        Utils.writeObject(INDEX, index);
    }

    /** Remove a file from the staging area.
     *
     * @param fileName Name of the file to be removed.
     */
    public static void rm(String fileName) {
        File file = join(CWD, fileName);
        Index index = Utils.readObject(INDEX, Index.class);
        if (index.isTracked(fileName)) {
            index.rmFile(fileName);
            if (file.exists()) {
                Utils.restrictedDelete(file);
            }
            Utils.writeObject(INDEX, index);
        } else if (!file.exists()) {
            Utils.message("File does not exist.");
        }
    }

    /** Create a new commit with all the changed files in the staging area.
     *
     * @param commitMessage Message associated with the commit.
     */
    public static void commit(String commitMessage) {
        Index index = Utils.readObject(INDEX, Index.class);
        if (!index.changesStaged()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        String branch = Utils.readContentsAsString(HEAD);
        String prevCommit = getBranch(branch);
        Tree tree = new Tree(index.getStaged());
        writeObject(getFileFromHash(tree.hash), tree);
        Commit commit = new Commit(prevCommit, tree.hash, commitMessage);
        writeObject(getFileFromHash(commit.hash), commit);
        File branchFile = join(REFS_DIR, branch);
        Utils.writeContents(branchFile, commit.hash);
        index.commitStaged();
        Utils.writeObject(INDEX, index);
        updateIndex();
    }

    /** Update the working directory versions of all files. */
    public static void updateIndex() {
        Index index = Utils.readObject(INDEX, Index.class);
        Set<String> filesToUpdate = index.getFiles();
        filesToUpdate.addAll(plainFilenamesIn(CWD));
        for (String fileName : filesToUpdate) {
            File file = join(CWD, fileName);
            if (!file.exists()) {
                index.updateFile(fileName, "");
            } else {
                String fileContents = Utils.readContentsAsString(file);
                String hash = Utils.sha1(fileContents);
                index.updateFile(fileName, hash);
            }
        }
        Utils.writeObject(INDEX, index);
    }

    /** Get the commit hash from the branch associated with branchName. */
    public static String getBranch(String branchName) {
        File branch = Utils.join(REFS_DIR, branchName);
        return readContentsAsString(branch);
    }

    public static void status() {
        Index index = Utils.readObject(INDEX, Index.class);
        index.dump();
    }
}

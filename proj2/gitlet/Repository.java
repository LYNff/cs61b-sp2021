package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** If this directory initialized. */
    public static boolean initFinished = false;
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_STAGING_AREA_DIR = join(GITLET_DIR, "staging-areas");
    public static final File GITLET_COMMITS_DIR = join(GITLET_DIR, "commits");

    public static final File GITLET_BLOBS_DIR = join(GITLET_DIR, "blobs");

    // Track the file by its SHA1 saved in the HEAD directory.
    public static final File GITLET_HEAD_DIR = join(GITLET_DIR, "HEAD");

    /* TODO: fill in the rest of this class. */

    /** Create the .gitlet dictionary.
     * @throws IOException
     */
    public static void setupPersistence() throws IOException {
        File gitlet = new File(".gitlet");
        File stagingAreas = new File("staging-areas");
        File commits = new File("commits");

        if (!gitlet.exists()) {
            gitlet.mkdir();
        }
        if (!stagingAreas.exists()) {
            stagingAreas.mkdir();
        }
        if (!commits.exists()) {
            commits.mkdir();
        }
    }

    // Create the branch pointer.
    private static class Branch {
        String name;
        Commit commit;

        public Branch(String name, Commit commit) {
            this.name = name;
            this.commit = commit;
        }

    }

    // Create the HEAD pointer.
    private static class HEAD {
        Commit commit;
        public HEAD(Commit commit) {
            this.commit = commit;
        }
    }


    public static void makeInit() throws IOException {
        if (initFinished) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        initFinished = true;
        Commit commit = new Commit();
        Branch master = new Branch("master", commit);
        HEAD head = new HEAD(commit);
    }

    public static void commit() {
        // Read from my computer the head commit object and the staging area.

        // Clone the head commit.
        // Modify its message and timestamp according to user input.
        // Use the staging area in order to modify the file tracked by the new commit.

        // Write back any new object made or any modified object read earlier.
    }
}

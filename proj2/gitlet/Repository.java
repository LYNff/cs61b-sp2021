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

    public static void makeInit() throws IOException {
        if (initFinished) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        initFinished = true;
        Commit commit0 = new Commit();
    }
}

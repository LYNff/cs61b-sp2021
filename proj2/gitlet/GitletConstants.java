package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class GitletConstants {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_STAGING_AREA_DIR = join(GITLET_DIR, "index");
    public static final File STAGING_FOR_ADDTION =  join(GITLET_STAGING_AREA_DIR, "addition");
    public static final File STAGING_FOR_REMOVAL = join(GITLET_STAGING_AREA_DIR, "removal");

    public static final File GITLET_COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File GITLET_BLOBS_DIR = join(GITLET_DIR, "blobs");

    public static final File GITLET_HEAD_DIR = join(GITLET_DIR, "refs/heads");

}

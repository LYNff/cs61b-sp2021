package gitlet;

import java.io.File;
import java.util.Stack;
import java.util.Date;

import static gitlet.GitletConstants.*;

public class CommitFromFile {
    // Read commit information from the commit file which head points at.
    public static Commit readFromfile(String sha1) {
        Commit headPointer;
        File f = new File(GITLET_COMMITS_DIR, sha1);

        headPointer = Utils.readObject(f, Commit.class);
        return headPointer;
    }
    // Return the file whose content is the sha1 of the commit that head pointer point at.
    public static File getHeadcommitFile() {
        File branchPath = new File(GITLET_DIR, "HEAD");
        String branchName = Utils.readContentsAsString(branchPath);
        return new File(GITLET_HEAD_DIR, branchName);
    }
    public static Commit headCommit() {
        File head = getHeadcommitFile();
        String sha1 = Utils.readContentsAsString(head);
        File commitpath = new File(GITLET_COMMITS_DIR, sha1);
        return Utils.readObject(commitpath, Commit.class);
    }
    public static Stack<Commit> commitStack() {
        Commit head = headCommit();
        Stack<Commit> list = new Stack<>();
        while (head.getParent() != null) {
            // This add is addLast.
            list.push(head);
            head = readFromfile(head.getParent());
        }
        return list;
    }
}

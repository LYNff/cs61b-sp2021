package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Stack;

import static gitlet.GitletConstants.*;

public class ObjectsFromFile {
    // Read commit information from the commit file with the sha1.
    public static Commit readFromfile(String sha1) {
        Commit commit;
        File f = new File(GITLET_COMMITS_DIR, sha1);

        commit = Utils.readObject(f, Commit.class);
        return commit;
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
        return readFromfile(sha1);
    }

    public static Stack<Commit> commitStack() {
        Commit head = headCommit();
        Stack<Commit> list = new Stack<>();
        while (head.getParent() != null) {
            // This add is addLast.
            list.push(head);
            head = readFromfile(head.getParent());
        }
        list.push(head);
        return list;
    }

    public static boolean commitContains(Commit commit, String fileName) {
        HashMap<String, String> fileSet = commit.getFileset();
        return fileSet.containsKey(fileName);
    }
    public static boolean branchContains(String branchName) {
        File branch = new File(GITLET_HEAD_DIR, branchName);
        return branch.exists();
    }

    // Return the file having contents that Key point at in the commit.
    public static File blob(Commit commit, String name) {
        HashMap<String, String> fileSet = commit.getFileset();
        if (fileSet.containsKey(name)) {
            return new File(GITLET_BLOBS_DIR, fileSet.get(name));
        }
        return null;
    }

    // Verify if the name exists in the staging area for addition.
    public static boolean containsInstage(String fileName) {
        File addStage = new File(STAGING_FOR_ADDTION, fileName);
        return addStage.exists();
    }
    public static void removeFromaddstage(String fileName) throws IOException {
        if (containsInstage(fileName)) {
            File addStage = new File(STAGING_FOR_ADDTION, fileName);
            Files.delete(addStage.toPath());
        }
    }

    public static boolean checkBranch(String branchName) {
        File head = new File(GITLET_DIR, "HEAD");
        String headBranch = Utils.readContentsAsString(head);
        if (headBranch.equals(branchName)) {
            return false;
        }
        Utils.writeContents(head, branchName);
        return true;
    }

    // Verify is the file tracked in this branch.
    public static boolean isTracked(String branchName, String fileName) {
        if (branchName == null) {
            File head = new File(GITLET_DIR, "HEAD");
            branchName = Utils.readContentsAsString(head);
        }
        File branch = new File(GITLET_HEAD_DIR, branchName);
        String headBranch = Utils.readContentsAsString(branch);
        Commit commit = readFromfile(headBranch);
        return containsInstage(fileName) ||  commitContains(commit, fileName);
    }

    // Clear the staging are.
    public static void cleanStage() throws IOException {
        File[] file = STAGING_FOR_ADDTION.listFiles();
        for (File f : file) {
            Files.delete(f.toPath());
        }
    }

    public static void addToremoval(String fileName) throws IOException {
        File remove = new File(STAGING_FOR_REMOVAL, fileName);
        if (!remove.exists()) {
            remove.createNewFile();
        }
        HashMap<String, String> fileSet = headCommit().getFileset();
        String blob = fileSet.get(fileName);
        Utils.writeContents(remove, blob);
    }
}

package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static gitlet.GitletConstants.*;

public class ObjectsFromFile {
    // Read commit information from the commit file with the sha1.
    public static Commit readFromfile(String sha1) {
        Commit commit;
        File f = new File(GITLET_COMMITS_DIR, sha1);
        if (!f.exists()) {
            return null;
        }

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

    public static Stack<Commit> commitStack(Commit commit) {
        Commit head = commit;
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
        return containsInstage(GITLET_HEAD_DIR, branchName);
    }

    public static String headBranchName() {
        File head = new File(GITLET_DIR, "HEAD");
        return Utils.readContentsAsString(head);
    }
    // The branches without headBranch in order.
    public static SortedSet<String> branchNames() {
        SortedSet<String> branchNames = fileSort(GITLET_HEAD_DIR);
        branchNames.remove(headBranchName());
        return branchNames;
    }

    public static Commit branchCommit(String branchName) {
        File branch = new File(GITLET_HEAD_DIR, branchName);
        return readFromfile(Utils.readContentsAsString(branch));
    }

    public static SortedSet<String> stagingAreas(File file) {
        return fileSort(file);
    }

    private static SortedSet<String> fileSort(File dir) {
        SortedSet<String> fileNames = new TreeSet<>();
        File[] files = dir.listFiles(file -> !file.isHidden() && file.isFile());
        if (files != null) {
            for (File f : files) {
                fileNames.add(f.getName());
            }
        }
        return fileNames;
    }
    // Return the file having contents that Key point at in the commit.
    public static File blobCommit(Commit commit, String name) {
        HashMap<String, String> fileSet = commit.getFileset();
        if (fileSet.containsKey(name)) {
            return new File(GITLET_BLOBS_DIR, fileSet.get(name));
        }
        return null;
    }
    public static File blobStage(String name) {
        File stage = new File(STAGING_FOR_ADDTION, name);
        String blobFile = Utils.readContentsAsString(stage);

        return new File(GITLET_BLOBS_DIR, blobFile);
    }

    // Verify if the name exists in the staging area for addition.
    public static boolean containsInstage(File stage, String fileName) {
        File addStage = new File(stage, fileName);
        return addStage.exists();
    }

    public static void removeFromstage(File file, String fileName) throws IOException {
        if (containsInstage(file, fileName)) {
            File addStage = new File(file, fileName);
            Files.delete(addStage.toPath());
        }
    }

    // Verify if the branch wanted to check is identical to the current branch.
    public static boolean checkBranch(String branchName) {
        File head = new File(GITLET_DIR, "HEAD");
        String headBranch = Utils.readContentsAsString(head);
        return !headBranch.equals(branchName);
    }

    // Verify is the file tracked in this branch.
    public static boolean isTracked(String branchName, String fileName) {
        File branch = new File(GITLET_HEAD_DIR, branchName);
        String headBranch = Utils.readContentsAsString(branch);
        Commit commit = readFromfile(headBranch);
        return containsInstage(STAGING_FOR_ADDTION, fileName) ||  commitContains(commit, fileName);
    }

    // Delete the files in the working directory that aren't tracked by the branch.
    public static void removeUntracked(String branchName) {
        File[] afterChange = CWD.listFiles(file -> !file.isHidden() && file.isFile());
        if (afterChange != null) {
            for (File file : afterChange) {
                if (!isTracked(branchName, file.getName())) {
                    Utils.restrictedDelete(file.getName());
                }
            }
        }
    }

    // Verify is the file in the working directory which is tracked changed.
    public static boolean isfileChanged(File blob, String fileName) {
        String content = Utils.readContentsAsString(blob);

        File file = new File(CWD, fileName);
        String text = Utils.readContentsAsString(file);
        return !content.equals(text);
    }

    public static SortedSet<String> notStaged() {
        SortedSet<String> notStaged = new TreeSet<>();
        // Tracked in the current commit, changed in the working, but not staged.
        File[] files = CWD.listFiles(file -> !file.isHidden() && file.isFile());
        if (files != null) {
            for (File f : files) {
                if (commitContains(headCommit(), f.getName())) {
                    // Changed but not staged.
                    boolean case1 = isfileChanged(blobCommit(headCommit(), f.getName()), f.getName()) && !containsInstage(STAGING_FOR_ADDTION, f.getName());
                    // Staged for addition, but with different contents than in the working directory.
                    boolean case2 = containsInstage(STAGING_FOR_ADDTION, f.getName()) && isfileChanged(blobStage(f.getName()), f.getName());

                    if (case1 || case2) {
                        String s = f.getName() + " (modified)";
                        notStaged.add(s);
                    }
                }
            }
        }
        // Staged for addition, but deleted in the working directory.
        File[] filesInstage = STAGING_FOR_ADDTION.listFiles(file -> !file.isHidden() && file.isFile());
        if (filesInstage != null) {
            for (File f : filesInstage) {
                File fileInworkspace = new File(CWD, f.getName());
                if (!fileInworkspace.exists()) {
                    String s = f.getName() + " (deleted)";
                    notStaged.add(s);
                }
            }
        }
        // Not staged for removal, but tracked in the current commit and delete from the working directory.
        Set<String> fileSet = headCommit().getFileset().keySet();
        for (String fileName : fileSet) {
            File fileInworkspace = new File(CWD, fileName);
            if (!fileInworkspace.exists() && !containsInstage(STAGING_FOR_REMOVAL, fileName)) {
                String s = fileName + " (deleted)";
                notStaged.add(s);
            }
        }
        return notStaged;
    }

    // Clear the staging are.
    public static void cleanStage() throws IOException {
        File[] files = STAGING_FOR_ADDTION.listFiles(file -> !file.isHidden() && file.isFile());
        if (files != null) {
            for (File f : files) {
                Files.delete(f.toPath());
            }
        }
    }
    public static void addTostage(File file, String fileName) throws IOException {
        File added = new File(file, fileName);
        if (!added.exists()) {
            added.createNewFile();
        }
        HashMap<String, String> fileSet = headCommit().getFileset();
        String blob = fileSet.get(fileName);
        Utils.writeContents(added, blob);
    }
}

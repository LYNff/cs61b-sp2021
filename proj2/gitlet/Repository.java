package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.GitletConstants.*;
import static gitlet.ObjectsFromFile.*;
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

    /* TODO: fill in the rest of this class. */

    /** Create the .gitlet dictionary.
     * @throws IOException
     */

    // Create the branch pointer.
    private static class Branch {
        String name;
        Commit commit;

        public Branch(String name, Commit commit) {
            this.name = name;
            this.commit = commit;
        }
    }

    /* TODO: Distinguish somehow between hashes for commits and hashes for blobs. */

    public static void makeInit() throws IOException {
        /**
         *  TODO: Create a new Gitlet version-control system in the current directory./
         *  TODO: Start with a commit that contains no files and has the commit message./
         *  TODO: Create a single branch(master) points to the commit./
         *  TODO: something if there is already a system in the current directory./
         *
         *  .gitlet
         *      - commits/
         *      - blobs/
         *      - refs/
         *      - index/ ------ staging area
         *      - HEAD
         */

        // Step1
        File gitlet = new File(CWD, ".gitlet");
        // Error exception
        if (gitlet.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        gitlet.mkdir();

        // Step2
        Commit commit = new Commit();
        // TODO: how to use the object of the commit to create a sha-1 to represent this commit.
        String sha1 = Utils.sha1(serialize(commit));
        commit.setName(sha1);
        // Use sha-1 to represent the commit and save it in the commits directory.
        File commits = new File(GITLET_DIR, "commits");
        commits.mkdir();
        File f = new File(GITLET_COMMITS_DIR, sha1);
        f.createNewFile();
        Utils.writeObject(f, commit);

        // Step3
        Branch branch = new Branch("master", commit);
        File head = new File(GITLET_DIR, "HEAD");
        head.createNewFile();
        Utils.writeContents(head, branch.name);
        // Create the head directory for this system.
        File heads = join(GITLET_DIR, "refs", "heads");
        heads.mkdirs();
        File master = new File(GITLET_HEAD_DIR, branch.name);
        master.createNewFile();
        Utils.writeContents(master, sha1);

        // Create the staging area for this system.
        File index = new File(GITLET_DIR, "index");
        index.mkdir();
    }

    public static void add(File f) throws IOException {
        /**
         *  TODO: Create a staging area for addition directory./
         *  TODO: Verify if the file is identical to the file in the current commit./
         *  TODO: remove the file from the staging area if exits while identical./
         *  TODO: If not add it to the staging area./
         *  TODO: If the file in the removal area, change it from removal to addition./
         *  TODO: something If the file does not exist./
         *
         */

        // Step1
        File addtion = new File(GITLET_STAGING_AREA_DIR, "addition");
        if (!addtion.exists()) {
            addtion.mkdir();
        }
        File blobarea = new File(GITLET_DIR, "blobs");
        if (!blobarea.exists()) {
            blobarea.mkdir();
        }

        // Failure cases
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // Step2
        // Use HEAD pointer.
        File c = getHeadcommitFile();
        Commit head = ObjectsFromFile.readFromfile(Utils.readContentsAsString(c));
        HashMap<String, String> fileSet = head.getFileset();

        // Create a file by the name of the f to add.
        File addStage = new File(STAGING_FOR_ADDTION, f.getName());

        // The blob of the file wants to add.
        String text = Utils.readContentsAsString(f);
        String blob2 = Utils.sha1(text);

        // f exists in the current commit.
        if (fileSet != null && fileSet.containsKey(f.getName())) {
            // The blob of the file in the current commit.
            String blob1 = fileSet.get(f.getName());

            // If SHA-1 of the blob is identical, then the two file is identical.
            if (blob1.equals(blob2)) {
                // If the file exists in the staging area, remove it.
                if (addStage.exists()) {
                    Files.delete(addStage.toPath());
                }
                System.exit(0);
            }
        }
        // Add the file to the staging ara.
        addStage.createNewFile();
        Utils.writeContents(addStage, blob2);

        // Add the content of the file to the blobs.
        File blobToadd = new File(GITLET_BLOBS_DIR, blob2);
        blobToadd.createNewFile();
        Utils.writeContents(blobToadd, text);

        // If in the removal area, remove it from the area.
        File removestage = new File(STAGING_FOR_REMOVAL, f.getName());
        if (removestage.exists()) {
            Files.delete(removestage.toPath());
        }
    }


    public static void commit(String message) throws IOException {
        /**
         * TODO: Clone the snapshot from its parent(e.g. head commit)./
         * TODO: Update the contents of files it is tracking that have been staged for addition./
         * TODO: Save and start tracking any files that were staged for addition but weren't tracked by its parent./
         * TODO: Change the head pointer points to the new commit./
         * TODO: Clear the staging area after a commit./
         * TODO: If no files have been staged./
         */
        // Read from my computer the head commit object and the staging area.
        File c = getHeadcommitFile();
        Commit head = ObjectsFromFile.readFromfile(Utils.readContentsAsString(c));
        HashMap<String, String> fileSet = head.getFileset();

        File addStage = new File(STAGING_FOR_ADDTION.toString());
        File[] files = addStage.listFiles();
        // Failure case
        if (files.length == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        // Clone the head commit.
        // Modify its message and timestamp according to user input.
        Commit newCommit = Commit.cloneCommit(head, message);
        newCommit.setParent(head.getName());
        // Use the staging area in order to modify the file tracked by the new commit.
        for (File file : files) {
            // Put the file in the stage area into Commit.
            fileSet.put(file.getName(), Utils.readContentsAsString(file));
            // Delete the file after commit.
            Files.delete(file.toPath());
        }
        // Save the new commit node to the commit tree.
        String commitSha1 = Utils.sha1(serialize(newCommit));
        newCommit.setName(commitSha1);

        File commitTosave = new File(GITLET_COMMITS_DIR, commitSha1);
        commitTosave.createNewFile();
        Utils.writeObject(commitTosave, newCommit);
        // Change the head pointer.
        File headpointer = getHeadcommitFile();
        Utils.writeContents(headpointer, commitSha1);

        // Write back any new object made or any modified object read earlier.
    }
    public static void rm(String fileName) throws IOException {
        // If the file is currently staged for addition.
        if (containsInstage(STAGING_FOR_ADDTION, fileName)) {
            removeFromaddstage(fileName);
        }
        // If the file is tracked in the current commit.
        else if (commitContains(headCommit(), fileName)) {
            // Stage it for removal and remove the file from the working directory.
            File removal = new File(GITLET_STAGING_AREA_DIR, "removal");
            if (!removal.exists()) {
                removal.mkdir();
            }
            addToremoval(fileName);
            File filetoremove = new File(CWD, fileName);
            if (filetoremove.exists()) {
                restrictedDelete(fileName);
            }
        }
        // Failure cases
        else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    public static void log() {
        /**
         *  TODO: Display information about each commit backwards along the commit tree until the initial commit./
         *  TODO: Follow the first parent commit links./
         *  TODO: Ignore any second parents found in merge commits./
         */
        Stack<Commit> stack = commitStack();
        StringBuilder texts = new StringBuilder();
        for (Commit commit : stack) {
            texts.append(Commit.contentsForlog(commit));
        }
        System.out.print(texts);
    }

    public static void globalLog() {
        File[] files = GITLET_COMMITS_DIR.listFiles();
        StringBuilder texts = new StringBuilder();
        for (File file : files) {
            Commit commit = readFromfile(Utils.readContentsAsString(file));
            texts.append(Commit.contentsForlog(commit));
        }
        System.out.print(texts);
    }

    public static void find(String message) {
        File[] files = GITLET_COMMITS_DIR.listFiles();
        boolean hasCommit = false;
        for (File file : files) {
            Commit commit = readFromfile(Utils.readContentsAsString(file));
            if (commit.getMessage().equals(message)) {
                hasCommit = true;
                System.out.println(commit.getName());
            }
        }
        // Failure cases.
        if (!hasCommit) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        StringBuilder texts = new StringBuilder();
        texts.append("=== Branches ===\n");
        // *master
        texts.append("*").append(headBranchName()).append("\n");
        SortedSet<String> branches = branchNames();
        for (String branch : branches) {
               texts.append(branch).append("\n");
        }
        texts.append("\n=== Staged Files ===\n");
        SortedSet<String> filesAdd = stagingAreas(STAGING_FOR_ADDTION);
        for (String fileName : filesAdd) {
            texts.append(fileName).append("\n");
        }
        texts.append("\n=== Removed Files ===\n");
        SortedSet<String> filesRemove = stagingAreas(STAGING_FOR_REMOVAL);
        for (String fileName : filesRemove) {
            texts.append(fileName).append("\n");
        }
        texts.append("\n=== Modifivations Not Staged For Commit ===\n");
        SortedSet<String> filesNotstaged = notStaged();
        for (String file : filesNotstaged) {
            texts.append(file).append("\n");
        }
        texts.append("\n=== Untracked Files ===\n");
        File[] workingArea = CWD.listFiles();
        if (workingArea != null) {
            for (File file : workingArea) {
                if (!isTracked(headBranchName(), file.getName())) {
                    texts.append(file.getName()).append("\n");
                }
            }
        }
        System.out.println(texts);
    }
    // Three cases in checkout.
    public static void checkout(Commit commit, String fileName) throws IOException {
        if (!commitContains(commit, fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File blob = blobCommit(commit, fileName);
        File addTowork = new File(CWD, fileName);
        if (!addTowork.exists()) {
            addTowork.createNewFile();
        }
        String text = Utils.readContentsAsString(blob);
        Utils.writeContents(addTowork, text);
    }
    public static void checkout(String branchName) throws IOException {
        // Check this branch to the current branch.
        boolean isChecked = checkBranch(branchName);
        if (!isChecked) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        // If there is an untracked file in the current branch.
        File[] filesCWD = CWD.listFiles();
        if (filesCWD != null) {
            for (File file : filesCWD) {
                if (!isTracked(headBranchName(), file.getName())) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        File heads = new File(GITLET_HEAD_DIR, branchName);
        String sha1 = Utils.readContentsAsString(heads);

        Commit commit = readFromfile(sha1);
        HashMap<String, String> fileSet = commit.getFileset();
        Set<String> keySet = fileSet.keySet();
        for (String fileName : keySet) {
            checkout(commit, fileName);
        }

        File head = new File(GITLET_DIR, "HEAD");
        Utils.writeContents(head, branchName);
        // TODO: Delete any files tracked in the current branch but not present in the checked-out branch./
        File[] afterChange = CWD.listFiles();
        if (afterChange != null) {
            for (File file : afterChange) {
                if (!isTracked(branchName, file.getName())) {
                    restrictedDelete(file.getName());
                }
            }
        }
        // Clear the staging area.
        cleanStage();
    }

    public static void branch(String branchName) throws IOException {
        Branch newbranch = new Branch(branchName, headCommit());

        File newbranchFile = new File(GITLET_HEAD_DIR, branchName);
        // Failure cases.
        if (newbranchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        newbranchFile.createNewFile();
        Commit headcommit = headCommit();
        Utils.writeContents(newbranchFile, headcommit.getName());
    }


}

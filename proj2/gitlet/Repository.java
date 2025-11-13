package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static gitlet.GitletConstants.*;
import static gitlet.ObjectsFromFile.*;
import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 */
public class Repository {
    /*
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */


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


    public static void makeInit() throws IOException {
        /*
           .gitlet
               - commits/
               - blobs/
               - refs/
               - index/ ------ staging area
               - HEAD
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

        // Create the add stage.
        File addtion = new File(GITLET_STAGING_AREA_DIR, "addition");
        addtion.mkdir();
        // Create the removal stage.
        File removal = new File(GITLET_STAGING_AREA_DIR, "removal");
        removal.mkdir();

        // Create the blob stage.
        File blobarea = new File(GITLET_DIR, "blobs");
        blobarea.mkdir();
    }

    public static void add(File f) throws IOException {
        // The removal stage.
        File removestage = new File(STAGING_FOR_REMOVAL, f.getName());
        
        // Failure cases
        if (!f.exists() && !removestage.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        // The file will no longer be staged for removal.
        if (!f.exists() && removestage.exists()) {
            String blob = Utils.readContentsAsString(removestage);
            File blobFile = new File(GITLET_BLOBS_DIR, blob);
            String contents = Utils.readContentsAsString(blobFile);

            Files.delete(removestage.toPath());

            File newFile = new File(CWD, f.getName());
            Utils.writeContents(newFile, contents);
            System.exit(0);
        }
        // Step2
        // Use HEAD pointer.
        HashMap<String, String> fileSet = headCommit().getFileset();

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
    }

    public static void commit(String message) throws IOException {
        // Read from my computer the head commit object and the staging area.
        File addStage = new File(STAGING_FOR_ADDTION.toString());
        File removeStage = new File(STAGING_FOR_REMOVAL.toString());
        File[] files = addStage.listFiles(file -> !file.isHidden() && file.isFile());
        File[] filesToremove = removeStage.listFiles(file -> !file.isHidden() && file.isFile());
        // Failure case
        if (files.length == 0 && filesToremove.length == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        // Every commit must have a non-blank message.
        if (Objects.equals(message, "")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        // Clone the head commit.
        // Modify its message and timestamp according to user input.
        Commit newCommit = Commit.cloneCommit(headCommit(), message);
        newCommit.setParent(headCommit().getName());
        HashMap<String, String> fileSet = newCommit.getFileset();

        // Use the staging area in order to modify the file tracked by the new commit.
        for (File file : files) {
            // Put the file in the stage area into Commit.
            fileSet.put(file.getName(), Utils.readContentsAsString(file));
            // Delete the file after commit.
            Files.delete(file.toPath());
        }
        // Untrack the file in the removal stage.
        if (filesToremove != null) {
            for (File file : filesToremove) {
                fileSet.remove(file.getName());
                // Delete the file in removal stage after commit.
                Files.delete(file.toPath());
            }
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
            removeFromstage(STAGING_FOR_ADDTION, fileName);
        } else if (commitContains(headCommit(), fileName)) {
            // Stage it for removal and remove the file from the working directory.
            File removal = new File(GITLET_STAGING_AREA_DIR, "removal");
            if (!removal.exists()) {
                removal.mkdir();
            }
            addToremoval(STAGING_FOR_REMOVAL, fileName);
            File filetoremove = new File(CWD, fileName);
            if (filetoremove.exists()) {
                restrictedDelete(fileName);
            }
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    public static void log() {
        Stack<Commit> stack = commitStack(headCommit());
        StringBuilder texts = new StringBuilder();
        for (Commit commit : stack) {
            texts.append(Commit.contentsForlog(commit));
        }
        System.out.print(texts);
    }

    public static void globalLog() {
        File[] files = GITLET_COMMITS_DIR.listFiles(file -> !file.isHidden() && file.isFile());
        StringBuilder texts = new StringBuilder();
        // Because of the init commit, the files won't be null.
        for (File file : files) {
            Commit commit = readFromfile(file.getName());
            texts.append(Commit.contentsForlog(commit));
        }
        System.out.print(texts);
    }

    public static void find(String message) {
        File[] files = GITLET_COMMITS_DIR.listFiles(file -> !file.isHidden() && file.isFile());
        boolean hasCommit = false;
        for (File file : files) {
            Commit commit = readFromfile(file.getName());
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
        File[] workingArea = CWD.listFiles(file -> !file.isHidden() && file.isFile());
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

        // The new version of the file is not staged.
        if (containsInstage(STAGING_FOR_ADDTION, fileName)) {
            removeFromstage(STAGING_FOR_ADDTION, fileName);
        }
        if (containsInstage(STAGING_FOR_REMOVAL, fileName)) {
            removeFromstage(STAGING_FOR_REMOVAL, fileName);
        }
    }
    public static void checkout(String branchName) throws IOException {
        // Check this branch to the current branch.
        boolean isChecked = checkBranch(branchName);
        if (!isChecked) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        // If there is an untracked file in the current branch.
        File[] filesCWD = CWD.listFiles(file -> !file.isHidden() && file.isFile());
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

        removeUntracked(branchName);
        // Clear the staging area.
        cleanStage();
    }

    public static void branch(String branchName) throws IOException {
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

    public static void rmBranch(String branchName) throws IOException {
        File branch = new File(GITLET_HEAD_DIR, branchName);
        // Failure cases.
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(headBranchName())) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        Files.delete(branch.toPath());
    }

    public static void reset(String commitId) throws IOException {
        // Failure cases.
        File commitFile = new File(GITLET_COMMITS_DIR, commitId);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Commit commit = readFromfile(commitId);

        Set<String> keySet = commit.getFileset().keySet();
        for (String fileName : keySet) {
            checkout(commit, fileName);
        }
        // Move the current branch's head to that commit node.
        File headBranch = new File(GITLET_HEAD_DIR, headBranchName());
        Utils.writeContents(headBranch, commitId);

        removeUntracked(headBranchName());
        // Clean the staging area.
        cleanStage();
    }

    public static void merge(String branchName) throws IOException {
        if (!stagingAreas(STAGING_FOR_ADDTION).isEmpty() || !stagingAreas(STAGING_FOR_REMOVAL).isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!branchContains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(headBranchName())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (branchCommit(branchName).getName().equals(headCommit().getName())) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        File[] files = CWD.listFiles(file -> !file.isHidden() && file.isFile());
        if (files != null) {
            for (File file : files) {
                if (!isTracked(headBranchName(), file.getName())) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        // Find the split point.
        Stack<Commit> headStack = commitStack(headCommit());
        Stack<Commit> branchStack = commitStack(branchCommit(branchName));

        Commit splitPoint = new Commit();
        for (Commit commit : headStack) {
            if (branchStack.contains(commit)) {
                splitPoint = commit;
            }
        }

        // If the split point is the same commit as the given branch.
        if (splitPoint.getName().equals(branchCommit(branchName).getName())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        // If the split point is the current branch.
        if (splitPoint.getName().equals(headCommit().getName())) {
            checkout(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        Set<String> fileSet = new HashSet<>(splitPoint.getFileset().keySet());
        HashMap<String, String> branchSet = branchCommit(branchName).getFileset();
        HashMap<String, String> headSet = headCommit().getFileset();

        fileSet.addAll(headSet.keySet());
        fileSet.addAll(branchSet.keySet());

        // Verify if the conflict happened.
        boolean isConflicted = false;

        // Any files that were present at the fileSet.
        for (String fileName : fileSet) {

            boolean branchEqualsplit = fileEquals(branchCommit(branchName), splitPoint, fileName);
            boolean headEqualsplit = fileEquals(headCommit(), splitPoint, fileName);
            boolean branchEqualhead = fileEquals(branchCommit(branchName), headCommit(), fileName);

            // Any files that have been modified in the given branch since the split point, but not modified in the current branch.
            if (!branchEqualsplit && headEqualsplit) {
                // Any files present at the split point, unmodified in the current branch, and absent in the given branch should be removed.
                if (!branchSet.containsKey(fileName)) {
                    rm(fileName);
                } else {
                    checkout(branchCommit(branchName), fileName);
                    File file = new File(CWD, fileName);
                    add(file);
                }
            }
            // Any files modified in different ways in the current and given branches are in conflict.
            if (!headEqualsplit && !branchEqualsplit && !branchEqualhead) {
                isConflicted = true;
                StringBuilder content = new StringBuilder();
                content.append("<<<<<<< HEAD\n");
                File headFile = new File(GITLET_BLOBS_DIR, headSet.get(fileName));
                String headFilecontent = Utils.readContentsAsString(headFile);
                File branchFile = new File(GITLET_BLOBS_DIR, branchSet.get(fileName));
                String branchFilecontent = Utils.readContentsAsString(branchFile);
                content.append(headFilecontent);
                content.append("=======\n");
                content.append(branchFilecontent);
                content.append(">>>>>>>\n");

                File newBlob = new File(GITLET_BLOBS_DIR, Utils.sha1(content.toString()));
                newBlob.createNewFile();

                // Update the file.
                File file = new File(CWD, fileName);
                add(file);
            }
        }
        // If the merge encounter a conflict.
        if (isConflicted) {
            System.out.println("Encountered a merge conflict.");
        }

        String message = String.format("Merged %s into %s", branchName, headBranchName());
        Commit mergeCommit = Commit.cloneCommit(headCommit(), message);
        // Merge commits differ from other commits: they record as parents both the head of the current branch and the head of the given branch.
        mergeCommit.setParent(headCommit().getName());
        mergeCommit.setMother(branchCommit(branchName).getName());
        mergeCommit.setName(Utils.sha1(serialize(mergeCommit)));

        HashMap<String, String> mergeCommitFiles = mergeCommit.getFileset();
        File[] filesToadd = STAGING_FOR_ADDTION.listFiles();
        if (filesToadd != null) {
            for (File file : filesToadd) {
                mergeCommitFiles.put(file.getName(), Utils.readContentsAsString(file));
                Files.delete(file.toPath());
            }
        }
        mergeCommit.setName(Utils.sha1(serialize(mergeCommit)));
        File commitFile = new File(GITLET_COMMITS_DIR, mergeCommit.getName());
        commitFile.createNewFile();
        Utils.writeObject(commitFile, mergeCommit);

        // Change the head of the current branch, making it point at the merge commit.
        File currentBranch = new File(GITLET_HEAD_DIR, headBranchName());
        Utils.writeContents(currentBranch, mergeCommit.getName());
    }
}

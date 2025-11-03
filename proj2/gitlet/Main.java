package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.GitletConstants.*;
import static gitlet.ObjectsFromFile.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        try {
            if (args.length == 0) {
                System.out.println("Please enter a command.");
                System.exit(0);
            }
            String firstArg = args[0];
            switch (firstArg) {
                case "init":
                    // TODO: handle the `init` command
                    validateNumArgs("init", args, 1);
                    Repository.makeInit();
                    break;
                case "add":
                    // TODO: handle the `add [filename]` command
                    if (args.length == 1) {
                        System.out.println("Please enter a commit message.");
                        System.exit(0);
                    }
                    validateNumArgs("add", args, 2);
                    File f = new File(args[1]);
                    Repository.add(f);
                    break;
                // TODO: FILL THE REST IN
                case "commit":
                    validateNumArgs("commit", args, 2);
                    Repository.commit(args[1]);
                    break;
                case "rm" :
                    validateNumArgs("rm", args, 2);
                    Repository.rm(args[1]);
                case "log":
                    validateNumArgs("rm", args, 1);
                    Repository.log();
                    break;
                case "global-log":
                    validateNumArgs("global-log", args, 1);
                    Repository.globalLog();
                case "find":
                    validateNumArgs("find", args, 2);
                    Repository.find(args[1]);
                case "status":
                    validateNumArgs("status", args, 1);
                    Repository.status();
                case "checkout":
                    if (args.length == 2) {
                        if (!branchContains(args[1])) {
                            System.out.println("No such branch exists.");
                            System.exit(0);
                        }
                        Repository.checkout(args[1]);
                    } else if (args.length == 3) {
                        Repository.checkout(headCommit(), args[2]);
                    } else if (args.length == 4) {
                        Commit commit = readFromfile(args[1]);
                        if (commit == null) {
                            System.out.println("No commit with that id exists.");
                            System.exit(0);
                        }
                        Repository.checkout(commit, args[3]);
                    } else {
                        validateNumArgs("checkout", args, 2);
                    }
                    break;
                case "branch":
                    validateNumArgs("branch", args, 2);
                    Repository.branch(args[1]);
                    break;
                case "rm-branch":
                    validateNumArgs("rm-branch", args, 2);
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

}

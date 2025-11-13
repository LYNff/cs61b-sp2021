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
                    initChecked();
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
                    initChecked();
                    validateNumArgs("commit", args, 2);
                    Repository.commit(args[1]);
                    break;
                case "rm" :
                    initChecked();
                    validateNumArgs("rm", args, 2);
                    Repository.rm(args[1]);
                    break;
                case "log":
                    initChecked();
                    validateNumArgs("rm", args, 1);
                    Repository.log();
                    break;
                case "global-log":
                    initChecked();
                    validateNumArgs("global-log", args, 1);
                    Repository.globalLog();
                    break;
                case "find":
                    initChecked();
                    validateNumArgs("find", args, 2);
                    Repository.find(args[1]);
                    break;
                case "status":
                    initChecked();
                    validateNumArgs("status", args, 1);
                    Repository.status();
                    break;
                case "checkout":
                    initChecked();
                    if (args.length == 2) {
                        if (!branchContains(args[1])) {
                            System.out.println("No such branch exists.");
                            System.exit(0);
                        }
                        Repository.checkout(args[1]);
                    } else if (args.length == 3) {
                        Repository.checkout(headCommit(), args[2]);
                    } else if (args.length == 4) {
                        if (!args[2].equals("--")) {
                            System.out.println("Incorrect operands");
                            System.exit(0);
                        }
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
                    initChecked();
                    validateNumArgs("branch", args, 2);
                    Repository.branch(args[1]);
                    break;
                case "rm-branch":
                    initChecked();
                    validateNumArgs("rm-branch", args, 2);
                    Repository.rmBranch(args[1]);
                    break;
                case "reset":
                    initChecked();
                    validateNumArgs("reset", args, 2);
                    Repository.reset(args[1]);
                    break;
                case "merge":
                    initChecked();
                    validateNumArgs("merge", args, 2);
                    Repository.merge(args[1]);
                    break;
                default:
                    System.out.println("No command with that name exists.");
                    System.exit(0);
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
    public static void initChecked() {
        File init = new File(CWD, ".gitlet");
        if (!init.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
 }
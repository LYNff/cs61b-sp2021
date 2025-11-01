package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
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
            case "log":
                validateNumArgs("rm", args, 1);
                Repository.log();
                break;
        }
    }
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

}

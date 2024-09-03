package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Utils.message("Please enter a command.");
            System.exit(0);
        }

        String command = args[0];
        if (command.equals("init")) {
            validateNumArgs(args, 1, 1);
            Repository.init();
            return;
        } else if (!Repository.GITLET_DIR.exists()) {
            Utils.message("Not in an initialized Gitlet directory.");
            return;
        }

        /* Update the repo on each command */
        Repository.updateIndex();

        switch (command) {
            case "add": {
                // TODO handle invalid files (including '.' ?)
                validateNumArgs(args, 2, 2);
                String fileName = args[1];
                Repository.add(fileName);
                break;
            }
            case "commit":
                validateNumArgs(args, 1, 2);
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    return;
                }
                String commitMessage = args[1];
                Repository.commit(commitMessage);
                break;
            case "rm": {
                validateNumArgs(args, 2, 2);
                String fileName = args[1];
                Repository.rm(fileName);
                break;
            }
            case "log":
                validateNumArgs(args, 1, 1);
                Repository.log();
                break;
            case "global-log":
                validateNumArgs(args, 1, 1);
                break;
            case "find":
                validateNumArgs(args, 1, 1);
                break;
            case "status":
                validateNumArgs(args, 1, 1);
                Repository.status();
                break;
            case "checkout":
                break;
            case "branch":
                break;
            case "rm-branch":
                break;
            case "reset":
                break;
            case "merge":
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }

    public static void validateNumArgs(String[] args, int min, int max) {
        if (args.length < min || args.length > max) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
    }
}

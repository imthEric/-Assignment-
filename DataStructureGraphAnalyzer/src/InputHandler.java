import java.util.Scanner;

/**
 * InputHandler - a small helper class that wraps a single Scanner object
 * and provides safe input methods with validation.
 *
 * Using one shared Scanner avoids the classic "input buffer" problem that
 * happens when several Scanner objects are created on System.in.
 */
public class InputHandler {

    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of text (never null).
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return line;
    }

    /**
     * Reads an integer, re-prompting until the user types a valid number.
     * This prevents InputMismatchException crashes and infinite loops
     * (we always consume the whole line).
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(">> Invalid input: '" + line
                        + "' is not a whole number. Please try again.");
            }
        }
    }

    /**
     * Reads an integer within a given range [min, max], re-prompting
     * until the value is valid.
     */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println(">> Out of range. Please enter a number between "
                    + min + " and " + max + ".");
        }
    }

    /**
     * Closes the underlying scanner / input resource.
     */
    public void close() {
        scanner.close();
    }
}

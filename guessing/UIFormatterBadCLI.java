import java.util.Scanner;

/// Doesn't actually format anything.
/// This formatter just prints out text using the formatted interface.
class UIFormatterBadCLI implements UIFormatterInterface {
	
	public char[] GetInput(String prompt) {
		System.out.print(prompt);
		
		String line = scanner.next();

		return line.toCharArray();
	}
	
	public void Display(String message) {
		System.out.print(message);
	}
	
	static Scanner scanner = new Scanner(System.in);
	
};
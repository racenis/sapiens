import java.util.Scanner;

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
import java.util.Scanner;

/// Fancy formatter, centers a message.
/// The formatter assumes that a 80-character wide console window is used.
class UIFormatterFancyCLI implements UIFormatterInterface {
	
	public char[] GetInput(String prompt) {
		System.out.println(prompt);
		
		String line = scanner.nextLine();
		
		return line.toUpperCase().toCharArray();
	}
	
	public void Display(String message) {
		int width = 80 - message.length() - 2;
		int padding_left = width / 2;
		
		for (int i = 0; i < padding_left; i++) {
			System.out.print(' ');
		}
		
		System.out.print(message);

		System.out.print('\n');
	}
	
	static Scanner scanner = new Scanner(System.in);

};
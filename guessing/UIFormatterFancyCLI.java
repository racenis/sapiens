import java.util.Scanner;

class UIFormatterFancyCLI implements UIFormatterInterface {
	
	public char[] GetInput(String prompt) {
		System.out.println(prompt);
		
		String line = scanner.nextLine();
		
		return line.toUpperCase().toCharArray();
	}
	
	public void Display(String message) {
		int width = 78 - message.length() - 2;
		int padding_left = width / 2;
		
		for (int i = 0; i < padding_left; i++) {
			System.out.print(' ');
		}
		
		System.out.print(message);

		System.out.print('\n');
	}
	
	static Scanner scanner = new Scanner(System.in);

};
import java.util.Scanner;

class UIFormatterPlainCLI implements UIFormatterInterface {
	
	public char[] GetInput(String prompt) {
		System.out.println(prompt);
		
		String line = scanner.nextLine();
		
		
		
		return line.toUpperCase().toCharArray();
	}
	
	public void Display(String message) {
		System.out.println(message);
	}
	
	static Scanner scanner = new Scanner(System.in);
	
};
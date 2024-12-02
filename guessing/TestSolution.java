class FieldMock implements FieldInterface {
	public boolean IsValid(char character) {
		return character != 'P'; // no P allowed!
	}
	
	public boolean Contains(char character) {
		return character != 'P'; // no p!!!
	}
	
	public void Insert(int index, RandomCharacterGeneratorInterface generator) {
		// okay whatever we won't be inserting anything
	}
	
	public char GetCharacter(int index) {
		switch (index) {
			case 0: return 'A';
			case 1: return 'F';
			case 2: return '0';
			case 3: return '9';
			default: return 'E'; // E is allowed. No P. but E.
		}
	}
	
	public void SetCharacter(int index, char character) {
		// whatever, just ignore
	}
	
	public int GetSize() {
		return 4;
	}
	
	public String toString() {
		return "AF09";
	}
};

class PrinterMock extends UIInterface {
	public PrinterMock() {
		super(null);
	}
	
	/*public enum Text {
		HELP_MESSAGE,
		PROMPT_SOLUTION,
		PROGRESS_REPORT,
		SOLUTION_CORRECT,
		ABSOLUTE_FAILURE,
		INVALID_SYMBOLS,
		INVALID_LENGTH,
		CORRECT_SYMBOLS,
		CORRECT_PLACES,
		SHOW_CORRECT,
	};*/
	
	public char[] GetInput(Text prompt) {
		return null; // won't need this
	}
	
	public void PrintMessage(Text message, String[] params) {
		System.out.println("accesising");
		
		switch (message) {
			case INVALID_SYMBOLS:
				invalid_symbol_printed = true;
				break;
			case INVALID_LENGTH:
				invalid_length_printed = true;
				break;
			case CORRECT_SYMBOLS:
				exist_printed = true;
				exist[0] = params[0];
				exist[1] = params[1];
				break;
			case CORRECT_PLACES:
				correct_printed = true;
				correct[0] = params[0];
				correct[1] = params[1];
				break;
		}
	}

	protected String FormatString(String string, String[] params) {
		return "";
	}
	
	protected String GetStringFromText(Text text) {
		return "invalid";
	}
	
	public String[] exist = {"", ""};
	public String[] correct = {"", ""};
	public boolean exist_printed = false;
	public boolean correct_printed = false;
	public boolean invalid_symbol_printed = false;
	public boolean invalid_length_printed = false;
};

public class TestSolution {
	class CharMock implements RandomCharacterGeneratorInterface {
		public char GetCharacter() {
			return chars[index++];
		}
		
		public void SetChars(char[] chars) {
			this.chars = chars;
			this.index = 0;
		}
		
		private static char[] chars = {};
		private static int index = 0;
	};
	
	
	
	public static void main(String args[]) {
		
		
		char[] characters = {'A', 'F', '0', '9'};
		
		//CharMock generator = new CharMock();
		//generator.SetChars(characters);
		
		Solution solution = new Solution(characters);
		
		//FieldMock field = new FieldMock();
		FieldMock field = new FieldMock();
		PrinterMock printer = new PrinterMock();
		
		//for (int i = 0; i < characters.length; i++) {
		//	solution.Insert(i, generator);
		//}
		
		solution.Validate(field, printer);
		
		// make solution
		// test stuff
		
		// use UI mock
	}
}
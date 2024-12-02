
class FormatterMock implements UIFormatterInterface {
	public char[] GetInput(String prompt) {
		last_display = prompt;
		System.out.println("FormatterMock: " + prompt);
		return new char[]{'F'};
	}
	
	public void Display(String message) {
		System.out.println("FormatterMock: " + message);
		last_display = message;
	}
	
	public String last_display = "invalid";
};

// it probably would have been a better design to implement the 
// GetStringFromText() method in a seperate class and then inject it into the
// UIInterface, since then we could test it directly, but this way probably
// works fine too
class UIDelanged extends UIInterface {
	public UIDelanged(UIFormatterInterface formatter) {
		super(formatter);
	}
	
	protected String GetStringFromText(Text text) {
		switch (text) {
			case HELP_MESSAGE: 		return "HELP_MESSAGE";
			case PROMPT_SOLUTION:	return "PROMPT_SOLUTION";
			case PROGRESS_REPORT:	return "PROGRESS_REPORT%0:%1";
			case SOLUTION_CORRECT:	return "SOLUTION_CORRECT%0%1%2";
			case ABSOLUTE_FAILURE:	return "ABSOLUTE_FAILURE";
			case INVALID_SYMBOLS:	return "INVALID_SYMBOLS";
			case INVALID_LENGTH:	return "INVALID_LENGTH";
			case CORRECT_SYMBOLS:	return "CORRECT_SYMBOLS%0:%1";
			case CORRECT_PLACES:	return "CORRECT_PLACES%0:%1";
			case SHOW_CORRECT:		return "SHOW_CORRECT%0";
		}
		assert(false);
		return "invalid";
	}
};

public class TestUI {
	public static void main(String args[]) {
		FormatterMock formatted = new FormatterMock();
		
		UIInterface delanged = new UIDelanged(formatted);
		
		// let's start with acking for some inputs
		char[] inpu = delanged.GetInput(UIInterface.Text.PROMPT_SOLUTION);
		
		assert(inpu.length == 1);
		assert(inpu[0] == 'F');
		assert(formatted.last_display.equals("PROMPT_SOLUTION"));
		
		
		
		
		// subclass UIInterface
		// override some strings
		// use storage formatter mock
		// check what prints!!
	}
}

class MainClass {

	private static FieldInterface MakeField(boolean hexadecimal, int size) {
		if (hexadecimal) {
			return new FieldHexadecimal(size);
		} else {
			return new FieldDecimal(size);
		}
	}
	
	private static RandomCharacterGeneratorInterface MakeFieldGenerator(boolean hexadecimal) {
		if (hexadecimal) {
			char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
			return new RandomCharacterFromSet(characters);
		} else {
			char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7',
			                     '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
			return new RandomCharacterFromSet(characters);
		}
	}
	
	private static void PopulateField(FieldInterface field, RandomCharacterGeneratorInterface generator) {
		for (int i = 0; i < field.GetSize(); i++) {
			field.Insert(i, generator);
		}
	}
	
	

	public static void main(String args[]) {
		// setting up default settings
		boolean fixed = false;
		char fixed_char = '0';
		
		boolean fancy = false;
		boolean alt_language = false;
		boolean hexadecimal = false;
		boolean allow_cheat = false;
		
		int field_size = 6;
		int allowed_guesses = 9;
		
		// reading in settings from CLI
		
		
		// setting up the game
		
		
		
		// TODO: initialize the field
		
		UIFormatterInterface formatter = new UIFormatterPlainCLI();
		UIInterface ui = new UIInterface(formatter);
		
		FieldInterface field = MakeField(hexadecimal, field_size);
		RandomCharacterGeneratorInterface generator = MakeFieldGenerator(hexadecimal);
		
		PopulateField(field, generator);
		
		for (int current_guess = 0; current_guess < allowed_guesses; current_guess++) {
			int one_based_guess_number = current_guess + 1;
			String[] params = {Integer.toString(one_based_guess_number),
			                   Integer.toString(allowed_guesses)};
			ui.PrintMessage(UIInterface.Text.PROGRESS_REPORT, params);
			
			Solution solution = new Solution(ui.GetInput(UIInterface.Text.PROMPT_SOLUTION));
			
			if (solution.Validate(field, ui)) {
				ui.PrintMessage(UIInterface.Text.SOLUTION_CORRECT, null);
				return;
			}
			
			if (allow_cheat) {
				String[] solution_strs = {field.toString()};
				ui.PrintMessage(UIInterface.Text.SHOW_CORRECT, solution_strs);
			}
		}
		
		ui.PrintMessage(UIInterface.Text.ABSOLUTE_FAILURE, null);

		String[] solution = {field.toString()};
		ui.PrintMessage(UIInterface.Text.SHOW_CORRECT, solution);
	}
}


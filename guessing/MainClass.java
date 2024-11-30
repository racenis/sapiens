/*

+------------------------------------------------------------------------------+
|                                                                              |
|                                    ideas                                     |
|                                                                              |
+------------------------------------------------------------------------------+

Use a more functional programming style
- code very imperative style for implementing algorithms
- more functional style could be more readable

Implement other counting systems
- currently we only have decimal and hexadecimal

Add dictionary generator
- instead of picking a random character, it would instead pick a random word
  from a provided dictionary and then sequentially give otu letters from it
- we could basically make a wordle

Replace CLI with GUI
- java already has swing UI, which might be useful
- UI class was designed with a simple future GUI in mind

Replace CLI UI with networked UI
- constructor opens socket
- provide REST API through socket
- structure inputs and outputs as JSON
- would allow commercializing as Worlde As A Service (WAAS)

Characters as a class
- characters are currently using builtin char type
- maybe we want pictograms? non-unicode characters?
- put them in class with a isEqual() method


*/

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
			char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7',
			                     '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
			return new RandomCharacterFromSet(characters);
		} else {
			char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
			
			return new RandomCharacterFromSet(characters);
		}
	}
	
	private static void PopulateField(FieldInterface field, RandomCharacterGeneratorInterface generator) {
		for (int i = 0; i < field.GetSize(); i++) {
			field.Insert(i, generator);
		}
	}
	
	private static UIInterface MakeUI(UIFormatterInterface formatter, boolean alt, boolean bad) {
		if (bad) {
			return new UIBadInterface(formatter);
		} else if (alt) {
			return new UIAltInterface(formatter);
		} else {
			return new UIInterface(formatter);
		}
	}
	
	
	
	

	public static void main(String args[]) {
		
		// setting up default settings
		
		boolean fixed = false;
		char fixed_char = '0';
		
		boolean fancy = false;
		boolean alt_language = false;
		boolean bad_language = true;
		boolean hexadecimal = false;
		boolean allow_cheat = true;
		
		int field_size = 4;
		int allowed_guesses = 8;
		
		UIFormatterInterface formatter = new UIFormatterBadCLI();
		
		// reading in settings from CLI
		
		for (String str : args) switch(str) {
			case "--help":
				formatter.Display("Usage: java MainClass [params...]\n" +
				                  "\t-fancy\tmakes output fancy\n" +
				                  "\t-alt\talternative language\n" +
				                  "\t-hex\thexadecimal numbers\n" +
				                  "\t-nochet\tdisables cheating\n" +
				                  "\t-d[n]\tsets digits to n digits\n" +
				                  "\t-a[n]\tsets allowed guesses to n guesses\n" +
								  "\t-good\tmakes the game actally good\n"+
								  "Example: java MainClass -good -hex -nochet -d2 -a16\n");
				return;
			case "-fancy":
				fancy = true;
				formatter = new UIFormatterFancyCLI();
				break;
			case "-alt":
				alt_language = true;
				break;
			case "-hex":
				hexadecimal = true;
				break;
			case "-nochet":
				allow_cheat = false;
				break;	
			case "-good":
				if (!fancy) formatter = new UIFormatterPlainCLI();
				bad_language = false;
				break;
			default:
				if (str.startsWith("-d")) {
					field_size = Integer.parseInt(str.substring("-d".length()));
				} else if (str.startsWith("-a")) {
					allowed_guesses = Integer.parseInt(str.substring("-a".length()));
				} else {
					formatter.Display("Unknown parameter: " + str
					                  + "\nUse --help flag to get help.");
					return;
				}
		}
		
		// setting up the game
		
		UIInterface ui = MakeUI(formatter, alt_language, bad_language);
		
		FieldInterface field = MakeField(hexadecimal, field_size);
		RandomCharacterGeneratorInterface generator = MakeFieldGenerator(hexadecimal);
		
		PopulateField(field, generator);
		
		// starting the game
		
		for (int current_guess = 0; current_guess < allowed_guesses; current_guess++) {
			int one_based_guess_number = current_guess + 1;
			String[] params = {Integer.toString(one_based_guess_number),
			                   Integer.toString(allowed_guesses)};
			ui.PrintMessage(UIInterface.Text.PROGRESS_REPORT, params);
			
			if (allow_cheat) {
				String[] solution_strs = {field.toString()};
				ui.PrintMessage(UIInterface.Text.SHOW_CORRECT, solution_strs);
			}
			
			Solution solution = new Solution(ui.GetInput(UIInterface.Text.PROMPT_SOLUTION));
			
			if (solution.Validate(field, ui)) {
				ui.PrintMessage(UIInterface.Text.SOLUTION_CORRECT, null);
				return;
			}
		}
		
		ui.PrintMessage(UIInterface.Text.ABSOLUTE_FAILURE, null);

		String[] solution = {field.toString()};
		ui.PrintMessage(UIInterface.Text.SHOW_CORRECT, solution);
	}
}


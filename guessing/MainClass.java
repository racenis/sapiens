/*

+------------------------------------------------------------------------------+
|                                                                              |
|                                    notes                                     |
|                                                                              |
+------------------------------------------------------------------------------+

Business case: who knows? definitely not very obvious.

User: someone who likes boring guessing games.

I feel like a GUI would work better than a CLI, considering the type of person
who would probably be intereseted in this kind of software.

Anyway, you can implement a GUI by subclassing the UIInterface class and
replacing the relevant methods. I think you should be able to get it working
without needing to modify any other part of the program code.

The requirements stated that the program needs to generate numbers with digits.
It did not say which base the numbers should be in. I implemented decimal
digits and hexadecimal digits, but more digits could be added by subclassing the
FieldInterface interface.

You could also have the program generate words to essentially make a Wordle
clone. That could be done by implementing the RandomCharacterGeneratorInterface
interface. Guessing words would be easier and probably more fun than guessing 
numbers. It is supposed to be game, is it not?

The requirements also specified the message format. It is very difficult to
understand. I added a bettwe format. By default the program will use the
difficult format, the better one can be toggled by a CLI flag.

This interface system also allows the localization of the program. CLI programs
usually are not localized, but it still would be possible to do so by
subclassing the UIInterface class.

The example in the specification also showed that the CLI should display the
secret number. The game is about guessing the secret number. Showing the secret
number would make it not secret. This behavior can be toggled by a CLI flag.

However hiding the secret number makes it very difficult to guess it,
especially since the number has 4 digits and there are only 8 allowed attempts.
This default behavior can be changed by using a CLI option.

+------------------------------------------------------------------------------+
|                                                                              |
|                                    ideas                                     |
|                                                                              |
+------------------------------------------------------------------------------+

Use a more functional programming style
- code uses very imperative style for implementing algorithms
- more functional style could be more readable

Implement other counting systems
- currently we only have decimal and hexadecimal

Add a dictionary generator
- instead of picking a random character, it would instead pick a random word
  from a provided dictionary and then sequentially give out letters from it
- we could basically make a wordle

Replace CLI with GUI
- java already has swing UI, which might be useful
- UI class was designed with a simple future GUI in mind

Replace CLI UI with networked UI
- constructor opens socket
- provide REST API through socket
- structure inputs and outputs as JSON
- would allow commercializing program as Wordle As A Service (WAAS)

Characters as a class
- characters are currently using builtin char type
- maybe we want pictograms? non-unicode characters?
- put them in class with a isEqual() method

Consider adding a configManager class
- contains ConfigOption class instances
- allows to simplify the logic in the main method
- also allows easy config saving to file and loading

Check Java standard library docs for containers
- use a proper dynamic array implemenation
- re-allocating arrays to re-size them is inefficient
- program is so simple that it is unlikely to be a performance issue
- mostly not good because code is hard to read

+------------------------------------------------------------------------------+
|                                                                              |
|                                implementation                                |
|                                                                              |
+------------------------------------------------------------------------------+

here it is:

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
			
			// print out the number of the guess
			int one_based_guess_number = current_guess + 1;
			String[] params = {Integer.toString(one_based_guess_number),
			                   Integer.toString(allowed_guesses)};
			ui.PrintMessage(UIInterface.Text.PROGRESS_REPORT, params);
			
			// print out the secret number
			if (allow_cheat) {
				String[] solution_strs = {field.toString()};
				ui.PrintMessage(UIInterface.Text.SHOW_CORRECT, solution_strs);
			}
			
			// prompt user for solution
			Solution solution = new Solution(ui.GetInput(UIInterface.Text.PROMPT_SOLUTION));
			
			// check if solution is correct
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


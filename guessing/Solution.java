class Solution {
	public Solution(char[] characters) {
		this.characters = characters;
	}
	
	public boolean Validate(FieldInterface field, UIInterface printer) {
		
		// check if only valid characters
		for (char c : characters) {
			if (!field.IsValid(c)) {
				printer.PrintMessage(UIInterface.Text.INVALID_SYMBOLS, null);
				return false;
			}
		}
		
		// check if correct length
		if (characters.length != field.GetSize()) {
			printer.PrintMessage(UIInterface.Text.INVALID_LENGTH, null);
			return false;
		}
		
		// count out how many correct characters
		int correct_place = 0;
		int correct_symbol = 0;
		boolean any_incorrect = false;
		
		for (int i = 0; i < field.GetSize(); i++) {
			if (field.Contains(characters[i])) {
				correct_symbol++;
			}
			
			if (field.GetCharacter(i) == characters[i]) {
				correct_place++;
			} else {
				any_incorrect = true;
			}
		}
		
		// print out results
		if (any_incorrect || true) {
			String[] symbols = {Integer.toString(correct_symbol),
			                    Integer.toString(field.GetSize())};
			String[] places =  {Integer.toString(correct_place),
			                    Integer.toString(field.GetSize())};
			
			printer.PrintMessage(UIInterface.Text.CORRECT_SYMBOLS, symbols);
			printer.PrintMessage(UIInterface.Text.CORRECT_PLACES, places);
		}
		
		return !any_incorrect;
	}
	
	protected char[] characters;
};
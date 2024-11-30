class UIBadInterface extends UIInterface {
	public UIBadInterface(UIFormatterInterface formatter) {
		super(formatter);
	}
	
	protected String GetStringFromText(Text text) {
		switch (text) {
			case HELP_MESSAGE: 		return "Here's help: http://google.com";
			case PROMPT_SOLUTION:	return "Enter solution: ";
			case PROGRESS_REPORT:	return "Waow! Current guess: %0/%1";
			case SOLUTION_CORRECT:	return "CONGRATULATION! YOU ARE WINNER!";
			case ABSOLUTE_FAILURE:	return "YOUR LOSER!";
			case INVALID_SYMBOLS:	return "You used invalid symbols. Ungood.";
			case INVALID_LENGTH:	return "You inputted invalid length.";
			case CORRECT_SYMBOLS:	return "Symbols that exist:\t%0/%1";
			case CORRECT_PLACES:	return "Correctly placed:\t%0/%1";
			case SHOW_CORRECT:		return "Correct solution:\t%0";
		}
		assert(false);
		return "invalid";
	}
}
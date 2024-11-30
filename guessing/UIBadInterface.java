class UIBadInterface extends UIInterface {
	public UIBadInterface(UIFormatterInterface formatter) {
		super(formatter);
	}
	
	protected String GetStringFromText(Text text) {
		switch (text) {
			case HELP_MESSAGE: 		return "";
			case PROMPT_SOLUTION:	return "Guess:   ";
			case PROGRESS_REPORT:	return "";
			case SOLUTION_CORRECT:	return "";
			case ABSOLUTE_FAILURE:	return "";
			case INVALID_SYMBOLS:	return "";
			case INVALID_LENGTH:	return "";
			case CORRECT_SYMBOLS:	return "Message: M:%0; ";
			case CORRECT_PLACES:	return "P:%0\n\n";
			case SHOW_CORRECT:		return "Secret:  %0 \n";
		}
		assert(false);
		return "invalid";
	}
}
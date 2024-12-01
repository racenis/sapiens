
/// Base class for UI interfaces.
class UIInterface {
	public UIInterface(UIFormatterInterface formatter) {
		this.formatter = formatter;
	}
	
	public enum Text {
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
	};
	
	/// Prompts input from user.
	public char[] GetInput(Text prompt) {
		String string = GetStringFromText(prompt);
		return formatter.GetInput(string);
	}
	
	/// Displays a message to the user.
	/// This message will be formatted using FormatString() method.
	public void PrintMessage(Text message, String[] params) {
		String string = GetStringFromText(message);
		string = FormatString(string, params);
		formatter.Display(string);
	}

	/// Formats a string by inserting in parameters.
	/// @example If passing in "p1: %0, p2: %1" as string and ["a", "b"] as 
	///          params, then "p1: a, p2: b" will be returned.
	/// @param params Either a string array of strings to be inserted into
	///               string, or a null.
	protected String FormatString(String string, String[] params) {
		if (params == null) return string;
		for (int i = 0; i < params.length; i++) {
			string = string.replace("%" + i, params[i]);
		}
		return string;
	}
	
	// NOTE: Some of the strings are grammatically incorrect. Do not fix!
	//       This is intentional.
	protected String GetStringFromText(Text text) {
		switch (text) {
			case HELP_MESSAGE: 		return "Here's help: http://google.com";
			case PROMPT_SOLUTION:	return "Enter solution: ";
			case PROGRESS_REPORT:	return "Waow! Current guess:\t%0/%1";
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
	
	protected UIFormatterInterface formatter;
};
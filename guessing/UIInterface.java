class UIInterface {
	public UIInterface(UIFormatterInterface formatter) {
		this.formatter = formatter;
	}
	
	public enum Text {
		HELP_MESSAGE,
		PROMPT_SOLUTION
	};
	
	/// Prompts input from user.
	public char[] GetInput(Text prompt) {
		String string = GetStringFromText(prompt);
		return formatter.GetInput(string);
	}
	
	/// Displays a message to the user.
	public void PrintMessage(Text message, String[] params) {
		String string = GetStringFromText(message);
		string = FormatString(string, params);
		formatter.Display(string);
	}

	protected String FormatString(String string, String[] params) {
		for (int i = 0; i < params.length; i++) {
			string = string.replace("%" + i + "%", params[i]);
		}
		return string;
	}
	
	protected String GetStringFromText(Text text) {
		switch (text) {
			case HELP_MESSAGE: 		return "Here's help: http://google.com";
			case PROMPT_SOLUTION:	return "Enter solution: ";
		}
		assert(false);
		return "invalid";
	}
	
	protected UIFormatterInterface formatter;
};
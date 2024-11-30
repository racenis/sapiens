class UIAltInterface extends UIInterface {
	public UIAltInterface(UIFormatterInterface formatter) {
		super(formatter);
	}
	
	protected String GetStringFromText(Text text) {
		switch (text) {
			case HELP_MESSAGE: 		return "Paliidziiba: http://google.com";
			case PROMPT_SOLUTION:	return "Ievadiit soluuciju: ";
			case PROGRESS_REPORT:	return "Waow! Mineejumi: %0/%1";
			case SOLUTION_CORRECT:	return "KONGRATULAACIJAS! JUUS ESIET WINNER!";
			case ABSOLUTE_FAILURE:	return "JUUS ESIET LOOSER!";
			case INVALID_SYMBOLS:	return "Jus izmantojiet nepareizu simbolus.";
			case INVALID_LENGTH:	return "Jus ievadijat nepareizu garumu.";
			case CORRECT_SYMBOLS:	return "Eksistejosi simboli:\t%0/%1";
			case CORRECT_PLACES:	return "Pareizi noliktie:\t%0/%1";
			case SHOW_CORRECT:		return "Pareizaa soluucija:\t%0";
		}
		assert(false);
		return "nekorekts";
	}
}
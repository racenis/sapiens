
/// Selects a random alphanumeric character.
class RandomCharacterRandom extends RandomCharacterFromSet {
	public RandomCharacterRandom() {
		super(empty_set);
		
		int decimal_count = '9' - '0';
		int letter_count = 'Z' - 'A';
		
		this.character_set = new char[decimal_count + letter_count];
		
		int pos = 0;
		for (char c = '0'; c < '9'; c++) {
			this.character_set[pos++] = c;
		}
		
		for (char c = 'A'; c < 'Z'; c++) {
			this.character_set[pos++] = c;
		}
	}
	
	private static char[] empty_set = {};
}
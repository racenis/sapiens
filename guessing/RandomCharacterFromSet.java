
import java.util.Random;

/// Selects a random character from a set.
class RandomCharacterFromSet implements RandomCharacterGeneratorInterface {
	public RandomCharacterFromSet(char[] character_set) {
		this.generator = new Random();
		this.character_set = character_set;
	}
	
	public char GetCharacter() {
		assert(character_set.length > 0);
		
		int index = generator.nextInt(character_set.length);
		char character = character_set[index];
		
		if (UseOnlyOnce()) {
			char[] new_set = new char[character_set.length - 1];
			
			int offset = 0;
			for (int i = 0; i < character_set.length - 1; i++) {
				new_set[i - offset] = character_set[i];
				
				if (i == index) offset = 1;
			}
		}
		
		return character;
	}
	
	public boolean UseOnlyOnce() {
		return true;
	}
	
	protected Random generator;
	protected char[] character_set;
}
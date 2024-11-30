
import java.util.Random;

/// Selects a random character from a set.
class RandomCharacterFromSet implements RandomCharacterGeneratorInterface {
	public RandomCharacterFromSet(char[] character_set) {
		this.generator = new Random();
		this.character_set = character_set;
	}
	
	public char GetCharacter() {
		return character_set[generator.nextInt(character_set.length)];
	}
	
	protected Random generator;
	protected char[] character_set;
}
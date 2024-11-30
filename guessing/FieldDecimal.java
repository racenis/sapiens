class FieldDecimal implements FieldInterface {
	
	public FieldDecimal(int size) {
		assert(size > 0);
		
		this.size = size;
		this.characters = new char[size];
	} 
	
	public boolean IsValid(char character) {
		return (character >= '0' && character <= '9')
			|| (character >= 'A' && character <= 'F');
	}
	
	public boolean Contains(char character) {
		for (int i = 0; i < size; i++) {
			if (characters[i] == character) return true;
		}
		return false;
	}
	
	public void Insert(int index, RandomCharacterGeneratorInterface generator) {
		for (int attempts = 0; attempts < 100; attempts++) {
			char new_char = generator.GetCharacter();
			
			if (!IsValid(new_char)) continue;
			
			assert(index < size);
			characters[index] = new_char;
			
			return;
		}
		
		assert(false);
	}
	
	public char GetCharacter(int index) {
		assert(index < size);
		
		return characters[index];
	}
	
	public void SetCharacter(int index, char character) {
		assert(index < size);
		assert(IsValid(character));
		
		characters[index] = character;
	}
	
	public int GetSize() {
		return size;
	}
	
	private int size;
	private char characters[];
};
class CharMock implements RandomCharacterGeneratorInterface {
	public char GetCharacter() {
		return chars[index++];
	}
	
	public void SetChars(char[] chars) {
		this.chars = chars;
		this.index = 0;
	}
	
	private static char[] chars = {};
	private static int index = 0;
};

public class TestField {
	public static void main(String args[]) {
		FieldDecimal field = new FieldDecimal(4);
		
		assert(field.GetSize() == 4);
		
		// let's put some stuff into the field!
		
		char[] characters = {'0', '1', '8', '9'};
		char[] alternative = {'2', '3', '6', '7'};
		char[] invalid = {'A', 'F', 'E', 'C'};
		CharMock generator = new CharMock();
		generator.SetChars(characters);
		
		for (int i = 0; i < characters.length; i++) {
			field.Insert(i, generator);
		}
		
		
		// decimal field should allow decimal characters
		for (char c : characters) assert(field.IsValid(c));
		
		// we just inserted these characters, so it should contain them, no?
		for (char c : characters) assert(field.Contains(c));
		
		// characters should not move between indices
		for (int i = 0; i < characters.length; i++) {
			assert(field.GetCharacter(i) == characters[i]);
		}
		
		// other decimal characters that were not inserted should be valid
		for (char c : alternative) assert(field.IsValid(c));
		
		// but should not be contained in the field
		for (char c : alternative) assert(!field.Contains(c));
	
	
		// decimal should not accept hecadecimal-only characters
		for (char c : invalid) assert(!field.IsValid(c));
		
		// also should not contain them
		for (char c : invalid) assert(!field.Contains(c));
		
		
		
		System.out.println("Done!");
	}
}
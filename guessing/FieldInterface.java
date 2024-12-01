
// Curently we are using the built-in char type to represent characters.
// It might be useful to encapsulate it into some kind of a Character class
// with an abstract interface that allows implementing a virtual compare()
// method, since that would allow the program to use non-discrete characters.

// Probably not needed for CLI program, since for most of the time it will
// accept only ASCII characters and maybe some unicode ones.
// If extending to a GUI program, then it might be useful.

// Maybe allowing the user to draw characters like in mspaint? The compare would
// return true if two drawn characters reach a similarity threshold.

interface FieldInterface {
	
	/// Checks whether a character can be inserted into the field.
	public boolean IsValid(char character);
	
	/// Checks whether the field contains a given character.
	public boolean Contains(char character);
	
	/// Inserts a character in a given position, sampled from a character generator.
	public void Insert(int index, RandomCharacterGeneratorInterface generator);
	
	/// Retrieves a character at a given position.
	public char GetCharacter(int index);
	
	/// Inserts a given character in a given position.
	public void SetCharacter(int index, char character);
	
	/// Returns the size of the field.
	public int GetSize();
	
};

/// Field, but supports hexadecimal numbers.
class FieldHexadecimal extends FieldDecimal {
	public FieldHexadecimal(int size) {
		super(size);
	}
	
	public boolean IsValid(char character) {
		return (character >= '0' && character <= '9')
			|| (character >= 'A' && character <= 'F');
	}
};
class Solution {
	public Solution(char[] characters) {
		this.characters = characters;
	}
	
	public boolean Validate(UIInterface printer) {
		String oo[] = {"ooo"};
		printer.PrintMessage(UIInterface.Text.HELP_MESSAGE, oo);
		
		return true;
	}
	
	protected char[] characters;
};

class MainClass {

	public static void main(String args[]) {
		System.out.println("i am amogus!");
		
		// TODO: initialize the field
		
		UIFormatterInterface formatter = new UIFormatterPlainCLI();
		UIInterface ui = new UIInterface(formatter);
		
		Solution solution = new Solution(ui.GetInput(UIInterface.Text.PROMPT_SOLUTION));
		
	}
}


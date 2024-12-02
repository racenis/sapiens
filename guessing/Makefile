run: compile
	java -ea MainClass

good: compile
	java -ea MainClass -good -hex	

fancy: compile
	java -ea MainClass -good -hex -fancy

test1: compile
	javac TestField.java
	java -ea TestField
	
test2: compile
	javac TestSolution.java
	java -ea TestSolution
	
test3: compile
	javac TestUI.java
	java -ea TestUI
	
compile:	MainClass.class FieldDecimal.class FieldHexadecimal.java \
			RandomCharacterFromSet.class RandomCharacterRandom.class \
			UIInterface.class UIAltInterface.class UIBadInterface.class \
			UIFormatterFancyCLI.class UIFormatterPlainCLI.class \
			UIFormatterBadCLI.class Solution.class
	

FieldDecimal.class: FieldDecimal.java
	javac FieldDecimal.java
FieldHexadecimal.class: FieldHexadecimal.java
	javac FieldHexadecimal.java
	
RandomCharacterFromSet.class: RandomCharacterFromSet.java
	javac RandomCharacterFromSet.java
RandomCharacterRandom.class: RandomCharacterRandom.java
	javac RandomCharacterRandom.java
	
UIInterface.class: UIInterface.java
	javac UIInterface.java
UIAltInterface.class: UIAltInterface.java
	javac UIAltInterface.java
UIBadInterface.class: UIBadInterface.java
	javac UIBadInterface.java
	
UIFormatterFancyCLI.class: UIFormatterFancyCLI.java
	javac UIFormatterFancyCLI.java
UIFormatterPlainCLI.class: UIFormatterPlainCLI.java
	javac UIFormatterPlainCLI.java
UIFormatterBadCLI.class: UIFormatterBadCLI.java
	javac UIFormatterBadCLI.java
	
Solution.class: Solution.java
	javac Solution.java
	

MainClass.class: MainClass.java
	javac MainClass.java
	
clean:
	del MainClass.class
	del FieldDecimal.class
	del FieldHexadecimal.class
	del FieldInterface.class
	del RandomCharacterGeneratorInterface.class
	del RandomCharacterFromSet.class
	del RandomCharacterRandom.class
	del UIInterface.class
	del UIAltInterface.class
	del UIBadInterface.class
	del UIFormatterInterface.class
	del UIFormatterFancyCLI.class
	del UIFormatterPlainCLI.class
	del UIFormatterBadCLI.class
	del Solution.class
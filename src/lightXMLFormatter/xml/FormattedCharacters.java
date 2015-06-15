package lightXMLFormatter.xml;

public class FormattedCharacters extends FormattedItem {
	
	private String characters;
	
	public FormattedCharacters(String chars) {
		this.characters = chars;
	}
	
	public String toString() {
		String s = "";
		
		for (String part : characters.split("\n")) {
			s += part.trim();
			if (!part.trim().equals("")) {
				s += " ";
			}
		}
		
		// Remove trailing whitespace
		return s.trim();
	}
	
	public String getCharacters() {
		return characters;
	}
	
	public void merge(String v) {
		characters += v;
	}
}

package lightXMLFormatter.formatter.policySortedAndIndented;

import lightXMLFormatter.exception.ValidationException;
import lightXMLFormatter.xml.definition.Comment;

public class FormattedTextual implements Comment {

	private String text;
	
	@Override
	public String toString(int ancestryLevel) {
		if (text != null) {
			throw new ValidationException("A comment must have text, even if that text is an empty string.");
		}
		return "<!-- " + text + " -->";
	}
	
	@Override
	public String toString() {
		return toString(0);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String string) {
		this.text = string;
	}

}

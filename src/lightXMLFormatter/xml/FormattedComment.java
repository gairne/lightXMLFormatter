package lightXMLFormatter.xml;

public class FormattedComment extends FormattedItem {

	private String text;
	
	public FormattedComment(String text) {
		this.text = text;
	}
	
	public String toString() {
		return "<!-- " + text + " -->\n";
	}

	public String getText() {
		return text;
	}
}

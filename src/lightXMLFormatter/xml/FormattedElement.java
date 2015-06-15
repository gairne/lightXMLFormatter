package lightXMLFormatter.xml;

import java.util.ArrayList;

public class FormattedElement extends FormattedItem {

	private ArrayList<FormattedAttribute> attrs;
	private FormattedQName tag;
	private ArrayList<FormattedItem> values;
	
	public FormattedElement(FormattedQName tag, ArrayList<FormattedAttribute> attributes) {
		this.tag = tag;
		this.attrs = attributes;
		values = new ArrayList<FormattedItem>();
	}
	
	public FormattedElement(FormattedQName tag) {
		this.tag = tag;
		attrs = new ArrayList<FormattedAttribute>();
		values = new ArrayList<FormattedItem>();
	}
	
	public String toString() {
		String s = "<" + tag.toString();
		for (FormattedAttribute attr : attrs) {
			s += " " + attr.toString();
		}
		if (values.size() > 0) {
			s += ">";
			if (!textualValue()) {
				s += "\n";
			}
			for (FormattedItem item : values) {
				s += item.toString();
			}
			s += "</" + tag.toString() + ">\n";
		}
		else {
			s += " />\n";
		}
		return s;
	}
	
	public boolean hasAttribute(FormattedAttribute a) {
		for (FormattedAttribute attr : attrs) {
			if (attr.equals(a)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean textualValue() {
		for (FormattedItem item : values) {
			if (!(item instanceof FormattedCharacters)) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<FormattedAttribute> getAttributes() {
		return attrs;
	}

	public FormattedQName getTag() {
		return tag;
	}

	public ArrayList<FormattedItem> getValues() {
		return values;
	}

	/**
	 * 
	 * ...
	 * 
	 * Sometimes two or more FormattedCharacters are created for a large body of text and can be merged into one.
	 */
	public void addChildValue(FormattedItem value) {
		if (values.size() > 0) {
			FormattedItem previous = values.get(values.size() - 1);
			
			if (value instanceof FormattedCharacters && previous instanceof FormattedCharacters) {
				((FormattedCharacters) previous).merge(((FormattedCharacters) value).getCharacters());
			}
			else {
				values.add(value);
			}
		}
		else {
			values.add(value);
		}
	}
}

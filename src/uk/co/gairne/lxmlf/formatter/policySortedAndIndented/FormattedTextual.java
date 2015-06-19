package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.HashSet;
import java.util.Set;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Element;
import uk.co.gairne.lxmlf.xml.definition.Namespace;
import uk.co.gairne.lxmlf.xml.definition.Textual;

public class FormattedTextual implements Textual {

	private String text;
	private String type;
	private Element parent;
	
	public FormattedTextual(String text) {
		setText(text);
	}
	
	@Override
	public String toString(int ancestryLevel) {
		if (text == null) {
			throw new ValidationException("Missing text.");
		}
		// Handle multi-line text.
		// Handle single-line text better, make it inline and don't indent.
		return PolicyUtil.cleanWhitespace(text);
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

	public void merge(String string) {
		text += string;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String string) {
		this.type = string;
	}

	@Override
	public Element getParent() {
		return parent;
	}

	@Override
	public void setParent(Element item) {
		parent = item;
	}
	
	@Override
	public Set<Namespace> getNamespaces() {
		Set<Namespace> ns = new HashSet<Namespace>();
		return ns;
	}
}

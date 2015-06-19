package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.HashSet;
import java.util.Set;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Comment;
import uk.co.gairne.lxmlf.xml.definition.Element;
import uk.co.gairne.lxmlf.xml.definition.Namespace;

public class FormattedComment implements Comment {

	private String text;
	private Element parent;
	
	public FormattedComment(String text) {
		setText(text);
	}
	
	@Override
	public String toString(int ancestryLevel) {
		if (text == null) {
			throw new ValidationException("A comment must have text, even if that text is an empty string.");
		}
		// TODO: Handle multi-line comments specially
		if (PolicyUtil.PRESERVE_COMMENT_SPACE) {
			return "<!-- " + text + " -->";
		}
		else {
			return PolicyUtil.indent("<!-- " + PolicyUtil.cleanWhitespace(text) + " -->", ancestryLevel);
		}
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

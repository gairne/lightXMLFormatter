package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.HashSet;
import java.util.Set;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
import uk.co.gairne.lxmlf.xml.definition.Comment;
import uk.co.gairne.lxmlf.xml.definition.Element;
import uk.co.gairne.lxmlf.xml.definition.Namespace;

public class FormattedComment implements Comment {

	private String text;
	
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
			return PolicyUtil.generateIndent(ancestryLevel) + "<!-- " + text + " -->";
		}
		else {
			return PolicyUtil.generateIndent(ancestryLevel) + "<!-- " + PolicyUtil.cleanWhitespace(text) + " -->";
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
	public Set<Namespace> getNamespaces() {
		Set<Namespace> ns = new HashSet<Namespace>();
		return ns;
	}

	@Override
	public boolean valueEquals(Comment comment) {
		if (getText() == null) {
			if (comment.getText() != null) return false;
		}
		else {
			if (!getText().equals(comment.getText())) return false;
		}
		
		if (getNamespaces() == null) {
			if (comment.getNamespaces() != null) return false;
		}
		else {
			if (!getNamespaces().equals(comment.getNamespaces())) return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Comment)) {
			return false;
		}
		
		Comment otherComment = (Comment) other;
		
		return valueEquals(otherComment);
	}
	
	@Override
	public int hashCode() {
		return (getText() != null ? getText().hashCode() : 0) + (getNamespaces() != null ? getNamespaces().hashCode() : 0);
	}
}

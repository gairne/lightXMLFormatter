package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.HashSet;
import java.util.Set;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
import uk.co.gairne.lxmlf.xml.definition.Element;
import uk.co.gairne.lxmlf.xml.definition.Namespace;
import uk.co.gairne.lxmlf.xml.definition.Textual;

public class FormattedTextual implements Textual {

	private String text;
	private String type;
	
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
	public Set<Namespace> getNamespaces() {
		Set<Namespace> ns = new HashSet<Namespace>();
		return ns;
	}

	@Override
	public boolean valueEquals(Textual textual) {
		if (getText() == null) {
			if (textual.getText() != null) return false;
		}
		else {
			if (!getText().trim().equals(textual.getText().trim())) return false;
		}
		
		if (getType() == null) {
			if (textual.getType() != null) return false;
		}
		else {
			if (!getType().equals(textual.getType())) return false;
		}
		
		if (getNamespaces() == null) {
			if (textual.getNamespaces() != null) return false;
		}
		else {
			if (!getNamespaces().equals(textual.getNamespaces())) return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Textual)) {
			return false;
		}
		
		Textual otherTextual = (Textual) other;
		
		return valueEquals(otherTextual);
	}
	
	@Override
	public int hashCode() {
		return (getText() != null ? getText().hashCode() : 0) + (getType() != null ? getType().hashCode() : 0) + (getNamespaces() != null ? getNamespaces().hashCode() : 0);
	}
}

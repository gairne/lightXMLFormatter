package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.formatter.sortable.SortableNamespace;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
import uk.co.gairne.lxmlf.xml.definition.Namespace;

public class FormattedNamespace extends SortableNamespace {

	private String prefix;
	private String uri;
	
	public FormattedNamespace(String prefix, String URI) {
		setPrefix(prefix);
		setURI(URI);
	}
	
	@Override
	public String toString(int ancestryLevel) {
		return toString(ancestryLevel, false);
	}
	
	@Override
	public String toString(int ancestryLevel, boolean isRootDeclaration) {
		if (prefix == null || uri == null) {
			throw new ValidationException("Namespaces must have a prefix (even if an empty string) and a URI.");
		}
		
		if (!isRootDeclaration) {
			return (prefix.equals("") ? "" : PolicyUtil.cleanWhitespace(prefix) + ":");
		}
		
		return "xmlns" + (prefix.equals("") ? "" : ":" + PolicyUtil.cleanWhitespace(prefix)) + "=\"" + uri + "\"";
	}

	@Override
	public String toString() {
		return toString(0);
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public void setPrefix(String string) {
		this.prefix = string;
	}

	@Override
	public String getURI() {
		return uri;
	}

	@Override
	public void setURI(String string) {
		this.uri = string;
	}

	@Override
	public boolean valueEquals(Namespace namespace) {
		if (getPrefix() == null) {
			if (namespace.getPrefix() != null) return false;
		}
		else {
			if (!getPrefix().equals(namespace.getPrefix())) return false;
		}
		
		if (getURI() == null) {
			if (namespace.getURI() != null) return false;
		}
		else {
			if (!getURI().equals(namespace.getURI())) return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Namespace)) {
			return false;
		}
		
		Namespace otherNamespace = (Namespace) other;
		
		return valueEquals(otherNamespace);
	}
	
	@Override
	public int hashCode() {
		return (getPrefix() != null ? getPrefix().hashCode() : 0) + (getURI() != null ? getURI().hashCode() : 0);
	}
}

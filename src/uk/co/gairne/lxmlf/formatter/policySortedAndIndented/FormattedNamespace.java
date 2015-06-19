package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.Comparator;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Namespace;

public class FormattedNamespace implements Comparable<Namespace>, Namespace, Comparator<Namespace> {

	private String prefix;
	private String uri;
	
	@Override
	public int compare(Namespace o1, Namespace o2) {
		return o1.toString().compareTo(o2.toString());
	}

	@Override
	public int compareTo(Namespace o) {
		return this.toString().compareTo(o.toString());
	}
	
	public FormattedNamespace(String prefix, String URI) {
		setPrefix(prefix);
		setURI(URI);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Namespace && getPrefix().equals(((Namespace) other).getPrefix()) && getURI().equals(((Namespace) other).getURI());
	}
	
	@Override
	public int hashCode() {
		return getPrefix().hashCode() + uri.hashCode();
	}
	
	@Override
	public String toString(int ancestryLevel) {
		if (prefix == null || uri == null) {
			throw new ValidationException("Namespaces must have a prefix (even if an empty string) and a URI.");
		}
		return (prefix.equals("") ? "" : PolicyUtil.cleanWhitespace(prefix) + ":");
	}
	
	@Override
	public String toString(int ancestryLevel, boolean isRootDeclaration) {
		if (!isRootDeclaration) {
			return toString(ancestryLevel);
		}
		
		if (prefix == null || uri == null) {
			throw new ValidationException("Namespaces must have a prefix (even if an empty string) and a URI.");
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
}

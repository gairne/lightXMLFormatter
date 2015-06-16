package lightXMLFormatter.formatter.policySortedAndIndented;

import lightXMLFormatter.exception.ValidationException;
import lightXMLFormatter.xml.definition.Namespace;

public class FormattedNamespace implements Namespace {

	private String prefix;
	private String uri;
	
	@Override
	public String toString(int ancestryLevel) {
		if (prefix == null || uri == null) {
			throw new ValidationException("Namespaces must have a prefix (even if an empty string) and a URI.");
		}
		return (prefix.equals("") ? "" : prefix + ":");
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

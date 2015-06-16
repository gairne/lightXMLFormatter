package lightXMLFormatter.formatter.policySortedAndIndented;

import java.util.List;

import lightXMLFormatter.exception.ValidationException;
import lightXMLFormatter.xml.definition.DTD;
import lightXMLFormatter.xml.definition.Document;
import lightXMLFormatter.xml.definition.Element;
import lightXMLFormatter.xml.definition.Namespace;

public class FormattedDocument implements Document {

	private DTD dtd;
	private Element root;
	private String charset;
	private String version;
	
	private boolean completed = false;
	
	@Override
	public String toString(int ancestryLevel) {
		if (version == null) {
			throw new ValidationException("XML documents must have a version.");
		}
		String s = "<?xml version=\"" + version + "\"";
		if (charset != null) {
			s += " encoding='" + charset + "'";
		}
		s += "?>\n";
		return s + (dtd != null ? dtd.toString(ancestryLevel) + "\n" : "") + root.toString(ancestryLevel);
	}
	
	@Override
	public String toString() {
		return toString(0);
	}

	@Override
	public DTD getDTD() {
		return dtd;
	}

	@Override
	public void setDTD(DTD dtd) {
		this.dtd = dtd;
	}

	@Override
	public Element getRoot() {
		return root;
	}

	@Override
	public void setRoot(Element element) {
		this.root = element;
	}

	@Override
	public String getCharset() {
		return charset;
	}

	@Override
	public void setCharset(String string) {
		this.charset = string;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String string) {
		this.version = string;
	}

	@Override
	public boolean isComplete() {
		return completed;
	}

	@Override
	public boolean isValid() {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public List<Namespace> getNamespaces() {
		throw new IllegalStateException("Not implemented");
	}

}

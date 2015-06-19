package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Namespace;
import uk.co.gairne.lxmlf.xml.definition.QName;

public class FormattedQName implements QName {

	private String localPart;
	private Namespace namespace;
	
	@Override
	public String toString(int ancestryLevel) {
		String s = "";
		if (namespace != null) {
			s += namespace.toString(ancestryLevel);
		}
		if (localPart == null) {
			throw new ValidationException("Missing name");
		}
		return s += PolicyUtil.cleanWhitespace(localPart);
	}
	
	@Override
	public String toString() {
		return toString(0);
	}

	@Override
	public String getLocalPart() {
		return localPart;
	}

	@Override
	public void setLocalPart(String localpart) {
		this.localPart = localpart;
	}

	@Override
	public Namespace getNamespace() {
		return namespace;
	}

	@Override
	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}
}

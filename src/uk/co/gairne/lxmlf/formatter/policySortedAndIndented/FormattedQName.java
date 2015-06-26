package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
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

	@Override
	public boolean valueEquals(QName qname) {
		if (getLocalPart() == null) {
			if (qname.getLocalPart() != null) return false;
		}
		else {
			if (!getLocalPart().equals(qname.getLocalPart())) return false;
		}
		
		if (getNamespace() == null) {
			if (qname.getNamespace() != null) return false;
		}
		else {
			if (!getNamespace().equals(qname.getNamespace())) return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof QName)) {
			return false;
		}
		
		QName otherQName = (QName) other;
		
		return valueEquals(otherQName);
	}
	
	@Override
	public int hashCode() {
		return (getLocalPart() != null ? getLocalPart().hashCode() : 0) + (getNamespace() != null ? getNamespace().hashCode() : 0);
	}
}

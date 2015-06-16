package lightXMLFormatter.formatter.policySortedAndIndented;

import lightXMLFormatter.exception.ValidationException;
import lightXMLFormatter.xml.definition.Namespace;
import lightXMLFormatter.xml.definition.QName;

public class FormattedQName implements QName {

	private String localPart;
	private Namespace namespace;
	
	@Override
	public String toString(int ancestryLevel) {
		String s = "";
		if (namespace != null) {
			s += namespace.toString();
		}
		if (localPart == null) {
			throw new ValidationException("Missing name");
		}
		return s += localPart;
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

package lightXMLFormatter.xml;

import javax.xml.namespace.QName;

public class FormattedQName {

	private String localPart;
	private String namespaceURI;
	private String namespace;
	
	public FormattedQName(String namespace, String namespaceURI, String localPart) {
		this.namespace = namespace;
		this.namespaceURI = namespaceURI;
		this.localPart = localPart;
	}
	
	public String toString() {
		return (namespace != "" ? namespace + ":" : "") + localPart;
	}

	public String getLocalPart() {
		return localPart;
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public String getNamespace() {
		return namespace;
	}
	
	public static final FormattedQName fromQName(QName name) {
		return new FormattedQName(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart());
	}
}

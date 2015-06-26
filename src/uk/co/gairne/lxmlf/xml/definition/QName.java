package uk.co.gairne.lxmlf.xml.definition;

public interface QName extends Item {
	public abstract String getLocalPart();
	public abstract void setLocalPart(String localpart);
	public abstract Namespace getNamespace();
	public abstract void setNamespace(Namespace namespace);
	
	public abstract boolean valueEquals(QName qname);
}

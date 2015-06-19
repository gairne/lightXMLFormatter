package uk.co.gairne.lxmlf.xml.definition;

import java.util.Set;

public interface Attribute extends Item {
	public abstract QName getName();
	public abstract void setName(QName qname);
	public abstract String getValue();
	public abstract void setValue(String string);
	public abstract String getType();
	public abstract void setType(String string);
	public abstract Element getParent();
	public abstract void setParent(Element item);
	public abstract Set<Namespace> getNamespaces();
}

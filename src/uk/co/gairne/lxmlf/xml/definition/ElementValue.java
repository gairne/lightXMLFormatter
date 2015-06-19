package uk.co.gairne.lxmlf.xml.definition;

import java.util.Set;

public interface ElementValue extends Item {
	public abstract Element getParent();
	public abstract void setParent(Element item);
	public abstract Set<Namespace> getNamespaces();
}

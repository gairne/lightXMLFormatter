package uk.co.gairne.lxmlf.xml.definition;

public interface Namespace extends Item {
	public abstract String getPrefix();
	public abstract void setPrefix(String string);
	public abstract String getURI();
	public abstract void setURI(String string);
	
	public abstract String toString(int ancestryLevel, boolean isRootDeclaration);
	public abstract boolean valueEquals(Namespace namespace);
}

package uk.co.gairne.lxmlf.xml.definition;

public interface Textual extends ElementValue {
	public abstract String getText();
	public abstract void setText(String string);
	public abstract String getType();
	public abstract void setType(String string);
	
	public abstract boolean valueEquals(Textual textual);
}

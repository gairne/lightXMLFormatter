package uk.co.gairne.lxmlf.xml.definition;

public interface DTD extends Item {
	public abstract String getDTD();
	public abstract void setDTD(String string);
	
	public abstract boolean valueEquals(DTD dtd);
}

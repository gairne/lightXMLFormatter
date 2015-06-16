package lightXMLFormatter.xml.definition;

public interface Attribute extends Item {
	public abstract QName getName();
	public abstract void setName(QName qname);
	public abstract String getValue();
	public abstract void setValue(String string);
	public abstract String getType();
	public abstract void setType(String string);
}

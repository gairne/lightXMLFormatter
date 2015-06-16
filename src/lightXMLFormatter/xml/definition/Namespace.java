package lightXMLFormatter.xml.definition;

public interface Namespace extends Item {
	public abstract String getPrefix();
	public abstract void setPrefix(String string);
	public abstract String getURI();
	public abstract void setURI(String string);
}

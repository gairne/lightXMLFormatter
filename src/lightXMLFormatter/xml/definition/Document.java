package lightXMLFormatter.xml.definition;

import java.util.List;

public interface Document extends Item {
	public abstract DTD getDTD();
	public abstract void setDTD(DTD dtd);
	public abstract Element getRoot();
	public abstract void setRoot(Element element);
	public abstract String getCharset();
	public abstract void setCharset(String string);
	public abstract String getVersion();
	public abstract void setVersion(String string);
	
	public boolean isComplete();
	public boolean isValid();
	public List<Namespace> getNamespaces();
}

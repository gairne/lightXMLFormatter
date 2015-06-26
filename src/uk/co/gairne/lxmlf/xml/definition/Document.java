package uk.co.gairne.lxmlf.xml.definition;

import java.util.List;
import java.util.Set;

public interface Document extends Item {
	public abstract DTD getDTD();
	public abstract void setDTD(DTD dtd);
	public abstract Element getRoot();
	public abstract void setRoot(Element element);
	public abstract String getCharset();
	public abstract void setCharset(String string);
	public abstract String getVersion();
	public abstract void setVersion(String string);
	
	public static enum COMMENT_LOCATION {
		POSTPROLOG_PREDTD, POSTDTD_PREROOT, POSTROOT, INSIDE_ELEMENT;
	}
	
	public abstract void addComment(Comment comment, COMMENT_LOCATION location);
	public abstract List<Comment> getComments(COMMENT_LOCATION location);
	
	public abstract boolean isComplete();
	public abstract void setComplete();
	public abstract boolean isValid();
	public abstract Set<Namespace> getNamespaces();
	
	public abstract boolean valueEquals(Document document);
	public void compare(Document document);
}

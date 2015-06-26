package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.co.gairne.lxmlf.exception.ParserException;
import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
import uk.co.gairne.lxmlf.xml.definition.Comment;
import uk.co.gairne.lxmlf.xml.definition.DTD;
import uk.co.gairne.lxmlf.xml.definition.Document;
import uk.co.gairne.lxmlf.xml.definition.Element;
import uk.co.gairne.lxmlf.xml.definition.ElementValue;
import uk.co.gairne.lxmlf.xml.definition.Namespace;

public class FormattedDocument implements Document {

	private DTD dtd;
	private Element root;
	private String charset;
	private String version;

	// Comments may go anywhere except before the XML prolog.
	// Elements track comments inside them, so we only track comments outside of elements.
	private ArrayList<Comment> commentsAboveDTD;
	private ArrayList<Comment> commentsAboveRoot;
	private ArrayList<Comment> commentsBelowRoot;
	
	private boolean completed = false;
	
	@Override
	public String toString(int ancestryLevel) {
		if (version == null) {
			throw new ValidationException("XML documents must have a version.");
		}
		
		// Document prolog - version
		String s = PolicyUtil.generateIndent(ancestryLevel) +  "<?xml version=\"" + PolicyUtil.cleanWhitespace(version) + "\"";
		
		// Document prolog - charset
		if (charset != null) {
			s += " encoding='" + PolicyUtil.cleanWhitespace(charset) + "'";
		}
		s += "?>\n";
		
		// Any comments between Document prolog and DTD
		if (commentsAboveDTD != null) {
			for (Comment c : commentsAboveDTD) {
				s += c.toString(ancestryLevel);
			}
		}
		
		// DTD
		if (dtd != null) {
			s += dtd.toString(ancestryLevel) + "\n";
		}
		
		// Any comments between DTD and root
		if (commentsAboveRoot != null) {
			for (Comment c : commentsAboveRoot) {
				s += c.toString(ancestryLevel);
			}
		}
		
		// Root
		s += root.toString(ancestryLevel);
		
		// Any comments at document end
		if (commentsBelowRoot != null) {
			for (Comment c : commentsBelowRoot) {
				s += c.toString(ancestryLevel);
			}
		}
		
		return s;
	}
	
	@Override
	public String toString() {
		return toString(0);
	}

	@Override
	public DTD getDTD() {
		return dtd;
	}

	@Override
	public void setDTD(DTD dtd) {
		this.dtd = dtd;
	}

	@Override
	public Element getRoot() {
		return root;
	}

	@Override
	public void setRoot(Element element) {
		this.root = element;
	}

	@Override
	public String getCharset() {
		return charset;
	}

	@Override
	public void setCharset(String string) {
		this.charset = string;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String string) {
		this.version = string;
	}

	@Override
	public boolean isComplete() {
		return completed;
	}
	
	@Override
	public void setComplete() {
		completed = true;
	}

	@Override
	public boolean isValid() {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public Set<Namespace> getNamespaces() {
		return root.getNamespaces();
	}

	public void addComment(Comment comment, COMMENT_LOCATION location) {
		if (location == COMMENT_LOCATION.POSTPROLOG_PREDTD) {
			if (commentsAboveDTD == null) {
				commentsAboveDTD = new ArrayList<Comment>();
			}
			commentsAboveDTD.add(comment);
		}
		else if (location == COMMENT_LOCATION.POSTDTD_PREROOT) {
			if (commentsAboveRoot == null) {
				commentsAboveRoot = new ArrayList<Comment>();
			}
			commentsAboveRoot.add(comment);
		}
		else if (location == COMMENT_LOCATION.POSTROOT) {
			if (commentsBelowRoot == null) {
				commentsBelowRoot = new ArrayList<Comment>();
			}
			commentsBelowRoot.add(comment);
		}
		else if (location == COMMENT_LOCATION.INSIDE_ELEMENT) {
			throw new ParserException("Comments that reside inside elements should be added as a value of that element.");
		}
		else {
			throw new ParserException("Invalid comment location.");
		}
	}

	@Override
	public List<Comment> getComments(COMMENT_LOCATION location) {
		if (location == COMMENT_LOCATION.POSTPROLOG_PREDTD) {
			return commentsAboveDTD;
		}
		else if (location == COMMENT_LOCATION.POSTDTD_PREROOT) {
			return commentsAboveRoot;
		}
		else if (location == COMMENT_LOCATION.POSTROOT) {
			return commentsBelowRoot;
		}
		else if (location == COMMENT_LOCATION.INSIDE_ELEMENT) {
			throw new ParserException("Comments that reside inside elements should be retrieved as values of that element.");
		}
		else {
			throw new ParserException("Invalid comment location.");
		}
	}

	@Override
	public boolean valueEquals(Document document) {
		if (getDTD() == null) {
			if (document.getDTD() != null) return false;
		}
		else {
			if (!getDTD().equals(document.getDTD())) return false;
		}
		
		if (getRoot() == null) {
			if (document.getRoot() != null) return false;
		}
		else {
			if (!getRoot().equals(document.getRoot())) return false;
		}
		
		if (getCharset() == null) {
			if (document.getCharset() != null) return false;
		}
		else {
			if (!getCharset().equals(document.getCharset())) return false;
		}
		
		if (getVersion() == null) {
			if (document.getVersion() != null) return false;
		}
		else {
			if (!getVersion().equals(document.getVersion())) return false;
		}
		
		if (getComments(COMMENT_LOCATION.POSTPROLOG_PREDTD) == null) {
			if (document.getComments(COMMENT_LOCATION.POSTPROLOG_PREDTD) != null) return false;
		}
		else {
			if (!getComments(COMMENT_LOCATION.POSTPROLOG_PREDTD).equals(document.getComments(COMMENT_LOCATION.POSTPROLOG_PREDTD))) return false;
		}
		
		if (getComments(COMMENT_LOCATION.POSTDTD_PREROOT) == null) {
			if (document.getComments(COMMENT_LOCATION.POSTDTD_PREROOT) != null) return false;
		}
		else {
			if (!getComments(COMMENT_LOCATION.POSTDTD_PREROOT).equals(document.getComments(COMMENT_LOCATION.POSTDTD_PREROOT))) return false;
		}
		
		if (getComments(COMMENT_LOCATION.POSTROOT) == null) {
			if (document.getComments(COMMENT_LOCATION.POSTROOT) != null) return false;
		}
		else {
			if (!getComments(COMMENT_LOCATION.POSTROOT).equals(document.getComments(COMMENT_LOCATION.POSTROOT))) return false;
		}
		
		if (getNamespaces() == null) {
			if (document.getNamespaces() != null) return false;
		}
		else {
			if (!getNamespaces().equals(document.getNamespaces())) return false;
		}
		
		return completed == document.isComplete();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Document)) {
			return false;
		}
		
		Document otherDocument = (Document) other;
		
		return valueEquals(otherDocument);
	}
	
	public void compare(Document document) {
		getRoot().compare(document.getRoot(), "Root");
	}
	
	@Override
	public int hashCode() {
		return (getDTD() != null ? getDTD().hashCode() : 0) + (getRoot() != null ? getRoot().hashCode() : 0) + (getCharset() != null ? getCharset().hashCode() : 0) + (getVersion() != null ? getVersion().hashCode() : 0) + (getNamespaces() != null ? getNamespaces().hashCode() : 0)
				+ (getComments(COMMENT_LOCATION.POSTPROLOG_PREDTD) != null ? getComments(COMMENT_LOCATION.POSTPROLOG_PREDTD).hashCode() : 0)
				+ (getComments(COMMENT_LOCATION.POSTDTD_PREROOT) != null ? getComments(COMMENT_LOCATION.POSTDTD_PREROOT).hashCode() : 0)
				+ (getComments(COMMENT_LOCATION.POSTROOT) != null ? getComments(COMMENT_LOCATION.POSTROOT).hashCode() : 0);
	}
}

package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.co.gairne.lxmlf.exception.ParserException;
import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Comment;
import uk.co.gairne.lxmlf.xml.definition.DTD;
import uk.co.gairne.lxmlf.xml.definition.Document;
import uk.co.gairne.lxmlf.xml.definition.Element;
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
		String s = PolicyUtil.indent("<?xml version=\"" + PolicyUtil.cleanWhitespace(version) + "\"", ancestryLevel);
		
		if (charset != null) {
			s += " encoding='" + PolicyUtil.cleanWhitespace(charset) + "'";
		}
		s += "?>\n";
		
		if (commentsAboveDTD != null) {
			for (Comment c : commentsAboveDTD) {
				s += c.toString(ancestryLevel);
			}
		}
		
		// DTD and root elements indent themselves.
		if (dtd != null) {
			s += dtd.toString(ancestryLevel) + "\n";
		}
		
		if (commentsAboveRoot != null) {
			for (Comment c : commentsAboveRoot) {
				s += c.toString(ancestryLevel);
			}
		}
		
		s += root.toString(ancestryLevel);
		
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
}

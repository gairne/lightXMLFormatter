package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

import uk.co.gairne.lxmlf.exception.ParserException;

public class PolicyUtil {

	// Should tabs be used for indenting, or spaces?
	public static final boolean INDENT_WITH_TAB = true;
	// If indenting with spaces, how many spaces per level.
	public static final int SPACES_TO_INDENT = 4;
	// Put each attribute on its own line
	public static final boolean LINE_PER_ATTRIBUTE = true;
	// Should we only put attributes on new lines if there are a certain amount of attributes or more? (0 = disable)
	public static final int ATTRIBUTE_THRESHOLD = (!LINE_PER_ATTRIBUTE ? 0 : 2);
	// Should we align attributes to one more tab level, or to the end of the tag name?
	public static final boolean INDENT_ATTRIBUTE_TO_TAG = LINE_PER_ATTRIBUTE && true;
	// Sort attributes alphabetically
	public static final boolean SORT_ATTRIBUTES = true;
	// Preserve comment whitespace
	public static final boolean PRESERVE_COMMENT_SPACE = false;
	// If using line per attribute, put the closing angle bracket on a new line
	public static final boolean ANGLE_BRACKET_ON_NEW_LINE = LINE_PER_ATTRIBUTE && false;
	
	public static String indent(String string, int ancestryLevel) {
		if (INDENT_WITH_TAB) {
			return StringUtils.repeat("\t", ancestryLevel) + string;
		}
		else {
			return StringUtils.repeat(" ", ancestryLevel * SPACES_TO_INDENT);
		}
	}
	
	public static FormattedQName fromQName(QName qname) throws ParserException {
		if (qname == null) {
			throw new ParserException("Null QName parameter");
		}
		
		if (qname.getLocalPart() == null || qname.getNamespaceURI() == null || qname.getPrefix() == null) {
			throw new ParserException("QName not complete. Either local part, namespace prefix or namepsace URI missing.");
		}
		
		FormattedQName newName = new FormattedQName();
		newName.setLocalPart(qname.getLocalPart());
		newName.setNamespace(new FormattedNamespace(qname.getPrefix(), qname.getNamespaceURI()));
		return newName;
	}
	
	public static String cleanWhitespace(String string) {
		String test = StringUtils.normalizeSpace(string); //.replaceAll("\n", ""));
		return test;
	}
}

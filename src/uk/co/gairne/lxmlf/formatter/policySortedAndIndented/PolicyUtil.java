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
	public static final boolean LINE_PER_ATTRIBUTE = false;
	// Should we only put attributes on new lines if there are a certain amount of attributes or more? (0 = disable)
	public static final int ATTRIBUTE_THRESHOLD = (!LINE_PER_ATTRIBUTE ? 0 : 2);
	// How many characters of attributes are allowed on each line. This is incompatible with LINE_PER_ATTRIBUTE. (0 = disable)
	public static final int ATTRIBUTE_CHAR_THRESHOLD = (LINE_PER_ATTRIBUTE ? 0 : 80);
	// Should we align attributes to one more tab level, or to the end of the tag name?
	public static final boolean INDENT_ATTRIBUTE_TO_TAG = (LINE_PER_ATTRIBUTE || ATTRIBUTE_CHAR_THRESHOLD > 0) && true;
	// Sort attributes alphabetically
	public static final boolean SORT_ATTRIBUTES = true;
	// Preserve comment whitespace
	public static final boolean PRESERVE_COMMENT_SPACE = false;
	// If using line per attribute, put the closing angle bracket on a new line
	public static final boolean ANGLE_BRACKET_ON_NEW_LINE = LINE_PER_ATTRIBUTE && false;
	
	//If, whilst we are comparing two elements, we find they are different, should we continue to scan the child elements for differences (true)
	//  or should we return immediately to the parent element (false).
	public static final boolean SCAN_CHILDREN_FOR_DIFFERENCES_IF_NOT_EQUAL = true;
	
	public static String generateIndent(int ancestryLevel) {
		if (INDENT_WITH_TAB) {
			return StringUtils.repeat("\t", ancestryLevel);
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
		return StringUtils.normalizeSpace(string);
	}
}

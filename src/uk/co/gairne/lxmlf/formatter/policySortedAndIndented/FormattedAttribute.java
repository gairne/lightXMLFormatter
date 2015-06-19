package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
import uk.co.gairne.lxmlf.xml.definition.Element;
import uk.co.gairne.lxmlf.xml.definition.Namespace;
import uk.co.gairne.lxmlf.xml.definition.QName;

public class FormattedAttribute implements Comparable<FormattedAttribute>, Attribute, Comparator<FormattedAttribute> {

	private QName name;
	private String value;
	private String type;
	private Element parent;
	
	@Override
	public int compare(FormattedAttribute o1, FormattedAttribute o2) {
		return o1.getName().getLocalPart().compareTo(o2.getName().getLocalPart());
	}

	@Override
	public int compareTo(FormattedAttribute o) {
		return name.getLocalPart().compareTo(o.getName().getLocalPart());
	}

	@Override
	public String toString(int ancestryLevel) {
		if (name == null || name.toString() == null || value == null) {
			throw new ValidationException("Attributes must have a name and a quoted value.");
		}
		
		int numAttrs = ((Element) parent).getAttributes().size();
		
		if (parent.isRoot()) {
			numAttrs += parent.getNamespaces().size();
		}
		
		if (PolicyUtil.LINE_PER_ATTRIBUTE && numAttrs >= PolicyUtil.ATTRIBUTE_THRESHOLD) {
			if (PolicyUtil.INDENT_ATTRIBUTE_TO_TAG) {
				return "\n" + PolicyUtil.indent("", ancestryLevel) + StringUtils.repeat(" ", ((Element) parent).getName().toString().length()+2) + name.toString(ancestryLevel) + "=\"" + PolicyUtil.cleanWhitespace(value) + "\"";
			}
			else {
				return "\n" + PolicyUtil.indent("", ancestryLevel+1) + name.toString(ancestryLevel) + "=\"" + PolicyUtil.cleanWhitespace(value) + "\"";
			}
		}
		else {
			return name.toString(ancestryLevel) + "=\"" + PolicyUtil.cleanWhitespace(value) + "\"";
		}
		
	}
	
	@Override
	public String toString() {
		return toString(0);
	}

	@Override
	public QName getName() {
		return name;
	}

	@Override
	public void setName(QName qname) {
		this.name = qname;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String string) {
		this.value = string;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String string) {
		this.type = string;
	}

	@Override
	public Element getParent() {
		return parent;
	}

	@Override
	public void setParent(Element item) {
		parent = item;
	}

	@Override
	public Set<Namespace> getNamespaces() {
		Set<Namespace> ns = new HashSet<Namespace>();
		
		if (name != null && name.getNamespace() != null) {
			ns.add(name.getNamespace());
		}
		
		return ns;
	}
}

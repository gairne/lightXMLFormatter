package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
import uk.co.gairne.lxmlf.xml.definition.Element;
import uk.co.gairne.lxmlf.xml.definition.ElementValue;
import uk.co.gairne.lxmlf.xml.definition.Namespace;
import uk.co.gairne.lxmlf.xml.definition.QName;
import uk.co.gairne.lxmlf.xml.definition.Textual;

public class FormattedElement implements Element {
	
	private List<Attribute> attributes;
	private QName tagName;
	private List<ElementValue> value;
	private Element parent;
	
	private boolean isRoot = false;
		
	@Override
	public String toString(int ancestryLevel) {
		if (tagName == null || tagName.getLocalPart() == null) {
			throw new ValidationException("Elements must have a tag name.");
		}
		String s = PolicyUtil.indent("<" + tagName.toString(), ancestryLevel);
		
		if (isRoot) {
			s += " ";
			Set<Namespace> namespacesSet = getNamespaces();
			List<Namespace> namespacesList = new ArrayList<Namespace>();
			
			// If this set contains entirely FormattedNamespace, we can sort.
			List<FormattedNamespace> sns = new ArrayList<FormattedNamespace>();
			for (Namespace ns : namespacesSet) {
				if (!(ns instanceof FormattedNamespace)) {
					sns = null;
					break;
				}
				else {
					sns.add((FormattedNamespace) ns);
				}
			}
			// Otherwise, we cannot sort.
			if (sns != null) {
				Collections.sort(sns);
				for (Namespace ns : sns) {
					namespacesList.add(ns);
				}
			}
			else {
				namespacesList = new ArrayList<Namespace>(namespacesSet);
			}
			
			for (Namespace n : namespacesList) {
				if (PolicyUtil.LINE_PER_ATTRIBUTE && namespacesList.size() >= PolicyUtil.ATTRIBUTE_THRESHOLD) {
					if (PolicyUtil.INDENT_ATTRIBUTE_TO_TAG) {
						s += "\n" + PolicyUtil.indent("", ancestryLevel) + StringUtils.repeat(" ", tagName.toString().length()+2) + n.toString(ancestryLevel, true);
					}
					else {
						s += "\n" + PolicyUtil.indent("", ancestryLevel+1) + n.toString(ancestryLevel, true);
					}
				}
				else {
					return n.toString(ancestryLevel, true);
				}
			}
		}
		
		if (attributes != null) {
			if (PolicyUtil.SORT_ATTRIBUTES) {
				ArrayList<FormattedAttribute> sortedAttr = new ArrayList<FormattedAttribute>();
				for (Attribute attr : attributes) {
					if (attr instanceof FormattedAttribute) {
						sortedAttr.add((FormattedAttribute) attr);
					}
				}
				Collections.sort(sortedAttr);
				for (FormattedAttribute attr : sortedAttr) {
					s += " " + attr.toString(ancestryLevel);
				}
			}
			else {
				for (Attribute attr : attributes) {
					s += " " + attr.toString(ancestryLevel);
				}
			}
		}
		
		if (hasEmptyValue()) {
			if (PolicyUtil.LINE_PER_ATTRIBUTE && (attributes != null ? attributes.size() >= PolicyUtil.ATTRIBUTE_THRESHOLD : false) && PolicyUtil.ANGLE_BRACKET_ON_NEW_LINE) {
				s += "\n" + PolicyUtil.indent("/>\n", ancestryLevel);
			}
			else {
				s += " />\n";
			}
		}
		else {
			if (PolicyUtil.LINE_PER_ATTRIBUTE && (attributes != null ? attributes.size() >= PolicyUtil.ATTRIBUTE_THRESHOLD : false) && PolicyUtil.ANGLE_BRACKET_ON_NEW_LINE) {
				s += "\n" + PolicyUtil.indent(">", ancestryLevel);
			}
			else {
				s += ">";
			}
			if (hasSimpleValue()) {
				for (ElementValue item : value) {
					s += PolicyUtil.cleanWhitespace(item.toString(ancestryLevel+1));
				}
				s += "</" + tagName.toString(ancestryLevel) + ">\n";
			}
			else {
				s += "\n";
				for (ElementValue item : value) {
					s += item.toString(ancestryLevel+1);
				}
				s += PolicyUtil.indent("</" + tagName.toString(ancestryLevel) + ">\n", ancestryLevel);
			}
		}
		
		return s;
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	@Override
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	@Override
	public void setAttributes(List<Attribute> list) {
		attributes = list;
	}
	
	@Override
	public void addAttribute(Attribute attribute) {
		if (attributes == null) {
			attributes = new ArrayList<Attribute>();
		}
		attributes.add(attribute);
	}
	
	@Override
	public QName getName() {
		return tagName;
	}
	
	@Override
	public void setName(QName qname) {
		this.tagName = qname;
	}
	
	@Override
	public List<ElementValue> getValue() {
		return value;
	}
	
	@Override
	public void setValue(List<ElementValue> list) {
		this.value = list;
	}
	
	@Override
	public void addValue(ElementValue elementvalue) {
		if (value == null) {
			value = new ArrayList<ElementValue>();
		}
		value.add(elementvalue);
	}
	
	@Override
	public boolean hasAttribute(Attribute attribute) {
		for (Attribute a : attributes) {
			if (a.equals(attribute)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasEmptyValue() {
		return (value == null || value.size() == 0);
	}
	
	@Override
	public boolean hasSimpleValue() {
		return (value != null && value.size() == 1 && value.get(0) instanceof Textual);
	}
	
	@Override
	public boolean hasComplexValue() {
		return (value != null && value.size() > 0 && !hasSimpleValue());
	}

	@Override
	public ElementValue getPreviousValue(ElementValue ev) {
		throw new IllegalStateException("Method not implemented");
	}

	@Override
	public ElementValue getNextValue(ElementValue ev) {
		throw new IllegalStateException("Method not implemented");
	}

	@Override
	public ElementValue getLastValue() {
		if (value == null || value.size() == 0) {
			return null;
		}
		
		return value.get(value.size() - 1);
	}
	
	@Override
	public ElementValue getFirstValue() {
		if (value == null || value.size() == 0) {
			return null;
		}
		
		return value.get(0);
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
		
		if (value != null) {
			for (ElementValue val : value) {
				ns.addAll(val.getNamespaces());
			}
		}
		
		if (tagName != null && tagName.getNamespace() != null) {
			ns.add(tagName.getNamespace());
		}
		
		return ns;
	}

	@Override
	public boolean isRoot() {
		return isRoot;
	}

	@Override
	public void setRoot() {
		isRoot = true;
	}
}

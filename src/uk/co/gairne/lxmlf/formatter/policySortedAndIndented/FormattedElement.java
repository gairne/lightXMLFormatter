package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.formatter.sortable.SortableAttribute;
import uk.co.gairne.lxmlf.formatter.sortable.SortableNamespace;
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
	
	private boolean isRoot = false;
		
	@Override
	public String toString(int ancestryLevel) {
		if (tagName == null || tagName.getLocalPart() == null) {
			throw new ValidationException("Elements must have a tag name.");
		}
		String s = PolicyUtil.generateIndent(ancestryLevel) + "<" + tagName.toString();
		
		// For attribute and namespace writing:
		// Ignoring indentation, which column are we on
		int currentCharacter = 0;
		
		// Attach namespaces to the root element as attributes.
		if (isRoot) {
			
			// Namespaces
			List<Namespace> namespacesList = sortNamespaces();
			
			for (Namespace n : namespacesList) {
				if (PolicyUtil.LINE_PER_ATTRIBUTE && namespacesList.size() >= PolicyUtil.ATTRIBUTE_THRESHOLD) {
					if (PolicyUtil.INDENT_ATTRIBUTE_TO_TAG) {
						s += "\n" + PolicyUtil.generateIndent(ancestryLevel) + StringUtils.repeat(" ", tagName.toString().length()+2) + n.toString(ancestryLevel, true);
					}
					else {
						s += "\n" + PolicyUtil.generateIndent(ancestryLevel+1) + n.toString(ancestryLevel, true);
					}
				}
				else if (PolicyUtil.ATTRIBUTE_CHAR_THRESHOLD >= 0) {
					String nextNSStr = n.toString(ancestryLevel, true);
					if (currentCharacter == 0 || currentCharacter + nextNSStr.length() + 1 <= PolicyUtil.ATTRIBUTE_CHAR_THRESHOLD) {
						s += " " + nextNSStr;
						currentCharacter += nextNSStr.length() + 1;
					}
					else {
						if (PolicyUtil.INDENT_ATTRIBUTE_TO_TAG) {
							s += "\n" + PolicyUtil.generateIndent(ancestryLevel) + StringUtils.repeat(" ", tagName.toString().length()+2) + nextNSStr;
						}
						else {
							s += "\n" + PolicyUtil.generateIndent(ancestryLevel+1) + nextNSStr;
						}
						currentCharacter = nextNSStr.length();
					}
				}
				else {
					s += " " + n.toString(ancestryLevel, true);
				}
			}
		}
		
		// Attach attributes after the tag (and namespaces if applicable)
		if (attributes != null) {
			List<Attribute> attributesList = attributes;
			if (PolicyUtil.SORT_ATTRIBUTES) {
				attributesList = sortAttributes();
			}
			
			for (Attribute attr : attributesList) {
				if (PolicyUtil.LINE_PER_ATTRIBUTE && attributesList.size() >= PolicyUtil.ATTRIBUTE_THRESHOLD) {
					if (PolicyUtil.INDENT_ATTRIBUTE_TO_TAG) {
						s += "\n" + PolicyUtil.generateIndent(ancestryLevel) + StringUtils.repeat(" ", tagName.toString().length()+2) + attr.toString(ancestryLevel);
					}
					else {
						s += "\n" + PolicyUtil.generateIndent(ancestryLevel+1) + attr.toString(ancestryLevel);
					}
				}
				else if (PolicyUtil.ATTRIBUTE_CHAR_THRESHOLD >= 0) {
					String nextAttrStr = attr.toString(ancestryLevel);
					if (currentCharacter == 0 || currentCharacter + nextAttrStr.length() + 1 <= PolicyUtil.ATTRIBUTE_CHAR_THRESHOLD) {
						s += " " + nextAttrStr;
						currentCharacter += nextAttrStr.length() + 1;
					}
					else {
						if (PolicyUtil.INDENT_ATTRIBUTE_TO_TAG) {
							s += "\n" + PolicyUtil.generateIndent(ancestryLevel) + StringUtils.repeat(" ", tagName.toString().length()+2) + nextAttrStr;
						}
						else {
							s += "\n" + PolicyUtil.generateIndent(ancestryLevel+1) + nextAttrStr;
						}
						currentCharacter = nextAttrStr.length();
					}
				}
				else {
					s += " " + attr.toString(ancestryLevel);
				}
			}
		}
		
		// End of opening element
		
		// We place the end of element bracket on a new line if:
		//  1) We are splitting attributes over multiple lines
		//  2) There are enough attributes to meet the splitting threshold
		//  3) We have turned on placement of element bracket on a new line
		if (PolicyUtil.LINE_PER_ATTRIBUTE && (attributes != null ? attributes.size() >= PolicyUtil.ATTRIBUTE_THRESHOLD : false) && PolicyUtil.ANGLE_BRACKET_ON_NEW_LINE) {
			s += "\n" + PolicyUtil.generateIndent(ancestryLevel) + (hasEmptyValue() ? "/" : "") + ">\n";
		}
		else {
			s += (hasEmptyValue() ? " />" : ">");
		}
		
		// Handle the value
		if (!hasEmptyValue()) {
			// If the value is purely text-based
			if (hasSimpleValue()) {
				for (ElementValue item : value) {
					s += PolicyUtil.cleanWhitespace(item.toString(ancestryLevel+1));
				}
			}
			else {
				s += "\n";
				for (ElementValue item : value) {
					s += item.toString(ancestryLevel+1) + "\n";
				}
			}
		}
		
		// Close element
		if (!hasEmptyValue()) {
			if (hasSimpleValue()) {
				// No new line after the textual value
				s += "</" + tagName.toString(ancestryLevel) + ">";
			}
			else {
				s += PolicyUtil.generateIndent(ancestryLevel) + "</" + tagName.toString(ancestryLevel) + ">";
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
	
	private List<Attribute> sortAttributes() {
		List<Attribute> sortedList = new ArrayList<Attribute>();
		ArrayList<SortableAttribute> sortedAttributes = new ArrayList<SortableAttribute>();
		for (Attribute attr : attributes) {
			if (attr instanceof SortableAttribute) {
				sortedAttributes.add((SortableAttribute) attr);
			}
			else {
				// This attribute cannot be sorted, add it directly to the returned list.
				sortedList.add(attr);
			}
		}
		Collections.sort(sortedAttributes);
		sortedList.addAll(sortedAttributes);
		return sortedList;
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
	public ElementValue getValue(int n) {
		if (value == null || value.size() <= n) {
			return null;
		}
		return value.get(n);
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
		for (ElementValue item : value) {
			if (!(item instanceof Textual)) {
				return false;
			}
		}
		return value != null && value.size() > 0;
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
	
	private List<Namespace> sortNamespaces() {
		List<Namespace> sortedList = new ArrayList<Namespace>();
		List<SortableNamespace> sortedNamespaces = new ArrayList<SortableNamespace>();
		for (Namespace ns : getNamespaces()) {
			if (ns instanceof SortableNamespace) {
				sortedNamespaces.add((SortableNamespace) ns);
			}
			else {
				// This attribute cannot be sorted, add it directly to the returned list.
				sortedList.add(ns);
			}
		}
		Collections.sort(sortedNamespaces);
		sortedList.addAll(sortedNamespaces);
		return sortedList;
	}

	@Override
	public boolean isRoot() {
		return isRoot;
	}

	@Override
	public void setRoot() {
		isRoot = true;
	}

	@Override
	public boolean valueEquals(Element element) {
		if (getAttributes() == null) {
			if (element.getAttributes() != null) return false;
		}
		else {
			if (!getAttributes().equals(element.getAttributes())) return false;
		}
		
		if (getName() == null) {
			if (element.getName() != null) return false;
		}
		else {
			if (!getName().equals(element.getName())) return false;
		}
		
		if (getValue() == null) {
			if (element.getValue() != null) return false;
		}
		else {
			if (!getValue().equals(element.getValue())) return false;
		}
		
		if (getNamespaces() == null) {
			if (element.getNamespaces() != null) return false;
		}
		else {
			if (!getNamespaces().equals(element.getNamespaces())) return false;
		}
		
		return isRoot == element.isRoot();
	}
	
	public void compare(Element element, String location) {
		if (!valueEquals(element)) {
			System.out.println("Difference at location " + location);
			
			if (toString().length() <= 50) {
				System.out.println("A: " + toString());
			}
			else {
				System.out.println("A: " + toString().substring(0, 50) + "...");
			}
			if (element.toString().length() <= 50) {
				System.out.println("B: " + element.toString());
			}
			else {
				System.out.println("B: " + element.toString().substring(0, 50) + "...");
			}
			
			if (!PolicyUtil.SCAN_CHILDREN_FOR_DIFFERENCES_IF_NOT_EQUAL) {
				return;
			}
		}
		
		if (value != null) {
			int childNo = 0;
			for (ElementValue item : value) {
				if (item instanceof Element) {
					ElementValue oppositeItem = element.getValue(childNo);
					if (oppositeItem instanceof Element) {
						((Element) item).compare((Element) element.getValue(childNo), location + "." + childNo);
					}
					else {
						System.out.println("Child mismatch at location " + location + "." + childNo);
						
						if (item.toString().length() <= 50) {
							System.out.println("A: " + item.toString());
						}
						else {
							System.out.println("A: " + item.toString().substring(0, 50) + "...");
						}
						if (oppositeItem.toString().length() <= 50) {
							System.out.println("B: " + oppositeItem.toString());
						}
						else {
							System.out.println("B: " + oppositeItem.toString().substring(0, 50) + "...");
						}
					}
				}
				childNo++;
			}
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Element)) {
			return false;
		}
		
		Element otherElement = (Element) other;
		
		return valueEquals(otherElement);
	}
	
	@Override
	public int hashCode() {
		return (getName() != null ? getName().hashCode() : 0) + (getValue() != null ? getValue().hashCode() : 0) + (getAttributes() != null ? getAttributes().hashCode() : 0) + (getNamespaces() != null ? getNamespaces().hashCode() : 0);
	}
}

package lightXMLFormatter.formatter.policySortedAndIndented;

import java.util.ArrayList;
import java.util.List;

import lightXMLFormatter.exception.ValidationException;
import lightXMLFormatter.xml.definition.Attribute;
import lightXMLFormatter.xml.definition.Element;
import lightXMLFormatter.xml.definition.ElementValue;
import lightXMLFormatter.xml.definition.QName;
import lightXMLFormatter.xml.definition.Textual;

public class FormattedElement implements Element {
	
	private List<Attribute> attributes;
	private QName tagName;
	private List<ElementValue> value;
	
	@Override
	public String toString(int ancestryLevel) {
		if (tagName == null || tagName.getLocalPart() == null) {
			throw new ValidationException("Elements must have a tag name.");
		}
		String s = "<" + tagName.toString();
		if (attributes != null) {
			for (Attribute attr : attributes) {
				s += " " + attr.toString();
			}
		}
		
		if (hasEmptyValue()) {
			s += " />\n";
		}
		else {
			s += ">";
			if (!hasSimpleValue()) {
				s += "\n";
			}
			for (ElementValue item : value) {
				s += item.toString(ancestryLevel+1);
			}
			s += "</" + tagName.toString() + ">\n";
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
	
}

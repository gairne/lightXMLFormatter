package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import java.util.HashSet;
import java.util.Set;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.formatter.sortable.SortableAttribute;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
import uk.co.gairne.lxmlf.xml.definition.Namespace;
import uk.co.gairne.lxmlf.xml.definition.QName;

public class FormattedAttribute extends SortableAttribute {

	private QName name;
	private String value;
	private String type;

	@Override
	public String toString(int ancestryLevel) {
		if (name == null || name.toString() == null || value == null) {
			throw new ValidationException("Attributes must have a name and a quoted value.");
		}
		
		return name.toString(ancestryLevel) + "=\"" + PolicyUtil.cleanWhitespace(value) + "\"";
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
	public Set<Namespace> getNamespaces() {
		Set<Namespace> ns = new HashSet<Namespace>();
		
		if (name != null && name.getNamespace() != null) {
			ns.add(name.getNamespace());
		}
		
		return ns;
	}

	@Override
	public boolean valueEquals(Attribute attribute) {
		if (getName() == null) {
			if (attribute.getName() != null) return false;
		}
		else {
			if (!getName().equals(attribute.getName())) return false;
		}
		
		if (getValue() == null) {
			if (attribute.getValue() != null) return false;
		}
		else {
			if (!getValue().equals(attribute.getValue())) return false;
		}
		
		if (getType() == null) {
			if (attribute.getType() != null) return false;
		}
		else {
			if (!getType().equals(attribute.getType())) return false;
		}
		
		if (getNamespaces() == null) {
			if (attribute.getNamespaces() != null) return false;
		}
		else {
			if (!getNamespaces().equals(attribute.getNamespaces())) return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Attribute)) {
			return false;
		}
		
		Attribute otherAttribute = (Attribute) other;
		
		return valueEquals(otherAttribute);
	}
	
	@Override
	public int hashCode() {
		return (getName() != null ? getName().hashCode() : 0) + (getValue() != null ? getValue().hashCode() : 0) + (getType() != null ? getType().hashCode() : 0) + (getNamespaces() != null ? getNamespaces().hashCode() : 0);
	}
}

package uk.co.gairne.lxmlf.formatter.sortable;

import java.util.Comparator;

import uk.co.gairne.lxmlf.xml.definition.Attribute;

public abstract class SortableAttribute implements Attribute, Comparable<SortableAttribute>, Comparator<SortableAttribute> {

	@Override
	public int compare(SortableAttribute attr1, SortableAttribute attr2) {
		return attr1.getName().getLocalPart().compareTo(attr2.getName().getLocalPart());
	}

	@Override
	public int compareTo(SortableAttribute other) {
		return getName().getLocalPart().compareTo(other.getName().getLocalPart());
	}
}

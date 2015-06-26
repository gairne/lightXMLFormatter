package uk.co.gairne.lxmlf.formatter.sortable;

import java.util.Comparator;

import uk.co.gairne.lxmlf.xml.definition.Namespace;

public abstract class SortableNamespace implements Namespace, Comparable<SortableNamespace>, Comparator<SortableNamespace>{

	@Override
	public int compare(SortableNamespace o1, SortableNamespace o2) {
		return o1.toString().compareTo(o2.toString());
	}

	@Override
	public int compareTo(SortableNamespace o) {
		return toString().compareTo(o.toString());
	}
}

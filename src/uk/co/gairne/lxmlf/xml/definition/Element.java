package uk.co.gairne.lxmlf.xml.definition;

import java.util.List;

public interface Element extends ElementValue {
	public abstract List<Attribute> getAttributes();
	public abstract void setAttributes(List<Attribute> list);
	public abstract void addAttribute(Attribute attribute);
	public abstract QName getName();
	public abstract void setName(QName qname);
	public abstract List<ElementValue> getValue();
	public abstract void setValue(List<ElementValue> list);
	public abstract void addValue(ElementValue elementvalue);
	public abstract boolean hasAttribute(Attribute attribute);
	
	public abstract ElementValue getPreviousValue(ElementValue ev);
	public abstract ElementValue getNextValue(ElementValue ev);
	public abstract ElementValue getLastValue();
	public abstract ElementValue getFirstValue();
	
	public abstract boolean isRoot();
	public abstract void setRoot();
	
	/**
	 * @return True if this element has no value.
	 */
	public boolean hasEmptyValue();
	
	/**
	 * @return True if this element's value is text only
	 */
	public boolean hasSimpleValue();
	
	/**
	 * @return True if this element's value contains child elements (including child elements alongside text)
	 */
	public boolean hasComplexValue();
}

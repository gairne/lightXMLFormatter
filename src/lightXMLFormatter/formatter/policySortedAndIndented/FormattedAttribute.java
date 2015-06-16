package lightXMLFormatter.formatter.policySortedAndIndented;

import java.util.Comparator;

import lightXMLFormatter.exception.ValidationException;
import lightXMLFormatter.xml.definition.Attribute;
import lightXMLFormatter.xml.definition.QName;

public class FormattedAttribute implements Attribute, Comparator<FormattedAttribute> {

	private QName name;
	private String value;
	private String type;
	
	@Override
	public int compare(FormattedAttribute o1, FormattedAttribute o2) {
		return o1.getName().getLocalPart().compareTo(o2.getName().getLocalPart());
	}

	@Override
	public String toString(int ancestryLevel) {
		if (name.toString() == null || value == null) {
			throw new ValidationException("Attributes must have a name and a quoted value.");
		}
		return name + "=\"" + value + "\"";
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
}

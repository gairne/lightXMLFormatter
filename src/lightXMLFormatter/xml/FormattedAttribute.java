package lightXMLFormatter.xml;

public class FormattedAttribute extends FormattedItem {
	
	private FormattedQName name;
	private String value;
	private String type;
	
	public FormattedAttribute(FormattedQName name, String value, String type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	public String toString() {
		return name + "=\"" + value + "\"";
	}

	public FormattedQName getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public String getType() {
		return type;
	}
}

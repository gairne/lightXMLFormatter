package lightXMLFormatter.xml;

public class FormattedDTD extends FormattedItem {
	
	private String dtd;
	
	public FormattedDTD(String dtd) {
		this.dtd = dtd;
	}
	
	public String toString() {
		return dtd;
	}
}

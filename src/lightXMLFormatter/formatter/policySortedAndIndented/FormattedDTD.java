package lightXMLFormatter.formatter.policySortedAndIndented;

import lightXMLFormatter.exception.ValidationException;
import lightXMLFormatter.xml.definition.DTD;

public class FormattedDTD implements DTD {

	private String dtd;

	@Override
	public String toString(int ancestryLevel) {
		if (dtd == null) {
			throw new ValidationException("DTD must contain declaration text.");
		}
		return dtd;
	}
	
	@Override
	public String toString() {
		return toString(0);
	}

	@Override
	public String getDTD() {
		return dtd;
	}

	@Override
	public void setDTD(String string) {
		this.dtd = string;
	}

}

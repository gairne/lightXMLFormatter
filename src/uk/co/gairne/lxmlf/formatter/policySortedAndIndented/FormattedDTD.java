package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.DTD;

public class FormattedDTD implements DTD {

	private String dtd;
	
	public FormattedDTD(String dtd) {
		setDTD(dtd);
	}

	@Override
	public String toString(int ancestryLevel) {
		if (dtd == null) {
			throw new ValidationException("DTD must contain declaration text.");
		}
		return PolicyUtil.indent(PolicyUtil.cleanWhitespace(dtd), ancestryLevel);
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

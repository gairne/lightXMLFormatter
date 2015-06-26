package uk.co.gairne.lxmlf.formatter.policySortedAndIndented;

import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.xml.definition.Attribute;
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
		
		return PolicyUtil.generateIndent(ancestryLevel) + PolicyUtil.cleanWhitespace(dtd);
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

	@Override
	public boolean valueEquals(DTD dtd) {
		if (getDTD() == null) {
			if (dtd.getDTD() != null) return false;
		}
		else {
			if (!getDTD().equals(dtd.getDTD())) return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DTD)) {
			return false;
		}
		
		DTD otherDTD = (DTD) other;
		
		return valueEquals(otherDTD);
	}
	
	@Override
	public int hashCode() {
		return (getDTD() != null ? getDTD().hashCode() : 0);
	}
}

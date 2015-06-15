package lightXMLFormatter.xml;

public class FormattedRoot extends FormattedItem {
	
	private FormattedElement root;
	private FormattedDTD dtd;
	
	private String charset;
	private String version;
	
	public FormattedRoot(String charset, String version) {
		this.charset = charset;
		this.version = version;
	}
	
	public String toString() {
		String s = "";
		s = "<?xml version=\"" + version + "\" encoding='" + charset + "'?>";
		return s + (dtd != null ? dtd.toString() : "") + "\n" + root.toString();
	}

	public FormattedElement getRoot() {
		return root;
	}

	public void setRoot(FormattedElement root) {
		this.root = root;
	}

	public FormattedDTD getDtd() {
		return dtd;
	}

	public void setDtd(FormattedDTD dtd) {
		this.dtd = dtd;
	}
}

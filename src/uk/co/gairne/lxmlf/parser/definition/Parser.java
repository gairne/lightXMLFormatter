package uk.co.gairne.lxmlf.parser.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import uk.co.gairne.lxmlf.exception.ParserException;
import uk.co.gairne.lxmlf.xml.definition.Document;
import uk.co.gairne.lxmlf.xml.definition.Element;

public interface Parser {
	
	public abstract Element getCurrentElement();
	public abstract Element peekCurrentElement();
	public abstract void pushNewCurrentElement(Element item);

	public abstract Document parseFile(File file) throws FileNotFoundException, ParserException;
	public abstract Document parseFile(String filename) throws FileNotFoundException, ParserException;
	public abstract Document parseFile(FileInputStream inputStream) throws ParserException;
}

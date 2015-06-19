package uk.co.gairne.lxmlf.parser.definition;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.NotationDeclaration;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

import uk.co.gairne.lxmlf.exception.ParserException;

public interface StaxParser extends Parser {
	
	public abstract void handleNewElement(StartElement event) throws ParserException;
	public abstract void handleCloseElement(EndElement event) throws ParserException;
	
	public abstract void handleNewDocument(StartDocument event) throws ParserException;
	public abstract void handleCloseDocument(EndDocument event) throws ParserException;
	
	public abstract void handleProcessingInstruction(ProcessingInstruction event) throws ParserException;
	public abstract void handleComment(Comment event) throws ParserException;
	public abstract void handleEntityReference(EntityReference event) throws ParserException;
	public abstract void handleAttribute(Attribute event) throws ParserException;
	public abstract void handleTextual(Characters event) throws ParserException;
	public abstract void handleDTD(DTD event) throws ParserException;
	public abstract void handleEntityDeclaration(EntityDeclaration event) throws ParserException;
	public abstract void handleNotationDeclaration(NotationDeclaration event) throws ParserException;
	public abstract void handleNamespaceDeclaration(Namespace event) throws ParserException;
	
}

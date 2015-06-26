package uk.co.gairne.lxmlf.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
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
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

import uk.co.gairne.lxmlf.exception.ParserException;
import uk.co.gairne.lxmlf.exception.ValidationException;
import uk.co.gairne.lxmlf.formatter.policySortedAndIndented.FormattedAttribute;
import uk.co.gairne.lxmlf.formatter.policySortedAndIndented.FormattedElement;
import uk.co.gairne.lxmlf.formatter.policySortedAndIndented.FormattedComment;
import uk.co.gairne.lxmlf.formatter.policySortedAndIndented.FormattedDocument;
import uk.co.gairne.lxmlf.formatter.policySortedAndIndented.FormattedDTD;
import uk.co.gairne.lxmlf.formatter.policySortedAndIndented.FormattedTextual;
import uk.co.gairne.lxmlf.formatter.policySortedAndIndented.PolicyUtil;
import uk.co.gairne.lxmlf.xml.definition.Document.COMMENT_LOCATION;
import uk.co.gairne.lxmlf.xml.definition.Element;

public class StaxParser implements uk.co.gairne.lxmlf.parser.definition.StaxParser {

	private static final boolean DEBUG = false;
	
	private Stack<Element> stack;
	private FormattedDocument document;
	
	@Override
	public FormattedDocument parseFile(File file) throws FileNotFoundException, ParserException {
		return parseFile(new FileInputStream(file));
	}

	@Override
	public FormattedDocument parseFile(String filename) throws FileNotFoundException, ParserException {
		return parseFile(new FileInputStream(filename));
	}

	@Override
	public FormattedDocument parseFile(FileInputStream inputStream) throws ParserException {
		stack = new Stack<Element>();
		
		XMLInputFactory factory;
		XMLEventReader reader;
		
		try {
			factory = XMLInputFactory.newInstance();
			reader = factory.createXMLEventReader(inputStream);
		}
		catch (FactoryConfigurationError | XMLStreamException exception) {
			throw new ParserException("An error occured whilst processing an XML document", exception);
		}
		
		while (reader.hasNext()) {
			try {
				XMLEvent event = reader.nextEvent();
				handleEvent(event);
			}
			catch (NoSuchElementException | XMLStreamException exception) {
				throw new ParserException("An error occured whilst processing an XML document", exception);
			}
		}
		
		FormattedDocument finishedDocument = document;
		document = null;
		
		if (!finishedDocument.isComplete()) {
			throw new ValidationException("Reached end of file and no end of document encountered.");
		}
		if (!stack.empty()) {
			throw new ValidationException("Reached end of file and parser stack not empty.");
		}
		
		return finishedDocument;
	}
	
	public void handleEvent(XMLEvent e) {
		switch(e.getEventType()) {
		case XMLEvent.ATTRIBUTE:
			handleAttribute((Attribute) e);
			break;
		case XMLEvent.CDATA:
			handleTextual((Characters) e);
			break;
		case XMLEvent.CHARACTERS:
			handleTextual((Characters) e);
			break;
		case XMLEvent.COMMENT:
			handleComment((Comment) e);
			break;
		case XMLEvent.DTD:
			handleDTD((DTD) e);
			break;
		case XMLEvent.END_DOCUMENT:
			handleCloseDocument((EndDocument) e);
			break;
		case XMLEvent.END_ELEMENT:
			handleCloseElement((EndElement) e);
			break;
		case XMLEvent.ENTITY_DECLARATION:
			handleEntityDeclaration((EntityDeclaration) e);
			break;
		case XMLEvent.ENTITY_REFERENCE:
			handleEntityReference((EntityReference) e);
			break;
		case XMLEvent.NAMESPACE:
			handleNamespaceDeclaration((Namespace) e);
			break;
		case XMLEvent.NOTATION_DECLARATION:
			handleNotationDeclaration((NotationDeclaration) e);
			break;
		case XMLEvent.PROCESSING_INSTRUCTION:
			handleProcessingInstruction((ProcessingInstruction) e);
			break;
		case XMLEvent.SPACE:
			handleTextual((Characters) e);
			break;
		case XMLEvent.START_DOCUMENT:
			handleNewDocument((StartDocument) e);
			break;
		case XMLEvent.START_ELEMENT:
			handleNewElement((StartElement) e);
			break;
		}
	}
	
	@Override
	public Element getCurrentElement() throws EmptyStackException {
		return stack.pop();
	}

	@Override
	public Element peekCurrentElement() throws EmptyStackException {
		return stack.peek();
	}

	@Override
	public void pushNewCurrentElement(Element item) {
		stack.push(item);
	}

	@Override
	public void handleNewElement(StartElement event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleNewElement: " + event.getName());
		}
		
		FormattedElement elem = new FormattedElement();
		
		elem.setName(PolicyUtil.fromQName(event.getName()));
		
		@SuppressWarnings("unchecked")
		Iterator<Attribute> attributeIterator = event.getAttributes();
		while (attributeIterator.hasNext()) {
			Attribute eventAttribute = attributeIterator.next();
			if (eventAttribute == null) {
				throw new ParserException("Invalid attribute");
			}
			if (eventAttribute.getDTDType() == null) {
				throw new ParserException("Missing attribute type");
			}
			if (eventAttribute.getName() == null) {
				throw new ParserException("Missing attribute name");
			}
			if (eventAttribute.getValue() == null) {
				throw new ParserException("Missing attribute value");
			}
						
			FormattedAttribute attribute = new FormattedAttribute();
			attribute.setType(eventAttribute.getDTDType());
			attribute.setValue(eventAttribute.getValue());
			attribute.setName(PolicyUtil.fromQName(eventAttribute.getName()));
			elem.addAttribute(attribute);
		}
		
		if (document == null) {
			throw new ParserException("Element encountered before document prolog");
		}
		
		// Are we the first element?
		if (document.getRoot() == null) {
			document.setRoot(elem);
			elem.setRoot();
		}
		
		pushNewCurrentElement(elem);
	}

	@Override
	public void handleCloseElement(EndElement event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleCloseElement: " + event.getName());
		}
		
		FormattedElement currentElement, parentElement;
		
		if (document == null) {
			throw new ParserException("Element encountered before document prolog");
		}
		
		try {
			currentElement = (FormattedElement) getCurrentElement();
			
			if (!currentElement.getName().equals(PolicyUtil.fromQName(event.getName()))) {
				throw new ParserException("Closed element does not match current open element.");
			}
		}
		catch (ClassCastException exception) {
			throw new ParserException("Element on stack is a not a " + FormattedElement.class.getName(), exception);
		}
		catch (EmptyStackException exception) {
			throw new ParserException("Unexpected empty stack");
		}
		
		try {
			parentElement = (FormattedElement) peekCurrentElement();
			parentElement.addValue(currentElement);
		}
		catch (EmptyStackException exception) {
			// This is fine as long as the currentElement was the document root.
			// The document root has no parent.
			
			if (!document.getRoot().equals(currentElement)) {
				throw new ParserException("Unexpected empty stack"); 
			}
		}
	}

	@Override
	public void handleNewDocument(StartDocument event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleNewDocument: " + event.getVersion());
		}
		
		if (document != null) {
			throw new ParserException("Encountered a document prolog when one was already open.");
		}
		
		if (event.getCharacterEncodingScheme() == null || event.getVersion() == null) {
			throw new ParserException("Missing information in document prolog");
		}
		
		document = new FormattedDocument();
		document.setCharset(event.getCharacterEncodingScheme());
		document.setVersion(event.getVersion());
	}

	@Override
	public void handleCloseDocument(EndDocument event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleCloseDocument");
		}
		
		if (document == null) {
			throw new ParserException("End of document encountered with no current document open");
		}
		
		document.setComplete();
	}

	@Override
	public void handleProcessingInstruction(ProcessingInstruction event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleNewElement: " + event.getData());
		}
		
		if (document == null) {
			throw new ParserException("Processing instruction encountered with no current document open");
		}
		
		throw new ParserException("Unimplemented feature");
	}

	@Override
	public void handleComment(Comment event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleComment: " + event.getText());
		}
		
		if (document == null) {
			throw new ParserException("Comment encountered before document prolog");
		}
		
		FormattedComment comment = new FormattedComment(event.getText());

		FormattedElement parentElement;
		try {
			parentElement = (FormattedElement) peekCurrentElement();
			parentElement.addValue(comment);
		}
		catch (EmptyStackException exception) {
			// The comment lies outside of the root element
			
			// Comment before DTD
			if (document.getDTD() == null) {
				document.addComment(comment, COMMENT_LOCATION.POSTPROLOG_PREDTD);
			}
			// Comment before root node / after DTD
			else if (document.getRoot() == null) {
				document.addComment(comment, COMMENT_LOCATION.POSTDTD_PREROOT);
			}
			// Comment after root node
			else {
				document.addComment(comment, COMMENT_LOCATION.POSTROOT);
			}
		}
	}

	@Override
	public void handleEntityReference(EntityReference event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleEntityReference: " + event.getName());
		}
		
		if (document == null) {
			throw new ParserException("Entity reference encountered before document prolog");
		}
		
		throw new ParserException("Unimplemented feature");
	}

	@Override
	public void handleAttribute(Attribute event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleAttribute: " + event.getName());
		}
		
		if (document == null) {
			throw new ParserException("Attribute encountered before document prolog");
		}
		
		if (event == null) {
			throw new ParserException("Invalid attribute");
		}
		if (event.getDTDType() == null) {
			throw new ParserException("Missing attribute type");
		}
		if (event.getName() == null) {
			throw new ParserException("Missing attribute name");
		}
		if (event.getValue() == null) {
			throw new ParserException("Missing attribute value");
		}
					
		FormattedAttribute attribute = new FormattedAttribute();
		attribute.setType(event.getDTDType());
		attribute.setValue(event.getValue());
		attribute.setName(PolicyUtil.fromQName(event.getName()));
		
		// All attributes were already added on the XMLEvent.STARTELEMENT event.
		// This event allows us to verify the attribute was added correctly.
		
		if (!peekCurrentElement().hasAttribute(attribute)) {
			throw new ParserException("When verifying an element's attributes, an attribute was found to be missing");
		}
	}

	@Override
	public void handleTextual(Characters event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleTextual: " + event.getData());
		}
		
		if (document == null) {
			throw new ParserException("Characters encountered before document prolog");
		}
		
		if (event.getData() == null) {
			throw new ParserException("Missing text");
		}
		
		FormattedTextual text = new FormattedTextual(event.getData());
		
		FormattedElement parentElement;
		try {
			parentElement = (FormattedElement) peekCurrentElement();
			
			if (StringUtils.isBlank(StringUtils.normalizeSpace(event.getData()))) {
				return;
			}
			
			// The parser seems to sometimes return a body of text as two separate character events. Merge these.
			if (parentElement.getLastValue() instanceof FormattedTextual) {
				((FormattedTextual) parentElement.getLastValue()).merge(text.getText());
			}
			else {
				parentElement.addValue(text);
			}
		}
		catch (EmptyStackException exception) {
			// Is text allowed outside of an element?
			throw new ParserException("Encountered text outside of the root element.");
		}
	}

	@Override
	public void handleDTD(DTD event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleDTD: " + event.getDocumentTypeDeclaration());
		}
		
		if (document == null) {
			throw new ParserException("DTD encountered before document prolog");
		}
		
		if (event.getDocumentTypeDeclaration() == null) {
			throw new ParserException("Invalid DTD");
		}
		
		FormattedDTD d = new FormattedDTD(event.getDocumentTypeDeclaration());
		document.setDTD(d);
	}

	@Override
	public void handleEntityDeclaration(EntityDeclaration event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleEntityDeclaration: " + event.getName());
		}
		
		if (document == null) {
			throw new ParserException("Entity declaration encountered before document prolog");
		}
		
		throw new ParserException("Unimplemented feature");
	}

	@Override
	public void handleNotationDeclaration(NotationDeclaration event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleNotationDeclaration: " + event.getName());
		}
		
		if (document == null) {
			throw new ParserException("Notation declaration encountered before document prolog");
		}
		
		throw new ParserException("Unimplemented feature");
	}

	@Override
	public void handleNamespaceDeclaration(Namespace event) throws ParserException {
		if (DEBUG) {
			System.out.println("handleNamespaceDeclaration: " + event.getName());
		}
		
		if (document == null) {
			throw new ParserException("Namespace declaration encountered before document prolog");
		}
		
		throw new ParserException("Unimplemented feature");
	}
}

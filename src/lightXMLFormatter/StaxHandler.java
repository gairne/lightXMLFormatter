package lightXMLFormatter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

import javax.xml.namespace.QName;
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

import lightXMLFormatter.xml.FormattedCharacters;
import lightXMLFormatter.xml.FormattedComment;
import lightXMLFormatter.xml.FormattedAttribute;
import lightXMLFormatter.xml.FormattedDTD;
import lightXMLFormatter.xml.FormattedElement;
import lightXMLFormatter.xml.FormattedQName;
import lightXMLFormatter.xml.FormattedRoot;

public class StaxHandler {
	
	private Stack<FormattedElement> elementStack;
	private FormattedRoot documentRoot;

	public StaxHandler(String filename) throws FileNotFoundException, XMLStreamException {
		elementStack = new Stack<FormattedElement>();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader reader = factory.createXMLEventReader(filename, new FileInputStream(filename));
		
		while (reader.hasNext()) {
			XMLEvent e = reader.nextEvent();
			handleEvent(e);
		}
		
		System.out.println(documentRoot);
	}
	
	public static void main(String args[]) {
		try {
			StaxHandler s = new StaxHandler(args[1]);
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void handleEvent(XMLEvent e) {
		switch(e.getEventType()) {
		case XMLEvent.ATTRIBUTE:
			handleAttribute((Attribute) e);
			break;
		case XMLEvent.CDATA:
			handleCData((Characters) e);
			break;
		case XMLEvent.CHARACTERS:
			handleCharacters((Characters) e);
			break;
		case XMLEvent.COMMENT:
			handleComment((Comment) e);
			break;
		case XMLEvent.DTD:
			handleDTD((DTD) e);
			break;
		case XMLEvent.END_DOCUMENT:
			handleEndDocument((EndDocument) e);
			break;
		case XMLEvent.END_ELEMENT:
			handleEndElement((EndElement) e);
			break;
		case XMLEvent.ENTITY_DECLARATION:
			handleEntityDeclaration((EntityDeclaration) e);
			break;
		case XMLEvent.ENTITY_REFERENCE:
			handleEntityReference((EntityReference) e);
			break;
		case XMLEvent.NAMESPACE:
			handleNamespace((Namespace) e);
			break;
		case XMLEvent.NOTATION_DECLARATION:
			handleNotationDeclaration((NotationDeclaration) e);
			break;
		case XMLEvent.PROCESSING_INSTRUCTION:
			handleProcessingInstruction((ProcessingInstruction) e);
			break;
		case XMLEvent.SPACE:
			handleSpace((Characters) e);
			break;
		case XMLEvent.START_DOCUMENT:
			handleStartDocument((StartDocument) e);
			break;
		case XMLEvent.START_ELEMENT:
			handleStartElement((StartElement) e);
			break;
		}
	}
	
	private void handleStartElement(StartElement e) {
		ArrayList<FormattedAttribute> attrs = new ArrayList<FormattedAttribute>();
		@SuppressWarnings("unchecked")
		Iterator<Attribute> i = e.getAttributes();
		while (i.hasNext()) {
			Attribute a = i.next();
			QName aName = a.getName();
			attrs.add(new FormattedAttribute(FormattedQName.fromQName(aName), a.getValue(), a.getDTDType()));
		}
		
		QName eName = e.getName();
		
		if (documentRoot.getRoot() == null) {
			FormattedElement fe = new FormattedElement(FormattedQName.fromQName(eName), attrs);
			documentRoot.setRoot(fe);
			elementStack.add(fe);
		}
		else {
			elementStack.add(new FormattedElement(FormattedQName.fromQName(eName), attrs));
		}
	}
	
	private void handleEndElement(EndElement e) {
		FormattedElement fe = elementStack.pop();
		
		if (fe.getTag().equals(FormattedQName.fromQName(e.getName()))) {
			throw new IllegalStateException("Mismatch");
		}
		
		try {
			FormattedElement parent = elementStack.peek();
			parent.addChildValue(fe);
		}
		catch (EmptyStackException exception) {
			//fe is already a childvalue of the documentRoot
		}
		 
		return;
	}
	
	private void handleProcessingInstruction(ProcessingInstruction e) {
		System.out.println(e.getEventType() + " " + e.toString());
		throw new IllegalStateException("Unimplemented feature");
	}
	
	private void handleCharacters(Characters e) {
		if (e.getData().trim().length() == 0) {
			return;
		}
		FormattedCharacters chars = new FormattedCharacters(e.getData());
		try {
			FormattedElement parent = elementStack.peek();
			parent.addChildValue(chars);
		}
		catch (EmptyStackException exception) {
			documentRoot.getRoot().addChildValue(chars);
		}
		return;
	}
	
	private void handleComment(Comment e) {
		FormattedComment c = new FormattedComment(e.getText());

		try {
			FormattedElement parent = elementStack.peek();
			parent.addChildValue(c);
		}
		catch (EmptyStackException exception) {
			documentRoot.getRoot().addChildValue(c);
		}
	}
	
	private void handleSpace(Characters e) {
		System.out.println(e.getEventType() + " " + e.toString());
		throw new IllegalStateException("Unimplemented feature");
	}
	
	private void handleStartDocument(StartDocument e) {
		documentRoot = new FormattedRoot(e.getCharacterEncodingScheme(), e.getVersion());
		System.out.println(e.encodingSet());
		System.out.println(e.getCharacterEncodingScheme());
		System.out.println(e.getSystemId());
		System.out.println(e.getVersion());
		System.out.println(e.isStandalone());
		System.out.println(e.standaloneSet());
		System.out.println(e);
		return;
	}
	
	private void handleEndDocument(EndDocument e) {
		documentRoot.close();
		return;
	}
	
	private void handleEntityReference(EntityReference e) {
		System.out.println(e.getEventType() + " " + e.toString());
		return;
	}
	
	private void handleAttribute(Attribute e) {
		QName aName = e.getName();
		FormattedAttribute fa = new FormattedAttribute(FormattedQName.fromQName(aName), e.getValue(), e.getDTDType());
		if (!elementStack.peek().hasAttribute(fa)) {
			throw new IllegalStateException("Mismatch");
		}
	}
	
	private void handleDTD(DTD e) {
		FormattedDTD d = new FormattedDTD(e.getDocumentTypeDeclaration());
		documentRoot.setDtd(d);
		return;
	}
	
	private void handleCData(Characters e) {
		System.out.println(e.getEventType() + " " + e.toString());
		throw new IllegalStateException("Unimplemented feature");
	}
	
	private void handleNamespace(Namespace e) {
		System.out.println(e.getEventType() + " " + e.toString());
		throw new IllegalStateException("Unimplemented feature");
	}
	
	private void handleNotationDeclaration(NotationDeclaration e) {
		System.out.println(e.getEventType() + " " + e.toString());
		throw new IllegalStateException("Unimplemented feature");
	}
	
	private void handleEntityDeclaration(EntityDeclaration e) {
		System.out.println(e.getEventType() + " " + e.toString());
		throw new IllegalStateException("Unimplemented feature");
	}
}

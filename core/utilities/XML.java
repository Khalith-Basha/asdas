package core.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import core.utilities.exceptions.ExceptionHandler;
import core.utilities.exceptions.SetupException;

/**
 * The class represents the essence of the xml file and contains all the
 * corresponding methods.
 */
public class XML {
	/**
	 * Creates xml file in the specified path.
	 *
	 * @param filePath
	 *            the specified path to XML file
	 * @return {@link Document}
	 */
	public static Document parse(Path filePath) {
		try {
			return generateDocumentBuilder().parse(filePath.toFile());
		} catch (SAXException | IOException exception) {
			throw new SetupException(
					String.format("Failed to parse xml file to document object. File name: %s", filePath), exception);
		}
	}

	/**
	 * Creates xml file in the specified path.
	 *
	 * @param filePath
	 *            the specified path to XML file
	 * @param xsltPath
	 *            the specified path to XLST file
	 * @return {@link Document}
	 */
	public static Document parse(Path filePath, InputStream xsltPath) {
		try (FileInputStream fileInputStream = new FileInputStream(filePath.toFile())) {
			Transformer transformer = TransformerFactory.newInstance().newTemplates(new StreamSource(xsltPath))
					.newTransformer();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			transformer.transform(new StreamSource(fileInputStream), new DOMResult(document));

			return document;
		} catch (IOException | TransformerException | ParserConfigurationException exception) {
			throw new SetupException(
					String.format("Failed to parse xml file to document object. File name: %s", filePath), exception);
		}
	}

	/**
	 * Prepares xml file for parse.
	 *
	 * @param xmlFileContent
	 *            xml content
	 * @return {@link Document}
	 */
	public static Document parse(String xmlFileContent) {
		Document document;

		try {

			document = generateDocumentBuilder().parse(new InputSource(new StringReader(xmlFileContent)));

			// Normalizes text representation
			document.getDocumentElement().normalize();
		} catch (SAXException | IOException exception) {
			throw new SetupException("Failed to parse xml content to document object.", exception);
		}

		return document;
	}

	/**
	 * Removes tag form xml file on first level of root.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param tagName
	 *            intended for remove tag name
	 * @return {@link Document}
	 */
	public static Document removeTag(Document document, String tagName) {
		NodeList list = document.getDocumentElement().getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);

			if (node.getNodeName().equals(tagName)) {
				node.getParentNode().removeChild(node);
			}
		}

		return document;
	}

	/**
	 * Replaces value between tags on first level of root.s
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param newValues
	 *            map with data, where key - necessary tag, value - necessary
	 *            value
	 * @return {@link Document}
	 */
	public static Document replaceValues(Document document, Map<String, String> newValues) {
		NodeList list = document.getDocumentElement().getChildNodes();

		String nodeName;

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);

			nodeName = node.getNodeName();

			if (newValues.containsKey(nodeName)) {
				node.setTextContent(newValues.get(nodeName));
			}
		}

		return document;
	}

	/**
	 * Replaces text content of all occurrence of tags.
	 *
	 * @param document
	 *            {@link Document} with DOM structure
	 * @param tagValuePairs
	 *            map with data, where key - existing necessary tag, value - new
	 *            value
	 * @return updated document
	 */
	public static Document replaceTextContent(Document document, Map<String, String> tagValuePairs) {
		if (tagValuePairs == null || tagValuePairs.isEmpty()) {
			throw new SetupException(
					"Map with data is empty or null. Fill map with existing tags names as keys and replaceable text contents as values.");
		}

		for (String tag : tagValuePairs.keySet()) {
			if (document.getElementsByTagName(tag).item(0) == null) {
				throw new SetupException(String.format("There is no such tag \"%s\"", tag));
			}

			for (int i = 0; i < document.getElementsByTagName(tag).getLength(); i++) {
				document.getElementsByTagName(tag).item(i).setTextContent(tagValuePairs.get(tag));
			}
		}

		return document;
	}

	/**
	 * Sets attribute to element.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param elementId
	 *            id element
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 * @return {@link Attr}
	 */
	public static Attr setAttribute(Document document, String elementId, String attrName, String attrValue) {
		return document.getElementById(elementId).setAttributeNode(createAttribute(document, attrName, attrValue));
	}

	/**
	 * Creates element in DOM structure.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param element
	 *            Element in DOM structure
	 * @param elementName
	 *            element name
	 * @return {@link Element}
	 */
	public static Element createChildElement(Document document, Element element, String elementName) {
		return (Element) element.appendChild(document.createElement(elementName));
	}

	/**
	 * Creates node with text content in DOM structure.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param tagName
	 *            tag name
	 * @param value
	 *            text content
	 * @return {@link Node}
	 */
	public static Node createNode(Document document, String tagName, String value) {
		Node node = document.createElement(tagName);
		node.setTextContent(value);

		return node;
	}

	/**
	 * Creates attribute.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param attrName
	 *            attribute name
	 * @param value
	 *            attribute name
	 * @return {@link Attr}
	 */
	public static Attr createAttribute(Document document, String attrName, String value) {
		Attr attr = document.createAttribute(attrName);
		attr.setValue(value);

		return attr;
	}

	/**
	 * Adds child node to element in DOM structure.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param parentId
	 *            parent id
	 * @param tagName
	 *            tag name
	 * @param value
	 *            text content
	 * @return {@link Document}
	 */
	public static Document addChild(Document document, String parentId, String tagName, String value) {

		return addChild(document, parentId, tagName, value, null, null);
	}

	/**
	 * Adds child node to element with attribute in DOM structure.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param parentId
	 *            parent id
	 * @param tagName
	 *            tag name
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 * @param value
	 *            text content
	 * @return {@link Document}
	 */
	public static Document addChild(Document document, String parentId, String tagName, String value, String attrName,
			String attrValue) {
		Element child = document.createElement(tagName);
		child.setTextContent(value);

		if (attrName != null) {
			child.setAttribute(attrName, attrValue);
		}

		Element root = document.getElementById(parentId);
		root.appendChild(child);

		return document;
	}

	/**
	 * Adds child node to element in DOM structure.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param parentNode
	 *            parent {@link Node} element
	 * @param tagName
	 *            tag name
	 * @param value
	 *            text content
	 * @return {@link Document}
	 */
	public static Document addChild(Document document, Node parentNode, String tagName, String value) {
		Element child = document.createElement(tagName);
		child.setTextContent(value);

		parentNode.appendChild(child);

		return document;
	}

	/**
	 * Returns a Element in xml file order with a given id.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param id
	 *            element id
	 * @return specific Element object
	 */
	public static Element getElementsById(Document document, String id) {
		return document.getElementById(id);
	}

	/**
	 * Returns a NodeList in xml file order with a given tag name.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param id
	 *            element tag name
	 * @return specific Element object
	 */
	public static NodeList getElementsByTagName(Document document, String id) {
		return document.getElementsByTagName(id);
	}

	/**
	 * Gets nodes from DOM structure by XPath.
	 *
	 * @param document
	 *            {@link Document} with DOM structure}
	 * @param xpath
	 *            path expressions to select nodes
	 * @return {@link NodeList}
	 */
	public static NodeList getNodesByXPath(final Document document, final String xpath) {
		return ExceptionHandler.execute(new Callable<NodeList>() {
			@Override
			public NodeList call() throws Exception {
				return (NodeList) XPathFactory.newInstance().newXPath().compile(xpath).evaluate(document,
						XPathConstants.NODESET);
			}
		}, String.format("Failed to get nodes by XPath: %s", xpath));
	}

	/**
	 * Gets child nodes from DOM structure by XPath.
	 *
	 * @param node
	 *            parent node
	 * @param xpath
	 *            path expressions to select nodes
	 * @return {@link NodeList}
	 */
	public static NodeList getChildNodesByXPath(final Node node, final String xpath) {
		return ExceptionHandler.execute(new Callable<NodeList>() {
			@Override
			public NodeList call() throws Exception {
				return (NodeList) XPathFactory.newInstance().newXPath().evaluate(xpath, node, XPathConstants.NODESET);
			}
		}, String.format("Failed to get child nodes by XPath: %s", xpath));
	}

	/**
	 * Checks if tag exists in {@link Document}.
	 *
	 * @param document
	 *            {@link Document} element
	 * @param tagName
	 *            tag name
	 * @return true if tag with given name is exists
	 */
	public static boolean isExists(Document document, String tagName) {
		return document.getElementsByTagName(tagName).getLength() > 0;
	}

	/**
	 * Applies changes in DOM structure to XML file. Should call only after call
	 * "{@link #parse}";
	 *
	 * @param document
	 *            {@link Document} object
	 * @param xmlFilePath
	 *            {@link Path} to the xml file
	 */
	public static void saveToFile(Document document, Path xmlFilePath) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			try (FileOutputStream fileOutputStream = new FileOutputStream(xmlFilePath.toString())) {
				transformer.transform(new DOMSource(document), new StreamResult(fileOutputStream));
			}
		} catch (TransformerException | IOException exception) {
			throw new SetupException(String.format("Failed to apply changes to %s", xmlFilePath.toString()), exception);
		}
	}

	/**
	 * Generates document builder.
	 *
	 * @return document builder
	 */
	public static DocumentBuilder generateDocumentBuilder() {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(true);
			builderFactory.setIgnoringElementContentWhitespace(true);

			return builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException exception) {
			throw new SetupException("Failed to generate document builder.", exception);
		}
	}

	/**
	 * Creates a random UUID.
	 *
	 * @return unique id
	 */
	public static String generateUUID() {
		return "id" + UUID.randomUUID().toString();
	}
}

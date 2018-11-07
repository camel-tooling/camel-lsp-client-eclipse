/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cameltooling.lsp.reddeer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XPathEvaluator {

	public static final boolean DEFAULT_NAMESPACE_AWARE = false;

	public static final DocumentBuilderFactory DOC_FACTORY = DocumentBuilderFactory.newInstance();
	private final XPath xpath = XPathFactory.newInstance().newXPath();

	private Document doc;

	public XPathEvaluator(Reader reader) throws SAXException, IOException, ParserConfigurationException {
		this(reader, DEFAULT_NAMESPACE_AWARE);
	}

	public XPathEvaluator(Reader reader, boolean namespaceAware) throws SAXException, IOException, ParserConfigurationException {
		DOC_FACTORY.setNamespaceAware(namespaceAware);
		doc = DOC_FACTORY.newDocumentBuilder().parse(new InputSource(reader));
	}

	public boolean evaluateBoolean(String expr) {
		try {
			return (Boolean) xpath.evaluate(expr, doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			logXpathEvaluationError(e, expr);
			return false;
		}
	}

	private void logXpathEvaluationError(Exception e, String expr) {
		Activator.getInstance().getLog().log(new Status(IStatus.ERROR, Activator.PLUGINID, "Error evaluating xPath '" + expr + "'", e));
	}

	public String evaluateString(String expr) {
		try {
			return (String) xpath.evaluate(expr, doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			logXpathEvaluationError(e, expr);
			return null;
		}
	}

	public Node evaluateNode(String expr) {
		try {
			return (Node) xpath.evaluate(expr, doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			logXpathEvaluationError(e, expr);
			return null;
		}
	}

	public void printDocument(Result target) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), target);
	}

	@Override
	public String toString() {
		StringWriter writer = new StringWriter();
		try {
			printDocument(new StreamResult(writer));
		} catch (TransformerException e) {
			Activator.getInstance().getLog().log(new Status(IStatus.ERROR, Activator.PLUGINID, "Error printing Document", e));
		}
		return writer.toString();
	}
}

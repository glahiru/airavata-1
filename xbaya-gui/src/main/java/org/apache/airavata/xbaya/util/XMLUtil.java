/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.xbaya.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.airavata.xbaya.XBayaRuntimeException;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xmlpull.infoset.XmlDocument;
import org.xmlpull.infoset.XmlElement;
import org.xmlpull.infoset.XmlNamespace;
import org.xmlpull.mxp1.MXParserFactory;
import org.xmlpull.mxp1_serializer.MXSerializer;

import xsul5.MLogger;

public class XMLUtil {

    /**
     * The XML builder for XPP5
     */
    public static final org.xmlpull.infoset.XmlInfosetBuilder BUILDER = org.xmlpull.infoset.XmlInfosetBuilder
            .newInstance();

    /**
     * The XML builder for XPP3.
     */
    public static final org.xmlpull.v1.builder.XmlInfosetBuilder BUILDER3 = org.xmlpull.v1.builder.XmlInfosetBuilder
            .newInstance(new MXParserFactory());

    private static final MLogger logger = MLogger.getLogger();

    private final static String PROPERTY_SERIALIZER_INDENTATION = "http://xmlpull.org/v1/doc/properties.html#serializer-indentation";

    private final static String INDENT = "    ";

    /**
     * Parses a specified string and returns the XmlElement (XPP3).
     * 
     * @param string
     * @return The XmlElement (XPP3) parsed.
     */
    public static org.xmlpull.v1.builder.XmlElement stringToXmlElement3(String string) {
        return BUILDER3.parseFragmentFromReader(new StringReader(string));
    }

    /**
     * Parses a specified string and returns the XmlElement (XPP5).
     * 
     * @param string
     * @return The XmlElement (XPP5) parsed.
     */
    public static org.xmlpull.infoset.XmlElement stringToXmlElement(String string) {
        XmlDocument document = BUILDER.parseString(string);
        org.xmlpull.infoset.XmlElement element = document.getDocumentElement();
        return element;
    }

    /**
     * Converts a specified XmlElement (XPP3) to the XmlElement (XPP5).
     * 
     * @param element
     * @return The XmlElement (XPP5) converted.
     */
    public static org.xmlpull.infoset.XmlElement xmlElement3ToXmlElement5(org.xmlpull.v1.builder.XmlElement element) {
        String string = xmlElementToString(element);
        return stringToXmlElement(string);
    }

    /**
     * Converts a specified XmlElement (XPP5) to the XmlElement (XPP3).
     * 
     * @param element
     * @return The XmlElement (XPP3) converted.
     */
    public static org.xmlpull.v1.builder.XmlElement xmlElement5ToXmlElement3(org.xmlpull.infoset.XmlElement element) {
        String string = xmlElementToString(element);
        return stringToXmlElement3(string);
    }

    /**
     * Returns the XML string of a specified XmlElement.
     * 
     * @param element
     *            The specified XmlElement
     * @return The XML string
     */
    public static String xmlElementToString(org.xmlpull.v1.builder.XmlElement element) {
        MXSerializer serializer = new MXSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);
        serializer.setProperty(PROPERTY_SERIALIZER_INDENTATION, INDENT);
        BUILDER3.serialize(element, serializer);
        String xmlText = writer.toString();
        return xmlText;
    }

    /**
     * Returns the XML string as a specified XmlElement (XPP5).
     * 
     * @param element
     *            The specified XmlElement
     * @return The XML string
     */
    public static String xmlElementToString(org.xmlpull.infoset.XmlElement element) {
        String string;
        if (element == null) {
            string = "";
        } else {
            string = BUILDER.serializeToStringPretty(element);
        }
        return string;
    }

    /**
     * Converts a specified XPP5 XML element to a DOM element under a specified document.
     * 
     * @param xppElement
     * @param document
     * @return The converted DOM element.
     */
    public static Element xppElementToDomElement(org.xmlpull.infoset.XmlElement xppElement, Document document) {
        Element domElement = document.createElement(xppElement.getName());

        for (org.xmlpull.infoset.XmlNamespace namespace : xppElement.namespaces()) {
            logger.finest("namespace: " + namespace);
            // TODO
        }

        for (org.xmlpull.infoset.XmlAttribute attribute : xppElement.attributes()) {
            // TODO namespace
            domElement.setAttribute(attribute.getName(), attribute.getValue());
        }

        for (Object object : xppElement.children()) {
            if (object instanceof org.xmlpull.infoset.XmlElement) {
                domElement.appendChild(xppElementToDomElement((org.xmlpull.infoset.XmlElement) object, document));
            } else if (object instanceof String) {
                Text text = document.createTextNode((String) object);
                domElement.appendChild(text);
            } else {
                logger.finest("object.getClass(): " + object.getClass());
            }
        }
        return domElement;
    }

    /**
     * @param definitions3
     * @return The WsdlDefinitions (XSUL5)
     */
    @Deprecated
    public static xsul5.wsdl.WsdlDefinitions wsdlDefinitions3ToWsdlDefintions5(xsul.wsdl.WsdlDefinitions definitions3) {
        return WSDLUtil.wsdlDefinitions3ToWsdlDefintions5(definitions3);
    }

    /**
     * @param definitions5
     * @return The WsdlDefinitions (XSUL3)
     */
    @Deprecated
    public static xsul.wsdl.WsdlDefinitions wsdlDefinitions5ToWsdlDefintions3(xsul5.wsdl.WsdlDefinitions definitions5) {
        return WSDLUtil.wsdlDefinitions5ToWsdlDefintions3(definitions5);
    }

    /**
     * Converts a specified XPP3 XML element to a DOM element under a specified document.
     * 
     * @param xppElement
     * @param document
     * @return The converted DOM element.
     */
    public static Element xppElementToDomElement(org.xmlpull.v1.builder.XmlElement xppElement, Document document) {
        Element domElement = document.createElement(xppElement.getName());

        Iterator nsIt = xppElement.namespaces();
        while (nsIt.hasNext()) {
            org.xmlpull.v1.builder.XmlNamespace namespace = (org.xmlpull.v1.builder.XmlNamespace) nsIt.next();
            logger.finest("namespace: " + namespace);
            // TODO
        }

        Iterator attrIt = xppElement.attributes();
        while (attrIt.hasNext()) {
            org.xmlpull.v1.builder.XmlAttribute attribute = (org.xmlpull.v1.builder.XmlAttribute) attrIt.next();
            // TODO namespace
            domElement.setAttribute(attribute.getName(), attribute.getValue());
        }

        Iterator elementIt = xppElement.children();
        while (elementIt.hasNext()) {
            Object object = elementIt.next();
            if (object instanceof org.xmlpull.v1.builder.XmlElement) {
                domElement.appendChild(xppElementToDomElement((org.xmlpull.v1.builder.XmlElement) object, document));
            } else if (object instanceof String) {
                Text text = document.createTextNode((String) object);
                domElement.appendChild(text);
            } else {
                logger.finest("object.getClass(): " + object.getClass());
            }
        }
        return domElement;
    }

    /**
     * @param element
     * @return The cloned XmlElement.
     */
    public static org.xmlpull.infoset.XmlElement deepClone(org.xmlpull.infoset.XmlElement element) {
        try {
            XmlElement clonedElement = element.clone();
            clonedElement.setParent(null);
            return clonedElement;
        } catch (CloneNotSupportedException e) {
            // This should not happen because we don't put any special Objects.
            throw new XBayaRuntimeException(e);
        }
    }

    /**
     * Saves a specified XmlElement to a specified file.
     * 
     * @param element
     * @param file
     * @throws IOException
     */
    public static void saveXML(org.xmlpull.infoset.XmlElement element, File file) throws IOException {
        XmlDocument document = BUILDER.newDocument();
        document.setDocumentElement(element);
        String xmlText = BUILDER.serializeToStringPretty(document);
        IOUtil.writeToFile(xmlText, file);
    }

    /**
     * Saves a specified XmlElement to a specified file.
     * 
     * @param element
     * @param file
     * @throws IOException
     */
    public static void saveXML(org.xmlpull.v1.builder.XmlElement element, File file) throws IOException {
        saveXML(xmlElement3ToXmlElement5(element), file);
    }

    /**
     * @param file
     * @return The XmlElement in the document.
     * @throws IOException
     */
    public static org.xmlpull.infoset.XmlElement loadXML(File file) throws IOException {
        String xmlText = IOUtil.readFileToString(file);
        XmlDocument document = BUILDER.parseString(xmlText);
        return document.getDocumentElement();
    }

    /**
     * @param string
     * @return true if the specified string is XML, false otherwise
     */
    public static boolean isXML(String string) {
        try {
            stringToXmlElement(string);
            return true;
        } catch (RuntimeException e) {
            logger.caught(e);
            return false;
        }
    }

    /**
     * Validates a specified XmlObject along with logging errors if any.
     * 
     * @param xmlObject
     */
    public static void validate(XmlObject xmlObject) {
        XmlOptions validateOptions = new XmlOptions();
        ArrayList errorList = new ArrayList();
        validateOptions.setErrorListener(errorList);

        boolean isValid = xmlObject.validate(validateOptions);
        if (isValid) {
            // Valid
            return;
        }

        // Error
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < errorList.size(); i++) {
            XmlError error = (XmlError) errorList.get(i);
            logger.warning("Message: " + error.getMessage());
            logger.warning("Location of invalid XML: " + error.getCursorLocation().xmlText());
            stringBuilder.append("Message:" + error.getMessage());
            stringBuilder.append("Location of invalid XML: " + error.getCursorLocation().xmlText());
        }
        throw new XBayaRuntimeException(stringBuilder.toString());
    }

    /**
     * Returns the local part of a specified QName.
     * 
     * @param qname
     *            the specified QName in string, e.g. ns:value
     * @return the local part of the QName, e.g. value
     */
    public static String getLocalPartOfQName(String qname) {
        int index = qname.indexOf(':');
        if (index < 0) {
            return qname;
        } else {
            return qname.substring(index + 1);
        }
    }

    /**
     * Returns the prefix of a specified QName.
     * 
     * @param qname
     *            the specified QName in string, e.g. ns:value
     * @return the prefix of the QName, e.g. ns
     */
    public static String getPrefixOfQName(String qname) {
        int index = qname.indexOf(':');
        if (index < 0) {
            return null;
        } else {
            return qname.substring(0, index);
        }
    }

    /**
     * @param prefixCandidate
     * @param uri
     * @param alwaysUseSuffix
     * @param element
     * @return The namespace found or declared.
     */
    public static XmlNamespace declareNamespaceIfNecessary(String prefixCandidate, String uri, boolean alwaysUseSuffix,
            XmlElement element) {
        XmlNamespace namespace = element.lookupNamespaceByName(uri);
        if (namespace == null) {
            return declareNamespace(prefixCandidate, uri, alwaysUseSuffix, element);
        } else {
            return namespace;
        }
    }

    /**
     * @param prefixCandidate
     * @param uri
     * @param alwaysUseSuffix
     * @param element
     * @return The namespace declared.
     */
    public static XmlNamespace declareNamespace(String prefixCandidate, String uri, boolean alwaysUseSuffix,
            XmlElement element) {
        if (prefixCandidate == null || prefixCandidate.length() == 0) {
            prefixCandidate = "a";
        }
        String prefix = prefixCandidate;
        if (alwaysUseSuffix) {
            prefix += "0";
        }
        if (element.lookupNamespaceByPrefix(prefix) != null) {
            int i = 1;
            prefix = prefixCandidate + i;
            while (element.lookupNamespaceByPrefix(prefix) != null) {
                i++;
            }
        }
        XmlNamespace namespace = element.declareNamespace(prefix, uri);
        return namespace;
    }

    /**
     * @param xml
     * @param string
     */
    public static void removeElements(XmlElement xml, String name) {

        Iterable<XmlElement> removeElements = xml.elements(null, name);
        LinkedList<XmlElement> removeList = new LinkedList<XmlElement>();
        for (XmlElement xmlElement : removeElements) {
            removeList.add(xmlElement);
        }
        for (XmlElement xmlElement : removeList) {
            xml.removeChild(xmlElement);
        }
        Iterable children = xml.children();
        for (Object object : children) {
            if (object instanceof XmlElement) {
                XmlElement element = (XmlElement) object;
                removeElements(element, name);
            }
        }
    }

    /**
     * @param url
     * @return Document
     */
    public static Document retrievalXMLDocFromUrl(String url) {
        try {
            URL xmlUrl = new URL(url);
            InputStream in = xmlUrl.openStream();
            Document ret = null;
            DocumentBuilderFactory domFactory;
            DocumentBuilder builder;

            domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setValidating(false);
            domFactory.setNamespaceAware(false);
            builder = domFactory.newDocumentBuilder();

            ret = builder.parse(in);

            return ret;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param url
     * @return Document
     */
    public static Document retrievalXMLDocForParse(String url) {
        try {
            URL xmlUrl = new URL(url);
            InputStream in = xmlUrl.openStream();
            DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
            xmlFact.setNamespaceAware(true);
            DocumentBuilder builder = xmlFact.newDocumentBuilder();
            Document doc = builder.parse(in);

            return doc;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

}
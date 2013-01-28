/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.guibuilder.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.deepnighttwo.ui.guibuilder.EditorSection;
import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.ui.guibuilder.componets.ColorPicker;
import com.deepnighttwo.ui.guibuilder.componets.Mandelbrot;
import com.deepnighttwo.ui.guibuilder.componets.MultiSelection;
import com.deepnighttwo.ui.guibuilder.componets.SingleCheck;
import com.deepnighttwo.ui.guibuilder.componets.SpinnerDouble;
import com.deepnighttwo.ui.guibuilder.componets.SpinnerInt;
import com.deepnighttwo.ui.guibuilder.componets.TextLine;
import com.deepnighttwo.util.ReflectUtility;

public class XMLGUIBuilder {

    private static HashMap<String, Class<?>> CMP_MAPPING = new HashMap<String, Class<?>>();

    static {
        CMP_MAPPING.put("SpinnerInt", SpinnerInt.class);
        CMP_MAPPING.put("SpinnerDouble", SpinnerDouble.class);
        CMP_MAPPING.put("SingleCheck", SingleCheck.class);
        CMP_MAPPING.put("MultiSelection", MultiSelection.class);
        CMP_MAPPING.put("ColorPicker", ColorPicker.class);
        CMP_MAPPING.put("TextLine", TextLine.class);
        CMP_MAPPING.put("MandelbrotPanel", Mandelbrot.class);
    }

    public static List<EditorSection> parseGUIXML(String path) {
        List<EditorSection> sections = new ArrayList<EditorSection>();
        InputStream xml = XMLGUIBuilder.class.getClassLoader().getResourceAsStream(path);
        Document doc = null;

        try {
            // read XML doc, build DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docb = dbf.newDocumentBuilder();
            doc = docb.parse(xml);
            Element root = doc.getDocumentElement();
            // Load Bean Class instance. Bean class instance will be used to
            // store value on GUI. Set value will be done by reflect.
            Class<?> beanClazz = getBeanClass(root.getElementsByTagName("beanclass"));

            // Process each property node.
            NodeList sectionsNode = root.getElementsByTagName("section");
            for (int secCur = 0; secCur < sectionsNode.getLength(); secCur++) {
                Node sectionNode = sectionsNode.item(secCur);
                EditorSection section = new EditorSection();
                section.setSectionLabel(sectionNode.getAttributes().getNamedItem("sectionLabel").getNodeValue());
                section.setProps(processSection(sectionNode, beanClazz));
                sections.add(section);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return sections;
    }

    private static List<PropertyLine> processSection(Node sectionNode, Class<?> beanClazz)
            throws ReflectSetValueException {
        List<PropertyLine> props = new ArrayList<PropertyLine>();
        for (Node prop = sectionNode.getFirstChild(); prop != null; prop = prop.getNextSibling()) {
            // Ignore text node in property children node.
            if (prop.getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            // Get all attribute from property node.
            NamedNodeMap attrs = prop.getAttributes();
            String componentType = "componentType";
            // Find component class in CMP_MAPPING, instance of component
            // class will be used to construct GUI.
            Class<?> cmpClazz = CMP_MAPPING.get(attrs.getNamedItem(componentType).getNodeValue());
            // Create component instance
            PropertyLine cmpInst;
            try {
                cmpInst = (PropertyLine) cmpClazz.newInstance();
            } catch (InstantiationException e) {
                throw new ReflectSetValueException(e);
            } catch (IllegalAccessException e) {
                throw new ReflectSetValueException(e);
            }
            // Set attribute values to component instance
            for (int attrCur = 0; attrCur < attrs.getLength(); attrCur++) {
                String attrName = attrs.item(attrCur).getNodeName();
                if (componentType.equals(attrName)) {
                    continue;
                }
                String attrValue = attrs.item(attrCur).getNodeValue();
                ReflectUtility.setValueReflect(cmpClazz, cmpInst, attrName, attrValue);
            }
            // Set beanClass property to component instance
            ReflectUtility.setValueReflect(cmpClazz, cmpInst, "beanClass", beanClazz);
            for (Node propSpec = prop.getFirstChild(); propSpec != null; propSpec = propSpec.getNextSibling()) {
                // Ignore text node in property children node.
                short nodeType = propSpec.getNodeType();
                if (nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE || nodeType == Node.COMMENT_NODE) {
                    continue;
                }
                String propName = propSpec.getNodeName();
                String propValue = propSpec.getFirstChild().getNodeValue();
                ReflectUtility.setValueReflect(cmpClazz, cmpInst, propName, propValue);
            }
            props.add(cmpInst);
        }
        return props;
    }

    private static Class<?> getBeanClass(NodeList clazzList) throws GUIXMLInvalidateException, ClassNotFoundException {
        if (clazzList.getLength() == 0) {
            throw new GUIXMLInvalidateException("No Bean Class Tag found!");
        }
        Node clazzNode = clazzList.item(0).getFirstChild();
        String clazz = clazzNode.getNodeValue().trim();
        return Class.forName(clazz);
    }

}

package org.yuriak.util;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil {
		SAXReader saxReader=null;
		Document document=null;
	public XmlUtil(String filePath){
        try {
        	saxReader = new SAXReader();
			document = saxReader.read(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getConfigValue(String key){
		Element root=document.getRootElement();
		return (String) root.element(key).getData();
	}
}

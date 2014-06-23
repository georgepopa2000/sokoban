package ro.pagepo.sokoban.levels;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ro.pagepo.sokoban.database.LevelsDataSource;
import ro.pagepo.sokoban.database.model.Level;
import ro.pagepo.sokoban.database.model.LevelsPack;
import ro.pagepo.sokoban.levels.exception.InvalidXMLLevelPackException;

public class ParseXMLLevelsPack {
	Document document;
	LevelsDataSource datasource;
	LevelsPack lp;
	ArrayList<Level> listLevels;
	
	private ParseXMLLevelsPack(InputStream is){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(is);
			document.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.listLevels = new ArrayList<Level>();
		
	}
	
	public static ParseXMLLevelsPack parseDocument(InputStream is) throws InvalidXMLLevelPackException{
		ParseXMLLevelsPack plv = new ParseXMLLevelsPack(is);
		plv.startParseXML();
		return plv;
	}
	
	/**
	 * starts parsing of the XML file and saves the data in lp LevelsPack and listLevels 
	 * @throws InvalidXMLLevelPackException if the file has an incorrect structure
	 */
	private void startParseXML() throws InvalidXMLLevelPackException{
		NodeList nl = document.getElementsByTagName("SokobanLevels");
		if (nl.getLength() == 0) throw new InvalidXMLLevelPackException("XML Should contain a <SokobanLevels> tag");
		Element elementSokobanLevels = (Element) nl.item(0);
		nl = elementSokobanLevels.getElementsByTagName("Title");
		if (nl.getLength() == 0) throw new InvalidXMLLevelPackException("XML Should contain a <Title> tag");
		lp = new LevelsPack(nl.item(0).getTextContent());
		
		nl = elementSokobanLevels.getElementsByTagName("LevelCollection");
		if (nl.getLength() == 0) throw new InvalidXMLLevelPackException("XML Should contain a <LevelCollection> tag");
		
		Element elementLevelCollection = (Element) nl.item(0);
		
		nl = elementLevelCollection.getElementsByTagName("Level");
		for (int i = 0; i < nl.getLength(); i++) {
			Level lvl = parseLevelFromNode(nl.item(i));
			listLevels.add(lvl);
		}		
	}
	
	
	/**
	 * parses a level from an XML node
	 * @param n - the node that contain the level
	 * @return an instance of Level with values from the node
	 */
	private Level parseLevelFromNode(Node n){
		
		Element el = (Element)n;	
		int width = Integer.parseInt(el.getAttribute("Width"));
		int height = Integer.parseInt(el.getAttribute("Height"));
		String name = el.getAttribute("Id");
		NodeList nl = el.getElementsByTagName("L");
		String content = "";
		for (int i = 0; i < nl.getLength(); i++) {
			Element line = (Element)nl.item(i);
			content+=extendStringToSize(line.getTextContent(),width);
		}
		Level lvl = new Level();
		lvl.setContent(content);
		lvl.setName(name);
		lvl.setHeight(height);
		lvl.setWidth(width);
		return lvl;
	}
	
	/**
	 * add spaces to a string to match a given length
	 * @param str - the string to add spaces to
	 * @param size - the new size of the string
	 * @return a string with the length equal size with spaces
	 */
	private String extendStringToSize(String str,int size){
		String newStr = new String(str);
		while (newStr.length()<size) newStr+=" ";
		return newStr;
	}

	public LevelsPack getLevelsPack() {
		return lp;
	}

	public ArrayList<Level> getListLevels() {
		return listLevels;
	}
	
	
	
	
}

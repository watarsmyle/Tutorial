package utility.file.xml.reader;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class MyXmlReader {

	public Node getRootNode(String targetPath) throws SAXException, IOException, ParserConfigurationException {
		Node ret = null;

		Path path = Paths.get(targetPath);

		// ファイルの存在、種別チェック
		if(path == null){
			throw new IllegalArgumentException("引数にパスを指定してください。");
		}
		if(Files.exists(path) == false){
			throw new FileNotFoundException("指定されたパス [" + targetPath + "] は存在しません。");
		}
		if(Files.isDirectory(path)){
			throw new IllegalArgumentException("指定されたパス [" + targetPath + "] はファイルではありません。");
		}
		if(getExtension(targetPath).equals("xml")){
			throw new IllegalArgumentException("xmlファイルを指定してください。");
		}

		// ドキュメントビルダの生成
		DocumentBuilderFactory factory =
				  DocumentBuilderFactory.newInstance();

		// 検証を行いたい場合、有効
		//factory.setValidating(true);

		// rootノードの取得
		DocumentBuilder builder = factory.newDocumentBuilder();
		ret = builder.parse(Files.newInputStream(path));

		return ret;
	}

	public boolean existAttributeValue(NamedNodeMap attribute, String attName, String attVal) {
		String val = attribute.getNamedItem(attName).getNodeValue();
		return (attVal.equals(val));
	}

	public String getAttributes(NamedNodeMap attribute, String attName) {
		String ret = null;

		Node val = attribute.getNamedItem(attName);
		ret = val.getNodeValue().toString();

		return ret;
	}

	/**
	 * ファイル名から拡張子を返します。
	 * @param fileName ファイル名
	 * @return ファイルの拡張子
	 */
	private String getExtension(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(point + 1);
	    }
	    return fileName;
	}
}

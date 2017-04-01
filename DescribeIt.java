import org.w3c.dom.*;
import javax.xml.xpath.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.*;
import javax.xml.parsers.*;
import java.lang.Object;
import java.io.*;
import java.nio.charset.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import com.drew.imaging.*;
import com.drew.metadata.*;
import com.drew.metadata.iptc.*;
public class DescribeIt
{
	public static void main (String[] kzp)
	{
		try
		{
			//add root tag to non well-formed xml file
			String filename = "tempIn.xml";
			DumpFile(filename,  "<root>" + ReadWholeFile("session.xml") +"</root>");
			
			//parse document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filename);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			//loop over picture entries
			NodeList nl = doc.getElementsByTagName("map");

			for(int i=0;i<nl.getLength();i++)
			{
				Node mapNode = nl.item(i);
				if(mapNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					//get description and path
					Element firstElement = (Element)mapNode;                              
					NodeList pathList = firstElement.getElementsByTagName("path");
					NodeList descList = firstElement.getElementsByTagName("desc");
					System.out.println(pathList.item(0).getTextContent());
					Element pathElement = (Element) pathList.item(0);
					
					//get image metadata
					File jpegFile = new File(pathList.item(0).getTextContent());
					Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
					for (Directory metaDirectory : metadata.getDirectories()) 
					{
						for (Tag tag : metaDirectory.getTags()) 
						{
							if(tag.toString().startsWith("[IPTC] Caption"))
							{
								//set tag content as description in XML
								descList.item(0).setTextContent(tag.getDescription());
								break;
							}
						}
					}
				}
			}
			//Save as temp file
			String fileOut = "tempOut.xml";
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File(fileOut));
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
			
			//remove root tags
			DumpFile("output.xml", ReadWholeFile(fileOut).replace("<root>","").replace("</root>", ""));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	public static void DumpFile(String filename, String content)
	{
		BufferedWriter writer = null;
		try
		{
		
		writer = new BufferedWriter(  new OutputStreamWriter(  new FileOutputStream(filename),
     Charset.forName("UTF-8").newEncoder()));
			writer.write(content);
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch(IOException ioe)
			{
				
			}
		}
	}
	
	public static String ReadWholeFile(String filename) throws IOException
	{
		Path path = FileSystems.getDefault().getPath(".", filename);
		return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
	}
}
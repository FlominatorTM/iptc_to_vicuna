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
		String FILE_INITIAL = "session.xml";
		String FILE_INITIAL_WITH_ROOT = "tempIn.xml";
		String FILE_RESULT_WITH_ROOT ="tempOut.xml";
		String FILE_RESULT ="output.xml";
		try
		{
			//add root tag to non well-formed xml file
			DumpFile(FILE_INITIAL_WITH_ROOT,  "<root>" + ReadWholeFile(FILE_INITIAL) +"</root>");
			
			//parse document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(FILE_INITIAL_WITH_ROOT);
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
					
					Element pathElement = (Element) pathList.item(0);
					System.out.println(pathList.item(0).getTextContent());
					//get image metadata
					File jpegFile = new File(pathList.item(0).getTextContent());
					Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
					
					Boolean found = false;
					for (Directory metaDirectory : metadata.getDirectories()) 
					{
						for (Tag tag : metaDirectory.getTags()) 
						{
							if(tag.toString().startsWith("[IPTC] Caption"))
							{
								//set tag content as description in XML
								String descr = tag.getDescription();
								descList.item(0).setTextContent(descr);
								System.out.println("=> " + descr);
								found = true;
								break;
							}
						}
						if(found) break;
					}
				}
			}
			//Save as temp file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File(FILE_RESULT_WITH_ROOT));
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
			
			//remove root tags
			DumpFile(FILE_RESULT, ReadWholeFile(FILE_RESULT_WITH_ROOT).replace("<root>","").replace("</root>", ""));
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
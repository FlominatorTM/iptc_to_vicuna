# Insert IPTC descriptions into VicuñaUploader session file

This is a proof of concept class that shows how one can manipulate a session file saved in [VicuñaUploader](http://yarl.github.io/vicuna/). In this case one field of [IPTC](https://en.wikipedia.org/wiki/IPTC_Information_Interchange_Model) data that might have been insertedby using a tool like [GeoSetter](http://www.geosetter.de/) gets retrieved from the files and inserted in the description field of the session file.

Installation
--------------
Since I didn't find the correct command line to run my class with a jar file yet, the installation is a little hacky:

1. Download [metadata-extractor-2.9.1.zip](https://github-cloud.s3.amazonaws.com/releases/26836459/3706e91c-0755-11e6-9929-0f7a828a7177.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAISTNZFOVBIJMK3TQ%2F20170331%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20170331T190753Z&X-Amz-Expires=300&X-Amz-Signature=66b5d9b7355976dfab39e958426babeddd3d7b02f00ddbae41f62cf68f460a62&X-Amz-SignedHeaders=host&actor_id=12448283&response-content-disposition=attachment%3B%20filename%3Dmetadata-extractor-2.9.1.zip&response-content-type=application%2Foctet-stream) which is currently the last version of [metadata-extractor](https://github.com/drewnoakes/metadata-extractor) that was released in binary form
2. Extract the zip file and the jar files inside (e.g. with 7zip) and put the content into the same folder as DescribeIt.class from this project (there should be a folder "com" now)

Usage
-----
1. Open the pictures in VicuñaUploader
2. Save the session as file "session.xml" in the same folder as DescribeIt.class
3. Run the class file (in a happy day scenario this should work via run.bat)
4. Load the result file "output.xml" in VicuñaUploader 

How it works
------------
1. It adds a root tag to session.xml (because of settings and list don't have a parent node required for parsing)
2. It parses session.xml and retrieves all map tags (one map equals one image)
3. Within a map tag it retrieves the tags description and path 
4. Path is used to locate and open the image file
5. Metadata of the image file is retrieved
6. If the meta data field starting with "[IPTC] Caption" is found, the value is written into the description tag retrieved before
7. After all map tags are processed the updated xml file is saved
8. The root tags are removed again

Compiling
---------
1. Clone this project
2. Install some Java development kit
3. Put the bin folder of it into your path environment variable or add it to the commands java and javac in build_and_run.bat
4. Get and unzip the metadata-extractor file mentioned in seciton "Installation" (you should now have to additional jar files in the folder)
5. Run build_and_run.bat

Components used
---------------
* [metadata-extractor](https://github.com/drewnoakes/metadata-extractor) by [Drew Noakes](https://github.com/drewnoakes)
* [VicuñaUploader](https://github.com/yarl/vicuna) by [Paweł Marynowski](https://github.com/yarl)



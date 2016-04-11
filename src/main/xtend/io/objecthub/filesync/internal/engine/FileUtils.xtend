package io.objecthub.filesync.internal.engine

import io.objecthub.filesync.FileOperation
import io.objecthub.filesync.FileOperationContext
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.internal.engine.metadata.MetadataImpl
import io.objecthub.filesync.internal.engine.metadata.MetadataUtilsJre
import de.mxro.file.FileItem
import java.util.List

class FileUtils {

	def saveMetadata(FileItem forFolder, Metadata metadata) {
		MetadataUtilsJre.saveToFile(metadata, forFolder.get(".filesync-meta").get("nodes.xml"))
	}

	def hasMetadata(FileItem forFolder) {

		forFolder.assertFolder(".filesync-meta").get("nodes.xml").exists
	}

	def assertMetadata(FileItem forFolder) {
		val metadataFolder = forFolder.assertFolder(".filesync-meta")

		metadataFolder.visible = false;

		if (!metadataFolder.get("nodes.xml").exists) {
			val metadataFile = metadataFolder.createFile("nodes.xml")
			val metadata = new MetadataImpl
			MetadataUtilsJre.saveToFile(metadata, metadataFile)
			return metadata
		}

		loadMetadata(forFolder)
	}
	
	def loadMetadata(FileItem forFolder) {
		MetadataUtilsJre.readFromFile(forFolder.get(".filesync-meta").get("nodes.xml"))
	}

	def execute(List<FileOperation> operations, FileItem withFolder, Metadata withMetadata) {

		val ctx = new FileOperationContext() {

			override folder() {
				withFolder
			}

			override metadata() {
				withMetadata
			}

		}

		for (op : operations) {
			op.apply(ctx)

		}
	}

	/**
     * based on http://grepcode.com/file_/repository.springsource.com/org.apache.activemq/com.springsource.org.apache.kahadb/5.3.0/org/apache/kahadb/util/IOHelper.java/?v=source
     * 
     * Converts any string into a string that is safe to use as a file name.
     * The result will only include ascii characters and numbers, and the "-","_", and "." characters.
     *
     * @param name
     * @param dirSeparators 
     * @param maxFileLength 
     * @return
     */
	def toFileSystemSafeName(String name, boolean dirSeparators, int maxFileLength) {
		val size = name.length();
		val StringBuffer rc = new StringBuffer(size * 2);
		for (i : 0 ..< size) {
			val char c = name.charAt(i);
			var boolean valid = c >= 'a' && c <= 'z';
			valid = valid || (c >= 'A' && c <= 'Z');
			valid = valid || (c >= '0' && c <= '9');
			valid = valid || (c == '_'.charAt(0)) || (c == '-'.charAt(0)) || (c == '.'.charAt(0)) || (c == '#'.charAt(0)) ||
				(dirSeparators && ( (c == '/'.charAt(0)) || (c == '\\'.charAt(0))));
			valid = valid || (c == ' '.charAt(0));

			if (valid) {
				rc.append(c);
			} else {
				rc.append("_")

			//rc.append('#');
			//rc.append(HexSupport.toHexFromInt(c, true));
			}
		}
		var result = rc.toString();
		if (result.length() > maxFileLength) {
			result = result.substring(0, maxFileLength-1);
		}
		return result;
	}
	
	def  String getExtension(String name) {

        val int idx = name.lastIndexOf(".");
        if (idx < 0) {
            return "";
        }

        return name.substring(idx + 1);
    }
    
    /**
	 * from http://stackoverflow.com/questions/941272/how-do-i-trim-a-file-
	 * extension-from-a-string-in-java
	 * 
	 * @param s
	 * @return
	 */
	def String removeExtension(String s) {
		val String works = s + "";
		val String separator = "/"; // System.getProperty("file.separator");

		var String filename;

		// Remove the path up to the filename.
		val int lastSeparatorIndex = works.replaceAll("\\\\", "/")
				.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		val int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return s;

		return s.substring(0, lastSeparatorIndex + 1)
				+ filename.substring(0, extensionIndex);
	}
	
	def String getSimpleName(String forName) {
		val String n = forName;
		if (n.length() > 0) {
			var simple = "";
			for (i : 0..<n.length()) {
				var boolean found = isSimpleCharacter(n
						.charAt(i));
				if (found) {
					simple = simple + n.charAt(i);
				} else {
					simple = simple + '_';
				}
			}
			return simple;
		} else
			return n;
	}
	
	static val char[] allowedCharacters = #{ 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '_', '-', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '0' }

    /**
     * Returns true if the given character is a 'standard' character. (a-z,
     * A-Z).
     * 
     * @param character
     * @return
     */
    def static boolean isSimpleCharacter( char character) {
        var boolean found = false;
        for ( element : allowedCharacters) {
            found = found || character == element
                    || character == Character.toUpperCase(element);
        }
        return found;
    }

}

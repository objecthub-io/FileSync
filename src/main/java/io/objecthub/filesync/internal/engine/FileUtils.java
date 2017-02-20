package io.objecthub.filesync.internal.engine;

import de.mxro.file.FileItem;
import io.objecthub.filesync.FileOperation;
import io.objecthub.filesync.FileOperationContext;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.internal.engine.metadata.MetadataImpl;
import io.objecthub.filesync.internal.engine.metadata.MetadataUtilsJre;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;

@SuppressWarnings("all")
public class FileUtils {
  public void saveMetadata(final FileItem forFolder, final Metadata metadata) {
    MetadataUtilsJre.saveToFile(metadata, forFolder.get(".filesync-meta").get("nodes.xml"));
  }
  
  public boolean hasMetadata(final FileItem forFolder) {
    return forFolder.assertFolder(".filesync-meta").get("nodes.xml").exists();
  }
  
  public Metadata assertMetadata(final FileItem forFolder) {
    Metadata _xblockexpression = null;
    {
      final FileItem metadataFolder = forFolder.assertFolder(".filesync-meta");
      metadataFolder.setVisible(false);
      boolean _exists = metadataFolder.get("nodes.xml").exists();
      boolean _not = (!_exists);
      if (_not) {
        final FileItem metadataFile = metadataFolder.createFile("nodes.xml");
        final MetadataImpl metadata = new MetadataImpl();
        MetadataUtilsJre.saveToFile(metadata, metadataFile);
        return metadata;
      }
      _xblockexpression = this.loadMetadata(forFolder);
    }
    return _xblockexpression;
  }
  
  public Metadata loadMetadata(final FileItem forFolder) {
    return MetadataUtilsJre.readFromFile(forFolder.get(".filesync-meta").get("nodes.xml"));
  }
  
  public void execute(final List<FileOperation> operations, final FileItem withFolder, final Metadata withMetadata) {
    final FileOperationContext ctx = new FileOperationContext() {
      @Override
      public FileItem folder() {
        return withFolder;
      }
      
      @Override
      public Metadata metadata() {
        return withMetadata;
      }
    };
    for (final FileOperation op : operations) {
      op.apply(ctx);
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
  public String toFileSystemSafeName(final String name, final boolean dirSeparators, final int maxFileLength) {
    final int size = name.length();
    final StringBuffer rc = new StringBuffer((size * 2));
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, size, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final char c = name.charAt((i).intValue());
        boolean valid = ((c >= 'a') && (c <= 'z'));
        valid = (valid || ((c >= 'A') && (c <= 'Z')));
        valid = (valid || ((c >= '0') && (c <= '9')));
        valid = (((((valid || (c == "_".charAt(0))) || (c == "-".charAt(0))) || (c == ".".charAt(0))) || (c == "#".charAt(0))) || (dirSeparators && ((c == "/".charAt(0)) || (c == "\\".charAt(0)))));
        valid = (valid || (c == " ".charAt(0)));
        if (valid) {
          rc.append(c);
        } else {
          rc.append("_");
        }
      }
    }
    String result = rc.toString();
    int _length = result.length();
    boolean _greaterThan = (_length > maxFileLength);
    if (_greaterThan) {
      result = result.substring(0, (maxFileLength - 1));
    }
    return result;
  }
  
  public String getExtension(final String name) {
    final int idx = name.lastIndexOf(".");
    if ((idx < 0)) {
      return "";
    }
    return name.substring((idx + 1));
  }
  
  /**
   * from http://stackoverflow.com/questions/941272/how-do-i-trim-a-file-
   * extension-from-a-string-in-java
   * 
   * @param s
   * @return
   */
  public String removeExtension(final String s) {
    final String works = (s + "");
    final String separator = "/";
    String filename = null;
    final int lastSeparatorIndex = works.replaceAll("\\\\", "/").lastIndexOf(separator);
    if ((lastSeparatorIndex == (-1))) {
      filename = s;
    } else {
      filename = s.substring((lastSeparatorIndex + 1));
    }
    final int extensionIndex = filename.lastIndexOf(".");
    if ((extensionIndex == (-1))) {
      return s;
    }
    String _substring = s.substring(0, (lastSeparatorIndex + 1));
    String _substring_1 = filename.substring(0, extensionIndex);
    return (_substring + _substring_1);
  }
  
  public String getSimpleName(final String forName) {
    final String n = forName;
    int _length = n.length();
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      String simple = "";
      int _length_1 = n.length();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length_1, true);
      for (final Integer i : _doubleDotLessThan) {
        {
          boolean found = FileUtils.isSimpleCharacter(
            n.charAt((i).intValue()));
          if (found) {
            char _charAt = n.charAt((i).intValue());
            String _plus = (simple + Character.valueOf(_charAt));
            simple = _plus;
          } else {
            simple = (simple + "_");
          }
        }
      }
      return simple;
    } else {
      return n;
    }
  }
  
  private final static char[] allowedCharacters = ((char[])Conversions.unwrapArray(Collections.<Character>unmodifiableSet(CollectionLiterals.<Character>newHashSet(Character.valueOf('a'), Character.valueOf('b'), Character.valueOf('c'), Character.valueOf('d'), Character.valueOf('e'), Character.valueOf('f'), Character.valueOf('g'), Character.valueOf('h'), Character.valueOf('i'), Character.valueOf('j'), Character.valueOf('k'), Character.valueOf('l'), Character.valueOf('m'), Character.valueOf('n'), Character.valueOf('o'), Character.valueOf('p'), Character.valueOf('q'), Character.valueOf('r'), Character.valueOf('s'), Character.valueOf('t'), Character.valueOf('u'), Character.valueOf('v'), Character.valueOf('w'), Character.valueOf('x'), Character.valueOf('y'), Character.valueOf('z'), Character.valueOf('_'), Character.valueOf('-'), Character.valueOf('1'), Character.valueOf('2'), Character.valueOf('3'), Character.valueOf('4'), Character.valueOf('5'), Character.valueOf('6'), Character.valueOf('7'), Character.valueOf('8'), Character.valueOf('9'), Character.valueOf('0'))), char.class));
  
  /**
   * Returns true if the given character is a 'standard' character. (a-z,
   * A-Z).
   * 
   * @param character
   * @return
   */
  public static boolean isSimpleCharacter(final char character) {
    boolean found = false;
    for (final char element : FileUtils.allowedCharacters) {
      found = ((found || (character == element)) || (character == Character.toUpperCase(element)));
    }
    return found;
  }
}

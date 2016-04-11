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
    FileItem _get = forFolder.get(".filesync-meta");
    FileItem _get_1 = _get.get("nodes.xml");
    MetadataUtilsJre.saveToFile(metadata, _get_1);
  }
  
  public boolean hasMetadata(final FileItem forFolder) {
    FileItem _assertFolder = forFolder.assertFolder(".filesync-meta");
    FileItem _get = _assertFolder.get("nodes.xml");
    return _get.exists();
  }
  
  public Metadata assertMetadata(final FileItem forFolder) {
    Metadata _xblockexpression = null;
    {
      final FileItem metadataFolder = forFolder.assertFolder(".filesync-meta");
      metadataFolder.setVisible(false);
      FileItem _get = metadataFolder.get("nodes.xml");
      boolean _exists = _get.exists();
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
    FileItem _get = forFolder.get(".filesync-meta");
    FileItem _get_1 = _get.get("nodes.xml");
    return MetadataUtilsJre.readFromFile(_get_1);
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
        boolean _or = false;
        boolean _or_1 = false;
        boolean _or_2 = false;
        boolean _or_3 = false;
        boolean _or_4 = false;
        if (valid) {
          _or_4 = true;
        } else {
          char _charAt = "_".charAt(0);
          boolean _equals = (c == _charAt);
          _or_4 = _equals;
        }
        if (_or_4) {
          _or_3 = true;
        } else {
          char _charAt_1 = "-".charAt(0);
          boolean _equals_1 = (c == _charAt_1);
          _or_3 = _equals_1;
        }
        if (_or_3) {
          _or_2 = true;
        } else {
          char _charAt_2 = ".".charAt(0);
          boolean _equals_2 = (c == _charAt_2);
          _or_2 = _equals_2;
        }
        if (_or_2) {
          _or_1 = true;
        } else {
          char _charAt_3 = "#".charAt(0);
          boolean _equals_3 = (c == _charAt_3);
          _or_1 = _equals_3;
        }
        if (_or_1) {
          _or = true;
        } else {
          boolean _and = false;
          if (!dirSeparators) {
            _and = false;
          } else {
            boolean _or_5 = false;
            char _charAt_4 = "/".charAt(0);
            boolean _equals_4 = (c == _charAt_4);
            if (_equals_4) {
              _or_5 = true;
            } else {
              char _charAt_5 = "\\".charAt(0);
              boolean _equals_5 = (c == _charAt_5);
              _or_5 = _equals_5;
            }
            _and = _or_5;
          }
          _or = _and;
        }
        valid = _or;
        boolean _or_6 = false;
        if (valid) {
          _or_6 = true;
        } else {
          char _charAt_6 = " ".charAt(0);
          boolean _equals_6 = (c == _charAt_6);
          _or_6 = _equals_6;
        }
        valid = _or_6;
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
      String _substring = result.substring(0, (maxFileLength - 1));
      result = _substring;
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
    String _replaceAll = works.replaceAll("\\\\", "/");
    final int lastSeparatorIndex = _replaceAll.lastIndexOf(separator);
    if ((lastSeparatorIndex == (-1))) {
      filename = s;
    } else {
      String _substring = s.substring((lastSeparatorIndex + 1));
      filename = _substring;
    }
    final int extensionIndex = filename.lastIndexOf(".");
    if ((extensionIndex == (-1))) {
      return s;
    }
    String _substring_1 = s.substring(0, (lastSeparatorIndex + 1));
    String _substring_2 = filename.substring(0, extensionIndex);
    return (_substring_1 + _substring_2);
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
          char _charAt = n.charAt((i).intValue());
          boolean found = FileUtils.isSimpleCharacter(_charAt);
          if (found) {
            char _charAt_1 = n.charAt((i).intValue());
            String _plus = (simple + Character.valueOf(_charAt_1));
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
      boolean _or = false;
      if ((found || (character == element))) {
        _or = true;
      } else {
        char _upperCase = Character.toUpperCase(element);
        boolean _equals = (character == _upperCase);
        _or = _equals;
      }
      found = _or;
    }
    return found;
  }
}

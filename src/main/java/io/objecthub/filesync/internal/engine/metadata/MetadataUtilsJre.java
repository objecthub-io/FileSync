package io.objecthub.filesync.internal.engine.metadata;

import com.google.common.base.Objects;
import com.thoughtworks.xstream.XStream;
import de.mxro.file.FileItem;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.internal.engine.metadata.MetadataImpl;
import io.objecthub.filesync.internal.engine.metadata.v01.ItemXml;
import io.objecthub.filesync.internal.engine.metadata.v01.NodesXml;
import java.util.List;

@SuppressWarnings("all")
public class MetadataUtilsJre {
  public static Metadata readFromFile(final FileItem file) {
    Metadata _xblockexpression = null;
    {
      boolean _exists = file.exists();
      boolean _not = (!_exists);
      if (_not) {
        return null;
      }
      final XStream xstream = new XStream();
      Object _fromXML = xstream.fromXML(file.getText());
      final NodesXml nodesXml = ((NodesXml) _fromXML);
      _xblockexpression = MetadataUtilsJre.toMetadata(nodesXml);
    }
    return _xblockexpression;
  }
  
  public static Metadata toMetadata(final NodesXml nodesXml) {
    MetadataImpl _xblockexpression = null;
    {
      final MetadataImpl metadata = new MetadataImpl();
      for (final ItemXml item : nodesXml.items) {
        metadata.add(item);
      }
      metadata.setValue(nodesXml.value);
      _xblockexpression = metadata;
    }
    return _xblockexpression;
  }
  
  public static void saveToFile(final Metadata metadata, final FileItem file) {
    boolean _exists = file.exists();
    boolean _not = (!_exists);
    if (_not) {
      throw new RuntimeException("File doesn\'t exist.");
    }
    final XStream xstream = new XStream();
    final NodesXml nodesXml = MetadataUtilsJre.toNodesXml(metadata);
    file.setText(xstream.toXML(nodesXml));
  }
  
  public static NodesXml toNodesXml(final Metadata metadata) {
    NodesXml _xblockexpression = null;
    {
      NodesXml nodesXml = new NodesXml();
      List<ItemMetadata> _children = metadata.getChildren();
      for (final ItemMetadata item : _children) {
        nodesXml.items.add(MetadataUtilsJre.toItemXml(item));
      }
      nodesXml.value = MetadataUtilsJre.toItemXml(metadata.value());
      _xblockexpression = nodesXml;
    }
    return _xblockexpression;
  }
  
  public static ItemXml toItemXml(final ItemMetadata item) {
    ItemXml _xblockexpression = null;
    {
      boolean _equals = Objects.equal(item, null);
      if (_equals) {
        return null;
      }
      final ItemXml itemXml = new ItemXml();
      itemXml.converter = item.converter();
      itemXml.lastModified = item.lastModified();
      itemXml.hash = item.hash();
      itemXml.name = item.name();
      itemXml.uri = item.uri();
      _xblockexpression = itemXml;
    }
    return _xblockexpression;
  }
}

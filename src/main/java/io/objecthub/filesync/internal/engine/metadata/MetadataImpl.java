package io.objecthub.filesync.internal.engine.metadata;

import com.appjangle.api.Node;
import com.google.common.base.Objects;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.internal.engine.metadata.v01.ItemXml;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class MetadataImpl implements Metadata {
  private final List<ItemMetadata> items;
  
  private ItemMetadata value;
  
  public MetadataImpl() {
    this.items = CollectionLiterals.<ItemMetadata>newArrayList();
  }
  
  @Override
  public List<ItemMetadata> getChildren() {
    return this.items;
  }
  
  @Override
  public ItemMetadata get(final String name) {
    final Function1<ItemMetadata, Boolean> _function = new Function1<ItemMetadata, Boolean>() {
      @Override
      public Boolean apply(final ItemMetadata it) {
        String _name = it.name();
        return Boolean.valueOf(Objects.equal(_name, name));
      }
    };
    return IterableExtensions.<ItemMetadata>findFirst(this.items, _function);
  }
  
  @Override
  public ItemMetadata get(final Node forNode) {
    final Function1<ItemMetadata, Boolean> _function = new Function1<ItemMetadata, Boolean>() {
      @Override
      public Boolean apply(final ItemMetadata it) {
        String _uri = it.uri();
        String _uri_1 = forNode.uri();
        return Boolean.valueOf(Objects.equal(_uri, _uri_1));
      }
    };
    return IterableExtensions.<ItemMetadata>findFirst(this.items, _function);
  }
  
  @Override
  public Metadata add(final ItemMetadata itemMetadata) {
    MetadataImpl _xblockexpression = null;
    {
      this.items.add(this.toXml(itemMetadata));
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  @Override
  public Metadata setValue(final ItemMetadata itemMetadata) {
    MetadataImpl _xblockexpression = null;
    {
      this.value = itemMetadata;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  @Override
  public Metadata update(final ItemMetadata itemMetadata) {
    MetadataImpl _xblockexpression = null;
    {
      final int foundIdx = this.indexOfChild(itemMetadata.name());
      if ((foundIdx == (-1))) {
        String _name = itemMetadata.name();
        String _plus = ("Cannot update child which is not defined: " + _name);
        throw new RuntimeException(_plus);
      }
      this.items.set(foundIdx, this.toXml(itemMetadata));
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  @Override
  public Metadata remove(final String name) {
    MetadataImpl _xblockexpression = null;
    {
      final int foundIdx = this.indexOfChild(name);
      if ((foundIdx == (-1))) {
        throw new RuntimeException(("Cannot remove child which is not defined: " + name));
      }
      this.items.remove(foundIdx);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  private ItemXml toXml(final ItemMetadata itemMetadata) {
    ItemXml _xblockexpression = null;
    {
      final ItemXml item = new ItemXml();
      item.name = itemMetadata.name();
      item.uri = itemMetadata.uri();
      item.lastModified = itemMetadata.lastModified();
      item.hash = itemMetadata.hash();
      item.converter = itemMetadata.converter();
      _xblockexpression = item;
    }
    return _xblockexpression;
  }
  
  private int indexOfChild(final String name) {
    int _xblockexpression = (int) 0;
    {
      int foundIdx = (-1);
      int _size = this.items.size();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
      for (final Integer i : _doubleDotLessThan) {
        {
          final ItemMetadata item = this.items.get((i).intValue());
          String _name = item.name();
          boolean _equals = Objects.equal(_name, name);
          if (_equals) {
            foundIdx = (i).intValue();
          }
        }
      }
      _xblockexpression = foundIdx;
    }
    return _xblockexpression;
  }
  
  @Override
  public ItemMetadata value() {
    return this.value;
  }
}

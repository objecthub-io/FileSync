package io.objecthub.filesync.internal.engine.convert;

import com.appjangle.api.Node;
import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Closure2;
import io.objecthub.filesync.Converter;
import io.objecthub.filesync.FileOperation;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.internal.engine.convert.ConvertUtils;
import io.objecthub.filesync.internal.engine.convert.FolderToNode;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ConverterCollection implements Converter {
  private final List<Converter> converters;
  
  public ConverterCollection() {
    ArrayList<Converter> _arrayList = new ArrayList<Converter>();
    this.converters = _arrayList;
  }
  
  public boolean addConverter(final Converter converter) {
    return this.converters.add(converter);
  }
  
  @Override
  public boolean worksOn(final FileItem source) {
    boolean _xblockexpression = false;
    {
      boolean res = false;
      for (final Converter c : this.converters) {
        res = (res || c.worksOn(source));
      }
      _xblockexpression = res;
    }
    return _xblockexpression;
  }
  
  @Override
  public void worksOn(final Node node, final ValueCallback<Boolean> cb) {
    final Closure2<Converter, ValueCallback<Boolean>> _function = new Closure2<Converter, ValueCallback<Boolean>>() {
      @Override
      public void apply(final Converter c, final ValueCallback<Boolean> itmcb) {
        c.worksOn(node, itmcb);
      }
    };
    final Closure<List<Boolean>> _function_1 = new Closure<List<Boolean>>() {
      @Override
      public void apply(final List<Boolean> res) {
        boolean _contains = res.contains(Boolean.valueOf(true));
        cb.onSuccess(Boolean.valueOf(_contains));
      }
    };
    ValueCallback<List<Boolean>> _embed = AsyncCommon.<List<Boolean>>embed(cb, _function_1);
    AsyncCommon.<Converter, Boolean>forEach(this.converters, _function, _embed);
  }
  
  private void findConverter(final FileItem forFileItem, final ValueCallback<Converter> cb) {
    final Closure2<Converter, ValueCallback<Object>> _function = new Closure2<Converter, ValueCallback<Object>>() {
      @Override
      public void apply(final Converter c, final ValueCallback<Object> itmcb) {
        boolean _worksOn = c.worksOn(forFileItem);
        if (_worksOn) {
          itmcb.onSuccess(c);
        } else {
          itmcb.onSuccess(ConvertUtils.NO_VALUE);
        }
      }
    };
    final Closure<List<Object>> _function_1 = new Closure<List<Object>>() {
      @Override
      public void apply(final List<Object> res) {
        for (final Object item : res) {
          if ((item instanceof Converter)) {
            cb.onSuccess(((Converter)item));
            return;
          }
        }
        Exception _exception = new Exception(("Cannot find converter for " + forFileItem));
        cb.onFailure(_exception);
      }
    };
    ValueCallback<List<Object>> _embed = AsyncCommon.<List<Object>>embed(cb, _function_1);
    AsyncCommon.<Converter, Object>forEach(this.converters, _function, _embed);
  }
  
  private void findConverter(final ItemMetadata forItem, final ValueCallback<Converter> cb) {
    for (final Converter c : this.converters) {
      String _id = c.id();
      String _converter = forItem.converter();
      boolean _equals = _id.equals(_converter);
      if (_equals) {
        cb.onSuccess(c);
        return;
      }
    }
    String _converter_1 = forItem.converter();
    String _plus = (((("Cannot find converter for [" + forItem) + "].\n") + 
      "  Required Converter: ") + _converter_1);
    String _plus_1 = (_plus + "\n");
    String _plus_2 = (_plus_1 + 
      "  Defined Converters: ");
    String _plus_3 = (_plus_2 + this.converters);
    throw new RuntimeException(_plus_3);
  }
  
  private void findConverter(final Node forNode, final ValueCallback<Converter> cb) {
    final Closure2<Converter, ValueCallback<Object>> _function = new Closure2<Converter, ValueCallback<Object>>() {
      @Override
      public void apply(final Converter c, final ValueCallback<Object> itmcb) {
        final Closure<Boolean> _function = new Closure<Boolean>() {
          @Override
          public void apply(final Boolean res) {
            if ((res).booleanValue()) {
              itmcb.onSuccess(c);
            } else {
              itmcb.onSuccess(ConvertUtils.NO_VALUE);
            }
          }
        };
        ValueCallback<Boolean> _embed = AsyncCommon.<Boolean>embed(itmcb, _function);
        c.worksOn(forNode, _embed);
      }
    };
    final Closure<List<Object>> _function_1 = new Closure<List<Object>>() {
      @Override
      public void apply(final List<Object> res) {
        for (final Object item : res) {
          if ((item instanceof Converter)) {
            cb.onSuccess(((Converter)item));
            return;
          }
        }
        Exception _exception = new Exception(("Cannot find converter for " + forNode));
        cb.onFailure(_exception);
      }
    };
    ValueCallback<List<Object>> _embed = AsyncCommon.<List<Object>>embed(cb, _function_1);
    AsyncCommon.<Converter, Object>forEach(this.converters, _function, _embed);
  }
  
  @Override
  public void createNodes(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    final Closure<Converter> _function = new Closure<Converter>() {
      @Override
      public void apply(final Converter converter) {
        converter.createNodes(metadata, source, cb);
      }
    };
    ValueCallback<Converter> _embed = AsyncCommon.<Converter>embed(cb, _function);
    this.findConverter(source, _embed);
  }
  
  @Override
  public void update(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    String _name = source.getName();
    ItemMetadata _get = metadata.get(_name);
    final Closure<Converter> _function = new Closure<Converter>() {
      @Override
      public void apply(final Converter converter) {
        converter.update(metadata, source, cb);
      }
    };
    ValueCallback<Converter> _embed = AsyncCommon.<Converter>embed(cb, _function);
    this.findConverter(_get, _embed);
  }
  
  @Override
  public void deleteNodes(final Metadata metadata, final ItemMetadata cachedFile, final ValueCallback<List<NetworkOperation>> cb) {
    final Closure<Converter> _function = new Closure<Converter>() {
      @Override
      public void apply(final Converter converter) {
        converter.deleteNodes(metadata, cachedFile, cb);
      }
    };
    ValueCallback<Converter> _embed = AsyncCommon.<Converter>embed(cb, _function);
    this.findConverter(cachedFile, _embed);
  }
  
  @Override
  public void createFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    String _uri = source.uri();
    boolean _endsWith = _uri.endsWith("Types");
    if (_endsWith) {
      FolderToNode _folderToNode = new FolderToNode();
      _folderToNode.createFiles(folder, metadata, source, cb);
      return;
    }
    final Closure<Converter> _function = new Closure<Converter>() {
      @Override
      public void apply(final Converter converter) {
        converter.createFiles(folder, metadata, source, cb);
      }
    };
    ValueCallback<Converter> _embed = AsyncCommon.<Converter>embed(cb, _function);
    this.findConverter(source, _embed);
  }
  
  @Override
  public void updateFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    ItemMetadata _get = metadata.get(source);
    final Closure<Converter> _function = new Closure<Converter>() {
      @Override
      public void apply(final Converter converter) {
        converter.updateFiles(folder, metadata, source, cb);
      }
    };
    ValueCallback<Converter> _embed = AsyncCommon.<Converter>embed(cb, _function);
    this.findConverter(_get, _embed);
  }
  
  @Override
  public void removeFiles(final FileItem folder, final Metadata metadata, final ItemMetadata item, final ValueCallback<List<FileOperation>> cb) {
    final Closure<Converter> _function = new Closure<Converter>() {
      @Override
      public void apply(final Converter converter) {
        converter.removeFiles(folder, metadata, item, cb);
      }
    };
    ValueCallback<Converter> _embed = AsyncCommon.<Converter>embed(cb, _function);
    this.findConverter(item, _embed);
  }
  
  @Override
  public String id() {
    return "collection";
  }
}

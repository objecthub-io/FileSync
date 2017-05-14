package io.objecthub.filesync.internal.engine;

import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.async.helper.Aggregator;
import delight.functional.Closure;
import delight.functional.collections.CollectionsUtils;
import io.objecthub.filesync.Converter;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.SyncParams;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Determines operations performed on local files which need to be uploaded to the cloud.
 */
@SuppressWarnings("all")
public class FileToNetworkOperations {
  private final SyncParams params;
  
  private final Metadata metadata;
  
  public FileToNetworkOperations(final SyncParams params, final Metadata metadata) {
    this.params = params;
    this.metadata = metadata;
  }
  
  public void determineOps(final ValueCallback<List<NetworkOperation>> cb) {
    try {
      FileItem _folder = this.params.getFolder();
      boolean _isDirectory = _folder.isDirectory();
      boolean _not = (!_isDirectory);
      if (_not) {
        FileItem _folder_1 = this.params.getFolder();
        String _plus = ("File passed and not directory. " + _folder_1);
        throw new Exception(_plus);
      }
      FileItem _folder_2 = this.params.getFolder();
      boolean _exists = _folder_2.exists();
      boolean _not_1 = (!_exists);
      if (_not_1) {
        FileItem _folder_3 = this.params.getFolder();
        String _plus_1 = ("File passed does not exist. " + _folder_3);
        throw new Exception(_plus_1);
      }
      FileItem _folder_4 = this.params.getFolder();
      Iterable<String> locallyAddedFiles = FileToNetworkOperations.determineLocallyAddedFiles(this.metadata, _folder_4);
      FileItem _folder_5 = this.params.getFolder();
      final ArrayList<String> locallyRemovedFiles = FileToNetworkOperations.determineLocallyRemovedFiles(this.metadata, _folder_5);
      FileItem _folder_6 = this.params.getFolder();
      final ArrayList<String> locallyChangedFiles = FileToNetworkOperations.determineLocallyChangedFiles(this.metadata, _folder_6);
      final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
        @Override
        public void apply(final List<List<NetworkOperation>> res) {
          final List<NetworkOperation> ops = CollectionsUtils.<NetworkOperation>flatten(res);
          cb.onSuccess(ops);
        }
      };
      ValueCallback<List<List<NetworkOperation>>> _embed = AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function);
      final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(3, _embed);
      ValueCallback<List<NetworkOperation>> _createCallback = agg.createCallback();
      this.createOperationsFromRemovedFiles(locallyRemovedFiles, _createCallback);
      ValueCallback<List<NetworkOperation>> _createCallback_1 = agg.createCallback();
      this.createOperationsFromChangedFiles(locallyChangedFiles, _createCallback_1);
      ValueCallback<List<NetworkOperation>> _createCallback_2 = agg.createCallback();
      this.createOperationsFromCreatedFiles(locallyAddedFiles, _createCallback_2);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void createOperationsFromChangedFiles(final List<String> fileNames, final ValueCallback<List<NetworkOperation>> cb) {
    int _size = fileNames.size();
    final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
      @Override
      public void apply(final List<List<NetworkOperation>> res) {
        List<NetworkOperation> _flatten = CollectionsUtils.<NetworkOperation>flatten(res);
        cb.onSuccess(_flatten);
      }
    };
    ValueCallback<List<List<NetworkOperation>>> _embed = AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function);
    final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(_size, _embed);
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String fileName) {
        Converter _converter = FileToNetworkOperations.this.params.getConverter();
        FileItem _folder = FileToNetworkOperations.this.params.getFolder();
        FileItem _get = _folder.get(fileName);
        ValueCallback<List<NetworkOperation>> _createCallback = agg.createCallback();
        _converter.update(FileToNetworkOperations.this.metadata, _get, _createCallback);
      }
    };
    fileNames.forEach(_function_1);
  }
  
  public void createOperationsFromRemovedFiles(final List<String> fileNames, final ValueCallback<List<NetworkOperation>> cb) {
    int _size = fileNames.size();
    final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
      @Override
      public void apply(final List<List<NetworkOperation>> res) {
        List<NetworkOperation> _flatten = CollectionsUtils.<NetworkOperation>flatten(res);
        cb.onSuccess(_flatten);
      }
    };
    ValueCallback<List<List<NetworkOperation>>> _embed = AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function);
    final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(_size, _embed);
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String fileName) {
        Converter _converter = FileToNetworkOperations.this.params.getConverter();
        ItemMetadata _get = FileToNetworkOperations.this.metadata.get(fileName);
        ValueCallback<List<NetworkOperation>> _createCallback = agg.createCallback();
        _converter.deleteNodes(FileToNetworkOperations.this.metadata, _get, _createCallback);
      }
    };
    fileNames.forEach(_function_1);
  }
  
  public void createOperationsFromCreatedFiles(final Iterable<String> fileNames, final ValueCallback<List<NetworkOperation>> cb) {
    int _size = IterableExtensions.size(fileNames);
    final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
      @Override
      public void apply(final List<List<NetworkOperation>> res) {
        List<NetworkOperation> _flatten = CollectionsUtils.<NetworkOperation>flatten(res);
        cb.onSuccess(_flatten);
      }
    };
    ValueCallback<List<List<NetworkOperation>>> _embed = AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function);
    final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(_size, _embed);
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String fileName) {
        Converter _converter = FileToNetworkOperations.this.params.getConverter();
        FileItem _folder = FileToNetworkOperations.this.params.getFolder();
        FileItem _get = _folder.get(fileName);
        ValueCallback<List<NetworkOperation>> _createCallback = agg.createCallback();
        _converter.createNodes(FileToNetworkOperations.this.metadata, _get, _createCallback);
      }
    };
    fileNames.forEach(_function_1);
  }
  
  public static ArrayList<String> determineLocallyChangedFiles(final Metadata metadata, final FileItem folder) {
    ArrayList<String> _xblockexpression = null;
    {
      final ArrayList<String> res = new ArrayList<String>(0);
      List<ItemMetadata> _children = metadata.getChildren();
      for (final ItemMetadata fileMetadata : _children) {
        {
          String _name = fileMetadata.name();
          final FileItem itemNow = folder.get(_name);
          boolean _exists = itemNow.exists();
          if (_exists) {
            Date _lastModified = itemNow.lastModified();
            long _time = _lastModified.getTime();
            Date _lastModified_1 = fileMetadata.lastModified();
            long _time_1 = _lastModified_1.getTime();
            boolean _greaterThan = (_time > _time_1);
            if (_greaterThan) {
              String _name_1 = itemNow.getName();
              res.add(_name_1);
            }
          }
        }
      }
      _xblockexpression = res;
    }
    return _xblockexpression;
  }
  
  public static ArrayList<String> determineLocallyAddedFiles(final Metadata metadata, final FileItem folder) {
    List<ItemMetadata> _children = metadata.getChildren();
    final ArrayList<String> previousNames = FileToNetworkOperations.getNamesFromCache(_children);
    List<FileItem> _children_1 = folder.getChildren();
    final ArrayList<String> currentNames = FileToNetworkOperations.getNames(_children_1);
    currentNames.removeAll(previousNames);
    return currentNames;
  }
  
  public static ArrayList<String> determineLocallyRemovedFiles(final Metadata metadata, final FileItem folder) {
    List<ItemMetadata> _children = metadata.getChildren();
    final ArrayList<String> previousNames = FileToNetworkOperations.getNamesFromCache(_children);
    List<FileItem> _children_1 = folder.getChildren();
    final ArrayList<String> currentNames = FileToNetworkOperations.getNames(_children_1);
    previousNames.removeAll(currentNames);
    return previousNames;
  }
  
  public static ArrayList<String> getNamesFromCache(final List<ItemMetadata> cachedChildren) {
    int _size = cachedChildren.size();
    final ArrayList<String> res = new ArrayList<String>(_size);
    for (final ItemMetadata fileItemMetaData : cachedChildren) {
      String _name = fileItemMetaData.name();
      res.add(_name);
    }
    return res;
  }
  
  public static ArrayList<String> getNames(final List<FileItem> cachedChildren) {
    int _size = cachedChildren.size();
    final ArrayList<String> res = new ArrayList<String>(_size);
    for (final FileItem fileItem : cachedChildren) {
      String _name = fileItem.getName();
      res.add(_name);
    }
    return res;
  }
}

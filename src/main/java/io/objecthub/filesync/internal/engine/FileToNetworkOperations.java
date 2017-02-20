package io.objecthub.filesync.internal.engine;

import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.async.helper.Aggregator;
import delight.functional.Closure;
import delight.functional.collections.CollectionsUtils;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.SyncParams;
import java.util.ArrayList;
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
      boolean _isDirectory = this.params.getFolder().isDirectory();
      boolean _not = (!_isDirectory);
      if (_not) {
        FileItem _folder = this.params.getFolder();
        String _plus = ("File passed and not directory. " + _folder);
        throw new Exception(_plus);
      }
      boolean _exists = this.params.getFolder().exists();
      boolean _not_1 = (!_exists);
      if (_not_1) {
        FileItem _folder_1 = this.params.getFolder();
        String _plus_1 = ("File passed does not exist. " + _folder_1);
        throw new Exception(_plus_1);
      }
      Iterable<String> locallyAddedFiles = FileToNetworkOperations.determineLocallyAddedFiles(this.metadata, this.params.getFolder());
      final ArrayList<String> locallyRemovedFiles = FileToNetworkOperations.determineLocallyRemovedFiles(this.metadata, this.params.getFolder());
      final ArrayList<String> locallyChangedFiles = FileToNetworkOperations.determineLocallyChangedFiles(this.metadata, this.params.getFolder());
      final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
        @Override
        public void apply(final List<List<NetworkOperation>> res) {
          final List<NetworkOperation> ops = CollectionsUtils.<NetworkOperation>flatten(res);
          cb.onSuccess(ops);
        }
      };
      final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(3, 
        AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function));
      this.createOperationsFromRemovedFiles(locallyRemovedFiles, agg.createCallback());
      this.createOperationsFromChangedFiles(locallyChangedFiles, agg.createCallback());
      this.createOperationsFromCreatedFiles(locallyAddedFiles, agg.createCallback());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void createOperationsFromChangedFiles(final List<String> fileNames, final ValueCallback<List<NetworkOperation>> cb) {
    final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
      @Override
      public void apply(final List<List<NetworkOperation>> res) {
        cb.onSuccess(CollectionsUtils.<NetworkOperation>flatten(res));
      }
    };
    final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(fileNames.size(), 
      AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function));
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String fileName) {
        FileToNetworkOperations.this.params.getConverter().update(FileToNetworkOperations.this.metadata, FileToNetworkOperations.this.params.getFolder().get(fileName), agg.createCallback());
      }
    };
    fileNames.forEach(_function_1);
  }
  
  public void createOperationsFromRemovedFiles(final List<String> fileNames, final ValueCallback<List<NetworkOperation>> cb) {
    final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
      @Override
      public void apply(final List<List<NetworkOperation>> res) {
        cb.onSuccess(CollectionsUtils.<NetworkOperation>flatten(res));
      }
    };
    final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(fileNames.size(), 
      AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function));
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String fileName) {
        FileToNetworkOperations.this.params.getConverter().deleteNodes(FileToNetworkOperations.this.metadata, FileToNetworkOperations.this.metadata.get(fileName), agg.createCallback());
      }
    };
    fileNames.forEach(_function_1);
  }
  
  public void createOperationsFromCreatedFiles(final Iterable<String> fileNames, final ValueCallback<List<NetworkOperation>> cb) {
    final Closure<List<List<NetworkOperation>>> _function = new Closure<List<List<NetworkOperation>>>() {
      @Override
      public void apply(final List<List<NetworkOperation>> res) {
        cb.onSuccess(CollectionsUtils.<NetworkOperation>flatten(res));
      }
    };
    final Aggregator<List<NetworkOperation>> agg = AsyncCommon.<List<NetworkOperation>>collect(IterableExtensions.size(fileNames), 
      AsyncCommon.<List<List<NetworkOperation>>>embed(cb, _function));
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String fileName) {
        FileToNetworkOperations.this.params.getConverter().createNodes(FileToNetworkOperations.this.metadata, FileToNetworkOperations.this.params.getFolder().get(fileName), agg.createCallback());
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
          final FileItem itemNow = folder.get(fileMetadata.name());
          boolean _exists = itemNow.exists();
          if (_exists) {
            long _time = itemNow.lastModified().getTime();
            long _time_1 = fileMetadata.lastModified().getTime();
            boolean _greaterThan = (_time > _time_1);
            if (_greaterThan) {
              res.add(itemNow.getName());
            }
          }
        }
      }
      _xblockexpression = res;
    }
    return _xblockexpression;
  }
  
  public static ArrayList<String> determineLocallyAddedFiles(final Metadata metadata, final FileItem folder) {
    final ArrayList<String> previousNames = FileToNetworkOperations.getNamesFromCache(metadata.getChildren());
    final ArrayList<String> currentNames = FileToNetworkOperations.getNames(folder.getChildren());
    currentNames.removeAll(previousNames);
    return currentNames;
  }
  
  public static ArrayList<String> determineLocallyRemovedFiles(final Metadata metadata, final FileItem folder) {
    final ArrayList<String> previousNames = FileToNetworkOperations.getNamesFromCache(metadata.getChildren());
    final ArrayList<String> currentNames = FileToNetworkOperations.getNames(folder.getChildren());
    previousNames.removeAll(currentNames);
    return previousNames;
  }
  
  public static ArrayList<String> getNamesFromCache(final List<ItemMetadata> cachedChildren) {
    int _size = cachedChildren.size();
    final ArrayList<String> res = new ArrayList<String>(_size);
    for (final ItemMetadata fileItemMetaData : cachedChildren) {
      res.add(fileItemMetaData.name());
    }
    return res;
  }
  
  public static ArrayList<String> getNames(final List<FileItem> cachedChildren) {
    int _size = cachedChildren.size();
    final ArrayList<String> res = new ArrayList<String>(_size);
    for (final FileItem fileItem : cachedChildren) {
      res.add(fileItem.getName());
    }
    return res;
  }
}

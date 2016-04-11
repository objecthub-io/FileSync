package io.objecthub.filesync.internal.engine.convert;

import com.appjangle.api.Node;
import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import io.objecthub.filesync.Converter;
import io.objecthub.filesync.FileOperation;
import io.objecthub.filesync.FileOperationContext;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.internal.engine.FileUtils;
import io.objecthub.filesync.internal.engine.N;
import io.objecthub.filesync.internal.engine.convert.ConvertUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class FolderToNode implements Converter {
  @Override
  public boolean worksOn(final FileItem source) {
    return source.isDirectory();
  }
  
  @Override
  public void worksOn(final Node node, final ValueCallback<Boolean> cb) {
    cb.onSuccess(Boolean.valueOf(true));
  }
  
  @Override
  public void createNodes(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method ICON is undefined for the type FolderToNode");
  }
  
  @Override
  public void update(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    ArrayList<NetworkOperation> _newArrayList = CollectionLiterals.<NetworkOperation>newArrayList();
    cb.onSuccess(_newArrayList);
  }
  
  @Override
  public void deleteNodes(final Metadata metadata, final ItemMetadata cachedFile, final ValueCallback<List<NetworkOperation>> cb) {
    this.utils.deleteNodes(metadata, cachedFile, cb);
  }
  
  @Override
  public void createFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    final Closure<String> _function = new Closure<String>() {
      @Override
      public void apply(final String rawFolderName) {
        final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
        final FileOperation _function = new FileOperation() {
          @Override
          public void apply(final FileOperationContext ctx) {
            final String folderName = FolderToNode.this.futils.toFileSystemSafeName(rawFolderName, false, 100);
            FileItem _folder = ctx.folder();
            _folder.assertFolder(folderName);
            Metadata _metadata = ctx.metadata();
            _metadata.add(
              new ItemMetadata() {
                @Override
                public String name() {
                  return folderName;
                }
                
                @Override
                public Date lastModified() {
                  return new Date();
                }
                
                @Override
                public String uri() {
                  return source.uri();
                }
                
                @Override
                public String hash() {
                  int _hashCode = folderName.hashCode();
                  return Integer.valueOf(_hashCode).toString();
                }
                
                @Override
                public String converter() {
                  Class<? extends FolderToNode> _class = FolderToNode.this.getClass();
                  return _class.toString();
                }
              });
          }
        };
        ops.add(_function);
        cb.onSuccess(ops);
      }
    };
    ValueCallback<String> _embed = AsyncCommon.<String>embed(cb, _function);
    this.utils.getFileName(source, _embed);
  }
  
  @Override
  public void updateFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    ArrayList<FileOperation> _newArrayList = CollectionLiterals.<FileOperation>newArrayList();
    cb.onSuccess(_newArrayList);
  }
  
  @Override
  public void removeFiles(final FileItem folder, final Metadata metadata, final ItemMetadata item, final ValueCallback<List<FileOperation>> cb) {
    final String folderName = item.name();
    final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
    final FileOperation _function = new FileOperation() {
      @Override
      public void apply(final FileOperationContext ctx) {
        FileItem _folder = ctx.folder();
        _folder.deleteFolder(folderName);
        Metadata _metadata = ctx.metadata();
        _metadata.remove(folderName);
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  @Extension
  private ConvertUtils utils = new ConvertUtils();
  
  @Extension
  private FileUtils futils = new FileUtils();
  
  @Extension
  private N n = new N();
}

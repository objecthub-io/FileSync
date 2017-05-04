package io.objecthub.filesync.internal.engine.convert;

import com.appjangle.api.Node;
import com.appjangle.api.Query;
import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.simplelog.Log;
import io.nextweb.promise.DataOperation;
import io.objecthub.filesync.Converter;
import io.objecthub.filesync.FileOperation;
import io.objecthub.filesync.FileOperationContext;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.NetworkOperationContext;
import io.objecthub.filesync.internal.engine.FileUtils;
import io.objecthub.filesync.internal.engine.N;
import io.objecthub.filesync.internal.engine.convert.ConvertUtils;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class FolderToNode implements Converter {
  private final static String ID = "folder";
  
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
    final String simpleName = this.futils.getSimpleName(source.getName());
    final LinkedList<NetworkOperation> ops = new LinkedList<NetworkOperation>();
    final NetworkOperation _function = new NetworkOperation() {
      @Override
      public void apply(final NetworkOperationContext ctx, final ValueCallback<List<DataOperation<?>>> opscb) {
        final Query baseNode = ctx.parent().appendSafe(source.getName(), ("./" + simpleName));
        metadata.add(
          new ItemMetadata() {
            @Override
            public String name() {
              return source.getName();
            }
            
            @Override
            public Date lastModified() {
              return source.lastModified();
            }
            
            @Override
            public String uri() {
              String _uri = ctx.parent().uri();
              String _plus = (_uri + "/");
              return (_plus + simpleName);
            }
            
            @Override
            public String hash() {
              return Integer.valueOf(simpleName.hashCode()).toString();
            }
            
            @Override
            public String converter() {
              return FolderToNode.ID;
            }
          });
        opscb.onSuccess(
          CollectionLiterals.<DataOperation<?>>newArrayList(baseNode));
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  @Override
  public void update(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    cb.onSuccess(CollectionLiterals.<NetworkOperation>newArrayList());
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
            final String folderName = FolderToNode.this.futils.toFileSystemSafeName(rawFolderName, false, 50);
            ctx.folder().assertFolder(folderName);
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
                  return Integer.valueOf(folderName.hashCode()).toString();
                }
                
                @Override
                public String converter() {
                  return FolderToNode.ID;
                }
              });
          }
        };
        ops.add(_function);
        cb.onSuccess(ops);
      }
    };
    this.utils.getFileName(source, 
      AsyncCommon.<String>embed(cb, _function));
  }
  
  @Override
  public void updateFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    cb.onSuccess(CollectionLiterals.<FileOperation>newArrayList());
  }
  
  @Override
  public void removeFiles(final FileItem folder, final Metadata metadata, final ItemMetadata item, final ValueCallback<List<FileOperation>> cb) {
    final String folderName = item.name();
    final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
    final FileOperation _function = new FileOperation() {
      @Override
      public void apply(final FileOperationContext ctx) {
        boolean _exists = ctx.folder().get(folderName).exists();
        if (_exists) {
          ctx.folder().deleteFolder(folderName);
        } else {
          String _plus = (FolderToNode.this + ": Cannot delete folder. The folder does not exist: ");
          String _plus_1 = (_plus + folderName);
          Log.warn(_plus_1);
        }
        ctx.metadata().remove(folderName);
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
  
  @Override
  public String id() {
    return FolderToNode.ID;
  }
}

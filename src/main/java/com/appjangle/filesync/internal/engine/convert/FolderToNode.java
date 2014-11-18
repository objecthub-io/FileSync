package com.appjangle.filesync.internal.engine.convert;

import com.appjangle.filesync.Converter;
import com.appjangle.filesync.FileOperation;
import com.appjangle.filesync.FileOperationContext;
import com.appjangle.filesync.ItemMetadata;
import com.appjangle.filesync.Metadata;
import com.appjangle.filesync.NetworkOperation;
import com.appjangle.filesync.internal.engine.FileUtils;
import com.appjangle.filesync.internal.engine.N;
import com.appjangle.filesync.internal.engine.convert.ConvertUtils;
import de.mxro.async.Async;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.file.FileItem;
import de.mxro.fn.Closure;
import io.nextweb.Node;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class FolderToNode implements Converter {
  public boolean worksOn(final FileItem source) {
    return source.isDirectory();
  }
  
  public void worksOn(final Node node, final ValueCallback<Boolean> cb) {
    cb.onSuccess(Boolean.valueOf(true));
  }
  
  public void createNodes(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field MxroGWTUtils is undefined for the type FolderToNode"
      + "\ngetSimpleName cannot be resolved"
      + "\nhashCode cannot be resolved"
      + "\ntoString cannot be resolved");
  }
  
  public void update(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    ArrayList<NetworkOperation> _newArrayList = CollectionLiterals.<NetworkOperation>newArrayList();
    cb.onSuccess(_newArrayList);
  }
  
  public void deleteNodes(final Metadata metadata, final ItemMetadata cachedFile, final ValueCallback<List<NetworkOperation>> cb) {
    this.utils.deleteNodes(metadata, cachedFile, cb);
  }
  
  public void createFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    final Closure<String> _function = new Closure<String>() {
      public void apply(final String rawFolderName) {
        final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
        final FileOperation _function = new FileOperation() {
          public void apply(final FileOperationContext ctx) {
            final String folderName = FolderToNode.this.futils.toFileSystemSafeName(rawFolderName, false, 100);
            FileItem _folder = ctx.folder();
            _folder.assertFolder(folderName);
            Metadata _metadata = ctx.metadata();
            _metadata.add(
              new ItemMetadata() {
                public String name() {
                  return folderName;
                }
                
                public Date lastModified() {
                  return new Date();
                }
                
                public String uri() {
                  return source.uri();
                }
                
                public String hash() {
                  int _hashCode = folderName.hashCode();
                  return Integer.valueOf(_hashCode).toString();
                }
                
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
    ValueCallback<String> _embed = Async.<String>embed(cb, _function);
    this.utils.getFileName(source, _embed);
  }
  
  public void updateFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    ArrayList<FileOperation> _newArrayList = CollectionLiterals.<FileOperation>newArrayList();
    cb.onSuccess(_newArrayList);
  }
  
  public void removeFiles(final FileItem folder, final Metadata metadata, final ItemMetadata item, final ValueCallback<List<FileOperation>> cb) {
    final String folderName = item.name();
    final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
    final FileOperation _function = new FileOperation() {
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

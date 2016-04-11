package io.objecthub.filesync.internal.engine.convert;

import com.appjangle.api.Client;
import com.appjangle.api.Link;
import com.appjangle.api.LinkList;
import com.appjangle.api.LinkListQuery;
import com.appjangle.api.Node;
import com.appjangle.api.Query;
import com.google.common.base.Objects;
import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import io.nextweb.promise.DataOperation;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.objecthub.filesync.Converter;
import io.objecthub.filesync.FileOperation;
import io.objecthub.filesync.FileOperationContext;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.NetworkOperationContext;
import io.objecthub.filesync.internal.engine.FileUtils;
import io.objecthub.filesync.internal.engine.convert.ConvertUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class FileToTextNode implements Converter {
  @Override
  public boolean worksOn(final FileItem source) {
    String _name = source.getName();
    return this.cutils.isTextValue(_name);
  }
  
  @Override
  public void worksOn(final Node node, final ValueCallback<Boolean> cb) {
    final LinkListQuery qry = node.selectAllLinks();
    final ExceptionListener _function = new ExceptionListener() {
      @Override
      public void onFailure(final ExceptionResult er) {
        Throwable _exception = er.exception();
        cb.onFailure(_exception);
      }
    };
    qry.catchExceptions(_function);
    final Closure<LinkList> _function_1 = new Closure<LinkList>() {
      @Override
      public void apply(final LinkList links) {
        for (final Link link : links) {
          boolean _isTextType = FileToTextNode.this.cutils.isTextType(link);
          if (_isTextType) {
            cb.onSuccess(Boolean.valueOf(true));
            return;
          }
        }
        cb.onSuccess(Boolean.valueOf(false));
      }
    };
    qry.get(_function_1);
  }
  
  @Override
  public void createNodes(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    throw new Error("Unresolved compilation problems:"
      + "\nType mismatch: cannot convert from void to DataOperation<?>");
  }
  
  @Override
  public void update(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    final String content = source.getText();
    String _name = source.getName();
    ItemMetadata _get = metadata.get(_name);
    final String address = _get.uri();
    final LinkedList<NetworkOperation> ops = new LinkedList<NetworkOperation>();
    final NetworkOperation _function = new NetworkOperation() {
      @Override
      public void apply(final NetworkOperationContext ctx, final ValueCallback<List<DataOperation<?>>> opscb) {
        Client _session = ctx.session();
        Link _link = _session.link(address);
        Query _setValueSafe = _link.setValueSafe(content);
        ArrayList<DataOperation<?>> _newArrayList = CollectionLiterals.<DataOperation<?>>newArrayList(_setValueSafe);
        opscb.onSuccess(_newArrayList);
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  @Override
  public void deleteNodes(final Metadata metadata, final ItemMetadata cachedFile, final ValueCallback<List<NetworkOperation>> cb) {
    this.cutils.deleteNodes(metadata, cachedFile, cb);
  }
  
  @Override
  public void createFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    final Closure<String> _function = new Closure<String>() {
      @Override
      public void apply(final String ext) {
        final Closure<String> _function = new Closure<String>() {
          @Override
          public void apply(final String rawFileName) {
            final String fileName = FileToTextNode.this.futils.toFileSystemSafeName(rawFileName, false, 100);
            final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
            final FileOperation _function = new FileOperation() {
              @Override
              public void apply(final FileOperationContext ctx) {
                FileItem _folder = ctx.folder();
                final FileItem file = _folder.createFile(fileName);
                String _value = source.<String>value(String.class);
                file.setText(_value);
                Metadata _metadata = ctx.metadata();
                _metadata.add(
                  new ItemMetadata() {
                    @Override
                    public String name() {
                      return fileName;
                    }
                    
                    @Override
                    public Date lastModified() {
                      return file.lastModified();
                    }
                    
                    @Override
                    public String uri() {
                      return source.uri();
                    }
                    
                    @Override
                    public String hash() {
                      return file.hash();
                    }
                    
                    @Override
                    public String converter() {
                      Class<? extends FileToTextNode> _class = FileToTextNode.this.getClass();
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
        FileToTextNode.this.cutils.getFileName(source, folder, ext, _embed);
      }
    };
    ValueCallback<String> _embed = AsyncCommon.<String>embed(cb, _function);
    this.cutils.getFileExtension(source, _embed);
  }
  
  @Override
  public void updateFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    ItemMetadata _get = metadata.get(source);
    final String fileName = _get.name();
    final String content = source.<String>value(String.class);
    final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
    final FileOperation _function = new FileOperation() {
      @Override
      public void apply(final FileOperationContext ctx) {
        FileItem _folder = ctx.folder();
        final FileItem file = _folder.get(fileName);
        String _text = file.getText();
        boolean _notEquals = (!Objects.equal(_text, content));
        if (_notEquals) {
          file.setText(content);
          Metadata _metadata = ctx.metadata();
          _metadata.update(
            new ItemMetadata() {
              @Override
              public String name() {
                return fileName;
              }
              
              @Override
              public Date lastModified() {
                return file.lastModified();
              }
              
              @Override
              public String uri() {
                return source.uri();
              }
              
              @Override
              public String hash() {
                return file.hash();
              }
              
              @Override
              public String converter() {
                Class<? extends FileToTextNode> _class = FileToTextNode.this.getClass();
                return _class.toString();
              }
            });
        }
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  @Override
  public void removeFiles(final FileItem folder, final Metadata metadata, final ItemMetadata item, final ValueCallback<List<FileOperation>> cb) {
    final String fileName = item.name();
    final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
    final FileOperation _function = new FileOperation() {
      @Override
      public void apply(final FileOperationContext ctx) {
        FileItem _folder = ctx.folder();
        _folder.deleteFile(fileName);
        Metadata _metadata = ctx.metadata();
        _metadata.remove(fileName);
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  @Extension
  private ConvertUtils cutils = new ConvertUtils();
  
  @Extension
  private FileUtils futils = new FileUtils();
}

package io.objecthub.filesync.internal.engine.convert;

import com.appjangle.api.Client;
import com.appjangle.api.Link;
import com.appjangle.api.LinkList;
import com.appjangle.api.LinkListQuery;
import com.appjangle.api.Node;
import com.appjangle.api.Query;
import com.appjangle.api.nodes.Bytes;
import com.appjangle.api.nodes.Values;
import com.google.common.base.Objects;
import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import io.nextweb.promise.DataOperation;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.nextweb.promise.utils.CallbackUtils;
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
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class FileToTextNode implements Converter {
  private final String id;
  
  private final String fileExtension;
  
  private final String markerClass;
  
  private final String valueReference;
  
  @Override
  public boolean worksOn(final FileItem source) {
    String _name = source.getName();
    final String ext = this.futils.getExtension(_name);
    InputOutput.<String>println(((ext + " == ") + this.fileExtension));
    return Objects.equal(ext, this.fileExtension);
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
          String _uri = link.uri();
          boolean _equals = Objects.equal(_uri, FileToTextNode.this.markerClass);
          if (_equals) {
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
    String _name = source.getName();
    final String nameWithoutExtension = this.futils.removeExtension(_name);
    final String simpleName = this.futils.getSimpleName(nameWithoutExtension);
    final LinkedList<NetworkOperation> ops = new LinkedList<NetworkOperation>();
    final NetworkOperation _function = new NetworkOperation() {
      @Override
      public void apply(final NetworkOperationContext ctx, final ValueCallback<List<DataOperation<?>>> opscb) {
        Node _parent = ctx.parent();
        String _text = source.getText();
        final Query baseNode = _parent.appendSafe(_text, ("./" + simpleName));
        metadata.add(new ItemMetadata() {
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
            Node _parent = ctx.parent();
            String _uri = _parent.uri();
            String _plus = (_uri + "/");
            return (_plus + simpleName);
          }
          
          @Override
          public String hash() {
            return source.hash();
          }
          
          @Override
          public String converter() {
            return FileToTextNode.this.id;
          }
        });
        final ArrayList<DataOperation<?>> res = CollectionLiterals.<DataOperation<?>>newArrayList();
        res.add(baseNode);
        opscb.onSuccess(res);
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  @Override
  public void update(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    final String sourceText = source.getText();
    Object contentVar = null;
    boolean _startsWith = sourceText.startsWith("//BASE64//");
    if (_startsWith) {
      final int base64End = "//BASE64//".length();
      int _indexOf = sourceText.indexOf("/", base64End);
      final int mimeTypeEnd = (_indexOf - 1);
      final String mimeType = sourceText.substring(base64End, mimeTypeEnd);
      int _length = mimeType.length();
      int _plus = (base64End + _length);
      int _plus_1 = (_plus + 1);
      final String data = sourceText.substring(_plus_1);
      Base64.Decoder _decoder = Base64.getDecoder();
      byte[] _decode = _decoder.decode(data);
      Bytes _bytes = Values.bytes(_decode, mimeType);
      contentVar = _bytes;
    } else {
      String _text = source.getText();
      contentVar = _text;
    }
    final Object content = contentVar;
    String _name = source.getName();
    ItemMetadata _get = metadata.get(_name);
    final String address = _get.uri();
    final LinkedList<NetworkOperation> ops = new LinkedList<NetworkOperation>();
    final NetworkOperation _function = new NetworkOperation() {
      @Override
      public void apply(final NetworkOperationContext ctx, final ValueCallback<List<DataOperation<?>>> opscb) {
        if ((FileToTextNode.this.valueReference == null)) {
          Client _session = ctx.session();
          Link _link = _session.link(address);
          Query _setValueSafe = _link.setValueSafe(content);
          ArrayList<DataOperation<?>> _newArrayList = CollectionLiterals.<DataOperation<?>>newArrayList(_setValueSafe);
          opscb.onSuccess(_newArrayList);
        } else {
          Client _session_1 = ctx.session();
          Link _link_1 = _session_1.link(address);
          Link _selectAsLink = _link_1.selectAsLink(FileToTextNode.this.valueReference);
          Query _setValueSafe_1 = _selectAsLink.setValueSafe(content);
          ArrayList<DataOperation<?>> _newArrayList_1 = CollectionLiterals.<DataOperation<?>>newArrayList(_setValueSafe_1);
          opscb.onSuccess(_newArrayList_1);
        }
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
            final Closure<Node> _function = new Closure<Node>() {
              @Override
              public void apply(final Node node) {
                final LinkedList<FileOperation> ops = new LinkedList<FileOperation>();
                final FileOperation _function = new FileOperation() {
                  @Override
                  public void apply(final FileOperationContext ctx) {
                    FileItem _folder = ctx.folder();
                    final FileItem file = _folder.createFile(fileName);
                    String _value = node.<String>value(String.class);
                    file.setText(_value);
                    Metadata _metadata = ctx.metadata();
                    _metadata.add(new ItemMetadata() {
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
                        return FileToTextNode.this.id;
                      }
                    });
                  }
                };
                ops.add(_function);
                cb.onSuccess(ops);
              }
            };
            ValueCallback<Node> _embed = AsyncCommon.<Node>embed(cb, _function);
            FileToTextNode.this.obtainValueNode(source, _embed);
          }
        };
        ValueCallback<String> _embed = AsyncCommon.<String>embed(cb, _function);
        FileToTextNode.this.cutils.getFileName(source, folder, ext, _embed);
      }
    };
    ValueCallback<String> _embed = AsyncCommon.<String>embed(cb, _function);
    this.cutils.getFileExtension(source, _embed);
  }
  
  public void obtainValueNode(final Node source, final ValueCallback<Node> cb) {
    if ((this.valueReference == null)) {
      cb.onSuccess(source);
      return;
    }
    final Link qry = source.selectAsLink(this.valueReference);
    ExceptionListener _asExceptionListener = CallbackUtils.<Node>asExceptionListener(cb);
    qry.catchExceptions(_asExceptionListener);
    final Closure<Node> _function = new Closure<Node>() {
      @Override
      public void apply(final Node node) {
        cb.onSuccess(node);
      }
    };
    qry.get(_function);
  }
  
  @Override
  public void updateFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    ItemMetadata _get = metadata.get(source);
    final String fileName = _get.name();
    final Closure<Node> _function = new Closure<Node>() {
      @Override
      public void apply(final Node node) {
        try {
          String contentVar = null;
          Object _value = node.value();
          if ((_value instanceof Bytes)) {
            Object _value_1 = node.value();
            final Bytes bytes = ((Bytes) _value_1);
            String _mimeType = bytes.getMimeType();
            String _plus = ("//BASE64//" + _mimeType);
            String _plus_1 = (_plus + "/");
            Base64.Encoder _encoder = Base64.getEncoder();
            byte[] _bytes = bytes.getBytes();
            byte[] _encode = _encoder.encode(_bytes);
            String _string = new String(_encode, "UTF-8");
            String _plus_2 = (_plus_1 + _string);
            contentVar = _plus_2;
          } else {
            String _value_2 = node.<String>value(String.class);
            contentVar = _value_2;
          }
          final String content = contentVar;
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
                _metadata.update(new ItemMetadata() {
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
                    return FileToTextNode.this.id;
                  }
                });
              }
            }
          };
          ops.add(_function);
          cb.onSuccess(ops);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    ValueCallback<Node> _embed = AsyncCommon.<Node>embed(cb, _function);
    this.obtainValueNode(source, _embed);
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
  
  public FileToTextNode(final String id, final String fileExtension, final String markerClass, final String valueReference) {
    this.id = id;
    this.fileExtension = fileExtension;
    this.markerClass = markerClass;
    this.valueReference = valueReference;
  }
  
  @Override
  public String id() {
    return this.id;
  }
}

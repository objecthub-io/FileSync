package io.objecthub.filesync.internal.engine;

import com.appjangle.api.Node;
import com.appjangle.api.Query;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import de.mxro.file.FileItem;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Success;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import java.util.Date;

@SuppressWarnings("all")
public class SyncValueOperations {
  public static ItemMetadata createMetadata(final Node node, final FileItem forFile) {
    return new ItemMetadata() {
      @Override
      public String name() {
        return forFile.getName();
      }
      
      @Override
      public Date lastModified() {
        return forFile.lastModified();
      }
      
      @Override
      public String uri() {
        return node.uri();
      }
      
      @Override
      public String hash() {
        return forFile.hash();
      }
      
      @Override
      public String converter() {
        return "";
      }
    };
  }
  
  public void downloadValue(final Node node, final Metadata metadata, final FileItem folder) {
    FileItem _get = folder.get("value.txt");
    boolean _exists = _get.exists();
    boolean _not = (!_exists);
    if (_not) {
      FileItem _createFile = folder.createFile("value.txt");
      Object _value = node.value();
      String _string = _value.toString();
      _createFile.setText(_string);
      FileItem _get_1 = folder.get("value.txt");
      ItemMetadata _createMetadata = SyncValueOperations.createMetadata(node, _get_1);
      metadata.setValue(_createMetadata);
      return;
    }
    FileItem _get_2 = folder.get("value.txt");
    final String oldText = _get_2.getText();
    Object _value_1 = node.value();
    String _string_1 = _value_1.toString();
    boolean _notEquals = (!Objects.equal(oldText, _string_1));
    if (_notEquals) {
      FileItem _get_3 = folder.get("value.txt");
      Object _value_2 = node.value();
      String _string_2 = _value_2.toString();
      _get_3.setText(_string_2);
      FileItem _get_4 = folder.get("value.txt");
      ItemMetadata _createMetadata_1 = SyncValueOperations.createMetadata(node, _get_4);
      metadata.setValue(_createMetadata_1);
    }
  }
  
  public void uploadValue(final Node node, final Metadata metadata, final FileItem folder, final ValueCallback<Success> cb) {
    final FileItem file = folder.get("value.txt");
    boolean _exists = file.exists();
    Preconditions.checkState(_exists);
    ItemMetadata _value = metadata.value();
    Date _lastModified = _value.lastModified();
    long _time = _lastModified.getTime();
    Date _lastModified_1 = file.lastModified();
    long _time_1 = _lastModified_1.getTime();
    boolean _greaterThan = (_time > _time_1);
    if (_greaterThan) {
      String _text = file.getText();
      final Query qry = node.setValueSafe(_text);
      final ExceptionListener _function = new ExceptionListener() {
        @Override
        public void onFailure(final ExceptionResult it) {
          Throwable _exception = it.exception();
          cb.onFailure(_exception);
        }
      };
      qry.catchExceptions(_function);
      final Closure<Node> _function_1 = new Closure<Node>() {
        @Override
        public void apply(final Node it) {
          FileItem _get = folder.get("value.txt");
          ItemMetadata _createMetadata = SyncValueOperations.createMetadata(node, _get);
          metadata.setValue(_createMetadata);
          cb.onSuccess(Success.INSTANCE);
        }
      };
      qry.get(_function_1);
    }
  }
}

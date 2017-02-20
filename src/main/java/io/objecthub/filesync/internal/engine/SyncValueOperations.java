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
    boolean _exists = folder.get("value.txt").exists();
    boolean _not = (!_exists);
    if (_not) {
      FileItem _createFile = folder.createFile("value.txt");
      _createFile.setText(node.value().toString());
      metadata.setValue(SyncValueOperations.createMetadata(node, folder.get("value.txt")));
      return;
    }
    final String oldText = folder.get("value.txt").getText();
    String _string = node.value().toString();
    boolean _notEquals = (!Objects.equal(oldText, _string));
    if (_notEquals) {
      FileItem _get = folder.get("value.txt");
      _get.setText(node.value().toString());
      metadata.setValue(SyncValueOperations.createMetadata(node, folder.get("value.txt")));
    }
  }
  
  public void uploadValue(final Node node, final Metadata metadata, final FileItem folder, final ValueCallback<Success> cb) {
    final FileItem file = folder.get("value.txt");
    Preconditions.checkState(file.exists());
    long _time = metadata.value().lastModified().getTime();
    long _time_1 = file.lastModified().getTime();
    boolean _greaterThan = (_time > _time_1);
    if (_greaterThan) {
      final Query qry = node.setValueSafe(file.getText());
      final ExceptionListener _function = new ExceptionListener() {
        @Override
        public void onFailure(final ExceptionResult it) {
          cb.onFailure(it.exception());
        }
      };
      qry.catchExceptions(_function);
      final Closure<Node> _function_1 = new Closure<Node>() {
        @Override
        public void apply(final Node it) {
          metadata.setValue(SyncValueOperations.createMetadata(node, folder.get("value.txt")));
          cb.onSuccess(Success.INSTANCE);
        }
      };
      qry.get(_function_1);
    }
  }
}

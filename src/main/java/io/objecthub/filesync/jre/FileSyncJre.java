package io.objecthub.filesync.jre;

import com.appjangle.api.Node;
import de.mxro.file.Jre.FilesJre;
import delight.async.Operation;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.functional.Success;
import io.objecthub.filesync.FileSync;
import java.io.File;

@SuppressWarnings("all")
public class FileSyncJre {
  public static void sync(final File folder, final Node node) {
    final Operation<Success> _function = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> cb) {
        FileSync.sync(FilesJre.wrap(folder), node, cb);
      }
    };
    Async.<Success>waitFor(_function);
  }
}

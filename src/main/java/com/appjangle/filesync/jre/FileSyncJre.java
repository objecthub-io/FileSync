package com.appjangle.filesync.jre;

import com.appjangle.filesync.FileSync;
import de.mxro.async.Deferred;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;
import de.mxro.file.FileItem;
import de.mxro.file.Jre.FilesJre;
import de.mxro.fn.Success;
import io.nextweb.Node;
import java.io.File;

@SuppressWarnings("all")
public class FileSyncJre {
  public static void sync(final File folder, final Node node) {
    final Deferred<Success> _function = new Deferred<Success>() {
      public void get(final ValueCallback<Success> cb) {
        FileItem _wrap = FilesJre.wrap(folder);
        FileSync.sync(_wrap, node, cb);
      }
    };
    AsyncJre.<Success>waitFor(_function);
  }
}
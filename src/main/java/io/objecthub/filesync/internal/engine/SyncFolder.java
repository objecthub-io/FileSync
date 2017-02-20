package io.objecthub.filesync.internal.engine;

import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Success;
import io.objecthub.filesync.FileOperation;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.SyncParams;
import io.objecthub.filesync.internal.engine.FileToNetworkOperations;
import io.objecthub.filesync.internal.engine.FileUtils;
import io.objecthub.filesync.internal.engine.NetworkToFileOperations;
import io.objecthub.filesync.internal.engine.NetworkUtils;
import io.objecthub.filesync.internal.engine.SyncValueOperations;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class SyncFolder {
  private final SyncParams params;
  
  public SyncFolder(final SyncParams params) {
    this.params = params;
  }
  
  private Metadata metadata;
  
  public void doIt(final ValueCallback<Success> cb) {
    this.params.getNotifications().onStartSynchronizing(this.params.getFolder(), this.params.getNode());
    boolean _hasMetadata = this.fileUtils.hasMetadata(this.params.getFolder());
    boolean _not = (!_hasMetadata);
    if (_not) {
      this.metadata = this.fileUtils.assertMetadata(this.params.getFolder());
      this.download(cb);
      return;
    }
    this.metadata = this.fileUtils.assertMetadata(this.params.getFolder());
    boolean _isUpload = this.params.getSettings().isUpload();
    boolean _not_1 = (!_isUpload);
    if (_not_1) {
      this.download(cb);
      return;
    }
    final Closure<Success> _function = new Closure<Success>() {
      @Override
      public void apply(final Success it) {
        final Closure<List<NetworkOperation>> _function = new Closure<List<NetworkOperation>>() {
          @Override
          public void apply(final List<NetworkOperation> ops) {
            final Closure<Success> _function = new Closure<Success>() {
              @Override
              public void apply(final Success it) {
                SyncFolder.this.download(cb);
              }
            };
            SyncFolder.this.networkUtils.execute(ops, SyncFolder.this.params.getNode(), 
              AsyncCommon.<Success>embed(cb, _function));
          }
        };
        new FileToNetworkOperations(SyncFolder.this.params, SyncFolder.this.metadata).determineOps(
          AsyncCommon.<List<NetworkOperation>>embed(cb, _function));
      }
    };
    new SyncValueOperations().uploadValue(this.params.getNode(), this.metadata, this.params.getFolder(), 
      AsyncCommon.<Success>embed(cb, _function));
  }
  
  public void download(final ValueCallback<Success> cb) {
    boolean _isDownload = this.params.getSettings().isDownload();
    boolean _not = (!_isDownload);
    if (_not) {
      this.params.getNotifications().onFinishedSynchronizing(this.params.getFolder(), this.params.getNode());
      cb.onSuccess(Success.INSTANCE);
      return;
    }
    new SyncValueOperations().downloadValue(this.params.getNode(), this.metadata, this.params.getFolder());
    final Closure<List<FileOperation>> _function = new Closure<List<FileOperation>>() {
      @Override
      public void apply(final List<FileOperation> ops) {
        SyncFolder.this.fileUtils.execute(ops, SyncFolder.this.params.getFolder(), SyncFolder.this.metadata);
        SyncFolder.this.fileUtils.saveMetadata(SyncFolder.this.params.getFolder(), SyncFolder.this.metadata);
        SyncFolder.this.params.getNotifications().onFinishedSynchronizing(SyncFolder.this.params.getFolder(), SyncFolder.this.params.getNode());
        cb.onSuccess(Success.INSTANCE);
      }
    };
    new NetworkToFileOperations(this.params, this.metadata).determineOps(
      AsyncCommon.<List<FileOperation>>embed(cb, _function));
  }
  
  @Extension
  private FileUtils fileUtils = new FileUtils();
  
  @Extension
  private NetworkUtils networkUtils = new NetworkUtils();
}

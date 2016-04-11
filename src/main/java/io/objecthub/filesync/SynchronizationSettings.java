package io.objecthub.filesync;

import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Settings for FileSync synchronization.
 */
@Accessors
@SuppressWarnings("all")
public class SynchronizationSettings {
  private boolean upload = true;
  
  private boolean download = true;
  
  @Pure
  public boolean isUpload() {
    return this.upload;
  }
  
  public void setUpload(final boolean upload) {
    this.upload = upload;
  }
  
  @Pure
  public boolean isDownload() {
    return this.download;
  }
  
  public void setDownload(final boolean download) {
    this.download = download;
  }
}

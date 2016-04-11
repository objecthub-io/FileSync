package io.objecthub.filesync;

import de.mxro.file.FileItem;
import io.objecthub.filesync.Metadata;

@SuppressWarnings("all")
public interface FileOperationContext {
  public abstract FileItem folder();
  
  public abstract Metadata metadata();
}

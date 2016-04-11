package io.objecthub.filesync.internal.engine.convert;

import com.appjangle.api.Node;
import de.mxro.file.FileItem;
import delight.async.callbacks.ValueCallback;
import delight.functional.Function;
import io.objecthub.filesync.Converter;
import io.objecthub.filesync.FileOperation;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class FolderToNothing implements Converter {
  private final Function<FileItem, Boolean> test;
  
  public FolderToNothing(final Function<FileItem, Boolean> test) {
    this.test = test;
  }
  
  @Override
  public boolean worksOn(final FileItem source) {
    return (this.test.apply(source)).booleanValue();
  }
  
  @Override
  public void worksOn(final Node node, final ValueCallback<Boolean> cb) {
    cb.onSuccess(Boolean.valueOf(false));
  }
  
  @Override
  public void createNodes(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    ArrayList<NetworkOperation> _newArrayList = CollectionLiterals.<NetworkOperation>newArrayList();
    cb.onSuccess(_newArrayList);
  }
  
  @Override
  public void update(final Metadata metadata, final FileItem source, final ValueCallback<List<NetworkOperation>> cb) {
    throw new IllegalStateException("This operation should never be triggered for this converter.");
  }
  
  @Override
  public void deleteNodes(final Metadata metadata, final ItemMetadata cachedFile, final ValueCallback<List<NetworkOperation>> cb) {
    throw new IllegalStateException("This operation should never be triggered for this converter.");
  }
  
  @Override
  public void createFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    throw new IllegalStateException("This operation should never be triggered for this converter.");
  }
  
  @Override
  public void updateFiles(final FileItem folder, final Metadata metadata, final Node source, final ValueCallback<List<FileOperation>> cb) {
    throw new IllegalStateException("This operation should never be triggered for this converter.");
  }
  
  @Override
  public void removeFiles(final FileItem folder, final Metadata metadata, final ItemMetadata item, final ValueCallback<List<FileOperation>> cb) {
    throw new IllegalStateException("This operation should never be triggered for this converter.");
  }
}

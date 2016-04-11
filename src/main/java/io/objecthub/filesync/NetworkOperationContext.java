package io.objecthub.filesync;

import com.appjangle.api.Client;
import com.appjangle.api.Node;

@SuppressWarnings("all")
public interface NetworkOperationContext {
  public abstract Client session();
  
  public abstract Node parent();
}

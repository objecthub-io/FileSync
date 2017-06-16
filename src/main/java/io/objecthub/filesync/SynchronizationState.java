package io.objecthub.filesync;

import com.appjangle.api.Node;
import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;

/**
 * Must be synchronized.
 */
@SuppressWarnings("all")
public class SynchronizationState {
  /**
   * All nodes already synchronized
   */
  private final Set<String> synced;
  
  public synchronized boolean addSynced(final Node n) {
    boolean _xblockexpression = false;
    {
      boolean _contains = this.synced.contains(n.uri());
      boolean _not = (!_contains);
      Preconditions.checkState(_not, "Node was already synced [%s]", n.uri());
      _xblockexpression = this.synced.add(n.uri());
    }
    return _xblockexpression;
  }
  
  public synchronized boolean wasSynced(final Node n) {
    return this.synced.contains(n.uri());
  }
  
  public SynchronizationState() {
    HashSet<String> _hashSet = new HashSet<String>();
    this.synced = _hashSet;
  }
}

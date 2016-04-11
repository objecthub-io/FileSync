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
      String _uri = n.uri();
      boolean _contains = this.synced.contains(_uri);
      boolean _not = (!_contains);
      String _uri_1 = n.uri();
      Preconditions.checkState(_not, "Node was already synced [%s]", _uri_1);
      String _uri_2 = n.uri();
      _xblockexpression = this.synced.add(_uri_2);
    }
    return _xblockexpression;
  }
  
  public synchronized boolean wasSynced(final Node n) {
    String _uri = n.uri();
    return this.synced.contains(_uri);
  }
  
  public SynchronizationState() {
    HashSet<String> _hashSet = new HashSet<String>();
    this.synced = _hashSet;
  }
}

package io.objecthub.filesync.internal.engine.metadata.v01;

import io.objecthub.filesync.internal.engine.metadata.v01.ItemXml;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pure;

@Accessors
@SuppressWarnings("all")
public class NodesXml implements Serializable {
  public final List<ItemXml> items;
  
  public ItemXml value;
  
  public NodesXml() {
    ArrayList<ItemXml> _newArrayList = CollectionLiterals.<ItemXml>newArrayList();
    this.items = _newArrayList;
    this.value = null;
  }
  
  @Pure
  public List<ItemXml> getItems() {
    return this.items;
  }
  
  @Pure
  public ItemXml getValue() {
    return this.value;
  }
  
  public void setValue(final ItemXml value) {
    this.value = value;
  }
}

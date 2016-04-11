package io.objecthub.filesync.internal.engine.metadata.v01;

import io.objecthub.filesync.ItemMetadata;
import java.io.Serializable;
import java.util.Date;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend.lib.annotations.EqualsHashCode;
import org.eclipse.xtext.xbase.lib.Pure;

@EqualsHashCode
@SuppressWarnings("all")
public class ItemXml implements ItemMetadata, Serializable {
  @Accessors
  public String name;
  
  @Accessors
  public Date lastModified;
  
  @Accessors
  public String uri;
  
  @Accessors
  public String hash;
  
  @Accessors
  public String converter;
  
  @Override
  public String name() {
    return this.name;
  }
  
  @Override
  public Date lastModified() {
    return this.lastModified;
  }
  
  @Override
  public String uri() {
    return this.uri;
  }
  
  @Override
  public String hash() {
    return this.hash;
  }
  
  @Override
  public String converter() {
    return this.converter;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ItemXml other = (ItemXml) obj;
    if (this.name == null) {
      if (other.name != null)
        return false;
    } else if (!this.name.equals(other.name))
      return false;
    if (this.lastModified == null) {
      if (other.lastModified != null)
        return false;
    } else if (!this.lastModified.equals(other.lastModified))
      return false;
    if (this.uri == null) {
      if (other.uri != null)
        return false;
    } else if (!this.uri.equals(other.uri))
      return false;
    if (this.hash == null) {
      if (other.hash != null)
        return false;
    } else if (!this.hash.equals(other.hash))
      return false;
    if (this.converter == null) {
      if (other.converter != null)
        return false;
    } else if (!this.converter.equals(other.converter))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.name== null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.lastModified== null) ? 0 : this.lastModified.hashCode());
    result = prime * result + ((this.uri== null) ? 0 : this.uri.hashCode());
    result = prime * result + ((this.hash== null) ? 0 : this.hash.hashCode());
    result = prime * result + ((this.converter== null) ? 0 : this.converter.hashCode());
    return result;
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
  
  public void setName(final String name) {
    this.name = name;
  }
  
  @Pure
  public Date getLastModified() {
    return this.lastModified;
  }
  
  public void setLastModified(final Date lastModified) {
    this.lastModified = lastModified;
  }
  
  @Pure
  public String getUri() {
    return this.uri;
  }
  
  public void setUri(final String uri) {
    this.uri = uri;
  }
  
  @Pure
  public String getHash() {
    return this.hash;
  }
  
  public void setHash(final String hash) {
    this.hash = hash;
  }
  
  @Pure
  public String getConverter() {
    return this.converter;
  }
  
  public void setConverter(final String converter) {
    this.converter = converter;
  }
}

package com.appjangle.filesync;

import io.nextweb.Node;
import io.nextweb.Session;

@SuppressWarnings("all")
public interface NetworkOperation {
  public abstract void define(final Session session, final Node node);
}
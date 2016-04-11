package io.objecthub.filesync.internal.engine;

import com.appjangle.api.Client;
import com.appjangle.api.Link;

@SuppressWarnings("all")
public class N {
  private final static String ID = "100";
  
  public static String getTypeLink(final String type) {
    return (((("https://beta.objecthub.io/dev/~" + N.ID) + "/~hub/hub/repo/~objects/~") + type) + "/versions/~*0.node.html");
  }
  
  public static Link getType(final Client client, final String type) {
    String _typeLink = N.getTypeLink(type);
    return client.link(_typeLink);
  }
  
  public static String HTML_VALUE() {
    return N.getTypeLink("html");
  }
  
  public Link HTML_VALUE(final Client client) {
    return N.getType(client, "html");
  }
  
  public static String LABEL() {
    return "https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/shortLabel";
  }
  
  public Link LABEL(final Client session) {
    String _LABEL = N.LABEL();
    return session.link(_LABEL);
  }
  
  public static String LABEL2() {
    return "https://u1.linnk.it/qc8sbw/usr/apps/textsync/upload/label";
  }
  
  public static String LABEL3() {
    return "https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/longLabel";
  }
  
  public static String COFFEESCRIPT() {
    return N.getTypeLink("plain-cs");
  }
  
  public Link COFFEESCRIPT(final Client client) {
    return N.getType(client, "plain-cs");
  }
  
  public static String JAVASCRIPT() {
    return N.getTypeLink("javascript");
  }
  
  public Link JAVASCRIPT(final Client client) {
    return N.getType(client, "javascript");
  }
  
  public static String CSS() {
    return N.getTypeLink("css");
  }
  
  public Link CSS(final Client client) {
    return N.getType(client, "css");
  }
  
  public static String TYPE() {
    return "http://slicnet.com/mxrogm/mxrogm/data/stream/2013/12/11/n5";
  }
  
  public Link TYPE(final Client session) {
    String _TYPE = N.TYPE();
    return session.link(_TYPE);
  }
  
  public static String RICHTEXT() {
    return "http://slicnet.com/mxrogm/mxrogm/data/stream/2014/3/28/n3";
  }
  
  public Link RICHTEXT(final Client session) {
    String _RICHTEXT = N.RICHTEXT();
    return session.link(_RICHTEXT);
  }
}

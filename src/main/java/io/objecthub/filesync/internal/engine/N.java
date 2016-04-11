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
  
  public static String TEXT_VALUE() {
    return "https://u1.linnk.it/6wbnoq/Types/aTextValue";
  }
  
  public Link TEXT_VALUE(final Client session) {
    String _TEXT_VALUE = N.TEXT_VALUE();
    return session.link(_TEXT_VALUE);
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
  
  public static String ICON() {
    return "https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/icon32";
  }
  
  public Link ICON(final Client session) {
    String _ICON = N.ICON();
    return session.link(_ICON);
  }
  
  public static String COFFEESCRIPT() {
    return "http://slicnet.com/mxrogm/mxrogm/data/stream/2014/1/10/n3";
  }
  
  public Link COFFEESCRIPT(final Client session) {
    String _COFFEESCRIPT = N.COFFEESCRIPT();
    return session.link(_COFFEESCRIPT);
  }
  
  public static String JAVASCRIPT() {
    return "https://u1.linnk.it/qc8sbw/usr/apps/textsync/upload/java-script-document";
  }
  
  public Link JAVASCRIPT(final Client session) {
    String _JAVASCRIPT = N.JAVASCRIPT();
    return session.link(_JAVASCRIPT);
  }
  
  public static String CSS() {
    return "https://u1.linnk.it/qc8sbw/usr/apps/textsync/upload/CSSDocument";
  }
  
  public Link CSS(final Client session) {
    String _CSS = N.CSS();
    return session.link(_CSS);
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

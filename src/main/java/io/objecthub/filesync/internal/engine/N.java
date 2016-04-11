package io.objecthub.filesync.internal.engine;

import com.appjangle.api.Client;
import com.appjangle.api.Link;

@SuppressWarnings("all")
public class N {
  private final static String ID = "100";
  
  public static String getTypeLink(final String type) {
    return (((("https://beta.objecthub.io/dev/~" + N.ID) + "/~hub/hub/repo/~objects/~") + type) + "/versions/~*0");
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
  
  public static String ATTRIBUTE() {
    return N.getTypeLink("attribute");
  }
  
  public Link ATTRIBUTE(final Client client) {
    return N.getType(client, "attribute");
  }
  
  public static String CLASS() {
    return N.getTypeLink("class");
  }
  
  public Link CLASS(final Client client) {
    return N.getType(client, "class");
  }
  
  public static String RICHTEXT() {
    return "http://slicnet.com/mxrogm/mxrogm/data/stream/2014/3/28/n3";
  }
  
  public Link RICHTEXT(final Client session) {
    String _RICHTEXT = N.RICHTEXT();
    return session.link(_RICHTEXT);
  }
}

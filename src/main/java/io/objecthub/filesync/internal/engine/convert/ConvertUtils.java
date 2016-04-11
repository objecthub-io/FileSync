package io.objecthub.filesync.internal.engine.convert;

import com.appjangle.api.Client;
import com.appjangle.api.Link;
import com.appjangle.api.LinkList;
import com.appjangle.api.LinkListQuery;
import com.appjangle.api.Node;
import com.appjangle.api.Query;
import com.appjangle.api.operations.OperationsExtension;
import com.appjangle.api.queries.QueriesExtension;
import com.google.common.base.Objects;
import de.mxro.file.FileItem;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.async.helper.Aggregator;
import delight.functional.Closure;
import delight.functional.Success;
import io.nextweb.promise.DataOperation;
import io.nextweb.promise.DataPromise;
import io.nextweb.promise.callbacks.DataCallback;
import io.nextweb.promise.exceptions.DataExceptionManager;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.nextweb.promise.exceptions.UndefinedListener;
import io.nextweb.promise.exceptions.UndefinedResult;
import io.nextweb.promise.utils.CallbackUtils;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.NetworkOperationContext;
import io.objecthub.filesync.internal.engine.FileUtils;
import io.objecthub.filesync.internal.engine.N;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class ConvertUtils {
  private final List<String> labelTypes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(N.LABEL(), N.LABEL2(), N.LABEL3()));
  
  private final Map<String, String> textValueExtensions = Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(Pair.<String, String>of(N.HTML_VALUE(), ".html"), Pair.<String, String>of(N.TYPE(), ".type"), Pair.<String, String>of(N.CSS(), ".css"), Pair.<String, String>of(N.JAVASCRIPT(), ".js"), Pair.<String, String>of(N.COFFEESCRIPT(), ".coffee"), Pair.<String, String>of(N.RICHTEXT(), ".htm")));
  
  public boolean isTextValue(final String fileName) {
    boolean _xblockexpression = false;
    {
      final String ext = this.futils.getExtension(fileName);
      _xblockexpression = this.textValueExtensions.containsValue(("." + ext));
    }
    return _xblockexpression;
  }
  
  public boolean isTextType(final Link link) {
    Set<String> _keySet = this.textValueExtensions.keySet();
    String _uri = link.uri();
    return _keySet.contains(_uri);
  }
  
  public void getFileExtension(final Node forNode, final ValueCallback<String> cb) {
    final LinkListQuery qry = forNode.selectAllLinks();
    final ExceptionListener _function = new ExceptionListener() {
      @Override
      public void onFailure(final ExceptionResult er) {
        Throwable _exception = er.exception();
        cb.onFailure(_exception);
      }
    };
    qry.catchExceptions(_function);
    final Closure<LinkList> _function_1 = new Closure<LinkList>() {
      @Override
      public void apply(final LinkList links) {
        Set<Map.Entry<String, String>> _entrySet = ConvertUtils.this.textValueExtensions.entrySet();
        for (final Map.Entry<String, String> mapping : _entrySet) {
          String _key = mapping.getKey();
          boolean _contains = links.contains(_key);
          if (_contains) {
            String _value = mapping.getValue();
            cb.onSuccess(_value);
            return;
          }
        }
      }
    };
    qry.get(_function_1);
  }
  
  public void deleteNodes(final Metadata metadata, final ItemMetadata cachedFile, final ValueCallback<List<NetworkOperation>> cb) {
    final String address = cachedFile.uri();
    final LinkedList<NetworkOperation> ops = new LinkedList<NetworkOperation>();
    final NetworkOperation _function = new NetworkOperation() {
      @Override
      public void apply(final NetworkOperationContext ctx, final ValueCallback<List<DataOperation<?>>> opscb) {
        String _name = cachedFile.name();
        metadata.remove(_name);
        Client _session = ctx.session();
        final Link nodeToBeRemoved = _session.link(address);
        final Node parent = ctx.parent();
        final ArrayList<DataOperation<?>> list = new ArrayList<DataOperation<?>>();
        Client _client = parent.client();
        Link _link = _client.link(parent);
        boolean _hasDirectChild = ConvertUtils.this.qxt.hasDirectChild(_link, nodeToBeRemoved);
        if (_hasDirectChild) {
          DataExceptionManager _exceptionManager = nodeToBeRemoved.getExceptionManager();
          final Closure<Success> _function = new Closure<Success>() {
            @Override
            public void apply(final Success it) {
              opscb.onSuccess(list);
            }
          };
          ValueCallback<Success> _embed = AsyncCommon.<Success>embed(opscb, _function);
          final DataCallback<Success> innercb = CallbackUtils.<Success>asDataCallback(_exceptionManager, _embed);
          ConvertUtils.this.ext.removeRecursive(parent, nodeToBeRemoved, innercb);
        } else {
          DataPromise<Success> _remove = parent.remove(nodeToBeRemoved);
          list.add(_remove);
          opscb.onSuccess(list);
        }
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  public Query appendLabel(final Query toNode, final String label) {
    Query _appendSafe = toNode.appendSafe(label, "./.label");
    Client _client = toNode.client();
    Link _LABEL = this.n.LABEL(_client);
    return _appendSafe.appendSafe(_LABEL);
  }
  
  public List<DataOperation<?>> appendTypesAndIcon(final Query toNode, final FileItem source) {
    ArrayList<DataOperation<?>> _xblockexpression = null;
    {
      final ArrayList<DataOperation<?>> res = CollectionLiterals.<DataOperation<?>>newArrayList();
      final Client session = toNode.client();
      String ext = source.getExtension();
      ext = ("." + ext);
      boolean _equals = Objects.equal(ext, ".html");
      if (_equals) {
        Link _HTML_VALUE = this.n.HTML_VALUE(session);
        Query _appendSafe = toNode.appendSafe(_HTML_VALUE);
        res.add(_appendSafe);
        final Query icon = toNode.appendSafe("https://appjangle.com/files/img/20141029/HTML.png", "./.icon");
        res.add(icon);
        Link _ICON = this.n.ICON(session);
        Query _appendSafe_1 = icon.appendSafe(_ICON);
        res.add(_appendSafe_1);
      } else {
        boolean _equals_1 = Objects.equal(ext, ".htm");
        if (_equals_1) {
          Link _RICHTEXT = this.n.RICHTEXT(session);
          Query _appendSafe_2 = toNode.appendSafe(_RICHTEXT);
          res.add(_appendSafe_2);
          final Query icon_1 = toNode.appendSafe("https://appjangle.com/files/img/20141119/RTF.png", "./.icon");
          res.add(icon_1);
          Link _ICON_1 = this.n.ICON(session);
          Query _appendSafe_3 = icon_1.appendSafe(_ICON_1);
          res.add(_appendSafe_3);
        } else {
          boolean _equals_2 = Objects.equal(ext, ".js");
          if (_equals_2) {
            Link _JAVASCRIPT = this.n.JAVASCRIPT(session);
            Query _appendSafe_4 = toNode.appendSafe(_JAVASCRIPT);
            res.add(_appendSafe_4);
            final Query icon_2 = toNode.appendSafe("https://appjangle.com/files/img/20141029/JavaScript.png", "./.icon");
            res.add(icon_2);
            Link _ICON_2 = this.n.ICON(session);
            Query _appendSafe_5 = icon_2.appendSafe(_ICON_2);
            res.add(_appendSafe_5);
          } else {
            boolean _equals_3 = Objects.equal(ext, ".coffee");
            if (_equals_3) {
              Link _COFFEESCRIPT = this.n.COFFEESCRIPT(session);
              Query _appendSafe_6 = toNode.appendSafe(_COFFEESCRIPT);
              res.add(_appendSafe_6);
              final Query icon_3 = toNode.appendSafe("https://appjangle.com/files/img/20141118/Coffeescript.png", "./.icon");
              res.add(icon_3);
              Link _ICON_3 = this.n.ICON(session);
              Query _appendSafe_7 = icon_3.appendSafe(_ICON_3);
              res.add(_appendSafe_7);
            } else {
              boolean _equals_4 = Objects.equal(ext, ".css");
              if (_equals_4) {
                Link _CSS = this.n.CSS(session);
                Query _appendSafe_8 = toNode.appendSafe(_CSS);
                res.add(_appendSafe_8);
                final Query icon_4 = toNode.appendSafe("https://appjangle.com/files/img/20141118/CSS.png", "./.icon");
                res.add(icon_4);
                Link _ICON_4 = this.n.ICON(session);
                Query _appendSafe_9 = icon_4.appendSafe(_ICON_4);
                res.add(_appendSafe_9);
              } else {
                boolean _equals_5 = Objects.equal(ext, ".type");
                if (_equals_5) {
                  Link _TYPE = this.n.TYPE(session);
                  Query _appendSafe_10 = toNode.appendSafe(_TYPE);
                  res.add(_appendSafe_10);
                  final Query icon_5 = toNode.appendSafe("https://appjangle.com/files/img/20141118/Type.png", "./.icon");
                  res.add(icon_5);
                  Link _ICON_5 = this.n.ICON(session);
                  Query _appendSafe_11 = icon_5.appendSafe(_ICON_5);
                  res.add(_appendSafe_11);
                  final Query description = toNode.appendSafe("");
                  res.add(description);
                  Link _link = session.link("http://slicnet.com/mxrogm/mxrogm/data/stream/2013/12/11/n9");
                  Query _appendSafe_12 = description.appendSafe(_link);
                  res.add(_appendSafe_12);
                }
              }
            }
          }
        }
      }
      _xblockexpression = res;
    }
    return _xblockexpression;
  }
  
  public final static Object NO_VALUE = new Object();
  
  public void getFileName(final Node forNode, final FileItem inFolder, final String fileExtension, final ValueCallback<String> cb) {
    final Closure<String> _function = new Closure<String>() {
      @Override
      public void apply(final String fileNameFromNode) {
        String fileName = (fileNameFromNode + fileExtension);
        int idx = 1;
        while (inFolder.get(fileName).exists()) {
          {
            fileName = ((fileNameFromNode + Integer.valueOf(idx)) + fileExtension);
            idx++;
          }
        }
        cb.onSuccess(fileName);
      }
    };
    ValueCallback<String> _embed = AsyncCommon.<String>embed(cb, _function);
    this.getFileName(forNode, _embed);
  }
  
  public void getFileName(final Node fromNode, final ValueCallback<String> cb) {
    int _size = this.labelTypes.size();
    final Closure<List<Object>> _function = new Closure<List<Object>>() {
      @Override
      public void apply(final List<Object> res) {
        for (final Object item : res) {
          if ((item instanceof String)) {
            cb.onSuccess(((String)item));
            return;
          }
        }
        String _uri = fromNode.uri();
        String _nameFromUri = ConvertUtils.getNameFromUri(_uri);
        cb.onSuccess(_nameFromUri);
      }
    };
    ValueCallback<List<Object>> _embed = AsyncCommon.<List<Object>>embed(cb, _function);
    final Aggregator<Object> cbs = AsyncCommon.<Object>collect(_size, _embed);
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String labelType) {
        Client _client = fromNode.client();
        Link _link = _client.link(labelType);
        final Query qry = fromNode.select(_link);
        final ValueCallback<Object> itmcb = cbs.createCallback();
        final UndefinedListener _function = new UndefinedListener() {
          @Override
          public void onUndefined(final UndefinedResult it) {
            itmcb.onSuccess(ConvertUtils.NO_VALUE);
          }
        };
        qry.catchUndefined(_function);
        final ExceptionListener _function_1 = new ExceptionListener() {
          @Override
          public void onFailure(final ExceptionResult er) {
            Throwable _exception = er.exception();
            itmcb.onFailure(_exception);
          }
        };
        qry.catchExceptions(_function_1);
        final Closure<Node> _function_2 = new Closure<Node>() {
          @Override
          public void apply(final Node label) {
            Object _value = label.value();
            itmcb.onSuccess(_value);
          }
        };
        qry.get(_function_2);
      }
    };
    this.labelTypes.forEach(_function_1);
  }
  
  public static String getNameFromUri(final String uri) {
    int _lastIndexOf = uri.lastIndexOf("/");
    int _plus = (_lastIndexOf + 1);
    return uri.substring(_plus);
  }
  
  @Extension
  private N n = new N();
  
  @Extension
  private OperationsExtension ext = new OperationsExtension();
  
  @Extension
  private QueriesExtension qxt = new QueriesExtension();
  
  @Extension
  private FileUtils futils = new FileUtils();
}

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
import delight.functional.Closure;
import delight.functional.Success;
import io.nextweb.promise.DataOperation;
import io.nextweb.promise.callbacks.DataCallback;
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
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class ConvertUtils {
  private final Map<String, String> textValueExtensions = Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(Pair.<String, String>of(N.HTML_VALUE(), ".html"), Pair.<String, String>of(N.ATTRIBUTE(), ".attribute"), Pair.<String, String>of(N.CLASS(), ".clazz"), Pair.<String, String>of(N.CSS(), ".css"), Pair.<String, String>of(N.MICRO_LIBRARY(), ".js"), Pair.<String, String>of(N.PLAIN_JS(), ".js"), Pair.<String, String>of(N.COFFEESCRIPT(), ".coffee"), Pair.<String, String>of(N.RICHTEXT(), ".htm")));
  
  public void getFileExtension(final Node forNode, final ValueCallback<String> cb) {
    final LinkListQuery qry = forNode.selectAllLinks();
    final ExceptionListener _function = new ExceptionListener() {
      @Override
      public void onFailure(final ExceptionResult er) {
        cb.onFailure(er.exception());
      }
    };
    qry.catchExceptions(_function);
    final Closure<LinkList> _function_1 = new Closure<LinkList>() {
      @Override
      public void apply(final LinkList links) {
        Set<Map.Entry<String, String>> _entrySet = ConvertUtils.this.textValueExtensions.entrySet();
        for (final Map.Entry<String, String> mapping : _entrySet) {
          boolean _contains = links.contains(mapping.getKey());
          if (_contains) {
            cb.onSuccess(mapping.getValue());
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
        metadata.remove(cachedFile.name());
        final Link nodeToBeRemoved = ctx.session().link(address);
        final Node parent = ctx.parent();
        final ArrayList<DataOperation<?>> list = new ArrayList<DataOperation<?>>();
        boolean _hasDirectChild = ConvertUtils.this.qxt.hasDirectChild(parent.client().link(parent), nodeToBeRemoved);
        if (_hasDirectChild) {
          final Closure<Success> _function = new Closure<Success>() {
            @Override
            public void apply(final Success it) {
              opscb.onSuccess(list);
            }
          };
          final DataCallback<Success> innercb = CallbackUtils.<Success>asDataCallback(nodeToBeRemoved.getExceptionManager(), AsyncCommon.<Success>embed(opscb, _function));
          ConvertUtils.this.ext.removeRecursive(parent, nodeToBeRemoved, innercb);
        } else {
          list.add(parent.remove(nodeToBeRemoved));
          opscb.onSuccess(list);
        }
      }
    };
    ops.add(_function);
    cb.onSuccess(ops);
  }
  
  public void appendLabel(final Query toNode, final String label) {
    throw new RuntimeException("Not supported!");
  }
  
  public List<DataOperation<?>> appendTypesAndIcon(final Query toNode, final FileItem source) {
    final ArrayList<Query> res = CollectionLiterals.<Query>newArrayList();
    final Client session = toNode.client();
    String ext = source.getExtension();
    ext = ("." + ext);
    boolean _equals = Objects.equal(ext, ".html");
    if (_equals) {
      res.add(toNode.appendSafe(this.n.HTML_VALUE(session)));
    } else {
      boolean _equals_1 = Objects.equal(ext, ".htm");
      if (_equals_1) {
        res.add(toNode.appendSafe(this.n.RICHTEXT(session)));
      } else {
        boolean _equals_2 = Objects.equal(ext, ".js");
        if (_equals_2) {
          res.add(toNode.appendSafe(this.n.MICRO_LIBRARY(session)));
        } else {
          boolean _equals_3 = Objects.equal(ext, ".coffee");
          if (_equals_3) {
            res.add(toNode.appendSafe(this.n.COFFEESCRIPT(session)));
          } else {
            boolean _equals_4 = Objects.equal(ext, ".css");
            if (_equals_4) {
              res.add(toNode.appendSafe(this.n.CSS(session)));
            } else {
              boolean _equals_5 = Objects.equal(ext, ".type");
              if (_equals_5) {
                res.add(toNode.appendSafe(this.n.ATTRIBUTE(session)));
              }
            }
          }
        }
      }
    }
    throw new RuntimeException("Not supported!");
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
    this.getFileName(forNode, 
      AsyncCommon.<String>embed(cb, _function));
  }
  
  public void getFileName(final Node fromNode, final ValueCallback<String> cb) {
    final Query qry = fromNode.select("./.meta/title");
    qry.catchExceptions(CallbackUtils.<String>asExceptionListener(cb));
    final UndefinedListener _function = new UndefinedListener() {
      @Override
      public void onUndefined(final UndefinedResult it) {
        cb.onSuccess(ConvertUtils.getNameFromUri(fromNode.uri()));
      }
    };
    qry.catchUndefined(_function);
    final Closure<Node> _function_1 = new Closure<Node>() {
      @Override
      public void apply(final Node title) {
        cb.onSuccess(title.<String>value(String.class));
      }
    };
    qry.get(_function_1);
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

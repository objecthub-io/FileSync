package io.objecthub.filesync;

import com.appjangle.api.Client;
import com.appjangle.api.Link;
import com.appjangle.api.Node;
import com.appjangle.api.nodes.Token;
import de.mxro.file.FileItem;
import de.mxro.file.Jre.FilesJre;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.functional.Closure2;
import delight.functional.Function;
import delight.functional.Success;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.objecthub.filesync.ItemMetadata;
import io.objecthub.filesync.Metadata;
import io.objecthub.filesync.SyncNotifications;
import io.objecthub.filesync.SyncParams;
import io.objecthub.filesync.SynchronizationSettings;
import io.objecthub.filesync.SynchronizationState;
import io.objecthub.filesync.internal.engine.FileUtils;
import io.objecthub.filesync.internal.engine.N;
import io.objecthub.filesync.internal.engine.SyncFolder;
import io.objecthub.filesync.internal.engine.convert.ConvertUtils;
import io.objecthub.filesync.internal.engine.convert.ConverterCollection;
import io.objecthub.filesync.internal.engine.convert.FileToTextNode;
import io.objecthub.filesync.internal.engine.convert.FolderToNode;
import io.objecthub.filesync.internal.engine.convert.FolderToNothing;
import io.objecthub.filesync.internal.engine.convert.NodeToNothing;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class FileSync {
  public static void syncSingleFolder(final SyncParams params, final ValueCallback<Success> cb) {
    SyncFolder _syncFolder = new SyncFolder(params);
    _syncFolder.doIt(cb);
  }
  
  public static SyncParams defaultSyncParams() {
    final SyncParams params = new SyncParams();
    ConverterCollection _createDefaultConverter = FileSync.createDefaultConverter();
    params.setConverter(_createDefaultConverter);
    SynchronizationSettings _synchronizationSettings = new SynchronizationSettings();
    params.setSettings(_synchronizationSettings);
    params.setState(new SynchronizationState() {
    });
    SyncNotifications _syncNotifications = new SyncNotifications();
    params.setNotifications(_syncNotifications);
    LinkedList<Link> _linkedList = new LinkedList<Link>();
    params.setSyncRoots(_linkedList);
    LinkedList<Link> _linkedList_1 = new LinkedList<Link>();
    params.setDontFollow(_linkedList_1);
    return params;
  }
  
  /**
   * <p>Synchronized the contents of a folder and a node without synchronizing sub-folders.
   */
  public static void syncSingleFolder(final File folder, final Node node, final ValueCallback<Success> cb) {
    FileItem _wrap = FilesJre.wrap(folder);
    FileSync.syncSingleFolder(_wrap, node, cb);
  }
  
  /**
   * <p>Synchronized the contents of a folder and a node without synchronizing sub-folders.
   */
  public static void syncSingleFolder(final FileItem folder, final Node node, final ValueCallback<Success> cb) {
    final SyncParams params = FileSync.defaultSyncParams();
    params.setFolder(folder);
    params.setNode(node);
    FileSync.syncSingleFolder(params, cb);
  }
  
  private static void syncInt(final SyncParams params, final ValueCallback<Success> cb) {
    SynchronizationState _state = params.getState();
    Node _node = params.getNode();
    boolean _wasSynced = _state.wasSynced(_node);
    if (_wasSynced) {
      SyncNotifications _notifications = params.getNotifications();
      FileItem _folder = params.getFolder();
      Node _node_1 = params.getNode();
      _notifications.onNodeSkippedBecauseItWasAlreadySynced(_folder, _node_1);
      cb.onSuccess(Success.INSTANCE);
      return;
    }
    SynchronizationState _state_1 = params.getState();
    Node _node_2 = params.getNode();
    _state_1.addSynced(_node_2);
    final Closure<Success> _function = new Closure<Success>() {
      @Override
      public void apply(final Success it) {
        FileItem _folder = params.getFolder();
        List<FileItem> _children = _folder.getChildren();
        final Function1<FileItem, Boolean> _function = new Function1<FileItem, Boolean>() {
          @Override
          public Boolean apply(final FileItem it) {
            boolean _and = false;
            boolean _and_1 = false;
            boolean _isDirectory = it.isDirectory();
            if (!_isDirectory) {
              _and_1 = false;
            } else {
              boolean _visible = it.getVisible();
              _and_1 = _visible;
            }
            if (!_and_1) {
              _and = false;
            } else {
              String _name = it.getName();
              boolean _startsWith = _name.startsWith(".");
              boolean _not = (!_startsWith);
              _and = _not;
            }
            return Boolean.valueOf(_and);
          }
        };
        final Iterable<FileItem> toSync = IterableExtensions.<FileItem>filter(_children, _function);
        List<FileItem> _list = IterableExtensions.<FileItem>toList(toSync);
        final Closure2<FileItem, ValueCallback<Success>> _function_1 = new Closure2<FileItem, ValueCallback<Success>>() {
          @Override
          public void apply(final FileItem childFolder, final ValueCallback<Success> itmcb) {
            FileItem _folder = params.getFolder();
            final Metadata metadata = FileSync.fileUtils.loadMetadata(_folder);
            String _name = childFolder.getName();
            final ItemMetadata itmmetadata = metadata.get(_name);
            String _uri = itmmetadata.uri();
            Node _node = params.getNode();
            String _uri_1 = _node.uri();
            final boolean isChild = _uri.startsWith(_uri_1);
            boolean withinSyncRoots = false;
            Link matchedSyncRoot = null;
            List<Link> _syncRoots = params.getSyncRoots();
            for (final Link syncRoot : _syncRoots) {
              String _uri_2 = itmmetadata.uri();
              String _uri_3 = syncRoot.uri();
              boolean _startsWith = _uri_2.startsWith(_uri_3);
              if (_startsWith) {
                withinSyncRoots = true;
                matchedSyncRoot = syncRoot;
              }
            }
            if (((!isChild) && (!withinSyncRoots))) {
              itmcb.onSuccess(Success.INSTANCE);
              return;
            }
            boolean inDontFollow = false;
            List<Link> _dontFollow = params.getDontFollow();
            for (final Link dontFollow : _dontFollow) {
              String _uri_4 = itmmetadata.uri();
              String _uri_5 = dontFollow.uri();
              boolean _equals = _uri_4.equals(_uri_5);
              if (_equals) {
                inDontFollow = true;
              }
            }
            if (inDontFollow) {
              itmcb.onSuccess(Success.INSTANCE);
              return;
            }
            Link qry = null;
            boolean _and = false;
            boolean _and_1 = false;
            if (!withinSyncRoots) {
              _and_1 = false;
            } else {
              String _secret = matchedSyncRoot.secret();
              boolean _tripleNotEquals = (_secret != null);
              _and_1 = _tripleNotEquals;
            }
            if (!_and_1) {
              _and = false;
            } else {
              String _secret_1 = matchedSyncRoot.secret();
              int _length = _secret_1.length();
              boolean _greaterThan = (_length > 0);
              _and = _greaterThan;
            }
            if (_and) {
              Node _node_1 = params.getNode();
              Client _client = _node_1.client();
              String _uri_6 = itmmetadata.uri();
              String _secret_2 = matchedSyncRoot.secret();
              Link _link = _client.link(_uri_6, _secret_2);
              qry = _link;
            } else {
              Node _node_2 = params.getNode();
              Client _client_1 = _node_2.client();
              String _uri_7 = itmmetadata.uri();
              Link _link_1 = _client_1.link(_uri_7);
              qry = _link_1;
            }
            final ExceptionListener _function = new ExceptionListener() {
              @Override
              public void onFailure(final ExceptionResult er) {
                Throwable _exception = er.exception();
                itmcb.onFailure(_exception);
              }
            };
            qry.catchExceptions(_function);
            final Closure<Node> _function_1 = new Closure<Node>() {
              @Override
              public void apply(final Node childNode) {
                final SyncParams childParams = new SyncParams(params);
                childParams.setFolder(childFolder);
                childParams.setNode(childNode);
                String _uri = childNode.uri();
                String _plus = ("Processing " + _uri);
                InputOutput.<String>println(_plus);
                String _uri_1 = childNode.uri();
                boolean _startsWith = _uri_1.startsWith("http://localhost");
                if (_startsWith) {
                  String _uri_2 = childNode.uri();
                  String _plus_1 = ("ERROR: Illegal node " + _uri_2);
                  String _plus_2 = (_plus_1 + " with parent ");
                  Node _node = params.getNode();
                  String _uri_3 = _node.uri();
                  String _plus_3 = (_plus_2 + _uri_3);
                  InputOutput.<String>println(_plus_3);
                  itmcb.onSuccess(Success.INSTANCE);
                  return;
                }
                FileSync.syncInt(childParams, itmcb);
              }
            };
            qry.get(_function_1);
          }
        };
        final Closure<List<Success>> _function_2 = new Closure<List<Success>>() {
          @Override
          public void apply(final List<Success> it) {
            cb.onSuccess(Success.INSTANCE);
          }
        };
        ValueCallback<List<Success>> _embed = AsyncCommon.<List<Success>>embed(cb, _function_2);
        AsyncCommon.<FileItem, Success>forEach(_list, _function_1, _embed);
      }
    };
    ValueCallback<Success> _embed = AsyncCommon.<Success>embed(cb, _function);
    FileSync.syncSingleFolder(params, _embed);
  }
  
  public static void sync(final SyncParams params, final ValueCallback<Success> cb) {
    List<Link> _syncRoots = params.getSyncRoots();
    int _size = _syncRoots.size();
    boolean _equals = (_size == 0);
    if (_equals) {
      List<Link> _syncRoots_1 = params.getSyncRoots();
      Node _node = params.getNode();
      Client _client = _node.client();
      Node _node_1 = params.getNode();
      Link _link = _client.link(_node_1);
      _syncRoots_1.add(_link);
    }
    FileSync.syncInt(params, cb);
  }
  
  /**
   * <p>Synchronized the contents of the specified folder with the specified nodes and does the same for all sub-folders and child nodes.
   */
  public static void sync(final FileItem folder, final Node node, final ValueCallback<Success> cb) {
    final SyncParams params = FileSync.defaultSyncParams();
    params.setFolder(folder);
    params.setNode(node);
    FileSync.sync(params, cb);
  }
  
  public static ConverterCollection createDefaultConverter() {
    ConverterCollection _xblockexpression = null;
    {
      final ConverterCollection coll = new ConverterCollection();
      String _HTML_VALUE = N.HTML_VALUE();
      FileToTextNode _fileToTextNode = new FileToTextNode(".html", _HTML_VALUE, "./baseHtml");
      coll.addConverter(_fileToTextNode);
      String _MICRO_LIBRARY = N.MICRO_LIBRARY();
      FileToTextNode _fileToTextNode_1 = new FileToTextNode(".js", _MICRO_LIBRARY, "./baseJs");
      coll.addConverter(_fileToTextNode_1);
      String _COFFEESCRIPT = N.COFFEESCRIPT();
      FileToTextNode _fileToTextNode_2 = new FileToTextNode(".coffee", _COFFEESCRIPT, "./baseCS");
      coll.addConverter(_fileToTextNode_2);
      String _CSS = N.CSS();
      FileToTextNode _fileToTextNode_3 = new FileToTextNode(".css", _CSS, "./baseCss");
      coll.addConverter(_fileToTextNode_3);
      String _CLASS = N.CLASS();
      FileToTextNode _fileToTextNode_4 = new FileToTextNode(".clazz", _CLASS, null);
      coll.addConverter(_fileToTextNode_4);
      String _ATTRIBUTE = N.ATTRIBUTE();
      FileToTextNode _fileToTextNode_5 = new FileToTextNode(".attribute", _ATTRIBUTE, null);
      coll.addConverter(_fileToTextNode_5);
      String _PLAIN_JS = N.PLAIN_JS();
      FileToTextNode _fileToTextNode_6 = new FileToTextNode(".js", _PLAIN_JS, null);
      coll.addConverter(_fileToTextNode_6);
      final Closure2<Node, ValueCallback<Boolean>> _function = new Closure2<Node, ValueCallback<Boolean>>() {
        @Override
        public void apply(final Node node, final ValueCallback<Boolean> cb) {
          Object _value = node.value();
          cb.onSuccess(Boolean.valueOf((_value instanceof Token)));
        }
      };
      NodeToNothing _nodeToNothing = new NodeToNothing(_function);
      coll.addConverter(_nodeToNothing);
      final Closure2<Node, ValueCallback<Boolean>> _function_1 = new Closure2<Node, ValueCallback<Boolean>>() {
        @Override
        public void apply(final Node node, final ValueCallback<Boolean> cb) {
          String _uri = node.uri();
          String _nameFromUri = ConvertUtils.getNameFromUri(_uri);
          boolean _startsWith = _nameFromUri.startsWith(".");
          cb.onSuccess(Boolean.valueOf(_startsWith));
        }
      };
      NodeToNothing _nodeToNothing_1 = new NodeToNothing(_function_1);
      coll.addConverter(_nodeToNothing_1);
      final Function<FileItem, Boolean> _function_2 = new Function<FileItem, Boolean>() {
        @Override
        public Boolean apply(final FileItem file) {
          boolean _or = false;
          String _name = file.getName();
          boolean _startsWith = _name.startsWith(".");
          if (_startsWith) {
            _or = true;
          } else {
            boolean _visible = file.getVisible();
            boolean _not = (!_visible);
            _or = _not;
          }
          return Boolean.valueOf(_or);
        }
      };
      FolderToNothing _folderToNothing = new FolderToNothing(_function_2);
      coll.addConverter(_folderToNothing);
      FolderToNode _folderToNode = new FolderToNode();
      coll.addConverter(_folderToNode);
      _xblockexpression = coll;
    }
    return _xblockexpression;
  }
  
  @Extension
  private static FileUtils fileUtils = new FileUtils();
}

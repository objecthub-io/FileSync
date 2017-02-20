package io.objecthub.filesync;

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
    new SyncFolder(params).doIt(cb);
  }
  
  public static SyncParams defaultSyncParams() {
    final SyncParams params = new SyncParams();
    params.setConverter(FileSync.createDefaultConverter());
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
    FileSync.syncSingleFolder(FilesJre.wrap(folder), node, cb);
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
    boolean _wasSynced = params.getState().wasSynced(params.getNode());
    if (_wasSynced) {
      params.getNotifications().onNodeSkippedBecauseItWasAlreadySynced(params.getFolder(), params.getNode());
      cb.onSuccess(Success.INSTANCE);
      return;
    }
    params.getState().addSynced(params.getNode());
    final Closure<Success> _function = new Closure<Success>() {
      @Override
      public void apply(final Success it) {
        final Function1<FileItem, Boolean> _function = new Function1<FileItem, Boolean>() {
          @Override
          public Boolean apply(final FileItem it) {
            return Boolean.valueOf(((it.isDirectory() && it.getVisible()) && (!it.getName().startsWith("."))));
          }
        };
        final Iterable<FileItem> toSync = IterableExtensions.<FileItem>filter(params.getFolder().getChildren(), _function);
        final Closure2<FileItem, ValueCallback<Success>> _function_1 = new Closure2<FileItem, ValueCallback<Success>>() {
          @Override
          public void apply(final FileItem childFolder, final ValueCallback<Success> itmcb) {
            final Metadata metadata = FileSync.fileUtils.loadMetadata(params.getFolder());
            final ItemMetadata itmmetadata = metadata.get(childFolder.getName());
            final boolean isChild = itmmetadata.uri().startsWith(params.getNode().uri());
            boolean withinSyncRoots = false;
            Link matchedSyncRoot = null;
            List<Link> _syncRoots = params.getSyncRoots();
            for (final Link syncRoot : _syncRoots) {
              boolean _startsWith = itmmetadata.uri().startsWith(syncRoot.uri());
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
              boolean _equals = itmmetadata.uri().equals(dontFollow.uri());
              if (_equals) {
                inDontFollow = true;
              }
            }
            if (inDontFollow) {
              itmcb.onSuccess(Success.INSTANCE);
              return;
            }
            Link qry = null;
            if (((withinSyncRoots && (matchedSyncRoot.secret() != null)) && (matchedSyncRoot.secret().length() > 0))) {
              qry = params.getNode().client().link(itmmetadata.uri(), matchedSyncRoot.secret());
            } else {
              qry = params.getNode().client().link(itmmetadata.uri());
            }
            final ExceptionListener _function = new ExceptionListener() {
              @Override
              public void onFailure(final ExceptionResult er) {
                itmcb.onFailure(er.exception());
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
                boolean _startsWith = childNode.uri().startsWith("http://localhost");
                if (_startsWith) {
                  String _uri_1 = childNode.uri();
                  String _plus_1 = ("ERROR: Illegal node " + _uri_1);
                  String _plus_2 = (_plus_1 + " with parent ");
                  String _uri_2 = params.getNode().uri();
                  String _plus_3 = (_plus_2 + _uri_2);
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
        AsyncCommon.<FileItem, Success>forEach(IterableExtensions.<FileItem>toList(toSync), _function_1, 
          AsyncCommon.<List<Success>>embed(cb, _function_2));
      }
    };
    FileSync.syncSingleFolder(params, 
      AsyncCommon.<Success>embed(cb, _function));
  }
  
  public static void sync(final SyncParams params, final ValueCallback<Success> cb) {
    int _size = params.getSyncRoots().size();
    boolean _equals = (_size == 0);
    if (_equals) {
      params.getSyncRoots().add(params.getNode().client().link(params.getNode()));
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
      FileToTextNode _fileToTextNode = new FileToTextNode("html", ".html", _HTML_VALUE, "./baseHtml");
      coll.addConverter(_fileToTextNode);
      String _MICRO_LIBRARY = N.MICRO_LIBRARY();
      FileToTextNode _fileToTextNode_1 = new FileToTextNode("js", ".js", _MICRO_LIBRARY, "./baseJs");
      coll.addConverter(_fileToTextNode_1);
      String _COFFEESCRIPT = N.COFFEESCRIPT();
      FileToTextNode _fileToTextNode_2 = new FileToTextNode("coffee", ".coffee", _COFFEESCRIPT, "./baseCS");
      coll.addConverter(_fileToTextNode_2);
      String _CSS = N.CSS();
      FileToTextNode _fileToTextNode_3 = new FileToTextNode("css", ".css", _CSS, "./baseCss");
      coll.addConverter(_fileToTextNode_3);
      String _CLASS = N.CLASS();
      FileToTextNode _fileToTextNode_4 = new FileToTextNode("clazz", ".clazz", _CLASS, null);
      coll.addConverter(_fileToTextNode_4);
      String _ATTRIBUTE = N.ATTRIBUTE();
      FileToTextNode _fileToTextNode_5 = new FileToTextNode("attribute", ".attribute", _ATTRIBUTE, null);
      coll.addConverter(_fileToTextNode_5);
      String _PLAIN_JS = N.PLAIN_JS();
      FileToTextNode _fileToTextNode_6 = new FileToTextNode("plain-js", ".js", _PLAIN_JS, null);
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
          cb.onSuccess(Boolean.valueOf(ConvertUtils.getNameFromUri(node.uri()).startsWith(".")));
        }
      };
      NodeToNothing _nodeToNothing_1 = new NodeToNothing(_function_1);
      coll.addConverter(_nodeToNothing_1);
      final Function<FileItem, Boolean> _function_2 = new Function<FileItem, Boolean>() {
        @Override
        public Boolean apply(final FileItem file) {
          return Boolean.valueOf((file.getName().startsWith(".") || (!file.getVisible())));
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

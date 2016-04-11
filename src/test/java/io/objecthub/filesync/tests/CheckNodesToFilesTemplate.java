package io.objecthub.filesync.tests;

import com.appjangle.api.Client;
import com.appjangle.api.Node;
import com.appjangle.api.Query;
import com.appjangle.api.common.LocalServer;
import com.appjangle.api.servers.jre.Servers;
import com.appjangle.jre.Clients;
import de.mxro.file.FileItem;
import de.mxro.file.Jre.FilesJre;
import delight.async.Operation;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.functional.Success;
import io.nextweb.promise.DataPromise;
import io.objecthub.filesync.FileSync;
import java.io.File;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("all")
public abstract class CheckNodesToFilesTemplate {
  protected LocalServer server;
  
  protected Client session;
  
  protected Node source;
  
  protected File target;
  
  protected FileItem result;
  
  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();
  
  @Before
  public void setUp() {
    try {
      LocalServer _createAndStart = Servers.createAndStart();
      this.server = _createAndStart;
      Client _create = Clients.create(this.server);
      this.session = _create;
      Query _seed = this.session.seed(this.server);
      Node _get = _seed.get();
      this.source = _get;
      File _newFolder = this.tempFolder.newFolder("sync1");
      this.target = _newFolder;
      FileItem _wrap = FilesJre.wrap(this.target);
      this.result = _wrap;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected boolean doRecursiveSync() {
    return false;
  }
  
  protected abstract void step1_defineData();
  
  protected abstract void step2_assertFiles();
  
  @Test
  public void test() {
    this.step1_defineData();
    DataPromise<Success> _commit = this.session.commit();
    _commit.get();
    final Operation<Success> _function = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> cb) {
        boolean _doRecursiveSync = CheckNodesToFilesTemplate.this.doRecursiveSync();
        boolean _not = (!_doRecursiveSync);
        if (_not) {
          FileSync.syncSingleFolder(CheckNodesToFilesTemplate.this.result, CheckNodesToFilesTemplate.this.source, cb);
        } else {
          FileSync.sync(CheckNodesToFilesTemplate.this.result, CheckNodesToFilesTemplate.this.source, cb);
        }
      }
    };
    Async.<Success>waitFor(_function);
    this.step2_assertFiles();
  }
  
  @After
  public void tearDown() {
    DataPromise<Success> _close = this.session.close();
    _close.get();
    DataPromise<Success> _shutdown = this.server.shutdown();
    _shutdown.get();
  }
}

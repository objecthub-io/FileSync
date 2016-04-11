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
public abstract class CheckFilesToNodesTemplate {
  protected LocalServer server;
  
  protected Client session;
  
  protected Node result;
  
  protected File sourceFolder;
  
  protected FileItem source;
  
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
      this.result = _get;
      File _newFolder = this.tempFolder.newFolder("sync1");
      this.sourceFolder = _newFolder;
      FileItem _wrap = FilesJre.wrap(this.sourceFolder);
      this.source = _wrap;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected abstract void step1_defineFiles();
  
  protected abstract void step2_assertNodes();
  
  @Test
  public void test() {
    final Operation<Success> _function = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> cb) {
        FileSync.syncSingleFolder(CheckFilesToNodesTemplate.this.sourceFolder, CheckFilesToNodesTemplate.this.result, cb);
      }
    };
    Async.<Success>waitFor(_function);
    this.step1_defineFiles();
    final Operation<Success> _function_1 = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> cb) {
        FileSync.syncSingleFolder(CheckFilesToNodesTemplate.this.sourceFolder, CheckFilesToNodesTemplate.this.result, cb);
      }
    };
    Async.<Success>waitFor(_function_1);
    DataPromise<Success> _commit = this.session.commit();
    _commit.get();
    this.step2_assertNodes();
  }
  
  @After
  public void tearDown() {
    DataPromise<Success> _close = this.session.close();
    _close.get();
    DataPromise<Success> _shutdown = this.server.shutdown();
    _shutdown.get();
  }
}

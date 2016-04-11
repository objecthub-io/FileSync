package io.objecthub.filesync.tests;

import delight.async.Operation;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.functional.Success;
import io.nextweb.promise.DataPromise;
import io.objecthub.filesync.FileSync;
import io.objecthub.filesync.tests.CheckNodesToFilesTemplate;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class CheckUpdatesTemplate extends CheckNodesToFilesTemplate {
  protected abstract void step3_updateNodes();
  
  protected abstract void step4_assertFilesAfterUpdate();
  
  protected abstract void step5_updateFiles();
  
  protected abstract void step6_assertNodesAfterUpdate();
  
  @Test
  @Override
  public void test() {
    try {
      this.step1_defineData();
      DataPromise<Success> _commit = this.session.commit();
      _commit.get();
      final Operation<Success> _function = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          FileSync.syncSingleFolder(CheckUpdatesTemplate.this.target, CheckUpdatesTemplate.this.source, cb);
        }
      };
      Async.<Success>waitFor(_function);
      this.step2_assertFiles();
      this.step3_updateNodes();
      DataPromise<Success> _commit_1 = this.session.commit();
      _commit_1.get();
      final Operation<Success> _function_1 = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          FileSync.syncSingleFolder(CheckUpdatesTemplate.this.target, CheckUpdatesTemplate.this.source, cb);
        }
      };
      Async.<Success>waitFor(_function_1);
      this.step4_assertFilesAfterUpdate();
      Thread.sleep(2000);
      this.step5_updateFiles();
      final Operation<Success> _function_2 = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          FileSync.syncSingleFolder(CheckUpdatesTemplate.this.target, CheckUpdatesTemplate.this.source, cb);
        }
      };
      Async.<Success>waitFor(_function_2);
      this.step6_assertNodesAfterUpdate();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

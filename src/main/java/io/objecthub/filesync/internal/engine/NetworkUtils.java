package io.objecthub.filesync.internal.engine;

import com.appjangle.api.Client;
import com.appjangle.api.Node;
import com.appjangle.api.Query;
import delight.async.AsyncCommon;
import delight.async.callbacks.ValueCallback;
import delight.async.helper.Aggregator;
import delight.functional.Closure;
import delight.functional.Success;
import io.nextweb.promise.DataOperation;
import io.nextweb.promise.DataPromise;
import io.nextweb.promise.exceptions.ExceptionListener;
import io.nextweb.promise.exceptions.ExceptionResult;
import io.objecthub.filesync.NetworkOperation;
import io.objecthub.filesync.NetworkOperationContext;
import java.util.List;

@SuppressWarnings("all")
public class NetworkUtils {
  public DataPromise<Success> execute(final List<NetworkOperation> ops, final Node onNode, final ValueCallback<Success> cb) {
    DataPromise<Success> _xblockexpression = null;
    {
      final NetworkOperationContext ctx = new NetworkOperationContext() {
        @Override
        public Client session() {
          return onNode.client();
        }
        
        @Override
        public Node parent() {
          return onNode;
        }
      };
      final Closure<List<Success>> _function = new Closure<List<Success>>() {
        @Override
        public void apply(final List<Success> it) {
          cb.onSuccess(Success.INSTANCE);
        }
      };
      final Aggregator<Success> opscbs = AsyncCommon.<Success>collect(ops.size(), 
        AsyncCommon.<List<Success>>embed(cb, _function));
      for (final NetworkOperation op : ops) {
        final Closure<List<DataOperation<?>>> _function_1 = new Closure<List<DataOperation<?>>>() {
          @Override
          public void apply(final List<DataOperation<?>> qries) {
            final ValueCallback<Success> opscbsitem = opscbs.createCallback();
            final Closure<List<Success>> _function = new Closure<List<Success>>() {
              @Override
              public void apply(final List<Success> it) {
                opscbsitem.onSuccess(Success.INSTANCE);
              }
            };
            final Aggregator<Success> cbs = AsyncCommon.<Success>collect(qries.size(), 
              AsyncCommon.<List<Success>>embed(cb, _function));
            for (final DataOperation<?> qry : qries) {
              {
                final ValueCallback<Success> itmcb = cbs.createCallback();
                if ((qry instanceof Query)) {
                  final ExceptionListener _function_1 = new ExceptionListener() {
                    @Override
                    public void onFailure(final ExceptionResult er) {
                      itmcb.onFailure(er.exception());
                    }
                  };
                  ((Query)qry).catchExceptions(_function_1);
                  final Closure<Node> _function_2 = new Closure<Node>() {
                    @Override
                    public void apply(final Node succ) {
                      itmcb.onSuccess(Success.INSTANCE);
                    }
                  };
                  ((Query)qry).get(_function_2);
                } else {
                  if ((qry instanceof DataPromise<?>)) {
                    final DataPromise<Object> safeQry = ((DataPromise<Object>) qry);
                    final ExceptionListener _function_3 = new ExceptionListener() {
                      @Override
                      public void onFailure(final ExceptionResult er) {
                        itmcb.onFailure(er.exception());
                      }
                    };
                    safeQry.catchExceptions(_function_3);
                    final Closure<Object> _function_4 = new Closure<Object>() {
                      @Override
                      public void apply(final Object succ) {
                        itmcb.onSuccess(Success.INSTANCE);
                      }
                    };
                    safeQry.get(_function_4);
                  } else {
                    Class<? extends DataOperation> _class = qry.getClass();
                    String _plus = ("Unsupported pending query: " + _class);
                    throw new RuntimeException(_plus);
                  }
                }
              }
            }
          }
        };
        op.apply(ctx, 
          AsyncCommon.<List<DataOperation<?>>>embed(cb, _function_1));
      }
      _xblockexpression = onNode.client().commit();
    }
    return _xblockexpression;
  }
}

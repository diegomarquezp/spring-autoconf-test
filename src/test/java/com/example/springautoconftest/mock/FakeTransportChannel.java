package com.example.springautoconftest.mock;

import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.TransportChannel;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class FakeTransportChannel implements TransportChannel {
  private final FakeChannel channel;
  private volatile boolean isShutdown = false;
  private volatile Map<String, String> headers;
  private volatile Executor executor;

  public FakeTransportChannel(FakeChannel channel) {
    this.channel = channel;
  }

  public static FakeTransportChannel create(FakeChannel channel) {
    return new FakeTransportChannel(channel);
  }

  /** The name of the Fake transport. */
  public static String getFakeTransportName() {
    return "httpjson";
  }

  @Override
  public String getTransportName() {
    return getFakeTransportName();
  }

  @Override
  public FakeCallContext getEmptyCallContext() {
    return FakeCallContext.createDefault();
  }

  @Override
  public void shutdown() {
    isShutdown = true;
  }

  @Override
  public boolean isShutdown() {
    return isShutdown;
  }

  @Override
  public boolean isTerminated() {
    return isShutdown;
  }

  @Override
  public void shutdownNow() {
    isShutdown = true;
  }

  @Override
  public boolean awaitTermination(long duration, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public void close() {}

  public FakeChannel getChannel() {
    return channel;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String> getHeaders() {
    return this.headers;
  }

  public void setExecutor(Executor executor) {
    this.executor = executor;
  }

  public Executor getExecutor() {
    return executor;
  }
}

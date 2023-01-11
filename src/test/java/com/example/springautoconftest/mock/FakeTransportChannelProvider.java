package com.example.springautoconftest.mock;

import com.google.api.client.util.Preconditions;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.Credentials;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public class FakeTransportChannelProvider implements TransportChannelProvider {

  private final TransportChannel transportChannel;

  public FakeTransportChannelProvider(TransportChannel transportChannel) {
    this.transportChannel = Preconditions.checkNotNull(transportChannel);
  }

  @Override
  public boolean shouldAutoClose() {
    return false;
  }

  @Override
  public boolean needsExecutor() {
    return false;
  }

  @Override
  public FakeTransportChannelProvider withExecutor(ScheduledExecutorService executor) {
    return withExecutor((Executor) executor);
  }

  @Override
  public FakeTransportChannelProvider withExecutor(Executor executor) {
    throw new UnsupportedOperationException(
        "FixedTransportChannelProvider doesn't need an executor");
  }

  @Override
  public boolean needsHeaders() {
    return false;
  }

  @Override
  public FakeTransportChannelProvider withHeaders(Map<String, String> headers) {
    throw new UnsupportedOperationException("FixedTransportChannelProvider doesn't need headers");
  }

  @Override
  public boolean needsEndpoint() {
    return false;
  }

  @Override
  public TransportChannelProvider withEndpoint(String endpoint) {
    throw new UnsupportedOperationException(
        "FixedTransportChannelProvider doesn't need an endpoint");
  }

  /** @deprecated FixedTransportChannelProvider doesn't support ChannelPool configuration */
  @Deprecated
  @Override
  public boolean acceptsPoolSize() {
    return false;
  }

  /** @deprecated FixedTransportChannelProvider doesn't support ChannelPool configuration */
  @Deprecated
  @Override
  public TransportChannelProvider withPoolSize(int size) {
    throw new UnsupportedOperationException(
        "FixedTransportChannelProvider doesn't allow pool size customization");
  }

  @Override
  public TransportChannel getTransportChannel() throws IOException {
    return transportChannel;
  }

  @Override
  public String getTransportName() {
    return transportChannel.getTransportName();
  }

  @Override
  public boolean needsCredentials() {
    return false;
  }

  @Override
  public TransportChannelProvider withCredentials(Credentials credentials) {
    throw new UnsupportedOperationException(
        "FixedTransportChannelProvider doesn't need credentials");
  }

  /** Creates a FixedTransportChannelProvider. */
  public static FakeTransportChannelProvider create(TransportChannel transportChannel) {
    return new FakeTransportChannelProvider(transportChannel);
  }
}

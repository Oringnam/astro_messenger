package astro.com.samples;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.13.1)",
    comments = "Source: sample.proto")
public final class SampleGrpc {

  private SampleGrpc() {}

  public static final String SERVICE_NAME = "sample.Sample";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<astro.com.samples.AstroMessage,
      astro.com.samples.Result> getSendMessageMethod;

  public static io.grpc.MethodDescriptor<astro.com.samples.AstroMessage,
      astro.com.samples.Result> getSendMessageMethod() {
    io.grpc.MethodDescriptor<astro.com.samples.AstroMessage, astro.com.samples.Result> getSendMessageMethod;
    if ((getSendMessageMethod = SampleGrpc.getSendMessageMethod) == null) {
      synchronized (SampleGrpc.class) {
        if ((getSendMessageMethod = SampleGrpc.getSendMessageMethod) == null) {
          SampleGrpc.getSendMessageMethod = getSendMessageMethod = 
              io.grpc.MethodDescriptor.<astro.com.samples.AstroMessage, astro.com.samples.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "sample.Sample", "sendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  astro.com.samples.AstroMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  astro.com.samples.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new SampleMethodDescriptorSupplier("sendMessage"))
                  .build();
          }
        }
     }
     return getSendMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SampleStub newStub(io.grpc.Channel channel) {
    return new SampleStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SampleBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SampleBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SampleFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SampleFutureStub(channel);
  }

  /**
   */
  public static abstract class SampleImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendMessage(astro.com.samples.AstroMessage request,
        io.grpc.stub.StreamObserver<astro.com.samples.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                astro.com.samples.AstroMessage,
                astro.com.samples.Result>(
                  this, METHODID_SEND_MESSAGE)))
          .build();
    }
  }

  /**
   */
  public static final class SampleStub extends io.grpc.stub.AbstractStub<SampleStub> {
    private SampleStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SampleStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SampleStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SampleStub(channel, callOptions);
    }

    /**
     */
    public void sendMessage(astro.com.samples.AstroMessage request,
        io.grpc.stub.StreamObserver<astro.com.samples.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SampleBlockingStub extends io.grpc.stub.AbstractStub<SampleBlockingStub> {
    private SampleBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SampleBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SampleBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SampleBlockingStub(channel, callOptions);
    }

    /**
     */
    public astro.com.samples.Result sendMessage(astro.com.samples.AstroMessage request) {
      return blockingUnaryCall(
          getChannel(), getSendMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SampleFutureStub extends io.grpc.stub.AbstractStub<SampleFutureStub> {
    private SampleFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SampleFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SampleFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SampleFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<astro.com.samples.Result> sendMessage(
        astro.com.samples.AstroMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_MESSAGE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SampleImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SampleImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_MESSAGE:
          serviceImpl.sendMessage((astro.com.samples.AstroMessage) request,
              (io.grpc.stub.StreamObserver<astro.com.samples.Result>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class SampleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SampleBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return astro.com.samples.SampleProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Sample");
    }
  }

  private static final class SampleFileDescriptorSupplier
      extends SampleBaseDescriptorSupplier {
    SampleFileDescriptorSupplier() {}
  }

  private static final class SampleMethodDescriptorSupplier
      extends SampleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SampleMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SampleGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SampleFileDescriptorSupplier())
              .addMethod(getSendMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}

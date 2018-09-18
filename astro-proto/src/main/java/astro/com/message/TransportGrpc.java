package astro.com.message;

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
    comments = "Source: MessageStructure.proto")
public final class TransportGrpc {

  private TransportGrpc() {}

  public static final String SERVICE_NAME = "Transport";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<astro.com.message.AstroMessage,
      astro.com.message.Return> getSendMessageMethod;

  public static io.grpc.MethodDescriptor<astro.com.message.AstroMessage,
      astro.com.message.Return> getSendMessageMethod() {
    io.grpc.MethodDescriptor<astro.com.message.AstroMessage, astro.com.message.Return> getSendMessageMethod;
    if ((getSendMessageMethod = TransportGrpc.getSendMessageMethod) == null) {
      synchronized (TransportGrpc.class) {
        if ((getSendMessageMethod = TransportGrpc.getSendMessageMethod) == null) {
          TransportGrpc.getSendMessageMethod = getSendMessageMethod = 
              io.grpc.MethodDescriptor.<astro.com.message.AstroMessage, astro.com.message.Return>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Transport", "sendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  astro.com.message.AstroMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  astro.com.message.Return.getDefaultInstance()))
                  .setSchemaDescriptor(new TransportMethodDescriptorSupplier("sendMessage"))
                  .build();
          }
        }
     }
     return getSendMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<astro.com.message.ACK,
      astro.com.message.Return> getSendACKMethod;

  public static io.grpc.MethodDescriptor<astro.com.message.ACK,
      astro.com.message.Return> getSendACKMethod() {
    io.grpc.MethodDescriptor<astro.com.message.ACK, astro.com.message.Return> getSendACKMethod;
    if ((getSendACKMethod = TransportGrpc.getSendACKMethod) == null) {
      synchronized (TransportGrpc.class) {
        if ((getSendACKMethod = TransportGrpc.getSendACKMethod) == null) {
          TransportGrpc.getSendACKMethod = getSendACKMethod = 
              io.grpc.MethodDescriptor.<astro.com.message.ACK, astro.com.message.Return>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Transport", "sendACK"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  astro.com.message.ACK.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  astro.com.message.Return.getDefaultInstance()))
                  .setSchemaDescriptor(new TransportMethodDescriptorSupplier("sendACK"))
                  .build();
          }
        }
     }
     return getSendACKMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TransportStub newStub(io.grpc.Channel channel) {
    return new TransportStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TransportBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TransportBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TransportFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TransportFutureStub(channel);
  }

  /**
   */
  public static abstract class TransportImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendMessage(astro.com.message.AstroMessage request,
        io.grpc.stub.StreamObserver<astro.com.message.Return> responseObserver) {
      asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
    }

    /**
     */
    public void sendACK(astro.com.message.ACK request,
        io.grpc.stub.StreamObserver<astro.com.message.Return> responseObserver) {
      asyncUnimplementedUnaryCall(getSendACKMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendMessageMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                astro.com.message.AstroMessage,
                astro.com.message.Return>(
                  this, METHODID_SEND_MESSAGE)))
          .addMethod(
            getSendACKMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                astro.com.message.ACK,
                astro.com.message.Return>(
                  this, METHODID_SEND_ACK)))
          .build();
    }
  }

  /**
   */
  public static final class TransportStub extends io.grpc.stub.AbstractStub<TransportStub> {
    private TransportStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TransportStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransportStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TransportStub(channel, callOptions);
    }

    /**
     */
    public void sendMessage(astro.com.message.AstroMessage request,
        io.grpc.stub.StreamObserver<astro.com.message.Return> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendACK(astro.com.message.ACK request,
        io.grpc.stub.StreamObserver<astro.com.message.Return> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendACKMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TransportBlockingStub extends io.grpc.stub.AbstractStub<TransportBlockingStub> {
    private TransportBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TransportBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransportBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TransportBlockingStub(channel, callOptions);
    }

    /**
     */
    public astro.com.message.Return sendMessage(astro.com.message.AstroMessage request) {
      return blockingUnaryCall(
          getChannel(), getSendMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public astro.com.message.Return sendACK(astro.com.message.ACK request) {
      return blockingUnaryCall(
          getChannel(), getSendACKMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TransportFutureStub extends io.grpc.stub.AbstractStub<TransportFutureStub> {
    private TransportFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TransportFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransportFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TransportFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<astro.com.message.Return> sendMessage(
        astro.com.message.AstroMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<astro.com.message.Return> sendACK(
        astro.com.message.ACK request) {
      return futureUnaryCall(
          getChannel().newCall(getSendACKMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_MESSAGE = 0;
  private static final int METHODID_SEND_ACK = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TransportImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TransportImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_MESSAGE:
          serviceImpl.sendMessage((astro.com.message.AstroMessage) request,
              (io.grpc.stub.StreamObserver<astro.com.message.Return>) responseObserver);
          break;
        case METHODID_SEND_ACK:
          serviceImpl.sendACK((astro.com.message.ACK) request,
              (io.grpc.stub.StreamObserver<astro.com.message.Return>) responseObserver);
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

  private static abstract class TransportBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TransportBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return astro.com.message.MessageProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Transport");
    }
  }

  private static final class TransportFileDescriptorSupplier
      extends TransportBaseDescriptorSupplier {
    TransportFileDescriptorSupplier() {}
  }

  private static final class TransportMethodDescriptorSupplier
      extends TransportBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TransportMethodDescriptorSupplier(String methodName) {
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
      synchronized (TransportGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TransportFileDescriptorSupplier())
              .addMethod(getSendMessageMethod())
              .addMethod(getSendACKMethod())
              .build();
        }
      }
    }
    return result;
  }
}

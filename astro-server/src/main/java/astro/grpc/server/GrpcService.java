package astro.grpc.server;

import astro.com.message.AstroMessage;
import astro.com.message.Return;
import astro.com.message.TransportGrpc;
import astro.grpc.server.basic.ServerQueue;
import io.grpc.stub.StreamObserver;

public class GrpcService extends TransportGrpc.TransportImplBase {
    private ServerQueue queue;

    public GrpcService(ServerQueue queue) {
        this.queue = queue;
    }

    @Override
    public void sendMessage(AstroMessage message, StreamObserver<Return> responseObserver) {
        Return result = null;

        result = messageErrorValidate(message);
        if(result != null) {
            Server.astromonitor.failedTransferMessageCount(message.getIndex());
            responseObserver.onNext(result);
            responseObserver.onCompleted();

            return;
        }

        boolean putSwitch = queue.put(message);

        if(!putSwitch) {
            Server.astromonitor.failedTransferMessageCount(message.getIndex());
            result = Return.newBuilder().setReturnCode(Return.storingErrorCode.Queue_Full_VALUE).build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();

            return;
        }


        Server.astromonitor.increaseTransferMessageCount();

        result = Return.newBuilder().setReturnCode(Return.successCode.Success_VALUE).build();
        responseObserver.onNext(result);
        responseObserver.onCompleted();
        return;
    }

    private Return messageErrorValidate(AstroMessage message) {
        Return result = null;

        if (message == null) {
            result = Return.newBuilder().setReturnCode(Return.messageErrorCode.AstroMessage_Null_VALUE).build();
        }

        if (message.getUuid() == null) {
            result = Return.newBuilder().setReturnCode(Return.messageErrorCode.Uuid_Null_VALUE).build();
        }

        if (message.getTopic() == null) {
            result = Return.newBuilder().setReturnCode(Return.messageErrorCode.Topic_Null_VALUE).build();
        }

        if (message.getMessage() == null) {
            result = Return.newBuilder().setReturnCode(Return.messageErrorCode.Message_Null_VALUE).build();
        }

        if (message.getDatetime() == 0) {
            result = Return.newBuilder().setReturnCode(Return.messageErrorCode.Date_Zero_VALUE).build();
        }

        if (message.getIndex() < 0) {
            result = Return.newBuilder().setReturnCode(Return.messageErrorCode.Index_Invalid_VALUE).build();
        }

        return result;
    }


}

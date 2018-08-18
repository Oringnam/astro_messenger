package astro.grpc.server;

import astro.com.message.AstroMessage;
import astro.com.message.Return;
import astro.com.message.TransportGrpc;
import astro.grpc.server.basic.ServerQueue;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageImplementation extends TransportGrpc.TransportImplBase {
    private ServerQueue queue;

    public MessageImplementation(ServerQueue queue) {
        this.queue = queue;
    }

    @Override
    public void sendMessage(AstroMessage message, StreamObserver<Return> responseObserver) {
        if (message == null) {
            Server.astromonitor.failedTransferMessageCount(message.getIndex());
            Return result = Return.newBuilder().setReturnCode(1).build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
            return;
        }

        try {
            queue.put(message);

            Server.astromonitor.increaseTransferMessageCount();

            Return result = Return.newBuilder().setReturnCode(0).build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}

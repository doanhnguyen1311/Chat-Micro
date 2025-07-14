package doanh.io.account_service.grpc;

import account_service.Common.IdRequest;
import account_service.Common.APIResponse;
import doanh.io.account_service.service.SecurityService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class SecurityGrpcService extends SecurityServiceGrpc.SecurityServiceImplBase {

    private final SecurityService securityService;

    @Override
    public void getTokenVersion(IdRequest request, StreamObserver<TokenVersionResponse> responseObserver) {
        var apiResponse = securityService.getTokenVersion(request.getId());

        int tokenVersion = 0;
        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
            tokenVersion = (Integer) apiResponse.getData();
        }

        responseObserver.onNext(TokenVersionResponse.newBuilder()
                .setTokenVersion(tokenVersion)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void incrementLoginAttempts(EmailRequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.incrementLoginAttempts(request.getEmail());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void resetLoginAttempts(IdRequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.resetLoginAttempts(request.getId());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void lockAccount(IdRequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.lockAccount(request.getId());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void unlockAccount(IdRequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.unlockAccount(request.getId());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void verifyEmail(IdRequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.verifyEmail(request.getId());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void enableMFA(EnableMFARequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.enableMFA(request.getId(), request.getMfaType(), request.getMfaSecret());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void disableMFA(IdRequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.disableMFA(request.getId());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void increaseTokenVersion(IdRequest request, StreamObserver<APIResponse> responseObserver) {
        var api = securityService.increaseTokenVersion(request.getId());

        APIResponse response = APIResponse.newBuilder()
                .setSuccess(api.isSuccess())
                .setMessage(api.getMessage())
                .setStatusCode(api.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}

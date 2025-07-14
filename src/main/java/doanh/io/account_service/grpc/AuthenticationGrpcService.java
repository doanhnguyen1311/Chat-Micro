package doanh.io.account_service.grpc;

import account_service.Common;
import doanh.io.account_service.entity.Account;
import doanh.io.account_service.entity.info.SecurityInfo;
import doanh.io.account_service.repository.AccountRepository;
import doanh.io.account_service.service.AuthenticationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.crypto.password.PasswordEncoder;

@GrpcService
@RequiredArgsConstructor
public class AuthenticationGrpcService extends AuthenticationServiceGrpc.AuthenticationServiceImplBase {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        var rq = new doanh.io.account_service.dto.request.LoginRequest(request.getUsername(), request.getPassword());
        var account = authenticationService.login(rq);

        if (account == null) {
            // Trả về response thất bại
            LoginResponse response = LoginResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Thông tin đăng nhập không đúng hoặc tài khoản bị khóa")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        LoginResponse response = LoginResponse.newBuilder()
                .setIsVerified(Boolean.TRUE.equals(account.getIsVerified()))
                .setAccountId(account.getId() == null ? "" : account.getId())
                .setMfaType(account.getMfaType() == null ? "" : account.getMfaType())
                .setMfaEnabled(Boolean.TRUE.equals(account.getIsMfaEnabled()))
                .setSuccess(true)
                .setMessage("Đăng nhập thành công")
                .build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }



    @Override
    public void logout(Common.IdRequest request, StreamObserver<Common.APIResponse> responseObserver) {
        Common.APIResponse response = Common.APIResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Logout thành công")
                .setStatusCode(200)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void sendLoginResponse(boolean success, String message, Account account, StreamObserver<LoginResponse> observer) {
        LoginResponse.Builder builder = LoginResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message);

        if (account != null) {
            var security = account.getSecurity();
            builder
                    .setAccountId(account.getId())
                    .setIsVerified(security.getIsVerified())
                    .setMfaEnabled(security.getMfaEnabled())
                    .setMfaType(security.getMfaType() == null ? "" : security.getMfaType());
        }

        observer.onNext(builder.build());
        observer.onCompleted();
    }

    private boolean isPasswordValid(String password, Account account) {
        return passwordEncoder.matches(password, account.getPassword());
    }
}

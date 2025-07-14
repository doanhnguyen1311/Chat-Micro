//package doanh.io.account_service;
//
//import net.devh.boot.grpc.client.inject.GrpcClient;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SecurityGrpcClientRunner implements CommandLineRunner {
//
//    @GrpcClient("account-service")
//    private SecurityServiceGrpc.SecurityServiceBlockingStub securityStub;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Tạo request
//        EmailRequest request = EmailRequest.newBuilder()
//                .setEmail("doanh@example.com")
//                .build();
//
//        // Gọi RPC
//        TokenVersionResponse response = securityStub.getTokenVersion(request);
//
//        // Hiển thị kết quả
//        System.out.println("Token version for email: " + request.getEmail() +
//                " is " + response.getTokenVersion());
//    }
//}
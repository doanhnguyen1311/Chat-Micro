package doanh.io.account_service.grpc;

import doanh.io.account_service.service.AccountService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;


@GrpcService
@RequiredArgsConstructor
public class AccountGrpcService extends AccountServiceGrpc.AccountServiceImplBase {

    private final AccountService accountService;

    @Override
    public void getAll(Empty request, StreamObserver<AccountDTO> responseObserver) {
        var apiResponse = accountService.getAll(); // trả về List<AccountDTO>
        List<doanh.io.account_service.dto.AccountDTO> list =
                (List<doanh.io.account_service.dto.AccountDTO>) apiResponse.getData();

        for (doanh.io.account_service.dto.AccountDTO account : list) {
            AccountDTO.Builder accountBuilder = AccountDTO.newBuilder()
                    .setId(account.getId())
                    .setUsername(account.getUsername())
                    .setEmail(account.getEmail())
                    .setPhoneNumber(account.getPhoneNumber())
                    .setPassword(account.getPassword())
                    .setStatus(account.getStatus());

            if (account.getProfile() != null) {
                accountBuilder.setProfile(UserProfileDTO.newBuilder()
                        .setFullName(account.getProfile().getFullName())
                        .setAvatarUrl(account.getProfile().getAvatarUrl())
                        .setCoverPhotoUrl(account.getProfile().getCoverPhotoUrl())
                        .setBio(account.getProfile().getBio())
                        .setGender(account.getProfile().getGender())
                        .setLocation(account.getProfile().getLocation())
                        .setWebsite(account.getProfile().getWebsite())
                );
            }

            if (account.getSettings() != null) {
                accountBuilder.setSettings(SettingsDTO.newBuilder()
                        .setTheme(account.getSettings().getTheme())
                        .setLanguage(account.getSettings().getLanguage())
                        .setSoundOn(account.getSettings().isSoundOn())
                        .setNotificationsEnabled(account.getSettings().isNotificationsEnabled())
                );
            }

            responseObserver.onNext(accountBuilder.build());
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getOne(AccountIdRequest request, StreamObserver<AccountResponse> responseObserver) {
        var apiResponse = accountService.getOne(request.getId());

        doanh.io.account_service.dto.AccountDTO accountDto =
                (doanh.io.account_service.dto.AccountDTO) apiResponse.getData();

        AccountDTO.Builder accountBuilder = AccountDTO.newBuilder()
                .setId(accountDto.getId())
                .setUsername(accountDto.getUsername())
                .setEmail(accountDto.getEmail())
                .setPhoneNumber(accountDto.getPhoneNumber())
                .setPassword(accountDto.getPassword())
                .setStatus(accountDto.getStatus());

        // Profile
        if (accountDto.getProfile() != null) {
            var profile = accountDto.getProfile();
            UserProfileDTO profileDto = UserProfileDTO.newBuilder()
                    .setFullName(profile.getFullName())
                    .setAvatarUrl(profile.getAvatarUrl())
                    .setCoverPhotoUrl(profile.getCoverPhotoUrl())
                    .setBio(profile.getBio())
                    .setGender(profile.getGender())
                    .setLocation(profile.getLocation())
                    .setWebsite(profile.getWebsite())
                    .build();
            accountBuilder.setProfile(profileDto);
        }

        // Settings
        if (accountDto.getSettings() != null) {
            var settings = accountDto.getSettings();
            SettingsDTO settingsDto = SettingsDTO.newBuilder()
                    .setTheme(settings.getTheme())
                    .setLanguage(settings.getLanguage())
                    .setSoundOn(settings.isSoundOn())
                    .setNotificationsEnabled(settings.isNotificationsEnabled())
                    .build();
            accountBuilder.setSettings(settingsDto);
        }

        // Provider
        if (accountDto.getProvider() != null) {
            var provider = accountDto.getProvider();
            ProviderInfoDTO providerDto = ProviderInfoDTO.newBuilder()
                    .setProvider(provider.getProvider())
                    .setProviderId(provider.getProviderId() != null ? provider.getProviderId() : "")
                    .build();
            accountBuilder.setProvider(providerDto);
        }

        AccountResponse response = AccountResponse.newBuilder()
                .setAccount(accountBuilder)
                .setSuccess(apiResponse.isSuccess())
                .setMessage(apiResponse.getMessage())
                .setStatusCode(apiResponse.getStatusCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}

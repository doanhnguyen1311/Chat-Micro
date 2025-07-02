package doanh.io.account_service.controller;

import doanh.io.account_service.dto.APIResponse;
import doanh.io.account_service.dto.UserProfileDTO; // This DTO might not be directly used if APIResponse wraps all data
import doanh.io.account_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<?>> getProfile(@PathVariable String id) {
        APIResponse<?> apiResponse = userProfileService.getProfile(id);

        HttpStatus httpStatus = apiResponse.isSuccess() ? HttpStatus.valueOf(apiResponse.getStatusCode()) : HttpStatus.BAD_REQUEST;
        if (!apiResponse.isSuccess() && apiResponse.getStatusCode() == 404) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(apiResponse, httpStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<?>> updateProfile(@PathVariable String id,
                                                        @RequestBody UserProfileDTO profileDTO) {
        APIResponse<?> apiResponse = userProfileService.updateProfile(id, profileDTO);

        HttpStatus httpStatus = apiResponse.isSuccess() ? HttpStatus.valueOf(apiResponse.getStatusCode()) : HttpStatus.BAD_REQUEST;
        if (!apiResponse.isSuccess() && apiResponse.getStatusCode() == 404) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}
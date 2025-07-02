package doanh.io.account_service.controller;

import doanh.io.account_service.dto.APIResponse;
import doanh.io.account_service.dto.SettingsDTO; // DTO này có thể không còn cần thiết nếu APIResponse bao bọc tất cả
import doanh.io.account_service.dto.request.SettingsUpdateRequest;
import doanh.io.account_service.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<?>> getSettings(@PathVariable String id) {
        APIResponse<?> apiResponse = settingsService.getSettings(id);

        HttpStatus httpStatus = apiResponse.isSuccess() ? HttpStatus.valueOf(apiResponse.getStatusCode()) : HttpStatus.BAD_REQUEST;
        if (!apiResponse.isSuccess() && apiResponse.getStatusCode() == 404) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(apiResponse, httpStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<?>> updateSettings(@PathVariable String id,
                                                         @RequestBody SettingsUpdateRequest request) {
        APIResponse<?> apiResponse = settingsService.updateSettings(id, request);

        HttpStatus httpStatus = apiResponse.isSuccess() ? HttpStatus.valueOf(apiResponse.getStatusCode()) : HttpStatus.BAD_REQUEST;
        if (!apiResponse.isSuccess() && apiResponse.getStatusCode() == 404) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}
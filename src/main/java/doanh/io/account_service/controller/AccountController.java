package doanh.io.account_service.controller;

import doanh.io.account_service.dto.AccountDTO;
import doanh.io.account_service.dto.APIResponse;
import doanh.io.account_service.dto.request.PasswordUpdaterRequest;
import doanh.io.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("")
    public ResponseEntity<APIResponse<?>> getAll() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<?>> getOne(@PathVariable String id) {
        var response = accountService.getOne(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse<?>> create(@RequestBody AccountDTO accountDTO) {
        var res = accountService.add(accountDTO);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<?>> update(@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        var res = accountService.update(id, accountDTO);
        return ResponseEntity
                .status(res.getStatusCode())
                .body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<?>> delete(@PathVariable String id) {
        var res = accountService.delete(id);
        return ResponseEntity
                .status(res.getStatusCode())
                .body(res);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<APIResponse<?>> updatePassword(@PathVariable String id, @RequestBody PasswordUpdaterRequest request) {
        var res = accountService.updatePassword(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity
                .status(res.getStatusCode())
                .body(res);
    }
}

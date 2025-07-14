package doanh.io.account_service.listener;

import doanh.io.account_service.dto.APIResponse;
import doanh.io.account_service.dto.AccountDTO;
import doanh.io.account_service.dto.request.UpdatePasswordRaw;
import doanh.io.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRpcListener {

    private final AccountService accountService;

    @RabbitListener(queues = "account.rpc.getAll")
    public APIResponse<?> getAll() {
        return accountService.getAll();
    }

    @RabbitListener(queues = "account.rpc.getOne")
    public APIResponse<?> getOne(String id) {
        return accountService.getOne(id);
    }

    @RabbitListener(queues = "account.rpc.add")
    public APIResponse<?> add(AccountDTO dto) {
        return accountService.add(dto);
    }

    @RabbitListener(queues = "account.rpc.update")
    public APIResponse<?> update(AccountDTO dto) {
        return accountService.update(dto.getId(), dto);
    }

    @RabbitListener(queues = "account.rpc.delete")
    public APIResponse<?> delete(String id) {
        return accountService.delete(id);
    }

    @RabbitListener(queues = "account.rpc.changePassword")
    public APIResponse<?> updatePassword(UpdatePasswordRaw req) {
        return accountService.updatePassword(req.getId(), req.getOldPassword(), req.getNewPassword());
    }
}

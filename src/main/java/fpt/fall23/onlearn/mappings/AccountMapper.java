package fpt.fall23.onlearn.mappings;
import fpt.fall23.onlearn.dto.account.response.AccountDetailResponse;
import fpt.fall23.onlearn.dto.account.response.AccountResponse;
import fpt.fall23.onlearn.entity.Account;

public interface AccountMapper {


    AccountResponse toResponse(Account account);

    AccountDetailResponse toDetailResponse(Account account);
}

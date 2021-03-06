package study.daydayup.wolf.business.uc.setting.biz.service.impl;

import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import study.daydayup.wolf.business.uc.setting.api.entity.AccountStatus;
import study.daydayup.wolf.business.uc.setting.api.service.AccountStatusService;
import study.daydayup.wolf.business.uc.setting.biz.dal.dao.AccountStatusDAO;
import study.daydayup.wolf.business.uc.setting.biz.dal.dataobject.AccountStatusDO;
import study.daydayup.wolf.framework.rpc.Result;
import study.daydayup.wolf.framework.rpc.RpcService;

import javax.annotation.Resource;

/**
 * study.daydayup.wolf.business.uc.setting.biz.service.impl
 *
 * @author Wingle
 * @since 2020/1/1 12:38 下午
 **/
@RpcService
public class AccountStatusServiceImpl implements AccountStatusService {
    @Resource
    private AccountStatusDAO dao;
    @Override
    public Result<AccountStatus> find(@NonNull Long accountId) {
        AccountStatusDO accountStatusDO = dao.findByAccountId(accountId);
        if (accountStatusDO == null) {
            return initStatus(accountId);
        }
        AccountStatus status = DOToModel(accountStatusDO);
        return Result.ok(status);
    }

    @Override
    public Result<Integer> save(@Validated AccountStatus accountStatus) {
        int status;
        AccountStatusDO accountStatusDO = dao.findByAccountId(accountStatus.getAccountId());
        if (accountStatusDO == null) {
            status = dao.insertSelective(modelToDO(accountStatus));
            return Result.ok(status);
        }

        status = dao.updateByAccountId(modelToDO(accountStatus), accountStatus.getAccountId());
        return Result.ok(status);
    }

    private Result<AccountStatus> initStatus(Long accountId) {
        AccountStatus status = new AccountStatus();
        status.setAccountId(accountId);

        AccountStatusDO statusDO = modelToDO(status);

        dao.insertSelective(statusDO);
        return Result.ok(status);
    }

    private AccountStatus DOToModel(AccountStatusDO accountStatusDO) {
        if (accountStatusDO == null) {
            return null;
        }

        AccountStatus accountStatus = new AccountStatus();
        BeanUtils.copyProperties(accountStatusDO, accountStatus);

        return accountStatus;
    }

    private AccountStatusDO modelToDO(AccountStatus accountStatus) {
        if (accountStatus == null) {
            return null;
        }

        AccountStatusDO accountStatusDO = new AccountStatusDO();
        BeanUtils.copyProperties(accountStatus, accountStatusDO);

        return accountStatusDO;
    }
}

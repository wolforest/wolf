package study.daydayup.wolf.business.org.biz.task.api;

import lombok.NonNull;
import study.daydayup.wolf.business.org.api.task.domain.enums.task.CollectionStateEnum;
import study.daydayup.wolf.business.org.api.task.dto.TaskOption;
import study.daydayup.wolf.business.org.api.task.service.task.CollectionTaskService;
import study.daydayup.wolf.business.org.biz.task.domain.entity.TaskEntity;
import study.daydayup.wolf.business.org.biz.task.domain.repository.TaskRepository;
import study.daydayup.wolf.framework.rpc.Result;
import study.daydayup.wolf.framework.rpc.RpcService;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * study.daydayup.wolf.business.org.biz.task.api
 *
 * @author Wingle
 * @since 2020/3/16 12:44 上午
 **/
@RpcService
public class CollectionTaskServiceImpl implements CollectionTaskService {
    @Resource
    private TaskRepository taskRepository;

    @Override
    public Result<Integer> partlyPay(@NonNull Long taskId, @NonNull Long orgId, @NonNull BigDecimal amount) {
        return changePayingState(taskId, orgId, CollectionStateEnum.PARTLY_PAYING.getCode(), amount);
    }

    @Override
    public Result<Integer> confirmPartlyPay(@NonNull Long taskId, @NonNull Long orgId, @NonNull BigDecimal amount) {
        return changePaidState(taskId, orgId, CollectionStateEnum.PARTLY_PAID.getCode(), amount);
    }

    @Override
    public Result<Integer> pay(@NonNull Long taskId, @NonNull Long orgId) {
        return changePayingState(taskId, orgId, CollectionStateEnum.PAYING.getCode());
    }

    @Override
    public Result<Integer> confirmPay(@NonNull Long taskId, @NonNull Long orgId) {
        return changePayingState(taskId, orgId, CollectionStateEnum.PAID.getCode());
    }

    @Override
    public Result<Integer> fail(@NonNull Long taskId, @NonNull Long orgId) {
        return changePayingState(taskId, orgId, CollectionStateEnum.FAILING.getCode());
    }

    @Override
    public Result<Integer> confirmFail(@NonNull Long taskId, @NonNull Long orgId) {
        return changePayingState(taskId, orgId, CollectionStateEnum.FAILED.getCode());
    }

    @Override
    public Result<Integer> questionPartlyPay(Long taskId, Long orgId) {
        return changePayingState(taskId, orgId, CollectionStateEnum.PARTLY_PAID_QUESTIONED.getCode());
    }

    @Override
    public Result<Integer> questionPay(Long taskId, Long orgId) {
        return changePayingState(taskId, orgId, CollectionStateEnum.PAID_QUESTIONED.getCode());
    }

    @Override
    public Result<Integer> questionFail(Long taskId, Long orgId) {
        return changePayingState(taskId, orgId, CollectionStateEnum.FAILED_QUESTIONED.getCode());
    }

    private Result<Integer> changePayingState(@NonNull Long taskId, @NonNull Long orgId, @NonNull Integer state) {
        TaskOption option = TaskOption.builder()
                .withDetail(false)
                .build();

        TaskEntity entity = taskRepository.find(taskId, orgId, option);
        entity.changeState(state);
        int status = taskRepository.save(entity);
        return Result.ok(status);
    }

    private Result<Integer> changePayingState(@NonNull Long taskId, @NonNull Long orgId, @NonNull Integer state, @NonNull BigDecimal amount) {
        TaskOption option = TaskOption.builder()
                .withDetail(false)
                .withTrade(true)
                .build();

        TaskEntity entity = taskRepository.find(taskId, orgId, option);
        entity.changeState(state);
        entity.changePayingAmount(amount);
        int status = taskRepository.save(entity);
        return Result.ok(status);
    }

    private Result<Integer> changePaidState(@NonNull Long taskId, @NonNull Long orgId, @NonNull Integer state, @NonNull BigDecimal amount) {
        TaskOption option = TaskOption.builder()
                .withDetail(false)
                .withTrade(true)
                .build();

        TaskEntity entity = taskRepository.find(taskId, orgId, option);
        entity.changeState(state);
        entity.changePaidAmount(amount);
        int status = taskRepository.save(entity);
        return Result.ok(status);
    }

}

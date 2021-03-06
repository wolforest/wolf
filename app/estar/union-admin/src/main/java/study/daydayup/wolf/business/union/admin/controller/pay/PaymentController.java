package study.daydayup.wolf.business.union.admin.controller.pay;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import study.daydayup.wolf.business.account.auth.agent.Session;
import study.daydayup.wolf.business.pay.api.domain.entity.Payment;
import study.daydayup.wolf.business.pay.api.dto.base.manage.PaymentQuery;
import study.daydayup.wolf.business.pay.api.service.PaymentService;
import study.daydayup.wolf.framework.layer.web.Controller;
import study.daydayup.wolf.framework.rpc.Result;
import study.daydayup.wolf.framework.rpc.page.Page;
import study.daydayup.wolf.framework.rpc.page.PageRequest;

import javax.annotation.Resource;

/**
 * study.daydayup.wolf.business.union.admin.controller.pay
 *
 * @author Wingle
 * @since 2020/6/22 11:40 上午
 **/
@RestController
public class PaymentController implements Controller {
    @Reference
    private PaymentService paymentService;
    @Resource
    private Session session;

    @GetMapping("/pay/payment/query")
    public Result<Page<Payment>> find(@Validated @ModelAttribute PaymentQuery query) {
        Long orgId = session.get("orgId", Long.class);
        query.setPayeeId(orgId);
        PageRequest pageRequest = PageRequest.builder()
                .pageNum(null == query.getPageNum() ? 1 : query.getPageNum())
                .pageSize(10)
                .build();

        return paymentService.query(query, pageRequest);
    }
}

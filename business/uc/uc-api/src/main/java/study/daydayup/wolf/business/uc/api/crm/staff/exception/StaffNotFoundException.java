package study.daydayup.wolf.business.uc.api.crm.staff.exception;

import study.daydayup.wolf.common.lang.exception.SystemException;

/**
 * study.daydayup.wolf.business.account.auth.agent.exception
 *
 * @author Wingle
 * @since 2019/12/12 3:04 下午
 **/
public class StaffNotFoundException extends SystemException {
    public StaffNotFoundException() {
        super(123000, "Can't find such staff" );
    }
}

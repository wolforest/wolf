package cn.coderule.common.sm;

import cn.coderule.common.sm.Event;
import cn.coderule.common.sm.State;
import cn.coderule.common.sm.StateGraph;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import cn.coderule.common.lang.enums.CodeBasedEnum;
import cn.coderule.common.sm.impl.DefaultStateGraph;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * com.wolf.common.sm
 *
 * @author Wingle
 * @since 2019/12/17 11:23 上午
 **/
public class DefaultStateGraphTest {

    @Test
    public void getInitState() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>(new WaitToPay());

        TradeState initState = stateGraph.getRootState();
        TradeState expected = new WaitToPay();
        assertEquals(expected.getClass().getName(), initState.getClass().getName(), "InitState failed");
    }

    @Test
    public void fire() {
        TradeState init = new WaitToPay();
        TradeState paid = new Paid();
        TradeState consigned = new Consigned();

        TradeEvent payEvent = new PayEvent();
        TradeEvent sendEvent = new SendEvent();

        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>(init);

        assertEquals(init, stateGraph.getRootState(), "init state fail");

        stateGraph.addTransition(init, paid, payEvent);
        stateGraph.addTransition(paid, consigned, sendEvent);

        TradeState expectedPaid = stateGraph.transit(init, payEvent);
        assertEquals(expectedPaid, paid, "paid event bind fail");

        TradeState expectedConsigned = stateGraph.transit(paid, sendEvent);
        assertEquals(expectedConsigned, consigned, "send event bind fail");
    }

    @Test
    public void test_different_event_instance() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>();

        TradeState paid = new Paid();
        TradeState consigned = new Consigned();
        TradeEvent sendEvent = new SendEvent();

        stateGraph.addTransition(paid, consigned, sendEvent);

        TradeState expectedState = stateGraph.transit(paid, new SendEvent());
        assertEquals(expectedState, consigned, "different event instance fail.");

    }

    @Test
    public void test_different_state_instance() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>();

        TradeState paid = new Paid();
        TradeState consigned = new Consigned();
        TradeEvent sendEvent = new SendEvent();

        stateGraph.addTransition(paid, consigned, sendEvent);

        TradeState expectedState = stateGraph.transit(new Paid(), new SendEvent());
        assertEquals(expectedState, consigned, "different source instance fail.");
    }

    @Test
    public void test_get_state_by_code() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>();

        TradeState paid = new Paid();
        TradeState consigned = new Consigned();
        TradeEvent sendEvent = new SendEvent();

        stateGraph.addTransition(paid, consigned, sendEvent);

        TradeState paidFromSM = stateGraph.getState(2);
        TradeState consignFromSM = stateGraph.getState(3);
        assertEquals(consignFromSM, consigned, "get state by code from StateMachine fail.");
        assertEquals(paid, paidFromSM, "get state by code from StateMachine fail.");
    }

    @Test
    public void test_get_bind_event_list() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>();

        TradeState paid = new Paid();
        TradeState consigned = new Consigned();
        TradeEvent sendEvent = new SendEvent();

        stateGraph.addTransition(paid, consigned, sendEvent);

        Set<TradeEvent> events = stateGraph.getTransitions(paid);
        assertTrue(events.contains(sendEvent), "get bind event list fail");
    }

    @Test
    public void test_enm_state() {
        StateGraph<PaymentStateEnum, PaymentEventEnum> stateGraph = new DefaultStateGraph<PaymentStateEnum, PaymentEventEnum>(PaymentStateEnum.WAIT_TO_PAY)
                .addTransition(PaymentStateEnum.WAIT_TO_PAY, PaymentStateEnum.PENDING, PaymentEventEnum.PAY)
                .addTransition(PaymentStateEnum.PENDING, PaymentStateEnum.FAILURE, PaymentEventEnum.FAIL)
                .addTransition(PaymentStateEnum.PENDING, PaymentStateEnum.SUCCESS, PaymentEventEnum.SUCCEED);

        PaymentStateEnum state1 = stateGraph.transit(PaymentStateEnum.PENDING, PaymentEventEnum.SUCCEED);
        assertEquals(PaymentStateEnum.SUCCESS, state1, "state machine transit fail");

        PaymentStateEnum state2 = stateGraph.getRootState();
        assertEquals(PaymentStateEnum.WAIT_TO_PAY, state2, "state machine transit fail");

        boolean s1 = stateGraph.canTransit(PaymentStateEnum.PENDING, PaymentStateEnum.SUCCESS);
        assertTrue(s1, "state machine  canTransit failed");

        boolean s2 = stateGraph.canTransit(PaymentStateEnum.PENDING, PaymentStateEnum.WAIT_TO_PAY);
        assertFalse(s2, "state machine  canTransit failed");

        boolean s3 = stateGraph.canTransit(PaymentStateEnum.WAIT_TO_PAY, PaymentEventEnum.PAY);
        assertTrue(s3, "state machine  canTransit failed");

        boolean s4 = stateGraph.canTransit(PaymentStateEnum.PENDING, PaymentEventEnum.PAY);
        assertFalse(s4, "state machine  canTransit failed");

    }

    @Getter
    enum PaymentStateEnum implements CodeBasedEnum {
        CANCELLED(120, "CANCELLED"),
        FAILURE(110, "FAILURE"),
        SUCCESS(100, "SUCCESS"),

        PENDING(20, "PENDING"),
        WAIT_TO_PAY(10, "WAIT_TO_PAY"),
        ;

        private final int code;
        private final String name;

        PaymentStateEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }


    }

    @Getter
    enum PaymentEventEnum implements CodeBasedEnum, Event {
        SUCCEED(120, "SUCCEED"),
        FAIL(110, "FAIL"),
        CANCEL(100, "CANCEL"),
        PAY(20, "PAY"),
        CREATE(10, "CREATE"),
        ;

        private final int code;
        private final String name;

        PaymentEventEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }


    interface TradeState extends State {
    }

    interface TradeEvent extends Event {
    }

    static class WaitToPay implements TradeState {
        @Override
        public int getCode() {
            return 1;
        }

        @Override
        public String getName() {
            return null;
        }
    }

    static class Paid implements TradeState {
        @Override
        public int getCode() {
            return 2;
        }

        @Override
        public String getName() {
            return null;
        }
    }

    static class Consigned implements TradeState {
        @Override
        public int getCode() {
            return 3;
        }

        @Override
        public String getName() {
            return null;
        }
    }

    static class PayEvent implements TradeEvent {
    }

    static class SendEvent implements TradeEvent {
    }

}

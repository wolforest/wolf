package com.wolf.common.sm;

import lombok.Getter;
import org.junit.Test;
import com.wolf.common.lang.enums.CodeBasedEnum;
import com.wolf.common.sm.impl.DefaultStateGraph;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertEquals("InitState failed", expected.getClass().getName(), initState.getClass().getName());
    }

    @Test
    public void fire() {
        TradeState init = new WaitToPay();
        TradeState paid = new Paid();
        TradeState consigned = new Consigned();

        TradeEvent payEvent = new PayEvent();
        TradeEvent sendEvent = new SendEvent();

        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>(init);

        assertEquals("init state fail", init, stateGraph.getRootState());

        stateGraph.addTransition(init, paid, payEvent);
        stateGraph.addTransition(paid, consigned, sendEvent);

        TradeState expectedPaid = stateGraph.transit(init, payEvent);
        assertEquals("paid event bind fail", expectedPaid, paid);

        TradeState expectedConsigned = stateGraph.transit(paid, sendEvent);
        assertEquals("send event bind fail", expectedConsigned, consigned);
    }

    @Test
    public void test_different_event_instance() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>();

        TradeState paid = new Paid();
        TradeState consigned = new Consigned();
        TradeEvent sendEvent = new SendEvent();

        stateGraph.addTransition(paid, consigned, sendEvent);

        TradeState expectedState = stateGraph.transit(paid, new SendEvent());
        assertEquals("different event instance fail.", expectedState, consigned);

    }

    @Test
    public void test_different_state_instance() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>();

        TradeState paid = new Paid();
        TradeState consigned = new Consigned();
        TradeEvent sendEvent = new SendEvent();

        stateGraph.addTransition(paid, consigned, sendEvent);

        TradeState expectedState = stateGraph.transit(new Paid(), new SendEvent());
        assertEquals("different source instance fail.", expectedState, consigned);
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
        assertEquals("get state by code from StateMachine fail.", consignFromSM, consigned);
        assertEquals("get state by code from StateMachine fail.", paid, paidFromSM);
    }

    @Test
    public void test_get_bind_event_list() {
        StateGraph<TradeState, TradeEvent> stateGraph = new DefaultStateGraph<>();

        TradeState paid = new Paid();
        TradeState consigned = new Consigned();
        TradeEvent sendEvent = new SendEvent();

        stateGraph.addTransition(paid, consigned, sendEvent);

        Set<TradeEvent> events = stateGraph.getTransitions(paid);
        assertTrue("get bind event list fail", events.contains(sendEvent));
    }

    @Test
    public void test_enm_state() {
        StateGraph<PaymentStateEnum, PaymentEventEnum> stateGraph = new DefaultStateGraph<PaymentStateEnum, PaymentEventEnum>(PaymentStateEnum.WAIT_TO_PAY)
                .addTransition(PaymentStateEnum.WAIT_TO_PAY, PaymentStateEnum.PENDING, PaymentEventEnum.PAY)
                .addTransition(PaymentStateEnum.PENDING, PaymentStateEnum.FAILURE, PaymentEventEnum.FAIL)
                .addTransition(PaymentStateEnum.PENDING, PaymentStateEnum.SUCCESS, PaymentEventEnum.SUCCEED);

        PaymentStateEnum state1 = stateGraph.transit(PaymentStateEnum.PENDING, PaymentEventEnum.SUCCEED);
        assertEquals("state machine transit fail", PaymentStateEnum.SUCCESS, state1);

        PaymentStateEnum state2 = stateGraph.getRootState();
        assertEquals("state machine transit fail", PaymentStateEnum.WAIT_TO_PAY, state2);

        boolean s1 = stateGraph.canTransit(PaymentStateEnum.PENDING, PaymentStateEnum.SUCCESS);
        assertTrue("state machine  canTransit failed", s1);

        boolean s2 = stateGraph.canTransit(PaymentStateEnum.PENDING, PaymentStateEnum.WAIT_TO_PAY);
        assertFalse("state machine  canTransit failed", s2);

        boolean s3 = stateGraph.canTransit(PaymentStateEnum.WAIT_TO_PAY, PaymentEventEnum.PAY);
        assertTrue("state machine  canTransit failed", s3);

        boolean s4 = stateGraph.canTransit(PaymentStateEnum.PENDING, PaymentEventEnum.PAY);
        assertFalse("state machine  canTransit failed", s4);

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
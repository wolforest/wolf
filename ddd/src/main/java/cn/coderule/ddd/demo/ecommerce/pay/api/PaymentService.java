package cn.coderule.ddd.demo.ecommerce.pay.api;

public interface PaymentService {
    PaymentResult pay(PaymentCommand command);
}

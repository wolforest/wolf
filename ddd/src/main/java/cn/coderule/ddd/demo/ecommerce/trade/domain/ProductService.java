package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.product.api.Product;

public interface ProductService {
    Product getSellableProduct(String productId);
}

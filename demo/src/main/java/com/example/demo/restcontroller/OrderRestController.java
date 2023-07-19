package com.example.demo.restcontroller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderRestController {


    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @PostMapping(value = "/add-order")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest,
                                              Principal principal) {
        List<ProductRequest> orderedProducts = new ArrayList<>();
        orderedProducts = orderRequest.getProducts();
        Double orderQuantity = orderRequest.getQuantity();
        List<Product> newOrderedProducts = new ArrayList<>();
        Double totalPrice = 0.0;
        try {
            for (ProductRequest product : orderedProducts) {
                Product orderedProduct = productService.findProductById(product.getId());
                totalPrice += orderedProduct.getProductPrice();
                newOrderedProducts.add(orderedProduct);
                if (orderedProduct == null) {
                    return new ResponseEntity<>("product with this id does not exist" + orderedProduct.getId(), HttpStatus.BAD_REQUEST);
                }
                newOrderedProducts.add(orderedProduct);
            }
            Order order = new Order();
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            String invoiceNumber = generateInvoiceNumber();
            order.setSetInvoiceNumber(invoiceNumber);
            //order.setProducts(newOrderedProducts);
            order.setQuantity(orderQuantity);
            order.setTotalPrice(totalPrice);
            User user = userService.findUserByEmail(principal.getName());
            order.setUser(user);
            orderService.saveOrder(order);
            return new ResponseEntity<>("order created", HttpStatus.CREATED);
        } catch (DataAccessException exception) {
            exception.printStackTrace();
            return new ResponseEntity("Error while saving the order. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Invalid request. Please check the order data and try again.", HttpStatus.BAD_REQUEST);
        }
    }

    private String generateInvoiceNumber() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();

    }

//    @GetMapping(value = "getAllProductByOrderId/{orderId}")
//    public ResponseEntity<String> getAllProductsByOrderId(@PathVariable Integer orderId){
//        List<Product> allProducts=productService.findAllProducts();
//        List<Product> productsByOrderId=new ArrayList<>();
//        for (Product product:allProducts){
//            if(orderId == product.getOrder().getId()){
//                productsByOrderId.add(product);
//            }
//        }
//        return new ResponseEntity<>(productsByOrderId.toString(),HttpStatus.OK);
//    }



    @GetMapping(value = "/getAllProductsByOrderId/{orderId}")
    public ResponseEntity<List<Product>> getAllProductsById(@PathVariable Integer orderId){
        Order order=orderService.findOrderById(orderId);
        List<Product> allProducts=order.getOrderedProducts();
        return new ResponseEntity<>(allProducts,HttpStatus.OK);
    }
}

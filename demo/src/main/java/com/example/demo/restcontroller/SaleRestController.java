package com.example.demo.restcontroller;


import com.example.demo.costomExceptions.ProductNotFoundException;
import com.example.demo.costomExceptions.ProductQuantityNotExistException;
import com.example.demo.dto.CreateOrderResponse;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.SaleProductDto;
import com.example.demo.dto.SaleRequestDto;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sale;
import com.example.demo.entity.User;
import com.example.demo.responseces.ProductResponse;
import com.example.demo.responseces.SaleProductResponse;
import com.example.demo.service.InventoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.SaleService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SaleRestController {

    private final Logger LOGGER = LogManager.getLogger("SaleRestControllerLogs");

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    SaleService saleService;

    @Autowired
    InventoryService inventoryService;

//    @PostMapping(value = "/sale-product")
//    public ResponseEntity<ProductResponse> saleProduct(@RequestBody @Valid SaleRequestDto saleRequestDto,
//                                              Principal principal) throws Exception {
//        User user = userService.findUserByEmail(principal.getName());
//        for (SaleProductDto saleProductDto : saleRequestDto.getSaleProducts()) {
//            Inventory existingInventory = inventoryService.findExistingInventoryOfTheProduct(saleProductDto.getProductId());
//            if (existingInventory != null) {
//                if (saleProductDto.getProductQuantity() <= existingInventory.getAvailableProductQuantity()) {
//                    try {
//                        Product existingProduct = productService.getProductById(saleProductDto.getProductId());
//                        Sale sale = new Sale();
//                        sale.setQuantity(saleProductDto.getProductQuantity().intValue());
//                        List<Product> products = new ArrayList<>();
//                        products.add(existingProduct);
//                        sale.setProducts(products);
//                        sale.setUser(user);
//                        Double totalPrice = existingProduct.getProductPrice() * saleProductDto.getProductQuantity();
//                        sale.setTotalPrice(totalPrice);
//                        saleService.saveTheSale(sale);
//                        inventoryService.updateProductSale(existingInventory, saleProductDto);
//                    } catch (Exception exception) {
//                        LOGGER.error(exception.getMessage());
//                        ProductResponse productResponse=new ProductResponse();
//                        productResponse.setMassage("error :"+exception.getMessage());
//                        productResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
//                        return new ResponseEntity<>(productResponse, HttpStatus.BAD_REQUEST);
//                    }
//                }else {
////                    LOGGER.error("products with entered quantity does not exist");
//                    ProductResponse productResponse = new ProductResponse();
//                    productResponse.setMassage("products with entered quantity does not exist, please enter below or equal to " + existingInventory.getAvailableProductQuantity());
//                    productResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
//                    return new ResponseEntity<>(productResponse, HttpStatus.BAD_REQUEST);
//                }
//            }
////            LOGGER.error("entered Product not available");
//            ProductResponse productResponse=new ProductResponse();
//            productResponse.setMassage("entered Product not available");
//            productResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
//            return new ResponseEntity<>(productResponse, HttpStatus.BAD_REQUEST);
//        }
//        ProductResponse productResponse=new ProductResponse();
//        productResponse.setMassage("Products are saled");
//        productResponse.setStatusCode(HttpStatus.OK.toString());
//        return new ResponseEntity<>(productResponse, HttpStatus.OK);
//    }


    @PostMapping(value = "/sale-product")
    public ResponseEntity<SaleProductResponse> saleProducts(@RequestBody @Valid SaleRequestDto saleRequestDto,
                                                                           Principal principal) throws Exception {
        User user=userService.findUserByEmail(principal.getName());
//        User user=null;
//        try {
//            user=userService.findUserByEmail(principal.getName());
//        }catch (Exception exception){
//            LOGGER.error(exception.getMessage());
//            LOGGER.error("error at finding User"+exception.getMessage());
//        }
        try {
            Sale sale=saleService.saleProducts(saleRequestDto,user);
            String massage="sale successful";
            SaleProductResponse saleProductResponse=new SaleProductResponse(massage,"ok");
            return new ResponseEntity<>(saleProductResponse,HttpStatus.OK);

        }
        catch (ProductNotFoundException exception){
            SaleProductResponse saleProductResponse=new SaleProductResponse(exception.getMessage(),HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>(saleProductResponse,HttpStatus.BAD_REQUEST);
        }
        catch (ProductQuantityNotExistException exception){
            SaleProductResponse saleProductResponse=new SaleProductResponse(exception.getMessage(),HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>(saleProductResponse,HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception){
            LOGGER.error("error at saving sale"+exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SaleProductResponse("Error at selling Products  . "+exception.getMessage(),HttpStatus.BAD_REQUEST.toString()));

        }
    }
}

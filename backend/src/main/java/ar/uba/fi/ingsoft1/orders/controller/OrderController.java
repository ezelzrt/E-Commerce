package ar.uba.fi.ingsoft1.orders.controller;

import java.util.*;
import java.util.stream.Collectors;

import ar.uba.fi.ingsoft1.auth.service.JwtService;
import ar.uba.fi.ingsoft1.exception.ExpiredTokenException;
import ar.uba.fi.ingsoft1.exception.InsuficientStockException;
import ar.uba.fi.ingsoft1.exception.InvalidStatusChangeException;
import ar.uba.fi.ingsoft1.exception.InvalidTokenException;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.orders.repository.OrderStatus;
import ar.uba.fi.ingsoft1.orders.service.OrderService;
import ar.uba.fi.ingsoft1.orders.repository.Order;
import ar.uba.fi.ingsoft1.products.repository.Product;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final JwtService jwtService;
    @GetMapping
    public ResponseEntity<?> getOrders(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader, @RequestParam(value = "role", defaultValue = "client", required = false) String role) {
        try {
            String token = jwtService.validateAndExtractToken(authorizationHeader);
            int accessType = jwtService.extractAccessType(token);
            long user_id = jwtService.extractUserId(token);

            if (role.equals("admin") && accessType != 1){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Unauthorized", "message", "Your user don't have access to this resource"));
            }

            List<Order> orders;
            if (role.equals("admin")) {
                orders = orderService.getOrders();
            } else {
                orders = orderService.getOrdersByUserId(user_id);
            }

            List<Map<String, Object>> formattedOrders = orders.stream()
                    .map(order -> {
                        Map<String, Object> formattedOrder = new HashMap<>();
                        formattedOrder.put("orderID", order.getId());
                        formattedOrder.put("date", order.getOrderDate());
                        formattedOrder.put("status", order.getStatus());

                        Map<String, Object> userDetails = new HashMap<>();
                        userDetails.put("id", order.getUser().getId());
                        userDetails.put("firstname", order.getUser().getFirstName());
                        userDetails.put("lastname", order.getUser().getLastName());
                        formattedOrder.put("customer", userDetails);

                    List<Map<String, Object>> orderDetails = new ArrayList<>();
                    for (OrderDetail detail : order.getOrderDetails()) {
                        Map<String, Object> detailMap = new HashMap<>();
                        detailMap.put("id", detail.getId());
                        detailMap.put("product_id", detail.getProduct().getId());
                        detailMap.put("product_amount", detail.getProductAmount());
                        detailMap.put("product_price", detail.getProductPrice());

                            orderDetails.add(detailMap);
                        }
                        formattedOrder.put("orderDetails", orderDetails);

                        return formattedOrder;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(formattedOrders);
        } catch (InvalidTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_token", "message", ex.getMessage()));
        } catch (ExpiredTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "expired_token", "message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
        }
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOrdersById(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader, @PathVariable("order_id") Long orderId) {
        try {
            String token = jwtService.validateAndExtractToken(authorizationHeader);
            int accessType = jwtService.extractAccessType(token);
            long user_id = jwtService.extractUserId(token);

            Optional<Order> opt_order = orderService.getOrderById(orderId);

            //Validamos que el usuario no admin acceda solamente a sus propias ordenes
            if (opt_order.isEmpty() || (accessType > 1 && !opt_order.get().getUser().getId().equals(user_id))) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found.");
            }

            Order order = opt_order.get();

            List<Map<String, Object>> orderDetails = order.getOrderDetails().stream()
                    .map(detail -> {
                        Map<String, Object> detailMap = new HashMap<>();
                        detailMap.put("id", detail.getId());
                        detailMap.put("productID", detail.getProduct().getId());
                        detailMap.put("name", getProductNameOrDefautl(detail.getProduct()));
                        detailMap.put("quantity", detail.getProductAmount());
                        detailMap.put("unitPrice", detail.getProductPrice());
                        return detailMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("orderID", order.getId());
            response.put("orderStatus", order.getStatus());
//            response.put("deliver", order.getDeliveryMethod());
            response.put("deliver", "Pick up in store");
            response.put("details", orderDetails);

            Map<String, Object> customerDetails = new HashMap<>();
            customerDetails.put("id", order.getUser().getId());
            customerDetails.put("first_name", order.getUser().getFirstName());
            customerDetails.put("last_name", order.getUser().getLastName());
            customerDetails.put("email", order.getUser().getEmail());

            response.put("customer", customerDetails);

            return ResponseEntity.ok(response);
        } catch (InvalidTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_token", "message", ex.getMessage()));
        } catch (ExpiredTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "expired_token", "message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader, @RequestBody final List<ProductOrder> products) {
        try{
            String token = jwtService.validateAndExtractToken(authorizationHeader);
            long user_id = jwtService.extractUserId(token);

            List<Order> orders = orderService.createOrder(user_id, products);

            return ResponseEntity.ok(orders);
        } catch (InvalidTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_token", "message", ex.getMessage()));
        } catch (ExpiredTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "expired_token", "message", ex.getMessage()));
        } catch (InsuficientStockException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "insuficient_stock", "message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
        }
    }

    @PatchMapping("/{orderId}/status")
    @CrossOrigin(methods = RequestMethod.PATCH, origins = "*")
    public ResponseEntity<?> patchStatusById(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader, 
        @PathVariable Long orderId, 
        @RequestBody final Map<String, OrderStatus> newStatusRequest
        ){
        try {
            String token = jwtService.validateAndExtractToken(authorizationHeader);
            int accessType = jwtService.extractAccessType(token);
            long user_id = jwtService.extractUserId(token);
            
            Optional<Order> opt_order = orderService.getOrderById(orderId);
            OrderStatus newStatus = newStatusRequest.get("newStatus");
            if (accessType > 1 && opt_order.get().getUser().getId().equals(user_id)) {
                // Logica si es usuario
                newStatus = orderService.userPatchStatusById(orderId, newStatus);
            } else {
                newStatus = orderService.patchStatusById(orderId, newStatus);
            }
            
            return ResponseEntity.status(HttpStatus.OK).body("The status was changed to: " + newStatus);

        } catch (InvalidTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_token", "message", ex.getMessage()));
        } catch (InvalidStatusChangeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "invalid_status_change", "message", ex.getMessage()));
        } catch (ExpiredTokenException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "expired_token", "message", ex.getMessage()));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
        }
    }

    private String getProductNameOrDefautl(Product product){
        String productName = product.getName();

        if (productName == null || productName.equalsIgnoreCase("null")) {
            return product.getMasterProduct().getName();
        }

        return productName;
    }
}



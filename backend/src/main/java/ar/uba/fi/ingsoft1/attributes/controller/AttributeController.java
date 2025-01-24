package ar.uba.fi.ingsoft1.attributes.controller;

import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import ar.uba.fi.ingsoft1.attributes.service.AttributeService;
import ar.uba.fi.ingsoft1.exception.ExpiredTokenException;
import ar.uba.fi.ingsoft1.exception.InvalidTokenException;
import ar.uba.fi.ingsoft1.orders.repository.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService service;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody final AddRequest request) {
        service.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Attribute has been successfully created.");
    }

    @GetMapping
    public ResponseEntity<?> getAttributes() {
        try {
            List<Attribute> attributes = service.getAttributes();
            return ResponseEntity.ok(attributes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
        }
    }
//    @PatchMapping("/")
//    public ResponseEntity<?> register(@RequestBody final AddRequest request) {
//        service.add(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Attribute has been successfully created.");
//    }
}

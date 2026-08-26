package com.booking.resourcebooking.controller;

import com.booking.resourcebooking.dto.ResourceRequest;
import com.booking.resourcebooking.dto.ResourceResponse;
import com.booking.resourcebooking.service.ResourceService;
import com.booking.resourcebooking.util.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<?> getAllResources(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        if (page != null || size != null) {
            int p = page != null ? page : 0;
            int s = size != null ? size : 10;
            Pageable pageable = PageableUtils.createPageable(p, s, sort);
            return ResponseEntity.ok(resourceService.getAllResources(pageable));
        }
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @PostMapping
    public ResourceResponse createResource(@Valid @RequestBody ResourceRequest request) {
        return resourceService.createResource(request);
    }

    @GetMapping("/{id}")
    public ResourceResponse getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id);
    }

    @PutMapping("/{id}")
    public ResourceResponse updateResource(
            @PathVariable Long id,
            @Valid @RequestBody ResourceRequest request) {
        return resourceService.updateResource(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
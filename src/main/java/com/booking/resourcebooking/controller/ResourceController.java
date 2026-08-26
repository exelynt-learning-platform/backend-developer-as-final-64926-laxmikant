package com.booking.resourcebooking.controller;

import com.booking.resourcebooking.dto.ResourceRequest;
import com.booking.resourcebooking.entity.Resource;
import com.booking.resourcebooking.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    @PostMapping
    public Resource createResource(@Valid @RequestBody ResourceRequest request) {
        return resourceService.createResource(request);
    }

    @GetMapping("/{id}")
    public Resource getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id);
    }

    @PutMapping("/{id}")
    public Resource updateResource(
            @PathVariable Long id,
            @Valid @RequestBody ResourceRequest request) {

        return resourceService.updateResource(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
    }
}
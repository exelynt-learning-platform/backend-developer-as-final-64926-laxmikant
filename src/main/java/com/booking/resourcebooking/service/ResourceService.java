package com.booking.resourcebooking.service;

import com.booking.resourcebooking.dto.ResourceRequest;
import com.booking.resourcebooking.entity.Resource;
import com.booking.resourcebooking.exception.ResourceNotFoundException;
import com.booking.resourcebooking.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    //constructor dependency injection
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Resource createResource(ResourceRequest request) {

        Resource resource = new Resource();

        resource.setName(request.getName());
        resource.setType(request.getType());
        resource.setCapacity(request.getCapacity());

        return resourceRepository.save(resource);
    }

    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    public Resource updateResource(Long id, ResourceRequest request) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        resource.setName(request.getName());
        resource.setType(request.getType());
        resource.setCapacity(request.getCapacity());

        return resourceRepository.save(resource);
    }

    public void deleteResource(Long id) {

        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }

        resourceRepository.deleteById(id);
    }
}
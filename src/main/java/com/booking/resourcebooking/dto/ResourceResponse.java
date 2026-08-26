package com.booking.resourcebooking.dto;

import com.booking.resourcebooking.entity.Resource;
import com.booking.resourcebooking.entity.ResourceType;

public class ResourceResponse {

    private Long id;
    private String name;
    private ResourceType type;
    private Integer capacity;

    public ResourceResponse() {
    }

    public ResourceResponse(Long id, String name, ResourceType type, Integer capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
    }

    public static ResourceResponse fromEntity(Resource resource) {
        if (resource == null) {
            return null;
        }
        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getType(),
                resource.getCapacity()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}

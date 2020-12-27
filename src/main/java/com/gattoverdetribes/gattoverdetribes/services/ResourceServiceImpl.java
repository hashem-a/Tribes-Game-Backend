package com.gattoverdetribes.gattoverdetribes.services;

import com.gattoverdetribes.gattoverdetribes.dtos.ResourceDetailsDTO;
import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidResourceException;
import com.gattoverdetribes.gattoverdetribes.models.Kingdom;
import com.gattoverdetribes.gattoverdetribes.models.resources.Resource;
import com.gattoverdetribes.gattoverdetribes.models.resources.ResourceFactory;
import com.gattoverdetribes.gattoverdetribes.repositories.ResourceRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

  private final ResourceRepository resourceRepository;

  @Autowired
  public ResourceServiceImpl(
      ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
  }

  @Override
  public Resource createResource(String resourceType, Kingdom kingdom) {
    try {
      Resource resource = ResourceFactory.createResource(resourceType);
      resource.setKingdom(kingdom);
      saveResource(resource);
      return resource;
    } catch (InvalidResourceException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public void saveResource(Resource resource) {
    resourceRepository.save(resource);
  }

  public ResponseEntity<List<ResourceDetailsDTO>> getResourcesByKingdom(Kingdom kingdom) {

    List<ResourceDetailsDTO> resources = convertResourcesToDTOByKingdom(kingdom);

    if (resources.isEmpty()) {
      return new ResponseEntity<>(resources, HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(resources, HttpStatus.OK);
    }
  }

  @Override
  public List<ResourceDetailsDTO> convertResourcesToDTOByKingdom(Kingdom kingdom) {
    List<Resource> resources = resourceRepository.findAllByKingdomId(kingdom.getId());
    return resources.stream().map(ResourceDetailsDTO::new).collect(Collectors.toList());
  }
}

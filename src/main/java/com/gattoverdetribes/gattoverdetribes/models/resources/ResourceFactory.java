package com.gattoverdetribes.gattoverdetribes.models.resources;

import static com.gattoverdetribes.gattoverdetribes.models.resources.ResourceType.generateResource;
import com.gattoverdetribes.gattoverdetribes.exceptions.InvalidResourceException;

public class ResourceFactory {

  public static Resource createResource(String resourceType) throws IllegalArgumentException {
    for (ResourceType type : ResourceType.values()) {
      try {
        if (ResourceType.valueOf(resourceType.toUpperCase()).equals(type)) {
          return generateResource(type);
        }
      } catch (IllegalArgumentException e) {
        throw new InvalidResourceException("No such resource");
      }
    }
    return null;
  }
}

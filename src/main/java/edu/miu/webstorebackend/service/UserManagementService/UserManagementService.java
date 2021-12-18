package edu.miu.webstorebackend.service.UserManagementService;

import edu.miu.webstorebackend.dto.SellerDtos.ResponseDtos.ActivateSellerResponse;

import java.util.AbstractMap;

public interface UserManagementService {
    AbstractMap.SimpleImmutableEntry<Boolean, ActivateSellerResponse> activateSeller(long id);
}

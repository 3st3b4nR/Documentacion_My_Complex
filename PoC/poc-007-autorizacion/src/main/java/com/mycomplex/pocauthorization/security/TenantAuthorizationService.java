package com.mycomplex.pocauthorization.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component("tenantAuthorization")
public class TenantAuthorizationService {

    public static final String TENANT_CLAIM = "https://mycomplex.com/tenant_id";

    public boolean canAccess(String requestedTenantId, Jwt jwt) {
        if (jwt == null || requestedTenantId == null) {
            return false;
        }

        String tokenTenantId = jwt.getClaimAsString(TENANT_CLAIM);

        return requestedTenantId.equals(tokenTenantId);
    }
}

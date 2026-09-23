/**
 * Auth0 Action - Post Login
 * PoC ADR-006 MyComplex
 *
 * Requisitos:
 * 1. El usuario debe tener:
 *    app_metadata:
 *    {
 *      "tenant_id": "conjunto-001"
 *    }
 *
 * 2. El usuario debe tener un rol asignado en Auth0.
 */
exports.onExecutePostLogin = async (event, api) => {
  const namespace = "https://mycomplex.com";

  const tenantId = event.user.app_metadata?.tenant_id;

  if (tenantId) {
    api.accessToken.setCustomClaim(
      `${namespace}/tenant_id`,
      tenantId
    );
  }

  if (event.authorization?.roles) {
    api.accessToken.setCustomClaim(
      `${namespace}/roles`,
      event.authorization.roles
    );
  }
};

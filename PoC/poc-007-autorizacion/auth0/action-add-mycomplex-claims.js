/**
 * Esta Action es la misma dependencia de claims usada en la PoC-006.
 * La PoC-007 necesita que el Access Token contenga:
 * - https://mycomplex.com/tenant_id
 * - https://mycomplex.com/roles
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

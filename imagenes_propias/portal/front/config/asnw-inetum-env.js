(function (window) {
  window.__env = window.__env || {};
  // window.__env.api = 'https://localhost/example-app-back';
  window.__env.api = "http://localhost:8080";
  // window.__env.api = 'https://desa2eap7.asturias.es/example-app-back';
  window.__env.appHost = "https://desa2eap7.asturias.es/asnw-inetum";
  window.__env.appname = "ASNW Inetum";
  window.__env.idpHint = "samlClaveV2";
  window.__env.linksJsonURL = "../assets/links.json";
  window.__env.isMultilanguage = false;
  window.__env.isLoginClave = true;
  window.__env.isLoginLdap = true;
  window.__env.isFullScreen = false;
  // window.__env.keycloak = 'https://desarhssosa.asturias.es:8443';
  // window.__env.keycloak = 'https://ov01lnxvue.princast.org:8443';
  // window.__env.keycloak = 'http://10.200.72.2:8088';
  // window.__env.keycloak = 'https://integrhsso.asturias.es';
  window.__env.keycloakClientId = "sampleapp";
  window.__env.keycloakLoadUserProfileAtStartUp = true;
  window.__env.keycloakRealm = "princast-asturias";
  window.__env.keycloakUrl = "https://desarhssosa.asturias.es:8443";
  // window.__env.keycloakUrl = 'https://ov01lnxvadl.princast.org:8443'
  window.__env.mockNif = "00001";
})(this);

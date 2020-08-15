# Xtivia Service Guard 2.0
[OSGI](https://www.osgi.org/) - All about registering components

[Whiteboard pattern](https://docs.osgi.org/whitepaper/whiteboard-pattern/) - Leveraging the OSGi framework’s service registry

[JAX-RS 2.0](https://jcp.org/en/jsr/detail?id=339) - Developing, exposing, and accessing REST applications

[JAX-RS Whiteboard Specification](https://docs.osgi.org/specification/osgi.cmpn/7.0.0/service.jaxrs.html) - All about usig OSGI for dynamic registration of JAX-RS applications, filters, interceptors, contexts, listeners

**Service Guard 2** is a collection of filters and context providers that dynamically get attached to a JAX-RS application.
Register an application with property `type=sgdxp2` and all the Service Guard 2 components will get attached to that application. Then you can utilize the Service Guard 2 annotations on your resource methodes/classes.

`com.xtivia.sgdxp2.samples.SgDxpSampleRestService-default.config`

```
type=sgdxp2
osgi.jaxrs.application.base=/sgdxp2
osgi.jaxrs.name=Xtivia.Service.Guard.DXP.2.Sample
auth.verifier.guest.allowed=true
oauth2.scopechecker.type=none
liferay.access.control.disable=true
auth.verifier.auth.verifier.PortalSessionAuthVerifier.check.csrf.token=false
```

### Reference Videos
[ECE2017 | When whiteboards play together. JAX-RS and Servlets the OSGi way](https://www.youtube.com/watch?v=26IP3u4b258&t=897s)

[Modular and configurable JAX-RS applications secured with OAuth 2.0](https://www.youtube.com/watch?v=H8FPO0nxiJo)

[Secure Web APIs using JAX-RS and OAuth2](https://www.youtube.com/watch?v=fneSTbTSxZw)
# Xtivia Service Guard 2.0

[OSGI](https://www.osgi.org/) - all about regesting components

[Benefits](https://www.osgi.org/developer/benefits-of-using-osgi/)

[Whiteboard pattern](https://docs.osgi.org/whitepaper/whiteboard-pattern/)

[JAX-RS 2.0](https://jcp.org/en/jsr/detail?id=339)

[JAX-RS Whiteboard Specification](https://docs.osgi.org/specification/osgi.cmpn/7.0.0/service.jaxrs.html)
	
- Dynamic registertion of applications, filters, interceptors, contexts, listeners

**Service Guard 2** is a collection of filters and context providers that dynamically get attached to an application.
Register an application with property type=sgdxp2 and all the registered Service Guard components will get attached to that application

`com.xtivia.sgdxp2.samples.SgDxpSampleRestService-default.config`

```markdown
type=sgdxp2
osgi.jaxrs.application.base=/sgdxp2
osgi.jaxrs.name=Xtivia.Service.Guard.DXP.2.Sample
auth.verifier.guest.allowed=true
oauth2.scopechecker.type=none
liferay.access.control.disable=true
auth.verifier.auth.verifier.PortalSessionAuthVerifier.check.csrf.token=false
```

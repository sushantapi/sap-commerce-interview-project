# SAP Commerce OCC Interview Guide

## What is OCC?

OCC stands for OmniCommerce Connect. In SAP Commerce, OCC exposes commerce functionality through REST APIs for clients such as storefronts, mobile applications, and external systems.

## Typical architecture

```text
Client / Storefront
        |
        v
OCC Controller
        |
        v
Facade
        |
        v
Service
        |
        v
DAO
        |
        v
Database
```

The OCC Controller should handle HTTP concerns such as request mapping, validation, request/response DTOs, and HTTP status codes. It should not directly call a DAO or repository.

## Project implementation

This project demonstrates the OCC pattern through:

```text
/occ/v1/products
```

The controller delegates to the existing `ProductFacade`, preserving the Controller -> Facade -> Service -> DAO architecture.

Available operations:

- `GET /occ/v1/products`
- `GET /occ/v1/products/search?query=iphone&page=0&size=10&sort=name,asc`
- `GET /occ/v1/products/{id}`
- `POST /occ/v1/products`
- `PUT /occ/v1/products/{id}`
- `DELETE /occ/v1/products/{id}`

## Interview distinction

This Spring Boot controller is an OCC-style practice implementation. It is **not the actual SAP Commerce OCC extension framework**. Actual SAP Commerce OCC uses the platform's OCC/webservices extensions and configuration.

## Short interview answer

> OCC is the REST API layer of SAP Commerce. It exposes commerce services to storefronts, mobile applications, and external consumers. The OCC Controller receives the HTTP request and delegates to the Facade. The Facade coordinates business operations through the Service and DAO layers. The controller should not directly access the repository or database.

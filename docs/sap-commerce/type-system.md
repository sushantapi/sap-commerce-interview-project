# SAP Commerce Type System Interview Practice

## items.xml

`items.xml` defines SAP Commerce types, attributes, relations, and persistence/generation configuration. In a real SAP Commerce extension, the platform processes this definition as part of the type system.

The `items.xml` in this directory is **interview practice documentation only**. This repository is a Spring Boot application, so Spring Boot does not process this file and it does not generate `ProductModel` from it.

## Product mapping

The Spring Boot project currently has a JPA `Product` entity. The conceptual SAP Commerce equivalent is a `ProductModel` backed by a `Product` item type.

| Spring Boot project | SAP Commerce concept |
|---|---|
| `Product` JPA entity | `ProductModel` |
| JPA attribute | item type attribute |
| `ProductRepository` | FlexibleSearch/DAO persistence access |
| `ProductService` | Service layer |
| `ProductFacade` | Facade layer |
| `ProductData` | Data DTO |

## Important attributes

- `code`: unique product identifier
- `name`: product name
- `description`: product description
- `price`: product price
- `stock`: available inventory
- `active`: product activation flag

## Interview points

### `autocreate`
Controls whether the type is created by the system during initialization/update processing.

### `generate`
Controls generation of model/source artifacts for the type in a real SAP Commerce extension.

### `persistence`
Defines how an attribute is persisted. A property-persisted attribute is stored as part of the item's persistence representation.

### `deployment`
Defines the persistence deployment details for a type, such as its table and type code. The type system remains the abstraction used by application code.

### ModelService
`ModelService` is the SAP Commerce service used to create, save, refresh, remove, and otherwise manage model instances.

Example conceptual flow:

```text
items.xml
   ↓
Type System
   ↓
ProductModel
   ↓
ModelService
   ↓
Persistence
```

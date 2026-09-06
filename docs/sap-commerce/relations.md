# SAP Commerce Relations

## Interview mapping

In SAP Commerce, relations are defined in `items.xml`. A common commerce example is Product to Category.

```text
Category 1 ---- * Product
```

This project demonstrates the same concept with JPA:

- `Category` is the target entity.
- `Product.category` is a many-to-one association.
- `products.category_id` acts as the foreign-key representation.
- `FetchType.LAZY` avoids loading the category when product details do not need it.

## SAP Commerce relation concepts

- `sourceElement`: source side of the relation.
- `targetElement`: target side of the relation.
- `cardinality`: number of items on each side, such as one or many.
- `ordered`: whether relation values preserve an order.
- `localized`: whether an attribute has language-specific values. This is an attribute concept, not a normal relation cardinality setting.

## Common relation types

### One-to-one

One source item relates to one target item.

### One-to-many

One source item relates to multiple target items.

Example: one Category has many Products.

### Many-to-one

Many source items relate to one target item.

Example: many Products belong to one Category.

### Many-to-many

Many source items relate to many target items.

Example: Products and Categories when a product can belong to multiple categories.

## Interview answer

**What is a relation in SAP Commerce?**

A relation defines an association between two item types in the SAP Commerce type system. It specifies the source and target item types and their cardinalities, and the platform manages the persistence and generated model access for that relationship.

**Example:**

```text
Category 1 ---- * Product
```

In this Spring Boot interview project, the equivalent is represented with a JPA `@ManyToOne` association from `Product` to `Category`. This is a demonstration of the concept, not SAP Commerce's actual relation engine.

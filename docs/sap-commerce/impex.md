# SAP Commerce ImpEx Interview Guide

## What is ImpEx?

ImpEx is SAP Commerce's data import/export mechanism. It is commonly used to create, update, remove, and export platform data such as products, categories, prices, users, and orders.

ImpEx is not SQL and is not FlexibleSearch.

## Common modes

### INSERT

Creates new items. It is appropriate when the item is expected not to exist already.

```impex
INSERT Product;
code[unique=true];name;description;price;stock
;IPHONE-17; iPhone 17; Apple smartphone;79999;50
```

### INSERT_UPDATE

Creates the item if it does not exist, otherwise updates the matching item. This is the most commonly used mode for repeatable product data loads.

```impex
INSERT_UPDATE Product;
code[unique=true];name;description;price;stock
;IPHONE-17; iPhone 17; Apple smartphone;79999;50
```

### UPDATE

Updates an existing item. A unique key must identify the target item.

```impex
UPDATE Product;
code[unique=true];price
;IPHONE-17;74999
```

### REMOVE

Removes matching items.

```impex
REMOVE Product;
code[unique=true]
;IPHONE-17
```

## FlexibleSearch vs ImpEx

| ImpEx | FlexibleSearch |
|---|---|
| Data import/export | Data querying |
| Create/update/remove data | Read/query data |
| Uses ImpEx headers and value rows | Uses FlexibleSearch syntax |
| Common for initial/master data loads | Common for application queries and administration |

Example FlexibleSearch:

```sql
SELECT {p:pk}
FROM {Product AS p}
WHERE {p:code} = ?code
```

Example ImpEx:

```impex
INSERT_UPDATE Product;
code[unique=true];name;price
;IPHONE-17;iPhone 17;79999
```

## Interview answer

**Q: What is ImpEx in SAP Commerce?**

> ImpEx is SAP Commerce's standard mechanism for importing and exporting data. It supports operations such as INSERT, UPDATE, INSERT_UPDATE and REMOVE, and is widely used for loading master data such as products, categories, prices and users.

**Q: INSERT vs INSERT_UPDATE?**

> INSERT expects a new item, while INSERT_UPDATE checks the unique key and creates the item when it does not exist or updates it when it already exists.

**Q: ImpEx vs FlexibleSearch?**

> ImpEx is primarily for data import/export and modification, whereas FlexibleSearch is primarily for querying SAP Commerce data through the type system.

## Project mapping

This Spring Boot project currently uses `data.sql` for sample product loading. That is the Spring Boot/PostgreSQL equivalent used by this project. It does not execute SAP Commerce ImpEx.

For interview purposes, the conceptual mapping is:

```text
data.sql / database initialization
            ≈
SAP Commerce ImpEx for initial data loading
```

Do not claim that Spring Boot `data.sql` is actually ImpEx.

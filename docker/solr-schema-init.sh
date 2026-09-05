#!/bin/bash
set -e

SOLR_URL="http://solr:8983/solr/products"
SCHEMA_URL="${SOLR_URL}/schema"
FIELD_URL="${SCHEMA_URL}/fields/name_sort"

# Wait until the products core is available.
until curl -fsS "${SOLR_URL}/admin/ping" >/dev/null; do
  sleep 2
done

echo "Solr products core is ready. Configuring name_sort..."

# name_sort must be a single-valued string field so Solr can sort on it.
# The operation is idempotent for both a fresh and an existing core.
FIELD_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "${FIELD_URL}")

if [ "${FIELD_STATUS}" = "200" ]; then
  curl -fsS -X POST "${SCHEMA_URL}" \
    -H "Content-Type: application/json" \
    --data-binary '{
      "replace-field": {
        "name": "name_sort",
        "type": "string",
        "indexed": true,
        "stored": true,
        "docValues": true,
        "multiValued": false
      }
    }'
else
  curl -fsS -X POST "${SCHEMA_URL}" \
    -H "Content-Type: application/json" \
    --data-binary '{
      "add-field": {
        "name": "name_sort",
        "type": "string",
        "indexed": true,
        "stored": true,
        "docValues": true,
        "multiValued": false
      }
    }'
fi

echo
echo "Solr name_sort field configured successfully."

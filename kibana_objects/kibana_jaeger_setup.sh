KIBANA_HOST=localhost
KIBANA_PORT=5601
ELASTIC_PORT=9200
ELASTIC_USER=
ELASTIC_PASSWORD=

if [ -n "${ELASTIC_USER}" ]; then
  CURL_UESR_OPT="-u ${ELASTIC_USER}:${ELASTIC_PASSWORD}"
fi

##fixes the numeric mapping
curl --header "Content-Type: application/json" -X POST "http://${KIBANA_HOST}:${ELASTIC_PORT}/_template/custom-jaeger-span?include_type_name" --data '{
  "order": 90,
  "index_patterns": [
    "*jaeger-span-*"
  ],
  "mappings": {
    "_doc": {
      "dynamic_templates": [
        {
          "span_long_no_index": {
            "match_mapping_type": "long",
            "mapping": {
              "type": "long",
              "index": false
            }
          }
        },
        {
          "span_double_no_index": {
            "match_mapping_type": "double",
            "mapping": {
              "type": "float",
              "index": false
            }
          }
        }
      ]
    }
  }
}'

##importing the actual index, dashboard and visualizations
curl ${CURL_UESR_OPT} -X POST "http://${KIBANA_HOST}:${KIBANA_PORT}/api/saved_objects/_import?overwrite=true" -H "kbn-xsrf: true" --form file=@kibana-jaeger-index.ndjson
curl ${CURL_UESR_OPT} -X POST "http://${KIBANA_HOST}:${KIBANA_PORT}/api/saved_objects/_import?overwrite=true" -H "kbn-xsrf: true" --form file=@kibana-visualizations.ndjson
curl ${CURL_UESR_OPT} -X POST "http://${KIBANA_HOST}:${KIBANA_PORT}/api/saved_objects/_import?overwrite=true" -H "kbn-xsrf: true" --form file=@kibana-dashboard.ndjson

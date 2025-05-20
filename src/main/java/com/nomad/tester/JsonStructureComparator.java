package com.nomad.tester;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JsonStructureComparator {

    private static final Logger log = LoggerFactory.getLogger(JsonStructureComparator.class);

    @Value("${app.json-compare}")
    private boolean autoRunJsonStructureComparator;

    public static boolean compareJsonStructure(String json1, String json2) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tree1 = mapper.readTree(json1);
        JsonNode tree2 = mapper.readTree(json2);
        return compareStructure(tree1, tree2);
    }

    private static boolean compareStructure(JsonNode node1, JsonNode node2) {
        if (node1.getNodeType() != node2.getNodeType()) return false; // Type mismatch

        if (node1.isObject() && node2.isObject()) {
            var fields1 = node1.fieldNames();
            var fields2 = node2.fieldNames();

            // Ensure both JSON objects have the same number of fields
            if (!fields1.hasNext() || !fields2.hasNext()) return false;
            if (node1.size() != node2.size()) return false; // Extra fields found

            while (fields1.hasNext()) {
                String field = fields1.next();
                if (!node2.has(field) || !compareStructure(node1.get(field), node2.get(field))) {
                    System.out.println("Mismatch found: " + field);
                    return false;
                }
            }
        } else if (node1.isArray() && node2.isArray()) {
            if (node1.size() != node2.size()) return false; // Array size mismatch
            for (int i = 0; i < node1.size(); i++) {
                if (!compareStructure(node1.get(i), node2.get(i))) return false;
            }
        }

        return true; // JSONs have exactly the same structure
    }

    private static boolean compareStructureOld(JsonNode node1, JsonNode node2) {
        if (node1.getNodeType() != node2.getNodeType()) return false; // Type mismatch
        if (node1.isObject() && node2.isObject()) {
            var fields1 = node1.fieldNames();
            var fields2 = node2.fieldNames();
            while (fields1.hasNext()) {
                String field = fields1.next();
                if (!node2.has(field) || !compareStructure(node1.get(field), node2.get(field))) {
                    log.info("Could not found {} in both json", field);
                    return false;
                }
            }
        } else if (node1.isArray() && node2.isArray()) {
            if (node1.size() != node2.size()) return false; // Ensuring same array structure
            for (int i = 0; i < node1.size(); i++) {
                if (!compareStructure(node1.get(i), node2.get(i))) return false;
            }
        }
        return true; // Matching structure
    }

    @PostConstruct
    public void init() throws Exception {
        log.info("autoRunJsonStructureComparator: {}", autoRunJsonStructureComparator);
        if (autoRunJsonStructureComparator) {
            String json2 = "{ \"id\": 1, \"id1\": 1, \"details\": { \"name\": \"Alice\", \"age\": 25 } }";
            String json1 = "{ \"id\": 42, \"id1\": 1, \"details\": { \"name\": \"Bob\", \"age\": 30 } }";

            log.info("Comparison of json1 and json2 is: {}", compareJsonStructure(json1, json2));
        }
    }
}

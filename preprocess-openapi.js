#!/usr/bin/env node

/**
 * OpenAPI Specification Preprocessor
 *
 * This script removes JSON Schema 2020-12 meta-properties ($schema and $id) from
 * the OpenAPI specification. These properties are valid in JSON Schema but cause
 * issues with OpenAPI Generator when present in component schemas.
 *
 * Usage:
 *   node preprocess-openapi.js <input-file> <output-file>
 *
 * Example:
 *   node preprocess-openapi.js src/main/resources/openapi.json src/main/resources/openapi-fixed.json
 */

const fs = require('fs');
const path = require('path');

// Get command line arguments
const args = process.argv.slice(2);

if (args.length < 2) {
  console.error('Usage: node preprocess-openapi.js <input-file> <output-file>');
  console.error('Example: node preprocess-openapi.js openapi.json openapi-fixed.json');
  process.exit(1);
}

const inputFile = args[0];
const outputFile = args[1];

// Check if input file exists
if (!fs.existsSync(inputFile)) {
  console.error(`Error: Input file not found: ${inputFile}`);
  process.exit(1);
}

console.log(`Reading OpenAPI specification from: ${inputFile}`);

// Read and parse the OpenAPI specification
let spec;
try {
  const content = fs.readFileSync(inputFile, 'utf8');
  spec = JSON.parse(content);
} catch (error) {
  console.error(`Error reading or parsing input file: ${error.message}`);
  process.exit(1);
}

/**
 * Returns true if a schema node describes a boolean type, including the
 * JSON Schema nullable form `"type": ["boolean", "null"]`.
 */
function isBooleanSchema(obj) {
  return (
    obj.type === 'boolean' ||
    (Array.isArray(obj.type) && obj.type.includes('boolean'))
  );
}

/**
 * Recursively remove JSON Schema keywords that OpenAPI Generator cannot handle:
 *
 *   - `$schema` / `$id` (JSON Schema 2020-12 meta-properties) break parsing
 *     when embedded in component schemas.
 *   - `const` / single-value `enum` on a BOOLEAN property makes the generator
 *     emit an uncompilable single-value enum (e.g. `TRUE("true")` assigned to a
 *     `Boolean` field). Stripping it generates a plain `Boolean` field instead;
 *     the constraint is still enforced server-side.
 */
function removeMetaProperties(obj) {
  if (typeof obj !== 'object' || obj === null) {
    return;
  }

  // Remove the meta-properties if they exist
  if (obj.hasOwnProperty('$schema')) {
    delete obj.$schema;
  }
  if (obj.hasOwnProperty('$id')) {
    delete obj.$id;
  }

  // Drop const / single-value enum on boolean schemas (generator bug)
  if (isBooleanSchema(obj)) {
    if (obj.hasOwnProperty('const')) {
      delete obj.const;
    }
    if (Array.isArray(obj.enum)) {
      delete obj.enum;
    }
  }

  // Recursively process all properties
  for (const key in obj) {
    if (obj.hasOwnProperty(key) && typeof obj[key] === 'object') {
      removeMetaProperties(obj[key]);
    }
  }
}

console.log('Preprocessing OpenAPI specification...');

// Count schemas before processing
let schemaCount = 0;
if (spec.components && spec.components.schemas) {
  schemaCount = Object.keys(spec.components.schemas).length;
  console.log(`Found ${schemaCount} component schemas`);
}

// Remove meta-properties from the entire spec
removeMetaProperties(spec);

console.log('Removed $schema and $id meta-properties from all schemas');

// Write the processed specification
try {
  const outputContent = JSON.stringify(spec, null, 2);
  fs.writeFileSync(outputFile, outputContent, 'utf8');
  console.log(`Processed OpenAPI specification written to: ${outputFile}`);
} catch (error) {
  console.error(`Error writing output file: ${error.message}`);
  process.exit(1);
}

// Verify the output
const outputSize = fs.statSync(outputFile).size;
console.log(`Output file size: ${(outputSize / 1024).toFixed(2)} KB`);
console.log('Preprocessing completed successfully!');

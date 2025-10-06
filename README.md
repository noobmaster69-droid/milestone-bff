# Milestone microservices (skeleton)

This archive contains a multi-module Gradle project with 5 subprojects:
- userservice (auth + token issuer)
- productservice (resource server validating tokens)
- orderservice (resource server validating tokens + Feign)
- namingservice (Eureka server)
- cloudgateway (gateway with routes)

Notes:
- JWT secret is a symmetric key placed in code for demo purposes only. Replace with proper key management.
- The project is a skeleton to get started — you'll want to flesh out repositories, entities and proper token issuance/validation backed by a database or an authorization server.

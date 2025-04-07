# MCP Common Module

This module contains shared code, models, and utilities that are used by both the MCP client and server modules. By centralizing these components, we ensure consistency and reduce code duplication.

## Components

- **Models**: Data classes representing domain entities
- **Utilities**: Helper classes and validation utilities
- **Service Interfaces**: Common service contracts that can be implemented by both client and server

## Usage

To use this module in your project, add the following dependency to your Maven POM file:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>mcp-common</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Key Classes

- `LeadData`: Model representing sales lead information
- `LeadDataValidator`: Utility for validating lead data
- `LeadService`: Service interface defining lead data operations

## Development

When adding new shared functionality:

1. Consider whether the functionality truly belongs in the common module
2. Ensure backward compatibility with existing implementations
3. Add comprehensive documentation
4. Include appropriate unit tests 
# MCP Client Demo

This application demonstrates how to integrate Spring AI with the Model Context Protocol (MCP) to create an AI assistant with access to external tools.

## Running the Application with the Standalone MCP Server

This demo is designed to work with the standalone MCP server in the `mcp-server-demo` directory. To run the complete demo:

1. **First, start the MCP server:**
   ```bash
   cd ../mcp-server-demo
   mvn spring-boot:run
   ```
   This will start the MCP server on port 8092, exposing several data analysis tools.

2. **Then, start this MCP client application:**
   ```bash
   cd ../mcp-client-demo
   mvn spring-boot:run
   ```
   This will start the client application on port 8080 and connect to the MCP server.

3. **Access the application at http://localhost:8080**

## Features

- AI-powered chatbot interface
- Integration with Spring AI 1.0.0-M6
- MCP client for invoking external tools
- Claude 3.7 Sonnet LLM integration
- React frontend

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Node.js 16+ and npm
- API key for Anthropic's Claude model

## Configuration Options

The application can be configured through `application.yml`:

```yaml
spring:
  ai:
    anthropic:
      api-key: ${ANTHROPIC_API_KEY:demo-key-replace-with-actual}
      model: claude-3-7-sonnet-20250219
      options:
        temperature: 0.7
        max-tokens: 4000
    mcp:
      client:
        enabled: true
        name: mcp-client-demo
        version: 1.0.0
        type: SYNC
        request-timeout: 30s
        sse:
          connections:
            local-server:
              url: http://localhost:8092/api
```

## Development

For frontend development:

```bash
# Install dependencies
npm install

# Start webpack in watch mode
npm run watch
```

## Available MCP Tools

The standalone MCP server provides several data analysis tools:

1. **analyzeData** - Analyze data synchronously and return results immediately
2. **analyzeDataWithUpdates** - Analyze data with real-time updates during processing
3. **monitorDataSource** - Monitor a data source in real-time

## Architecture

- **Spring Boot backend**: Serves the API and static content
- **Spring AI**: Manages AI model interaction
- **MCP Client**: Connects to external MCP servers
- **React frontend**: Provides the user interface

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
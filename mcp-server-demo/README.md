# MCP Server Demo

This project is a demonstration of a Spring AI MCP (Model Context Protocol) server that exposes data analysis tools to AI models. It works in conjunction with the MCP client demo application in this repository.

## Features

- Exposes data analysis tools via the Model Context Protocol
- Spring AI 1.0.0-M6 integration
- Reactive SSE-based data streaming for real-time updates
- Example implementation of synchronous and streaming tools

## Quick Start

1. **Set environment variables (optional)**
   ```bash
   export OPENAI_API_KEY=your-openai-api-key
   ```

2. **Start the server**
   ```bash
   mvn spring-boot:run
   ```
   
   The server will start on port 8092 with context path `/api`.

3. **Verify the server is running**
   
   Open a browser and go to: http://localhost:8092/api/status
   
   You should see a JSON response with the server status and available tools.

## Available Tools

This MCP server exposes the following tools:

1. **analyzeData** - Performs synchronous data analysis and returns results immediately
2. **analyzeDataWithUpdates** - Streams analysis results in real-time as they become available
3. **monitorDataSource** - Continuously monitors a data source and streams updates

## Using with the MCP Client Demo

1. Start this MCP server first
2. Then start the MCP client demo
3. Use the client's UI to chat with the AI, which will have access to the tools provided by this server

## Configuration

The server is configured in `src/main/resources/application.yml`. Key configurations include:

```yaml
server:
  port: 8092
  servlet:
    context-path: /api

spring:
  ai:
    mcp:
      server:
        name: mcp-server-demo
        version: 1.0.0
        type: SYNC
        message-endpoint: /mcp/stream
```

## Architecture

- **Spring Boot backend** with Spring AI integration
- **MCP Server** exposes tools to AI models
- **Data Analysis Service** provides example implementations of data processing tools
- **RESTful APIs** for status monitoring

## Troubleshooting

- If clients cannot connect, check:
  - Server is running on port 8092
  - Context path is /api
  - Client is configured to connect to http://localhost:8092/api
  - Network settings (if running on different machines)

## License

This project is licensed under the MIT License. 
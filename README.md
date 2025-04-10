# Building AI-Powered Applications with Spring AI and Model Context Protocol

This demonstration project showcases how to build AI applications that leverage specialized tools through Spring AI's implementation of the [Model Context Protocol (MCP)](https://github.com/Cohesible/model-context-protocol).

## What is Model Context Protocol?

Model Context Protocol (MCP) enables AI applications to access specialized tools and services. It works by:

1. **Exposing tools**: A server exposes specialized tools that AI systems can use
2. **Connecting AI models**: Allows AI models to discover and use these tools
3. **Standardizing interactions**: Provides a consistent way for AI to interact with external capabilities

## Project Architecture

This demo consists of two main applications:

### 1. MCP Server (`mcp-server-demo`)

A Spring Boot application that exposes data analysis tools through the Model Context Protocol. These tools can be discovered and called by AI models.

Key features:
- Exposes three demo tools: `analyzeData`, `analyzeDataWithUpdates`, and `monitorDataSource`
- Implements the server-side of MCP using Spring AI
- Runs on port 8092 with context path `/api`

### 2. MCP Client (`mcp-client-demo`)

A Spring Boot application with React frontend that:
- Connects to an AI model (Anthropic Claude)
- Connects to the MCP server to access its tools
- Provides a chat interface where users can interact with the AI

Key features:
- Chat interface built with React
- Spring AI integration with Anthropic Claude
- Tool-calling capabilities via MCP client
- Runs on port 8080 (backend) and can be accessed via port 5000 (development server)

## How It Works

![Architecture Diagram](https://mermaid.ink/img/pako:eNqNkk1rwzAMhv-K0KmF5QfZoZRQWA87bGy9ZLeQg5I4jUljB9vZGKP_fbaTrmuhgx1k6dWjVx-zwpjMMwUqViEVLB2h7hD3pMFZrq0N6OAGGwp-mE-m42a46-eDcRXFxeK2xbAI9iH6GFl3Xxe3eDOdbM8jYtQhALNy3pFmrnWBuGXXwH2IVr7m2t33o3G_j6fj8fwbFoq98FamWTe9c9Zg-1b2xawfbmfTH9a6QZC8dLAT7pK3LEgXF4-EoUlp0tM0yd2r1kTTJFrBZFFTTGRpnmqtJJdSQaGZxXRu0sYU1FaQzb94dSIo7iRDRk4eBEvO1GGQsxJ9nBF6v-OtSoNZBa9tJR_Qs9qg0iR8S5nA8ywLTGN2RzUqZYGmpQpUbLPzSPu4Ef6t9GKfwU958tNTJnJ_1zH4ItCL01PtTnrHK-0OeO2ZEzKlPdS6y9rL-n6b6XO1E7rI4eHpFTquq-SPX-N_Nq8?type=png)

1. **User Interaction**:
   - User accesses the client application at http://localhost:5000
   - Sends messages through the chat interface

2. **AI Processing**:
   - Spring AI forwards the user message to Anthropic Claude
   - Claude processes the message and may identify the need for a tool

3. **Tool Calling**:
   - If Claude identifies a task for a tool, Spring AI connects to the MCP server
   - The MCP client discovers and calls the appropriate tool
   - The tool executes and returns results

4. **Response Generation**:
   - Claude receives the tool results
   - Generates a final response incorporating the tool outputs
   - The response is sent back to the user

## Key Components

### MCP Server Components

1. **DataAnalysisMcpConfiguration.java**
   - Registers data analysis tools with the MCP server
   - Uses Spring's `@Configuration` and `@Bean` to define tools
   - Uses `MethodToolCallbackProvider` to expose methods as tools

2. **DataAnalysisService.java**
   - Contains the actual implementation of the data analysis tools
   - Simulates data processing and analysis capabilities

3. **StatusController.java**
   - REST endpoint that reports the status of the MCP server
   - Provides information about available tools

### MCP Client Components

1. **ChatbotController.java**
   - REST endpoint that handles user chat messages
   - Uses Spring AI's `ChatClient` to interact with Anthropic Claude
   - Manages conversation context

2. **McpClientService.java**
   - Handles connections to the MCP server
   - Discovers and calls tools on the MCP server
   - Acts as a bridge between the AI and external tools

3. **React Frontend (app.js)**
   - Provides the chat interface
   - Manages message history and displays AI responses
   - Shows available MCP tools in the sidebar

## Intelligent Tool Resolution

The client application implements an intelligent tool resolution approach that optimizes the interaction between the AI model and available tools:

1. **Automatic Tool Resolution**
   - The system can automatically identify which tools are relevant to a user query
   - This is controlled by the `app.tools.autoResolve` property (default: true)
   - Implemented via the `ToolResolverService` which analyzes user queries

2. **Non-Verbose Initial Tool List**
   - Instead of overwhelming the AI model with all available tools and their detailed schemas
   - The system first provides a high-level list of available tools
   - This minimizes token usage and improves model performance

3. **Agent-Driven Tool Selection**
   - Based on the user query, the AI agent can request specific tools it needs
   - The `toolResolverService` identifies the most relevant tools for the current context
   - Selected tools are stored in the session state for the conversation

4. **Tool Deduplication**
   - The system intelligently manages tool availability to prevent duplicates
   - Filters out tools that are already provided by default providers
   - Prevents errors like "Multiple tools with the same name" that could occur with overlapping tools

This approach offers several benefits:
- **Reduced token usage**: Only sending relevant tool definitions to the model
- **Improved response quality**: The model can focus on relevant tools instead of parsing many options
- **Better context management**: Tools can be dynamically added or removed as the conversation evolves
- **Enhanced reliability**: Avoiding duplicate tool definitions prevents runtime errors

## Spring AI Integration

This project leverages several key Spring AI features:

1. **AI Model Integration**
   - Uses Spring AI's Anthropic integration to connect to Claude
   - Configuration via `application.yml` properties

2. **Tool Calling Support**
   - Spring AI's tool calling framework allows Claude to use external tools
   - Manages the tool calling protocol between the AI and tools

3. **MCP Implementation**
   - Spring AI provides built-in support for the Model Context Protocol
   - `spring-ai-mcp-server-spring-boot-starter` for the server side
   - `spring-ai-mcp-client-spring-boot-starter` for the client side

## Running the Demo

### Prerequisites
- Java 17+ 
- Maven
- Node.js and npm
- An Anthropic API key

### Step 1: Start the MCP Server

```bash
cd blogCode/mcp-server-demo
mvn spring-boot:run
```

The server will start on port 8092. Verify it's running by accessing http://localhost:8092/api/status

### Step 2: Start the MCP Client

```bash
cd blogCode/mcp-client-demo
export ANTHROPIC_API_KEY=your_api_key_here
mvn spring-boot:run
```

The Spring Boot backend will start on port 8080.

### Step 3: Start the React Frontend (Development Mode)

```bash
cd blogCode/mcp-client-demo
npm start
```

This will open a browser window at http://localhost:5000 with the chat interface.

### Using the Demo

1. Type a message in the chat interface
2. The AI will respond and may use one of the available data analysis tools
3. Example prompts:
   - "Can you analyze this data: 123,456,789,101,112"
   - "Monitor the temperature data source for 20 seconds"
   - "Analyze this data with updates: user engagement metrics for Q1"

## Configuration

### MCP Server Configuration (application.yml)

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
        sse-message-endpoint: /mcp/message
```

### MCP Client Configuration (application.yml)

```yaml
server:
  port: 8080

spring:
  ai:
    anthropic:
      api-key: ${ANTHROPIC_API_KEY}
      model: claude-3-7-sonnet-20250219
    mcp:
      client:
        enabled: true
        sse:
          connections:
            local-server:
              url: http://localhost:8092/api
      server:
        enabled: false

app:
  tools:
    autoResolve: true  # Enable automatic tool resolution based on user queries
```

## Conclusion

This demo shows how Spring AI and the Model Context Protocol can be used to build powerful AI applications that leverage specialized tools. The architecture can be extended to include additional tools and services, making it a flexible foundation for AI-powered applications.

By separating the tools (MCP server) from the AI interface (MCP client), this approach allows for:

1. **Modularity**: Add or modify tools without changing the AI interface
2. **Reusability**: The same tools can be used by different AI applications
3. **Separation of concerns**: Tool developers can focus on their domain expertise

Spring AI simplifies this integration by providing standardized components and configurations that handle the complexity of AI model integration and tool calling. 
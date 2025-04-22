# MCP from API Definition for Timefold Quickstarts

This repository contains the code for an MCP server which allows you to modify certain aspects of the Timefold Quickstarts.

> [!NOTE]
> This repository doesn't work with the default quickstarts of Timefold, they require a special branch.
> Consider this more as a project for inspiration.

## Architecture

I have chosen to use a barebones setup using Quarkus:

- Some simple Tool endpoints, these can be used by an MCP Client (like Claude desktop or Goose).
- A generated API client from the OpenAPI Spec.

## Run Config 

Add this configuration to the Claude Configuration, it will use JBang to run the application.

```json
{
  "mcpServers": {
    "Timefold-mcp-quickstarts": {
      "command": "jbang",
      "args": [
        "be.tomcools:timefold-mcp-for-quickstarts:1.0.0-SNAPSHOT:runner"
      ]
    }
  }
}
```
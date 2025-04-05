import React, { useState, useEffect, useRef } from 'react';
import { createRoot } from 'react-dom/client';
import './app.css';

const Chatbot = () => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [mcpStatus, setMcpStatus] = useState({ connected: false, availableTools: [] });
  const messagesEndRef = useRef(null);

  useEffect(() => {
    // Check MCP connection status on component mount
    fetchMcpStatus();
  }, []);

  useEffect(() => {
    // Scroll to bottom when messages change
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const fetchMcpStatus = async () => {
    try {
      const response = await fetch('/api/chatbot/status');
      const data = await response.json();
      setMcpStatus(data);
    } catch (error) {
      console.error('Error fetching MCP status:', error);
    }
  };

  const handleSendMessage = async () => {
    if (!input.trim()) return;

    const userMessage = {
      role: 'user',
      content: input,
      id: Date.now().toString(),
    };

    setMessages((prevMessages) => [...prevMessages, userMessage]);
    setInput('');
    setIsLoading(true);

    try {
      const payload = {
        messages: [...messages, userMessage].map(msg => ({
          role: msg.role,
          content: msg.content
        }))
      };

      const endpoint = messages.length === 0 
        ? '/api/chatbot/startConversation' 
        : '/api/chatbot/continueConversation';

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      const data = await response.json();

      if (response.ok) {
        setMessages((prevMessages) => [
          ...prevMessages, 
          {
            role: 'assistant',
            content: Array.isArray(data.content) 
              ? data.content[0].text 
              : data.content,
            id: data.id || Date.now().toString(),
          }
        ]);
      } else {
        console.error('Error from AI service:', data.error);
        setMessages((prevMessages) => [
          ...prevMessages,
          {
            role: 'assistant',
            content: `Error: ${data.error || 'Something went wrong'}`,
            id: Date.now().toString(),
            isError: true,
          },
        ]);
      }
    } catch (error) {
      console.error('Error sending message:', error);
      setMessages((prevMessages) => [
        ...prevMessages,
        {
          role: 'assistant',
          content: `Error: ${error.message || 'Failed to connect to the server'}`,
          id: Date.now().toString(),
          isError: true,
        },
      ]);
    } finally {
      setIsLoading(false);
    }
  };

  const ToolsList = () => {
    if (!mcpStatus.connected) {
      return <div className="tools-status">MCP Server: Not Connected</div>;
    }

    return (
      <div className="tools-container">
        <div className="tools-status">MCP Server: Connected</div>
        <h3>Available Tools</h3>
        <ul className="tools-list">
          {mcpStatus.availableTools.map((tool, index) => (
            <li key={index} className="tool-item">
              <strong>{tool.name}</strong>
              <p>{tool.description}</p>
            </li>
          ))}
        </ul>
      </div>
    );
  };

  return (
    <div className="chatbot-container">
      <div className="header">
        <h1>MCP Client Demo</h1>
        <p>Chat with the AI assistant to use MCP tools</p>
      </div>
      
      <div className="chat-window">
        <div className="messages-container">
          {messages.length === 0 ? (
            <div className="empty-chat">
              <p>No messages yet. Start a conversation!</p>
            </div>
          ) : (
            messages.map((message) => (
              <div
                key={message.id}
                className={`message ${message.role} ${message.isError ? 'error' : ''}`}
              >
                <div className="message-header">
                  {message.role === 'user' ? 'You' : 'Assistant'}
                </div>
                <div className="message-content">{message.content}</div>
              </div>
            ))
          )}
          {isLoading && (
            <div className="message assistant loading">
              <div className="message-header">Assistant</div>
              <div className="message-content">
                <div className="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
          )}
          <div ref={messagesEndRef} />
        </div>
        
        <div className="input-area">
          <textarea
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                handleSendMessage();
              }
            }}
            placeholder="Type your message here..."
            disabled={isLoading}
          />
          <button 
            onClick={handleSendMessage} 
            disabled={isLoading || !input.trim()}
          >
            Send
          </button>
        </div>
      </div>
      
      <div className="sidebar">
        <ToolsList />
      </div>
    </div>
  );
};

const App = () => {
  return (
    <div className="app-container">
      <Chatbot />
    </div>
  );
};

// Render the app
const rootElement = document.getElementById('root');
if (rootElement) {
  const root = createRoot(rootElement);
  root.render(<App />);
} 
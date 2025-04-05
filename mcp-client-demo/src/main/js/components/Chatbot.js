import React, { useState, useRef, useEffect } from 'react';
import './Chatbot.css';

const Chatbot = () => {
    const [messages, setMessages] = useState([
        {
            role: 'assistant',
            content: 'Hello! I am an AI assistant with access to data analysis tools. How can I help you today?'
        }
    ]);
    const [input, setInput] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [toolsAvailable, setToolsAvailable] = useState(false);
    const [executingTool, setExecutingTool] = useState(false);
    
    const messagesEndRef = useRef(null);
    
    // Check MCP connection status on mount
    useEffect(() => {
        checkMcpStatus();
    }, []);
    
    // Scroll to bottom when messages change
    useEffect(() => {
        scrollToBottom();
    }, [messages]);
    
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };
    
    const checkMcpStatus = () => {
        fetch('/api/chatbot/status')
            .then(response => response.json())
            .then(data => {
                console.log("MCP Status:", data);
                const toolsAvailable = data.connected === true && data.availableTools && data.availableTools.length > 0;
                setToolsAvailable(toolsAvailable);
                
                if (toolsAvailable) {
                    console.log("MCP tools available:", data.availableTools);
                } else {
                    console.warn("MCP tools not available. Full functionality may be limited.");
                }
            })
            .catch(error => {
                console.error("Error checking MCP status:", error);
                setToolsAvailable(false);
            });
    };
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!input.trim()) {
            return;
        }
        
        // Stop any ongoing operations when user sends a new message
        setExecutingTool(false);
        
        const userMessage = {
            role: 'user',
            content: input
        };
        
        setMessages(prev => [...prev, userMessage]);
        setInput('');
        setIsLoading(true);
        
        try {
            const endpoint = messages.length <= 2 
                ? '/api/chatbot/startConversation' 
                : '/api/chatbot/continueConversation';
                
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    model: 'claude-3-7-sonnet-20250219',
                    max_tokens: 4000,
                    messages: [...messages, userMessage],
                    system: "You are a helpful AI assistant with access to tools for data analysis. Use the tools when needed to answer questions that require complex calculations or real-time data processing."
                }),
            });
            
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            
            const data = await response.json();
            console.log("Response from AI:", data);
            
            // Check if AI is using a tool
            if (data.stop_reason === "tool_use") {
                handleToolUse(data, [...messages, userMessage]);
            } else {
                // Extract response text
                let responseText = extractResponseText(data);
                
                setMessages(prev => [...prev, {
                    role: 'assistant',
                    content: responseText
                }]);
                setIsLoading(false);
            }
        } catch (error) {
            console.error('Error:', error);
            
            setMessages(prev => [...prev, {
                role: 'assistant',
                content: 'I apologize, but I encountered an error while processing your request. Please try again later.'
            }]);
            
            setIsLoading(false);
        }
    };
    
    const handleToolUse = async (data, previousMessages) => {
        // Show "thinking" message to the user
        setMessages(prev => [...prev, {
            role: 'assistant',
            content: "I'm gathering some technical information to help answer your question..."
        }]);
        
        setExecutingTool(true);
        
        try {
            const response = await fetch('/api/chatbot/continueConversation', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    model: 'claude-3-7-sonnet-20250219',
                    max_tokens: 4000,
                    messages: previousMessages,
                    system: "You are a helpful AI assistant with access to tools for data analysis. Use the tools when needed to answer questions that require complex calculations or real-time data processing."
                }),
            });
            
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            
            const finalData = await response.json();
            console.log("Final response after tool execution:", finalData);
            
            // Replace the "thinking" message with the actual response
            let responseText = extractResponseText(finalData);
            
            setMessages(prev => {
                const updatedMessages = [...prev];
                updatedMessages[updatedMessages.length - 1] = {
                    role: 'assistant',
                    content: responseText
                };
                return updatedMessages;
            });
            
            setIsLoading(false);
            setExecutingTool(false);
            
            // If Claude is requesting to use another tool in the chain, handle it
            if (finalData.stop_reason === "tool_use") {
                handleToolUse(finalData, [...previousMessages, {
                    role: 'assistant',
                    content: responseText
                }]);
            }
        } catch (error) {
            console.error('Error executing tool:', error);
            
            // Replace the "thinking" message with an error message
            setMessages(prev => {
                const updatedMessages = [...prev];
                updatedMessages[updatedMessages.length - 1] = {
                    role: 'assistant',
                    content: 'I apologize, but I encountered an error while gathering the technical information. Please try again later.'
                };
                return updatedMessages;
            });
            
            setIsLoading(false);
            setExecutingTool(false);
        }
    };
    
    const extractResponseText = (data) => {
        if (!data.content || data.content.length === 0) {
            return 'I apologize, but I could not generate a response.';
        }
        
        // Extract text from response
        let responseText = "";
        
        // Try to find text content
        for (let i = 0; i < data.content.length; i++) {
            const content = data.content[i];
            if (typeof content === 'string') {
                responseText = content;
                break;
            } else if (content.type === 'text') {
                responseText = content.text;
                break;
            } else if (content.text) {
                responseText = content.text;
                break;
            }
        }
        
        // If no text was found, use a generic message
        if (!responseText) {
            responseText = "I apologize, but I could not generate a response.";
        }
        
        // Remove <thinking> blocks if present
        responseText = responseText.replace(/<thinking>[\s\S]*?<\/thinking>/g, '').trim();
        
        return responseText;
    };
    
    const renderMessageContent = (content) => {
        if (!content) return '';
        
        // Split by code blocks (```...```)
        const parts = content.split(/(```(?:.*?)\n[\s\S]*?```)/g);
        
        if (parts.length === 1) {
            // No code blocks, return as is with line breaks converted to <br>
            return content.split('\n').map((line, i) => (
                <span key={i}>
                    {line}
                    <br />
                </span>
            ));
        }
        
        return parts.map((part, index) => {
            // Check if this part is a code block
            if (part.startsWith('```') && part.endsWith('```')) {
                // Extract language and code
                let code = part.substring(3, part.length - 3).trim();
                let language = '';
                
                // Check if there's a language specified
                const firstLineBreak = code.indexOf('\n');
                if (firstLineBreak > 0) {
                    language = code.substring(0, firstLineBreak).trim();
                    code = code.substring(firstLineBreak + 1).trim();
                }
                
                return (
                    <div key={index} className="code-block">
                        {language && <div className="code-language">{language}</div>}
                        <pre><code>{code}</code></pre>
                    </div>
                );
            } else {
                // Regular text, split by line breaks
                return part.split('\n').map((line, i) => (
                    <span key={`${index}-${i}`}>
                        {line}
                        <br />
                    </span>
                ));
            }
        });
    };
    
    return (
        <div className="chatbot-container">
            <div className="chatbot-header">
                <div className="title-container">
                    <span className="title">AI Data Analysis Assistant</span>
                </div>
                
                <div className="tools-container">
                    {toolsAvailable && 
                        <span className="tool-badge">Tools Available</span>
                    }
                    {executingTool &&
                        <span className="tool-badge executing">Executing Tool</span>
                    }
                </div>
            </div>
            
            <div className="message-container">
                {messages.map((message, index) => (
                    <div 
                        key={index} 
                        className={"message " + (message.role === 'assistant' ? 'assistant' : 'user')}
                    >
                        {renderMessageContent(message.content)}
                    </div>
                ))}
                
                {isLoading && (
                    <div className="message assistant loading">
                        <div className="typing-indicator">
                            <span></span>
                            <span></span>
                            <span></span>
                        </div>
                    </div>
                )}
                
                <div ref={messagesEndRef} />
            </div>
            
            <div className="chatbot-footer">
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <input
                            type="text"
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                            placeholder="Ask me about data analysis..."
                            disabled={isLoading || executingTool}
                        />
                        <button 
                            type="submit" 
                            disabled={isLoading || executingTool || !input.trim()}
                        >
                            Send
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Chatbot; 
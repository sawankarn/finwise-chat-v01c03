# 📗 Volume 1 · Chapter 3: Making Sense of Intelligence: Output & Evaluation

# FinWise Chat

> **Edition B — NextGen Visual Edition**
> _Story-driven · Illustration-rich · Production-oriented_

Welcome to the third chapter of the FinWise AI Engineering Academy! This module focuses on turning chaotic LLM strings into reliable, strongly-typed Java objects and evaluating model truthfulness.

---

## 📝 Chapter Summary

When you move an LLM from a fun demo to a production system, you are no longer just looking at the output and saying, "Looks good!" You need automated, programmatic guarantees. This chapter introduces Spring AI's `BeanOutputConverter` to enforce strict JSON schemas and maps them directly into Java Records. We also explore the LLM Evaluation Triangle (Relevance, Accuracy, Safety) and how to catch hallucinations before they reach the user.

---

## 📖 Chapter Learning Objectives

- [ ] Test LLM outputs for quality
- [ ] Parse structured responses into Java Records
- [ ] Understand the Evaluation Triangle (Relevance, Accuracy, Safety)
- [ ] Use `BeanOutputConverter` to enforce JSON schemas
- [ ] Conceptualize LLM-based Evaluators (Fact-checking)

---

## 🚀 Features

- **Spring AI Integration**: Uses the stable `1.0.0` release.
- **Structured Output**: Uses `BeanOutputConverter` to guarantee strict JSON schemas that map to `FinancialAdviceResponse`.
- **System Prompts**: Configured to act strictly as a FinWise financial advisor.
- **Streaming Responses**: Delivers faster, token-by-token responses to improve UX.
- **PromptTemplates**: Dynamic context injection using variables.
- **Gemini Powered**: Configured out of the box to use Google's `gemini-2.5-flash` model.
- **Local Fallback**: Includes an `application-local.yml` profile for running offline with Ollama.
- **Java 25 Ready**: Pre-configured to build with JDK 21 (Loom EA).

---

## 📁 Folder Structure

```text
chapter3/
├── ch03_chapter3.md             ← Full chapter content (NextGen Visual format)
├── ch03_quiz.md                 ← Chapter quiz with answers
├── ch03_mission.md              ← Hands-on mission card
└── finwise-chat-v01c03/         ← Working Spring Boot project
    ├── README.md                ← You are here
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/finwise/
        │   │   ├── FinwiseChatApplication.java
        │   │   ├── chat/
        │   │   │   ├── ChatModels.java        ← Updated with FinancialAdviceResponse
        │   │   │   ├── ChatService.java       ← Uses BeanOutputConverter
        │   │   │   └── ChatController.java    ← Exposes /api/chat/structured
        │   │   └── config/
        │   │       └── AiConfig.java
        │   └── resources/
        │       ├── application.yml
        │       └── application-local.yml
        └── test/
            └── java/com/finwise/chat/
                └── ChatServiceTest.java
```

---

## 🛠️ Prerequisites

1. **JDK 21**: Make sure you have JDK 21 installed.
2. **Gemini API Key**: Get a free API key from [Google AI Studio](https://aistudio.google.com/).

---

## 💻 How to Run

### Option A: Using Command Prompt (`cmd.exe`)

Open your command prompt, navigate to this directory, and run the following commands:

```cmd
:: 1. Point to your JDK 21 installation
set JAVA_HOME=C:\path\to\your\jdk-21

:: 2. Set your Gemini API key
set GEMINI_API_KEY=your_actual_key_here

:: 3. Run the application
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

### Option B: Using PowerShell

Open PowerShell, navigate to this directory, and run:

```powershell
# 1. Point to your JDK 21 installation
$env:JAVA_HOME="C:\path\to\your\jdk-21"

# 2. Set your Gemini API key
$env:GEMINI_API_KEY="your_actual_key_here"

# 3. Run the application
.\mvnw clean install
.\mvnw spring-boot:run
```

---

## 🧪 Testing the Application (Supported Examples)

Once the application starts successfully (usually on port 8080), you can test the REST endpoints. The application exposes three main endpoints:

### 1. Send a Chat Message (`POST /api/chat/structured`) - NEW in Chapter 3

This endpoint guarantees a structured JSON response instead of a plain string.

**Using curl:**

```bash
curl -X POST http://localhost:8080/api/chat/structured \
  -H "Content-Type: application/json" \
  -d "{\"message\": \"I want to save for a house down payment in 2 years.\"}"
```

**Expected Response (Pure JSON Object):**

```json
{
  "summary": "To save for a house down payment in 2 years, prioritize safety and liquidity...",
  "actionItems": [
    "Open a High-Yield Savings Account (HYSA).",
    "Set up automated monthly transfers.",
    "Avoid volatile investments like stocks or crypto."
  ],
  "riskLevel": "Low"
}
```

### 2. Standard Chat Message (`POST /api/chat`)

**Using curl:**

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\": \"What is compound interest?\", \"sessionId\": \"user-123\"}"
```

### 3. Check AI Health (`GET /api/chat/health`)

**Using curl:**

```bash
curl http://localhost:8080/api/chat/health
```

### 4. Stream a Chat Message (`POST /api/chat/stream`)

**Using curl:**

```bash
curl -X POST http://localhost:8080/api/chat/stream \
  -H "Content-Type: application/json" \
  -d "{\"message\": \"What is an ISA?\"}"
```

### 5. Get Concept Summary (`GET /api/chat/summary`)

**Using curl:**

```bash
curl "http://localhost:8080/api/chat/summary?concept=inflation"
```

---

## 🦙 Running Locally with Ollama (Offline)

1. Install [Ollama](https://ollama.com/) and run a local model (e.g., `ollama run llama3`).
2. Run the Spring Boot application with the `local` profile activated:

   **Command Prompt:**

   ```cmd
   .\mvnw.cmd clean install
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
   ```

   **PowerShell:**

   ```powershell
   .\mvnw clean install
   .\mvnw spring-boot:run "-Dspring-boot.run.profiles=local"
   ```

---

_Previous: [Chapter 2 — Talking to Intelligence: Prompts & Models](../../chapter2/finwise-chat-v01c02/README.md)_

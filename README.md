# Supply Chain Control Tower — AI-Powered Operations

> **DEMO-001** · Autonomous Supply Chain Orchestration (Enterprise Edition)
> Built from Jira story [SCRUM-23](https://carlosfloresdfw.atlassian.net)

A full-stack proof-of-concept demonstrating an **AI-First** approach to supply chain management. A Java/Spring Boot backend continuously emits inventory shortage signals and runs an autonomous reasoning loop to resolve them. A Next.js dashboard visualizes every decision in real time over a Server-Sent Events (SSE) stream.

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Next.js 14 Frontend                       │
│   Header · LogisticsFeed (sidebar) · ControlTower · Cards   │
│                   (http://localhost:3000)                    │
└──────────────────────┬──────────────────────────────────────┘
                       │  SSE  /api/events/stream
                       │  (proxied via next.config.ts)
┌──────────────────────▼──────────────────────────────────────┐
│                  Spring Boot 3 Backend                       │
│                   (http://localhost:8080)                    │
│                                                             │
│  InventoryEventProducer  ──►  ReasoningService              │
│      @Scheduled/15s            ├── WarehouseService         │
│      TEMPLE_TX_DIST            │   (nearest hub lookup)     │
│      STOCK_OUT signal          └── SseEmitterService        │
│                                    (broadcast to clients)   │
└─────────────────────────────────────────────────────────────┘
```

### Simulation Loop

1. Every **15 seconds** the producer fires a `STOCK_OUT` or `DELAY_DETECTED` event for the Temple, TX distribution center.
2. The **Reasoning Service** queries four secondary hubs (Dallas, Austin, San Antonio, Houston), selects the nearest one with sufficient stock, and calculates an Impact Score.
3. A **ResolutionDTO** is generated — either `AGENT_RESOLVED` (green) or `HUMAN_REQUIRED` (red, ~20% of events).
4. Both objects are pushed over SSE to every connected browser tab — **no manual refresh needed**.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend language | Java 17 |
| Backend framework | Spring Boot 3.2.3 |
| Build tool | Maven |
| Messaging (simulated) | Spring `@Scheduled` + `CopyOnWriteArrayList<SseEmitter>` |
| Database | In-memory Java collections (no external DB required) |
| Frontend framework | Next.js 14.2.3 (App Router) |
| Frontend language | TypeScript 5 |
| Styling | Tailwind CSS 3 (dark mode, McLane Blue palette) |
| Real-time transport | Server-Sent Events (SSE) |

---

## Project Structure

```
supply-chain-ai-demo/
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/supplychainai/
│       │   ├── SupplyChainAiApplication.java
│       │   ├── config/
│       │   │   └── CorsConfig.java
│       │   ├── controller/
│       │   │   └── SseController.java          # /api/events/stream · /api/warehouses · /api/health
│       │   ├── model/
│       │   │   ├── InventoryEvent.java          # eventId, timestamp, warehouseId, sku, status, qtyGap
│       │   │   ├── ResolutionDTO.java           # resolutionId, nearestHub, impactScore, justification, status
│       │   │   └── WarehouseStock.java          # warehouseId, city, availableStock, distanceMiles
│       │   └── service/
│       │       ├── InventoryEventProducer.java  # @Scheduled every 15s
│       │       ├── ReasoningService.java        # AI tool-use loop
│       │       ├── SseEmitterService.java       # SSE lifecycle & broadcast
│       │       └── WarehouseService.java        # Mock hub inventory
│       └── resources/
│           └── application.properties
│
└── frontend/
    ├── next.config.ts                           # Proxies /api/* → localhost:8080
    ├── tailwind.config.ts
    ├── package.json
    └── src/
        ├── types/
        │   └── index.ts                         # InventoryEvent & ResolutionDTO interfaces
        └── app/
            ├── layout.tsx
            ├── page.tsx                         # Root page — wires all components
            ├── globals.css
            ├── hooks/
            │   └── useSupplyChainSSE.ts         # EventSource hook, 50-item ring buffer
            └── components/
                ├── Header.tsx                   # Branding + live metrics
                ├── LogisticsFeed.tsx            # Left sidebar — raw event stream
                ├── ControlTower.tsx             # Main panel — resolution cards
                └── ResolutionCard.tsx           # Green / Red card + AI Reasoning section
```

---

## Prerequisites

| Tool | Minimum version |
|---|---|
| Java JDK | 17 |
| Maven | 3.8+ (or use the Maven wrapper if added) |
| Node.js | 18+ |
| npm | 9+ |

---

## Running the App

### 1 — Start the Spring Boot backend

```bash
cd backend
mvn spring-boot:run
```

The backend starts on **http://localhost:8080**.
Verify it is healthy:

```bash
curl http://localhost:8080/api/health
# {"status":"UP","activeConnections":0}
```

### 2 — Start the Next.js frontend

Open a **second terminal**:

```bash
cd frontend
npm install        # first run only
npm run dev
```

The frontend starts on **http://localhost:3000**.

### 3 — Open the dashboard

Navigate to **[http://localhost:3000](http://localhost:3000)** in your browser.

The `LIVE` indicator in the top-right corner turns green as soon as the SSE connection is established. The first supply chain signal arrives within 15 seconds and the simulation loop runs indefinitely without any manual action.

---

## API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/events/stream` | SSE stream — emits `connected`, `inventory-event`, `resolution` events |
| `GET` | `/api/warehouses` | Returns the mock hub inventory list as JSON |
| `GET` | `/api/health` | Returns backend status and active SSE connection count |

### SSE Event Types

**`connected`**
```json
{ "message": "Connected to Supply Chain Control Tower", "timestamp": "2026-03-15T12:00:00" }
```

**`inventory-event`**
```json
{
  "eventId": "A1B2C3D4",
  "timestamp": "2026-03-15T12:00:15",
  "warehouseId": "TEMPLE_TX_DIST",
  "sku": "MCL-990-DRINK",
  "status": "STOCK_OUT",
  "qtyGap": 420
}
```

**`resolution`**
```json
{
  "resolutionId": "E5F6G7H8",
  "eventId": "A1B2C3D4",
  "resolvedAt": "2026-03-15T12:00:15",
  "sourceWarehouse": "TEMPLE_TX_DIST",
  "nearestHub": "DALLAS_HUB",
  "qtyTransferred": 420,
  "impactScore": 126,
  "justification": "Found surplus at Dallas, TX; initiated 420-unit transfer to protect OTIF for 126 local retailers.",
  "resolutionStatus": "AGENT_RESOLVED"
}
```

---

## Mock Warehouse Network

| Hub ID | City | Stock | Distance from Temple, TX |
|---|---|---|---|
| `DALLAS_HUB` | Dallas, TX | 2,200 units | 62 mi |
| `AUSTIN_HUB` | Austin, TX | 1,800 units | 68 mi |
| `SAN_ANTONIO_HUB` | San Antonio, TX | 3,500 units | 145 mi |
| `HOUSTON_HUB` | Houston, TX | 5,000 units | 183 mi |

The agent always selects the **nearest hub** that can satisfy the required quantity. A simulated 20% chance of full network depletion triggers the `HUMAN_REQUIRED` escalation path.

---

## UI Features

- **Dark mode** dashboard with McLane Blue (`#1d4ed8`) accent palette
- **Left sidebar** — scrollable raw logistics feed, color-coded by event type
- **Control Tower** — card grid of autonomous resolutions
  - 🟢 Green border — `AGENT_RESOLVED`
  - 🔴 Red border — `HUMAN_REQUIRED`
- **AI Reasoning** section on every card with simulated LLM justification text
- **Live metrics** in the header — Active Signals, Agent Resolved, Human Required counts
- **Connection status** badge with animated pulse indicator

## Screenshots system running

<img width="2067" height="1785" alt="supply-chain-control-tower-2" src="https://github.com/user-attachments/assets/55d83d16-8c95-4dec-9359-5438677ac7f2" />


---

## Acceptance Criteria (from SCRUM-23)

| # | Criterion | Status |
|---|---|---|
| 1 | TypeScript frontend, Java POJOs backend | ✅ |
| 2 | Professional logistics dashboard — dark mode / McLane Blue | ✅ |
| 3 | Automatic simulation loop — no manual refresh | ✅ |

'use client'

import { useSupplyChainSSE } from './hooks/useSupplyChainSSE'
import Header       from './components/Header'
import LogisticsFeed from './components/LogisticsFeed'
import ControlTower  from './components/ControlTower'

export default function Page() {
  const { events, resolutions, isConnected, error, agentResolved, humanRequired } =
    useSupplyChainSSE()

  return (
    <div className="flex flex-col h-screen overflow-hidden">
      <Header
        isConnected={isConnected}
        eventsCount={events.length}
        agentResolved={agentResolved}
        humanRequired={humanRequired}
      />

      {/* Connection error banner */}
      {error && (
        <div className="flex items-center gap-2 px-6 py-2 bg-red-950/60 border-b border-red-900/60 text-red-400 text-xs shrink-0">
          <span className="w-1.5 h-1.5 rounded-full bg-red-400 animate-pulse" />
          {error}
        </div>
      )}

      <div className="flex flex-1 overflow-hidden">
        {/* Left sidebar: raw logistics feed */}
        <aside className="w-72 shrink-0 border-r border-slate-800 bg-slate-900/40 overflow-hidden flex flex-col">
          <LogisticsFeed events={events} />
        </aside>

        {/* Main: control tower */}
        <main className="flex-1 overflow-hidden">
          <ControlTower resolutions={resolutions} />
        </main>
      </div>
    </div>
  )
}

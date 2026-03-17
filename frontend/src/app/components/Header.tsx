'use client'

interface HeaderProps {
  isConnected:  boolean
  eventsCount:  number
  agentResolved: number
  humanRequired: number
}

export default function Header({ isConnected, eventsCount, agentResolved, humanRequired }: HeaderProps) {
  return (
    <header className="flex items-center justify-between px-6 py-3 bg-slate-900 border-b border-slate-800 shrink-0">
      {/* Brand */}
      <div className="flex items-center gap-3">
        <div className="flex items-center justify-center w-9 h-9 rounded-lg bg-mclane-700">
          <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
        </div>
        <div>
          <h1 className="text-base font-bold text-white leading-tight">Supply Chain Control Tower</h1>
          <p className="text-[11px] text-slate-500">McLane Distribution Network — AI-Powered Operations</p>
        </div>
      </div>

      {/* Metrics */}
      <div className="flex items-center gap-6">
        <Metric label="Active Signals"  value={eventsCount}   color="text-mclane-400" />
        <Metric label="Agent Resolved"  value={agentResolved} color="text-emerald-400" />
        <Metric label="Human Required"  value={humanRequired} color="text-red-400" />

        <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-slate-800 border border-slate-700">
          <span className={`w-2 h-2 rounded-full ${isConnected ? 'bg-emerald-400 animate-pulse' : 'bg-red-400'}`} />
          <span className={`text-xs font-semibold tracking-widest ${isConnected ? 'text-emerald-400' : 'text-red-400'}`}>
            {isConnected ? 'LIVE' : 'OFFLINE'}
          </span>
        </div>
      </div>
    </header>
  )
}

function Metric({ label, value, color }: { label: string; value: number; color: string }) {
  return (
    <div className="text-center">
      <div className={`text-2xl font-bold tabular-nums ${color}`}>{value}</div>
      <div className="text-[10px] text-slate-500 uppercase tracking-wider">{label}</div>
    </div>
  )
}

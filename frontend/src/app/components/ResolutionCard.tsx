'use client'

import { ResolutionDTO } from '@/types'

interface ResolutionCardProps {
  resolution: ResolutionDTO
  isNew: boolean
}

export default function ResolutionCard({ resolution, isNew }: ResolutionCardProps) {
  const resolved = resolution.resolutionStatus === 'AGENT_RESOLVED'

  return (
    <div className={[
      'rounded-xl border p-5 transition-all',
      resolved
        ? 'bg-emerald-950/20 border-emerald-800/40'
        : 'bg-red-950/20 border-red-800/40',
      isNew ? 'animate-fade-slide-in' : '',
    ].join(' ')}>

      {/* ── Header ── */}
      <div className="flex items-start justify-between mb-4">
        <div className="flex items-center gap-2">
          <span className={`w-2.5 h-2.5 rounded-full shrink-0 ${resolved ? 'bg-emerald-400' : 'bg-red-400 animate-pulse-soft'}`} />
          <span className={`text-sm font-bold tracking-wide ${resolved ? 'text-emerald-400' : 'text-red-400'}`}>
            {resolved ? 'RESOLVED BY AGENT' : 'HUMAN INTERVENTION REQUIRED'}
          </span>
        </div>
        <span className="text-[11px] font-mono text-slate-600">#{resolution.resolutionId}</span>
      </div>

      {/* ── Detail Grid ── */}
      <div className="grid grid-cols-3 gap-3 mb-4">
        <Cell label="Event ID"       value={resolution.eventId} />
        <Cell label="Source"         value={resolution.sourceWarehouse} />
        <Cell label="Transfer Hub"   value={resolved ? resolution.nearestHub : 'ESCALATED'} highlight={resolved} urgent={!resolved} />
        <Cell label="Qty Transferred" value={resolved ? `${resolution.qtyTransferred} units` : '—'} />
        <Cell label="Impact Score"   value={`${resolution.impactScore} orders`} urgent={!resolved} />
        <Cell label="Resolved At"    value={fmtTime(resolution.resolvedAt)} />
      </div>

      {/* ── AI Reasoning ── */}
      <div className={[
        'rounded-lg p-3.5 border',
        resolved ? 'bg-emerald-900/15 border-emerald-800/25' : 'bg-red-900/15 border-red-800/25',
      ].join(' ')}>
        <div className="flex items-center gap-1.5 mb-2">
          {/* bolt icon */}
          <svg className={`w-3.5 h-3.5 shrink-0 ${resolved ? 'text-emerald-400' : 'text-red-400'}`} fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M11.3 1.046A1 1 0 0112 2v5h4a1 1 0 01.82 1.573l-7 10A1 1 0 018 18v-5H4a1 1 0 01-.82-1.573l7-10a1 1 0 011.12-.38z" clipRule="evenodd" />
          </svg>
          <span className={`text-[10px] font-bold uppercase tracking-widest ${resolved ? 'text-emerald-400' : 'text-red-400'}`}>
            AI Reasoning
          </span>
        </div>
        <p className="text-sm text-slate-300 italic leading-relaxed">
          &ldquo;{resolution.justification}&rdquo;
        </p>
      </div>
    </div>
  )
}

function Cell({ label, value, highlight, urgent }: {
  label: string; value: string; highlight?: boolean; urgent?: boolean
}) {
  return (
    <div>
      <div className="text-[10px] text-slate-600 uppercase tracking-wider mb-0.5">{label}</div>
      <div className={`text-xs font-mono font-medium ${
        highlight ? 'text-emerald-300' : urgent ? 'text-red-400' : 'text-slate-300'
      }`}>{value}</div>
    </div>
  )
}

function fmtTime(ts: string) {
  try {
    return new Date(ts).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit' })
  } catch {
    return ts
  }
}

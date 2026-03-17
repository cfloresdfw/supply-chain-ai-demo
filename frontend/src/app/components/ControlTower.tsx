'use client'

import { ResolutionDTO } from '@/types'
import ResolutionCard from './ResolutionCard'

interface ControlTowerProps {
  resolutions: ResolutionDTO[]
}

export default function ControlTower({ resolutions }: ControlTowerProps) {
  return (
    <div className="flex flex-col h-full overflow-hidden">
      <div className="px-6 py-3 border-b border-slate-800 shrink-0 flex items-center justify-between">
        <div>
          <h2 className="text-xs font-semibold text-slate-400 uppercase tracking-widest">Autonomous Resolutions</h2>
          <p className="text-[11px] text-slate-600 mt-0.5">AI Agent Decisions — Temple TX Distribution Network</p>
        </div>
        {resolutions.length > 0 && (
          <div className="flex gap-3">
            <Badge color="emerald" label="Agent Resolved" count={resolutions.filter(r => r.resolutionStatus === 'AGENT_RESOLVED').length} />
            <Badge color="red"    label="Human Required" count={resolutions.filter(r => r.resolutionStatus === 'HUMAN_REQUIRED').length} />
          </div>
        )}
      </div>

      <div className="flex-1 overflow-y-auto p-6 space-y-4">
        {resolutions.length === 0 ? (
          <div className="flex flex-col items-center justify-center h-full text-slate-600 gap-4">
            <div className="w-16 h-16 rounded-full border border-slate-800 flex items-center justify-center">
              <svg className="w-8 h-8 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                  d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div className="text-center">
              <p className="text-sm">No resolutions yet</p>
              <p className="text-xs mt-1 text-slate-700">Agent decisions will appear here automatically</p>
            </div>
          </div>
        ) : (
          resolutions.map((r, i) => (
            <ResolutionCard key={r.resolutionId} resolution={r} isNew={i === 0} />
          ))
        )}
      </div>
    </div>
  )
}

function Badge({ color, label, count }: { color: 'emerald' | 'red'; label: string; count: number }) {
  const styles = color === 'emerald'
    ? 'bg-emerald-900/30 border-emerald-800/50 text-emerald-400'
    : 'bg-red-900/30 border-red-800/50 text-red-400'

  return (
    <div className={`flex items-center gap-1.5 px-2.5 py-1 rounded-full border text-xs font-medium ${styles}`}>
      <span className={`w-1.5 h-1.5 rounded-full ${color === 'emerald' ? 'bg-emerald-400' : 'bg-red-400'}`} />
      {count} {label}
    </div>
  )
}

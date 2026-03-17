'use client'

import { InventoryEvent } from '@/types'

interface LogisticsFeedProps {
  events: InventoryEvent[]
}

export default function LogisticsFeed({ events }: LogisticsFeedProps) {
  return (
    <div className="flex flex-col h-full overflow-hidden">
      <div className="px-4 py-3 border-b border-slate-800 shrink-0">
        <h2 className="text-xs font-semibold text-slate-400 uppercase tracking-widest">Raw Logistics Feed</h2>
        <p className="text-[11px] text-slate-600 mt-0.5">Temple, TX Distribution Center</p>
      </div>

      <div className="flex-1 overflow-y-auto p-3 space-y-2">
        {events.length === 0 ? (
          <div className="flex flex-col items-center justify-center h-full gap-3 text-slate-600">
            <div className="w-7 h-7 border-2 border-slate-700 border-t-mclane-600 rounded-full animate-spin" />
            <span className="text-xs">Awaiting signals…</span>
          </div>
        ) : (
          events.map((event, i) => (
            <EventRow key={event.eventId} event={event} isNew={i === 0} />
          ))
        )}
      </div>
    </div>
  )
}

function EventRow({ event, isNew }: { event: InventoryEvent; isNew: boolean }) {
  const isStockOut = event.status === 'STOCK_OUT'

  return (
    <div className={[
      'rounded-lg p-3 border text-xs transition-all',
      isStockOut
        ? 'bg-red-950/30 border-red-900/50'
        : 'bg-amber-950/30 border-amber-900/50',
      isNew ? 'animate-fade-slide-in' : '',
    ].join(' ')}>
      <div className="flex items-center justify-between mb-1.5">
        <span className={`px-1.5 py-0.5 rounded text-[10px] font-bold ${
          isStockOut ? 'bg-red-900/60 text-red-300' : 'bg-amber-900/60 text-amber-300'
        }`}>
          {event.status}
        </span>
        <span className="font-mono text-slate-600">{event.eventId}</span>
      </div>

      <div className="font-semibold text-slate-200">{event.sku}</div>

      <div className="flex justify-between mt-1.5 text-slate-500">
        <span>{event.warehouseId}</span>
        <span className="text-red-400 font-mono">−{event.qtyGap} units</span>
      </div>

      <div className="text-slate-700 text-[10px] mt-1">{fmtTime(event.timestamp)}</div>
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

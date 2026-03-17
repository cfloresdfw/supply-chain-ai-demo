'use client'

import { useEffect, useState } from 'react'
import { InventoryEvent, ResolutionDTO } from '@/types'

const MAX_ITEMS = 50

export function useSupplyChainSSE() {
  const [events,      setEvents]      = useState<InventoryEvent[]>([])
  const [resolutions, setResolutions] = useState<ResolutionDTO[]>([])
  const [isConnected, setIsConnected] = useState(false)
  const [error,       setError]       = useState<string | null>(null)

  useEffect(() => {
    const es = new EventSource('http://localhost:8080/api/events/stream')

    es.addEventListener('connected', () => {
      setIsConnected(true)
      setError(null)
    })

    es.addEventListener('inventory-event', (e: MessageEvent) => {
      const evt: InventoryEvent = JSON.parse(e.data)
      setEvents(prev => [evt, ...prev].slice(0, MAX_ITEMS))
    })

    es.addEventListener('resolution', (e: MessageEvent) => {
      const res: ResolutionDTO = JSON.parse(e.data)
      setResolutions(prev => [res, ...prev].slice(0, MAX_ITEMS))
    })

    es.onerror = () => {
      setIsConnected(false)
      setError('Connection lost — attempting to reconnect…')
    }

    return () => {
      es.close()
      setIsConnected(false)
    }
  }, [])

  const agentResolved  = resolutions.filter(r => r.resolutionStatus === 'AGENT_RESOLVED').length
  const humanRequired  = resolutions.filter(r => r.resolutionStatus === 'HUMAN_REQUIRED').length

  return { events, resolutions, isConnected, error, agentResolved, humanRequired }
}

import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'Supply Chain Control Tower | AI-Powered Operations',
  description: 'AI-First Supply Chain Management — McLane Distribution Network',
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className="bg-slate-950 text-slate-100 antialiased">
        {children}
      </body>
    </html>
  )
}

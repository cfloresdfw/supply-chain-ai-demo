import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch
from matplotlib.lines import Line2D

fig, ax = plt.subplots(figsize=(18, 8))
fig.patch.set_facecolor('#0d1117')
ax.set_facecolor('#0d1117')
ax.set_xlim(0, 18)
ax.set_ylim(1.4, 10)
ax.axis('off')

# ── helpers ───────────────────────────────────────────────────────────────────

def card(x, y, w, h, face, edge, radius=0.35, lw=2.0, alpha=1.0):
    p = FancyBboxPatch((x, y), w, h,
                       boxstyle=f"round,pad=0,rounding_size={radius}",
                       linewidth=lw, edgecolor=edge, facecolor=face,
                       alpha=alpha, zorder=3)
    ax.add_patch(p)

def txt(x, y, s, color='white', size=10, weight='normal', ha='center', va='center', zorder=5, style='normal'):
    ax.text(x, y, s, color=color, fontsize=size, fontweight=weight,
            ha=ha, va=va, zorder=zorder, style=style,
            fontfamily='sans-serif')

def flow_arrow(x1, y1, x2, y2, label='', color='#64748b', lw=2):
    ax.annotate('',
                xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=lw,
                                mutation_scale=18,
                                connectionstyle='arc3,rad=0.0'),
                zorder=4)
    if label:
        mx, my = (x1+x2)/2, (y1+y2)/2
        dx, dy = x2-x1, y2-y1
        length = (dx**2+dy**2)**0.5
        ox, oy = -dy/length*0.28, dx/length*0.28
        ax.text(mx+ox, my+oy, label, color=color, fontsize=7.5,
                ha='center', va='center', zorder=6, style='italic',
                fontfamily='sans-serif')

# ── colors ───────────────────────────────────────────────────────────────────
NAVY     = '#0f172a'
BLUE     = '#1d4ed8';  BLUE_L   = '#93c5fd'
GREEN    = '#065f46';  GREEN_L  = '#6ee7b7'; GREEN_E = '#10b981'
RED      = '#450a0a';  RED_E    = '#f87171'
AMBER    = '#451a03';  AMBER_E  = '#fbbf24'
PURPLE   = '#2e1065';  PURPLE_E = '#c084fc'
CYAN_E   = '#22d3ee'
SLATE    = '#1e293b';  SLATE_E  = '#475569'

# ═════════════════════════════════════════════════════════════════════════════
# TITLE
# ═════════════════════════════════════════════════════════════════════════════
txt(9, 9.6, 'Supply Chain Control Tower - Service Data Flow',
    color='white', size=15, weight='bold')
txt(9, 9.25, 'DEMO-001  |  AI-First Autonomous Orchestration  |  Temple, TX Distribution Network',
    color='#64748b', size=9)

# ═════════════════════════════════════════════════════════════════════════════
# BACKEND boundary
# ═════════════════════════════════════════════════════════════════════════════
back_box = FancyBboxPatch((3.6, 4.5), 13.0, 4.6,
                          boxstyle="round,pad=0,rounding_size=0.4",
                          linewidth=1, edgecolor='#1e293b',
                          facecolor='#0f172a', alpha=0.5, zorder=1)
ax.add_patch(back_box)
txt(10.1, 9.0, 'Spring Boot 3.2  |  Java 17  |  localhost:8080',
    color='#334155', size=8, weight='bold')

# ═════════════════════════════════════════════════════════════════════════════
# ROW 1  -  Scheduler
# ═════════════════════════════════════════════════════════════════════════════
card(0.4, 7.2, 2.8, 1.3, SLATE, SLATE_E, lw=1.5)
txt(1.8, 7.95, 'Scheduler', color='#94a3b8', size=9.5, weight='bold')
txt(1.8, 7.55, 'every 15 seconds', color='#64748b', size=8.5)

# ═════════════════════════════════════════════════════════════════════════════
# ROW 1  -  Inventory Event Producer
# ═════════════════════════════════════════════════════════════════════════════
card(3.8, 7.0, 3.5, 1.7, AMBER, AMBER_E, lw=2)
txt(5.55, 7.97, 'Inventory Event Producer', color=AMBER_E, size=10, weight='bold')
txt(5.55, 7.58, 'Warehouse:  TEMPLE_TX_DIST', color='#d97706', size=8.5)
txt(5.55, 7.22, 'STOCK_OUT  |  DELAY_DETECTED', color='#78350f', size=8)

flow_arrow(3.2, 7.85, 3.8, 7.85, label='trigger', color=AMBER_E)

# ═════════════════════════════════════════════════════════════════════════════
# ROW 1  -  AI Reasoning Agent
# ═════════════════════════════════════════════════════════════════════════════
card(8.0, 7.0, 3.5, 1.7, PURPLE, PURPLE_E, lw=2)
txt(9.75, 7.97, 'AI Reasoning Agent', color=PURPLE_E, size=10, weight='bold')
txt(9.75, 7.58, 'Tool-use reasoning loop', color='#a855f7', size=8.5)
txt(9.75, 7.22, 'Impact Score = qty_gap x 0.3', color='#6b21a8', size=8)

flow_arrow(7.3, 7.85, 8.0, 7.85, label='InventoryEvent', color=AMBER_E, lw=2)

# ═════════════════════════════════════════════════════════════════════════════
# ROW 1  -  Warehouse Service
# ═════════════════════════════════════════════════════════════════════════════
card(12.2, 7.0, 3.5, 1.7, RED, RED_E, lw=2)
txt(13.95, 7.97, 'Warehouse Service', color=RED_E, size=10, weight='bold')
txt(13.95, 7.58, 'Hub inventory lookup', color='#f87171', size=8.5)
txt(13.95, 7.22, 'Nearest hub by distance (mi)', color='#7f1d1d', size=8)

flow_arrow(11.5, 7.65, 12.2, 7.65, label='findNearestHub(qtyGap)', color=RED_E, lw=1.8)
flow_arrow(12.2, 7.2,  11.5, 7.2,  label='WarehouseStock',         color=RED_E, lw=1.8)

# ═════════════════════════════════════════════════════════════════════════════
# HUB CLUSTER
# ═════════════════════════════════════════════════════════════════════════════
hubs = [
    ('DALLAS',      'Dallas, TX',      ' 62 mi', '2 200 u'),
    ('AUSTIN',      'Austin, TX',      ' 68 mi', '1 800 u'),
    ('SAN ANTONIO', 'San Antonio, TX', '145 mi', '3 500 u'),
    ('HOUSTON',     'Houston, TX',     '183 mi', '5 000 u'),
]
hub_xs = [10.4, 11.85, 13.3, 14.75]
for (name, city, dist, stock), hx in zip(hubs, hub_xs):
    card(hx, 4.9, 1.35, 1.8, '#1a0505', RED_E, lw=1.2, radius=0.25)
    txt(hx+0.675, 6.42, name,  color=RED_E,     size=7.5, weight='bold')
    txt(hx+0.675, 6.12, city,  color='#fca5a5', size=7)
    txt(hx+0.675, 5.82, dist,  color='#94a3b8', size=7)
    txt(hx+0.675, 5.52, stock, color='#94a3b8', size=7)
    txt(hx+0.675, 5.22, 'in stock', color='#475569', size=6.5)

# connector line from Warehouse Service down to hub row
ax.plot([13.95, 13.95], [7.0, 6.72], color=RED_E, lw=1.5, zorder=4, linestyle='--')
ax.plot([11.075, 16.1], [6.72, 6.72], color=RED_E, lw=1.5, zorder=4, linestyle='--')
for hx in hub_xs:
    ax.plot([hx+0.675, hx+0.675], [6.72, 6.7], color=RED_E, lw=1.5, zorder=4)
    ax.annotate('', xy=(hx+0.675, 6.7), xytext=(hx+0.675, 6.72),
                arrowprops=dict(arrowstyle='->', color=RED_E, lw=1.2), zorder=4)

txt(16.5, 6.72, 'Hub Pool', color=RED_E, size=7.5, ha='left', weight='bold')

# ═════════════════════════════════════════════════════════════════════════════
# ROW 2  -  SSE Stream Service
# ═════════════════════════════════════════════════════════════════════════════
card(5.5, 4.6, 4.5, 1.7, '#0c1929', CYAN_E, lw=2)
txt(7.75, 5.57, 'SSE Stream Service', color=CYAN_E, size=10, weight='bold')
txt(7.75, 5.18, 'GET /api/events/stream', color='#67e8f9', size=8.5)
txt(7.75, 4.82, 'inventory-event  |  resolution  |  connected', color='#164e63', size=7.8)

flow_arrow(9.75, 7.0, 7.75, 6.3, label='ResolutionDTO + InventoryEvent', color=CYAN_E, lw=2)

# ═════════════════════════════════════════════════════════════════════════════
# FRONTEND boundary
# ═════════════════════════════════════════════════════════════════════════════
front_box = FancyBboxPatch((3.8, 1.85), 7.9, 4.55,
                           boxstyle="round,pad=0,rounding_size=0.4",
                           linewidth=1, edgecolor='#1e3a5f',
                           facecolor='#0a1628', alpha=0.3, zorder=1)
ax.add_patch(front_box)
txt(7.75, 2.05, 'Next.js Frontend', color='#1e3a5f', size=7.5)

# ═════════════════════════════════════════════════════════════════════════════
# ROW 3  -  Next.js Dashboard
# ═════════════════════════════════════════════════════════════════════════════
card(4.0, 2.2, 7.5, 2.0, '#0a1628', BLUE, lw=2.5)
txt(7.75, 3.85, 'Next.js Control Tower Dashboard', color=BLUE_L, size=11, weight='bold')
txt(7.75, 3.45, 'localhost:3000  |  EventSource  |  React 19  |  Tailwind CSS', color='#3b82f6', size=8.5)

for px, pw, ptitle, psub, pc in [
    (4.15, 2.1, 'Raw Logistics Feed',     'sidebar - event stream',     '#1e3a5f'),
    (6.45, 2.6, 'Autonomous Resolutions', 'AI decision cards',          '#1a3320'),
    (9.25, 2.1, 'AI Reasoning Panel',     'justification + metrics',    '#2e1065'),
]:
    card(px, 2.38, pw, 1.5, pc, '#334155', lw=1, radius=0.2)
    txt(px+pw/2, 2.38+1.08, ptitle, color='#94a3b8', size=8,   weight='bold')
    txt(px+pw/2, 2.38+0.65, psub,   color='#475569', size=7.5)

flow_arrow(7.75, 4.6, 7.75, 4.2, label='real-time SSE push', color=CYAN_E, lw=2.5)

# ═════════════════════════════════════════════════════════════════════════════
# LEGEND
# ═════════════════════════════════════════════════════════════════════════════
lx, ly = 0.3, 5.5
card(lx, ly-3.35, 2.95, 3.55, '#0d1117', '#1e293b', lw=1, radius=0.3)
txt(lx+1.475, ly+0.08, 'LEGEND', color='#475569', size=8.5, weight='bold')
for i, (color, label) in enumerate([
    (AMBER_E,  'Event Producer'),
    (PURPLE_E, 'AI Reasoning Agent'),
    (RED_E,    'Warehouse Service'),
    (CYAN_E,   'SSE Stream'),
    (BLUE_L,   'Frontend Dashboard'),
    (SLATE_E,  'Scheduler'),
]):
    cy = ly - 0.42 - i*0.48
    ax.add_patch(plt.Circle((lx+0.32, cy), 0.12, color=color, zorder=6))
    txt(lx+0.55, cy, label, color='#94a3b8', size=8, ha='left')

# ═════════════════════════════════════════════════════════════════════════════
# RESOLUTION STATUS
# ═════════════════════════════════════════════════════════════════════════════
card(0.3, 1.85, 2.95, 1.55, '#0d1117', '#1e293b', lw=1, radius=0.3)
txt(1.775, 3.22, 'Resolution Status', color='#475569', size=8, weight='bold')
card(0.5,  2.6, 1.05, 0.5, GREEN, GREEN_E, lw=1.5, radius=0.15)
txt(1.025, 2.85, 'AGENT RESOLVED', color=GREEN_L, size=6.5, weight='bold')
card(1.85, 2.6, 1.15, 0.5, RED,   RED_E,   lw=1.5, radius=0.15)
txt(2.425, 2.85, 'HUMAN REQUIRED', color='#fca5a5', size=6.5, weight='bold')
txt(1.775, 2.28, '~80% auto-resolved', color='#475569', size=7, style='italic')

plt.tight_layout(pad=0.3)
out = 'C:/Users/carlo/AI/supply-chain-ai-demo/system-diagram.png'
plt.savefig(out, dpi=150, bbox_inches='tight', facecolor=fig.get_facecolor())
print(f"Saved: {out}")
plt.close()

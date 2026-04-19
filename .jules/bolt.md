## 2025-05-14 - [Broadcaster Optimization]
**Learning:** In Minecraft mods, long-running threads like LAN broadcasters are often implemented with simple loops that allocate objects (Strings, byte arrays, packets) every iteration. While once per second seems small, it's unnecessary GC pressure. Proper thread lifecycle management (volatile, interrupt) is often missing.
**Action:** Always check loop bodies for static data being re-calculated or re-allocated. Ensure threads can be shut down immediately with `interrupt()` instead of waiting for a sleep timer.

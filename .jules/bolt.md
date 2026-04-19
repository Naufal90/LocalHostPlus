## 2025-05-14 - [Performance & UX Optimizations]
**Learning:** Network operations in Minecraft mods must be asynchronous to prevent blocking the game's UI thread. Continuous loops should pre-allocate objects to minimize GC pressure. UI components like server lists need duplicate prevention when discovery mechanisms are active.
**Action:** Always offload HTTP requests to a background thread. Move constant allocations outside of loops. Implement idempotent addition logic in UI lists populated by network events.

# Floatify 🌌

A lightweight, high-performance Paper/Spigot plugin that alters player gravity seamlessly. Allow players to float in mid-air within specific WorldGuard regions or dynamically via structured commands. **Baked fresh by ToastCPS.**

---

## ✨ Features
* **Zero-Gravity Emulation:** Smoothly lift players and let them float effortlessly up to a configurable altitude limit.
* **WorldGuard Integration:** Seamlessly apply the gravity effect inside designated zones using a custom WorldGuard flag (`floatify`). No complex coordinates in configuration files.
* **Granular Permissions:** Clean multi-tier permission system tailored for VIP players, staff members, and senior server administrators.
* **Modern Messaging:** Completely supports MiniMessage styling for beautiful, easily customizable chat and action-bar feedback.
* **Human-Written & Optimized Code:** Lightweight, highly performant footprint, explicitly designed without unnecessary abstraction or redundant logic.

---

## 🛠️ Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/floatify on [player]` | Activates the anti-gravity float effect. | `floatify.use` / `floatify.others` |
| `/floatify off [player]` | Restores standard Minecraft gravity physics. | `floatify.use` / `floatify.others` |
| `/floatify reload` | Reloads the configuration file safely. | `floatify.admin` |

---

## 📦 WorldGuard Setup
To bound the float effect to a specific area in your server, select your zone with WorldEdit (`//wand`) and execute the following commands:

/rg define lobby-gravity
/rg flag lobby-gravity floatify allow
/rg setpriority lobby-gravity 10

---

## 🚀 Compilation & Installation
This project uses Maven for automated dependencies and build management.

1. Clone the repository into your workspace.
2. Build the production jar executing the following command in your terminal:
   mvn clean package
3. Locate the compiled floatify-1.0.0.jar inside the target/ directory.
4. Drag and drop the jar file into your Paper/Spigot server's plugins/ folder and restart the server.

---

## 📝 Configuration (config.yml)

# Floatify Configuration
# Crafted with care by ToastCPS

levitation-amplifier: 0
max-height: 120

messages:
  prefix: "<gradient:#55cdfc:#f7a8b8><b>[Floatify]</b></gradient> "
  enabled: "<gray>Gravity disabled. You are now floating!</gray>"
  disabled: "<gray>Gravity restored. Welcome back to earth.</gray>"
  no-permission: "<red>You do not have permission to execute this command.</red>"
  reload: "<green>Configuration reloaded successfully.</green>"

---
Developed by ToastCPS © 2026. All rights reserved.

# HUD Add Menu — Minecraft Java 26.1.2 (Fabric)

Mod cliente con un panel visual inspirado en la imagen que compartiste.

## Cómo abrirlo
1. Instala Fabric Loader para Minecraft Java **26.1.2** y usa Java 25.
2. Copia `hudmenu-26.1.2.jar` a `%appdata%\.minecraft\mods`.
3. Entra al mundo o servidor y abre el chat con `T`.
4. Haz clic derecho para abrir el panel. Para volver al chat, pulsa `Esc` o haz clic derecho otra vez.

## Qué incluye esta primera versión
Aparece el panel oscuro con acentos rojos, los 18 nombres, los iconos pixelados y el resaltado al pasar el cursor. Al hacer clic izquierdo sobre una opción, se queda resaltada. Por ahora los nombres son visuales: todavía no crean ni configuran widgets reales de coordenadas, armadura, arraylist, etc.

## Código fuente
El proyecto fuente está configurado para Fabric 26.1.2. Para compilarlo desde el código, abre la carpeta en IntelliJ IDEA 2025.3 o posterior con JDK 25, sincroniza Gradle y ejecuta `build`. El `.jar` generado por Gradle quedará en `build/libs/`.

**Corrección de la versión:** se corrigió el empaquetado de las anotaciones de Mixin que provocaba el error `ChatScreenMixin is missing an @Mixin annotation`. Descarga el JAR actualizado y reemplaza/elimina el anterior antes de probar.

**Importante:** este entorno solo dispone de Java 21 y no puede descargar las dependencias de Minecraft/Fabric, así que no pude ejecutar una compilación oficial ni abrir el juego para probarlo. El JAR corregido se inspeccionó internamente, pero todavía no se ha validado dentro de un cliente real. Pruébalo primero en una instalación/perfil separado.

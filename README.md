# Pokémon VRF

## 📋 Introducción
**Pokémon VRF** es una aplicación móvil diseñada para ofrecer una experiencia inmersiva en la gestión de Pokémon. Los usuarios pueden explorar la Pokédex, capturar Pokémon y gestionar su colección personal. Además, incluye opciones de personalización y ajustes avanzados para mejorar la experiencia de usuario.

---

## 🌟 Características Principales

### 🔍 **Pokédex**
- Consulta información de los primeros 150 Pokémon.
- Visualiza sus nombres, imágenes, tipo, peso y altura.
- Carga dinámica de datos desde [PokeAPI](https://pokeapi.co/).

### 🎯 **Captura de Pokémon**
- Añade Pokémon a tu colección personal desde la Pokédex.
- Los datos capturados incluyen:
  - Nombre.
  - Índice en la Pokédex.
  - Tipo(s).
  - Altura y peso.
  - Foto.

### 📂 **Gestión de Pokémon Capturados**
- Lista dinámica con detalles completos de cada Pokémon capturado.
- Posibilidad de eliminar Pokémon capturados (ajustable en configuración).

### ⚙️ **Pantalla de Ajustes**
- Cambio de idioma entre **Español** e **Inglés**.
- Opción para habilitar o deshabilitar la eliminación de Pokémon.
- Información de la aplicación:
  - **Desarrollador:** Verónica Romero Fernández.
  - **Versión:** 1.0.0.
- Cierre de sesión seguro.

---

## 🛠️ Tecnologías Utilizadas

- **Java:** Lenguaje principal de desarrollo.
- **Firebase:**
  - **Authentication:** Gestión de usuarios para autenticación.
  - **Firestore:** Almacenamiento de datos de Pokémon capturados.
- **Retrofit:** Consumo de APIs REST para obtener datos de la Pokédex.
- **Glide:** Carga eficiente de imágenes.
- **RecyclerView:** Listado eficiente de Pokémon en la Pokédex y Pokémon capturados.
- **CardView:** Elementos visuales para cada Pokémon.
- **SharedPreferences:** Persistencia de configuraciones del usuario.

---

## 🖥️ Instrucciones de Uso

### Paso 1: Clonar el Repositorio
```bash
git clone https://github.com/veronicarf/pokemon_vrf.git
cd pokemon_vrf

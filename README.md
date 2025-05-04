<h1 align="center">🏨 Velyo</h1> 

<p align="center">Velyo es una plataforma web para la reserva de alojamientos, diseñada para ofrecer una experiencia simple, rápida e intuitiva. Permite a los usuarios buscar hospedajes por categoría y destino, ver detalles, hacer reservas y gestionar sus favoritos.</p>


---

## 🌐 Caracteristicas principales
- **Búsqueda de alojamientos**: Encontra alojamientos por categoría, destino y fechas del viaje.
- **Reserva de alojamientos**: Realiza reservas de forma rápida y sencilla, además vas a poder visualizar tus reservas.
- **Alojamientos favoritos**: Marca alojamientos como favoritos y volvé a verlos más tarde.
- **Reseñas de usuarios**: Vas a poder dejar tu opinión sobre los alojamientos que hayas visitado y contar tu experiencia.


# Antes de arrancar


## Prerequisitos
### Frontend 
- **Node.js**(Version 16 o superior como recomendacion)
  ```bash
  # Comprueba si tienes Node.js instalado
  node -v

  # Si no lo tenes intalado descargalo desde el sitio oficial:
  # https://nodejs.org/
  ```
- **NPM**(incluido con Node.js) o **Yarn**
  ```bash
  # Actualiza NPM a la última versión
  npm install -g npm@latest

  # O instala Yarn si prefieres usarlo
  npm install -g yarn
  ```
### Backend
- **JAVA** (Version 11 o superior)
```bash
# Actualiza NPM a la última versión
npm install -g npm@latest

# O instala Yarn si prefieres usarlo
npm install -g yarn
```
- **Maven**
```bash
# Verifica si tienes Maven instalado
mvn -v

# Descárgalo desde https://maven.apache.org/ si no está instala
```

## 🧪 Instalación local

### Clonar el repositorio

```bash
# Clona el repositorio
git clone https://github.com/maticarrera12/velyo-professional-developer.git
```
### Frontend
1. Dirigite al directorio del frontend:
```bash
cd velyo-frontend
```
2. Instala las dependencias:
```bash
npm install
# O si usas Yarn
yarn install
```
3. Inicia la aplicacion:
```bash
npm run dev
# O con Yarn
yarn run dev
```
### Backend
1. Dirigite al directorio del backend:
```bash
# Si estas en la carpteta del frontend
cd ..
cd velyo-backend
```
2. Compila el proyecto con maven:
```bash
mvn clean install
```
3. Inicia el servidor
```
mvn spring-boot:run
# Tambien podes hacerlo con shift+f10 o correr la aplicacion manualmente
```

## Sobre la base de datos
- La aplicacion esta configurada para inicializar datos automaticamente al iniciar el servidor

# Notas adicionales
- Asegurate que el backend este configurado para conectar con una base de datos activa.
- Si necesitas configurar credenciales o URLs personalizadas, edita el archivo `application.properties` en el backend.
- La aplicación cuenta con una base de datos con datos `src/main/resources/data.sql`.
- Usuario predefinidos
  - 👤 **Administrador**
    - Email:`admin@gmail.com`
    - Contraseña: `admin123`
  - 👤 **Usuario registrado**
    - Email: `carreramatias12@gmail.com`
    - Contraseña: `12345678`
- Vas a neccesitar un archivo .env en la carpeta resources o en la raiz del backend, con el siguiente formato
```bash
DATABASE_USER=tu_usuario
DATABASE_PASSWORD=tu_contraseña
DATABASE_URL=jdbc:mysql://localhost:3306/velyo
MAIL_USERNAME=tu_mail@gmail.com
MAIL_PASSWORD=contraseña_app
SECRET_KEY=clave_secreta_para_jwt
```

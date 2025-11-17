# ing-sw-app-moviles
Repositorio de equipo para el desarrollo del proyecto de aplicación móviles

## 📱 Descargar APK

**APK Release 2**: [Descargar Vinilos.apk](./app/release/app-release.apk)

## 🛠️ Construcción Local

### Prerrequisitos

#### Software Requerido
- **Android Studio**: Arctic Fox (2020.3.1) o superior
- **JDK**: Java 11 o superior
- **Android SDK**: API Level 24 (Android 7.0) mínimo, API Level 35 recomendado
- **Git**: Para clonar el repositorio

#### Configuración del SDK
- **compileSdk**: 36
- **minSdk**: 24
- **targetSdk**: 35

### Pasos de Instalación

#### 1. Clonar el Repositorio
```bash
git clone https://github.com/[usuario]/ing-sw-app-moviles.git
cd ing-sw-app-moviles
```

#### 2. Abrir en Android Studio
1. Abrir Android Studio
2. Seleccionar "Open an existing Android Studio project"
3. Navegar y seleccionar la carpeta `ing-sw-app-moviles`
4. Esperar a que Gradle sincronice automáticamente

#### 3. Configurar SDK (si es necesario)
1. Ir a `File > Project Structure > SDK Location`
2. Verificar que Android SDK esté configurado
3. Instalar SDK Platform 35 si no está disponible

#### 4. Sincronizar Dependencias
```bash
# En terminal de Android Studio o línea de comandos
./gradlew build
```

#### 5. Ejecutar la Aplicación

**Opción A: Desde Android Studio**
1. Conectar dispositivo Android o iniciar emulador
2. Hacer clic en el botón "Run" (▶️) o presionar `Shift + F10`

**Opción B: Línea de Comandos**
```bash
# Instalar en dispositivo conectado
./gradlew installDebug

# Generar APK debug
./gradlew assembleDebug
# APK generado en: app/build/outputs/apk/debug/

# Generar APK release
./gradlew assembleRelease
# APK generado en: app/build/outputs/apk/release/
```

### Dependencias Principales

```kotlin
// UI y Navigation
implementation "androidx.navigation:navigation-fragment-ktx:2.7.5"
implementation "androidx.navigation:navigation-ui-ktx:2.7.5"
implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

// Database (Room)
implementation "androidx.room:room-runtime:2.5.0"
implementation "androidx.room:room-ktx:2.5.0"
kapt "androidx.room:room-compiler:2.5.0"

// Network
implementation "com.android.volley:volley:1.2.1"

// Image Loading
implementation "com.github.bumptech.glide:glide:4.16.0"

// Testing
testImplementation "junit:junit:4.13.2"
testImplementation "io.mockk:mockk:1.13.8"
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
```

### Configuración de Red

#### Backend API
- **URL Base**: `https://vinilos-backend-5f9h.onrender.com/`
- **Endpoints**:
  - `GET /albums` - Lista de álbumes
  - `GET /collectors` - Lista de coleccionistas

#### Permisos de Red
La aplicación requiere permisos de internet (ya configurados en AndroidManifest.xml):
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Estructura del Proyecto

```
app/
├── src/main/java/com/example/vinilos/
│   ├── ui/                    # Fragmentos y Activities
│   ├── viemodels/            # ViewModels
│   ├── repository/           # Repositorios
│   ├── database/             # Room Database y DAOs
│   ├── network/              # NetworkServiceAdapter
│   └── models/               # Data classes
├── src/test/                 # Unit Tests
├── src/androidTest/          # Integration Tests
└── build.gradle.kts          # Configuración de dependencias
```


### Testing

#### Ejecutar Tests Unitarios
```bash
./gradlew test
```

#### Ejecutar Tests de Instrumentación
```bash
./gradlew connectedAndroidTest
```

#### Coverage Report
```bash
./gradlew jacocoTestReport
# Reporte en: app/build/reports/jacoco/
```

### Características de la Aplicación

- ✅ **Offline-First**: Funciona sin conexión usando cache local
- ✅ **Pull-to-Refresh**: Actualización manual de datos
- ✅ **MVVM Architecture**: Separación clara de responsabilidades
- ✅ **Room Database**: Persistencia local
- ✅ **Navigation Component**: Navegación entre pantallas
- ✅ **Data Binding**: Binding automático de datos en UI
- ✅ **Error Handling**: Manejo de errores de red
- ✅ **Unit Testing**: Tests unitarios con MockK
- ✅ **Integration Testing**: Tests de UI con Espresso


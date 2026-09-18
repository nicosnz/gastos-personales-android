# 💰 Gastos

> Aplicación Android para registrar, consultar y gestionar movimientos financieros personales.

Construida con **Kotlin** y **Jetpack Compose**, la aplicación permite consultar el saldo disponible, registrar ingresos y gastos, y visualizar el historial de movimientos desde una interfaz moderna y sencilla.

<p align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge\&logo=kotlin\&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge\&logo=android\&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge\&logo=jetpackcompose\&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-757575?style=for-the-badge\&logo=materialdesign\&logoColor=white)

</p>

---

## 📑 Table of Contents

* [✨ Features](#-features)

    * [💵 Balance](#-balance)
    * [➕ Transactions](#-transactions)
    * [📋 Transaction History](#-transaction-history)
* [🛠️ Tech Stack](#️-tech-stack)
* [🏗️ Project Configuration](#️-project-configuration)
* [🧭 Navigation](#-navigation)
* [🎨 UI](#-ui)

---

## ✨ Features

### 💵 Balance

Visualización del estado financiero actual de la aplicación.

* Saldo total disponible.
* Cálculo basado en los movimientos registrados.
* Información presentada de forma clara y resumida.

### ➕ Transactions

Registro de nuevos movimientos financieros.

* Ingresos.
* Gastos.
* Monto.
* Descripción.
* Información asociada al movimiento.

Los movimientos registrados se incorporan automáticamente al historial y afectan el saldo total.

### 📋 Transaction History

Consulta de todos los movimientos registrados.

* Visualización de ingresos y gastos.
* Monto de cada operación.
* Información asociada a cada movimiento.
* Historial organizado para facilitar la consulta.

---

## 🛠️ Tech Stack

| Tecnología                | Uso                             |
| ------------------------- | ------------------------------- |
| **Kotlin**                | Lenguaje principal              |
| **Jetpack Compose**       | UI declarativa                  |
| **Material 3**            | Sistema de diseño y componentes |
| **Navigation Compose**    | Navegación entre pantallas      |
| **AndroidX Core KTX**     | APIs y extensiones de Android   |
| **Gradle Kotlin DSL**     | Build y configuración           |

---

## 🏗️ Project Configuration

```text
Application ID    com.example.gastos
Compile SDK       37
Target SDK        37
Minimum SDK       24
Java              11
Navigation        2.8.0
Version           1.0
```

---

## 🧭 Navigation

La navegación de la aplicación está implementada utilizando **Navigation Compose 2.8.0**.

```text
                         App
                          │
             ┌────────────┼────────────┐
             │            │            │
             ▼            ▼            ▼
        ┌─────────┐  ┌───────────┐  ┌────────────┐
        │ Balance │  │ Transaction│  │  History   │
        │         │  │   Form     │  │            │
        └─────────┘  └───────────┘  └────────────┘
```

La navegación se gestiona mediante `NavController` y `NavHost`.

---

## 🎨 UI

La interfaz está desarrollada completamente con **Jetpack Compose** y utiliza componentes de **Material 3**.

La construcción de las pantallas se realiza mediante componentes declarativos:

```kotlin
Column {
    Text("Saldo total")

    Text("$ 1.250")

    Button(
        onClick = {
            // Acción
        }
    ) {
        Text("Registrar movimiento")
    }
}
```

Componentes principales utilizados:

* `Column`
* `Row`
* `Box`
* `Text`
* `Button`
* `Card`
* `TextField`
* Material 3 Components

---

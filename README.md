 # Kioskotec
   Plataforma de pedidos y compras online
   
**Curso CS2031** Desarrollo Basado en Plataforma

### Integrantes del equipo:  
- Paucar Barrios Miguel Luis
- Muñoz Portugal, Romina Valeria
- Ortega Huaman, Jhonatan Eder
- Leonardo Gabriel Sanchez Terrazos
- Joel Modesto Cayllahua Hilario

## Índice
1. [Introducción](#introducción)  
2. [Identificación del Problema o Necesidad](#identificación-del-problema-o-necesidad)  
3. [Descripción de la Solución](#descripción-de-la-solución)  
4. [Modelo de Entidades](#modelo-de-entidades)  
5. [Testing y Manejo de Errores](#testing-y-manejo-de-errores)  
6. [Medidas de Seguridad Implementadas](#medidas-de-seguridad-implementadas)  
7. [Eventos y Asincronía](#eventos-y-asincronía)  
8. [GitHub](#github)  
9. [Conclusión](#conclusión)  
10. [Apéndices](#apéndices)

---

## Introducción
### Contexto
Dentro del contexto de las tiendas, kiosko y minimercados, se presenta una alta demanda de diversos productos durante horarios específicos en el día. Muchos de estos negocios, mayormente ubicados en zonas urbanas, no siempre presentan un fácil acceso para todos los clientes, ya sea por la ubicación, horarios limitados o falta de medios digitales.
Debido a estas exigencias, se requiere de una alternativa accesible al público, que permita un fácil acceso a diferentes productos de consumo de forma óptima y organizada. Dónde se garantice una eficiente gestión en los pedidos de compra y venta de productos, permitiendo un servicio funcional y accesible.
Es en este contexto, que se plantea el desarrollo de un sistema digital que registre la venta y compra de los pedidos de productos.

### Objetivos del Proyecto
Como objetivo principal se plantea la elaboración del backend de una plataforma digital que permita la gestión óptima del negocio. Para lograrlo se necesitará de los siguientes objetivos clave:
- Fácil acceso al pedido y compra de productos 
- Gestionar productos de forma estructurada; permitir un registro personalizable.
- Implementación de un sistema de pedidos, que permita al usuario registrar y gestionarlos, además de visualizar su estado de entrega.
- Garantizar seguridad, incorporar autenticación de usuario y funciones de registro para garantizar la seguridad a los usuarios.
- Implementar funcionalidad de reseñas, permitir a los usuarios calificar su satisfacción con los productos pedidos mediante reseñas.
- Incorporar funcionalidades de promociones, permitir a los usuarios adquirir descuentos mediante promociones de productos.
- Añadir a un carrito de compras virtual todos los productos pedidos por el usuario, el usuario pone en un listado carrito de compra todos los productos que piensa comprar, teniendo la opción de añadir más artículos o eliminar alguno.

---

## Identificación del Problema o Necesidad

### Descripción del Problema
Muchos negocios carecen de una gestión accesible con poco o nulo apoyo de medios tecnológicos. Esta carencia da como consecuencia a dificultades a los clientes para realizar pedidos, registrar sus compras y ventas y ofrecerles un servicio óptimo y funcional, no permiten un funcionamiento fluido accesible a todos los clientes. 
### Justificación
La implementación de un servicio de negocio digital puede permitir un mayor acceso y alcance a todos los productos que exigen los clientes, optimizando el proceso de compra para los clientes, las operaciones de venta para los vendedores, el manejo de las entregas que realice el repartidor y el manejo operativo del sistema por los administradores.

---

## Descripción de la Solución
El sistema desarrollado proporciona un backend para la gestión de los productos, pedidos, promociones, reseñas y usuarios. Entre sus funcionalidades se encuentra:
- **Gestión de productos:** Registro, edición y eliminación de diferentes productos, con los atributos como ID, nombre, fecha de vencimiento, descripción, precio, stock, categoría, ID del pedido, peso e indicamiento si es local o no.
- **Funcionalidad de carrito de compras:** Donde los usuarios podrán añadir y eliminar productos que vayan deseando pedir para luego proceder a su pedido y compra.
- **Sistema de pedidos:** Creación de diversos pedidos con múltiples productos, donde se pueda visualizar el historial de pedidos y su estado actual.
- **Reseñas:** Los usuarios podrán calificar a través de reseñas su satisfacción con el producto, lo que permitirá una retroalimentación para los demás compradores.
- **Promociones:** Se podrá acceder a diferentes promociones que incluyen descuentos de un producto o un conjunto de productos seleccionados a preferencia del cliente.
### Funcionalidades Implementadas

### Tecnologías Utilizadas
A lo largo del desarrollo del backend se hizo uso del programa informático IntelliJ IDEA. Dentro de dicho programa informático se utilizó:
- **Lenguaje de programación:** Java.
- **Framework principal:** Spring Boot.
- **Gestión de dependencias:** Maven.
- **Persistencia:** Spring Data JPA.
- **Base de datos:** PostgreSQL.
- **Seguridad:** Spring Security.
- **Pruebas endpoint:** Postman Collections.

---

## Modelo de Entidades

### Diagrama de Entidades
![e0921e99-f947-48cb-98d6-9a61b3bed977](https://github.com/user-attachments/assets/e5ebb8ec-da48-48d2-802a-2cc626497fa7)
### Descripción de Entidades
- **Local:** Ubicación donde provienen los productos. Tiene como atributos: id, sede, direccion, telefono, horario, productos, encargado.
- **Usuario:** Usuario del sistema. Tiene como atributos: id, nombre, correo, telefono, direccion, rol, contrasena, fechaRegistro y local
- **Carrito:** Contiene todos los productos añadidos por un usuario. Tiene como atributos: id, usuario, productos y promociones.
- **Producto:** Representa un articulo en venta. Tiene como atributos: id, nombre, descripcion, precio, stock, peso, categoria, fechaVencimiento, local, promociones.
- **Pedido:** Representa una orden de venta realizada por un cliente. Tiene como atributos: id, usuario, productos, estado, direccionEntrega, total, fechaPedido y fechaEntregaEstimada.
- **Promocion:** Representa los descuentos a productos en especifico. Tiene como atributos: id, nombre, descripcion, codigoPromocion, productos, precioOriginal, precioFinal, porcentajeDescuento, fechaInicio y fechaFin.
- **Resena:** Permite a los usuarios calificar productos. Tiene como atributos: id, producto, usuario, calificacion, comentario y fecha.

---

## Testing y Manejo de Errores

### Niveles de Testing Realizados
Durante el desarrollo del backend se aplicaron diferentes pruebas con el objetivo de validar el funcionamiento del sistema, estas pruebas se implementaron para las entidades principales del sistema.
Se organizaron en carpetas cada test correspondiente de las siguientes entidades:
- **CarritoTest**: Para carrito se implementó los siguientes tests: CarritoControllerTest, CarritoServiceTest, CarritoRepositoryTest, CarritoProductoTest, CarritoPromocionTest, CarritoProductoRepositoryTest, CarritoPromocionRepositoryTest y CarritoNotFoundExceptionTest.
- **LocalTest:** Para local se implementó los siguientes tests: LocalControllerTest, LocalServiceTest, LocalRepositoryTest y LocalMapperTest.
- **PedidoTest:** Para pedido se implementó los siguientes test: PedidoControllerTest, PedidoServiceTest, PedidoRepositoryTest y PedidoMapperTest
- **ProductoTest:** Para producto se implementó los siguientes tests: ProductoControllerTest, ProductoServiceTest, ProductoRepositoryTest y ProductoMapperTest
- **PromocionTest:** Para promoción se implementó los siguientes tests: PromocionControllerTest, PromocionServiceTest, PromocionServiceImplemTest y PromocionRepositoryTest.
- **UsuarioTest:** Para usuario se implementó los siguientes tests: UsuarioControllerTest, UsuarioServiceTest, UsuarioRepositoryTest, UsuarioTest y UsuarioMapperTest.
- **Email:** Para email se implementó los siguientes test: EmailServiceTest.
- **Resena:** Para resena se implementó los siguientes tets: ResenaControllerTest, ResenaRepositoryTest, ResenaServiceTest.

### Resultados

### Manejo de Errores

📧 1. EmailController
Errores manejados:

Datos incompletos o inválidos en la solicitud del usuario.

Casos:

Campos to, subject o message vacíos o nulos.

Tipo de error retornado:

400 Bad Request

Respuesta al usuario:

Mensaje indicando qué campos son inválidos o faltantes (mediante validaciones del DTO).

📧 2. EmailService
Errores manejados:

Fallos al enviar el correo electrónico.

Casos:

Problemas de configuración del servidor de correo.

Dirección de correo inválida que no puede ser procesada.

Tipo de error retornado:

500 Internal Server Error

Respuesta al usuario:

Mensaje genérico indicando que ocurrió un error al enviar el correo.

🛒 3. CarritoService
Errores manejados:

Producto no existente al intentar agregarlo.

Cantidad no válida (cero o negativa).

Promoción inválida o no existente.

Casos:

El ID del producto o promoción no corresponde a un registro existente.

El usuario intenta agregar un producto con cantidad cero.

Tipo de error retornado:

404 Not Found (cuando la entidad no existe).

400 Bad Request (cuando los datos son inválidos).

Respuesta al usuario:

Mensajes claros indicando qué entidad no fue encontrada o qué dato fue inválido.

🧾 4. PedidoService
Errores manejados:

Intento de generar pedido con carrito vacío.

Usuario no existente o inactivo.

Casos:

El usuario intenta confirmar un pedido sin productos.

El usuario no está registrado o no tiene permisos.

Tipo de error retornado:

400 Bad Request

404 Not Found

Respuesta al usuario:

Explicación clara de por qué el pedido no puede generarse (ej. "El carrito está vacío").

📍 5. LocalService
Errores manejados:

Local no encontrado.

Datos inválidos para registrar o actualizar un local.

Casos:

Se consulta un local por ID y no existe.

Se intenta registrar un local con campos vacíos.

Tipo de error retornado:

404 Not Found

400 Bad Request

Respuesta al usuario:

Se informa qué campo falló o qué local no se encontró.

🧠 6. ChatService (Asistente virtual con Azure OpenAI)
Errores manejados:

Preguntas vacías o sin sentido.

Error al obtener respuesta de la API externa de OpenAI.

Casos:

El usuario envía una cadena vacía o nula.

Fallo de conexión con el servicio de OpenAI.

Tipo de error retornado:

400 Bad Request

500 Internal Server Error

Respuesta al usuario:

Indicación de que la pregunta no es válida o que ocurrió un error interno al procesarla.

🎁 7. ProductoService
Errores manejados:

Producto no encontrado.

Producto desactivado.

Stock insuficiente.

Casos:

Se consulta un ID inexistente.

Se intenta acceder a un producto que ha sido deshabilitado.

Se intenta comprar más unidades de las disponibles.

Tipo de error retornado:

404 Not Found

400 Bad Request

Respuesta al usuario:

Mensajes que informan que el producto no existe, está inactivo o no hay suficiente stock.

🏷️ 8. PromocionService
Errores manejados:

Promoción no encontrada.

Producto no encontrado.

Casos:

Se consulta una promoción con un ID inexistente.

Se intenta actualizar una promoción que no existe.

Se intenta asociar un producto inexistente a una promoción.

Se intenta eliminar una promoción que no existe.

Tipo de error retornado:

404 Not Found

400 Bad Request

Respuesta al usuario:
Mensajes que informan que la promoción no existe o que uno de los productos asociados no fue encontrado.

📝 9. ResenaService
Errores manejados:

Reseña no encontrada.

Producto o usuario no encontrado.

Reseña duplicada.

Usuario sin permiso para modificar o eliminar una reseña.

Casos:

Se intenta crear una reseña para un producto que ya fue reseñado por el mismo usuario.

Se intenta actualizar o eliminar una reseña que no existe.

Se intenta modificar o borrar una reseña que no pertenece al usuario.

Se intenta crear, editar o eliminar una reseña con producto o usuario inexistente.

Tipo de error retornado:

400 Bad Request

403 Forbidden

404 Not Found

Respuesta al usuario:
Mensajes que informan que ya se dejó una reseña, que el recurso no existe o que no tiene permiso para modificar o eliminar la reseña.
---

## Medidas de Seguridad Implementadas

### Seguridad de Datos
Para garantizar seguridad dentro de la plataforma se implementaron las siguientes medidas:
- **Autenticación de usuarios:** Se implementó un sistema de registro y login, donde cada usuario ingresa credenciales válidas para acceder al sistema.
- **Asignación de roles y permisos:** Se declararon diferentes roles para cada usuario para diferenciar y establecer diferentes permisos según su rol.

### Prevención de Vulnerabilidades
Durante el desarrollo backend se implementaron las siguientes prevenciones de vulnerabilidades:
- **Validación datos entrada:** Los datos enviados por los usuarios son validados mediante DTOs y entidades definidas. De esta forma se evita el ingreso de datos erróneos o posibles ataques.
- **Errores personalizados:** Si ocurre un error como una búsqueda errónea, el propio sistema responderá con un mensaje personalizado acorde al error  ocurrido.
- **Protección de ataques de inyección SQL:** Mediante Spring Data JPA, las consultas de la base de datos se construyen utilizando un ORM seguro, de esta forma se evitan posibles ataques mediante inyección de SQL.

---

## Eventos y Asincronía

---

## GitHub

### Uso de GitHub Projects
Mediante GitHub projects se crearon diferentes issues donde cada uno de nuestros integrantes desarrolló una parte del sistema backend, usando conventional commits y mediante se iban resolviendo cada tarea de los issues, se reportaban como pendientes, en curso y hechos. De esta forma se logró una buena organización con el equipo, al igual que un desarrollo ordenado y estructurado.

---

## Conclusión

### Logros del Proyecto

### Aprendizajes Clave

### Trabajo Futuro

---

## Apéndices

### Licencia

### Referencias

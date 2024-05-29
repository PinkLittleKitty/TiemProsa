# TiemProsa - Widget de Reloj Literario para Android


# Esta es una traducción a español y continuación de desarrollo de [ProseZeit](https://github.com/sgreg/ProseZeit), por sgreg.


Inspirado por (es decir, completamente copiado de) [tjaap](https://www.instructables.com/member/tjaap/) y su [Reloj Literario hecho con un Kindle](https://www.instructables.com/id/Literary-Clock-Made-From-E-reader/) (y [obvia y descarada promoción](https://hackaday.com/2018/08/01/kindle-tells-the-time-by-quoting-literature/)), *TiemProsa* es un widget para Android (los que puedes poner en tu pantalla de inicio) que hace exactamente lo mismo: mostrar la hora actual citando literatura.

<img alt='13:00' src='images/1300.jpg' width=400 />

Y al igual que el original, la opción de trivia se añade al omitir el origen de la cita, revelando el título del libro y el autor solo al hacer clic en el widget.

<img alt='13:00 con origen' src='images/1300_revealed.jpg' width=400 />

Gran crédito, por supuesto, a tjaap por proporcionar públicamente su colección de citas en un archivo CSV, que sirve como fuente principal para este proyecto.

## Obteniendo las citas en la aplicación

La solución más simple parecía convertir el archivo CSV en una base de datos SQLite que se pueda enviar con la aplicación del widget de Android. Un simple script en Python (como, realmente simple, en el sentido de "esto es lo mínimo necesario") se encarga de eso, y se puede encontrar en el directorio `tools/`, junto con el archivo CSV como entrada. Me tomé la libertad de corregir algunas inconsistencias y problemas cosméticos en el archivo CSV original de tjaap: la sensibilidad a mayúsculas y minúsculas en el patrón y la cita no coincidían en algunos casos, y había algunos espacios adicionales al final de la cita, el autor o el título del libro.

Esto es más bien un aviso, si solo quieres compilar y ejecutar la aplicación, no es necesario usar ese script, ya hay un archivo de base de datos SQLite convertido en la carpeta de assets de la aplicación. Pero en caso de que no te guste el resaltado de la hora actual, puedes ajustar eso en el script de Python y volver a generar la base de datos SQLite (y copiarla al directorio de assets de la aplicación).

<img alt='01:27' src='images/0127.jpg' width=400 />

## Compilar y ejecutar la aplicación

El código de Android es un proyecto normal de Android Studio y se puede importar allí en consecuencia.

Ten en cuenta que puedes recibir un error de "Actividad predeterminada no encontrada" al intentar ejecutar la aplicación desde Android Studio; edita la configuración de ejecución (*Run* -> *Edit Configurations...*) y cambia las *Opciones de inicio* de "Actividad predeterminada" a "Nada".

Dado que TiemProsa es solo un widget, no se instalará ninguna aplicación real. Para abrir el widget, mantén presionada la pantalla de inicio, lo que debería abrir un menú para seleccionar "Widgets". Desplázate por la lista de widgets disponibles hasta encontrar TiemProsa. Arrástralo a tu pantalla de inicio, y tadaa, sabes la hora.

<img alt='captura de pantalla' src='images/screenshot.jpg' width=540 />

## Solo ejecutando la aplicación

<a href=''><img alt='Consíguelo en Google Play' src='images/google-play-badge.png' width=200/></a>

El widget está disponible para pruebas beta abiertas en la tienda Google Play.

Aunque no tengo ninguna intención real de convertir este proyecto en un widget de reloj serio y completamente funcional (ver *Problemas conocidos* a continuación), pensé que aún podría ser útil hacerlo fácilmente accesible, por si alguien tiene curiosidad. Quiero decir, claro, puedes construirlo e instalarlo tú mismo con Android Studio, pero eso no es del gusto de todos.

### Únete a las pruebas beta abiertas
Aproveché esta oportunidad para echar un vistazo a las pruebas beta abiertas. No estoy completamente seguro de si tiene mucho sentido, considerando que actualmente no planeo ningún desarrollo adicional. Pero supongo que permite dejar comentarios o no sé. Bueno, [siéntete libre de unirte]() por diversión ¯\\\_(ツ)\_/¯

<img alt='23:56' src='images/2356.jpg' width=400 />

# Problemas conocidos

Considera este proyecto como una prueba de concepto en lugar de una aplicación seriamente bien desarrollada. Hasta ahora, solo se ha probado con el emulador de Android Studio ejecutando Android 6.0 (probablemente debería actualizar eso) y con un dispositivo real con Android 8.0. Más o menos funcionó con algunos inconvenientes.

* **El widget solo muestra "No se pudo agregar el widget" después de agregarlo a una pantalla de inicio**
  * De alguna manera relacionado con el listener de clic en el widget; al menos eliminar la llamada a `setOnClickPendingIntent()` hizo que funcionara para mí™.
  * Eliminar el widget nuevamente de la pantalla de inicio y agregarlo nuevamente puede "arreglarlo" (solo elimina el widget, no toda la aplicación)
* **La actualización de la hora no es precisa al minuto**
  * Sí, a menos que me haya perdido algo importante, así es como es.
  * El temporizador de actualización probablemente se desplace también un poco, retrasando aún más la actualización. Podría haber alguna corrección automática, pero de nuevo, esto es más una prueba de concepto, y otras excusas.
* **La base de datos se carga nuevamente cada vez que cambia la hora**
  * Voy a culpar al ciclo de vida del widget aquí, que obviamente no he comprendido completamente. ¿Supongo?
* **El texto no encaja / necesita demasiado espacio / se ve mal / ...**
  * De nuevo, prueba de concepto. Pero también, algunas de las limitaciones que vienen con el entorno del widget no me dejaron muy feliz con la situación general de este proyecto para perseguir un camino más amigable para el usuario. Podría haber alguna actividad de configuración adjunta para establecer colores, fuentes y fondo o algo así, seguro.

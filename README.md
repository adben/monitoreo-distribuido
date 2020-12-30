## Monitoreo Distribuido
Division de la estructura del articulo seria:

#Introducir sistemas reactivos
Hablar de verte y sus usos
Simple conceptos acerca de que siempre se espera finalizar el span y su correspondiente
//codigo
#TDD
correlación con Mockspan

Hablar de los problemas introducidos por microservices  en la organización

Introducir Jaeger y su historia la CNC y como se genero el
Mostrar como se instrumenta en java

# OpenTracing
Introducción De la mano de Uber, OpenTracing se ofrece como un estándar asociado a la trazabilidad distribuída. Aunque parezca que OpenTracing es un estándar nuevo, lleva existiendo y usándose en arquitecturas complejas basadas en servicios desde hace tiempo, pero no ha dado el salto a popularizarse hasta encontrar su espacio en los microservicios.
Conceptos
Cabe destacar que OpenTracing no se apoya en ningún lenguaje de programación específico, es decir, tiene sentido bajo cualquier lenguaje. A efectos prácticos, esto se traduce en ofrecer al desarrollador una interfaz con una serie de funcionalidades para cumplir su estándar. El listado de los lenguajes de programación soportados se puede consultar aquí. En nuestro caso, elegiremos la interfaz que se ofrece para Java y su relacion con Vert.x. La implementación de esta interfaz recaerá en manos del desarrollador.
Span
Se define como la representación de una unidad lógica de trabajo, pudiendo ser desde una llamada http hasta un acceso a base de datos. La parte clave de este concepto es que tiene referencia a otros spans, construyéndose a través de esta información un flujo temporal, es decir, una traza.
Contiene los siguientes campos:
Nombre.
Timespan de inicio y de fin.
Referencias a otros spans.
SpanContext.
Conjunto de tags (clave/valor).
Conjunto de logs (clave/valor).
En Opentracing, se entienden dos tipos de referencias entre spans:
ChildOf: el span padre depende del span hijo (síncrono). El span padre siempre espera respuesta del span hijo. Por ejemplo, una petición http la cual esperamos respuesta.
FollowsFrom: el span padre no depende del span hijo (asíncrono). El span padre podría terminar antes que el hijo. Por ejemplo, en mensajería asíncrona.
OpenTracing proveerá una interfaz Span capaz de realizar todas las operaciones necesarias para obtener y alterar estos campos.
SpanContext
Contenido dentro del Span, encontramos el SpanContext. Representa el estado del span que se suministrará a hijos y a otros procesos que lo requieran. En esta entidad, podemos encontrar el identificador asociado al span y el identificador de la traza. Además, contiene una lista de baggage items, entendiéndose como un listado clave/valor de información extra que pueda servirnos como identificador extra.
De nuevo, OpenTracing proveerá una interfaz SpanContext para gestionarlos, aunque la mayor parte de las operaciones se podrán realizar a través de la funcionalidad que ofrece la interfaz Span.
Tags y logs
Los tags sirven para anotar consultas, filtros y de más información necesaria para comprender la traza. En cambio, los logs son útiles para capturar mensajes de log siguiendo una línea temporal, facilitando entre otras cosas el debug de la ejecución del span.
Existe una convención de nombrado de las claves que podemos encontrar aquí.
Ejemplo de span

#Vert.x

#ElasticSearch
Introducir ElasticSearch y como se combinaria con jaeger
Exponer la base de Observablidad corriendo una aplicación con jaeger de fondo
// codigo para containers… solo jaeger app y solo app instrumented
Conceptos de instrumentacion y caminar por Jaeger

# Codigo docker
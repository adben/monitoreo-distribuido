**"Observación y Monitoreo de Sistemas Reactivos en Ambientes Distribuidos"**

*Ante proyecto de grado para optar al título de Ingeniero de Sistemas*

*Realizado por:*

Adolfo Enrique Benedetti Velasquez

*Bajo la dirección de:*

John Freddy Parra Peña

Máster universitario en Dirección e Ingeniería de Sitios Web

![](images/media/image1.png){width="3.0555555555555554in" height="2.368996062992126in"}

Universidad Distrital Francisco José de Caldas

FACULTAD DE INGENIERÍA

Contenido

CAPÍTULO 1 4

Introducción 4

CAPÍTULO 2 5

Planteamiento del problema 5

CAPÍTULO 3 7

Objetivo General 7

Objetivos Específicos 7

CAPÍTULO 4 8

Justificación 8

CAPÍTULO 5 9

Delimitación 9

CAPÍTULO 6 10

Marco teórico 10

6.1 Flujos Reactivos 10

6.2 Programación Reactiva 10

6.2.1 Programación Reactiva y Sistemas Reactivos 10

6.3 Microservicios Reactivos 11

6.4 Modelo de desarrollo Asíncrono 12

6.5 Herramientas y estándares 14

6.5.1 CNCF 14

6.5.2 Containers (Docker) 14

6.5.3 Jaeger 15

6.5.4 Opentracing 17

6.5.5 ElasticSearch 18

6.5.6 Kubernetes 19

6.5.7 Redhat Openshift 19

6.5.8 Quarkus 20

6.5.9 Vert.x 21

6.5.9.1 Reactor (Patrón de diseño) y el Bucle de eventos 21

6.6 Kanban y Scrum 23

CAPÍTULO 7 24

Recursos Humanos 24

CAPÍTULO 8 25

Plan 25

8.1 Etapas 25

8.2 Hitos 25

8.3 Control de Calidad 26

8.4 Presupuesto y recursos 26

8.5 Entregables 27

Referencias 28

CAPÍTULO 1

Introducción

El presente documento define y justifica el proyecto de grado para optar al título de Ingeniero de Sistemas por parte del estudiante Adolfo Enrique Benedetti Velásquez de la Universidad distrital Francisco José de Caldas.

A lo largo de 16 años de experiencia como desarrollador Java para diferentes industrias y organizaciones tanto en Colombia como en Holanda, veo como microservicios reactivos y el uso de la nube mas que tendencias, ofrecen verdadero valor agregado a las organizaciones que hacen uso de estas herramientas.

A pesar de que el lenguaje Java y la Maquina Virtual de Java (JVM) siguen estando a la cabeza como elección preferida en ambientes empresariales, muchas de las APIs, frameworks y librerías tienen una falla fundamental en términos de performance por definición: Bloquean el hilo donde se están ejecutando hasta que la subtarea este finalizada. Para resolver este problema de forma reactiva podemos hacer uso de tecnologías que están diseñadas para no hacer este tipo de bloqueos como lo es Vert.x.

Naturalmente el costo que se tiene en términos de complejidad es alto debido a que el diseño, desarrollo, despliegue, mantenimiento y extension de estos servicios cambian drásticamente. Y esto se ve reflejado en la falta de visualización y monitoreo que tengamos de las aplicaciones. Este trabajo de grado pretende mostrar una solución alternativa a este problema combinado monitoreo clásico con las nuevas técnicas y estándares de "observabilidad" en la nube.

CAPÍTULO 2

Planteamiento del problema

Debido a la complejidad del problema, lo expondré presentándolo gradualmente desde: "Microservicios" reactivos hasta llegar a la "Observabilidad" de microservicios e impacto del uso de microservicios reactivos en ambientes corporativos que usan metodologías ágiles.

*Microservicios reactivos*

Programación reactiva es un modelo de desarrollo orientado alrededor de flujo de datos y la propagación de datos. En programación reactiva el estímulo es la data moviéndose en el flujo o Streams.

Si bien no existe una definición oficial de microservicios. Martin Fowler, argumenta que las arquitecturas de microservicios exhiben las siguientes características comunes[^1]:

*Componentización* a través de (micro) servicios: La c*omponentización* de funcionalidad en una aplicación compleja se logra a través de servicios o microservicios, que son procesos independientes que se comunican a través de una red. Los microservicios están diseñados para proporcionar interfaces de grano fino y ser de tamaño pequeño, desarrollados de forma autónoma y desplegables de forma independiente.

*Puntos finales inteligentes y tuberías tontas*: las comunicaciones entre los servicios utilizan protocolos independientes de la tecnología, como HTTP y REST, a diferencia de los mecanismos inteligentes como el Enterprise Service Bus (ESB).

*Organizado en función de las capacidades empresariales*: Osea productos, no proyectos: los servicios se organizan en torno a funciones empresariales (\"servicio de perfil de usuario\" o \"servicio de cumplimiento\"), en lugar de tecnologías. El proceso de desarrollo trata los servicios como productos en continua evolución en lugar de proyectos que se consideran completados una vez entregados.

*Gobernanza descentralizada*: permite implementar diferentes microservicios utilizando diferentes pilas de tecnología.

*Gestión de datos descentralizada*: se manifiesta en las decisiones tanto para los modelos de datos conceptuales como para las tecnologías de almacenamiento de datos que se realizan de forma independiente entre los servicios.

*Automatización de infraestructura*: los servicios se crean, lanzan e implementan con procesos automatizados, utilizando pruebas automatizadas, integración continua e implementación continua.

*Diseño para fallas*: siempre se espera que los servicios toleren fallas de sus dependencias y reintenten las solicitudes o degraden con gracia su propia funcionalidad.

*Diseño evolutivo*: se espera que los componentes individuales de una arquitectura de microservicios evolucionen de forma independiente, sin forzar actualizaciones en los componentes que dependen de ellos.

*"Observabilidad"*

Ahora bien, el rápido escalamiento de compañías de internet, y posteriores subproductos de la nube como Amazon AWS o Google Cloud, combinado con herramientas enfocadas a infraestructura como Containers y Kubernetes, han abierto una nueva era de sistemas distribuidos operando en cientos de nodos alrededor de múltiples centros de datos. Arquitecturas enfocadas a microservicios actualmente han incrementando la complejidad de estos sistemas exponencialmente, a pesar de que permiten a los sistemas responder en un tiempo aceptable, escalar vertical y horizontalmente, permitir flujos de datos de forma asíncrona, hace aún más difícil monitorear el estatus actual de dichos sistemas.

La programación asincrónica es clave para maximizar el uso de estos nuevos recursos de hardware, ya que permite lidiar con más conexiones concurrentes que con los paradigmas tradicionales de bloqueo de E / S (Entrada/ Salida). Los servicios deben atender las cargas de trabajo que pueden cambiar drásticamente de una hora a otra, por lo tanto, debemos diseñar un código que admita naturalmente la escalabilidad horizontal. Pero esto solo incrementa la complejidad de las aplicaciones, algo para lo cual el monitoreo de microservicios reactivos no estaba diseñado inicialmente.

El monitoreo de microservicios reactivos se puede realizar haciendo uso combinado del clásico registro de eventos *(Logging), rastreo distribuido* y combinado esto con *métricas,* en los ambientes donde estos sistemas sean desplegados.

CAPÍTULO 3

Objetivo General

Monitoreo de ambientes distribuidos trabajando con microservicios reactivos es difícil, *logging*(registro de eventos) clásico no es suficiente, *Rastreo distribuido* ofrece mejor desempeño, pero la combinación ésas dos técnicas ademas de la inclusión de otros roles el de la organización en la retroalimentación de esta información presentada es posiblemente una manera más acertada de incrementar la visibilidad de estos sistemas.

Objetivos Específicos

Proveer una alternativa de observabilidad y monitoreo en sistemas distribuidos, enfocado a frameworks que hace uso del *bucle de eventos* y el patrón del reactor, específicamente Quarkus y Vert.x, demostrando así como estos simplifican el procesamiento de eventos asíncronos Teniendo en cuenta que la administración de E / S sin bloqueo es más compleja que el código imperativo equivalente basado en el bloqueo de E / S.

Identificar las mejores practicas mínimamente invasoras a la hora de monitorear complejos sistemas distribuidos. Monitoreando un sistema reactivo que es escalable y resistente para producir respuestas con latencias constantes a pesar de las cargas de trabajo exigentes y las fallas.

Demostrar como Vert.x es un kit de herramientas accesible y eficiente para escribir de forma asíncrona y reactiva aplicaciones en la maquina virtual de Java (JVM). Usando programación asincrónica para multiplexar múltiples conexiones de red en un solo hilo.

CAPÍTULO 4

Justificación

Los microservicios reactivos ofrecen aislamiento y autonomía a un nivel que las arquitecturas tradicionales no pueden ofrecer. Los microservicios reactivos tienen una responsabilidad única y publican sus capacidades a través de un protocolo. Se basan en mensajes y pueden cooperar y colaborar sin estar estrechamente vinculados.

El uso de microservicios reactivos no es una mera tendencia, en ambientes distribuidos ofrecen la manera mas rápida, eficiente, robusta y escalable para producir valor agregado a las organizaciones que hacen uso de la nube como su plataforma de despliegue. El problema de este enfoque, es que le quita visibilidad del comportamiento de estos sistemas o se hace mas difícil el monitoreo de estos.

Vert.x ayuda al desarrollo de microservicios reactivos, pero ¿qué pasa con la observabilidad y/o monitoreo de la aplicación? Es importante que en despliegues distribuidos que podamos observar las solicitudes que maneja la aplicación. Consideremos una aplicación de comercio electrónico, por ejemplo; Se puede pasar una única solicitud de pago a decenas o cientos de servicios antes de que la aplicación termine de manejar ese proceso; ya sea en entornos de desarrollo o producción, los equipos de desarrolladores, de negocios y de soporte necesitan herramientas para comprender y depurar los problemas que puedan surgir dentro de sus servicios o para la tomar decisiones para futuros desarrollos.

CAPÍTULO 5

Delimitación

El foco de este trabajo de grado ES observabilidad y monitoreo de microservicios reactivos y como para ello debemos entonces hacer uso combinado del clásico *Log(registro de eventos), rastreo o traceo distribuido* y uso de *métricas,* en los ambientes donde estos sistemas sean desplegados[^2], específicamente containers. El despliegue de esta información podría presentarse de una forma atractiva, preferiblemente visualizaciones vía dashboards, de tal manera por ejemplo, roles dentro de la organización (diferente a desarrolladores) puedan también producir valor agregado para futuros desarrollos mediante conjeturas basadas en información real. NO se analizaran las mejores practicas de SCRUM vs Kanban cuando nos enfrentamos a microservicios reactivos, pero se espera que las técnicas y herramientas a explorar en este proyecto, enriquezcan los equipos de trabajo Agiles.

Se hará uso de Vert.x y Quarkus como convenientes herramientas para desarrollar microservicios reactivos en la maquina virtual de java(JVM), señalare las ventajas y los problemas que este resuelve pero NO profundizare en complejas técnicas del manejo de Streams, o el event-bus, incluso si en dado caso el uso de mecanismos como *Back-Pressure* sean requeridos.

Se instrumentara una aplicación que refleje lo aquí expuesto y los resultados encontrados serán publicados en un articulo(posiblemente de alcance académico) con código fuente verificable liberando este con licencias libres.

CAPÍTULO 6

Marco teórico

6.1 Flujos Reactivos[^3]

Los flujos reactivos son una iniciativa para proporcionar un estándar para el procesamiento de flujo asíncrono con contrapresión(*back-pressure*). Proporciona un conjunto mínimo de interfaces y protocolos que describen las operaciones y entidades para lograr los flujos de datos asincrónicos con contrapresión sin bloqueo. No define los operadores que manipulan los flujos, y se utiliza principalmente como una capa de interoperabilidad. Esta iniciativa es apoyada por Netflix, Lightbend y Red Hat(Uno de los mayores contribuidores de Vert.x y Kubernetes), entre otros.

6.2 Programación Reactiva

La programación reactiva es un modelo de desarrollo orientado a los flujos de datos y la propagación de datos. En la programación reactiva, los estímulos son los datos que transitan en el flujo, que se denominan corrientes.![](images/media/image2.png){width="5.5in" height="3.585209973753281in"}

6.2.1 Programación Reactiva y Sistemas Reactivos

Si bien la programación reactiva es un modelo de desarrollo, los sistemas reactivos son un estilo arquitectónico utilizado para construir sistemas distribuidos[^4]. Provee un conjunto de principios utilizados para lograr capacidad de respuesta y crear sistemas que respondan a las solicitudes de manera oportuna, tolerante a fallas y/o bajo carga.

Para construir dicho sistema, los sistemas reactivos adoptan un enfoque basado en mensajes. Todos los componentes interactúan usando mensajes enviados y recibidos de forma asíncrona. Para desacoplar remitentes y receptores, los componentes envían mensajes a direcciones virtuales (por ejemplo una URL). Se pueden registrar varios receptores en la misma dirección: Los remitentes no bloquean y esperan una respuesta. El remitente puede recibir una respuesta más tarde, pero mientras tanto, puede recibir y enviar otros mensajes. El uso de interacciones asíncronas de paso de mensajes proporciona sistemas reactivos con dos propiedades críticas:

• *Elasticidad*: la capacidad de escalar horizontalmente

• *Resiliencia*: la capacidad de tolerar fallos y recuperarse

La elasticidad proviene del desacoplamiento proporcionado por las interacciones de mensajes. Los mensajes enviados a una dirección pueden ser consumidos por un conjunto de consumidores que utilizan una estrategia de equilibrio de carga(*load-balancing*). Cuando un sistema reactivo se enfrenta a un pico en la carga, puede generar nuevas instancias de consumidores y eliminarlas después.

Esta característica de resiliencia es proporcionada por la capacidad de manejar fallas sin bloqueo, así como la capacidad de replicar componentes. Primero, las interacciones de mensajes permiten a los componentes lidiar con fallas localmente. Gracias al aspecto asíncrono, los componentes no esperan activamente las respuestas, por lo que una falla en un componente no afectaría a otros componentes. La replicación también es una habilidad clave para manejar la resiliencia. Cuando un mensaje de procesamiento de nodo falla, el mensaje puede ser procesado por otro nodo registrado en la misma dirección.

Gracias a estas dos características, el sistema se vuelve receptivo. Puede adaptarse a cargas más altas o más bajas y continuar atendiendo solicitudes ante cargas altas o fallas. Este conjunto de principios es primordial cuando se construyen sistemas de microservicios que están altamente distribuidos y cuando se trata de servicios que están fuera del control de la persona que llama(de ahí la importancia de observabilidad y monitoreo). Es necesario ejecutar varias instancias de sus servicios para equilibrar la carga y manejar fallas sin interrumpir la disponibilidad. Por medio del uso de Vert.x implementaré estos conceptos.

6.3 Microservicios Reactivos

Al construir un sistema compuesto de microservicios (y por lo tanto distribuido), cada servicio puede cambiar, evolucionar, fallar, exhibir lentitud o ser retirado en cualquier momento. Tales problemas no deben afectar el comportamiento de todo el sistema. El sistema debe aceptar los cambios y ser capaz de manejar fallas. Podrá ejecutar en un modo degradado, pero aun así el sistema deberá ser capaz de manejar las solicitudes.

Para garantizar dicho comportamiento, los sistemas de microservicio reactivos son microservicios comprimidos o reactivos. Estos microservicios tienen cuatro características:

Asincronismo

Autonomía

Resiliencia

Elasticidad

Los microservicios reactivos son autónomos. Se pueden adaptar a la disponibilidad o indisponibilidad de los servicios que los rodean. Sin embargo, la autonomía se combina con el aislamiento. Los microservicios reactivos pueden manejar fallas localmente, actuar de manera independiente y cooperar con otros según sea necesario. Un microservicio reactivo utiliza el paso de mensajes asíncrono para interactuar con sus pares. También recibe mensajes y tiene la capacidad de producir respuestas a estos mensajes.

Gracias al flujo de mensajes asíncrono, los microservicios reactivos pueden enfrentar fallas y adaptar su comportamiento consecuentemente. Las fallas no deben propagarse, sino manejarse cerca del componente causante de la falla. Es aquí donde *Observabilidad* y *Monitoreo* juegan un rol a la hora de identificar estos fallos, tanto en diferentes faces del desarrollo así como en la puesta en marcha a producción y posterior mantenimiento de estos microservicios. Cuando falla un microservicio, el microservicio del consumidor debe manejar la falla y no propagarla. Este principio de aislamiento es una característica clave para evitar que las fallas surjan y *rompan* el sistema. La resiliencia no se trata solo de manejar el fracaso, también se trata de la auto-recuperación. Un microservicio reactivo debe implementar estrategias de recuperación o compensación cuando ocurren fallas.

Finalmente, un microservicio reactivo debe ser elástico, de modo que el sistema pueda adaptarse a la cantidad de instancias para administrar la carga. Esto implica un conjunto de restricciones como por ejemplo; evitar el estado en memoria, compartir el estado entre instancias si es necesario, o poder enrutar mensajes a las mismas instancias para servicios con estado.

6.4 Modelo de desarrollo Asíncrono

Los patrones de diseño *RESTful*[^5] son comunes en el mundo de los microservicios. Lo más probable es que se implementen de forma síncrona y por lo tanto, de bloqueo. Incluso si esto se puede cambiar en Java EE, y las implementaciones admiten llamadas asincrónicas, aún podrían considerarse como ciudadano de segunda clase en una implementación típica.![](images/media/image3.png){width="5.5in" height="3.7291666666666665in"}

El *middleware* orientado a mensajes (MOM[^6]) es una solución más razonable para los problemas de integración y mensajería en este campo, especialmente cuando se trata de microservicios expuestos por sistemas host y conectados a través de MOM. Se puede usar una combinación de solicitud/respuesta en REST y publicación/suscripción de mensajes para satisfacer las necesidades de negocio.

6.5 Herramientas y estándares

6.5.1 CNCF

Como indicado en su sitio web[^7], La Cloud Native Computing Foundation (CNCF) aloja componentes críticos de la infraestructura tecnológica global cuando se trabaja con tecnologías alojadas en la nube. CNCF reúne a los principales desarrolladores, usuarios finales y proveedores del mundo y ejecuta las conferencias de desarrolladores de código abierto más grandes. CNCF es parte de la Fundación Linux sin fines de lucro.![](images/media/image4.png){width="5.5in" height="4.313889982502187in"}

6.5.2 Containers (Docker)

Docker es una herramienta diseñada para facilitar la creación, implementación y ejecución de aplicaciones mediante el uso de contenedores. Los contenedores permiten a un desarrollador empaquetar una aplicación con todas las partes que necesita, como bibliotecas y otras dependencias, y desplegarla como un paquete. Al hacerlo, gracias al contenedor, el desarrollador puede estar seguro de que la aplicación se ejecutará en cualquier otra máquina Linux, independientemente de cualquier configuración personalizada que pueda tener la máquina que podría diferir de la máquina utilizada para escribir y probar el código.

Acá es importante hacer una aclaración: Docker es también una compañía detrás de la tecnología que aloja probablemente el monopolio de containers en este momento, se tiende a asociarse automáticamente a Docker con containers, pero otros productos como *Podman*[^8] (desarrollada por *RedHat*) proveen mayor seguridad y mejor protección de los estándares propuestos por la Open Container Initiative (OCI)[^9], mi propuesta de observabilidad y monitoreo pretende ser independiente de la implementación de containers usada.

En cierto modo, Docker es un poco como una máquina virtual. Pero a diferencia de una máquina virtual, en lugar de crear un sistema operativo virtual completo, Docker permite que las aplicaciones usen el mismo kernel de Linux que el sistema en el que se ejecutan y solo requiere que las aplicaciones se envíen con cosas que aún no se ejecutan en la computadora host. Esto proporciona un aumento significativo del rendimiento y reduce el tamaño de la aplicación.

Y lo más importante, Docker es de código abierto. Esto significa que cualquiera puede contribuir a Docker y extenderlo para satisfacer sus propias necesidades si necesitan funciones adicionales que no están disponibles de fábrica.

6.5.3 Jaeger

Jaeger es el corazón de la observabilidad en este trabajo de grado, desarrollado por Uber para suplir eficientemente monitoreo a su compleja arquitectura de microservicios. A pesar de que ElasticSearch tiene una herramienta similar llamada APM[^10], Jaeger es de código abierto y esta estrictamente ligada con los estándares dictados por la CNCF. Jaeger se utiliza para monitorear y solucionar problemas de entornos complejos de microservicios usando rastreo distribuido.

El rastreo distribuido es una forma de ver y comprender toda la cadena de eventos en una interacción compleja entre microservicios.

Una sola llamada en una aplicación puede invocar docenas de servicios diferentes que interactúan entre sí. ¿Cómo pueden los desarrolladores e ingenieros aislar un problema cuando algo sale mal o una solicitud se ejecuta lentamente? Necesitamos una manera de hacer un seguimiento de todas las conexiones.

Ahí es donde entra el rastreo distribuido. A menudo se ejecuta como parte de una malla de servicios, que es una forma de administrar y observar microservicios.![](images/media/image5.png){width="5.5in" height="3.7677952755905513in"}

Jaeger utiliza el rastreo distribuido para seguir la ruta de una solicitud a través de diferentes microservicios. En lugar de adivinar, podemos ver una representación visual de los flujos de llamadas.

La información organizada sobre las transacciones es útil para la depuración y la optimización. Jaeger incluye herramientas para monitorear transacciones distribuidas, optimizar el rendimiento y la latencia, y realizar análisis de causa raíz (RCA), un método de resolución de problemas.

6.5.3.1 Terminología y componentes de Jaeger:

Jaeger presenta solicitudes de ejecución como *trazas*. Una traza muestra la ruta de datos / ejecución a través de un sistema.

Una traza se compone de uno o más tramos. Un *lapso* es una unidad lógica de trabajo en Jaeger. Cada intervalo incluye el nombre de la operación, la hora de inicio y la duración. Los tramos pueden estar anidados y ordenados.

Jaeger incluye varios componentes que trabajan juntos para recopilar, almacenar y visualizar tramos y trazas.

*Jaeger Client* incluye implementaciones específicas del idioma de la API OpenTracing para el rastreo distribuido. Estos se pueden usar manualmente o con una variedad de marcos de código abierto.

*Jaeger Agent* es un demonio de red que escucha los tramos enviados a través del Protocolo de datagramas de usuario. El agente debe colocarse en el mismo host que la aplicación instrumentada. Esto generalmente se implementa a través de un sidecar en entornos de contenedor como Kubernetes.

*Jaeger Collector* recibe tramos y los coloca en una cola para su procesamiento.

Los *Collectors* requieren un backend de almacenamiento persistente, por lo que Jaeger también tiene un mecanismo enchufable para el almacenamiento span.

*Query* es un servicio que recupera rastros del almacenamiento.

*Jaeger Console* es una interfaz de usuario que le permite visualizar sus datos de rastreo distribuidos.

6.5.4 Opentracing[^11]

Es una API independiente del proveedor a utilizar, para ayudar a los desarrolladores a instrumentar fácilmente el rastreo en su base de código. Está abierto porque ninguna empresa lo posee. De hecho, muchas compañías de herramientas de rastreo están apoyando OpenTracing como una forma estandarizada de instrumentar el rastreo distribuido, parte de los core asociado a Jaeger usa esta API.

Entre sus principales componentes de OpenTracing tenemos a:

*Tracer - Trazador*![](images/media/image6.png){width="5.5in" height="4.147737314085739in"}

El rastreador(*tracer*) es el punto de entrada a la API de rastreo. Nos da la capacidad de crear tramos. También nos permite extraer información de rastreo de fuentes externas e inyectar información a destinos externos.

*Span - Lapso*

Esto representa una unidad de trabajo en la traza. Por ejemplo, una solicitud web que inicia un nuevo rastreo se denomina Span raíz. Si llama a otro servicio web, esa solicitud HTTP se incluiría en un nuevo Span secundario. Los tramos llevan consigo un conjunto de etiquetas de información pertinentes a la solicitud que se está llevando a cabo. También puede registrar eventos dentro del contexto de un Span. Pueden admitir flujos de trabajo más complejos que las solicitudes web, como la mensajería asincrónica. Tienen marcas de tiempo adjuntas para que podamos construir fácilmente una línea de tiempo de eventos para el seguimiento.

*SpanContext*

El SpanContext es la forma serializable de un Span. Permite que la información de Span se transfiera fácilmente a otros sistemas a través del cable.

6.5.5 ElasticSearch[^12]

Para centralización del monitoreo y observabilidad usare Elasticsearch, Elasticsearch es un motor de búsqueda y análisis de código abierto distribuido para todo tipo de datos, incluidos los textuales, numéricos, geoespaciales, estructurados y no estructurados. Elasticsearch se basa en Apache Lucene y fue lanzado por primera vez en 2010 por Elasticsearch N.V. (ahora conocido como Elastic). Conocido por sus API REST simples, naturaleza distribuida, velocidad y escalabilidad, Elasticsearch es el componente central de Elastic Stack, un conjunto de herramientas de código abierto para la ingestión, enriquecimiento, almacenamiento, análisis y visualización de datos. Conocido comúnmente como ELK Stack (después de Elasticsearch, Logstash y Kibana), Elastic Stack ahora incluye una rica colección de agentes de envío livianos conocidos como Beats para enviar datos a Elasticsearch.

*6.5.5.1 MetricBeat*

También parte de ElasticSearch, Metricbeat es un cargador ligero que se puede instalar en los servidores para recopilar periódicamente métricas del sistema operativo y de los servicios que se ejecutan en el servidor, en este caso, containers. Metricbeat toma las métricas y estadísticas que recopila y las envía a la salida que especifique, como Elasticsearch o Logstash.

*6.5.5.2 Kibana*

También parte de ElasticSearch, Kibana es una aplicación frontend de código abierto que se encuentra en la parte superior del Elastic Stack, proporcionando capacidades de búsqueda y visualización de datos para datos indexados en Elasticsearch. Conocida comúnmente como la herramienta de gráficos para Elastic Stack. La idea es usar Kibana para:

Buscar, ver y visualizar datos indexados en Elasticsearch y analizar los datos mediante la creación de gráficos de barras, gráficos circulares, tablas, histogramas y mapas.

Combinando estos elementos visuales para luego ser compartidos a través del navegador para proporcionar vistas analíticas en tiempo real en grandes volúmenes de datos en apoyo de casos de uso como:

> Logging y log analytics.
>
> Métrica de infraestructura y monitoreo de contenedores.
>
> Supervisión del rendimiento de la aplicación (APM).
>
> Análisis y visualización de datos geoespaciales.
>
> Análisis de seguridad.
>
> Análisis de negocio.

6.5.6 Kubernetes

Kubernetes (también conocido como k8s o \"kube\") es una plataforma de orquestación de contenedores de código abierto que automatiza muchos de los procesos manuales involucrados en la implementación, administración y escalado de aplicaciones en contenedores.

En otras palabras, puede agrupar grupos de hosts que ejecutan contenedores de Linux, y Kubernetes le ayuda a administrar esos clústeres de manera fácil y eficiente.

Los clústeres de Kubernetes pueden abarcar hosts en nubes locales, públicas, privadas o híbridas. Por esta razón, Kubernetes es una plataforma ideal para alojar aplicaciones nativas de la nube que requieren un escalado rápido, como la transmisión de datos en tiempo real a través de Apache Kafka.

Kubernetes fue originalmente desarrollado y diseñado por ingenieros de Google. Google genera más de 2 mil millones de implementaciones de contenedores a la semana, todo impulsado por su plataforma interna, Borg. Borg fue el predecesor de Kubernetes, y las lecciones aprendidas del desarrollo de Borg a lo largo de los años se convirtieron en la principal influencia detrás de gran parte de la tecnología de Kubernetes.

Con Kubernetes se puede:

> Orquestar contenedores en varios hosts.
>
> Aprovechar mejor el hardware para maximizar los recursos necesarios para ejecutar sus aplicaciones empresariales.
>
> Controlar y automatizar las implementaciones y actualizaciones de aplicaciones.
>
> Montar y agregar almacenamiento para ejecutar aplicaciones con estado.
>
> Escalar aplicaciones en contenedores y sus recursos sobre la marcha.
>
> Gestionar declarativamente los servicios, lo que garantiza que las aplicaciones implementadas siempre se ejecutan de la manera deseada.
>
> Verificar el estado y autorecuperacion las aplicaciones con colocación automática, inicio automático, autorreplicación y escalado automático.

6.5.7 Redhat Openshift

De acuerdo con la Wikipedia[^13]; "OpenShift, formalmente llamado Openshift Container Platform (OCP), es un producto de computación en la nube de plataforma como servicio de Red Hat. Los desarrolladores pueden usar Git para desplegar sus aplicaciones Web en los diferentes lenguajes de la plataforma.", pero toda su extensa funcionalidad la podríamos resumir en Openshift es la versión empresarial de Kubernetes hecha por RedHat, Inc.![](images/media/image7.png){width="5.5in" height="4.097222222222222in"}

6.5.8 Quarkus

Quarkus es un fullstack framework de trabajo Java nativo de Kubernetes creado para máquinas virtuales Java (JVM) y compilación nativa, que optimiza Java específicamente para contenedores y le permite convertirse en una plataforma eficaz para entornos sin servidor, en la nube y Kubernetes.

Quarkus está diseñado para funcionar con estándares, marcos y bibliotecas populares de Java como Eclipse MicroProfile y Vert.x, así como con Apache Kafka, RESTEasy (JAX-RS), Hibernate ORM (JPA), Spring, Infinispan, Camel y muchos más.

La solución de inyección de dependencias de Quarkus se basa en CDI (inyección de contextos y dependencias) e incluye un marco de extensión para ampliar la funcionalidad y configurar, arrancar e integrar un marco en su aplicación. Agregar una extensión es tan fácil como agregar una dependencia, o puede usar las herramientas de Quarkus.

También proporciona la información correcta a GraalVM (una máquina virtual universal para ejecutar aplicaciones escritas en varios lenguajes, incluidos Java y JavaScript) para la compilación nativa de su aplicación.

6.5.9 Vert.x

La esencia de Eclipse Vert.x es procesar eventos asincrónicos, principalmente provenientes de E / S sin bloqueo, y el modelo de subprocesos es procesar eventos en un *bucle de eventos*.

Vert.x es un kit de herramientas y no un marco: no proporciona una base predefinida para su aplicación, por lo que puede usar Vert.x como una biblioteca dentro de una base de código más grande. Vert.x no tiene muchas opiniones sobre las herramientas de compilación que debe utilizar, cómo desea estructurar su código, cómo piensa empaquetarlo y desplegarlo, etc. Una aplicación Vert.x es un conjunto de módulos que proporciona exactamente lo que necesita , y nada más. Si no necesita acceder a una base de datos, su proyecto no necesita depender de las API relacionadas con la base de datos.

Vert.x está estructurado de la siguiente manera:

*Vertx-core*, proporciona la programación síncrona de API, E / S sin bloqueo, transmisión y acceso conveniente a protocolos en red como TCP, UDP, DNS, HTTP o WebSockets,

*Vert.x stack(modulos proporcionados por la comunidad),* como una mejor API web (vertx-web) o bases de datos (vertx-redis, vertx-mongo, etc.),

*Extensiones*: un amplio sistema de proyectos que brindan incluso más funcionalidades, como

conectando con Apache Cassandra, E / S sin bloqueo para comunicarse entre procesos del sistema, etc.

Vert.x es políglota ya que admite la mayoría de los lenguajes JVM populares y relacionados: JavaScript, Ruby, Kotlin, Scala, Groovy y más. Curiosamente, el soporte de estos lenguajes no es solo a través de la interoperabilidad de estos lenguajes con Java. Se están generando enlaces idiomáticos, para que pueda escribir código Vert.x en estos idiomas que aún se siente natural. Por ejemplo, los enlaces Scala usan las futuras API de Scala[^14]. Y, por supuesto, puede mezclar y combinar diferentes lenguajes compatibles dentro de la misma aplicación Vert.x.

6.5.9.1 Reactor (Patrón de diseño) y el Bucle de eventos

Desde la wikipedia[^15] vemos que: "El patrón de diseño reactor es un patrón de programación concurrente para manejar los pedidos de servicio entregados de forma concurrente a un manejador de servicio desde una o más entradas. El manejador de servicio demultiplexa los pedidos entrantes y los entrega de forma sincrónica a los manejadores de pedidos asociados."

![](images/media/image8.png){width="5.5in" height="2.6139807524059493in"}

Ahora bien, Eclipse Vert.x implementa un patrón multi-reactor soportado por eventloops. En el Reactor, existe un flujo de eventos delegados a los manejadores por un hilo llamado eventloop(bucle de eventos). Debido a que el bucle de eventos observa el flujo de eventos y llama a los controladores(handlers) para manejar el evento, es importante que NUNCA bloquee el bucle de eventos. Si los controladores no están disponibles para el bucle de eventos, entonces el bucle de eventos tiene que esperar; por lo tanto, efectivamente llamamos al evento loop bloqueado.![](images/media/image9.png){width="5.5in" height="3.0584405074365706in"}

Eclipse Vert.x implementa un patrón multi-reactor donde, por defecto, cada núcleo de CPU tiene dos bucles de eventos. Esto le da a las aplicaciones que usan Vert.x la capacidad de respuesta necesaria cuando aumenta el número de eventos.

Otro concepto importante es el bus de eventos, que es cómo las vértices(*verticles*: unidades mínimas de procesamiento de Vert.x) pueden comunicarse entre sí de una manera de publicación-suscripción. Los vértices se registran en el bus de eventos y se les da una dirección para escuchar. El bus de eventos permite escalar los vértices, ya que solo necesitamos especificar en qué dirección un vértice escucha los eventos y dónde debe publicar esos eventos.

6.6 Kanban y Scrum

Agile es un enfoque estructurado e iterativo para la gestión de proyectos y el desarrollo de productos. Reconoce la volatilidad del desarrollo de productos y proporciona una metodología para que los equipos auto-organizados respondan al cambio sin salirse de los rieles. Hoy, ágil no es una ventaja competitiva. Nadie tiene el lujo de desarrollar un producto durante años o incluso meses en una caja negra.

*Kanban* se trata de visualizar su trabajo, limitar el trabajo en progreso y maximizar la eficiencia (o flujo). Los equipos Kanban se centran en reducir el tiempo que lleva llevar un proyecto (o historia de usuario) de principio a fin. Para ello, utilizan un tablero kanban y mejoran continuamente su flujo de trabajo.

Los equipos de *Scrum* se comprometen a enviar software de trabajo a través de intervalos establecidos llamados sprints. Su objetivo es crear ciclos de aprendizaje para recopilar e integrar rápidamente los comentarios de los clientes. Los equipos de Scrum adoptan roles específicos, crean artefactos especiales y celebran ceremonias regulares para que las cosas sigan avanzando.

CAPÍTULO 7

Recursos Humanos

Adolfo Enrique Benedetti Velasquez

Profesional del software con una gran pasión por el desarrollo de software en general, y herramientas modernas de desarrollo de código abierto en particular. Mi interés no se limita al lenguaje de desarrollo *Java*, sino también soy competente en *Scala*, *Clojure*, *Groovy*, *Kotlin* y *Python*. Me esfuerzo por obtener el mejor resultado, a fin de lograr la satisfacción óptima del cliente. En mi opinión, esto es posible al brindar a los desarrolladores las herramientas más apropiadas para el trabajo.

A menudo, *Java* es suficiente para cumplir las necesidades de negocio en proyectos específicos, pero a veces *Kotlin* es mejor, creo que esto aplica además de los lenguajes de programación para *Libraries*, *frameworks* y/o plataformas.

He tenido la suerte de trabajar en diferentes proyectos tanto en Colombia como en Holanda, que me han proporcionado amplia experiencia en casi todas las facetas del desarrollo de software, ya sea que se trate de interfaces de usuario (*HTML, CSS, JavaScript* en general *ReactJS*, *AngularJS*, *Vue.js* y *PolymerJS* en particular), aplicaciones de back-end (*Spring Boot, HippoCMS, Liferay Portal, AWS Lambda*) o bases de datos (incluido *PostgreSQL , MongoDB, DynamoDB y Datomic*).

Durante este projecto ademas de la extensión, desarrollo y puesta en marcha del monitoreo relacionados con los microservicios reactivos, diseñare los dashboards y las visualizaciones requeridas para una efectiva observabilidad.

CAPÍTULO 8

Plan

8.1 Etapas

A pesar de que utilizare la metodología SCRUM con ciclos de desarrollo y feedback de dos semanas, y de que uno de los goles del proyecto es empoderar diferentes roles a los del desarrollo dentro del producto final de investigación, no me extenderé en el marco teórico en relación con conceptos relacionados con metodologías Agiles.

El proyecto también da por establecido que ya se ha de tener herramientas listas para el proceso evolutivo de integración continua / despliegue continuo (*CI/DC*, git, jenkins, etc), pero se incluirá en el presupuesto[^16] el servicio de la nube a la todo en uno.

+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+
| Fase           | Secuencia                                                                                                      | Horas\                    |
|                |                                                                                                                | Estimadas                 |
+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+
| Planificación  | Selección de Proyecto con Vert.x para monitoreo.                                                               | 24                        |
|                |                                                                                                                |                           |
|                | Contacto y planeamiento con los mantenedores del proyecto indicado la intensión de extensión del código fuente |                           |
|                |                                                                                                                |                           |
|                | Extension y evaluación de Herramientas a usar.                                                                 |                           |
+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+
| Implementación | Implementación OpenTracing de puesta en marcha del proyecto.                                                   | 320                       |
|                |                                                                                                                |                           |
|                | Control de calidad via Sonar.                                                                                  |                           |
|                |                                                                                                                |                           |
|                | Despliegue de aplicación en Kubernetes (DTAP)                                                                  |                           |
|                |                                                                                                                |                           |
|                | Despliegue ELK stack                                                                                           |                           |
|                |                                                                                                                |                           |
|                | Despliegue Jaeger                                                                                              |                           |
|                |                                                                                                                |                           |
|                | Configuración Indices Jaeger - ELK                                                                             |                           |
|                |                                                                                                                |                           |
|                | Creación Dashboards Kibana                                                                                     |                           |
+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+
| Seguimiento    | Análisis plataforma                                                                                            | 36                        |
|                |                                                                                                                |                           |
|                | Pruebas de Carga                                                                                               |                           |
|                |                                                                                                                |                           |
|                | Generación de Reportes                                                                                         |                           |
+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+
| Control        | Evaluación reportes Sonar, control de calidad.                                                                 | 36                        |
+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+
| Finalización   | Liberación código fuente (con licencias libres)                                                                | 80                        |
|                |                                                                                                                |                           |
|                | Generación publicación                                                                                         |                           |
+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+
|                |                                                                                                                | 496 horas estimadas total |
+----------------+----------------------------------------------------------------------------------------------------------------+---------------------------+

8.2 Hitos

El proyecto es iterativo, pero los hitos nos ayudaran a identificar el porcentaje de completado en relación con las diferentes etapas

  ------------------------------------------------------------ ----------------
  Hito                                                         Dependencia(s)
  a\) Aprobación proyecto de grado                             
  b\) Selección proyecto para Monitoreo                        A
  c\) Implementación Opentracing                               A, B
  d\) Despliegue plataforma                                    A
  e\) Creación Dashboards y Visualizaciones                    C, D
  f\) Reportes de Control de calidad sobre la implementación   C
  g\) Reportes y Análisis resultados                           C, E, F
  h\) Generación entregables                                   C, E, G
  ------------------------------------------------------------ ----------------

8.3 Control de Calidad

Para una deuda técnica aceptable de la implementación de *OpenTracing*, mantener la legibilidad del código y mejorar los estándares de seguridad (por ejemplo Top 10 *OWASP*[^17] ) utilizare SONAR(*sonarqube*)[^18] en las diferentes etapas y se asignara tiempo e iteraciones del proyecto para este fin.

8.4 Presupuesto y recursos

Intente enumerar el presupuesto y los recursos para los entregables por hito y/o etapa, teniendo en cuenta que soy el recurso humano disponible pero no ejecuto un único rol, los costos difieren respecto al rol a ejecutar en mi contexto; un Desarrollador Java en holanda tiene en promedio una tarifa de 80/110 euros por hora, un Project manager 100/120 euros por hora, un Tester/Ingeniero QA 60/80 euros por hora [^19] sin incluir el impuesto BTW o IVA, esto varia dependiendo el lenguaje, la experiencia y la naturaleza del contrato. Obviamente, esa estimación no es perfectamente correcta. A medida que avanza el tiempo balanceare estas estimación con los hitos, a fin de controlar el tiempo de entrega de los entregables.

  --------------------------------- ------------------------------ ------------------------------
  Rol/Recurso                       Etapa/Hito/Razon               Costo (en euros)
  Project manager                   Planificación                  2,400
  Ingeniero Java                    Implementación                 25,600
  Ingeniero QA                      Seguimiento                    2,520
  Ingeniero Java/QA                 Control                        2,880
  Ingeniero QA                      Generación entregables         6,400
  MacBookPro 16' 2018               Hardware                       3,840
  Intellij Idea IDE                 Software / Licencia            499
  Openshift(kubernetes, git, ELK)   Software / Servicios de nube   36,000
                                                                   € 80,139
                                                                   Total (+BTW 19%) € 95.365,41
  --------------------------------- ------------------------------ ------------------------------

8.5 Entregables

+---------------+------------------------------------------------------------------------------------------------------------+
| Entregable    | Descripcion                                                                                                |
+---------------+------------------------------------------------------------------------------------------------------------+
| Código Fuente | Código fuente aplicación con instrumentación                                                               |
|               |                                                                                                            |
|               | Plantillas Jaeger                                                                                          |
|               |                                                                                                            |
|               | ELK Dashboards Fuente                                                                                      |
+---------------+------------------------------------------------------------------------------------------------------------+
| Publicación   | Articulo sobre Observación y Monitoreo de Sistemas Reactivos en Ambientes Distribuidos en Ingles y Español |
+---------------+------------------------------------------------------------------------------------------------------------+

Referencias

"Microservices: a definition of this new architectural term:" por Martin Fowler, James Lewis. [[https://www.martinfowler.com/articles/microservices.html]{.ul}](https://www.martinfowler.com/articles/microservices.html).

"Mastering Distributed Tracing" por Yuri Shkuro publicado por Packt Publishing, 2019

"Vert.x in Action" por Julien Ponge, será publicado al rededor de octubre 2020, por Manning Publications [[https://www.manning.com/books/vertx-in-action]{.ul}](https://www.manning.com/books/vertx-in-action)

"Building Reactive Microservices in Java" por Clement Escoffier (ebook gratuito) [[https://developers.redhat.com/promotions/building-reactive-microservices-in-java/]{.ul}](https://developers.redhat.com/promotions/building-reactive-microservices-in-java/)

"Reactive Microservices on the JVM with Vert.x" por Burr Sutter: [[https://www.youtube.com/watch?v=MydhJVPEnzU]{.ul}](https://www.youtube.com/watch?v=MydhJVPEnzU)

"Controlling Software Projects\" por DeMarco, Tom, Prentice-Hall, 1982

"Grafana v7.0 released: New plugin architecture, visualizations, transformations, native trace support, and more:" [[https://grafana.com/blog/2020/05/18/grafana-v7.0-released-new-plugin-architecture-visualizations-transformations-native-trace-support-and-more/?isource=hp]{.ul}](https://grafana.com/blog/2020/05/18/grafana-v7.0-released-new-plugin-architecture-visualizations-transformations-native-trace-support-and-more/?isource=hp)

\"The introduction to Reactive Programming you\'ve been missing" [[https://gist.github.com/staltz/868e7e9bc2a7b8c1f754]{.ul}](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754)

"Reactive programming tutorial: Reactive programming and Vert.x" [[https://youtu.be/o-cBfanMJ8A]{.ul}](https://youtu.be/o-cBfanMJ8A)

"Reactica, a thrilling roller coaster ride through another dimension featuring Red Hat Reactive Technologies" [[https://github.com/reactica/rhte-demo]{.ul}](https://github.com/reactica/rhte-demo)

"Introduction to OpenTelemetry" por Ran Ribenzaft [[https://epsagon.com/tools/introduction-to-opentelemetry-overview/]{.ul}](https://epsagon.com/tools/introduction-to-opentelemetry-overview/)

"Zipkin or Jaeger? The Best Open Source Tools for Distributed Tracing" por Ran Ribenzaft [[https://epsagon.com/observability/zipkin-or-jaeger-the-best-open-source-tools-for-distributed-tracing/]{.ul}](https://epsagon.com/observability/zipkin-or-jaeger-the-best-open-source-tools-for-distributed-tracing/)

"The Observer pattern done right" [[http://reactivex.io/]{.ul}](http://reactivex.io/)

"Observe 20/20 VIRTUAL: A one-day online conference about observability and the OpenTelemetry CNCF project." [[https://observe2020.io/videos/]{.ul}](https://observe2020.io/videos/)

"Elasticsearch 7.7 Brings Asynchronous Search, Secure Keystore and More" [[https://www.infoq.com/news/2020/05/elasticsearch-7-7-released/]{.ul}](https://www.infoq.com/news/2020/05/elasticsearch-7-7-released/)

"Monitoring Applications with Elasticsearch and Elastic APM" [[https://www.elastic.co/blog/monitoring-applications-with-elasticsearch-and-elastic-apm]{.ul}](https://www.elastic.co/blog/monitoring-applications-with-elasticsearch-and-elastic-apm)

"Metrics for the Win: Using Micrometer to Understand Application Behavior" por Erin Schnabel [[https://www.infoq.com/presentations/micrometer-metrics/]{.ul}](https://www.infoq.com/presentations/micrometer-metrics/)

"SRE with Java Microservices" por Jon Schneider publicado por O\'Reilly Media, Inc., 2020

"Jaeger Elasticsearch and Kibana" por [[Pavol Loffay]{.ul}](https://medium.com/@p.loffay?source=post_page-----7ecb846137b6----------------------) [[https://medium.com/jaegertracing/jaeger-elasticsearch-and-kibana-7ecb846137b6]{.ul}](https://medium.com/jaegertracing/jaeger-elasticsearch-and-kibana-7ecb846137b6)

"The Reactive Manifesto" [[https://www.reactivemanifesto.org/]{.ul}](https://www.reactivemanifesto.org/)

"What is Jaeger?" [[https://www.redhat.com/en/topics/microservices/what-is-jaeger]{.ul}](https://www.redhat.com/en/topics/microservices/what-is-jaeger)

"Building and understanding reactive microservices using Eclipse Vert.x and distributed tracing" por [[Tiffany Jachja]{.ul}](https://developers.redhat.com/blog/author/tjachja/) [[https://developers.redhat.com/blog/2019/05/13/building-and-understanding-reactive-microservices-using-eclipse-vert-x-and-distributed-tracing/]{.ul}](https://developers.redhat.com/blog/2019/05/13/building-and-understanding-reactive-microservices-using-eclipse-vert-x-and-distributed-tracing/)

"Distributed Tracing with Jaeger and the ELK Stack" [[https://logz.io/blog/jaeger-and-the-elk-stack/]{.ul}](https://logz.io/blog/jaeger-and-the-elk-stack/)

"What is OpenShift" [[https://www.openshift.com/learn/what-is-openshift]{.ul}](https://www.openshift.com/learn/what-is-openshift)

"Observability, Distributed Tracing and Kubernetes Management" https://thenewstack.io/observability-distributed-tracing-and-kubernetes-management/

"Learn how to design and build large scale distributed systems" [[https://systemdesignclass.com/]{.ul}](https://systemdesignclass.com/)

"Using OpenTracing with Jaeger to collect Application Metrics in Kubernetes" [[https://developers.redhat.com/blog/2017/07/10/using-opentracing-with-jaeger-to-collect-application-metrics-in-kubernetes/]{.ul}](https://developers.redhat.com/blog/2017/07/10/using-opentracing-with-jaeger-to-collect-application-metrics-in-kubernetes/)

"Jaeger Architecture" [[https://www.jaegertracing.io/docs/1.11/architecture/]{.ul}](https://www.jaegertracing.io/docs/1.11/architecture/)

"OpenTracing Prometheus Examples" [[https://github.com/objectiser/opentracing-prometheus-example]{.ul}](https://github.com/objectiser/opentracing-prometheus-example)

"Helloworld-MSA (Microservices architecture) Openshift workshop" [[http://bit.ly/msa-tutorial]{.ul}](https://bit.ly/msa-tutorial)

"Minishift: Minishift is a tool that helps you run OpenShift locally by running a single-node OpenShift" [[https://github.com/minishift/minishift]{.ul}](https://github.com/minishift/minishift)

"Infoq: Observability" [[https://www.infoq.com/observability/]{.ul}](https://www.infoq.com/observability/)

"Kanban vs. Scrum" https://www.atlassian.com/agile/kanban/kanban-vs-scrum

[^1]: Martin Fowler, James Lewis. Microservices: a definition of this new architectural term: https://www.martinfowler.com/articles/microservices.html.

[^2]: Apesar de que el autor de "Mastering Distributed Tracing"(publicado por Packt Publishing, 2019) y creador de Jaeger, Yuri Shkuro, propone el uso de *Prometheus*, *Prometheus* se queda corto a la hora de ponerle sentido a la data recolectada, por ende usare los dashboards de *Kibana*

[^3]: http://www.reactive-streams.org/

[^4]: http://www.reactivemanifesto.org/

[^5]: 

[^6]: https://en.wikipedia.org/wiki/Message-oriented_middleware

[^7]: https://www.cncf.io/

[^8]: https://podman.io/

[^9]: [[https://podman.io/whatis.html]{.ul}](https://podman.io/whatis.html)

[^10]: [[https://www.elastic.co/blog/monitoring-applications-with-elasticsearch-and-elastic-apm]{.ul}](https://www.elastic.co/blog/monitoring-applications-with-elasticsearch-and-elastic-apm)

[^11]: [[https://opentracing.io/specification/]{.ul}](https://opentracing.io/specification/)

[^12]: https://www.elastic.co/

[^13]: https://es.wikipedia.org/wiki/OpenShift

[^14]: [[https://www.scala-lang.org/blog/2018/04/19/scala-3.html]{.ul}](https://www.scala-lang.org/blog/2018/04/19/scala-3.html)

[^15]: https://es.wikipedia.org/wiki/Reactor\_(patr%C3%B3n_de_dise%C3%B1o)

[^16]: https://www.openshift.com/products/dedicated/

[^17]: https://www.cloudflare.com/learning/security/threats/owasp-top-10/

[^18]: https://www.sonarqube.org/

[^19]: https://uurtarief.tips/nl/zzp/ict/uurtarief-ict-zzp-freelancer

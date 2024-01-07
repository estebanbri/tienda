# TiendaAngular

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 13.2.4.

## Development server : http://localhost:4200

## Estructura y flujo de carpetas/modules (feature modules based approach)
+-----------+
|Modulo CORE|      ←   (imports)       ┐
+-----------+
      ↓  (imports)                     │
+------------+                    +-------------+
|  AppModule |     ←   (imports)  |Modulo Shared|
+------------+                    +-------------+
      ↓  lazy load                     │
+---------------+            
|Modulo Features|  ←   (imports)       ┘
+---------------+

Explicacion:
- Modulo core: 
      - componentes que son necesarios para la aplicación (pero que no deben compartirse, como un encabezado o pie de página) y otros componentes básicos centralizados de la aplicación (servicios singleton, ctes, utils, guards, interceptos, models).
      - unica instancia en toda la app, aseguramos esto con la validacion en el constructor de la clase CoreModule. 
      - su unica inyeccion es dentro de AppModule
- Modulo Shared:
      - Contiene componentes compartidos, directivas y pipes que utilizan varios módulos y sus componentes. A diferencia de core.module, puede (y debe) importarse en varios módulos cuando se deba reutilizar cualquiera de sus componentes, directivas o pipes
- Modulos features:
      - funcionalidades de negocio ded la app.

Pasos agregar stripe:
1. npm install @stripe/stripe-js

Nota:
El campo client_secret lo usa el FE para completar el proceso de pago de manera segura.

Stripe flow
FE                                                                     BE                                                          STRIPE
x   ------------send order info----------------------------->          x         ------------POST v1/payment_intents--------->    create payment intent   
x   <-----------payment_intent_id and client_secret---------           x         <------------{payment_intents}---------------
x   ------------stripe.confirmPayment() -------------------------------------------------------------------------------------->
x   <-------------------------------------------------------------------------------------------return_url---------------------


Podes agregar webhook y configurarlos en el dashboard y estos van a ser util para que el Backend se entere del status de un pago, 
sin tener que cargar la responsabilidad al FE de enviarle el status. Con estos webhook el backend directamente va a reaccionar,
una casuistica es que cuando queres guardar info o enviar un email al cliente desde el backend.

// 4. Modelo de datos
Tabla Customer (id, nom, ape, email, tel, dni, status)
Tabla User (id, user_name, password,activacion,  token_activacion, password_reset_token, password_reset_request, fk_customer_id)
Tabla Compra (id, transaction_id, medio_pago,  fecha, status,  email, id_customer, total_amount)

Explicacion:
transaction_id: en caso de stripe seria el payment_intent_id
medio_pago: el medio de pago utilizado valores enum por ej 'MP' en caso de que se efectuo por Mercado Pago, 'PP' Paypal
status: status del payment_intent_id (fijate el enum de status creo que el valor 'success' es uno de ellos)
id_customer: es el customer id de stripe o la plataforma que sea.


Casuisticas:

# Vizualizar /compra_detalle?orden={orden_id}&token={token} ? Me parece que esto es porque asi trabaja PHP porque no se entiende para que necesitas el token si se supone que tu usuario esta logueado por ende cuando tras el detalle de la compras por id vas a filtrar por el id de usuario de la current session
En este caso el token como no lo vas a almacenar en la DB, vas a guardarlo en sessionStorage del browser para hacer la valicacion contra lo que tengas
almacenado ahi. A diferencia de las casuisticas anteriores el valor del token lo almacenabas en alguna tabla de la DB por eso no hacia falta 
guardar el sessionStorage porque la validacion del token la hacias contra el valor almacenado de token en la tabla de la db.


# FRONTEND
:) Para debuguear llamadas http de servicios de httpclient en angular podes hacerle un tap(data = > console.log(data))
:) Los componentens que tengan un @Input() ponelos como ChangeDetectionStrategy.ONPUSH. Si no le pones ONPUSH cualquier evento producido en toda tu app va a 
triggerear re-crear tu componente aunque no haya cambiado el valor de @Input(), entonces es poco performante y te va a dar comportameintos indeseaods. Poniendo
a dicho componente ONPUSH es como decirle a angular ey sacá del arbol de deteccion de cambios a mi componente y yo te voy a avisar explicitamente cuando tengas que refrescar un cambio. Cuando usas en angular changedetectionstrategy.ONPUSH solo va a refrescarse el template html por eventos de usuarios como clicks en pantalla, input de teclado (igualmente buscar por bien por que otros eventos se repinta la pantalla), pero lo que va a pasar con ONPUSH es que los cambios de estados hechos por vos (ej asignacion de una variable dentro de un suscribe de rxjs) no va a hacer triger para repintar la pantalla y no vas a ver el cambio. Este caso se dio en el componente password-change-confirm que tiene un @Input() email, como vos no lo tenias a dicho componente con estrategy ONPUSH entonces hacias cualquier accion en tu app (ej ir a la pantalla de perfil-usuario)  y veias como el ngOnInput de password-change-confirm se ejecutaba es decir se recreaba el componente y nada que ver porque ni si quiera estabas en esa pantalla. Entonces la solucion fue ponele ONPUSH a password-change-confirm y le inyectaste private cdr: ChangeDetectionRef,
y cuando necesitabas actualizar la pantalla porque cambio el estado de la variable dentro del suscribe llamas a this.cdr.markForCheck() y con eso le indicas a 
manualmente angular que cuando termine de ejecutar el suscribe haga una deteccion de cambios para asi se actualice en html el estado de tokenValidationSuccess.
:) Los  atributos name de cada input <input name="XXX"> del formulario es bueno ponerlos aunque (angular no los requiera para el propio funcionamiento del formgroup) porque chrome al detectar el name del input te da la opcion de autocompletar con valores de formularios que hayas cargado anteriormente que tengan el mismo attribute name del elemento input.
Si no le pones el atributo name a los input chrome no te va a dar el menu contextual de autocompletar.
maneje los estados confirmation o el que sea. es como si dejas un componente signup mas generico
:) Operador de fusión nula (nullish coalescing operator) (??) versus Operador lógico OR (||):
EJ:
value ?? 0: devolvera 0 si value es null o undefined
value || 0: devolvera 0 si value es null o undefined o un valor falsy (ej de falsy: por ejemplo, nulo, indefinido, cero, cadena vacía, etc.)
:) Es util solicituar un username (Nombre de usuario) (podes llamarle Alias en el Front end peror internamente en be es el username) para poder
usarlo como valor para ver el perfil de usuario /perfil/{username}
:) Configura variables de entorno en tu servidor de implementación y accede a ellas en tiempo de ejecución. Angular puede consumir variables de entorno mediante el servicio process.env
:) Muy importante relacionado a creacion de componentes realizado automaticamente por angular. (Que te trajo problemas porque recreaba el componente
de password_reset_confirmation y te lo mostraba en pantalla despues de hacer el login :s) Esto es lo que dice chat gpt:
Para evitar recreaciones innecesarias:
- 1. Change Detection Strategy: cuando tengas un componente que tenga un @Input ponele OnPush  (Esto ya lo sabias)  Si los datos de entrada no cambian con frecuencia, esta estrategia puede ayudar a reducir la sobrecarga de detección de cambios.
- 2. Desuscribite de los observables custom en el ngOnDestroy . (custom me refiero a los que vos crees a mano por ej: los BehaviorSubject que creaste
en los servicios para almacenar en cache data y hacer una unica consulta y emitir la respuesta a todos los componentes que esten suscriptos
asi no hacer una llamada propia por cada componente que se vuelve poco performante, recorda que los observables que retorna HttpClient son de 
un solo uso por ende no hace falta desuscribirse) (Recorda que si no te suscribis manualmente y lo usas con el pipe async dentro de tu componente
el mismo pipe gestiona la desuscripcion pero si te suscribis usando el metodo suscribe() en el ts tenes que usar el unsubscribe en el ngDestroy)
Teniendo en cuenta estos dos puntos quiere decir que supone que tenes un componente X, dicho componente tiene una propiedad @Input(), 
en caso de no usar ONPUSH cada vez que se cambie en tiempo de ejecucion el valor del @Input() esto va a hacer recrear el componente en caso que no este creado y repintarlo en pantalla aunque no lo necesites. Lo mismo para el punto 2 que fue donde tuviste el problema tenias esto en el ngOnInit: 
 ngOnInit(): void {
    console.log("PasswordChangeConfirmComponent created");
    ....
    this.userService.user$
    .subscribe(user => this.loggedUser = user);
  }
Y te habias olvidado de hacerle un unsuscribe() en el ngDestroy del obserbable retornado por this.userService.user$ (Siendo que user$ viene de un 
BehaviorSubject<UserDTO>) por ende cuando cada vez que te logueabas en la pantalla login, como en dicho componente de login se llama al servicio de user que actualiza el valor de dicha user$, entonces angular recreaba el componente PasswordChangeConfirmComponent porque al cambiar al llegarle nueva
informacion de user$ es decir al estar suscripto eso hacia que angular recree el componente de passwordchange que nada que ver. (lo solucionaste 
poniendo un unsuscribe en el ngOnDestroy)
:) El orden en el que defindas tus interceptores importa! Segun el orden se van a ir ejecutando cada uno.
:) Para propagar un error que ingreso por un catchError tenes que usar la funcion throwError
:) Para cancelar (es decir llevarlo al estado completed) un observable en rxjs podes usar la constante EMPTY
:) Para saber cuando un observable se completo o finalizó existe la funcion finalize() por ej en tu proyecto te permitio saber cuando termino
de procesarse la solicitud de refresh_token para poder trabajar de manera serial la soliciitud de refresh_token. (finalize(() => this.tokenService.isRefreshingToken = false),)
:) Donde guardar el token jwt en el FE? La mejor opcion es guardarlo en coockies con expiracion y el httpOnly, nunca guardes los tokens jwt 
en ningun tipo de storage como sessionStorage o localStorage porque un hacker con codigo malicioso mediante js podria extraer facilmente los datos 
del usuario accediendo a dichos stores, en cambio si lo guardas como cookie en el navegador el hacker no tiene manera de extraer la info porque
queda guardada para siempre en el navegador del cliente y segura por la opcion httpOnly que no se puede acceder por js para ser modificada.
:) (NO RECOMENDADO) Para codificar a nivel de browser a base64 un password por ej: en js y ts window.btoa() y para decodificar window.atob() 
(Ojo en caso de estar sobre una app server side de node recomeniendan usar Buffer pero no es tu caso)
Thoose deprecations are a false alert and cames from the nodeJS api, that is needed by angular tools but will not be present in browser context. You must use btoa/atob in browser platform an Buffer in server platform. Using window.atob instead of atob is a workaround to get rid of it. 
(RECOMENDADO) Usar alguna libreria de hashing de password como Bcryptjs, el backend puede directamente encriptar el hashing que le envia el FE con el 
algorimto + secret del backend dicha hash sin necesidad de desencriptarlo previamente. Tambien es util para cuando necesitar guardar datos en sessionStorage o
localStorage se puede usar esta funcion de encriptacion para encriptar y desencriptar para cuando necesites leer y escribir en storages.
Recorda de agrega key y iv para que no genere valores random el hash la liberia crypt-js
key = CryptoJS.enc.Base64.parse(key);
iv  = CryptoJS.enc.Base64.parse(iv);
:) Si un usuario bloquea las cookies a nivel de navegador directamente ni quiera podes usar ni sessinStorage y localStorage te bloquea todo tipo de storage el hecho de
que el usuario bloquea las cookies a nivel de configuracion del browser. Y por otro lado una alternativa que le podes dar al usuario es un modal de consent de almacenamiento de cookies, en caso de que diga que no le almacenas la data en algun storage (sessionstorage o local), y si te dice que si usas las cookies; eso si siempre y cuando no bloque las cookies  anivel de navegador porque te va a quedar inutilizable el sitio.
:) Herramientas que vienen en angular under the hood:
- Transpilador TS/JS: (configurable via tsconfig.js) es el encargado de transpilar Typescript a JS para que sea ejecutable en browser.
- Webpack: te va a permitir armar archivos bundle (app.bundle.js, app.bundle.css) para asi desplegar pocos archivos en el server y cuando un usuario ingrese a tu app la cantidad de request para buscar cada tipo de archivo ahora es solo 1 request por tipo de archivo porque el contenido esta todo metido dentro de cada bundle.
Ademas realiza minificacion y compresion.
- Node.js lo utilizamos para poder gestionar dependencias gracias a su npm y para tener un servidor en desarrolllo para probar en vivo que detecta cambios
y recompila la app automaticamente. Recoda que npm es el sistema de gestión de paquetes por defecto para Node.js
- Recorda que segun la version de angular que estes trabajando necesitas una version de node en especifico, el matcheo de versiones entre version de angular
y version de node es tajante es decir si intentas usar una version de node 8 y tu app es angular 11 no te va a funcionar directamente. Revisa la documentacion
oficial tiene una tablita de compatibilidad entre versiones (recorda que nvm (node version manager) es una cli que te permite tener instalado varias versiones de node en tu maquina al mismo tiempo y poder gestionarlas, tip: si ya tenes instalado nodejs y despues le instalas a tu compu nvm consejo desintala el nodejs que tenes instalado previamente para que no haga conflicto). Para instalar angular globalmente en tu SO con npm install -g @angular/cli y comando ng v para ver
si se instaló correctamente.
:) Predicado !!:  '!!data' equivale a 'data != null'
:) Comando para crear modulo con el routing module: ng g m nombre_modulo --routing 
:) platformBrowserDynamic versus platformBrowser. Son Packages que se encargan de boostrapear una aplicacion angular, ambos son necesarios si y solo
si tu app angular va a ser ejecutada en un browser. Es como la caja de herramientas que le da angular a tu app para que se pueda ejecutar en un browser.
La diferencia clave entre los dos esta en que, supongamos que un usuario ingresa a tu app tienda en un browser, si tu app esta configurada con:
-  platformBrowser va a ser usada una compilacion AOT de tu app, es decir, el browser recibe una version precompilada de tu app, por ende recibe un paquete mas pequeño de tu app. Ventaja: paquete mas pequeño de tu app y la compilacion final se vuelve mas rapida en el browser porque parte de una version precompiled. Desventaja: no es flexible.
- platformBrowserDynamic usa compilacion JIT de tu app, es decir la tarea de compilación recae al 100% en el browser del cliente. La ventaja y desventaja es opuesta
a la anterior, la ventaja de la flexibilidad que te aporta este es que podes inyectar valores de llamadas http a tu backend como secrets al momento de que se ejecuta.
:) Inyectar configuracion asyncrona via HTTP lo podes realizar con el token APP_INITIALIZER, en useFactory le pasas una funcion que queres que se ejecute antes de iniciar la app tuya.
:) Tema encriptacion: (PD: HTTPS using SSL ya encripta todo el canal de comunicacion entre tu fe y be no hay neceidad de reinventar la rueda encriptando manualmente la
data enviada al be, ademas el computo de encriptar y desencriptar es muy alto) 
- Algoritmos de encriptacion simetricos (ej AES) no deben ser usados para intercarmbar info entre el FE y BE porque dichos algoritmos usan una unica llave compartida. 
y lo que este dentro de js no es seguro ya que es el codigo que va a recibir el browser. 
- Algoritmos de encriptacion asimetricos (ej RSA) usan un par de llaves una publica y otra privada. Con la llave publica el FE va a encriptar data a enviar al BE
y el BE va a usar la llave privada para desencriptarla.
:) Pagina para buscar el peso de dependencias npm que va a impactar en tu bundle final de la app: https://bundlephobia.com/
:) Recorda que los imports no utilizados te afectan el tamaño del bundle final, entonces revisa no dejar imports sin usar en los archivos. Aca no es como Java.
:) Recorda que en el array de 'imports' de cada modulo @NgModule es un (espcifica una carga EAGER de modulos)
:) Sin usar lazy modules (recordando que main.js es el empaquetado de tu aplicacion es lo que se le va a transferir al usuario al browser de tu app):

:) EJEMPLO REAL DE TU APP USANDO Y SIN USAR LAZY LOAD MODULES:
Fijate como el tamaño del bundle total final paso de 857.61 kB a 803.94 kB. No es mucho porque seguramente tu app tenia pocos componentes pero en una app mas grande seguramente esto es exponencial. Recorda que el archivo main.js es el bundle que se le envia al browser del cliente la primera vez que ingresa a tu app, es decir 
cuanto mas grande es el archivo main.js mas le va a tardar cargar la pagina.

BUNDLE BUILDEADO SIN LAZY LOAD (TODOS LOS COMPONENTES IMPORTADOS EN NGMODULE)
Initial Chunk Files           | Names                            |  Raw Size | Estimated Transfer Size
main.767d8b4e2a3f153c.js      | main                             | 748.02 kB |               172.09 kB
                              | Initial Total                    | 857.61 kB |               191.61 kB (incluye polyfills, runtime y styles) 

BUNDLE BUILDEADO CON LAZY LOAD DE MODULOS
Initial Chunk Files           | Names                            |  Raw Size | Estimated Transfer Size
main.232ed8e2a134f058.js      | main                             | 692.54 kB |               161.62 kB   
                              | Initial Total                    | 803.94 kB |               181.93 kB (incluye polyfills, runtime y styles)        

Pasos creacion modulo y crear rutas lazy:
1- Crear modulo y componentes
2- Definir rutas de los componentes dentro del modulos feature (children...forChild())
3- Importar el feature-routing-module al app-module.ts
4- Definir ruta del modulo y definir lazy load en app-routing.ts
Listo: Angular te va a crear un archivo por cada modulo src_app_auth_auth_module_ts.js y en el lazy load va a ir llamando al server
de manera lazy para traer la info de cada modulo

:) Notas sobre estructura de carpetas escalable: el modulo core debe ser importado una unica vez en tu app por ende se crea una guard para ello, fijate en codigo de la app hay una clase guard que se encarga eso.

# BACKEND
:) La generacion del token (aleatorio y basasdo en tiempo) para realizar cualquier accion es un MUST generarlo en el backend para mayor seguridad y no en el FE
:) Almacenar el token de activacion en la db (o en un redis con ttl) es para que si el cliente le da al link de nuevo activar cuenta le reenvias el mismo token que ya generaste y no otro que si no me equivoco generar esos tokens es una operacion costosa computacionalmente. Una mejor implementacion de token es agregarle expiracion y no almacenarlos en la db sino en un redis asi cuando expira se elimina, y no ocupas espacio al dope en la db si un usuario por ej crea un request para cambiar el password y nunca lo cambio vas a tener el token de por vida al pedo
:) Desventaja de enviar confirmaciones como link de activacion los email que las url de activacion van a quedar guardadas en el historial de navegacion,
por ende esta quedando guardado un token que ponele el usuario ya lo uso pero un hacker teniendo acceso a su historial por ende a dichos tokens lo podria descifrar de alguna manera que sepa por ende por lo que vi en las tiendas como mercadolibre o ebay utilizan el mecanismo de enviar 4 numeros random para confirmar email al email registrado y tambien para confirmar el celular envian 4 numeros random al celu y para en caso de el flow de olvido de passowrd mercadolibre te envia 6 numeros aleatorios. Otra desventaja es que cuando adquiere importancia una tienda como mercadolibre aparecen los hackers con email phisihng, y hace email identicos de activacion y el boton redirecciona a una pagina hacker entonces como mecanismo de seguridad para que un usuario no sea engañado haciendo clik en hipervinculos
seguro tomaron esta medida de enviar random numbers para confirmacion y que el usuario copie y pegue sin salir de la pagina de mercadolibre donde esta posicionado.
Creo que otra ventaja de que sea numerico es que es muy menos costoso las comparaciones o calculos ya que son numeros nomas de pocas cifras, encambio un token con 30 o 40 chars o mas y mucho mas si hay que descifrar algun token hash.
:) @Component vs @Service. Si bien @Service hereda de @Component. Las clases anotadas con @Service son clases con logica de negocio mientras que si tu clase
tiene logica general por ejemplo generacion token numerico entonces deberia de ser anotada con @Component porque no es una logica especifica del flujo de negocio.
:) Cuando le definis un Logout handler a Spring Security es como le estas diciendo ey spring security ejecuta este handleer que te voy a definir
con cierta logica cuando el usuario haga logout y no lo redirijas a ningun controller. Spring Security tiene autoconfigurada la ruta '/logout' como url de logout pero lo podes configurar manualmente para adaptarlo a tu gusto ej: logoutUrl('api/auth/logout')
:) Cuidado cuando ves en spring secuirity te cree un objeto de authenticacion de tipo anonymoususer acordate de desabilitarlo en la configuracion de spring security con anonymous.disable()
:) En JavaMailSender el setFrom En el caso de gmail el username coincide con el FROM, para algun otro provedor SMTP puede haber casos que el username sea ej myusername y la casilla sea myusername@provider.com
 Por mas que se lo modifiques el FROM manualmente no se va a cambiar por esto: Esto se debe a que muchos proveedores de correo electrónico, incluido Gmail, exigen el uso de la cuenta autenticada como remitente para prevenir el suplantamiento de identidad (spoofing) en los correos electrónicos.
 Como workaround messageHelper.setReplyTo(new InternetAddress(this.emailFrom)); // El cliente que intente darle reply va a aparecer el email noreply@tienda.com
:) Recorda que los Filters se ejecutan antes de llegar al controller por ende, las excepciones que se lancen el los Filters (ej: JwtFilter) nunca jamas de los jamaces van a poder ser capturadas por un controlleradvise ya que ni si quiera el request llegó al controller recien esta en el Filter, asi que tenes que manejar las excepciones dentro del Filter explicitamente.
:) OncePerRequestFilter se ejecuta SIEMPRE! indistintamente si pongas un endpoint como publico, igualmente va a pasar por ahi el request por eso tenes que controlar bien la implementacion del Filter custom que pongas de OncePerRequestFilter
:) En backend, Como decodificar validando la firma (en caso de que tu app haya sido la que generó dicho token vas a poder validar la firma) 
y como decodificar sin validar la firma (en caso de que el token sea generado por otra app de tercero y vos solo consumas el token generado por ella)
Nota: token decodificado quiere decir que a el string pasa a ser un objeto que podes acceder al subject, scopes, etc. Y codificar un jwt es
lo inverso llevar dicho objeto a representacion de string de cadena ey...
Usando la libreria java-jwt:
# Caso 1: Decodificar + Validar (valida firma y expiracion por defecto):
Algorithm algo = Algorithm.HMAC256(this.secret);      // 1. Definis el algorimo y secret que usaste para generar la firmar el jwt
JWTVerifier jwtVerifier = JWT.require(algo).build();  // 2. Le seteas el algoritmo anterior al metodo require
DecodedJWT decodedJWT =  jwtVerifier.verify(jwt);     // 3. El metodo verify valida el token si es valido o no y retorna un jwt decodificado

# Caso 2: Solo Decodificar:
DecodedJWT decodedJWT =  JWT.decode(jwt); // Decodifica el token pero sin validar la firma, ni expiracion (la expiracion no lo sé si no la valida).

:) tiempo de expiracion apropiados para tus tokens jwt para los access entre 15 y 30 minutos y los refresh pueden durar dias.
:) Para leer cookies con spring tenes la anotacion @CookieValue("myKey", defaultValue="myDefault") y para crear cookies con spring esta 
la clase ResponseCookie.class que tiene un builder.
:) CORS: Es una proteccion BE, que define solo dominios validos que pueden consumir mi BE.  El ejemplo claro es server tomcat corre sobre localhost:8080 y una
app angular corre sobre localhost:4200, como el spring security al tener habilitado cors por defecto cuando recibe un request de un origin con dominio diferente
te dice no muchacho no tenes permitido consumir mi BE. Configurando cors en spring es como armar una lista de dominios de origen whitelist que te pueden impactar a tu backend y no cualquier dominio de origen.
:) La vulnerabilidad a CSRF vulnerability depends de como el cliente es decir tu app angular almacene y guarde el jwt y envia en cada consulta. Aplicando
las mejores practicas no hay forma de que roben el token, la mejor practica es almacenar el access_token jwt dentro de una variable de la aplicacion angular, es decir el valor del jwt se termina almacenando en la memoria RAM del usuario.
This will save the token in the browser’s memory, and it will be available only for the current page. It’s the most secure way: CSRF and XSS attacks always lead to opening the client application on a new page, which can’t access the memory of the initial page used to sign in. However, our user will have to sign in again every time he accesses or refreshes the page.(es decir sin exponerla en cookies ni ningun tipo de storage como localstorage o sessionstorage) y con eso un atacante no tiene posibilidad de robo de ese token ya que esta embebido dentro de la app del browser del cliente (ademas esta como httpOnly asi no puede ser leida por js). Por otro lado el refresh_token jwt si puede viajar por cookies porque unicamente sirve para crear un nuevo access_token jwt y nada más, es decir por mas que se roben un refresh_token no sirve para realizar acciones dentro de tu app distas de refrescar un token. Nota: La implementacion de guardar tokens en localstorage o session storare son vulnerable to XSS attacks: a malicious JavaScript code can access the browser storage and send the token along with the request. In this case, we must protect our application (https://www.baeldung.com/spring-prevent-xss). En este video muestra la mejor practica de almacen de jwt access y refresh si bien usa
otro lenguaje pero es bastante claro: https://www.youtube.com/watch?v=53VBlv7K-BI
:) Docker Compose vs Kubernetes: Ambos sirven para orquestar, pero "Docker Compose" es una herramienta diseñada para la orquestación de contenedores en un entorno de desarrollo local o en un solo host, mientras que Kubernetes es plataforma más robusta que se utiliza para la orquestación de contenedores a nivel de producción en clústeres, ademas cumple otras funciones como balanceo de carga,  escalabilidad, gestión de recursos y alta disponibilidad que van más allá de las capacidades ofrecidas por Docker Compose. Docker compose es como el hermano menor de todo lo que podes llegar a hacer con kubernetes.
:) Ejemplo con OPENAPI usando OpenAPI-specific annotations
@Operation(summary = "Get a book by its id")
@ApiResponses(value = { 
  @ApiResponse(responseCode = "200", description = "Found the book", 
    content = { @Content(mediaType = "application/json", 
      schema = @Schema(implementation = Book.class)) }),
  @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
    content = @Content), 
  @ApiResponse(responseCode = "404", description = "Book not found", 
    content = @Content) })
@GetMapping("/{id}")
public Book findById(@Parameter(description = "id of book to be searched") 
  @PathVariable long id) {
    return repository.findById(id).orElseThrow(() -> new BookNotFoundException());
}

* Componentes de k8s:
- POD: En k8s no trabajamos con containers directamente sino con unidades funcionales que envuelven a ellos llamada PODS. Un pod puede contener 1 o mas containers.
Y cada POD esta asociado a una unica direccion IP que le permite realizar comunicacion intra-pods. (Hasta el momento solo viste ejemplos de asociar una app spring boot (1 container) por cada pod es decir una relacion 1 a 1, para las casuisticas que manejas vos creo que siempre vas a usar este modelo porque recorda que cada pod tiene una IP es decir para poder comunicarte con el container docker dentro del POD k8 va a usar esta IP, es decir como minimo vas a manejar 1 pod para el BE,
1 pod para el FE y 1 pod para una DB, no se en que caso podria aplicar el caso de que existan mas de un container docker dentro de un mismo pod)
- NODE: es una unidad virtual o fisica que envuelve a los pods anteriores. Un node puede contener 1 unico pod o N pods.
- CLUSTER: es una unidad virtual o fisica que envuelve a los NODS. Un custer puede contener 1 unico NODE o N NODE. K8s clusters estan formados por un
master node y 1 o N workers nodes. Estos nodos pueden ser computadoras fisicas o virtual machines.
- REPLICATION CONTROLLER (REPLICA SET): cumplen la funcion de replica o backup de tus pods en caso de que crasheen.
- SERVICES: Funciones de los SERVICES:
1) MANEJA EL SERVICE DISCOVERY: supone que tenes un pod para FE y otro pod para BE, si el pod del BE crashea el replica set te regenera un nuevo pod para BE pero
le asigna una nueva IP a dicho POD por ende para ambos pods FE y BE puedan seguir comunicandose aparece el componente SERVICE.
Cada pod puede estar asociado a un unico SERVICE, o multiples PODS pueden compartir un mismo SERVICE configuracion los labels y selectors.
Un SERVICE le provee al POD un IP STATIC y un nombre DNS, gracias a esto por mas que crashee el pod y sea recreado con otra IP al estar asociado a un
service la comunicacion intra-pod se hace a traves del nombre DNS de cada uno.
2) LOAD BALANCING: cuando 1 service tiene mas de un pod asociado es capaz de hacer load balancing entre ellos basado en el enrutamiento.
Tipos de SERVICES:
- Cluster IP
- NodePort
- LoadBalancer
Estos services difieren en como vas a exponer tus pods internamente o externamente, y cómo vas a manejar el trafico.
- DEPLOYMENTS: son usados para administrar PODS. Podes escalar tu app aumentando la cantidad de PODS corriendo o bien podes actualizar tu app 
que se encuentra ejecutandose usando el objeto DEPLOYMENT.
Podes usar este comando o bien podes usar el archivo "deployment.yml" file:

(ej: kubectl create deployment mi_deployment --image=*** --port=8080 --replicas=4)
- SECRETS & CONFIGMAPS: son dos componentes usados para guardar info sensible que usen los pods. Se pueden compartir entre pods.
La diferencia entre ellos dos es que en SECRET la info se va a guardar encriptada versus CONFIGMAP en texto plano. (caso de uso: si tenes que conectarte a una db
en el componente SECRET unicamente vas a guardar el password de la db y en CONFIGMAP el username y el connection string)
- ETCS: es un db de clave valor. Almacena toda la configuracion del cluster. Tambien almacena los SECRETS Y CONFIGMAPS. (LIMITE MAX 1MB)


:) Comandos usados:
# Docker
*Generar la imagen docker: docker build --tag estebanbri/tienda:1.0 .
*Crear y ejecutar el contenedor: docker run --name tienda -d -p 8080:8080 --env-file .env estebanbri/tienda:1.0

# k8
*Para instalar minikube: minikube start
*Status de minikube: minikube status
*Pära stopear el minikube cluster: minikube stop
*Para eliminar el cluster local y eliminar la vm y todos los archivos asociados: minikube delete
*Para obtener info del cluster: kubectl cluster-info
*Para obtener info del node: kubectl get node (minimuke es un single node (el name del node tambien es minikube en este caso) cluster)
*Para darle permiso a k8s para que pueda leer tu repositorio de imagenes docker: "minikube docker-env" y luego copias y pegas la ultima linea y listo. Otra manera mas simple es simplemente ejecutar eval $(minikube docker-env) y listo el pollo.
*Para crear un deployment object via command line (recorda que para que puedas correr tu contenedor de tu imagen dentro de un pod necesitas definirlo en un deployment object, es decir aca es donde ocurre la magia con este comando o con el deployment file tu imagen docker va a terminar dentro de un pod): create deployment tienda_deployment --image=itienda:latest --port=8080
*Para crear un deployment via file: kubectl apply -f nombre-del-archivo.yaml
*Para conocer los deployments que existen ya creados: kubectl get deployments
*Para conocer todo el detalle de un deployment (ver las replicas etc): kubectl describe deployment tienda_deployment
*Para conocer el status de tus pods: kubectl get pods
*Para ver el log de un pod: kubectl logs tienda_pod
*Para nuestra app tienda pueda ser accedida desde fuera del cluster, via URL por ej es decir que puedas ir a un browser y meter localhost:8080 y se ejecute tu container, tenemos que crear un objeto SERVICE. Para crear un SERVICE tenemos que exponer nuestro current deployment con un tipo de servicio (Service Type) especifico. Comando con tipo de serivico NodePort: kubectl expose deployments tienda_deployment --type=NodePort
(Trafico -> SERVICE (NodePort) va a redireccionar a --> POD)
Ese comando para crear el servicio no lo vas a usar en la vida real, porque es mas facil definir el archivo yml y ejecutarlo:
kubectl apply -f mi_service.yaml
y mejor dicho ni te conviene definir el service del deployment en un archivo separado del deployment porque en realidad es algo que irian juntos en el mismo archivo, es decir tienen una relacion directo, en cambio por ej tiene mas sentido definirlos en archivos separados a los objetos configmap y secret ya que esos son algo externo de los pods en si. 
*Minikube dashboard: minikube dashboard. (Si intentas eliminar un POD fijate que te lo recrea k8 automaticamente)
*Comando  Para eliminar un service: kubectl delete service tienda_service
*Comando  Para eliminar un deployment: kubectl delete deployment tienda_deployment
*Comando Para conocer la ip NODE (util para cuando usas un SERVICE TYPE=NODEPORT y lo creas con yml file): kubectl get nodes -p wide
(en el caso de que uses minikube para las pruebas como minkube usa un unico NODE la IP de minikube y la IP del node coinciden es decir si ejecutas
el comando minikube ip va a ser la misma IP del node). Y el valor de puerto te lo provee el objeto SERVICE que actua de capa abstracta para interactuar con tus PODS.
*Para conocer los service creados: kubectl get service
*En caso de que tengas un unico pod podes exponerlo con la url del service propiamente dicha para poder accederlo via url: minikube service tienda_service --url
*Para codificar los valores a base 64 para ponerlos en SECRET: echo -n 'myvaloracodificar' | base64

# Paso a paso para configurarar y levantar el cluster de minikube:
https://medium.com/@javatechie/kubernetes-tutorial-setup-kubernetes-in-windows-run-spring-boot-application-on-k8s-cluster-c6cab8f7de5a


# Fuentes:
https://www.youtube.com/watch?v=nNnYNAaKQlc&list=PL-Mlm_HYjCo-Odv5-wo3CCJ4nv0fNyl9b&index=12 (Tienda online en php)
https://www.youtube.com/watch?v=3q3w-RT1sg0 (jwt access token y refresh en frances)
https://www.youtube.com/watch?v=n65zFfl9dqA  (jwt access token y refresh ejemplo practico en frances)



# Tienda

## Starup K8s
```
./entrypoint-cluster-k8s.ps1
```

### Opcional Instalar Consul via helm (REQUIERE TERMINAL COMO ADMINISTRADOR)
Usando una terminal con privilegio de admin:
```
powershell -ExecutionPolicy Bypass -File .\k8s\consul\instalar_consul.ps1
```
Nota: ver al final del readme como inyectar el sidecontainer envoy-proxy a tus containers.

# Para ver si levanto todos los objetos de k8s correctamente
``` 
kubectl get all --all-namespaces
``` 

#### 4.2 Habilitar Ingress
``` 
minikube addons enable ingress
``` 
```
minikube tunnel
```

#### Para validar la que db se creo correctamente dentro del POD:
Accedemos via bash al pod:
```  
kubectl exec --stdin --tty --namespace=tienda mysql_pod_name -- sh 
```  
Nos logueamos:
```  
mysql -h mysql -u root -p
```
```  
show databases;  
```
```  
use tiendadb; 
```
En este punto ya podes hacer el insert de la configuracion del fe.
```  
show tables;
```

# En caso de que hayas agregado Consul ejecutar lo siguiente
Para visualizar el dashboard de consul:
``` 
kubectl port-forward service/consul-ui -n consul 8500:80
``` 

## Pasos para crear certificados auto-firmado (self signed certificate)
Un certificado autoafirmado es un certificado SSL que no ha sido validado por una autoridad de certificación (CA).
Esto es lo que significa ser auto firmado.

### Pasos para instalar OpenSSL en Windows
1. Descargar openssl de la pagina https://slproweb.com/products/Win32OpenSSL.html
2. Agregar variable de entorno OPENSSL_HOME y actualizar variables de entorno PATH
```
OPENSSL_HOME: C:\Program Files\OpenSSL-Win64
PATH: C:\Program Files\OpenSSL-Win64\bin
```
3. Validar si se instaló correctamente ejecutando el comando:
```
openssl -v
```

```
openssl req -x509 -nodes -days 365 -newkey rsa:2048     -out self-signed-tls.crt      -keyout self-signed-tls.key      -subj "/CN=tienda.com/O=self-signed-tls"
```

### Crear el Kubernetes secret con los datos del certificado

```
kubectl create secret tls self-signed-tls --key self-signed-tls.key --cert self-signed-tls.crt
```

### Crear el archivo ingress (asegurate de tener ingress controller ya instalado)
Nota: el ingress controller por defecto es nginx pero es configurable para cambiarlo a traves de cambiar el valor de ingress.class (traeffik)
```
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: demo-ingress
spec:
  tls:
  - hosts:
    - xyz.com # Replace with your cluster DNS name
    secretName: self-signed-tls
  rules:
  ...
```
### Check by hitting the URL
(Nota: en caso de no tener un nombre de dominio agrega la entrada al archivo host asi podes desde el navegador ingresar tienda.com: 	   
```
127.0.0.1   fe-tienda.com    # minikube
127.0.0.1   bff-tienda.com    # minikube
```

Ir al navegador a 
- WebApp : https://fe-tienda.com/
- Swagger: # https://bff-tienda.com/swagger-ui/index.html
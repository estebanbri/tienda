# Tienda

## Starup K8s
./entrypoint-cluster-k8s.ps1

### Opcional Instalar Consul via helm (REQUIERE TERMINAL COMO ADMINISTRADOR)
Usando una terminal con privilegio de admin:
```
powershell -ExecutionPolicy Bypass -File .\k8s\consul\instalar_consul.ps1
```
Nota: ver al final del readme como inyectar el sidecontainer envoy-proxy a tus containers.


#### 4.2 Ejemplo exponiendo el servicio ClusterIP con Ingress
Habilitá ingress en minikube:
``` 
minikube addons enable ingress
``` 
``` 
.../tienda/k8s/ingress> kubectl apply -f .
``` 
Para conocer la ip asignada a ingress ejecuta el siguiente comando y revisa la columna ADDRESS que es la IP:
``` 
kubectl get ingress -n tienda
```
o lo que es lo mismo, ya que por mas que tengas N nodos siempre uno va a ser tu nodo entrypoint el cual contenga el
ingress entonces quiere decir que tanto el ingress como el node van a compartir la misma IP entonces podes
```
minikube ip
``` 
Listo ahora ya podes acceder a tu aplicacion via Ingress:
```
http://ingressIP:puertoConfiguradoEnIngressDefinitionFile
``` 
o bien
```
http://nodeIp:puertoConfiguradoEnIngressDefinitionFile
``` 


# Check that it's running
``` 
kubectl get pods --namespace=tienda
``` 
o lo que es lo mismo de definir el namespace con la short form
``` 
kubectl get pods -n tienda
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

# Extras
```
minikube dashboard
```
```
minikube delete
```

# Opcion 1: si no estas usando minikube
# Open bff-tienda
```
kubectl port-forward POD_NAME -n tienda  8080:8080
```
# Open elasticsearch
```
kubectl port-forward POD_NAME -n tienda 9200:9200
```
# Open zipkin
```
kubectl port-forward POD_NAME -n tienda 9411:9411
```
# Open kibana
```
kubectl port-forward POD_NAME -n tienda 5601:5601
```

# Opcion 2: si estas usando minikube, te provee un tunel
# Open bff-tienda
```
minikube service bff-tienda-service -n tienda
```
# Open elasticsearch
```
minikube service elasticsearch-service -n tienda
```
# Open zipkin
```
minikube service zipkin-service -n tienda
```
# Open kibana
```
minikube service kibana-service -n tienda
```

Fuente: https://medium.com/@javatechie/kubernetes-tutorial-setup-kubernetes-in-windows-run-spring-boot-application-on-k8s-cluster-c6cab8f7de5a

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
  - host: xyz.com # Replace with your cluster DNS name
    http:
      paths:
      - backend:
          service:
            name: demo
            port:
              number: 8080
        path: /demo
        pathType: Prefix 
```

### Abrir tunel desde localhost al ingress
```
minikube tunnel
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


## Pasos para configurar cert-manager 
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.13.3/cert-manager.yaml

# Para ver si levanto todos los objetos de k8s correctamente
kubectl get all --all-namespaces
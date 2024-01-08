# Tienda

## Starup K8s
```
./entrypoint-cluster-k8s.ps1
```

## Para ver si levanto todos los objetos de k8s correctamente
``` 
kubectl get all --all-namespaces
``` 

## Habilitar Ingress
``` 
minikube addons enable ingress
``` 
```
minikube tunnel
```

## Para validar que se haya creado la db mysql correctamente:
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

## Check by hitting the URL
(Nota: en caso de no tener un nombre de dominio agrega la entrada al archivo host asi podes desde el navegador ingresar tienda.com: 	   
```
127.0.0.1   fe-tienda.com    # minikube
127.0.0.1   bff-tienda.com    # minikube
```

Ir al navegador a 
- WebApp : https://fe-tienda.com/
- Swagger: # https://bff-tienda.com/swagger-ui/index.html

## Opcional Instalar Consul via helm (REQUIERE TERMINAL COMO ADMINISTRADOR)
Usando una terminal con privilegio de admin:
```
powershell -ExecutionPolicy Bypass -File .\k8s\consul\instalar_consul.ps1
```
Nota: ver al final del readme como inyectar el sidecontainer envoy-proxy a tus containers.

Para visualizar el dashboard de consul:
``` 
kubectl port-forward service/consul-ui -n consul 8500:80
``` 
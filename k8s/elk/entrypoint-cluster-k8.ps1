kubectl create -f ./k8s/elk/elasticsearch-deployment.yaml
kubectl create -f ./k8s/elk/kibana-deployment.yaml
kubectl set env deployments/kibana ELASTICSEARCH_HOSTS=http://elasticsearch-service.tienda.svc.cluster.local:9200 --namespace=tienda
kubectl create configmap beat-manual-config --from-file ./k8s/elk/filebeat.yml --namespace=tienda
kubectl create configmap log-manual-pipeline --from-file ./k8s/elk/logstash.conf --namespace=tienda
kubectl create -f ./k8s/elk/logstash-deployment.yml
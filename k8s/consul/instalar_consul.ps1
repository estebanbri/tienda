# Instalar Helm con Chocolatey
choco install kubernetes-helm --yes

# Agregar el repositorio Helm de HashiCorp (Consul)
helm repo add hashicorp https://helm.releases.hashicorp.com

# Actualizar los repositorios Helm
helm repo update

# Instalar Consul con Helm
helm install consul hashicorp/consul --set global.name=consul --create-namespace -n consul --values ./k8s/consul/helm-values.yml
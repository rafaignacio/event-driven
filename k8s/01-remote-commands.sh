#copiar arquivo de configuracao na maquina local
scp -i <pem file> <username>@<master ip>:~/.kube/config .

#instalar weave antes de qualquer aplicacao
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"

#instalar dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/master/src/deploy/recommended/kubernetes-dashboard.yaml

#verificar pods
kubectl get pods --all-namespaces

#caso queira resetar os nodes
kubectl drain <node name> --delete-local-data --force --ignore-daemonsets
kubectl delete node <node name>

kubeadm reset
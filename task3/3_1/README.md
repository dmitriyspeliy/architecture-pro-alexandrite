# Task3. OpenTelemetry + Jaeger MVP

## Состав

Реализованы два сервиса:

- service-a
- service-b

`service-a` принимает HTTP-запрос и вызывает `service-b`.  
Оба сервиса инструментированы через OpenTelemetry Java Agent.  
Трейсы отправляются в Jaeger через OpenTelemetry Collector.

# Task3 — Tracing (OpenTelemetry + Jaeger)

## Требования

* Docker Desktop (запущен)
* minikube
* kubectl
* Maven

---

## Запуск

### 1. Kubernetes

```bash
minikube start --driver=docker
kubectl get nodes
```

---

### 2. Подключение Docker к minikube

```bash
minikube docker-env --shell powershell | Invoke-Expression
```

---

### 3. Сборка сервисов

```bash
cd task3/3_1/k8s/service-a
mvn clean package
cd ../../../..
docker build -t service-a:latest task3/3_1/k8s/service-a

cd task3/3_1/k8s/service-b
mvn clean package
cd ../../../..
docker build -t service-b:latest task3/3_1/k8s/service-b
```

---

### 4. Jaeger

```bash
kubectl apply -f task3/3_1/k8s/jaeger.yaml
kubectl get pods -n observability
```

---

### 5. Деплой сервисов

```bash
kubectl apply -f task3/3_1/k8s/service-a.yaml
kubectl apply -f task3/3_1/k8s/service-b.yaml
kubectl get pods
```

---

### 6. Проверка

```bash
kubectl port-forward svc/service-a 8080:8080
```

Во втором терминале:

```bash
curl.exe http://localhost:8080
```

Ожидаемый ответ:

```
service-a -> service-b OK
```

---

### 7. Трейсинг

```bash
kubectl port-forward -n observability svc/jaeger 16686:16686
```

Открыть:

```
http://localhost:16686
```

* выбрать `service-a`
* нажать **Find Traces**
* открыть trace

Ожидаемый результат:

```
service-a
  -> service-b
```

---

### 8. Результат

Скриншот трейса:

```
task3/3_1/122058.png
task3/3_1/122107.png
```

# Выбор и настройка мониторинга в системе

## 1. Мотивация

Сейчас проблемы выявляются через жалобы клиентов. Это приводит к:
- потере заказов и контрактов;
- росту сроков выполнения;
- отсутствию понимания, где возникает сбой.

Мониторинг позволит:
- обнаруживать проблемы до клиентов;
- контролировать SLA (расчёт цены, доставка);
- понимать, где застревает заказ;
- принимать решения на основе данных.

---

## 2. Выбор подхода

Используется комбинация:

- RED — для API (shop, CRM, MES)
- USE — для инфраструктуры (БД, RabbitMQ, EC2)
- Бизнес-метрики — для контроля заказов

Причина:
- RED показывает деградацию сервисов
- USE — узкие места ресурсов
- бизнес-метрики — влияние на деньги

---

## 3. Метрики

### 3.1 API (RED)

**Number of requests (RPS)**
- зачем: нагрузка
- labels: service

**Response time (latency)**
- зачем: деградация
- labels: service, endpoint

**Number of HTTP 500**
- зачем: ошибки
- labels: service

---

### 3.2 RabbitMQ

**Number of messages in flight**
- зачем: backlog заказов
- labels: queue_name

**Number of dead-letter messages**
- зачем: потерянные заказы
- labels: queue_name

---

### 3.3 Сервисы

**CPU % (shop, CRM, MES)**
- зачем: saturation

**Memory utilisation (shop, CRM, MES)**
- зачем: риск OOM

---

### 3.4 База данных

**Memory utilisation (DB)**
- зачем: перегрузка

**Number of connections**
- зачем: исчерпание пула

---

### 3.5 Дополнительно (критично)

**Время расчёта цены**
- зачем: основной bottleneck MES

**Количество заказов в очереди**
- зачем: показывает задержки

### Детализация API-метрик

RPS:
- shop_api_rps
- crm_api_rps
- mes_api_rps
- b2b_api_rps
- labels: service, endpoint, method, partnerId

HTTP 500:
- shop_api_http_500_total
- crm_api_http_500_total
- mes_api_http_500_total
- labels: service, endpoint

Latency:
- shop_api_latency_p95
- crm_api_latency_p95
- mes_api_latency_p95
- labels: service, endpoint

---

## 4. План действий

1. Развернуть стек:
    - Prometheus
    - Grafana
    - Alertmanager

2. Инструментировать сервисы:
    - Spring Boot Actuator (shop, CRM)
    - метрики в MES (C#)
    - кастомные бизнес-метрики

3. Подключить инфраструктуру:
    - node exporter
    - DB exporter
    - RabbitMQ exporter

4. Настроить дашборды:
    - API
    - очереди
    - БД
    - бизнес-метрики

5. Настроить алерты:
    - рост очередей
    - ошибки API
    - деградация latency
    - перегрузка ресурсов

6. Ввести on-call процесс

---

## 5. Показатели насыщенности

**CPU > 80% (5 мин)**
- действие: добавить инстанс

**Memory > 85%**
- действие: рестарт + анализ

**DB connections > 90%**
- действие: увеличить пул / оптимизация

**RabbitMQ backlog растёт**
- действие: масштабировать consumers

**Dead-letter > 0**
- действие: тикет + расследование

**Latency > SLA**
- действие: анализ и фикс

**Расчёт цены > SLA (5 мин)**
- действие: масштабировать MES
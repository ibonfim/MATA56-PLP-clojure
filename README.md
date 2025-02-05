# Sistema de Reservas de Datashow com Auditoria (Clojure)

## Introdução
Este projeto implementa um **sistema funcional de gerenciamento de reservas de datashow** com um **log de auditoria** para rastrear todas as ações realizadas. Ele segue o paradigma funcional de Clojure, garantindo **imutabilidade, concorrência segura e programação declarativa**.

## Estrutura do Código

### Banco de Dados e Estado
O sistema utiliza **atoms** para armazenar os dados de forma concorrente e segura:
```clojure
(def reservations (atom {}))
(def audit-log (atom []))
```
- `reservations` mantém o estado atual das reservas.
- `audit-log` registra todas as ações realizadas, permitindo auditoria.

### Processamento de Reservas
A função `process-reservation` verifica se um datashow já está reservado e, se não estiver, adiciona uma nova reserva e registra a ação no log de auditoria.
```clojure
(defn process-reservation [request]
  (let [{:keys [id user time]} request]
    (if (get @reservations id)
      {:status "error" :message "Datashow already reserved"}
      (do
        (swap! reservations assoc id {:user user :time time})
        (swap! audit-log conj {:action "reserve" :id id :user user :time time :timestamp (java.time.LocalDateTime/now)})
        {:status "success" :message "Reservation confirmed"}))))
```
Essa função demonstra **imutabilidade** ao modificar o estado utilizando **swap!**, garantindo que mudanças concorrentes sejam seguras.

### Listagem de Reservas
Para visualizar todas as reservas ativas:
```clojure
(defn list-reservations []
  (if (empty? @reservations)
    (println "Nenhuma reserva cadastrada.")
    (doseq [[id {user :user time :time}] @reservations]
      (println (str "ID: " id " | Usuário: " user " | Horário: " time)))))
```
Isso permite que os usuários verifiquem quais datashows estão ocupados.

### Auditoria de Ações
O sistema mantém um log de todas as ações realizadas:
```clojure
(defn show-audit-log []
  (if (empty? @audit-log)
    (println "Nenhuma ação registrada no log.")
    (doseq [entry @audit-log]
      (println entry))))
```
Isso segue o princípio da **imutabilidade**, onde cada modificação é registrada sem alterar estados anteriores.

### Testes Automatizados
O código inclui testes automatizados para verificar a funcionalidade das reservas e do log de auditoria.
```clojure
(deftest test-process-reservation
  (testing "Reservation processing"
    (reset! reservations {})
    (reset! audit-log [])
    (is (= {:status "success" :message "Reservation confirmed"}
           (process-reservation {:id 1 :user "Alice" :time "10:00"})))
    (is (= {:status "error" :message "Datashow already reserved"}
           (process-reservation {:id 1 :user "Bob" :time "11:00"})))
    (is (= 1 (count @audit-log)))))
```
Isso mostra o suporte de Clojure a **testes automatizados** para garantir a confiabilidade do sistema.

## Paradigma Funcional em Clojure
Clojure enfatiza:
- **Imutabilidade:** O estado é atualizado com operações atômicas (`swap!`), garantindo consistência e evitando problemas de concorrência.
- **Funções Puras:** As funções são previsíveis e não alteram estados externos diretamente.
- **Concorrência Segura:** `atom` permite atualizações controladas sem risco de condições de corrida.
- **Programação Declarativa:** Em vez de modificar diretamente um estado, descrevemos como ele deve ser transformado.

## Exemplos de Uso
### Criando uma Reserva
```clojure
(process-reservation {:id 1 :user "Alice" :time "10:00"})
```
**Saída esperada:**
```
{:status "success" :message "Reservation confirmed"}
```

### Tentativa de Reservar um Datashow Já Ocupado
```clojure
(process-reservation {:id 1 :user "Bob" :time "11:00"})
```
**Saída esperada:**
```
{:status "error" :message "Datashow already reserved"}
```

### Listando Reservas
```clojure
(list-reservations)
```
**Saída esperada:**
```
ID: 1 | Usuário: Alice | Horário: 10:00
```

### Exibindo o Log de Auditoria
```clojure
(show-audit-log)
```
**Saída esperada:**
```
{:action "reserve" :id 1 :user "Alice" :time "10:00" :timestamp 2024-02-04T15:30:00}
```


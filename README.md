# Sistema de Reservas de Datashow (Clojure)

## Introdução
Este projeto implementa um **sistema funcional de gerenciamento de reservas de datashow**, garantindo que **não haja conflitos de horários** e mantendo uma abordagem funcional baseada no paradigma da linguagem Clojure.

## Estrutura do Código

### Banco de Dados e Estado
O sistema utiliza um **atom** para armazenar as reservas:
```clojure
(def reservations (atom {}))
```
Em Clojure, **atoms** são utilizados para gerenciar estados mutáveis de forma segura, garantindo consistência mesmo em cenários concorrentes.

### Manipulação de Horários
Para facilitar o gerenciamento de horários, os tempos são convertidos para **minutos desde a meia-noite**:
```clojure
(defn parse-time [time]
  (let [[h m] (map #(Integer/parseInt %) (clojure.string/split time #":"))]
    (+ (* h 60) m)))
```
Essa abordagem permite comparações numéricas diretas, simplificando a detecção de conflitos de horário.

### Verificação de Conflitos
O código impede a sobreposição de horários utilizando a função `conflicting?`:
```clojure
(defn conflicting? [existing start end]
  (let [{existing-start :start-time existing-end :end-time} existing
        s-min (parse-time start)
        e-min (parse-time end)
        ex-s-min (parse-time existing-start)
        ex-e-min (parse-time existing-end)]
    (not (or (<= e-min ex-s-min) (>= s-min ex-e-min)))))
```
Esse método garante que nenhuma reserva possa ser adicionada se houver sobreposição de horários com uma existente.

### Adicionando Reservas
A função `add-reservation` gerencia a criação de novas reservas, verificando conflitos e tratando exceções:
```clojure
(defn add-reservation [id start end]
  (try
    (if (or (contains? @reservations id)
            (some #(conflicting? % start end) (vals @reservations)))
      (throw (Exception. "Conflito de reserva ou ID já existente."))
      (do
        (swap! reservations assoc id {:start-time start :end-time end})
        (println "Reserva adicionada com sucesso!")))
    (catch Exception e
      (println "Erro ao adicionar reserva:" (.getMessage e)))))
```
Essa implementação exemplifica o uso do **swap!**, uma operação atômica para atualização segura do estado.

### Removendo Reservas
A remoção de reservas também é tratada com exceções, garantindo robustez ao sistema:
```clojure
(defn remove-reservation [id]
  (try
    (if (contains? @reservations id)
      (do
        (swap! reservations dissoc id)
        (println "Reserva removida com sucesso!"))
      (throw (Exception. "Reserva não encontrada.")))
    (catch Exception e
      (println "Erro ao remover reserva:" (.getMessage e)))))
```

### Listando Reservas
Para exibir todas as reservas ativas, a função `list-reservations` itera sobre o **atom** e exibe os horários cadastrados:
```clojure
(defn list-reservations []
  (if (empty? @reservations)
    (println "Nenhuma reserva cadastrada.")
    (doseq [[id {s :start-time e :end-time}] @reservations]
      (println (str "ID: " id " | Início: " s " | Fim: " e)))))
```
Essa abordagem é **imutável**, característica essencial da programação funcional.

## Paradigma Funcional em Clojure
Clojure é uma linguagem funcional baseada em Lisp que enfatiza **imutabilidade, funções puras e tratamento declarativo de dados**. Este projeto reflete esses princípios ao:

- Utilizar **funções puras**, que não alteram estado externo, para manipulação de horários e verificação de conflitos.
- Utilizar **estruturas de dados imutáveis**, garantindo previsibilidade e segurança na manipulação de estados.
- Gerenciar estado mutável de maneira controlada usando **atoms**, que garantem atualizações seguras e consistentes.
- Priorizar **programação declarativa**, onde se descreve o que deve ser feito, em vez de como fazê-lo passo a passo.

## Exemplos de Uso
### Adicionar uma reserva
```clojure
(add-reservation 1 "10:00" "11:00")
```
**Saída esperada:**
```
Reserva adicionada com sucesso!
```

### Tentativa de adicionar um horário conflitante
```clojure
(add-reservation 2 "10:30" "11:30")
```
**Saída esperada:**
```
Erro ao adicionar reserva: Conflito de reserva ou ID já existente.
```

### Remover uma reserva
```clojure
(remove-reservation 1)
```
**Saída esperada:**
```
Reserva removida com sucesso!
```

### Listar todas as reservas
```clojure
(list-reservations)
```
**Saída esperada:**
```
ID: 2 | Início: 11:00 | Fim: 12:00
```


(ns datashow-reservas.core
  (:require [clojure.string :as str]))

;; Estrutura inicial com a lista de datashows
(def datashows
  (atom [{:id 1 :status :disponivel}
         {:id 2 :status :disponivel}
         {:id 3 :status :alocado}
         {:id 4 :status :disponivel}]))

;; Banco de dados de reservas
(def reservas (atom {}))

;; Função para consultar o status de um datashow
(defn consultar [id]
  (some #(when (= (:id %) id) (:status %)) @datashows))

;; Função para atualizar o status de um datashow
(defn atualizar-status [id novo-status]
  (swap! datashows #(mapv (fn [ds] (if (= (:id ds) id) (assoc ds :status novo-status) ds)) %)))

;; Função para alocar um datashow
(defn alocar [id]
  (if-let [ds (some #(when (= (:id %) id) %) @datashows)]
    (if (= (:status ds) :disponivel)
      (do (atualizar-status id :alocado)
          (println "Datashow alocado com sucesso!"))
      (println "Datashow já está alocado."))
    (println "Datashow não encontrado.")))

;; Função para desalocar um datashow
(defn desalocar [id]
  (if-let [ds (some #(when (= (:id %) id) %) @datashows)]
    (if (= (:status ds) :alocado)
      (do (atualizar-status id :disponivel)
          (println "Datashow desalocado com sucesso!"))
      (println "Datashow não estava alocado."))
    (println "Datashow não encontrado.")))

;; Converte horário (HH:MM) para minutos desde a meia-noite
(defn converte-horario [time]
  (let [[h m] (map #(Integer/parseInt %) (str/split time #":"))]
    (+ (* h 60) m)))

;; Verifica se há conflito de horários
(defn conflito? [existing start end]
  (let [{existing-start :start-time existing-end :end-time} existing
        s-min (converte-horario start)
        e-min (converte-horario end)
        ex-s-min (converte-horario existing-start)
        ex-e-min (converte-horario existing-end)]
    (not (or (<= e-min ex-s-min) (>= s-min ex-e-min)))))

;; Adiciona uma nova reserva, se possível
(defn adicionar-reserva [id start end]
  (if (not (some #(= (:id %) id) @datashows))
    (println "Erro: Datashow não existe.")
    (if (or (contains? @reservas id)
            (some #(conflito? % start end) (vals @reservas)))
      (println "Conflito de reserva ou ID já existente.")
      (do
        (swap! reservas assoc id {:start-time start :end-time end})
        (println "Reserva adicionada com sucesso!")))))

;; Remove uma reserva, se existir
(defn remove-reserva [id]
  (if (contains? @reservas id)
    (do
      (swap! reservas dissoc id)
      (println "Reserva removida com sucesso!"))
    (println "Reserva não encontrada.")))

;; Lista todas as reservas ativas
(defn lista-reservas []
  (if (empty? @reservas)
    (println "Nenhuma reserva cadastrada.")
    (doseq [[id {s :start-time e :end-time}] @reservas]
      (println (str "ID: " id " | Início: " s " | Fim: " e)))))

;; Lista todos os datashows
(defn exibir-datashows [] 
  (doseq [ds @datashows]
    (println "ID:" (:id ds) "Status:" (:status ds))))

;; Exemplos de uso
(defn -main []
  ;; Exibir os datashows iniciais
  (println "Estado inicial dos datashows:")
  (exibir-datashows) ;; Exibe a lista de datashows
  (println)

  ;; Consultar status
  (println "Consultando status do datashow 1:" (consultar 1))
  (println "Consultando status do datashow 3:" (consultar 3))
  (println)

  ;; Alocar um datashow
  (println "Alocando datashow 1:")
  (alocar 1) ;; Não é necessário reatribuir, pois a função já altera o estado global
  (exibir-datashows)
  (println)

  ;; Tentar alocar um datashow já alocado
  (println "Tentando alocar datashow 3:")
  (alocar 3) ;; Não é necessário reatribuir, pois a função já altera o estado global
  (exibir-datashows)
  (println)

  ;; Desalocar um datashow
  (println "Desalocando datashow 3:")
  (desalocar 3) ;; Não é necessário reatribuir, pois a função já altera o estado global
  (exibir-datashows)
  (println)

  ;; Adicionar uma reserva para o datashow 1
  (println "Adicionando reserva para o datashow 1:")
  (adicionar-reserva 1 "10:00" "12:00") ;; Tenta adicionar reserva para o datashow
  (lista-reservas) ;; Lista as reservas atuais
  (println)

  ;; Tentar adicionar uma reserva com conflito de horário
  (println "Tentando adicionar uma reserva com conflito para o datashow 1:")
  (adicionar-reserva 1 "11:00" "13:00") ;; Tenta adicionar reserva com conflito
  (lista-reservas) ;; Lista as reservas atuais
  (println)

  ;; Remover uma reserva
  (println "Removendo reserva do datashow 1:")
  (remove-reserva 1) ;; Remove a reserva do datashow
  (lista-reservas) ;; Lista as reservas atuais
  (println)

  ;; Exibir estado final dos datashows
  (println "Estado final dos datashows:")
  (exibir-datashows))

;; Executar o código principal
(-main)
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

;; Lista todos os datashows
(defn exibir-datashows [] 
  (doseq [ds @datashows]
    (println "ID:" (:id ds) "Status:" (:status ds))))

;; Exemplos de uso
(defn -main []
  ;; Exibir os datashows iniciais
  (println "Estado inicial dos datashows:")
  (exibir-datashows datashows)
  (println)

  ;; Consultar status
  (println "Consultando status do datashow 1:" (consultar datashows 1))
  (println "Consultando status do datashow 3:" (consultar datashows 3))
  (println)

  ;; Alocar um datashow
  (println "Alocando datashow 1:")
  (def datashows (alocar datashows 1)) ;; Reatribui a lista após a alocação
  (exibir-datashows datashows)
  (println)

  ;; Tentar alocar um datashow já alocado
  (println "Tentando alocar datashow 3:")
  (def datashows (alocar datashows 3)) ;; Reatribui após tentar alocar
  (exibir-datashows datashows)
  (println)

  ;; Desalocar um datashow
  (println "Desalocando datashow 3:")
  (def datashows (desalocar datashows 3)) ;; Reatribui após desalocar
  (exibir-datashows datashows))

;; Executar o código principal
(-main)
